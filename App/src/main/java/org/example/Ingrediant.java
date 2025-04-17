package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Ingrediant {
    private static final String FILE_PATH = "ingredients.csv";
    private static final String WASTE_LOG_PATH = "waste_log.csv";
    private final CategoryManager categoryManager;

    public Ingrediant() {
        this.categoryManager = new CategoryManager();
    }

    public void addIngredient(Scanner scanner) {
        System.out.print("Enter ingredient name: ");
        String name = scanner.nextLine();

        List<String> categories = categoryManager.readCategoriesFromCSV();
        System.out.println("Select a category (or type 'new' to add a new category):");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }

        String categoryInput = scanner.nextLine();
        int categoryIndex;

        if (categoryInput.equalsIgnoreCase("new")) {
            System.out.print("Enter new category name: ");
            String newCategory = scanner.nextLine();
            categories.add(newCategory);
            categoryManager.writeCategoryToCSV(newCategory);
            System.out.println("New category added: " + newCategory);
            categoryIndex = categories.size() - 1;
        } else {
            try {
                categoryIndex = Integer.parseInt(categoryInput) - 1;
                if (categoryIndex < 0 || categoryIndex >= categories.size()) {
                    System.out.println("Invalid category selection.");
                    return;
                }
                System.out.println("Selected category: " + categories.get(categoryIndex));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select a valid category.");
                return;
            }
        }

        int quantity;
        while (true) {
            System.out.print("Enter quantity (gm or ml): ");
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity < 1) {
                    System.out.println("Quantity must be a positive number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a valid number.");
            }
        }

        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String expirationDate = scanner.nextLine();

        String ingredientData = name + "," + categories.get(categoryIndex) + "," + quantity + "," + expirationDate + "\n";
        saveIngredientToFile(ingredientData);
    }

    public void viewAllIngredients() {
        System.out.println("\n\n                         List of All Ingredients                             ");
        System.out.println("------------------------------------------------------------------------------");
        List<String> ingredients = getAllIngredients();

        System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", "Ingredient", "Category", "Quantity", "Expiration Date");
        System.out.println("------------------------------------------------------------------------------");

        if (!ingredients.isEmpty()) {
            for (String ingredient : ingredients) {
                String[] details = ingredient.split(",");
                if (details.length == 4) {
                    System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", details[0], details[1], details[2], details[3]);
                }
            }
        } else {
            System.out.println("|                        ------ Empty ------                            |");
        }

        System.out.println("------------------------------------------------------------------------------\n\n");
    }

    private List<String> getAllIngredients() {
        List<String> ingredients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ingredients.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading ingredients: " + e.getMessage());
        }
        return ingredients;
    }

    private void saveIngredientToFile(String ingredientData) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(ingredientData);
            System.out.println("Ingredient added successfully!");
        } catch (IOException e) {
            System.out.println("Error saving ingredient: " + e.getMessage());
        }
    }

    // ✅ FEATURE 5: Remove Expired Items and Log Them
    public void removeExpiredItems(Scanner scanner) {
        List<String> allIngredients = getAllIngredients();
        List<String> expiredIngredients = new ArrayList<>();
        List<Integer> expiredIndexes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate today = LocalDate.now();

        // Find expired ingredients
        for (int i = 0; i < allIngredients.size(); i++) {
            String[] parts = allIngredients.get(i).split(",");
            if (parts.length == 4) {
                try {
                    LocalDate expiryDate = LocalDate.parse(parts[3], formatter);
                    if (expiryDate.isBefore(today)) {
                        expiredIngredients.add(allIngredients.get(i));
                        expiredIndexes.add(i);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid date format in record: " + allIngredients.get(i));
                }
            }
        }

        if (expiredIngredients.isEmpty()) {
            System.out.println("No expired ingredients found.");
            return;
        }

        System.out.println("\nExpired Ingredients:");
        for (int i = 0; i < expiredIngredients.size(); i++) {
            String[] parts = expiredIngredients.get(i).split(",");
            System.out.printf("%d. %s (Category: %s, Quantity: %s, Expired On: %s)\n",
                    i + 1, parts[0], parts[1], parts[2], parts[3]);
        }

        System.out.print("\nEnter index of ingredient to remove (comma separated for multiple): ");
        String input = scanner.nextLine();
        String[] tokens = input.split(",");

        Set<Integer> indexesToRemove = new HashSet<>();

        try {
            for (String token : tokens) {
                int idx = Integer.parseInt(token.trim()) - 1;
                if (idx >= 0 && idx < expiredIngredients.size()) {
                    indexesToRemove.add(expiredIndexes.get(idx));
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        // Write updated ingredient list (excluding removed ones)
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (int i = 0; i < allIngredients.size(); i++) {
                if (!indexesToRemove.contains(i)) {
                    writer.write(allIngredients.get(i) + "\n");
                } else {
                    logWasteItem(allIngredients.get(i));
                }
            }
            System.out.println("Selected expired items removed and logged to waste log.");
        } catch (IOException e) {
            System.out.println("Error updating ingredient file: " + e.getMessage());
        }
    }

    private void logWasteItem(String ingredientLine) {
        try (FileWriter wasteWriter = new FileWriter(WASTE_LOG_PATH, true)) {
            wasteWriter.write(ingredientLine + "\n");
        } catch (IOException e) {
            System.out.println("Error logging waste item: " + e.getMessage());
        }
    }
}
