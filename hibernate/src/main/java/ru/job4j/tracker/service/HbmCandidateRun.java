package ru.job4j.tracker.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.tracker.model.Candidate;
import ru.job4j.tracker.model.JobBase;
import ru.job4j.tracker.model.Vacancy;

public class HbmCandidateRun {
    private static final Logger LOGGER = LoggerFactory.getLogger(HbmCandidateRun.class.getName());

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            add(sf);
            getCandidate(sf, 1);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static void add(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Vacancy vacancyFirst = new Vacancy();
            vacancyFirst.setName("Vacancy First");
            Vacancy vacancySecond = new Vacancy();
            vacancySecond.setName("Vacancy Second");
            session.save(vacancyFirst);
            session.save(vacancySecond);
            JobBase jobBase = new JobBase();
            jobBase.setName("JobBase");
            jobBase.getVacancyList().add(vacancyFirst);
            jobBase.getVacancyList().add(vacancySecond);
            session.save(jobBase);
            Candidate first = new Candidate("First", 10, 40000);
            first.setJobBase(jobBase);
            Candidate second = new Candidate("Second", 5, 25000);
            second.setJobBase(jobBase);
            Candidate third = new Candidate("Third", 20, 120000);
            third.setJobBase(jobBase);
            session.save(first);
            session.save(second);
            session.save(third);
            session.getTransaction().commit();
        }
    }

    private static void getCandidate(SessionFactory sf, int id) {
        try (Session session = sf.openSession()) {
            Candidate candidate = session.createQuery(
                    "select distinct c from Candidate c "
                            + "join fetch c.jobBase j "
                            + "join fetch j.vacancyList v "
                            + "where c.id = :id", Candidate.class
            ).setParameter("id", id).uniqueResult();
            System.out.println("Candidate id = " + candidate.getId());
            System.out.println("Candidate Name = " + candidate.getName());
            System.out.println("JobBase Name = " + candidate.getJobBase().getName());
            System.out.println("Vacancy Name = " + candidate.getJobBase()
                    .getVacancyList().get(0).getName());
        }
    }
}
