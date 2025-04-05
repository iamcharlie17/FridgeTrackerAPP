package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private static final String CATEGORY_FILE = "categories.csv"; 


    public List<String> readCategoriesFromCSV() {
        List<String> categories = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading categories from file: " + e.getMessage());
        }
        return categories;
    }


    public void writeCategoryToCSV(String category) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORY_FILE, true))) {
            writer.write(category);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to category file: " + e.getMessage());
        }
    }
}
