package container.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import container.Miscs.Chess;
import container.Miscs.Computer;
import container.Miscs.Player;
import container.Miscs.Storage;

import java.util.Random;

abstract public class AnimationController {
    private static Random r = new Random();
    private static ImageView[] dice = new ImageView[2];

    //initialize the dices
    static void initialize(ImageView dice0, ImageView dice1) {
        AnimationController.dice[0] = dice0;
        AnimationController.dice[1] = dice1;
    }

    //kick the chess
    public static void kicked(ImageView image, double originX, double originY) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(500),image);
        transition.setToX(originX - image.getLayoutX());                                                                //move the chess to the original position
        transition.setToY(originY - image.getLayoutY());
        transition.play();
        transition.setOnFinished(t -> {
            image.setLayoutX(originX);
            image.setLayoutY(originY);
            image.setTranslateX(0);
            image.setTranslateY(0);
        });
    }

    //roll dices
    public static void rollDiceAnimation(int a, int b) {
        dice[0].setOpacity(1);                                                                                          //return the dices to normal
        dice[1].setOpacity(1);
        int[] angle = new int[]{0,0};
        float[] vector = new float[]{r.nextInt(11) + 10, r.nextInt(41) - 20,                              //x,y vectors for first dice
                                     r.nextInt(11) - 20, r.nextInt(41) - 20,                              //x, y vectors for second dice
                                     r.nextInt(91) - 45, r.nextInt(91) - 45};                             //angles of two dices
        dice[0].setLayoutX(356);
        dice[0].setLayoutY(354);
        dice[1].setLayoutX(356);
        dice[1].setLayoutY(354);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50), (ActionEvent event) -> {                          //move the dices by 1 vector unit and rotate them by 1 angle unit
            dice[0].setLayoutX(dice[0].getLayoutX() + vector[0]);
            dice[0].setLayoutY(dice[0].getLayoutY() + vector[1]);
            dice[1].setLayoutX(dice[1].getLayoutX() + vector[2]);
            dice[1].setLayoutY(dice[1].getLayoutY() + vector[3]);
            for (int i = 0; i < 4; i ++) vector[i] = vector[i] * 4 / 5;                                                 //change the current locations of the dices
            angle[0] += vector[4];                                                                                      //change the angle of the dices
            angle[1] += vector[5];
            dice[0].setRotate(angle[0]);
            dice[1].setRotate(angle[1]);
            dice[0].setImage(new Image("sprites/dice" + (r.nextInt(6) + 1) + ".png"));                       //set the images of the dices randomly
            dice[1].setImage(new Image("sprites/dice" + (r.nextInt(6) + 1) + ".png"));
        }));
        timeline.setCycleCount(15);                                                                                     //repeat 15 times
        timeline.play();
        timeline.setOnFinished(t -> {
            dice[0].setImage(new Image("sprites/dice" + a + ".png"));                                               //set the images of the dices accordingly
            dice[1].setImage(new Image("sprites/dice" + b + ".png"));
            TurnController.checkAfterRoll();
        });
    }

    //move chess
    public static void moveAnimation(int[] i, double x, double y, Chess chess, int pos, int index) {
        ImageView image = chess.getImage();
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), image);
        i[0] = i[0] == 55 ? 0 : i[0] + 1;                                                                               //move to the next circle to the one the chess is standing
        transition.setToX(Storage.move[i[0]].getLayoutX() - x - 25);
        transition.setToY(Storage.move[i[0]].getLayoutY() - y - 55);
        transition.play();
        if (i[0] != ((pos == 55) ? 0 : pos + 1)) transition.setOnFinished(t->moveAnimation(i, x, y, chess, pos, index));//repeat the process over and over until the chess reach the destination
        else {
            MediaController.stopMoveSound();
            transition.stop();
            Player.checkKickChess(chess);
            image.setLayoutX(image.getLayoutX() + image.getTranslateX());
            image.setLayoutY(image.getLayoutY() + image.getTranslateY());
            image.setTranslateX(0);
            image.setTranslateY(0);
            updateDices(index, true);
        }
    }

    //move chess to home position
    public static void homeMoveAnimation(double x, double y, ImageView image, int index) {
        MediaController.playMoveSound();
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), image);
        transition.setToX(x - image.getLayoutX());                                                                      //move the chess to the destined home position
        transition.setToY(y - image.getLayoutY());
        transition.play();
        transition.setOnFinished(t->{
            MediaController.stopMoveSound();
            image.setLayoutX(image.getLayoutX() + image.getTranslateX());
            image.setLayoutY(image.getLayoutY() + image.getTranslateY());
            image.setTranslateX(0);
            image.setTranslateY(0);
            updateDices(index,true);
        });
    }

    //update the dices after the user finish a move or to check if a chess can use the dices
    public static void updateDices(int index, boolean real) {
        if (index == 2) {
            if (real) {                                                                                                 //change the display of 2 dices, and if 2 dices are used then remove them
                Storage.dices[0] = -13;
                Storage.dices[1] = -13;
            }
            dice[0].setOpacity(0.5);
            dice[1].setOpacity(0.5);
        }
        else {                                                                                                          //change the display of a dice, and if that dice is used then remove it and the sum of 2 dices
            dice[index].setOpacity(0.5);
            if (real) {
                Storage.dices[index] = -13;
                Storage.dices[2] = -13;
                Player.initializePossibleMove();
                if (TurnController.isComTurn()) Computer.makeDecision();                                                //make next move if the player is a computer
            }
        }
        TurnController.checkEndTurn();
    }
}
