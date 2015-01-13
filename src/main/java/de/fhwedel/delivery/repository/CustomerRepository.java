package de.fhwedel.delivery.repository;

import de.fhwedel.delivery.model.Address;
import de.fhwedel.delivery.model.Customer;
import de.fhwedel.delivery.transaction.TxManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class CustomerRepository {

    private static TxManager txManager = new TxManager();

    public static List<Customer> findCustomersByAddressId(Session session, Long addressId) {
        Criteria criteria = session.createCriteria(Customer.class);
        criteria.add(Restrictions.eq("address", new Address(addressId)));

        return criteria.list();
    }

    public static void deleteCustomer(Session session, Customer toBeDeleted) {
        Address address = toBeDeleted.getAddress();

        txManager.removeEntity(session, toBeDeleted);

        if (findCustomersByAddressId(session, address.getId()).isEmpty()) {
            txManager.removeEntity(session, address);
        }
    }
}
