package com.recipe.finder.domain.offer;

import java.util.List;

public record SingleRecipe(
    String title,
    List<Ingredient> extendedIngredients,
    String instructions
) {
}
