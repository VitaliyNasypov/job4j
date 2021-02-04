package ru.job4j.tracker.associations.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.associations.model.CarAuto;
import ru.job4j.tracker.associations.model.Driver;
import ru.job4j.tracker.associations.model.Engine;

public class HbmAssociations {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmAssociations.class.getName());

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Engine electric = new Engine();
            electric.setName("Electric");
            Engine gasoline = new Engine();
            gasoline.setName("Gasoline");
            session.save(electric);
            session.save(gasoline);

            CarAuto tesla = new CarAuto();
            tesla.setName("Tesla");
            tesla.setEngine(electric);
            CarAuto volvo = new CarAuto();
            volvo.setName("Volvo");
            volvo.setEngine(gasoline);
            Driver first = new Driver();
            first.setName("First");
            Driver second = new Driver();
            second.setName("Second");
            first.getCarAutos().add(tesla);
            first.getCarAutos().add(volvo);
            second.getCarAutos().add(volvo);
            session.save(first);
            session.save(second);
            tesla.getDrivers().add(first);
            volvo.getDrivers().add(first);
            volvo.getDrivers().add(second);
            session.save(tesla);
            session.save(volvo);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
