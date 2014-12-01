package de.fhwedel.delivery.transaction;

import com.google.common.collect.Sets;
import de.fhwedel.delivery.model.Ingredient;
import de.fhwedel.delivery.model.Pizza;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Set;

public class TxManager {
    public void addIngredient(Session session, Ingredient toBeAdded) {
        session.beginTransaction();
        session.save(toBeAdded);
        session.getTransaction().commit();
    }

    public void addPizza(Session session, Pizza pizza) {
        session.beginTransaction();
        session.save(pizza);
        session.getTransaction().commit();
    }

    public void removePizza(Session session, Pizza pizza) {
        session.beginTransaction();
        session.delete(pizza);
        session.getTransaction().commit();
    }

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

    public void addOrder(Session session, de.fhwedel.delivery.model.Order order) {
        session.beginTransaction();

        session.save(order);
        session.getTransaction().commit();
    }
}
