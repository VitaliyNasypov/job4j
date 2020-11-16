package ru.job4j.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Wget {
    private static final Logger logger = Logger.getLogger(Wget.class.getName());

    public static void main(String[] args) {
        Thread thread = new Thread(
                () -> {
                    try {
                        System.out.print("Start loading ... ");
                        Thread.sleep(500);
                        for (int i = 0; i <= 100; i++) {
                            System.out.print("\rLoading : " + i + "%");
                            Thread.sleep(1000);
                        }
                        System.out.println("\rLoaded.");
                    } catch (InterruptedException e) {
                        logger.log(Level.SEVERE, "Exception: ", e);
                    }
                }
        );
        thread.start();
    }
}
