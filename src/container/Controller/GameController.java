package container.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import container.Miscs.Chess;
import container.Miscs.Human;
import container.Miscs.Shadow;
import container.Miscs.Storage;

import java.io.IOException;

public class GameController {
    private Chess currentChess, oldChess;
    static boolean[] comPlayer, chosenPlayer;
    private static AnchorPane end;
    private static Label[] endScore;
    @FXML
    private Button rollBt, language, playAgain, exitWhenEnd, menuWhenEnd, exitWhenPlay, menuWhenPlay, soundFX;
    @FXML
    private Label name0, name1, name2, name3, score0, score1, score2, score3, volumeLabel, end0, end1, end2, end3, history;
    @FXML
    private ImageView dice0, dice1, b0, b1, b2, b3, y0, y1, y2, y3, g0, g1, g2, g3, r0, r1, r2, r3, move0, move1, move2, turnIndicator, soundFXIcon;
    @FXML
    private Circle bM0,bM1,bM2,bM3,bM4,bM5,bM6,bM7,bM8,bM9,bM10,bM11,bM12,yH0,
           yM0,yM1,yM2,yM3,yM4,yM5,yM6,yM7,yM8,yM9,yM10,yM11,yM12,gH0,
           gM0,gM1,gM2,gM3,gM4,gM5,gM6,gM7,gM8,gM9,gM10,gM11,gM12,rH0,
           rM0,rM1,rM2,rM3,rM4,rM5,rM6,rM7,rM8,rM9,rM10,rM11,rM12,bH0;
    @FXML
    private Rectangle bH1,bH2,bH3,bH4,bH5,bH6,
              yH1,yH2,yH3,yH4,yH5,yH6,
              gH1,gH2,gH3,gH4,gH5,gH6,
              rH1,rH2,rH3,rH4,rH5,rH6;

    @FXML
    private AnchorPane endScreen;

    @FXML
    private Slider volumeSlider;

    //display the end screen after the game ends
    static void end() {                                                                                                 
        MediaController.playWinSound();
        MediaController.stopMusic();
        end.setVisible(true);
        int[] order = Storage.sortScore();                                                                              //sort the score of the player
        int k = 0;
        for (int i : order) {
            if (chosenPlayer[k]) {
                String temp = Storage.playerName[i] + "\n" + Storage.score[i];
                endScore[k].setText(temp);
                if (k == TurnController.getTurn())
                    endScore[k].setGraphic(new ImageView(new Image("sprites/finisher.png")));
            }
            k++;
        }
    }

    //initialize the Storage and the other properties
    private void storageInitialize() {
        Storage.scoreDisplay = new Label[]{score0, score1, score2, score3};
        Storage.move = new Circle[]{bM0, bM1, bM2, bM3, bM4, bM5, bM6, bM7, bM8, bM9, bM10, bM11, bM12, yH0,
                                    yM0, yM1, yM2, yM3, yM4, yM5, yM6, yM7, yM8, yM9, yM10, yM11, yM12, gH0,
                                    gM0, gM1, gM2, gM3, gM4, gM5, gM6, gM7, gM8, gM9, gM10, gM11, gM12, rH0,
                                    rM0, rM1, rM2, rM3, rM4, rM5, rM6, rM7, rM8, rM9, rM10, rM11, rM12, bH0};
        for (int i = 0; i < 56; i++) Storage.move[i].setId("");                                                         //set the id of the circles to ""
        Storage.home = new Rectangle[]{bH1, bH2, bH3, bH4, bH5, bH6,
                                       yH1, yH2, yH3, yH4, yH5, yH6,
                                       gH1, gH2, gH3, gH4, gH5, gH6,
                                       rH1, rH2, rH3, rH4, rH5, rH6};
        ImageView[] chess = new ImageView[]{b0, b1, b2, b3, y0, y1, y2, y3, g0, g1, g2, g3, r0, r1, r2, r3};
        for (int i = 0; i < 16; i++) Storage.chess[i] = new Chess(chess[i]);
        name0.setText(Storage.playerName[0]);                                                                           //set the players' names
        name1.setText(Storage.playerName[1]);
        name2.setText(Storage.playerName[2]);
        name3.setText(Storage.playerName[3]);
        end = endScreen;                                                                                                //set the end screen
    }

