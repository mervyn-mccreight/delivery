package de.fhwedel.delivery.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseTest {

    @Test
    public void nonEquality() throws Exception {
        Purchase given = Purchase.empty();
        Object anotherObject = new Object();
        assertThat(given).isNotEqualTo(anotherObject);

        Purchase anotherPurchase = Purchase.empty().addProducts(Pizza.empty());
        assertThat(given).isNotEqualTo(anotherPurchase);
    }

    @Test
    public void simpleEquality() throws Exception {
        Purchase given = Purchase.empty();
        Purchase equal = Purchase.empty();

        assertThat(given).isEqualTo(equal);
        assertThat(given.hashCode()).isEqualTo(equal.hashCode());
    }

    @Test
    public void complexEquality() throws Exception {
        Purchase given = Purchase.empty().addProducts(Pizza.empty().addIngredients(Ingredient.CHEESE));
        Purchase equal = Purchase.empty().addProducts(Pizza.empty().addIngredients(Ingredient.CHEESE));

        assertThat(given).isEqualTo(equal);
        assertThat(given.hashCode()).isEqualTo(equal.hashCode());
    }

    @Test
    public void emptyCost() throws Exception {
        Purchase empty = Purchase.empty();

        assertThat(empty.evaluateCost()).isZero();
    }

    @Test
    public void onePizzaCost() throws Exception {
        Pizza pizza = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);

        Purchase purchase = Purchase.empty();
        Purchase result;
        purchase.addProducts(pizza);

        assertThat(purchase.evaluateCost()).isEqualTo(pizza.evaluateCost());
    }

    @Test
    public void multiplePizzasCost() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.SALAMI);

        Purchase purchase = Purchase.empty();
        Purchase result1;
        purchase.addProducts(pizza1);
        Purchase result;
        purchase.addProducts(pizza2);

        assertThat(purchase.evaluateCost()).isEqualTo(pizza1.evaluateCost().add(pizza2.evaluateCost()));
    }
}
