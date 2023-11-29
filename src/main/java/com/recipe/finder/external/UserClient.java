package com.recipe.finder.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class UserClient {
    private final Logger logger = LoggerFactory.getLogger(UserClient.class);

    private final ObjectMapper objectMapper;

    private final ObjectReader arrayReader;




    public UserClient(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.arrayReader = objectMapper.readerForArrayOf(UserDto.class);
    }
    @Value("${offers.client.url}")
    private String finderUrl;

    public void createUser(UserDto userDto){
        send(
                UriComponentsBuilder.fromHttpUrl(finderUrl).path("v1/auth/addNewUser"),
                "POST",
                jsonBodyPublisher(userDto),
                HttpResponse.BodyHandlers.discarding()
        );
        logger.info("passed user: {}",userDto.getPassword());
    }

    public void authorizeUser(String name, String password){
        AuthRequest authRequest = new AuthRequest(name,password);
        Cookie cookie = new Cookie("Finder-token", send(
                UriComponentsBuilder.fromHttpUrl(finderUrl).path("v1/auth/generateToken"),
                "POST",
                jsonBodyPublisher(authRequest),
                HttpResponse.BodyHandlers.ofString()
        ));
        cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
        VaadinService.getCurrentResponse().addCookie(cookie);


        logger.info("authorized user: {} / {}",name, password);
    }

    private HttpRequest.BodyPublisher jsonBodyPublisher(Object dto){
        logger.info("mapped {}",dto.toString());
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
            logger.info("passed user {} , Sending request to {} , ended with {}",bodyPublisher.toString(), uri, responseBody);


            return responseBody;
        } catch(Exception e){
            throw new RestClientException("error performing request", e);
        }
    }
    private UriComponentsBuilder baseURL(){
        return UriComponentsBuilder.fromHttpUrl(finderUrl).path("v1/auth");
    }

    public Cookie getCookieByName(String name){
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        for(Cookie cookie:cookies){
            if(name.equals(cookie.getName())){
                return cookie;
            }
        }
        return null;
    }


}
