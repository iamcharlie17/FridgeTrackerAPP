package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeAPIClient {

    private static final String API_KEY = "c34f4d9421b5424283f48d1fdf94c423"; 
    private static final String BASE_URL = "https://api.spoonacular.com/recipes/findByIngredients";

    public void suggestRecipes(List<String> ingredients) {
        OkHttpClient client = new OkHttpClient();
    
        String joinedIngredients = String.join(",", ingredients);
        HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("ingredients", joinedIngredients)
                .addQueryParameter("number", "5")
                .addQueryParameter("apiKey", API_KEY)
                .build();
    
        Request request = new Request.Builder().url(url).build();
    
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("API request failed: " + response.code());
                return;
            }
    
            String responseData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(responseData).getAsJsonArray();
    
            if (jsonArray.size() == 0) {
                System.out.println("No recipes found for your ingredients.");
                return;
            }
    
            System.out.println("\nSuggested Recipes:");
            for (JsonElement element : jsonArray) {
    
                String title = element.getAsJsonObject().get("title").getAsString();
                JsonArray usedIngredients = element.getAsJsonObject().getAsJsonArray("usedIngredients");
                JsonArray missedIngredients = element.getAsJsonObject().getAsJsonArray("missedIngredients");
    
                int boxWidth = 62;
    
                String border = "+--------------------------------------------------------------+";
                String recipeTitle = "| Recipe: " + String.format("%-53s", title) + "|";
                String usedHeader = "| Used Ingredients:                                            |";
                String missedHeader = "| Missed Ingredients:                                          |";
    
                System.out.println(border);
                System.out.println(recipeTitle);
                System.out.println(border);
                System.out.println(usedHeader);
    
                for (JsonElement usedElement : usedIngredients) {
                    String ingredient = usedElement.getAsJsonObject().get("name").getAsString();
                    System.out.println("|   - " + String.format("%-55s", ingredient) + "  |");
                }
    
                System.out.println(missedHeader);
                for (JsonElement missedElement : missedIngredients) {
                    String ingredient = missedElement.getAsJsonObject().get("name").getAsString();
                    System.out.println("|   - " + String.format("%-55s", ingredient) + "  |");
                }
    
                System.out.println(border);
                System.out.println();
            }
    
        } catch (IOException e) {
            System.out.println("Error fetching recipes: " + e.getMessage());
        }
    }

    


    public List<String> getIngredientsFromCSV(String filePath) {
        List<String> ingredients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0 && !parts[0].isBlank()) {
                    ingredients.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading ingredient file: " + e.getMessage());
        }
        return ingredients;
    }
}
