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
        txManager.addEntity(session, pizza1);

        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addEntity(session, pizza2);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(2);
        assertThat(pizzas).containsOnly(pizza1, pizza2);
    }

    @Test
    public void addTwoPizzas_ingredientsArePersisted() throws Exception {
        txManager.addEntity(session, Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI));
        txManager.addEntity(session, Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI));

        Set<Ingredient> ingredients = txManager.getTableEntities(session, Ingredient.class);

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).containsOnly(Ingredient.CHEESE, Ingredient.SALAMI, Ingredient.TOMATO_SAUCE);
    }

    @Test
    public void removePizza_pizzaIsRemovedIngredientsArePersisted() {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addEntity(session, pizza1);

        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        txManager.addEntity(session, pizza2);

        txManager.removeEntity(session, pizza1);

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
        Order order = Order.empty().addProducts(productToOrder);

        txManager.addEntity(session, order);

        Set<Order> orders = txManager.getTableEntities(session, Order.class);

        assertThat(orders).hasSize(1);
        assertThat(orders).containsOnly(order);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(productToOrder);
    }

    @Test
    public void deleteSingleItemOrder() throws Exception {
        Pizza productToOrder = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE);
        Order order = Order.empty().addProducts(productToOrder);

        txManager.addEntity(session, order);

        Set<Order> orders = txManager.getTableEntities(session, Order.class);

        assertThat(orders).hasSize(1);
        assertThat(orders).containsOnly(order);

        txManager.removeEntity(session, order);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(0);
        assertThat(pizzas).isEmpty();
    }

    @Test
    public void deleteOrderOtherProductsRemain() throws Exception {
        Pizza productToOrder1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE);
        Pizza productToOrder2 = Pizza.empty().addIngredients(Ingredient.CHEESE);
        Order order1 = Order.empty().addProducts(productToOrder1);
        Order order2 = Order.empty().addProducts(productToOrder2);

        txManager.addEntity(session, order1);
        txManager.addEntity(session, order2);

        Set<Order> orders = txManager.getTableEntities(session, Order.class);

        assertThat(orders).hasSize(2);
        assertThat(orders).containsOnly(order1, order2);

        txManager.removeEntity(session, order1);

        orders = txManager.getTableEntities(session, Order.class);
        assertThat(orders).hasSize(1);
        assertThat(orders).containsOnly(order2);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(productToOrder2);
    }

    @Test
    public void persistCustomer() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.CHEESE);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.TOMATO_SAUCE);
        Order order1 = Order.empty().addProducts(pizza1);
        Order order2 = Order.empty().addProducts(pizza2);

        Customer hansMeier = new Customer("Hans", "Meier", new Address("Straße 1", "12345", "Trollhausen", "Schlumpfenland"));
        hansMeier.addOrder(order1).addOrder(order2);

        txManager.addEntity(session, hansMeier);

        Set<Customer> customers = txManager.getTableEntities(session, Customer.class);
        assertThat(customers).hasSize(1);
        assertThat(customers).containsOnly(hansMeier);

        Set<Order> orders = txManager.getTableEntities(session, Order.class);
        assertThat(orders).hasSize(hansMeier.getOrders().size());
        assertThat(orders).containsOnlyElementsOf(hansMeier.getOrders());
    }

    @Test
    public void deleteCustomer() throws Exception {
        Customer hansMeier = new Customer("Hans", "Meier", new Address("Straße 1", "12345", "Trollhausen", "Schlumpfenland"));

        txManager.addEntity(session, hansMeier);

        Set<Customer> customers = txManager.getTableEntities(session, Customer.class);
        assertThat(customers).hasSize(1);
        assertThat(customers).containsOnly(hansMeier);

        txManager.removeEntity(session, hansMeier);

        Set<Address> addresses = txManager.getTableEntities(session, Address.class);
        assertThat(addresses).isEmpty();
    }

    @Test
    public void customersWithSameAddress() throws Exception {
        Address sharedAddress = new Address("ABC-Straße", "12345", "Trollhausen", "Schlumpfenland");
        Customer hansMeier = new Customer("Hans", "Meier", sharedAddress);
        Customer petraMeier = new Customer("Petra", "Meier", sharedAddress);

        txManager.addEntity(session, hansMeier);
        txManager.addEntity(session, petraMeier);

        Set<Address> addresses = txManager.getTableEntities(session, Address.class);
        assertThat(addresses).hasSize(1);
        assertThat(addresses).containsOnly(sharedAddress);
    }

    @Test
    public void customersWithSameAddressDeleteOneCustomer_addressStillExists() throws Exception {
        Address sharedAddress = new Address("ABC-Straße", "12345", "Trollhausen", "Schlumpfenland");
        Customer petraMeier = new Customer("Petra", "Meier", sharedAddress);

        txManager.addEntity(session, new Customer("Hans", "Meier", sharedAddress));
        txManager.addEntity(session, petraMeier);

        assertThat(txManager.getTableEntities(session, Address.class)).hasSize(1);
        assertThat(txManager.getTableEntities(session, Address.class)).containsOnly(sharedAddress);

        txManager.removeEntity(session, petraMeier);

        assertThat(txManager.getTableEntities(session, Customer.class)).hasSize(1);
        assertThat(txManager.getTableEntities(session, Address.class)).hasSize(1);
    }
}
