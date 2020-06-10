package container.Miscs;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import container.Controller.TurnController;

public class Shadow {
    private ImageView shadow;
    private int pos;
    private int index;

    Shadow(ImageView shadow) {
        this.shadow = shadow;
        index = shadow.getId().charAt(4) - '0';
    }

    public int getIndex() {
        return index;
    }

    //set the position of the shadow
    public void setPos(int pos) {
        shadow.setVisible(true);
        this.pos = pos;
        Circle circle = Storage.move[pos];
        shadow.setLayoutX(circle.getLayoutX() - 25);
        shadow.setLayoutY(circle.getLayoutY() - 55);
    }

    //set the home position of the shadow
    public void setHomePos(int pos) {
        int turn = TurnController.getTurn();
        shadow.setVisible(true);
        this.pos = pos;
        Rectangle rectangle = Storage.home[turn * 6 - pos - 1];
        shadow.setLayoutX(rectangle.getLayoutX() - ((turn % 2 == 0) ? -15 : 5));
        shadow.setLayoutY(rectangle.getLayoutY() - ((turn % 2 == 0) ? 30 : 15));
    }

    public int getPos() {
        return pos;
    }

    public void vanish() {
        shadow.setVisible(false);
    }
}
