package de.fhwedel.delivery.model;

import de.fhwedel.delivery.transaction.TxManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DbPersistenceTest {

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
    public void addTwoPizzas_pizzaAndIngredientsArePersisted() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(pizza1);

        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(pizza2);

        Set<Pizza> pizzas = txManager.getTableEntities(Pizza.class);

        assertThat(pizzas).hasSize(2);
        assertThat(pizzas).containsOnly(pizza1, pizza2);
    }

    @Test
    public void addTwoPizzas_ingredientsArePersisted() throws Exception {
        txManager.addPizza(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI));
        txManager.addPizza(Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI));

        Set<Ingredient> ingredients = txManager.getTableEntities(Ingredient.class);

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).containsOnly(Ingredient.CHEESE, Ingredient.SALAMI, Ingredient.TOMATO_SAUCE);
    }

    @Test
    public void removePizza_pizzaIsRemovedIngredientsArePersisted() {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(pizza1);

        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(pizza2);

        txManager.removePizza(pizza1);

        Set<Pizza> pizzas = txManager.getTableEntities(Pizza.class);
        Set<Ingredient> ingredients = txManager.getTableEntities(Ingredient.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(pizza2);

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).containsOnly(Ingredient.TOMATO_SAUCE, Ingredient.SALAMI, Ingredient.CHEESE);
    }

    @Test
    public void persistOrder() throws Exception {
        Pizza productToOrder = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE);
        Order order = Order.empty().addProduct(productToOrder);

        txManager.addOrder(order);

        Set<Order> orders = txManager.getTableEntities(Order.class);

        assertThat(orders).hasSize(1);
        assertThat(orders).containsOnly(order);

        Set<Pizza> pizzas = txManager.getTableEntities(Pizza.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(productToOrder);
    }
}
