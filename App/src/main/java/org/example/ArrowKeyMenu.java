package org.example;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class ArrowKeyMenu {
    public static void main(String[] args) throws Exception {
        Terminal terminal = TerminalBuilder.terminal();
        terminal.enterRawMode();
        var writer = terminal.writer();

        String[] options = {
            "Add Ingredient",
            "Suggest Recipes",
            "View All Ingredients",
            "Exit"
        };

        int selected = 0;
        boolean running = true;

        while (running) {
            // Clear screen
            writer.print("\033[H\033[2J");
            writer.flush();

            writer.println("===== MENU =====");
            for (int i = 0; i < options.length; i++) {
                if (i == selected) {
                    writer.println("-> " + options[i]);
                } else {
                    writer.println("   " + options[i]);
                }
            }

            writer.flush();

            int key = terminal.reader().read();

            switch (key) {
                case 65: // UP Arrow
                    selected = (selected - 1 + options.length) % options.length;
                    break;
                case 66: // DOWN Arrow
                    selected = (selected + 1) % options.length;
                    break;
                case 10, 13: // ENTER key
                    writer.println("\nYou selected: " + options[selected]);
                    writer.flush();
                    Thread.sleep(500); // brief pause
            
                    switch (selected) {
                        case 0 -> writer.println("Adding Ingredient...");
                        case 1 -> writer.println("Suggesting Recipes...");
                        case 2 -> writer.println("Viewing Ingredients...");
                        case 3 -> {
                            writer.println("Exiting...");
                            running = false;
                        }
                    }
                    writer.flush();
                    Thread.sleep(1000);
                    break;
            }
        }

        terminal.close();
    }
}
