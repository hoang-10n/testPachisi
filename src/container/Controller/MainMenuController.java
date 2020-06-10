package container.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import container.Miscs.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuController
{
    private int current_color = 0, num = 0;
    private TextField[] plName;
    private Pane[] panes;
    private Rectangle[] rectangles;
    private CheckBox[] isCom;

    @FXML
    private CheckBox isCom0, isCom1, isCom2, isCom3;

    @FXML
    private Pane pane0, pane1, pane2, pane3;

    @FXML
    private Rectangle color1, color2, color3, color4;

    @FXML
    private ImageView menu;

    @FXML
    private Button start, language, back, done, exit;

    @FXML
    private ComboBox<String> choice;

    @FXML
    private TextField plName1,plName2,plName3,plName4;

    @FXML
    //initialize the arrays and arrayLists
    private void initialize() {
        choice.getItems().addAll("1","2","3","4");
        isCom = new CheckBox[]{isCom0, isCom1, isCom2, isCom3};
        plName = new TextField[]{plName1,plName2,plName3,plName4};
        panes = new Pane[]{pane0, pane1, pane2, pane3};
        rectangles = new Rectangle[]{color1, color2, color3, color4};
        LanguageController.changeLanguage();
        changeLanguage();
    }

    @FXML
    //remove and show some properties
    private void start() {
        start.setVisible(false);
        language.setVisible(false);
        exit.setVisible(false);
        choice.setVisible(true);
        back.setVisible(true);
        done.setVisible(true);
    }

    @FXML
    //change the language of the components in MainMenu
    private void changeLanguage() {
        LanguageController.changeLanguage();
        start.setText(LanguageController.getString("start"));
        language.setText(LanguageController.getString("language"));
        exit.setText(LanguageController.getString("exit"));
        choice.setPromptText(LanguageController.getString("choose"));
        menu.setImage(new Image(LanguageController.getString("menu")));
        done.setText(LanguageController.getString("done"));
        back.setText(LanguageController.getString("back"));
        for (int i = 0; i < 4; i++) {
            plName[i].setText(LanguageController.getString("player") + (i + 1));
            isCom[i].setText(LanguageController.getString("com"));
        }
    }

    @FXML
    //exit the program
    private void exit() { Platform.exit(); }

    @FXML
    //display the text area and check box when the user select a choice from the combo box
    private void getPlayerNum() {
        done.setDisable(false);
        boolean visible = true;
        num = (choice.getValue()).charAt(0) - '0';
        for (int i = 0; i < 4; i++) {
            if (i == num) visible = false;
            panes[i].setVisible(visible);
        }
    }

    @FXML
    //return to the MainMenu
    private void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/container/MainMenu.fxml"))));
    }

    @FXML
    //go to GamePlay and initialize the number of players, computer players, names, colors
    private void goToGamePlay(MouseEvent event) throws IOException {
        ArrayList<Color> color = new ArrayList<>(Arrays.asList(Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED));      //TODO List.of() doesn't work in java 8
        boolean[] compPlayer = new boolean[4];
        boolean[] chosenPlayer = new boolean[4];
        for (int i = 0; i < 4; i++) {                                                                                   //get the number of players, computer players, colors and names accordingly
            int k = color.indexOf(rectangles[i].getFill());
            compPlayer[k] = (isCom[i].isSelected());
            chosenPlayer[k] = (i <= num - 1);
            if (chosenPlayer[k]) Storage.playerName[k] = plName[i].getCharacters().toString();
            else Storage.playerName[k] = "";
        }
        GameController.comPlayer = compPlayer;                                                                          //pass values of the computer players and available players to GamePlay
        GameController.chosenPlayer = chosenPlayer;
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();                                         //switch to GamePlay scene
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/container/GamePlay.fxml"))));
    }

    @FXML
    //change the color
    private void changeColor(MouseEvent event) {
        Rectangle temp = (Rectangle)event.getSource();
        current_color++;
        if (current_color > 3) current_color = 0;
        if (rectangles[current_color] == temp)
            if (current_color == 3) current_color = 0;
            else current_color ++;
        Color temp_color = (Color) temp.getFill();
        temp.setFill(rectangles[current_color].getFill());
        rectangles[current_color].setFill(temp_color);
    }
}
