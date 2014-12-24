package de.fhwedel.delivery.operation;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import de.fhwedel.delivery.model.Ingredient;
import de.fhwedel.delivery.model.Order;
import de.fhwedel.delivery.model.Pizza;
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
    public void testDeliverOrder() throws Exception {
        Order empty = Order.empty();

        txManager.addEntity(session, empty);

        CookOperator.prepareOrder(session, Iterables.getOnlyElement(CookOperator.getUnpreparedOrders(session)));

        DelivererOperator.deliverOrder(session, Iterables.getOnlyElement(DelivererOperator.findOrdersReadyToDeliver(session)));

        Set<Order> tableEntities = txManager.getTableEntities(session, Order.class);
        Order onlyElement = Iterables.getOnlyElement(tableEntities);

        assertThat(onlyElement.isDelivered()).isTrue();
    }

    @Test(expected = IllegalStateException.class)
    public void testDeliverUnpreparedOrder() throws Exception {
        Order empty = Order.empty();

        txManager.addEntity(session, empty);

        DelivererOperator.deliverOrder(session, Iterables.getOnlyElement(txManager.getTableEntities(session, Order.class)));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeliverAlreadyDeliveredOrder() throws Exception {
        Order empty = Order.empty();

        txManager.addEntity(session, empty);

        CookOperator.prepareOrder(session, Iterables.getOnlyElement(CookOperator.getUnpreparedOrders(session)));

        DelivererOperator.deliverOrder(session, Iterables.getOnlyElement(DelivererOperator.findOrdersReadyToDeliver(session)));

        DelivererOperator.deliverOrder(session, Iterables.getOnlyElement(txManager.getTableEntities(session, Order.class)));
    }

    @Test
    public void testFindOrdersReadyToDeliver() throws Exception {
        List<Order> unprepared = Lists.newArrayList();
        unprepared.add(Order.empty().addProducts(Pizza.empty()));
        unprepared.add(Order.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE)));

        List<Order> prepared = Lists.newArrayList();
        Order emptyAndPrepared = Order.empty();
        emptyAndPrepared.setPrepared(true);
        prepared.add(emptyAndPrepared);

        Order pizzaAndPrepared = Order.empty().addProducts(Pizza.empty());
        pizzaAndPrepared.setPrepared(true);
        prepared.add(pizzaAndPrepared);

        Order cheesePizzaAndPrepared = Order.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE));
        cheesePizzaAndPrepared.setPrepared(true);
        prepared.add(cheesePizzaAndPrepared);

        List<Order> delivered = Lists.newArrayList();

        Order foo = Order.empty();
        foo.setPrepared(true);
        foo.setDelivered(true);

        delivered.add(foo);

        for (Order order : delivered) {
            txManager.addEntity(session, order);
        }

        for (Order order : prepared) {
            txManager.addEntity(session, order);
        }

        for (Order order : unprepared) {
            txManager.addEntity(session, order);
        }

        List<Order> ordersReadyToDeliver = DelivererOperator.findOrdersReadyToDeliver(session);

        assertThat(ordersReadyToDeliver).hasSize(prepared.size());
        assertThat(ordersReadyToDeliver).hasSameElementsAs(prepared);
    }
}