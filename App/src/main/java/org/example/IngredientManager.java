package org.example;

import java.util.List;
import java.util.Scanner;

public class IngredientManager {
    private final CategoryManager categoryManager;
    private final IngredientFileHandler fileHandler;
    private final WasteManager wasteManager;

    public IngredientManager(CategoryManager categoryManager, IngredientFileHandler fileHandler, WasteManager wasteManager) {
        this.categoryManager = categoryManager;
        this.fileHandler = fileHandler;
        this.wasteManager = wasteManager;
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
            categoryManager.writeCategoryToCSV(newCategory);
            categories.add(newCategory);
            categoryIndex = categories.size() - 1;
        } else {
            try {
                categoryIndex = Integer.parseInt(categoryInput) - 1;
                if (categoryIndex < 0 || categoryIndex >= categories.size()) {
                    System.out.println("Invalid category selection.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select a valid category.");
                return;
            }
        }

        int quantity = 0;
        while (quantity <= 0) {
            System.out.print("Enter quantity (gm or ml): ");
            try {
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity <= 0) System.out.println("Quantity must be a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity.");
            }
        }

        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String expirationDate = scanner.nextLine();

        String ingredientData = name + "," + categories.get(categoryIndex) + "," + quantity + "," + expirationDate;
        fileHandler.saveIngredient(ingredientData);
    }

    public void viewAllIngredients() {
        fileHandler.displayIngredients(fileHandler.readIngredients());
    }

    public void removeExpiredIngredients() {
        List<String> allIngredients = fileHandler.readIngredients();
        wasteManager.handleExpiredIngredients(allIngredients);
    }

    public void updateIngredientQuantity(Scanner scanner) {
        List<String> ingredients = fileHandler.readIngredients();
        fileHandler.updateQuantity(scanner, ingredients);
    }

    public void filterIngredientsByCategory(Scanner scanner) {
        List<String> allIngredients = fileHandler.readIngredients();
        fileHandler.filterByCategory(scanner, allIngredients, categoryManager.readCategoriesFromCSV());
    }

    public void showWasteLog() {
        wasteManager.showWasteLog();
    }
    
    public void clearWasteLog() {
        wasteManager.clearWasteLog();
    }
}
