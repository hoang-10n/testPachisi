package container.Miscs;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import container.Controller.AnimationController;

public class Human extends Player {
    public static Shadow[] move = new Shadow[3];
    private static Button rollBt;

    //initialize the 'shadows' and the rollBt
    public static void initialize(ImageView move0, ImageView move1, ImageView move2, Button rollBt) {
        Human.move[0] = new Shadow(move0);
        Human.move[1] = new Shadow(move1);
        Human.move[2] = new Shadow(move2);
        Human.rollBt = rollBt;
    }

    //clear all the 'shadows'
    public static void clearShadow() {
        for (int i = 0; i < 3; i++) {
            move[i].vanish();
        }
    }

    //show the possible move of the chosen chess
    public static void showPossibleMove(Chess currentChess) {
        try {
            int[] possibleMove = Storage.possibleMove.get(currentChess);
            if (possibleMove[0] == -13) AnimationController.updateDices(0, false);
            if (possibleMove[1] == -13) AnimationController.updateDices(1, false);
            int pos = currentChess.getPos();
            for (int i = 0; i < 3; i++) {                                                                               //move the 'shadows' to the positions in the possibleMove
                if (possibleMove[i] != -13) {
                    if (currentChess.getDistanceFromHome() > 0) {
                        move[i].setPos(pos != -13 ? possibleMove[i] : currentChess.getSpawnPos());
                    } else move[i].setHomePos(possibleMove[i]);
                }
            }
        } catch (NullPointerException e) {                                                                              //set the test of the rollBt to RED for 3 seconds if the player hasn't rolled their dices
            rollBt.setTextFill(Color.RED);
            PauseTransition transition = new PauseTransition(Duration.seconds(3));
            transition.play();
            transition.setOnFinished(t -> rollBt.setTextFill(Color.BLACK));
        }
    }
}
