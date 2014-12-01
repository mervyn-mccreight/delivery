package de.fhwedel.delivery.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class IngredientTest {

    private Ingredient zucker;

    @Before
    public void setUp() throws Exception {
        zucker = new Ingredient(0l, "Zucker", new BigDecimal("1337"));
    }

    @Test
    public void equality() throws Exception {
        Ingredient equal = new Ingredient(0l, "Zucker", new BigDecimal("1337"));

        assertThat(zucker).isEqualTo(equal);
    }

    @Test
    public void notEquality() throws Exception {
        Ingredient diffId = new Ingredient(1l, "Zucker", new BigDecimal("1337"));
        Ingredient diffName = new Ingredient(0l, "Salz", new BigDecimal("1337"));
        Ingredient diffCost = new Ingredient(1l, "Zucker", new BigDecimal("42"));

        assertThat(zucker).isNotEqualTo(diffId);
        assertThat(zucker).isNotEqualTo(diffName);
        assertThat(zucker).isNotEqualTo(diffCost);
    }

    @Test
    public void cost() throws Exception {
        Ingredient ingredient = new Ingredient(0l, "Zucker", new BigDecimal("5.00"));

        assertThat(ingredient.getCost()).isEqualTo("5.00");
    }
}
