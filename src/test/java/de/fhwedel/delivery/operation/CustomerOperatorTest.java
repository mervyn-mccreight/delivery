package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.*;
import de.fhwedel.delivery.transaction.SessionManager;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerOperatorTest {
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
    public void orderWithNewCustomer() throws Exception {
        Customer customer = new Customer("Heinz", "Egon", new Address("Straße 1", "12345", "Trollhausen", "Schlumpfenland"));
        Pizza pizza = Pizza.empty().addIngredient(Ingredient.CHEESE);

        Long orderId = CustomerOperator.order(session, customer, pizza);

        assertThat(orderId).isNotNull();

        Order order = txManager.findEntityById(session, Order.class, orderId);

        assertThat(order).isNotNull();
        assertThat(order.getProducts()).containsOnly(pizza);
    }
}
