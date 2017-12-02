package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Controller {

    private Media media;
    private MediaPlayer mediaPlayer;
    private PlaylistPrincipal playlistMySongs;

    @FXML
    public Button btnPlayPause;
    public Button btnStop;
    public Button btnForward;
    public Button btnBackward;
    public Button btnAddMusic;
    public Button btnCreateNewPlaylist;
    public Button btnLoadPlaylist;
    public ToggleButton tgbSwitchMode;
    public Button btnAddMusicToPlaylist = new Button();
    public Button btnRemoveMusicFromPlaylist = new Button();
    public Button btnShufflePlaylist;
    public Button btnAlphabeticSortPlaylist;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public Label lblDisplay;

    public AnchorPane ancpBackground;

    private MyThread threadBarra;

    public ListView<String> lstvLista = new ListView<String>();

    private boolean isPlaying;
    private boolean modeOne;
    public String currentSong;
    public ObservableList<String> currentList;
    public ArrayList<Playlist> playlistList = new ArrayList();

//    https://docs.oracle.com/javafx/2/ui_controls/list-view.htm

    private final String songPath = "resources/songs/";


    public String africaPath = songPath + "Africa.mp3";

    public Controller(){

        isPlaying = false;
        modeOne = true;

        try {
            //Depois ajeitar o trycatch, não sei se fica necessário fazer isso aqui, pode ser no playPause msm...
            //Se bem que seria util você colocar uma música inicial p/ prevenção de erros
            //Ou carregar a pasta songs em vez de alguma playlist
            media = new Media(new File(africaPath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }catch (Exception e){
            System.out.println("Caminho não encontrado: " + e);
        }

        //Criando a playlist MySongs
        playlistMySongs = new PlaylistPrincipal();
    }

    public void btnPlayPauseOnAction(ActionEvent event) {
        try {
            lblDisplay.setText("Now playing: " + getNomeDaMusica(media));
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.play();
                isPlaying = true;
                threadBarra = new MyThread(this, mediaPlayer, sldProgressBar);
                threadBarra.start();
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

        try {
            lblDisplay.setText(" ");
        } catch (Exception e){
            System.out.println("Erro de label");
        }

        try{
            sldProgressBar.setValue(0);
        } catch (Exception e){
            System.out.println("Erro na barra de progresso");
        }
    }

    public void btnForwardOnAction(ActionEvent event) {
//        media = new Media(new File(fileChooser.showOpenDialog());
//        mediaPlayer = new MediaPlayer(media);
    }

    public void btnBackwardOnAction(ActionEvent event) {

    }

    public void btnCreateNewPlaylistOnAction(ActionEvent event){
        Playlist newPlaylist = new Playlist();
        newPlaylist.criarPlaylist(playlistList, newPlaylist);
    }

    public void btnLoadPlaylistOnAction(ActionEvent event){
        if (currentList != null){
            currentList.removeAll();
        }
        if (playlistList.size() != 0){
            for (int i = 0; i < playlistList.size(); i++) {
                currentList.add(playlistList.get(i).getNome());
            }
        }

        lstvLista.setItems(currentList);
    }

    public void tgbSwitchModeOnAction(ActionEvent event){
        if (modeOne){
            btnAddMusic.setVisible(false);
            btnCreateNewPlaylist.setVisible(false);
            btnLoadPlaylist.setVisible(false);

            btnAddMusicToPlaylist.setVisible(true);
            btnRemoveMusicFromPlaylist.setVisible(true);

            modeOne = false;
        } else {
            btnAddMusic.setVisible(true);
            btnCreateNewPlaylist.setVisible(true);
            btnLoadPlaylist.setVisible(true);

            btnAddMusicToPlaylist.setVisible(false);
            btnRemoveMusicFromPlaylist.setVisible(false);

            modeOne = true;
        }
    }
    public void btnAddMusicOnAction(ActionEvent event){
        playlistMySongs.adicionarMusica();
        //System.out.println(file.getName());
    }

    public void btnAddMusicToPlaylistOnAction(ActionEvent event){
        System.out.println(playlistList.size());
    }

    public void btnRemoveMusicFromPlaylistOnAction(ActionEvent event){

    }

    public void btnShufflePlaylistOnAction(ActionEvent event){

    }

    public void btnAlphabeticSortPlaylistOnAction(ActionEvent event){

    }

    //Métodos úteis

    public String getNomeDaMusica(Media media){
        String songName = new File(media.getSource()).getName();
        return songName.substring(0, songName.length() - 4);
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
