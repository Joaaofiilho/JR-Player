package sample;

import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

public class MyThread extends Thread {
    private int tempo;
    private MediaPlayer mediaPlayer;
    private Slider sldProgressBar;
    private Controller controller;
    private static int number;

    public MyThread(Controller controller, MediaPlayer mediaPlayer, Slider sldProgressBar) {
        tempo = 200;
        this.controller = controller;
        this.mediaPlayer = mediaPlayer;
        this.sldProgressBar = sldProgressBar;
        number = 0;
    }

    public void run() {
        while (controller.isPlaying()) {
            if((mediaPlayer.getCurrentTime().toSeconds()) * (sldProgressBar.getMax() / mediaPlayer.getTotalDuration().toSeconds()) != 0) {
                sldProgressBar.setValue((mediaPlayer.getCurrentTime().toSeconds()) * (sldProgressBar.getMax() / mediaPlayer.getTotalDuration().toSeconds()));
            }

            try {
                Thread.sleep(tempo);
            } catch (Exception e) {
                System.out.println("Erro no thread!");
            }
        }
    }

}
