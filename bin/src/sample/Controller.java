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
    public String africaPath = songPath + "Imagine Dragons - Radioactive.mp3";
    public Media media;
    public MediaPlayer mediaPlayer;
    public FileChooser fileChooser;

    //Ler e escrever em arquivos de texto
    public FileOutputStream arquivoO;
    public PrintWriter pr;

    public FileInputStream arquivoI;
    public InputStreamReader input;
    public BufferedReader br;

    //Criar e copiar arquivos
    public DataOutputStream data;
    @FXML
    public Button btnPlayPause;
    public Button btnStop;
    public Button btnForward;
    public Button btnBackward;
    public Button btnAddMusic;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public Label lblDisplay;

    public AnchorPane ancpBackground;

    public ListView lstvLista;

    public Controller(){
        isPlaying = false;
        fileChooser = new FileChooser();
        try {
            media = new Media(new File(africaPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }catch (Exception e){
            System.out.println("Caminho não encontrado: " + e);
        }

        //Configurando os leitores e escritores de arquivos txt
        try {
            arquivoO = new FileOutputStream("");
            pr = new PrintWriter(arquivoO);

            arquivoI = new FileInputStream("");
            input = new InputStreamReader(arquivoI);
            br = new BufferedReader(input);
        }catch (FileNotFoundException e){
            System.out.println("Arquivo não encontrado");
        }

        //Definindo que tipo de arquivos o FileChooser pode pegar
        //fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo MP3", "*.mp3"));
    }

    public void btnPlayPauseOnAction(ActionEvent event) {
        try {
            lblDisplay.setText("Now playing:" +  media.toString());
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

    public void btnAddMusicOnAction(ActionEvent event){
        File file = fileChooser.showOpenDialog(null);
        //System.out.println(file.getName());
    }

    //Métodos úteis

    public void definirLeitor(String uri){
        try {
            arquivoO = new FileOutputStream(new File(uri).toURI().toString());
        }catch (FileNotFoundException e){
            System.out.println("Arquivo não encontrado!");
        }
    }

    public void definirEscritor(String uri){
        try {
            arquivoI = new FileInputStream(new File(uri).toURI().toString());
        }catch (FileNotFoundException e){
            System.out.println("Arquivo não encontrado!");
        }
    }

    public boolean isPlaying;
}
