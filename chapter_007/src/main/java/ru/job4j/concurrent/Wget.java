package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Wget {
    private static final Logger logger = Logger.getLogger(Wget.class.getName());

    public static void main(String[] args) {
        if (args.length == 2
                && args[0].matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")
                && args[1].matches("\\d+")) {
            String[] nameTempFile = args[0].split("\\.");
            File fileTemp = new File("fileTemp." + nameTempFile[nameTempFile.length - 1]);
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(args[0]).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(fileTemp, true)) {
                byte[] buffer = new byte[1000];
                int bytesRead;
                long speedLimit = Long.parseLong(args[1]);
                long startTime = System.nanoTime();
                long diffTime = 0;
                int count = 0;
                while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                    diffTime += System.nanoTime() - startTime;
                    count++;
                    if (diffTime / 1_000_000 < 1000 && count == speedLimit) {
                        Thread.sleep(1000 - diffTime / 1_000_000);
                        count = 0;
                        diffTime = 0;
                    } else if (count == speedLimit) {
                        count = 0;
                        diffTime = 0;
                    }
                    startTime = System.nanoTime();
                }
            } catch (IOException | InterruptedException e) {
                logger.log(Level.SEVERE, "Exception: ", e);
            }
        } else {
            System.out.println("Неверные параметры");
        }
    }
}
