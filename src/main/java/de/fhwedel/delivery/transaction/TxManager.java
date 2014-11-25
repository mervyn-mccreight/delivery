package de.fhwedel.delivery.transaction;

import de.fhwedel.delivery.model.Ingredient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

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

    public void printDB() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List list = session.createCriteria(Ingredient.class).list();

        System.out.println("DB CONTENT:");
        System.out.println("------------");

        for (Ingredient ingredient : (List<Ingredient>) list) {
            System.out.println(ingredient);
        }

        System.out.println("-----END-----");
        session.getTransaction().commit();
        session.close();
    }
}
