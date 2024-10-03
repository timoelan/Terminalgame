package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class StoryReader {
    @JsonIgnoreProperties(ignoreUnknown = true)
    record Game(String description, Map<String, Room > rooms, String startRoom) {
        record Room(String name, String description, Map <String, Map <String, List<Action>>> verbs) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            record Action(String room, String message) {}
        }
    }
    public static void main(String[] args) throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get("src\\main\\resources\\game-confing.yaml"));
        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        Game game = objectMapper.readValue(jsonData, Game.class);

        // StoryTeller initialisieren und das Spiel starten, beginnend mit "room-one"
        StoryTeller storyteller = new StoryTeller(game);
        storyteller.startGame();
    }


}

