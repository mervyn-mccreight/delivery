package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.Purchase;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DelivererOperator {
    private static TxManager txManager = new TxManager();

    public static List<Purchase> findPurchasesReadyToDeliver(Session session) {
        Criteria criteria = session.createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("prepared", true));
        criteria.add(Restrictions.eq("delivered", false));
        return criteria.list();
    }

    public static void deliverPurchase(Session session, Purchase purchase) {
        if (purchase.isDelivered()) {
            throw new IllegalStateException(String.format("Purchase with id %d is already delivered", purchase.getId()));
        }

        if (!purchase.isPrepared()) {
            throw new IllegalStateException(String.format("Purchase with id %d is not prepared", purchase.getId()));
        }

        purchase.setDelivered(true);
        txManager.addEntity(session, purchase);
    }
}
