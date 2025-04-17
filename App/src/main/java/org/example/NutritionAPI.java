package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;


public class NutritionAPI {

    private static final String APP_ID = "1de4f09a";
    private static final String APP_KEY = "7e571b3a590b8ee8d4b42786433ae026";
    private static final String API_URL = "https://api.edamam.com/api/nutrition-details?app_id=" + APP_ID + "&app_key=" + APP_KEY;

    public void showNutritionalSummaryViaAPI() {
        Scanner scanner = new Scanner(System.in);
        List<String> userIngredients = new ArrayList<>();

        System.out.println("Enter ingredients one by one (type 'done' to finish):");

        while (true) {
            System.out.print("Ingredient: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            if (!input.trim().isEmpty()) {
                userIngredients.add(input.trim());
            } else {
                System.out.println("Empty input skipped.");
            }
        }

        if (!userIngredients.isEmpty()) {
            try {
                String jsonResponse = sendPostRequest(userIngredients);
                fetchNutritionSummary(jsonResponse);
            } catch (IOException | InterruptedException e) {
                System.out.println("❌ Error fetching nutrition: " + e.getMessage());
            }
        } else {
            System.out.println("❌ No valid ingredients entered.");
        }
    }

    private String sendPostRequest(List<String> ingredients) throws IOException, InterruptedException {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "User Ingredients");
        requestBody.put("ingr", ingredients);

        ObjectMapper mapper = new ObjectMapper();
        String requestBodyJson = mapper.writeValueAsString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    private void fetchNutritionSummary(String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(jsonResponse, Map.class);

            List<Map<String, Object>> ingredientsList = (List<Map<String, Object>>) map.get("ingredients");

            if (ingredientsList != null && !ingredientsList.isEmpty()) {
                System.out.println("\n====== API Nutrition Summary ======");
                System.out.printf("%-20s %-15s %-10s%n", "Nutrient", "Quantity", "Unit");
                System.out.println("-----------------------------------------------------");

                for (Map<String, Object> ingredient : ingredientsList) {
                    List<Map<String, Object>> parsedList = (List<Map<String, Object>>) ingredient.get("parsed");

                    if (parsedList != null && !parsedList.isEmpty()) {
                        Map<String, Object> parsed = parsedList.get(0);
                        Map<String, Map<String, Object>> nutrients = (Map<String, Map<String, Object>>) parsed.get("nutrients");

                        if (nutrients != null) {
                            printNutrient(nutrients, "ENERC_KCAL", "Calories");
                            printNutrient(nutrients, "FAT", "Fat");
                            printNutrient(nutrients, "CHOCDF", "Carbs");
                            printNutrient(nutrients, "FIBTG", "Fiber");
                            printNutrient(nutrients, "SUGAR", "Sugars");
                            printNutrient(nutrients, "PROCNT", "Protein");
                            printNutrient(nutrients, "CHOLE", "Cholesterol");
                            printNutrient(nutrients, "NA", "Sodium");
                            System.out.println("-----------------------------------------------------");
                        } else {
                            System.out.println("❌ No nutrients data found for one of the ingredients.");
                        }
                    }
                }
            } else {
                System.out.println("❌ No ingredients found in API response.");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to parse nutritional data: " + e.getMessage());
        }
    }

    private void printNutrient(Map<String, Map<String, Object>> nutrients, String key, String label) {
        if (nutrients.containsKey(key)) {
            Map<String, Object> nutrient = nutrients.get(key);
            double quantity = ((Number) nutrient.get("quantity")).doubleValue();
            String unit = (String) nutrient.get("unit");
            System.out.printf("%-20s %-15.2f %-10s%n", label, quantity, unit);
        }
    }
}
