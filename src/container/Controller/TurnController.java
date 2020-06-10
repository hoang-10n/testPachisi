package container.Controller;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import container.Miscs.Computer;
import container.Miscs.Player;
import container.Miscs.Storage;

import java.util.ArrayList;

abstract public class TurnController {
    private static boolean[] comPlayer = new boolean[4], chosenPlayer = new boolean[4];
    private static int turn = 0;
    private static ArrayList<Integer> firstTurn = new ArrayList<>();
    private static int[] firstThrowTotal = new int[4];
    static boolean firstTurnDetermined =true;
    private static Button rollBt;
    private static ImageView turnIndicator;
    private static Label history;
    private static boolean anotherTurn;

    //check if current turn belongs to a computer player
    static boolean isComTurn() {
        return comPlayer[turn];
    }

    //get current turn
    public static int getTurn() { return turn; }

    //check if the turn ends
    static void checkEndTurn() {
        if (Storage.dices[0] == -13 && Storage.dices[1] == -13) endTurn();
    }

    //initialize all properties in TurnController
    static void initialize(Button rollBt, ImageView turnIndicator, boolean[] comPlayer, boolean[] chosenPlayer, Label history) {
        TurnController.comPlayer = comPlayer;
        TurnController.chosenPlayer = chosenPlayer;
        TurnController.rollBt = rollBt;
        TurnController.turnIndicator = turnIndicator;
        TurnController.history = history;
        for (int t = 0; t < 4; t++)
            if (chosenPlayer[t]) firstTurn.add(t);
        for (boolean t : chosenPlayer) {                                                                                //initialize the first throw (not the first turn) to the nearest available player: blue, yellow, green, red
            if (!t) turn = (turn == 3) ? 0 : turn + 1;
            else break;
        }
        firstTurnDetermined = firstTurn.size() == 1;
        if (firstTurnDetermined) firstTurn.clear();
        startTurn();
    }

    //check if first turn is determined
    private static boolean checkFirstTurn() {
        ArrayList<Integer> remain = new ArrayList<>();                                                                  //store the players with the same maximum roll value
        for (int t = 0; t < 4; t++) if (firstThrowTotal[t] != 0) remain.add(t);
        if (firstTurn.indexOf(turn) == firstTurn.size() - 1) {                                                          //the players with the same maximum roll value must roll again
            firstTurn = remain;
            firstThrowTotal = new int[4];
            if (firstTurn.size() == 1) {                                                                                //if there is one player left, they get first throw
                history.setText(history.getText() + "\n\n" + Storage.playerName[firstTurn.get(0)]
                                + " " + LanguageController.getString("firstThrow"));
                turn = firstTurn.get(0);
                firstTurn.clear();
                startTurn();
                return true;
            } else history.setText("");
        }
        return false;
    }

    //determine first turn
    private static void getFirstTurn() {
        String temp = (history.getText().equals("") ? "" : "\n") + Storage.playerName[turn] + ": " + Storage.dices[2];  //display on the label
        history.setText(history.getText() + temp);
        for (int t : firstTurn) {                                                                                       //if the total is not greater than the inspected value, set it to 0
            if (firstThrowTotal[t] > Storage.dices[2]) {
                firstThrowTotal[turn] = 0;
                break;
            } else {
                if (firstThrowTotal[t] < Storage.dices[2]) firstThrowTotal[t] = 0;                                      //else set the inspected value to 0 and set the current value to the total
                firstThrowTotal[turn] = Storage.dices[2];
            }
        }
    }

    //start a turn
    private static void startTurn() {
        if (firstTurnDetermined) {                                                                                      //remove the label
            history.setText("");
            history.setVisible(false);
        }
        turnIndicator.setImage(new Image("sprites\\" + Storage.player.get(turn) + ".png"));                         //set the turn indicator
        if (comPlayer[turn]) {                                                                                          //if this is a computer player turn, then disable the rollBt, pause then roll the dices
            rollBt.setDisable(true);
            PauseTransition transition = new PauseTransition(Duration.millis(500));
            transition.play();
            transition.setOnFinished(t-> Computer.rollDices());
        }
    }

    //end a turn
    private static void endTurn() {
        if (!Player.won()) {                                                                                            //check if any player's won the game
            rollBt.setDisable(false);                                                                                   //return the rollBt to normal
            if (!firstTurnDetermined) {                                                                                 //check if the first turn is determined
                firstTurnDetermined = checkFirstTurn();
                if (firstTurnDetermined) return;
            }
            if (!anotherTurn) {                                                                                         //check if a player's got an extra turn
                turn = (turn == 3) ? 0 : turn + 1;
                while (firstTurnDetermined ? !chosenPlayer[turn] : !firstTurn.contains(turn)) {                         //move to the next available player's turn
                    turn = (turn == 3) ? 0 : turn + 1;
                }
            }
            startTurn();
            anotherTurn = false;
        } else GameController.end();                                                                                    //trigger end screen
    }

    //check after the player roll
    static void checkAfterRoll() {
        boolean isComTurn = isComTurn();
        if (!firstTurnDetermined) {                                                                                     //check if the turn is determined, if not determine it
            getFirstTurn();
            endTurn();
        }
        else {
            if (Storage.dices[0] == Storage.dices[1]) anotherTurn = true;                                               //check if a player's got an extra turn
            Player.initializePossibleMove();                                                                            //get all the possible moves
            if (isComTurn) Computer.makeDecision();                                                                     //if this is a computer's turn, make a move
            checkEndTurn();
        }
    }
}
