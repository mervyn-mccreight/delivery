package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.Purchase;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class CookOperator {
    private static TxManager txManager = new TxManager();

    public static List<Purchase> getUnpreparedPurchases(Session session) {
        Criteria criteria = session.createCriteria(Purchase.class);
        criteria.add(Restrictions.eq("prepared", false));
        return criteria.list();
    }

    public static void preparePurchase(Session session, Purchase purchase) {
        if (purchase.isPrepared()) {
            throw new IllegalStateException(String.format("Purchase with id %d is already prepared", purchase.getId()));
        }

        purchase.setPrepared(true);
        txManager.addEntity(session, purchase);
    }
}
