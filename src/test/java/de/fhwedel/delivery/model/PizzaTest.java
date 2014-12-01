package de.fhwedel.delivery.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PizzaTest {

    private Pizza empty;
    private Pizza salamiCheese;

    @Before
    public void setUp() throws Exception {
        empty = Pizza.empty();
        empty.setId(0l);

        salamiCheese = Pizza.empty().addIngredient(Ingredient.SALAMI).addIngredient(Ingredient.CHEESE);
        salamiCheese.setId(1l);
    }

    @Test
    public void equality() throws Exception {
        Pizza equalsEmpty = Pizza.empty();
        equalsEmpty.setId(0l);

        Pizza equalsSalamiCheese = Pizza.empty().addIngredient(Ingredient.SALAMI).addIngredient(Ingredient.CHEESE);
        equalsSalamiCheese.setId(1l);

        assertThat(empty).isEqualTo(equalsEmpty);
        assertThat(salamiCheese).isEqualTo(equalsSalamiCheese);
    }

    @Test
    public void notEquality() throws Exception {
        Pizza notEqualsEmpty = Pizza.empty();
        notEqualsEmpty.setId(1l);

        Pizza notEqualsSalamiCheese = Pizza.empty().addIngredient(Ingredient.TOMATO_SAUCE).addIngredient(Ingredient.CHEESE);
        notEqualsSalamiCheese.setId(1l);

        assertThat(empty).isNotEqualTo(notEqualsEmpty);
        assertThat(salamiCheese).isNotEqualTo(notEqualsSalamiCheese);
    }

    @Test
    public void cost() throws Exception {
        assertThat(salamiCheese.getCost()).isEqualTo(Ingredient.SALAMI.getCost().add(Ingredient.CHEESE.getCost()));
    }
}
