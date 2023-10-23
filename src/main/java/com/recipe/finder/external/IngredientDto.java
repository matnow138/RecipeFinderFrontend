package com.recipe.finder.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.finder.domain.offer.Ingredient;

public class IngredientDto {

    @JsonProperty("name")
    private String name;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("amount")
    private double amount;

    public IngredientDto() {
    }

    public IngredientDto(String name, String unit, double amount) {
        this.name = name;
        this.unit = unit;
        this.amount = amount;
    }

    public Ingredient toDomain(){
        return new Ingredient(
                name,
                unit,
                amount
        );
    }


    public static IngredientDto fromDomain(Ingredient ingredient){
        return new IngredientDto(
                ingredient.name(),
                ingredient.unit(),
                ingredient.amount()
        );
    }

    public String getName() {
        return name;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
