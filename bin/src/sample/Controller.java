package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
    private final String songPath = "resources/songs/";
    //FORMATO!!!!!!!!!!!!!!!
    public String africaPath = songPath + "Africa.mp3";

    public boolean isPlaying;
    public String currentSong;

    public Controller(){
        isPlaying = false;
        fileChooser = new FileChooser();

        //Depois ajeitar o trycatch, não sei se fica necessário fazer isso aqui, pode ser no playPause msm...
        //Se bem que seria util você colocar uma música inicial p/ prevenção de erros
        //Ou carregar a pasta songs em vez de alguma playlist
        try {
            media = new Media(new File(africaPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }catch (Exception e){
            System.out.println("Caminho não encontrado: " + e);
        }

        //Tentei fazer uns bicho com volume não deu certo kOASDKOASDKOASKDOASKDOA
//        sldVolumeBar = new Slider();
//        sldVolumeBar.setValue(mediaPlayer.getVolume() * 50);
//        sldVolumeBar.valueProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                mediaPlayer.setVolume(sldVolumeBar.getValue() / 100);
//            }
//        });
    }

    public void btnPlayPauseOnAction(ActionEvent event) {
        try {
            lblDisplay.setText("Now playing: " +  media.getSource().substring(64));
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.play();
                isPlaying = true;
            }
        }catch (Exception e){
            System.out.println("Música não encontrada: " + e);
        }
    }

    public void btnStopOnAction(ActionEvent event) {
        try {
            mediaPlayer.stop();
        }catch (Exception e){
            System.out.println("Nenhuma música está tocando: " + e);
        }
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

}
