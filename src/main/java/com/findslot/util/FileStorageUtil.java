package com.findslot.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageUtil {

    @SuppressWarnings("unchecked")
    public static <T> List<T> readFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading from file " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> void writeToFile(String filename, List<T> objects) {
        // Ensure directory exists
        File file = new File(filename);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            oos.writeObject(objects);
        } catch (IOException e) {
            System.err.println("Error writing to file " + filename + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading object from file " + filename + ": " + e.getMessage());
            return null;
        }
    }

    public static <T> void writeObjectToFile(String filename, T object) {
        // Ensure directory exists
        File file = new File(filename);
        file.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file))) {
            oos.writeObject(object);
        } catch (IOException e) {
            System.err.println("Error writing object to file " + filename + ": " + e.getMessage());
        }
    }
}