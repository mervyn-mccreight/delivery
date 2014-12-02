package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.Customer;
import de.fhwedel.delivery.model.Order;
import de.fhwedel.delivery.model.Product;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Session;

public class CustomerOperator {
    private static TxManager txManager = new TxManager();

    public static Long order(Session session, Customer customer, Product... products) {
        Order order = Order.empty().addProducts(products);
        customer.addOrder(order);
        txManager.addEntity(session, customer);

        return order.getId();
    }
}
