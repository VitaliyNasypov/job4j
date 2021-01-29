package ru.job4j.concurrent;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class WgetTest {

    @Test
    public void shouldRequestNoCorrect() {
        String url = "test://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml";
        String speedLimit = "aaa";
        Wget.main(new String[]{url, speedLimit});
        File file = new File("fileTemp.xml");
        Assertions.assertFalse(file.exists());
    }

    @Test
    public void shouldRequestCorrect() {
        String url = "https://raw.githubusercontent.com/peterarsentev/course_test/master/pom.xml";
        String speedLimit = "200";
        Wget.main(new String[]{url, speedLimit});
        File file = new File("fileTemp.xml");
        Assertions.assertTrue(file.exists());
        file.delete();
    }
}

class DownloaderTest {
    private static final Logger LOGGER = Logger.getLogger(Downloader.class.getName());

    private static Stream<Callable<String>> correctUrl() {
        return Stream.of(
                new Downloader(
                        "https://raw.githubusercontent.com/peterarsentev/"
                                + "course_test/master/pom.xml",
                        200));
    }

    private static Stream<Callable<String>> noCorrectUrl() {
        return Stream.of(
                new Downloader("https://raw.githubusercontent.com/peterarsentev/course_test/master",
                        200));
    }

    @ParameterizedTest
    @MethodSource("noCorrectUrl")
    public void fileNotShouldUploaded(Downloader downloader) {
        FutureTask<String> future = new FutureTask<>(downloader);
        new Thread(future).start();
        String result = null;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            Assertions.assertTrue(LOGGER.isLoggable(Level.SEVERE));
        }
        Assertions.assertEquals("Файл не загружен", result);
        File file = new File("fileTemp.xml");
        Assertions.assertFalse(file.exists());
    }

    @ParameterizedTest
    @MethodSource("correctUrl")
    public void fileShouldUploaded(Downloader downloader)
            throws ExecutionException, InterruptedException {
        FutureTask<String> future = new FutureTask<>(downloader);
        new Thread(future).start();
        String result = future.get();
        Assertions.assertEquals("Файл загружен", result);
        File file = new File("fileTemp.xml");
        Assertions.assertTrue(file.exists());
        file.delete();
    }
}