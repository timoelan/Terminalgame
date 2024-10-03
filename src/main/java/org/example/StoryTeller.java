package org.example;

import java.util.Map;
import java.util.Scanner;

public class StoryTeller {

    // currentRoomID speichern damit ich weiss welcher Raum ich momentan bin.
    String currentRoomID = null;

    // das ist dre Map string wie aus dem yaml file. Hier sieht man Game.Room> rooms. Das sind alle Rooms drin.
    StoryReader.Game game;

    // Constructor  hier übergebe ich die Map Rooms dem Constructor und die ID des jetztigem Room ID


    public StoryTeller(StoryReader.Game game) {
        this.game = game;
        this.currentRoomID = game.startRoom();
    }

    public void startGame(){
        System.out.println(game.description());
        Scanner scanner = new Scanner(System.in);

        while (true){
            StoryReader.Game.Room currentRoom = game.rooms().get(currentRoomID);
            System.out.println(currentRoom.description());

            System.out.print(currentRoom.name() + "  >> ");
            String input = scanner.nextLine();

            ProccesInput(input, currentRoom);

        }


    }
    private void ProccesInput(String input, StoryReader.Game.Room room){
        String[] parts = input.split(" ");

        if (parts.length < 2){
            System.out.println("Ungültiger Befehl. Bitte <verb object> verwenden.");
            return;
        }
        String verb = parts[0];
        String object = parts[1];

        if (!room.verbs().containsKey(verb)) {
            System.out.println("Du kannst das nicht tun.");
            return;
        }

    }




}
