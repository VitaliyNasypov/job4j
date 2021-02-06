package ru.job4j.tracker.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.Candidate;
import ru.job4j.tracker.model.Item;

import java.util.List;

public class HbmCandidateRun {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmCandidateRun.class.getName());

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Candidate first = new Candidate("First", 10, 40000);
            Candidate second = new Candidate("Second", 5, 25000);
            Candidate third = new Candidate("Third", 20, 120000);
            session.save(first);
            session.save(second);
            session.save(third);
            List<Candidate> candidateListAll = session.createQuery("from Candidate").list();
            for (Candidate candidate : candidateListAll) {
                System.out.println(candidate.getId() + " " + candidate.getName());
            }
            Candidate candidateId = session.get(Candidate.class, 1);
            System.out.println(candidateId.getId());

            List<Candidate> candidateListName = session.createQuery("from Candidate c "
                    + "where c.name = :name")
                    .setParameter("name", "First")
                    .list();
            for (Candidate candidate : candidateListName) {
                System.out.println(candidate.getName());
            }
            session.createQuery("update Candidate c set c.name = :newName, "
                    + "c.experience = :newExperience where c.id = :id")
                    .setParameter("newName", "Fourth")
                    .setParameter("newExperience", 30)
                    .setParameter("id", 1)
                    .executeUpdate();
            session.createQuery("delete from Candidate c where c.id = :id")
                    .setParameter("id", 3)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
