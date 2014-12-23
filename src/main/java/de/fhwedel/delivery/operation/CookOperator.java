package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.Order;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class CookOperator {
    private static TxManager txManager = new TxManager();

    public static List<Order> getUnpreparedOrders(Session session) {
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("prepared", false));
        return criteria.list();
    }

    public static void prepareOrder(Session session, Order order) {
        if (order.isPrepared()) {
            throw new IllegalStateException(String.format("Order with id %d is already prepared", order.getId()));
        }

        order.setPrepared(true);
        txManager.addEntity(session, order);
    }

//    public static Long order(Session session, Customer customer, Product... products) {
//        Order order = Order.empty().addProducts(products);
//        customer.addOrder(order);
//        txManager.addEntity(session, customer);
//
//        return order.getId();
//    }
//
//    public static Long orderWithOnlinePayment(Session session, Customer customer, Product... products) {
//        Order order = Order.empty().addProducts(products);
//        order.setBilled(true);
//        customer.addOrder(order);
//
//        txManager.addEntity(session, customer);
//
//        return order.getId();
//    }
}
