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
        return inputContent(false).toString();
    }

    public String getContentWithoutUnicode() {
        return inputContent(true).toString();
    }

    private synchronized StringBuilder inputContent(boolean mode) {
        StringBuilder output = new StringBuilder();
        try (InputStream i = new FileInputStream(file)) {
            int data;
            while ((data = i.read()) > 0) {
                if (mode && data < 0x80) {
                    output.append((char) data);
                } else if (!mode) {
                    output.append((char) data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void saveContent(String content) {
        try (OutputStream o = new FileOutputStream(file)) {
            o.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}