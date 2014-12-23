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

public class CookOperatorTest {
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
    public void testPrepareOrder() throws Exception {
        Order empty = Order.empty();

        txManager.addEntity(session, empty);

        Set<Order> tableEntities = txManager.getTableEntities(session, Order.class);
        assertThat(tableEntities).hasSize(1);
        Order onlyElement = Iterables.getOnlyElement(tableEntities);

        assertThat(onlyElement.isPrepared()).isFalse();

        CookOperator.prepareOrder(session, empty);

        Set<Order> orders = txManager.getTableEntities(session, Order.class);

        assertThat(orders).hasSize(1);
        Order order = Iterables.getOnlyElement(orders);

        assertThat(order.isPrepared()).isTrue();
    }

    @Test
    public void testGetUnpreparedOrders() throws Exception {
        List<Order> unprepared = Lists.newArrayList();
        List<Order> prepared = Lists.newArrayList();

        unprepared.add(Order.empty().addProducts(Pizza.empty()));
        unprepared.add(Order.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE)));

        Order emptyAndPrepared = Order.empty();
        emptyAndPrepared.setPrepared(true);
        prepared.add(emptyAndPrepared);

        Order pizzaAndPrepared = Order.empty().addProducts(Pizza.empty());
        pizzaAndPrepared.setPrepared(true);
        prepared.add(pizzaAndPrepared);

        Order cheesePizzaAndPrepared = Order.empty().addProducts(Pizza.empty().addIngredients(Ingredient.TOMATO_SAUCE, Ingredient.CHEESE));
        cheesePizzaAndPrepared.setPrepared(true);
        prepared.add(cheesePizzaAndPrepared);

        for (Order order : prepared) {
            txManager.addEntity(session, order);
        }

        for (Order order : unprepared) {
            txManager.addEntity(session, order);
        }

        List<Order> unpreparedOrders = CookOperator.getUnpreparedOrders(session);

        assertThat(unpreparedOrders).hasSize(unprepared.size());
        assertThat(unpreparedOrders).hasSameElementsAs(unprepared);
    }
}