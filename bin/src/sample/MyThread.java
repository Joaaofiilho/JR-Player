package sample;

import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

public class MyThread extends Thread {
    private int tempo;
    private MediaPlayer mediaPlayer;
    private Slider sldProgressBar;
    private Controller controller;

    public MyThread(Controller controller, MediaPlayer mediaPlayer, Slider sldProgressBar){
        tempo = 200;
        this.controller = controller;
        this.mediaPlayer = mediaPlayer;
        this.sldProgressBar = sldProgressBar;
    }

    public void run(){
        do {
            do {
                sldProgressBar.setValue((mediaPlayer.getCurrentTime().toSeconds()) * (sldProgressBar.getMax() / mediaPlayer.getTotalDuration().toSeconds()));
                try {
                    Thread.sleep(tempo);
                } catch (Exception e) {
                    System.out.println("Deu ruim: " + e.getMessage());
                }
            } while (controller.isPlaying());
        }while (controller.isPlaying());
    }
}
