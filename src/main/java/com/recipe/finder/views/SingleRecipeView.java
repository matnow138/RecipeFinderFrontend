package com.recipe.finder.views;

import com.recipe.finder.domain.offer.Ingredient;
import com.recipe.finder.domain.offer.Recipes;
import com.recipe.finder.domain.offer.SingleRecipe;
import com.recipe.finder.external.RecipeDto;
import com.recipe.finder.external.RecipesService;
import com.recipe.finder.external.SingleRecipeDto;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Route("SingleRecipe")
@PageTitle("SingleRecipe")
public class SingleRecipeView extends VerticalLayout {
    private final transient RecipesService recipesService;
    private final Binder<Recipes> binder = new Binder<>(Recipes.class);
    private final Logger logger = LoggerFactory.getLogger(SingleRecipeView.class);
    Grid<SingleRecipe> recipeGrid = new Grid<>(SingleRecipe.class);
    Grid<Ingredient> ingredientGrid = new Grid<>(Ingredient.class);
    TextArea textArea = new TextArea();
    private transient Recipes recipes;

    public SingleRecipeView(RecipesService recipesService) throws Exception {
        this.recipesService = recipesService;
        logger.info("constructor recipe {}", recipes);
        ingredientGrid.addColumn(Ingredient::name).setHeader("Name");
        ingredientGrid.addColumn(Ingredient::amount).setHeader("Amount");
        ingredientGrid.addColumn(Ingredient::unit).setHeader("Unit");
        Button goBack = new Button("Go back");
        goBack.addClickListener(event ->
                goBack.getUI()
                        .flatMap(ui -> ui.navigate(RecipesView.class)));
        add(goBack, ingredientGrid, textArea);

    }

    private void configureGrid() {
        recipeGrid.addClassName("Recipe-grid");
        recipeGrid.setSizeFull();
        recipeGrid.getColumns().forEach(col -> col.setWidth("150"));
    }

    public void setRecipeDto(Recipes recipes) throws Exception {
        logger.info("passed recipe {}", recipes);
        this.recipes = recipes;
        binder.readBean(recipes);

        updateGrid();


    }

    private void updateGrid() throws Exception {
        logger.info("updateGrid {}", recipes);
        SingleRecipeDto singleRecipeDto = recipesService.getRecipe(RecipeDto.fromDomain(recipes));
        ingredientGrid.setItems(singleRecipeDto.getIngredientsList());
        logger.info("ingredients: {}", singleRecipeDto.getIngredientsList());
        textArea.setSizeFull();
        textArea.setValue(singleRecipeDto.toDomain().instructions());

    }


}
