package de.fhwedel.delivery.model;

import com.google.common.collect.Lists;
import de.fhwedel.delivery.repository.CustomerRepository;
import de.fhwedel.delivery.transaction.SessionManager;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DbPersistenceTest {

    private TxManager txManager;
    private SessionFactory sessionFactory;
    private Session session;
    private Customer hansMeier;
    private Address sharedAddress;

    @Before
    public void setUp() throws Exception {
        txManager = new TxManager();
        sessionFactory = SessionManager.createSessionFactory();
        session = sessionFactory.openSession();

        sharedAddress = new Address("ABC-Straße", "12345", "Trollhausen", "Schlumpfenland");
        hansMeier = new Customer("Hans", "Meier", sharedAddress);
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        sessionFactory.close();
    }

    @Test
    public void addTwoPizzas_pizzaAndIngredientsArePersisted() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);

        hansMeier.addPurchase(Purchase.empty().addProducts(pizza1, pizza2));
        txManager.addEntity(session, hansMeier);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(2);
        assertThat(pizzas).containsOnly(pizza1, pizza2);
    }

    @Test
    public void pizzaWithSameIngredientTwice_ingredientIsPersistedTwice() throws Exception {
        Pizza doubleCheese = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.CHEESE);

        hansMeier.addPurchase(Purchase.empty().addProducts(doubleCheese));

        txManager.addEntity(session, hansMeier);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        Pizza dbDoubleCheese = pizzas.iterator().next();

        assertThat(dbDoubleCheese.getIngredients()).hasSize(3);

        Collection<Ingredient> ingredients = dbDoubleCheese.getIngredients();

        assertThat(countElementsInCollection(Ingredient.TOMATO_SAUCE, ingredients)).isEqualTo(1);
        assertThat(countElementsInCollection(Ingredient.CHEESE, ingredients)).isEqualTo(2);
    }

    private <T> int countElementsInCollection(T toCount, Collection<T> toCountIn) {
        if (toCountIn.isEmpty()) {
            return 0;
        }

        T next = toCountIn.iterator().next();
        Collection toCountInWithout = Lists.newArrayList(toCountIn);
        toCountInWithout.remove(next);

        if (next.equals(toCount)) {
            return 1 + countElementsInCollection(toCount, toCountInWithout);
        }

        return countElementsInCollection(toCount, toCountInWithout);
    }

    @Test
    public void addTwoPizzas_ingredientsArePersisted() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE, Ingredient.SALAMI);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.SALAMI);
        hansMeier.addPurchase(Purchase.empty().addProducts(pizza1, pizza2));

        txManager.addEntity(session, hansMeier);

        Set<Ingredient> ingredients = txManager.getTableEntities(session, Ingredient.class);

        assertThat(ingredients).hasSize(3);
        assertThat(ingredients).containsOnly(Ingredient.CHEESE, Ingredient.SALAMI, Ingredient.TOMATO_SAUCE);
    }

    @Test
    public void persistPurchase() throws Exception {
        Pizza productToOrder = Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE);
        Purchase purchase = Purchase.empty().addProducts(productToOrder);
        hansMeier.addPurchase(purchase);

        txManager.addEntity(session, hansMeier);

        Set<Purchase> purchases = txManager.getTableEntities(session, Purchase.class);

        assertThat(purchases).hasSize(1);
        assertThat(purchases).containsOnly(purchase);

        Set<Pizza> pizzas = txManager.getTableEntities(session, Pizza.class);

        assertThat(pizzas).hasSize(1);
        assertThat(pizzas).containsOnly(productToOrder);
    }

    @Test
    public void deleteSingleItemPurchase() throws Exception {
        //TODO: wäre richtig wenn er da wäre.
    }

    @Test
    public void deletePurchaseOtherProductsRemain() throws Exception {
        //TODO: wäre richtig wenn er da wäre.
    }

    @Test
    public void persistCustomer() throws Exception {
        Pizza pizza1 = Pizza.empty().addIngredients(Ingredient.CHEESE);
        Pizza pizza2 = Pizza.empty().addIngredients(Ingredient.CHEESE, Ingredient.TOMATO_SAUCE);
        Purchase purchase1 = Purchase.empty().addProducts(pizza1);
        Purchase purchase2 = Purchase.empty().addProducts(pizza2);

        Customer hansMeier = new Customer("Hans", "Meier", new Address("Straße 1", "12345", "Trollhausen", "Schlumpfenland"));
        hansMeier.addPurchase(purchase1).addPurchase(purchase2);

        txManager.addEntity(session, hansMeier);

        Set<Customer> customers = txManager.getTableEntities(session, Customer.class);
        assertThat(customers).hasSize(1);
        assertThat(customers).containsOnly(hansMeier);

        Set<Purchase> purchases = txManager.getTableEntities(session, Purchase.class);
        assertThat(purchases).hasSize(hansMeier.getPurchases().size());
        assertThat(purchases).containsOnlyElementsOf(hansMeier.getPurchases());
    }

    @Test
    public void deleteCustomer() throws Exception {
        Customer hansMeier = new Customer("Hans", "Meier", new Address("Straße 1", "12345", "Trollhausen", "Schlumpfenland"));

        txManager.addEntity(session, hansMeier);

        Set<Customer> customers = txManager.getTableEntities(session, Customer.class);
        assertThat(customers).hasSize(1);
        assertThat(customers).containsOnly(hansMeier);

        CustomerRepository.deleteCustomer(session, hansMeier);

        Set<Address> addresses = txManager.getTableEntities(session, Address.class);
        assertThat(addresses).isEmpty();
    }

    @Test
    public void customersWithSameAddress() throws Exception {
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

        CustomerRepository.deleteCustomer(session, petraMeier);

        assertThat(txManager.getTableEntities(session, Customer.class)).hasSize(1);
        assertThat(txManager.getTableEntities(session, Address.class)).hasSize(1);
    }

    @Test
    public void customersWithSameAddressDeleteBothCustomers_addressIsDeleted() throws Exception {
        Customer petraMeier = new Customer("Petra", "Meier", sharedAddress);

        txManager.addEntity(session, hansMeier);
        txManager.addEntity(session, petraMeier);

        assertThat(txManager.getTableEntities(session, Address.class)).hasSize(1);
        assertThat(txManager.getTableEntities(session, Address.class)).containsOnly(sharedAddress);

        CustomerRepository.deleteCustomer(session, petraMeier);
        CustomerRepository.deleteCustomer(session, hansMeier);

        assertThat(txManager.getTableEntities(session, Customer.class)).hasSize(0);
        assertThat(txManager.getTableEntities(session, Address.class)).hasSize(0);
    }
}
