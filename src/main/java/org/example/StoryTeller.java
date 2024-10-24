package org.example;
import java.text.MessageFormat;
import java.util.*;
public class StoryTeller {
Console console = new Console();
    String currentRoomID = null;
    StoryReader.Game game;
    Set<String> states = new HashSet<>();

    Set<String> bag = new HashSet<>();
    boolean resentChangedRoom;

    Map<String, String> synonyms = new HashMap<>();

    public StoryTeller(StoryReader.Game game) {
        this.game = game;
        this.currentRoomID = game.startRoom();
    }

    public void restartGame(){
        resetGame();
        startGame();
    }
    public void resetGame() {
        currentRoomID = game.startRoom();
        states.clear();
        bag.clear();
        resentChangedRoom = true;
    }



    public void startGame(){

        console.displayMessage(game.description());



        while (true) {

            StoryReader.Game.Room currentRoom = game.rooms().get(currentRoomID);

            for (String verb : game.verbs().keySet()){
                List<String>synonymList = game.verbs().get(verb).synonyms();
                if (synonymList == null){
                    continue;
                }
                for (String synonym : synonymList) {
                    synonyms.put(synonym ,verb);
                }

            }

            if (resentChangedRoom) {
                console.displayMessage(currentRoom.description());
                resentChangedRoom = false;
            }
            console.displayMessage(currentRoom.name() + "  >> ");


            Console.UserInput parsedInput = console.getInput();

            String verbOrSynonym = parsedInput.verb();
            String verb = synonyms.get(verbOrSynonym);
            if (verb == null){
                verb = verbOrSynonym;
            }
            Map<String, List<StoryReader.Game.Room.Action>> currentVerb = currentRoom.verbs().get(verb);


            if (currentVerb == null) {
                console.displayErrorMessage(getVerbErrorMessage(verb));
                continue;
            }




            boolean runNextAction = true;
            String object = parsedInput.object();
            List<StoryReader.Game.Room.Action> currentActions = currentVerb.get(object);
            if (currentActions == null) {
                console.displayMessage(currentVerb.keySet().toString());
                console.displayErrorMessage(MessageFormat.format(game.verbs().get(verb).errors().object(), object));

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
                    console.displayMessage(message);
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


            if (bag.contains("gameOver")){
                console.displayGameOverMessage("GAME OVER");
                restartGame();

            }
            if (bag.contains("youWon")){
                console.displaySuccessMessage("You Won!!");
                restartGame();
            }
        }

    }

    private String getVerbErrorMessage(String verb) {
        String verbErrorMessage;
        if(!game.verbs().containsKey(verb)){
            verbErrorMessage = game.verbs().get("default").errors().verb();
        }
        else {
           verbErrorMessage = game.verbs().get(verb).errors().verb();
            if (verbErrorMessage == null) {
                verbErrorMessage = game.verbs().get("default").errors().verb();
            }
        }
        return verbErrorMessage;
    }


}
