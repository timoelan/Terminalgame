package org.example;
import java.util.*;
public class StoryTeller {
    String currentRoomID = null;
    StoryReader.Game game;
    Set<String> states = new HashSet<>();
    Set<String> bag = new HashSet<>();
    boolean resentChangedRoom;

    public StoryTeller(StoryReader.Game game) {
        this.game = game;
        this.currentRoomID = game.startRoom();
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    public void startGame(){

        System.out.println(ANSI_GREEN + game.description() + ANSI_RESET);
        Scanner scanner = new Scanner(System.in);


        while (true) {

            StoryReader.Game.Room currentRoom = game.rooms().get(currentRoomID);
            if (resentChangedRoom) {
                System.out.println(currentRoom.description());
                resentChangedRoom = false;
            }
            System.out.print(currentRoom.name() + "  >> ");
            String input = scanner.nextLine();

            String[] parsedInput = ProccesInput(input, currentRoom);

            String verb = parsedInput[0];
            Map<String, List<StoryReader.Game.Room.Action>> currentVerb = currentRoom.verbs().get(verb);

            if (currentVerb == null) {
                System.out.println("That Verb doesn't exist");
                continue;
            }

            boolean runNextAction = true;
            String object = parsedInput[1];
            List<StoryReader.Game.Room.Action> currentActions = currentVerb.get(object);
            if (currentActions == null) {
                System.out.println(currentVerb.keySet());
                continue;
            }

            for (StoryReader.Game.Room.Action activeAction : currentActions) {
                if (!runNextAction) break;
                String ifState = activeAction.ifState();
                if (ifState != null) {
                    if (!bag.contains(ifState)) {
                        continue;
                    }
                }

                String message = activeAction.message();
                if (message != null) {
                    System.out.println(message);
                }

                if (activeAction.room() != null){
                    currentRoomID = activeAction.room();
                    resentChangedRoom = true;
                }

                String addState = activeAction.addState();
                if (addState != null) {
                    if (!bag.contains(addState)) {
                        bag.add(addState);
                    }
                }
                runNextAction = false;
            }
        }
    }
    private String[] ProccesInput(String input, StoryReader.Game.Room room) {
        String[] parts = input.split(" ");
        String verb = parts[0];
        String object = parts.length > 1 ? parts[1] : "";
        return new String[]{verb, object};
    }


}
