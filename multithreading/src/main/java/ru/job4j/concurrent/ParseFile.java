package ru.job4j.concurrent;

import java.io.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ParseFile {
    final private File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getContent() {
        return inputContent(c -> true);
    }

    public String getContentWithoutUnicode() {
        return inputContent(c -> c < 0x80);
    }

    private synchronized String inputContent(Predicate<Integer> filter) {
        try (BufferedReader i = new BufferedReader(new FileReader(file))) {
            return i.lines()
                    .flatMapToInt(String::chars)
                    .filter(filter::test)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void saveContent(String content) {
        try (OutputStream o = new FileOutputStream(file)) {
            o.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}