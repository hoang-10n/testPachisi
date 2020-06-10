package container.Miscs;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import container.Controller.AnimationController;
import container.Controller.MediaController;
import container.Controller.TurnController;

public class Chess {
    private ImageView image;
    private int pos = -13;
    private int distanceFromHome = 56;
    private double originX;
    private double originY;
    private Chess kickTarget = null;
    private int chessIndex;

    public Chess(ImageView image) {
        this.image = image;
        originX = image.getLayoutX();
        originY = image.getLayoutY();
        chessIndex = Storage.player.indexOf(image.getId().charAt(0));
    }

    public boolean canBeUsed() {
        return Storage.possibleMove.get(this)[0] == -13 && Storage.possibleMove.get(this)[1] == -13;
    }

    public void setKickTarget(Chess kickTarget) {
        this.kickTarget = kickTarget;
    }

    public Chess getKickTarget() {
        return kickTarget;
    }

    public void kicked() {
        pos = -13;
        distanceFromHome = 56;
        MediaController.playKickSound();
        AnimationController.kicked(image, originX, originY);
    }

    public int getSpawnPos() {
        return chessIndex * 14;
    }

    public ImageView getImage() {
        return image;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setDistanceFromHome() {
        int color = Storage.player.indexOf(image.getId().charAt(0));
        if (pos < 0) distanceFromHome = pos;
        else distanceFromHome = pos > color * 14 - 1 ? 56 - (pos - color * 14 + 1) : color * 14 - pos - 1;
    }

    public int getDistanceFromHome() {
        return distanceFromHome;
    }

    //move the chess to the position
    public void moveChess(int newPos, int index) {
        if (pos != -13) {
            Storage.move[pos].setId("");
            MediaController.playMoveSound();
        }
        else MediaController.playSpawnSound();
        int[] i = new int[]{pos == -13 ? chessIndex * 14 - 1 : pos};
        setPos(newPos);
        setDistanceFromHome();
        Storage.move[pos].setId(image.getId());
        int loop = (i[0] > pos) ? pos + 56 - i[0] : pos - i[0];
        if (loop != 0) AnimationController.moveAnimation(i, image.getLayoutX(), image.getLayoutY(), this, pos, index);
    }

    //move the chess to the home position
    public void moveChessHome(int newPos, int index) {
        int turn = TurnController.getTurn();
        if (pos > 0) Storage.move[pos].setId("");
        int oldDistanceFromHome = distanceFromHome;
        setPos(newPos);
        setDistanceFromHome();
        Storage.updateScore(oldDistanceFromHome - distanceFromHome, chessIndex);                                   //update the score
        Rectangle rectangle = Storage.home[turn * 6 - pos - 1];
        double x = rectangle.getLayoutX() - ((turn % 2 == 0) ? -15 : 5);
        double y = rectangle.getLayoutY() - ((turn % 2 == 0) ? 30 : 15);
        AnimationController.homeMoveAnimation(x, y , image, index);
    }

    //set test case
    public void setTest(int pos) {
        setPos(pos);
        setDistanceFromHome();
        if (pos >= 0) Storage.move[pos].setId(image.getId());
        else Storage.updateScore(-distanceFromHome, chessIndex);
        if (distanceFromHome >= 0) {
            image.setLayoutX(Storage.move[pos].getLayoutX() - 25);
            image.setLayoutY(Storage.move[pos].getLayoutY() - 55);
        } else {
            int color = Storage.player.indexOf(image.getId().charAt(0));
            Rectangle rectangle = Storage.home[color * 6 - pos - 1];
            image.setLayoutX(rectangle.getLayoutX() - ((color % 2 == 0) ? -15 : 5));
            image.setLayoutY(rectangle.getLayoutY() - ((color % 2 == 0) ? 30 : 15));
        }
    }
}