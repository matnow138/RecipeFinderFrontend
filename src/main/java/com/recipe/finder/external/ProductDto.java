package com.recipe.finder.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.recipe.finder.domain.offer.Product;

public class ProductDto {

    public ProductDto() {
    }

    public ProductDto(Long id, String name, double quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("quantity")
    private double quantity;
    @JsonProperty("unit")
    private String unit;

    public Product toDomain(){
        return new Product(
                id,
                name,
                quantity,
                unit
        );
    }
    public static ProductDto fromDomain(Product product){
        return new ProductDto(
                product.id(),
                product.name(),
                product.quantity(),
                product.unit()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
