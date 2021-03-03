package ru.job4j.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StartUI {
    @Autowired
    private Store store;
    @Autowired
    private ConsoleInput consoleInput;

    public void add(String value) {
        store.add(value);
        consoleInput.add(value);
    }

    public void print() {
        for (String value : store.getAll()) {
            System.out.println("Store - " + value);
        }
        for (String value : consoleInput.getAll()) {
            System.out.println("ConsoleInput - " + value);
        }
    }
}