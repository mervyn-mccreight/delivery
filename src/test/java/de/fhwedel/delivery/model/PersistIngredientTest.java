package de.fhwedel.delivery.model;

import de.fhwedel.delivery.transaction.TxManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PersistIngredientTest {

    private TxManager txManager;

    @Before
    public void setUp() throws Exception {
        txManager = new TxManager();
    }


    @After
    public void tearDown() throws Exception {
        txManager.dispose();
    }

    @Test
    public void addPizza_pizzaAndIngredientsArePersisted() throws Exception {

        Pizza pizza1 = Pizza.empty().addIngredient(Ingredient.TOMATO_SAUCE).addIngredient(Ingredient.CHEESE).addIngredient(Ingredient.SALAMI);
        txManager.addPizza(pizza1);

        Pizza pizza2 = Pizza.empty().addIngredient(Ingredient.CHEESE).addIngredient(Ingredient.SALAMI);
        txManager.addPizza(pizza2);

        txManager.printTableOfEntityClass(Pizza.class);
        txManager.printTableOfEntityClass(Ingredient.class);

        // TODO: Assert database contents
    }

    @Test
    public void removePizza_pizzaIsRemovedIngredientsArePersisted() {
        Pizza pizza1 = Pizza.empty().addIngredient(Ingredient.TOMATO_SAUCE).addIngredient(Ingredient.CHEESE).addIngredient(Ingredient.SALAMI);
        txManager.addPizza(pizza1);

        Pizza pizza2 = Pizza.empty().addIngredient(Ingredient.CHEESE).addIngredient(Ingredient.SALAMI);
        txManager.addPizza(pizza2);

        txManager.removePizza(pizza1);

        txManager.printTableOfEntityClass(Pizza.class);
        txManager.printTableOfEntityClass(Ingredient.class);
    }
}
