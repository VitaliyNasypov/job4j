package ru.job4j.concurrent;

public class Switcher {

    public static void main(String[] args) throws InterruptedException {
        final Switcher lock = new Switcher();
        Thread first = new Thread(
                () -> {
                    while (true) {
                        System.out.println("Thread A");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock.on();
                        lock.off();
                    }
                }
        );
        Thread second = new Thread(
                () -> {
                    while (true) {
                        lock.off();
                        System.out.println("Thread B");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        lock.on();
                    }
                }
        );
        first.start();
        second.start();
        first.join();
        second.join();
    }

    private synchronized void on() {
        notify();
    }

    private synchronized void off() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
