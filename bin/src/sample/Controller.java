package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import java.io.*;

public class Controller {

    private final String songPath = "resources/songs/";
    //FORMATO!!!!!!!!!!!!!!!
    //teste
    public String africaPath = songPath + "Africa.mp3";
    public Media media;
    public MediaPlayer mediaPlayer;
    public FileChooser fileChooser;

    @FXML
    public Button btnPlayPause;
    public Button btnStop;
    public Button btnForward;
    public Button btnBackward;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public Label lblDisplay;

    public AnchorPane ancpBackground;

    public Controller(){
        isPlaying = false;
        fileChooser = new FileChooser();
        try {
            media = new Media(new File(africaPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }catch (Exception e){
            System.out.println("Caminho não encontrado: " + e);
        }

    }

    public void btnPlayPauseOnAction(ActionEvent event) {
        lblDisplay.setText("Now playing:" +  media.toString());

        if (isPlaying){
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            mediaPlayer.play();
            isPlaying = true;
        }
    }

    public void btnStopOnAction(ActionEvent event) {
        mediaPlayer.stop();
        isPlaying = false;
    }

    public void btnForwardOnAction(ActionEvent event) {
//        media = new Media(new File(fileChooser.showOpenDialog());
//        mediaPlayer = new MediaPlayer(media);
    }

    public void btnBackwardOnAction(ActionEvent event) {

    }

    //Métodos úteis
    public static void definirEscritor(FileOutputStream arquivo, PrintWriter pr, String uri) throws IOException {
        arquivo = new FileOutputStream(uri);
        pr = new PrintWriter(arquivo);
    }

    public static void definirLeitor(FileInputStream arquivo, InputStreamReader input, BufferedReader br, String uri) throws IOException {
        arquivo = new FileInputStream(uri);
        input = new InputStreamReader(arquivo);
        br = new BufferedReader(input);
    }

    public boolean isPlaying;
}
