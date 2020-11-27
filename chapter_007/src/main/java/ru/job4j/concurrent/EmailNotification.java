package ru.job4j.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private final ExecutorService pool;
    private final String subject = "Notification %s to email %s";
    private final String body = "Add a new event to %s";

    public EmailNotification() {
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void emailTo(UserEmail userEmail) {
        String newSubject = String.format(subject, userEmail.getUsername(), userEmail.getEmail());
        String newBody = String.format(body, userEmail.getUsername());
        pool.submit(() -> send(newSubject, newBody, userEmail.getEmail()));
    }

    public void send(String subject, String body, String email) {
    }

    public void close() {
        pool.shutdown();
    }

}

class UserEmail {
    private final String username;
    private final String email;

    public UserEmail(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}