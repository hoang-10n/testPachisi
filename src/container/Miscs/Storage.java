package container.Miscs;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

abstract public class Storage {
    public static Circle[] move;
    public static Rectangle[] home;
    public static Chess[] chess = new Chess[16];
    public static int[] dices = new int[3];
    public static ArrayList<Character> player = new ArrayList<>(List.of('b','y','g','r'));
    public static HashMap<Chess, int[]> possibleMove = new HashMap<>();
    public static String[] playerName = new String[4];
    public static int[] score = new int[4];
    public static Label[] scoreDisplay;

    public static int getChessIndex(String name) {
        return player.indexOf(name.charAt(0)) * 4 + (name.charAt(1) - '0');
    }

    static void updateScore(int add, int index) {
        score[index] += add;
        String str = scoreDisplay[index].getText().substring(0, 7);
        scoreDisplay[index].setText(str + score[index]);
    }

    public static int[] sortScore() {
        ArrayList<Integer> oldScore = new ArrayList<>(), newScore = new ArrayList<>();
        for (int i : score) {
            oldScore.add(i);
            newScore.add(i);
        }
        newScore.sort(Collections.reverseOrder());
        int[] order = new int[4];
        for (int i = 0; i < 4; i++) {
            order[i] = oldScore.indexOf(newScore.get(i));
            newScore.set(i, -13);
        }
        return order;
    }
}
