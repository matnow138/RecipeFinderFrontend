package com.recipe.finder.views;

import com.recipe.finder.domain.offer.Product;
import com.recipe.finder.external.ProductDto;
import com.recipe.finder.external.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Route("Storage")
@PageTitle("Main Page")
public class Frontend extends VerticalLayout {
    Grid<Product> productGrid = new Grid<>(Product.class, false);

    private final ProductService productService;


    TextField filterText = new TextField();
    private final Logger logger = LoggerFactory.getLogger(Frontend.class);



    public Frontend(ProductService productService){
        this.productService = productService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        updateGrid();
        add(getToolbar(),productGrid);
    }

    private void configureGrid() {
        productGrid.addClassName("product-grid");
        productGrid.setSizeFull();
        productGrid.addColumn(Product::name).setHeader("Name");
        productGrid.addColumn(Product::quantity).setHeader("Quantity");
        productGrid.addColumn(Product::unit).setHeader("Unit");
        productGrid.addComponentColumn(edit -> {
            Button editProduct = new Button("Edit product");
            editProduct.addClickListener(event -> {
                logger.info("sent recipe {}", edit);
                editProduct.getUI()
                                .flatMap(ui -> ui.navigate(AddItem.class))
                                .ifPresent(editor -> {
                                    try {
                                        editor.setProductDto(edit);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                updateGrid();
            });

            return editProduct;
        }).setWidth("150px").setFlexGrow(0);
        productGrid.addComponentColumn(remove -> {
            Button removeProduct = new Button("Remove product");
            removeProduct.addClickListener(event -> {
                logger.info("removed recipe {}", remove);
                    productService.deleteProduct(ProductDto.fromDomain(remove));
                updateGrid();
            });

            return removeProduct;
        }).setWidth("150px").setFlexGrow(0);
        productGrid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);



        Button addContactButton = new Button("Add product");
        addContactButton.addClickListener(event ->addContactButton.getUI()
                .flatMap(ui -> ui.navigate(AddItem.class)));
        Button findRecipeButton = new Button("Find recipes");
        findRecipeButton.addClickListener(event -> findRecipeButton.getUI()
                .flatMap(ui -> ui.navigate(RecipesView.class))
        );
        var toolbar = new HorizontalLayout(filterText, addContactButton,findRecipeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void updateGrid(){
        List<Product> productList = productService.getProducts();
        GridListDataView<Product> dataView = productGrid.setItems(productList);
        filterText.addValueChangeListener(e -> dataView.refreshAll());
        dataView.addFilter(product -> {
            String searchTerm = filterText.getValue().trim();
            if (searchTerm.isEmpty())
                return true;

            return matches(product.name(),
                    searchTerm);
        });

        //productGrid.setItems(productList);
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty()
                || value.toLowerCase().contains(searchTerm.toLowerCase());
    }


}
