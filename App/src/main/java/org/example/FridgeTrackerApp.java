package org.example;

import java.util.List;
import java.util.Scanner;

public class FridgeTrackerApp {

    public static void main(String[] args) {
        Welcome w = new Welcome();
        w.printWelcome();

        Ingrediant ingrediant = new Ingrediant();
        RecipeAPIClient recipeAPIClient = new RecipeAPIClient();
        SeasonalTips seasonalTips = new SeasonalTips(); 
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
                case 1 -> ingrediant.addIngredient(scanner);
                case 2 -> {
                    List<String> ingredientList = recipeAPIClient.getIngredientsFromCSV("ingredients.csv");
                    if (!ingredientList.isEmpty()) {
                        recipeAPIClient.suggestRecipes(ingredientList);
                    } else {
                        System.out.println("No ingredients found to suggest recipes.");
                    }
                }
                case 5 -> ingrediant.removeExpiredItems(scanner);
                case 6 -> seasonalTips.displaySeasonalTips(); 
                case 8 -> ingrediant.viewAllIngredients();
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
        System.out.println("1. Add Ingredient");
        System.out.println("2. Suggest Recipes");
        System.out.println("3. Filter Ingredients by Category");
        System.out.println("4. Show Nutritional Summary");
        System.out.println("5. Remove Expired Items");
        System.out.println("6. Display Seasonal Health Tips");
        System.out.println("7. Update Ingredient Quantity");
        System.out.println("8. View All Ingredients");
        System.out.println("9. Show Waste Log");
        System.out.println("0. Exit");
        System.out.println("==========================\n");
    }
}