    @FXML
    //change the language of the components in GamePlay
    private void changeLanguage() {
        String firstTurn = LanguageController.getString("firstThrow");
        LanguageController.changeLanguage();
        rollBt.setText(LanguageController.getString("rollBt"));
        volumeLabel.setText(LanguageController.getString("volume"));
        language.setText(LanguageController.getString("language"));
        playAgain.setText(LanguageController.getString("again"));
        exitWhenEnd.setText(LanguageController.getString("exit"));
        menuWhenEnd.setText(LanguageController.getString("main_menu"));
        exitWhenPlay.setText(LanguageController.getString("exit"));
        menuWhenPlay.setText(LanguageController.getString("main_menu"));
        soundFX.setText(LanguageController.getString("soundFX"));
        history.setText(history.getText().replace(firstTurn, LanguageController.getString("firstThrow")));
        for (int i = 0; i < 4; i++)
            if (chosenPlayer[i])
                Storage.scoreDisplay[i].setText(LanguageController.getString("score") + Storage.score[i]);
    }

    @FXML
    //mute or unmute the sound FX
    private void soundFX() {
        char icon = soundFXIcon.getId().equals("0") ? '1' : '0';
        soundFXIcon.setId(String.valueOf(icon));
        String path = "sprites/sound" + icon + ".jpg";
        MediaController.mute(icon);
        soundFXIcon.setImage(new Image(path));
    }

    @FXML
    //play the game again with the same settings, players and scores
    private void playAgain(MouseEvent event) throws IOException {                                                       
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/container/GamePlay.fxml"))));
    }

    @FXML
    //go to MainMenu
    private void backToMainMenu(MouseEvent event) throws IOException {
        TurnController.clearComPlayer();
        MediaController.stopMusic();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/container/MainMenu.fxml"))));
    }

    @FXML
    //exit the program
    private void exit() { Platform.exit(); }                                                                            

    @FXML
    //initialize other methods, displays, sounds and classes
    private void initialize(){
        soundFXIcon.setId("0");
        endScore = new Label[]{end0, end1, end2, end3};
        storageInitialize();
        AnimationController.initialize(dice0, dice1);
        TurnController.initialize(rollBt, turnIndicator, comPlayer, chosenPlayer, history);
        Human.initialize(move0, move1, move2, rollBt);
        LanguageController.changeLanguage();
        changeLanguage();
//        setTest();
        for (int i = 0; i < 4; i++) {                                                                                   //prevent the chess from the computer and the not-chosen players from being clicked
            if (comPlayer[i] || !chosenPlayer[i])
                for (int k = i * 4; k < i * 4 + 4; k++)
                    Storage.chess[k].getImage().setOnMouseClicked(null);
        }
        double volume = MediaController.getVolume();
        MediaController.changeVolume(volume);
        volumeSlider.setValue(volume * 100);                                                                            //set the value of the volume slider and initialize its listener
        volumeSlider.valueProperty().addListener(observable -> MediaController.changeVolume(volumeSlider.getValue() / 100));
    }

    @FXML
    //role the dices
    private void rollDices() {                                                                                          
        rollBt.setDisable(true);
        if (currentChess != null) currentChess.getImage().setEffect(new Glow(0));
        Human.clearShadow();
        Human.rollDices();
    }

    @FXML
    //choose the chess to move and display the 'shadows' which show the possible move
    private void choose(MouseEvent event) {                                                                             
        Human.clearShadow();
        dice0.setOpacity(1);
        dice1.setOpacity(1);
        String name = ((ImageView) event.getSource()).getId();
        int index = Storage.getChessIndex(name);
        currentChess = Storage.chess[index];
        if (index / 4 == TurnController.getTurn()) {                                                                    //set glowing effect for the clicked chess and remove the effect of the previously-clicked chess
            if (oldChess != null) oldChess.getImage().setEffect(new Glow(0));
            currentChess.getImage().setEffect(new Glow(1));
            oldChess = currentChess;
            if (TurnController.firstTurnDetermined)                                                                     //show possible moves if the first turn is determined
                Human.showPossibleMove(currentChess);
        }
    }

    @FXML
    //move chess to the chosen 'shadow'
    private void moveChess(MouseEvent event) {
        Human.clearShadow();
        String name = ((ImageView) event.getSource()).getId();                                                          //get the name of the chosen 'shadow'
        Shadow shadow = Human.move[name.charAt(4) - '0'];
        currentChess.getImage().setEffect(new Glow(0));
        if (currentChess.getDistanceFromHome() <= 0) currentChess.moveChessHome(shadow.getPos(), shadow.getIndex());    //move chess to that 'shadow'
        else currentChess.moveChess(shadow.getPos(), shadow.getIndex());
    }
}
