package org.example;

import java.util.Scanner;

public class Console {
    // verschiedene Funktionen (z.B. displayMessage, displayErrorMessage, displaySuccessMessage)
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BOLD = "\u001B[1m";

    Scanner scanner = new Scanner(System.in);

    public void displayMessage(String message) {
        System.out.println(ANSI_PURPLE + message + ANSI_RESET);
    }

    public void displayErrorMessage(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    public void displaySuccessMessage(String message) {
        displayBox(message, ANSI_GREEN);
    }

    public void displayGameOverMessage(String message) {
        displayBox(message, ANSI_RED);
    }

    private void displayBox(String message, String color) {
        String upperMessage = message.toUpperCase();
        String[] lines = upperMessage.split("\n");
        int maxLength = getMaxLineLength(lines);

        System.out.println(color + ANSI_BOLD + "+-" + "-".repeat(maxLength) + "-+");

        for (String line : lines) {
            System.out.printf("| %-"+ maxLength +"s |\n", line);
        }

        System.out.println("+-" + "-".repeat(maxLength) + "-+" + ANSI_RESET);
    }

    private int getMaxLineLength(String[] lines) {
        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }
        return maxLength;
    }

    public record UserInput(String verb, String object) {}

    public UserInput getInput() {
        String input = scanner.nextLine();
        return proccesInput(input);
    }

    private UserInput proccesInput(String input) {
        String[] parts = input.split(" ");
        String verb = parts[0];
        String object = parts.length > 1 ? parts[1] : "";
        return new UserInput(verb, object);
    }
}
