package container.Miscs;

import container.Controller.TurnController;

import java.util.ArrayList;
import java.util.Collections;

public class Computer extends Player {
    private static int[] sortDistance(int t) {
        ArrayList<Integer> distance = new ArrayList<>(), newDistance = new ArrayList<>();
        for (int i = t; i < t + 4; i++) {
            int distanceFromHome = Storage.chess[i].getDistanceFromHome();                                              //store all chess's distances from hone
            distance.add(distanceFromHome);
            newDistance.add(distanceFromHome);
        }
        Collections.sort(newDistance);                                                                                  //sort all distances in ascending order
        int[] sorted = new int[4];
        for (int i = 0; i < 4; i++) {
            int index = distance.indexOf(newDistance.get(i));                                                           //get the index from the closest to the furthest-to-home chess
            sorted[i] = index;
            distance.set(index, -13);
        }
        return sorted;
    }

    //check if the computer player's chess cannot do anything
    private static boolean canDoNothing(Chess chess) {
        int[] possibleMove = Storage.possibleMove.get(chess);
        return possibleMove[0] == -13 && possibleMove[1] == -13;
    }

    //check if the computer player's chess can kick
    private static int[] canKick(Chess chess) {
        if (chess.getKickTarget() != null) {
            int[] possibleMove = Storage.possibleMove.get(chess);
            for (int i = 0; i < 3; i++) {
                if (possibleMove[i] == chess.getKickTarget().getPos()) {
                    if (i == 2) return new int[]{3, 2};                                                                 //if the chess can kick with 2 dices, give this move 3 points
                    else return new int[]{6, i};                                                                        //else give it 6 points
                }
            }
        }
        return null;
    }

    //check if the computer player's chess can return home
    private static int[] canReturnHome(Chess chess) {
        if (chess.getDistanceFromHome() <= 0) {
            int[] possibleMove = Storage.possibleMove.get(chess);
            if (possibleMove[0] == -13) possibleMove[0] = 13;
            if (possibleMove[1] == -13) possibleMove[1] = 13;
            int index = Math.min(possibleMove[0], possibleMove[1]) == possibleMove[0] ? 0 : 1;                          //choose the higher dice value
            return new int[]{chess.getDistanceFromHome() == 0 ? 5 : 4, index};                                          //if the chess is at home position 0, give this move 5 points, else give it 4 points
        }
        return null;
    }

    //check if the computer player's chess can spawn
    private static int[] canSpawn(Chess chess) {                                                                        //give this move 2 points
        if (chess.getPos() == -13) {
            if (Storage.dices[0] == 6 || Storage.dices[0] == 1) {
                if (Storage.dices[1] == 1) return new int[]{2, 1};                                                      //if the player got a 6 and a 1, use a 1 to spawn
                else return new int[]{2, 0};
            }
            if (Storage.dices[1] == 6 || Storage.dices[1] == 1) return new int[]{2, 1};
        }
        return null;
    }

    //check if the computer player's chess can move
    private static int[] canMove(Chess chess) {
        int[] possibleMove = Storage.possibleMove.get(chess);
        if (chess.getPos() == chess.getSpawnPos()) {                                                                    //if this chess is at spawning position and it can move and the player got a 1 or a 6, give this move 1 point
            if ((Storage.dices[0] == 6 || Storage.dices[0] == 1) && possibleMove[1] != -13) {
                if (Storage.dices[1] != 1) return new int[]{1, 1};
            }
            if ((Storage.dices[1] == 6 || Storage.dices[1] == 1) && possibleMove[0] != -13) return new int[]{1, 0};
        }
        if (possibleMove[2] != -13) return new int[]{0, 2};                                                             //if the chess can used move, give this move 0 point
        else return new int[]{0,(possibleMove[0] > possibleMove[1]) ? 0 : 1};
    }

    //make a move for the computer player
    public static void makeDecision() {
        int t = TurnController.getTurn() * 4;                                                                           //the range to get the chess with the color of the player
        int index = -1, max = -1;
        int[][] score = new int[4][2];                                                                                  //store the priorities of the moves
        for (int i = t; i < t + 4; i++) {
            Chess chess = Storage.chess[i];
            if (!canDoNothing(chess)) {
                if (canKick(chess) != null) score[i - t] = canKick(chess);
                else if (canReturnHome(chess) != null) score[i - t] = canReturnHome(chess);
                else if (canSpawn(chess) != null) score[i - t] = canSpawn(chess);
                else score[i - t] = canMove(chess);
            } else score[i - t] = null;
        }
        int[] sorted = sortDistance(t);                                                                                 //get the sorted-distance chess list
        for (int i : sorted) {
            if (score[i] != null) {
                if (max < score[i][0]) {
                    max = score[i][0];
                    index = i;
                }
            }
        }
        if (index != -1) {                                                                                              //move the chess from the old position to the new position
            Chess chess = Storage.chess[index + t];
            int move = score[index][1];
            int newPos = Storage.possibleMove.get(chess)[move];
            if (chess.getDistanceFromHome() > 0)
                chess.moveChess(chess.getPos() != -13 ? newPos : chess.getSpawnPos(), move);
            else chess.moveChessHome(newPos, move);
        }
    }
}
