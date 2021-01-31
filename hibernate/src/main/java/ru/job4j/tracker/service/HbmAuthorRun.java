package ru.job4j.tracker.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Author;
import ru.job4j.tracker.model.Book;

public class HbmAuthorRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
            Book one = new Book("Война и мир");
            Book two = new Book("Добрые предзнаменования");
            session.save(one);
            session.save(two);
            Author levTolstoy = new Author("Лев Толстой");
            Author terryPratchett = new Author("Терри Пратчетт");
            Author neilGaiman = new Author("Нейл Гейман");
            levTolstoy.getBooks().add(one);
            terryPratchett.getBooks().add(two);
            neilGaiman.getBooks().add(two);
            session.save(levTolstoy);
            session.save(terryPratchett);
            session.save(neilGaiman);
            neilGaiman = session.get(Author.class, 3);
            session.remove(neilGaiman);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
