package de.fhwedel.delivery.operation;

import de.fhwedel.delivery.model.Customer;
import de.fhwedel.delivery.model.Product;
import de.fhwedel.delivery.model.Purchase;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Session;

public class CustomerOperator {
    private static TxManager txManager = new TxManager();

    public static Long purchase(Session session, Customer customer, Product... products) {
        Purchase purchase = Purchase.empty().addProducts(products);
        customer.addPurchase(purchase);
        txManager.addEntity(session, customer);

        return purchase.getId();
    }

    public static Long purchaseWithOnlinePayment(Session session, Customer customer, Product... products) {
        Purchase purchase = Purchase.empty().addProducts(products);
        purchase.setBilled(true);
        customer.addPurchase(purchase);

        txManager.addEntity(session, customer);

        return purchase.getId();
    }
}
