package org.example;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Displays seasonal health tips based on the current date.
 */
public class SeasonalTips {

    private final Map<String, List<String>> seasonalTips = new HashMap<>();
    private final Random random = new Random();

    public SeasonalTips() {
        seasonalTips.put("Winter", List.of(
            "Consume more citrus fruits to boost immunity.",
            "Stay warm and drink herbal teas.",
            "Include nuts and seeds for energy.",
            "Take vitamin D supplements if sunlight is limited.",
            "Avoid cold drinks and prioritize warm soups."
        ));

        seasonalTips.put("Summer", List.of(
            "Stay hydrated by drinking lots of water.",
            "Eat fruits like cucumbers and melons.",
            "Avoid heavy meals during the day.",
            "Use sunscreen when going outside.",
            "Consume electrolytes to replenish minerals."
        ));

        seasonalTips.put("Spring", List.of(
            "Detox your body with leafy greens.",
            "Include berries for antioxidants.",
            "Get more sunlight and vitamin D.",
            "Add fresh herbs like mint and basil to meals.",
            "Start a light workout to boost your mood."
        ));

        seasonalTips.put("Autumn", List.of(
            "Boost immunity with soups and stews.",
            "Eat seasonal fruits like apples and pears.",
            "Get enough sleep to prepare for colder months.",
            "Stay active with light indoor exercises.",
            "Incorporate root vegetables into your meals."
        ));
    }


    public void displaySeasonalTips() {
        final String YELLOW = "\u001B[33m";
        final String RESET = "\u001B[0m";

        String season = getCurrentSeason();
        List<String> tips = seasonalTips.get(season);

        if (tips != null && !tips.isEmpty()) {
            String randomTip = tips.get(random.nextInt(tips.size()));
            System.out.println("\n📅 Season: " + season);
            System.out.println("💡 Health Tip for " + season + ":");
            System.out.println("→ " + YELLOW + randomTip + RESET + "\n");
        } else {
            System.out.println("⚠️ No tips available for the current season.");
        }
    }


    private String getCurrentSeason() {
        Month currentMonth = LocalDate.now().getMonth();

        // Month currentMonth = Month.DECEMBER;
        
        return switch (currentMonth) {
            case DECEMBER, JANUARY, FEBRUARY -> "Winter";
            case MARCH, APRIL, MAY -> "Spring";
            case JUNE, JULY, AUGUST -> "Summer";
            case SEPTEMBER, OCTOBER, NOVEMBER -> "Autumn";
        };
    }
}
