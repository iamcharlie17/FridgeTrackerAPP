package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ingrediant {
    private static final String FILE_PATH = "ingredients.csv";
    private final CategoryManager categoryManager; 

    // Constructor to initialize CategoryManager
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

        int categoryIndex = -1; 

        if (categoryInput.equalsIgnoreCase("new")) {
            // Add new category
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
                String selectedCategory = categories.get(categoryIndex);
                System.out.println("Selected category: " + selectedCategory);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select a valid category.");
                return;
            }
        }

        int quantity = 0;
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
        System.out.println();
        System.out.println();
        System.out.println("                         List of All Ingredients                             ");
        System.out.println("------------------------------------------------------------------------------");
        List<String> ingredients = getAllIngredients();

        // Print table header
        System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", "Ingredient", "Category", "Quantity",
                "Expiration Date");
        System.out.println("------------------------------------------------------------------------------");

        int size = ingredients.size();
        if (size > 0) {
            for (String ingredient : ingredients) {
                String[] details = ingredient.split(",");
                if (details.length == 4) {
                    System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", details[0], details[1], details[2],
                            details[3]);
                }
            }
        } else {
            System.out.println("|                        ------ Empty ------                            |");
        }

        System.out.println("------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
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
}
