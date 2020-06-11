package container.Controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

abstract public class MediaController
{
    //media paths
    private static String kickPath = "src/sounds/whip.mp3";
    private static String movePath = "src/sounds/move.mp3";
    private static String spawnPath = "src/sounds/spawn.wav";
    private static String rollPath = "src/sounds/roll.wav";
    private static String winPath = "src/sounds/win.mp3";
    private static String musicPath = "src/sounds/music.mp3";

    //audio clips and music
    private static AudioClip kickSound = new AudioClip(new File(kickPath).toURI().toString());
    private static AudioClip moveSound = new AudioClip(new File(movePath).toURI().toString());
    private static AudioClip spawnSound = new AudioClip(new File(spawnPath).toURI().toString());
    private static AudioClip rollSound = new AudioClip(new File(rollPath).toURI().toString());
    private static AudioClip winSound = new AudioClip(new File(winPath).toURI().toString());
    private static MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(musicPath).toURI().toString()));

    //play audio clips
    public static void playKickSound(){kickSound.play();}
    public static void playMoveSound(){
        moveSound.setCycleCount(AudioClip.INDEFINITE);
        moveSound.play();
    }
    public static void playSpawnSound(){ spawnSound.play();}
    public static void playRollSound(){rollSound.play();}
    public static void playWinSound(){winSound.play();}

    //stop 'moving' sound and music
    public static void stopMoveSound(){moveSound.stop();}
    public static void stopMusic(){mediaPlayer.stop();}

    //mute/unmute sound FX
    public static void mute(char status) {
        int muted = status == '1' ? 0 : 10;
        kickSound.setVolume(muted);
        moveSound.setVolume(muted);
        spawnSound.setVolume(muted);
        rollSound.setVolume(muted);
        winSound.setVolume(muted);
        kickSound.stop();
        moveSound.stop();
        spawnSound.stop();
        rollSound.stop();
        winSound.stop();
    }

    //get volume of the media
    public static double getVolume() {
        return mediaPlayer.getVolume();
    }

    //play and set  volume for the media
    public static void changeVolume(double volume) {
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
        mediaPlayer.setVolume(volume);
    }
}