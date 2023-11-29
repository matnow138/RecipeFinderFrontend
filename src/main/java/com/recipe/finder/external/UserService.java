package com.recipe.finder.external;

import com.recipe.finder.domain.offer.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserClient userClient;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserService(@Autowired UserClient userClient){
        this.userClient=userClient;
    }

    public void createUser(User user){
        logger.info("userService user {}",user);
        if(user.id()==null){
            storeInDatabase(user);
        }else{
            updateInDatabase(user);
        }
    }

    public void authorizeUser(String name, String password){
        logger.info("authorizing user: {} / {}", name, password);
        authorize(name,password);
    }

    private void storeInDatabase(User user){
        userClient.createUser(UserDto.fromDomain(user));
    }

    private void updateInDatabase(User user){
        userClient.createUser(UserDto.fromDomain(user));
    }

    private void authorize(String name, String password){ userClient.authorizeUser(name, password);}

    public boolean checkIfCookiePresent(String name){
        return userClient.getCookieByName(name).getValue() != null;
    }

}
