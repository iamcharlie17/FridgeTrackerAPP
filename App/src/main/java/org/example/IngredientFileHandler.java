package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IngredientFileHandler {
    private static final String FILE_PATH = "ingredients.csv";

    public void saveIngredient(String data) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(data + "\n");
            System.out.println("\nIngredient added successfully!\n");
        } catch (IOException e) {
            System.out.println("Error saving ingredient: " + e.getMessage());
        }
    }

    public List<String> readIngredients() {
        return readFile(FILE_PATH);
    }

    public List<String> readFile(String filePath) {
        List<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) list.add(line);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return list;
    }

    public void writeIngredients(List<String> data) {
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            for (String line : data) writer.write(line + "\n");
        } catch (IOException e) {
            System.out.println("Error writing ingredients: " + e.getMessage());
        }
    }

    public void clearFile(String path) {
        try (FileWriter writer = new FileWriter(path, false)) {
            // Clear file
        } catch (IOException e) {
            System.out.println("Error clearing file: " + e.getMessage());
        }
    }

    public void displayIngredients(List<String> ingredients) {
        displayIngredients(ingredients, "List of All Ingredients");
    }

    public void displayIngredients(List<String> ingredients, String title) {
        System.out.println("\n" + title);
        System.out.println("------------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", "Ingredient", "Category", "Quantity", "Expiration Date");
        System.out.println("------------------------------------------------------------------------------");

        if (ingredients.isEmpty()) {
            System.out.println("|                        ------ Empty ------                            |");
        } else {
            for (String line : ingredients) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", details[0], details[1], details[2], details[3]);
                }
            }
        }

        System.out.println("------------------------------------------------------------------------------\n");
    }

    public void updateQuantity(Scanner scanner, List<String> lines) {
        if (lines.isEmpty()) {
            System.out.println("\nNo ingredients found.\n");
            return;
        }

        for (int i = 0; i < lines.size(); i++) {
            String[] details = lines.get(i).split(",");
            if (details.length >= 3) {
                System.out.printf("%d. %s (Quantity: %s ml/gram)\n", i + 1, details[0], details[2]);
            }
        }

        System.out.print("\nSelect an ingredient number to update: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= lines.size()) {
                System.out.println("\nInvalid selection.\n");
                return;
            }

            String[] details = lines.get(index).split(",");
            System.out.printf("Current quantity of '%s' is %s ml/gram\n", details[0], details[2]);
            System.out.print("Enter new quantity ml/gram (0 to delete): ");
            int newQty = Integer.parseInt(scanner.nextLine());

            if (newQty < 0) {
                System.out.println("Quantity must be positive.");
                return;
            }

            if (newQty == 0) {
                lines.remove(index);
                System.out.println("Ingredient deleted successfully.");
            } else {
                details[2] = String.valueOf(newQty);
                lines.set(index, String.join(",", details));
                System.out.println("Quantity updated successfully.");
            }

            writeIngredients(lines);

        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    public void filterByCategory(Scanner scanner, List<String> ingredients, List<String> categories) {
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        System.out.print("Select a category: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= categories.size()) {
                System.out.println("Invalid category selection.");
                return;
            }

            String selected = categories.get(index);
            List<String> filtered = new ArrayList<>();
            for (String ingredient : ingredients) {
                String[] details = ingredient.split(",");
                if (details.length == 4 && details[1].equalsIgnoreCase(selected)) {
                    filtered.add(ingredient);
                }
            }

            displayIngredients(filtered, "Ingredients in Category: " + selected);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }
}
