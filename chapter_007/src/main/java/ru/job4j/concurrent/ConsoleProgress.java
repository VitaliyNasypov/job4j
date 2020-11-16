package ru.job4j.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleProgress implements Runnable {
    private static final Logger logger = Logger.getLogger(Wget.class.getName());

    @Override
    public void run() {
        String[] process = new String[]{"\\", "|", "/"};
        int index = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.print("\r Loading:  ..." + process[index++] + ".");
            if (index == 3) {
                index = 0;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                if (Thread.currentThread().isInterrupted()) continue;
                logger.log(Level.SEVERE, "Exception: ", e);
            }
        }
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        try {
            Thread.sleep(1000); /* симулируем выполнение параллельной задачи в течение 1 секунды. */
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }
        progress.interrupt();
    }
}
