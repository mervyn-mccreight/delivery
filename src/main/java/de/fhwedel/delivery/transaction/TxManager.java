package de.fhwedel.delivery.transaction;

import com.google.common.collect.Sets;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Set;

public class TxManager {

    public <T> Set<T> getTableEntities(Session session, Class<T> entityClass) {
        session.beginTransaction();

        Criteria criteria = session.createCriteria(entityClass);
        criteria.addOrder(Order.asc("id"));
        List list = criteria.list();
        session.getTransaction().commit();

        return Sets.newHashSet(list);
    }

    public void printTableOfEntityClass(Session session, Class tableEntityClass) {
        session.beginTransaction();

        Criteria criteria = session.createCriteria(tableEntityClass);
        criteria.addOrder(Order.asc("id"));
        List list = criteria.list();

        System.out.println("TABLE CONTENT:");
        System.out.println("------------");

        for (Object entity : list) {
            System.out.println(entity);
        }

        System.out.println("-----END-----");
        session.getTransaction().commit();
    }

    public void addEntity(Session session, Object object) {
        session.beginTransaction();
        session.save(object);
        session.getTransaction().commit();
    }

    public void removeEntity(Session session, Object object) {
        session.beginTransaction();
        session.delete(object);
        session.getTransaction().commit();
    }
}
