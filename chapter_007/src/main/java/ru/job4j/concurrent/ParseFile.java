package ru.job4j.concurrent;

import java.io.*;

public class ParseFile {
    final private File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getContent() {
        StringBuilder output = new StringBuilder();
        try (InputStream i = new FileInputStream(file)) {
            int data;
            while ((data = i.read()) > 0) {
                output.append((char) data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public String getContentWithoutUnicode() {
        StringBuilder output = new StringBuilder();
        try (InputStream i = new FileInputStream(file)) {
            int data;
            while ((data = i.read()) > 0) {
                if (data < 0x80) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public void saveContent(String content) {
        try (OutputStream o = new FileOutputStream(file)) {
            o.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}