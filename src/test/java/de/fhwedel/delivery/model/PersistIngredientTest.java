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
        persistStaticIngredients();
    }

    private void persistStaticIngredients() {
        txManager.addIngredient(Ingredient.CHEESE);
        txManager.addIngredient(Ingredient.SALAMI);
        txManager.addIngredient(Ingredient.TOMATO_SAUCE);
    }

    @After
    public void tearDown() throws Exception {
        txManager.dispose();
    }

    @Test
    public void printStaticValuesTest() throws Exception {
        txManager.printDB();
    }
}
