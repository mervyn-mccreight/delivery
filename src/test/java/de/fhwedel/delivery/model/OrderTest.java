package de.fhwedel.delivery.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderTest {

    @Test
    public void nonEquality() throws Exception {
        Order given = Order.empty();
        Object anotherObject = new Object();
        assertThat(given).isNotEqualTo(anotherObject);

        Order anotherOrder = Order.empty().addProducts(Pizza.empty());
        assertThat(given).isNotEqualTo(anotherOrder);
    }

    @Test
    public void simpleEquality() throws Exception {
        Order given = Order.empty();
        Order equal = Order.empty();

        assertThat(given).isEqualTo(equal);
        assertThat(given.hashCode()).isEqualTo(equal.hashCode());
    }

    @Test
    public void complexEquality() throws Exception {
        Order given = Order.empty().addProducts(Pizza.empty().addIngredients(Ingredient.CHEESE));
        Order equal = Order.empty().addProducts(Pizza.empty().addIngredients(Ingredient.CHEESE));

        assertThat(given).isEqualTo(equal);
        assertThat(given.hashCode()).isEqualTo(equal.hashCode());
    }

    @Test
    public void emptyCost() throws Exception {
        Order empty = Order.empty();

        assertThat(empty.evaluateCost()).isZero();
    }

    @Test
    public void onePizzaCost() throws Exception {
        Pizza pizza = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);

        Order order = Order.empty();
        Order result;
        order.addProducts(pizza);

        assertThat(order.evaluateCost()).isEqualTo(pizza.evaluateCost());
    }

    @Test
    public void multiplePizzasCost() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.SALAMI);

        Order order = Order.empty();
        Order result1;
        order.addProducts(pizza1);
        Order result;
        order.addProducts(pizza2);

        assertThat(order.evaluateCost()).isEqualTo(pizza1.evaluateCost().add(pizza2.evaluateCost()));
    }
}
