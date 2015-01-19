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

public class DelivererOperatorTest {
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
    public void testDeliverPurchase() throws Exception {
        Purchase empty = Purchase.empty();
        customer.addPurchase(empty);

        txManager.addEntity(session, customer);

        CookOperator.preparePurchase(session, Iterables.getOnlyElement(CookOperator.getUnpreparedPurchases(session)));

        DelivererOperator.deliverPurchase(session, Iterables.getOnlyElement(DelivererOperator.findPurchasesReadyToDeliver(session)));

        Set<Purchase> tableEntities = txManager.getTableEntities(session, Purchase.class);
        Purchase onlyElement = Iterables.getOnlyElement(tableEntities);

        assertThat(onlyElement.isDelivered()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void testDeliverUnpreparedPurchase() throws Exception {
        Purchase empty = Purchase.empty();
        customer.addPurchase(empty);

        txManager.addEntity(session, customer);

        DelivererOperator.deliverPurchase(session, Iterables.getOnlyElement(txManager.getTableEntities(session, Purchase.class)));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeliverAlreadyDeliveredPurchase() throws Exception {
        Purchase empty = Purchase.empty();
        customer.addPurchase(empty);

        txManager.addEntity(session, customer);

        CookOperator.preparePurchase(session, Iterables.getOnlyElement(CookOperator.getUnpreparedPurchases(session)));

        DelivererOperator.deliverPurchase(session, Iterables.getOnlyElement(DelivererOperator.findPurchasesReadyToDeliver(session)));

        DelivererOperator.deliverPurchase(session, Iterables.getOnlyElement(txManager.getTableEntities(session, Purchase.class)));
    }

    @Test
    public void testFindPurchasesReadyToDeliver() throws Exception {
        List<Purchase> unprepared = Lists.newArrayList();
        unprepared.add(Purchase.empty().addProducts(Pizza.empty()));
        unprepared.add(Purchase.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE)));

        List<Purchase> prepared = Lists.newArrayList();
        Purchase emptyAndPrepared = Purchase.empty();
        emptyAndPrepared.setPrepared(true);
        prepared.add(emptyAndPrepared);

        Purchase pizzaAndPrepared = Purchase.empty().addProducts(Pizza.empty());
        pizzaAndPrepared.setPrepared(true);
        prepared.add(pizzaAndPrepared);

        Purchase cheesePizzaAndPrepared = Purchase.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE));
        cheesePizzaAndPrepared.setPrepared(true);
        prepared.add(cheesePizzaAndPrepared);

        List<Purchase> delivered = Lists.newArrayList();

        Purchase foo = Purchase.empty();
        foo.setPrepared(true);
        foo.setDelivered(true);

        delivered.add(foo);

        for (Purchase purchase : delivered) {
            customer.addPurchase(purchase);
        }

        for (Purchase purchase : prepared) {
            customer.addPurchase(purchase);
        }

        for (Purchase purchase : unprepared) {
            customer.addPurchase(purchase);
        }

        txManager.addEntity(session, customer);

        List<Purchase> purchasesReadyToDeliver = DelivererOperator.findPurchasesReadyToDeliver(session);

        assertThat(purchasesReadyToDeliver).hasSize(prepared.size());
        assertThat(purchasesReadyToDeliver).hasSameElementsAs(prepared);
    }
}