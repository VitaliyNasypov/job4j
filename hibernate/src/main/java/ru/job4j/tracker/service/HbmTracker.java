package ru.job4j.tracker.service;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.Item;

import javax.persistence.Query;
import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateRun.class.getName());
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        }
        return item;
    }

    @Override
    public boolean replace(String id, Item item) {
        item.setId(Integer.parseInt(id));
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            session.update(item);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Item item = new Item(null);
            item.setId(Integer.parseInt(id));
            session.delete(item);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOGGER.warn(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Item> findAll() {
        List<Item> result;
        try (Session session = sf.openSession()) {
            result = session.createQuery("from ru.job4j.tracker.Item").list();
        }
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> result;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from ru.job4j.tracker.Item where name = :key");
            query.setParameter("key", key);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }

    @Override
    public Item findById(String id) {
        Item result;
        try (Session session = sf.openSession()) {
            result = session.get(Item.class, Integer.parseInt(id));
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
