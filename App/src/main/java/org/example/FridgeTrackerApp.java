package org.example;

import java.util.List;
import java.util.Scanner;

public class FridgeTrackerApp {

    public static void main(String[] args) {
        Welcome w = new Welcome();
        w.printWelcome();

        IngredientManager ingredientManager = new IngredientManager(
                new CategoryManager(),
                new IngredientFileHandler(),
                new WasteManager(new IngredientFileHandler()));

        RecipeAPIClient recipeAPIClient = new RecipeAPIClient();
        SeasonalTips seasonalTips = new SeasonalTips();
        NutritionAPI nutritionAPI = new NutritionAPI();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printMenu();

        while (running) {
            System.out.print("Choose an option (or type 'menu' to see options): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("menu")) {
                printMenu();
                continue;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 2 -> ingredientManager.addIngredient(scanner);
                case 6 -> {
                    List<String> ingredientList = recipeAPIClient.getIngredientsFromCSV("ingredients.csv");
                    if (!ingredientList.isEmpty()) {
                        recipeAPIClient.suggestRecipes(ingredientList);
                    } else {
                        System.out.println("No ingredients found to suggest recipes.");
                    }
                }
                case 5 -> ingredientManager.filterIngredientsByCategory(scanner);
                case 7 -> {
                    nutritionAPI.showNutritionalSummaryViaAPI();
                }
                case 8 -> ingredientManager.removeExpiredIngredients();
                case 1 -> seasonalTips.displaySeasonalTips();
                case 3 -> ingredientManager.updateIngredientQuantity(scanner);
                case 4 -> ingredientManager.viewAllIngredients();
                case 9 -> ingredientManager.showWasteLog();
                case 10 -> ingredientManager.clearWasteLog();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========== MENU ==========");
        System.out.println("1. Display Seasonal Health Tips");
        System.out.println("2. Add Ingredient");
        System.out.println("3. Update Ingredient Quantity");
        System.out.println("4. View All Ingredients");
        System.out.println("5. Filter Ingredients by Category");
        System.out.println("6. Suggest Recipes");
        System.out.println("7. Nutrition Calculator");
        System.out.println("8. Remove Expired Items");
        System.out.println("9. Show Waste Log");
        System.out.println("10. Clear Waste Log");
        System.out.println("0. Exit");
        System.out.println("==========================\n");
    }
}
