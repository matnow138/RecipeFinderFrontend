package com.recipe.finder.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.finder.domain.offer.Ingredient;
import com.recipe.finder.domain.offer.SingleRecipe;
import lombok.Getter;

import java.util.List;

public class SingleRecipeDto {

    @JsonProperty("title")
    private String title;
    @Getter
    @JsonProperty("extendedIngredients")
    private List<IngredientDto> extendedIngredients;
    @JsonProperty("instructions")
    private String instructions;

    public SingleRecipeDto(String title, List<IngredientDto> extendedIngredients, String instructions) {
        this.title = title;
        this.extendedIngredients = extendedIngredients;
        this.instructions = instructions;
    }

    public SingleRecipeDto(){

    }

    public SingleRecipe toDomain(){
        return new SingleRecipe(
                title,
                mapToIngredient(extendedIngredients),
                instructions
        );
    }

    public static SingleRecipeDto fromDomain(SingleRecipe singleRecipe){
        return new SingleRecipeDto(
                singleRecipe.title(),
                mapToIngredientDto(singleRecipe.extendedIngredients()),
                singleRecipe.instructions()

        );
    }

    private static List<Ingredient> mapToIngredient(List<IngredientDto> extendedIngredients){
        return extendedIngredients.stream()
                .map(IngredientDto::toDomain)
                .toList();
    }
    private static List<IngredientDto> mapToIngredientDto(List<Ingredient> extendedIngredients){
        return extendedIngredients.stream()
                .map(IngredientDto::fromDomain)
                .toList();
    }

    public List<Ingredient> getIngredientsList(){
        return mapToIngredient(extendedIngredients);
    }

}
