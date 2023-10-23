package com.recipe.finder.external;

import com.recipe.finder.domain.offer.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService {
    private final ProductClient productClient;

    public ProductService(@Autowired ProductClient productClient){
        this.productClient=productClient;
    }

    public void addOrUpdateProduct(Product product){
        if(product.id()==null){
            storeInDatabase(product);

        }else{
            updateInDatabase(product);
        }
    }
    private void storeInDatabase(Product product){
        productClient.addProduct(ProductDto.fromDomain(product));
    }

    private void updateInDatabase(Product product){
        productClient.addProduct(ProductDto.fromDomain(product));
    }

    public List<Product> getProducts(){
        return productClient.getProducts();
    }

    public void deleteProduct(ProductDto productDto){
        productClient.deleteProduct(productDto);

    }
}
