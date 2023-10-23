package com.recipe.finder.views;

import com.recipe.finder.domain.offer.Product;
import com.recipe.finder.external.ProductDto;
import com.recipe.finder.external.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("addItem")
public class AddItem extends FormLayout {

    private final ProductService productService;
    private transient ProductDto productDto = new ProductDto();

    @PropertyId("name")
    private final TextField name = new TextField("Name");

    @PropertyId("unit")
    private final TextField unit = new TextField("Unit");

    @PropertyId("quantity")
    private final TextField quantity = new TextField("Quantity");

    private final Button save = new Button("Save");
    private final Binder<ProductDto> binder = new Binder<>(ProductDto.class);
    private final Logger logger = LoggerFactory.getLogger(AddItem.class);


    public AddItem(ProductService productService) {
        this.productService = productService;

        HorizontalLayout fields = new HorizontalLayout(name,unit,quantity);
        HorizontalLayout buttons = new HorizontalLayout(save);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> save());

        binder.bindInstanceFields(this);
        add(fields,buttons);
    }

    private void save(){
        try {
            binder.writeBean(productDto);
            productService.addOrUpdateProduct(productDto.toDomain());
            save.getUI().flatMap(ui -> ui.navigate(Frontend.class));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public void setProductDto(Product product) throws Exception {
        logger.info("passed product {}", productDto);
        this.productDto = ProductDto.fromDomain(product);
        binder.readBean(productDto);
        productService.addOrUpdateProduct(productDto.toDomain());

    }

}
