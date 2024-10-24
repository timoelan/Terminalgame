package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StoryReader {
    @JsonIgnoreProperties(ignoreUnknown = true)
    record Game(String description, Map<String, Room> rooms, String startRoom, Map<String, Verb> verbs) {
        record Room(String name, String description, Map<String, Map<String, List<Action>>> verbs) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            record Action(String room, String message, String ifState, String addState) {}
        }
        @JsonIgnoreProperties(ignoreUnknown = true)
        record Verb(ErrorAction errors, List<String> synonyms){}
        @JsonIgnoreProperties(ignoreUnknown = true)
        record ErrorAction(String verb, String object){}
    }

    record GameConfig(String name, String filename, String difficulty) {}

    public static void main(String[] args) throws IOException {
        List<GameConfig> games = Arrays.asList(
                new GameConfig("Tutorial", "easy-tutorial.yaml", "easy"),
                new GameConfig("Cursed Foyer", "hard-cursed-foyer.yaml", "hard")
        );

        Scanner scanner = new Scanner(System.in);
        System.out.println("Wählen Sie die Schwierigkeit: (easy/hard)");
        String difficulty = scanner.nextLine().trim().toLowerCase();

        // Filter für die spiele
        List<GameConfig> availableGames = new ArrayList<>();
        for (GameConfig game : games) {
            if (game.difficulty.equals(difficulty)) {
                availableGames.add(game);
            }
        }

        // hier zeigt man dan die Spiele an plus die nummer
        System.out.println("Wählen Sie ein Spiel:");
        for (int i = 0; i < availableGames.size(); i++) {
            System.out.println((i + 1) + ". " + availableGames.get(i).name); // Anzeige der Spiele mit nummer
        }


        int chosenIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;

        String yamlFilePath;
        if (chosenIndex >= 0 && chosenIndex < availableGames.size()) {
            yamlFilePath = "src/main/resources/" + availableGames.get(chosenIndex).filename; // Erstellen des Pfads zur YAML-Datei
        } else {
            System.out.println("Ungültige Auswahl. Standardmäßig wird das Tutorial-Spiel geladen.");
            yamlFilePath = "src/main/resources/easy-tutorial.yaml";
        }

        // Laden der YAML-Datei
        byte[] yamlData = Files.readAllBytes(Paths.get(yamlFilePath));
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        Game game = objectMapper.readValue(yamlData, Game.class);
        StoryTeller storyteller = new StoryTeller(game);
        storyteller.startGame();
    }
}