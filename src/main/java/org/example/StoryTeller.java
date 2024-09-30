package org.example;

import java.util.Map;
import java.util.Scanner;

public class StoryTeller {

    // currentRoomID speichern damit ich weiss welcher Raum ich momentan bin.
    String currentRoomID = null;

    // das ist dre Map string wie aus dem yaml file. Hier sieht man Game.Room> rooms. Das sind alle Rooms drin.
    Map<String, StoryReader.Game.Room> rooms;

    // Constructor  hier übergebe ich die Map Rooms dem Constructor und die ID des jetztigem Room ID
    public StoryTeller(Map<String, StoryReader.Game.Room> rooms, String startRoomID) {
        this.rooms = rooms;
        this.currentRoomID = startRoomID;
    }

    public void startGame(){
        Scanner scanner = new Scanner(System.in);

        while (true){
            StoryReader.Game.Room currentRoom = rooms.get(currentRoomID);
            System.out.println(currentRoom.description());

            System.out.print("> ");
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
