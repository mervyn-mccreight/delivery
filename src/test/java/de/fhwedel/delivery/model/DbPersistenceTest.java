package de.fhwedel.delivery.model;

import de.fhwedel.delivery.transaction.SessionManager;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DbPersistenceTest {

    private TxManager txManager;
    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() throws Exception {
        txManager = new TxManager();
        sessionFactory = SessionManager.createSessionFactory();
        session = sessionFactory.openSession();
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        sessionFactory.close();
    }

    @Test
    public void addTwoPizzas_pizzaAndIngredientsArePersisted() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(session, pizza1);

        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(session, pizza2);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(2);
        assertThat(pizzas).containsOnly(pizza1, pizza2);
    }

    @Test
    public void addTwoPizzas_ingredientsArePersisted() throws Exception {
        txManager.addPizza(session, Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI));
        txManager.addPizza(session, Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI));

        Set<Ingredient> ingredients = txManager.getTableEntities(session, Ingredient.class);

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).containsOnly(Ingredient.CHEESE, Ingredient.SALAMI, Ingredient.TOMATO_SAUCE);
    }

    @Test
    public void removePizza_pizzaIsRemovedIngredientsArePersisted() {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(session, pizza1);

        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addPizza(session, pizza2);

        txManager.removePizza(session, pizza1);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);
        Set<Ingredient> ingredients = txManager.getTableEntities(session, Ingredient.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(pizza2);

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).containsOnly(Ingredient.TOMATO_SAUCE, Ingredient.SALAMI, Ingredient.CHEESE);
    }

    @Test
    public void persistOrder() throws Exception {
        Pizza productToOrder = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE);
        Order order = Order.empty().addProduct(productToOrder);

        txManager.addOrder(session, order);

        Set<Order> orders = txManager.getTableEntities(session, Order.class);

        assertThat(orders).hasSize(1);
        assertThat(orders).containsOnly(order);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(productToOrder);
    }
}
