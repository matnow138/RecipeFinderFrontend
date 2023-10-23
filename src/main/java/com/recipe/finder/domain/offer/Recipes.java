package com.recipe.finder.domain.offer;

public record Recipes(
        Long id,
        String title,
        int usedIngredientCount,
        int missedIngredientCount
) {

}
