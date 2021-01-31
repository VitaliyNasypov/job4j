package ru.job4j.tracker.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.BrandAuto;
import ru.job4j.tracker.model.ModelAuto;

import java.util.List;

public class HbmBrandAutoRun {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmBrandAutoRun.class.getName());

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            add(sf);
            firstOption(sf);
            secondOption(sf);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void add(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();

            BrandAuto volvo = new BrandAuto("Volvo");
            session.save(volvo);

            ModelAuto xc90 = new ModelAuto("XC90");
            xc90.setBrandAuto(volvo);
            ModelAuto xc60 = new ModelAuto("XC60");
            xc60.setBrandAuto(volvo);
            session.save(xc90);
            session.save(xc60);
            session.getTransaction().commit();
        }
    }

    private static void firstOption(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            List<BrandAuto> list = session.createQuery("from BrandAuto").list();
            for (BrandAuto brandAuto : list) {
                for (ModelAuto modelAuto : brandAuto.getModelAutos()) {
                    System.out.println(brandAuto + ", " + modelAuto);
                }
            }
            session.getTransaction().commit();
        }
    }

    private static void secondOption(SessionFactory sf) {
        List<BrandAuto> list;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            list = session.createQuery(
                    "select distinct c from BrandAuto c join fetch c.modelAutos").list();
            session.getTransaction().commit();
        }
        for (BrandAuto brandAuto : list) {
            for (ModelAuto modelAuto : brandAuto.getModelAutos()) {
                System.out.println(brandAuto + ", " + modelAuto);
            }
        }
    }
}
