package com.recipe.finder.external;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.recipe.finder.domain.offer.Product;
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
public class ProductClient {
    private final Logger logger = LoggerFactory.getLogger(ProductClient.class);

    private final ObjectMapper objectMapper;

    private final ObjectReader arrayReader;

    public ProductClient(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.arrayReader = objectMapper.readerForArrayOf(ProductDto.class);
    }

    @Value("${offers.client.url}")
    private String finderUrl;

    public List<Product> getProducts(){
        try{
            ProductDto[] productArrayNode = getProductDtos();
            return Arrays.stream(productArrayNode).map(ProductDto::toDomain).toList();
        } catch(Exception e){
            logger.error("Cannot read products", e);
            return Collections.emptyList();
        }
    }
    private ProductDto[] getProductDtos() throws IOException, InterruptedException {
        String body =
                HttpClient.newHttpClient()
                        .send(createRequestForGetProductDtos(), HttpResponse.BodyHandlers.ofString()).body();
        logger.info("response: {}", body);
        return arrayReader.readValue(body, ProductDto[].class);
    }
    private HttpRequest createRequestForGetProductDtos(){
        try {
            return HttpRequest.newBuilder()
                    .uri(new URIBuilder(finderUrl).setPath("/v1/product").build())
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public void addProduct(ProductDto productDto){
        send(
                baseURL(),
                "POST",
                jsonBodyPublisher(productDto),
                HttpResponse.BodyHandlers.discarding()
        );

    }
    public void deleteProduct(ProductDto productDto){
        send(
                baseURL(),
                "DELETE",
                jsonBodyPublisher(productDto),
                HttpResponse.BodyHandlers.discarding()
        );

    }

    private HttpRequest.BodyPublisher jsonBodyPublisher(Object dto){
        try{
            return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(dto));
        } catch (JsonProcessingException e){
            throw new RestClientException("error performing json conversion of " +dto, e);
        }
    }
    private Void send(UriComponentsBuilder uriComponentsBuilder, String method){
        return send(uriComponentsBuilder, method, HttpRequest.BodyPublishers.noBody(), HttpResponse.BodyHandlers.discarding());
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
        return UriComponentsBuilder.fromHttpUrl(finderUrl).path("v1/product");
    }
}
