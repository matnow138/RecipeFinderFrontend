package com.recipe.finder.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.finder.domain.offer.Recipes;

public class RecipeDto {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("usedIngredientCount")
    private int usedIngredientCount;
    @JsonProperty("missedIngredientCount")
    private int missedIngredientCount;
    public RecipeDto(){

    }

    public RecipeDto(Long id, String title, int usedIngredientCount, int missedIngredientCount) {
        this.id = id;
        this.title = title;
        this.usedIngredientCount = usedIngredientCount;
        this.missedIngredientCount = missedIngredientCount;
    }

    public Recipes toDomain(){
        return new Recipes(
                id,
                title,
                usedIngredientCount,
                missedIngredientCount
        );
    }

    public static RecipeDto fromDomain(Recipes recipes){
        return new RecipeDto(
                recipes.id(),
                recipes.title(),
                recipes.usedIngredientCount(),
                recipes.missedIngredientCount()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setUsedIngredientCount(int usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }
}
