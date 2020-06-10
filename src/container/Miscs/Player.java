package container.Miscs;

import container.Controller.AnimationController;
import container.Controller.MediaController;
import container.Controller.TurnController;

import java.util.ArrayList;
import java.util.Random;

abstract public class Player {
    private static Random r = new Random();
    private static int t = 0;

    //check if a player won
    public static boolean won() {
        ArrayList<Integer> homePos = new ArrayList<>();
        for (int i = t; i < t + 4; i++) {                                                                               //get all the position of all chess of that color
            homePos.add(Storage.chess[i].getPos());
        }
        for (int i = -6; i < -2; i++) {                                                                                 //check if they are in their winning position (home 6, home 5, home 4, home 3)
            if (!homePos.contains(i)) return false;
        }
        return true;
    }

    //check if the chess is able to kick after moving
    public static void checkKickChess(Chess chess) {
        Chess kickedChess = chess.getKickTarget();
        if (kickedChess != null && kickedChess.getPos() == chess.getPos()) {                                            //if the chess has a kick target and they have some position, then the other chess is kicked
            kickedChess.kicked();
            Storage.updateScore(-2, kickedChess.getSpawnPos() / 14);                                         //update the score for the kick and the kicked chess
            Storage.updateScore(2, chess.getSpawnPos() / 14);
        }
        chess.setKickTarget(null);
    }

    //check if the chess is spawnable
    private static void isSpawnable(int index) {
        int k0 = Storage.dices[0] == 6 || Storage.dices[0] == 1 ? 0 : -13;                                              //if either dice is not a 6 or a 1, then this move is not possible
        int k1 = Storage.dices[1] == 6 || Storage.dices[1] == 1 ? 0 : -13;
        if (k0 == 0 || k1 == 0) {
            String name = Storage.move[Storage.chess[index].getSpawnPos()].getId();
            if (!name.equals("")) {                                                                                     //if there is a chess at the spawn position
                int i = Storage.getChessIndex(name);
                if (i < t || i > t + 3) Storage.chess[index].setKickTarget(Storage.chess[i]);                           //if the chess has different color, set it as kick target
                else return;                                                                                            //else this move is not possible
            }
            if (Storage.chess[index].getPos() == -13) {
                Storage.possibleMove.get(Storage.chess[index])[0] = k0;                                                 //modify the possibleMove
                Storage.possibleMove.get(Storage.chess[index])[1] = k1;
            }
        }
    }

    //check if the chess is at home and movable
    private static void isHomeMovable(int index) {
        int d0 = Storage.dices[0], d1 = Storage.dices[1], homePos = - Storage.chess[index].getPos();
        if (d0 <= homePos || d0 == -13) d0 = 13;                                                                        //if either dice has garbage value or is smaller than the current home position, then this move is not possible
        if (d1 <= homePos || d1 == -13) d1 = 13;
        for (int i = t; i < t + 4; i++) {
            if (d0 == 13 && d1 == 13) break;
            int anotherPos = - Storage.chess[i].getPos();
            if (i != index && anotherPos > homePos && anotherPos > 0) {                                                 //if there is a chess between the current and the new position, then this move is not possible
                if (anotherPos <= d0) d0 = 13;
                if (anotherPos <= d1) d1 = 13;
            }
        }
        Storage.possibleMove.get(Storage.chess[index])[0] = -d0;                                                        //modify the possibleMove (home moves will be negative)
        Storage.possibleMove.get(Storage.chess[index])[1] = -d1;
    }

    //check if the chess is movable
    private static void isMovable(int index) {
        Chess chess = Storage.chess[index];
        for (int k = 0; k < 3; k++) {
            int movable = 0;
            int i = chess.getPos();
            int newPos = i + Storage.dices[k];
            if (newPos >= 56) newPos = newPos - 56;
            if (chess.getDistanceFromHome() < Storage.dices[k] || Storage.dices[k] == -13) movable = 1;                 //if the chess's distance from home is smaller than the dice or the value is garbage, then this move is not possible
            else {
                while (i != newPos) {
                    i = i == 55 ? 0 : i + 1;
                    String name = Storage.move[i].getId();
                    if (!name.equals("")) {                                                                             //if there is a chess on the move, then this move is not possible
                        if (i == newPos && name.charAt(0) != chess.getImage().getId().charAt(0))                        //if there is a chess with different color at the destination, set it as the kick target
                            chess.setKickTarget(Storage.chess[Storage.getChessIndex(name)]);
                        else movable = 1;                                                                               //else this move is not possible
                        break;
                    }
                }
            }
            if (movable == 0) Storage.possibleMove.get(Storage.chess[index])[k] = newPos;                               //is this move is possible, modify the possibleMove
        }
    }

    //get possible moves and store them in possibleMove
    private static void getPossibleMove() {
        boolean nexTurn = true;
        for (int i = t; i < t + 4; i++) {                                                                               //get the possible moves
            Chess chess = Storage.chess[i];
            if (chess.getPos() == -13) isSpawnable(i);
            else if (chess.getDistanceFromHome() <= 0) isHomeMovable(i);
            else isMovable(i);
        }
        for (int i = t; i < t + 4; i++) {                                                                               //check if no move can be made
            nexTurn = Storage.chess[i].canBeUsed();
            if (!nexTurn) break;
        }
        if (nexTurn) {                                                                                                  //remove all the dices
            Storage.dices[0] = -13;
            Storage.dices[1] = -13;
        }
    }

    //initialize possibleMove
    public static void initializePossibleMove() {
        t = TurnController.getTurn() * 4;
        for (int i = t; i < t + 4; i++) {
            assert Storage.possibleMove != null;
            Storage.possibleMove.put(Storage.chess[i], new int[]{-13,-13,-13});
        }
        getPossibleMove();
//        print();
    }

    //print out the possible moves
    private static void print() {
        for (int i = t; i < t + 4; i++) {
            Chess temp = Storage.chess[i];
            System.out.print(temp.getImage().getId() + ": ");
            for (int k : Storage.possibleMove.get(temp)) System.out.print(k + " ");
            System.out.println();
        }
    }

    //roll the dices
    public static void rollDices() {
        MediaController.playRollSound();
        int[] dices = new int[]{r.nextInt(6) + 1, r.nextInt(6) + 1};
        Storage.dices[0] = dices[0];
        Storage.dices[1] = dices[1];
        Storage.dices[2] = dices[0] + dices[1];
        AnimationController.rollDiceAnimation(dices[0], dices[1]);
    }
}
