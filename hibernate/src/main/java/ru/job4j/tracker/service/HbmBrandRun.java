package ru.job4j.tracker.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Brand;
import ru.job4j.tracker.model.Model;

public class HbmBrandRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Model xc90 = new Model("XC90");
            Model xc60 = new Model("XC60");
            Model xc40 = new Model("XC40");
            Model s90 = new Model("S90");
            Model s60 = new Model("S60");
            session.save(xc90);
            session.save(xc60);
            session.save(xc40);
            session.save(s90);
            session.save(s60);

            Brand volvo = new Brand("Volvo");
            volvo.addModel(session.load(Model.class, 1));
            volvo.addModel(session.load(Model.class, 2));
            volvo.addModel(session.load(Model.class, 3));
            volvo.addModel(session.load(Model.class, 4));
            volvo.addModel(session.load(Model.class, 5));

            session.save(volvo);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
