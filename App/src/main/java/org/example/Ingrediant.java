package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        int categoryIndex = -1;

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

        System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", "Ingredient", "Category", "Quantity", "Expiration Date");
        System.out.println("------------------------------------------------------------------------------");

        int size = ingredients.size();
        if (size > 0) {
            for (String ingredient : ingredients) {
                String[] details = ingredient.split(",");
                if (details.length == 4) {
                    System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", details[0], details[1], details[2], details[3]);
                }
            }
        } else {
            System.out.println("|                        ------ Empty ------                            |");
        }

        System.out.println("------------------------------------------------------------------------------");
        System.out.println();
        System.out.println();
    }

    public void removeExpiredIngredients() {
        List<String> updatedIngredients = new ArrayList<>();
        List<String> expiredIngredients = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    String expiry = details[3];
                    try {
                        LocalDate expirationDate = LocalDate.parse(expiry);
                        if (expirationDate.isBefore(LocalDate.now())) {
                            expiredIngredients.add(line);
                        } else {
                            updatedIngredients.add(line);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid date format for: " + line);
                        updatedIngredients.add(line);
                    }
                }
            }

            try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
                for (String ingredient : updatedIngredients) {
                    writer.write(ingredient + "\n");
                }
            }


            try (FileWriter wasteWriter = new FileWriter(WASTE_LOG_PATH, true)) {
                for (String waste : expiredIngredients) {
                    wasteWriter.write(waste + "\n");
                }
            }
            System.out.println();
            System.out.println();
            System.out.println("Expired ingredients removed and logged to waste.");
            System.out.println();
            System.out.println();

        } catch (IOException e) {
            System.out.println("Error removing expired ingredients: " + e.getMessage());
        }
    }

    public void showWasteLog() {
        System.out.println();
        System.out.println("                              Waste Log                                  ");
        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", "Ingredient", "Category", "Quantity", "Expiration Date");
        System.out.println("----------------------------------------------------------------------------");

        try (BufferedReader reader = new BufferedReader(new FileReader(WASTE_LOG_PATH))) {
            String line;
            boolean hasWaste = false;

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 4) {
                    System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", details[0], details[1], details[2], details[3]);
                    hasWaste = true;
                }
            }

            if (!hasWaste) {
                System.out.println("|                        ------ No Waste Yet ------                        |");
            }

        } catch (IOException e) {
            System.out.println("Error reading waste log: " + e.getMessage());
        }

        System.out.println("----------------------------------------------------------------------------");
    }
    
    public void clearWasteLog() {
        try (FileWriter writer = new FileWriter(WASTE_LOG_PATH, false)) {
            // Overwrites the file with nothing, effectively clearing it
        } catch (IOException e) {
            System.out.println("Error clearing waste log: " + e.getMessage());
            return;
        }
    
        System.out.println();
        System.out.println("Waste log has been cleared.");
        System.out.println();
    }

    public void filterIngredientsByCategory(Scanner scanner) {
        List<String> categories = categoryManager.readCategoriesFromCSV();
    
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }
    
        System.out.println("\nAvailable Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i));
        }
    
        System.out.print("Select a category to filter by: ");
        String input = scanner.nextLine();
    
        int categoryIndex;
        try {
            categoryIndex = Integer.parseInt(input) - 1;
            if (categoryIndex < 0 || categoryIndex >= categories.size()) {
                System.out.println("Invalid category selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return;
        }
    
        String selectedCategory = categories.get(categoryIndex);
        List<String> allIngredients = getAllIngredients();
    
        System.out.println();
        System.out.println("                         Ingredients in Category: " + selectedCategory);
        System.out.println("------------------------------------------------------------------------------");
        System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", "Ingredient", "Category", "Quantity", "Expiration Date");
        System.out.println("------------------------------------------------------------------------------");
    
        boolean found = false;
        for (String ingredient : allIngredients) {
            String[] details = ingredient.split(",");
            if (details.length == 4 && details[1].equalsIgnoreCase(selectedCategory)) {
                System.out.printf("| %-20s | %-20s | %-10s | %-15s |\n", details[0], details[1], details[2], details[3]);
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("|           No ingredients found in this category.                           |");
        }
    
        System.out.println("------------------------------------------------------------------------------");
    }



    public void updateIngredientQuantity(Scanner scanner) {
        List<String> lines = new ArrayList<>();
    

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading ingredients file: " + e.getMessage());
            return;
        }
    
        if (lines.isEmpty()) {
            System.out.println();
            System.out.println("No ingredients found.");
            System.out.println();
            return;
        }
    

        System.out.println("\nAvailable Ingredients:");
        for (int i = 0; i < lines.size(); i++) {
            String[] details = lines.get(i).split(",");
            if (details.length >= 3) {
                System.out.printf("%d. %s (Quantity: %s ml/gram)\n", i + 1, details[0], details[2]);
            }
        }
    
        System.out.print("\nSelect an ingredient number to update: ");
        int selectedIndex;
        try {
            selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (selectedIndex < 0 || selectedIndex >= lines.size()) {
                System.out.println();
                System.out.println("Invalid selection.");
                System.out.println();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println();
            System.out.println("Invalid input. Must be a number.");
            System.out.println();
            return;
        }
    

        String[] selectedDetails = lines.get(selectedIndex).split(",");
    
        System.out.printf("Current quantity of '%s' is %s ml/gram\n", selectedDetails[0], selectedDetails[2]);
        System.out.print("Enter new quantity ml/gram (0 to delete): ");
    
        int newQuantity;
        try {
            newQuantity = Integer.parseInt(scanner.nextLine());
            if (newQuantity < 0) {
                System.out.println();
                System.out.println("Quantity must be a positive number.");
                System.out.println();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println();
            System.out.println("Invalid quantity.");
            System.out.println();
            return;
        }
    
        if (newQuantity == 0) {
            lines.remove(selectedIndex);
            System.out.println();
            System.out.println("Ingredient deleted successfully.");
            System.out.println();
        } else {
            selectedDetails[2] = String.valueOf(newQuantity);
            lines.set(selectedIndex, String.join(",", selectedDetails));
            System.out.println();
            System.out.println("Quantity updated successfully.");
            System.out.println();
        }
    
        try (FileWriter writer = new FileWriter(FILE_PATH, false)) {
            for (String updatedLine : lines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
            System.out.println();
            System.out.println("Error writing to file: " + e.getMessage());
            System.out.println();
        }
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
            System.out.println();
            System.out.println("Ingredient added successfully!");
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error saving ingredient: " + e.getMessage());
        }
    }

    
}
