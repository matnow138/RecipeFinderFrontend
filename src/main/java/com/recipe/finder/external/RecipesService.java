package com.recipe.finder.external;

import com.recipe.finder.domain.offer.Recipes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecipesService {

    private final RecipesClient recipesClient;

    public RecipesService(@Autowired RecipesClient recipesClient){
        this.recipesClient = recipesClient;
    }

    public List<Recipes> getRecipes(){
        return recipesClient.getRecipes();
    }

    public SingleRecipeDto getRecipe(RecipeDto recipeDto) throws IOException, InterruptedException {
        return recipesClient.getSingleRecipe(recipeDto.getId());
    }
}
