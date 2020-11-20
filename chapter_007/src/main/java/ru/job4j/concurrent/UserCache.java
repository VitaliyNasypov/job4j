package ru.job4j.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCache {
    private final ConcurrentHashMap<Integer, UserName> users = new ConcurrentHashMap<>();
    private final AtomicInteger id = new AtomicInteger();

    public void add(UserName userName) {
        users.put(id.incrementAndGet(), UserName.of(userName.getName()));
    }

    public UserName findById(int id) {
        return UserName.of(users.get(id).getName());
    }

    public List<UserName> findAll() {
        ArrayList<UserName> copyUserNames = new ArrayList<>();
        users.forEach((key, value) -> copyUserNames.add(UserName.of(value.getName())));
        return copyUserNames;
    }
}

class UserName {
    private int id;
    private String name;

    public static UserName of(String name) {
        UserName userName = new UserName();
        userName.name = name;
        return userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}