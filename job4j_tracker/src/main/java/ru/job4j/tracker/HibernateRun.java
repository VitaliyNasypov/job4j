package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

public class HibernateRun {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateRun.class.getName());

    public static void main(String[] args) {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Item item1 = create(new Item("Learn Hibernate", "Description",
                    new Timestamp(1459510232000L)), sf);
            create(new Item("Learn Hibernate 2", "Description 2",
                    new Timestamp(14595102234200L)), sf);
            create(new Item("Learn Hibernate 3", "Description 3",
                    new Timestamp(1459510243242000L)), sf);
            System.out.println(item1);
            item1.setName("Learn Hibernate 5.");
            update(item1, sf);
            System.out.println(item1);
            Item rsl = findById(item1.getId(), sf);
            System.out.println(rsl);
            delete(rsl.getId(), sf);
            List<Item> list = findAll(sf);
            for (Item it : list) {
                System.out.println(it);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static Item create(Item item, SessionFactory sf) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
        return item;
    }

    public static void update(Item item, SessionFactory sf) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
        }
    }

    public static void delete(Integer id, SessionFactory sf) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Item item = new Item(null);
            item.setId(id);
            session.delete(item);
            session.getTransaction().commit();
        }
    }

    public static List<Item> findAll(SessionFactory sf) {
        List result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.createQuery("from ru.job4j.tracker.Item").list();
            session.getTransaction().commit();
        }
        return result;
    }

    public static Item findById(Integer id, SessionFactory sf) {
        Item result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            result = session.get(Item.class, id);
            session.getTransaction().commit();
        }
        return result;
    }
}
