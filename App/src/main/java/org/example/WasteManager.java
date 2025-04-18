package org.example;

import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WasteManager {
    private static final String WASTE_LOG_PATH = "waste_log.csv";
    private final IngredientFileHandler fileHandler;

    public WasteManager(IngredientFileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    public void handleExpiredIngredients(List<String> ingredients) {
        List<String> updated = new ArrayList<>();
        List<String> expired = new ArrayList<>();

        for (String line : ingredients) {
            String[] details = line.split(",");
            if (details.length == 4) {
                try {
                    LocalDate date = LocalDate.parse(details[3]);
                    if (date.isBefore(LocalDate.now())) {
                        expired.add(line);
                    } else {
                        updated.add(line);
                    }
                } catch (Exception e) {
                    updated.add(line);
                }
            }
        }

        fileHandler.writeIngredients(updated);
        logWaste(expired);

        System.out.println("\nExpired ingredients removed and logged to waste.\n");
    }

    public void logWaste(List<String> wasteItems) {
        try (FileWriter writer = new FileWriter(WASTE_LOG_PATH, true)) {
            for (String item : wasteItems) writer.write(item + "\n");
        } catch (Exception e) {
            System.out.println("Error logging waste: " + e.getMessage());
        }
    }

    public void showWasteLog() {
        fileHandler.displayIngredients(fileHandler.readFile(WASTE_LOG_PATH), "Waste Log");
    }

    public void clearWasteLog() {
        fileHandler.clearFile(WASTE_LOG_PATH);
        System.out.println("\nWaste log has been cleared.\n");
    }
}
