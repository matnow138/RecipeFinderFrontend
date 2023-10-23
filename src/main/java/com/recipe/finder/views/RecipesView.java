package com.recipe.finder.views;

import com.recipe.finder.domain.offer.Recipes;
import com.recipe.finder.external.RecipesService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("recipes")
public class RecipesView extends VerticalLayout {
    private final Logger logger = LoggerFactory.getLogger(RecipesView.class);


    Grid<Recipes> recipeGrid = new Grid<>(Recipes.class);

    @Autowired
    private final RecipesService recipesService;

    public RecipesView(RecipesService recipesService) throws Exception {
        this.recipesService = recipesService;
        setSizeFull();
        configureGrid();
        updateGrid();
        Button goBack = new Button("Go back");
        goBack.addClickListener(event->
                goBack.getUI()
                        .flatMap(ui -> ui.navigate(Frontend.class)));
        add(goBack, recipeGrid);

    }

    private void configureGrid() throws Exception {
        recipeGrid.addClassName("Recipes");
        recipeGrid.addColumn(com.recipe.finder.domain.offer.Recipes::title).setHeader("Recipe Name");
        recipeGrid.addColumn(com.recipe.finder.domain.offer.Recipes::usedIngredientCount).setHeader("Used ingredients count");
        recipeGrid.addColumn(com.recipe.finder.domain.offer.Recipes::missedIngredientCount).setHeader("Missed ingredients count");
        updateGrid();
        recipeGrid.addComponentColumn(recipes -> {
            Button findRecipeButton = new Button("Find Recipe");
            findRecipeButton.addClickListener(event -> {
                logger.info("sent recipe {}", recipes);
                findRecipeButton.getUI()
                        .flatMap(ui -> ui.navigate(SingleRecipeView.class))
                        .ifPresent(editor -> {
                            try {
                                editor.setRecipeDto(recipes);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
            });
            return findRecipeButton;
        }).setWidth("150px").setFlexGrow(0);
        recipeGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateGrid() throws Exception{
        List<com.recipe.finder.domain.offer.Recipes> recipesList = recipesService.getRecipes();
        logger.info("Setting items {}", recipesList);
        recipeGrid.setItems(recipesList);
    }



}
