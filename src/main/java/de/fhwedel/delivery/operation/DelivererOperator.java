package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.Order;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DelivererOperator {
    private static TxManager txManager = new TxManager();

    public static List<Order> findOrdersReadyToDeliver(Session session) {
        Criteria criteria = session.createCriteria(Order.class);
        criteria.add(Restrictions.eq("prepared", true));
        criteria.add(Restrictions.eq("delivered", false));
        return criteria.list();
    }

    public static void deliverOrder(Session session, Order order) {
        if (order.isDelivered()) {
            throw new IllegalStateException(String.format("Order with id %d is already delivered", order.getId()));
        }

        if (!order.isPrepared()) {
            throw new IllegalStateException(String.format("Order with id %d is not prepared", order.getId()));
        }

        order.setDelivered(true);
        txManager.addEntity(session, order);
    }
}
