package ru.job4j.di;

import org.springframework.stereotype.Component;

@Component
public class StartUI {
    private final Store store;
    private final ConsoleInput consoleInput;

    public StartUI(Store store, ConsoleInput consoleInput) {
        this.store = store;
        this.consoleInput = consoleInput;
    }

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