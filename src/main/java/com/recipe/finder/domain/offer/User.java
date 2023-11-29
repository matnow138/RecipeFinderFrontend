package com.recipe.finder.domain.offer;

public record User(
        Integer id,
        String name,
        String email,
        String password,
        String role
) {
}
