package de.fhwedel.delivery.operation;


import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fhwedel.delivery.model.*;
import de.fhwedel.delivery.transaction.SessionManager;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CookOperatorTest {
    private TxManager txManager;
    private SessionFactory sessionFactory;
    private Session session;
    private Customer customer;

    @Before
    public void setUp() throws Exception {
        txManager = new TxManager();
        sessionFactory = SessionManager.createSessionFactory();
        session = sessionFactory.openSession();
        customer = new Customer("Hans", "Meier", new Address("Trollstraße 1337", "12345", "Trollstadt", "Trololo"));
    }

    @After
    public void tearDown() throws Exception {
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testPreparePurchase() throws Exception {
        Purchase empty = Purchase.empty();
        customer.addPurchase(empty);

        txManager.addEntity(session, customer);

        Set<Purchase> tableEntities = txManager.getTableEntities(session, Purchase.class);
        assertThat(tableEntities).hasSize(1);
        Purchase onlyElement = Iterables.getOnlyElement(tableEntities);

        assertThat(onlyElement.isPrepared()).isFalse();

        CookOperator.preparePurchase(session, empty);

        Set<Purchase> purchases = txManager.getTableEntities(session, Purchase.class);

        assertThat(purchases).hasSize(1);
        Purchase purchase = Iterables.getOnlyElement(purchases);

        assertThat(purchase.isPrepared()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void testTryToPrepareAlreadyPreparedPurchase() throws Exception {
        Purchase empty = Purchase.empty();
        empty.setPrepared(true);

        customer.addPurchase(empty);
        txManager.addEntity(session, customer);

        CookOperator.preparePurchase(session, empty);
    }

    @Test
    public void testGetUnpreparedPurchases() throws Exception {
        List<Purchase> unprepared = Lists.newArrayList();
        List<Purchase> prepared = Lists.newArrayList();

        unprepared.add(Purchase.empty().addProducts(Pizza.empty()));
        unprepared.add(Purchase.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE)));

        Purchase emptyAndPrepared = Purchase.empty();
        emptyAndPrepared.setPrepared(true);
        prepared.add(emptyAndPrepared);

        Purchase pizzaAndPrepared = Purchase.empty().addProducts(Pizza.empty());
        pizzaAndPrepared.setPrepared(true);
        prepared.add(pizzaAndPrepared);

        Purchase cheesePizzaAndPrepared = Purchase.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE));
        cheesePizzaAndPrepared.setPrepared(true);
        prepared.add(cheesePizzaAndPrepared);

        for (Purchase purchase : prepared) {
            customer.addPurchase(purchase);
        }

        for (Purchase purchase : unprepared) {
            customer.addPurchase(purchase);
        }

        txManager.addEntity(session, customer);

        List<Purchase> unpreparedPurchases = CookOperator.getUnpreparedPurchases(session);

        assertThat(unpreparedPurchases).hasSize(unprepared.size());
        assertThat(unpreparedPurchases).hasSameElementsAs(unprepared);
    }
}