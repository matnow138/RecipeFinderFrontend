package com.recipe.finder.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.finder.domain.offer.User;

public class UserDto {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("roles")
    private String roles;

    public UserDto(Integer id, String name, String email, String password, String roles) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = "ROLE_USER";
    }

    public UserDto(){

    }

    public User toDomain(){
        return new User(
                id,
                name,
                email,
                password,
                roles
        );
    }

    public static UserDto fromDomain(User user){
        return new UserDto(
                user.id(),
                user.name(),
                user.email(),
                user.password(),
                user.role()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
