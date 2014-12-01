package de.fhwedel.delivery.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    // TODO: test equals, etc

    @Test
    public void emptyCost() throws Exception {
        Order empty = Order.empty();

        assertThat(empty.getCost()).isZero();
    }

    @Test
    public void onePizzaCost() throws Exception {
        Pizza pizza = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);

        Order order = Order.empty();
        order.addProduct(pizza);

        assertThat(order.getCost()).isEqualTo(pizza.getCost());
    }

    @Test
    public void multiplePizzasCost() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.SALAMI);

        Order order = Order.empty();
        order.addProduct(pizza1);
        order.addProduct(pizza2);

        assertThat(order.getCost()).isEqualTo(pizza1.getCost().add(pizza2.getCost()));
    }
}
