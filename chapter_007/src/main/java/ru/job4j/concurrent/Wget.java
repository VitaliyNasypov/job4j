package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wget {
    private static final Logger LOGGER = Logger.getLogger(Wget.class.getName());

    public static void main(String[] args) {
        if (inquiryVerification(args)) {
            Callable<String> downloader = new Downloader(args[0], Long.parseLong(args[1]));
            FutureTask<String> future = new FutureTask<>(downloader);
            new Thread(future).start();
            try {
                LOGGER.info("Результат загрузки: " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.severe("Exception: " + e);
            }
        } else {
            LOGGER.info("Неверные параметры");
        }
    }

    private static boolean inquiryVerification(String[] args) {
        return args.length == 2
                && args[0].matches(
                "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
                && args[1].matches("\\d+");
    }
}

class Downloader implements Callable<String> {
    private static final Logger LOGGER = Logger.getLogger(Downloader.class.getName());
    private final String url;
    private final long speedLimit;

    Downloader(String url, long speedLimit) {
        this.speedLimit = speedLimit;
        this.url = url;
    }

    @Override
    public String call() {
        return fileUpload();
    }

    private String fileUpload() {
        File fileTemp = generationTempFileName();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(fileTemp, true)) {
            byte[] buffer = new byte[1000];
            int bytesRead;
            long diffTime = 0;
            int countBytesRead = 0;
            long startTime = System.nanoTime();
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                diffTime += System.nanoTime() - startTime;
                countBytesRead += bytesRead;
                if (checkDownloadSpeed(diffTime, countBytesRead)) {
                    countBytesRead = 0;
                    diffTime = 0;
                }
                startTime = System.nanoTime();
            }
            return "Файл загружен";
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e);
            return "Файл не загружен";
        }
    }

    private File generationTempFileName() {
        String[] nameTempFile = url.split("\\.");
        return new File("fileTemp." + nameTempFile[nameTempFile.length - 1]);
    }

    private boolean checkDownloadSpeed(long diffTime, int countBytesRead) {
        if (diffTime / 1_000_000 < 1000 && countBytesRead >= speedLimit / 1000) {
            try {
                Thread.sleep(1000 - diffTime / 1_000_000);
                return true;
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Exception: ", e);
            }
        }
        return countBytesRead >= speedLimit;
    }
}