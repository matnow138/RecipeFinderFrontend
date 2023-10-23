package com.recipe.finder.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.recipe.finder.domain.offer.Recipes;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class RecipesClient {

    private final Logger logger = LoggerFactory.getLogger(RecipesClient.class);

    private final ObjectMapper objectMapper;
    private final ObjectReader arrayReader;
    @Value("${offers.client.url}")
    private String finderUrl;


    public RecipesClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.arrayReader = objectMapper.readerForArrayOf(RecipeDto.class);
    }

    public List<Recipes> getRecipes(){
        try{
            RecipeDto[] recipeArrayNode = getRecipesDtos();
            return Arrays.stream(recipeArrayNode).map(RecipeDto::toDomain).toList();
        }catch (Exception e){
            logger.error("Cannot read products", e);
            return Collections.emptyList();
        }
    }

    private RecipeDto[] getRecipesDtos() throws IOException, InterruptedException {
        String body = HttpClient.newHttpClient()
                .send(createRequestForGetRecipesDto(), HttpResponse.BodyHandlers.ofString()).body();
        logger.info("response: {}", body);
        return arrayReader.readValue(body, RecipeDto[].class);
    }

    private HttpRequest createRequestForGetRecipesDto(){
        try{
            return HttpRequest.newBuilder()
                    .uri(new URIBuilder(finderUrl).setPath("/v1/recipe").build())
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    public SingleRecipeDto getSingleRecipe(long id) throws IOException, InterruptedException {
        String body=
                HttpClient.newHttpClient()
                        .send(createRequestForSingleRecipeDto(id), HttpResponse.BodyHandlers.ofString()).body();
        logger.info("response: {}", body);
        return arrayReader.readValue(body, SingleRecipeDto.class);

    }
    private HttpRequest createRequestForSingleRecipeDto(long id){
        try{
            return HttpRequest.newBuilder()
                    .uri(new URIBuilder(finderUrl).setPath("/v1/recipe/getRecipe")
                            .addParameter("recipeId",String.valueOf(id))
                            .build())
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    private HttpRequest.BodyPublisher jsonBodyPublisher(Object dto){
        try{
            return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(dto));
        }catch (JsonProcessingException e){
            throw new RestClientException("Error performing json conversion of " +dto, e);
        }
    }

    private <T> T send(UriComponentsBuilder uriComponentsBuilder, String method, HttpRequest.BodyPublisher bodyPublisher, HttpResponse.BodyHandler<T> bodyHandler){
        URI uri = uriComponentsBuilder.build().toUri();
        try{
            HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                    .method(method, bodyPublisher)
                    .header("Content-Type", "application/json")
                    .build();
            T responseBody = HttpClient.newHttpClient()
                    .send(httpRequest, bodyHandler)
                    .body();
            logger.info("Sending request to {} , ended with {}", uri, responseBody);
            return responseBody;
        } catch(Exception e){
            throw new RestClientException("error performing request", e);
        }
    }
    private UriComponentsBuilder baseURL(){
        return UriComponentsBuilder.fromHttpUrl(finderUrl).path("v1/recipe");
    }



}
