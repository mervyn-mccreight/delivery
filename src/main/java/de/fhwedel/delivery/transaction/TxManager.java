package de.fhwedel.delivery.transaction;

import de.fhwedel.delivery.model.Ingredient;
import de.fhwedel.delivery.model.Pizza;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;

import java.util.List;

public class TxManager {
    private SessionFactory sessionFactory;

    public TxManager() {
        this.sessionFactory = createSessionFactory();
    }

    private SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void dispose() {
        sessionFactory.close();
    }

    public void addIngredient(Ingredient toBeAdded) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.save(toBeAdded);
        session.getTransaction().commit();
        session.close();
    }

    public void addPizza(Pizza pizza) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.save(pizza);
        session.getTransaction().commit();
        session.close();
    }

    public void removePizza(Pizza pizza) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.delete(pizza);
        session.getTransaction().commit();
        session.close();
    }

    public void printTableOfEntityClass(Class tableEntityClass) {
        Session session = sessionFactory.openSession();
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
        session.close();
    }
}
