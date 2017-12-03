package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sample.exceptions.MusicaNaoEncontradaException;


import java.io.*;
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
    public ToggleButton tgbSwitchMode;
    public Button btnAddMusicToPlaylist = new Button();
    public Button btnRemoveMusicFromPlaylist = new Button();
    public Button btnOrder;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public Label lblDisplay;

    public AnchorPane ancpBackground;

    private MyThread threadBarra;

    public ListView<String> lstvLista;

    private boolean isPlaying;
    private boolean modeOne;
    public boolean alphabeticOrder;
    public String currentSong;
    private ObservableList<String> currentList;
    private ArrayList<Playlist> playlistList;
    private ArrayList<String> songList;

    private Playlist selectedPlaylist;

    private FileWriter fileWriter;
    private BufferedWriter escritor;

    private FileReader fileReader;
    private BufferedReader leitor;
    //    https://docs.oracle.com/javafx/2/ui_controls/list-view.htm
    private final String playlistListPath = "resources/playlists/playlistList.txt";
    private final String playlistsPath = "resources/playlists/";
    private final String songPath = "resources/songs/";
    public String africaPath = songPath + "Africa.mp3";

    public Controller() {

        isPlaying = false;
        modeOne = true;
        alphabeticOrder = true;

        try {
            //Depois ajeitar o trycatch, não sei se fica necessário fazer isso aqui, pode ser no playPause msm...
            //Se bem que seria util você colocar uma música inicial p/ prevenção de erros
            //Ou carregar a pasta songs em vez de alguma playlist
//            media = new Media(new File(africaPath).toURI().toString());
//            mediaPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            System.out.println("Caminho não encontrado: " + e);
        }

        //Criando a playlist MySongs
        playlistMySongs = new PlaylistPrincipal();
        playlistList = new ArrayList<>();
        currentList = FXCollections.observableArrayList();
        songList = new ArrayList<>();
//        loadDataFromTxt();
        //setListView(songList);
        /*
        ListView<String> list = new ListView<String>();
ObservableList<String> items =FXCollections.observableArrayList (
    "Single", "Double", "Suite", "Family App");
list.setItems(items);
         */
    }

    public void btnPlayPauseOnAction(ActionEvent event) {

        try {
            //Quando ele seta a musica aparentemente o programa comeca a tocar musica em cima de
            //musica
            setMusica(getItemSelecionadoList());
            lblDisplay.setText("Now playing: " + getNomeDaMusica(media));

            if(isPlaying){
                mediaPlayer.pause();
                isPlaying = false;
            }else {
                mediaPlayer.play();
                isPlaying = true;
                threadBarra = new MyThread(this, mediaPlayer, sldProgressBar);
                threadBarra.start();
            }
//            if (isPlaying && getItemSelecionadoList().equals(currentSong)) {
//                System.out.println("aaaaaaaa");
//                mediaPlayer.pause();
//                isPlaying = false;
//            }
//
//            if(isPlaying && !getItemSelecionadoList().equals(currentSong)){
//                System.out.println("Teste!");
//                mediaPlayer.stop();
//                threadBarra.stop();
//
//                setMusica(getItemSelecionadoList());
//                lblDisplay.setText("Now playing: " + getNomeDaMusica(media));
//
//                mediaPlayer.play();
//                isPlaying = true;
//            }
//            if(!isPlaying && getItemSelecionadoList().equals(currentSong)){
//                System.out.println("bbbbbbbbbb");
//                mediaPlayer.play();
//                isPlaying = true;
//                threadBarra = new MyThread(this, mediaPlayer, sldProgressBar);
//                threadBarra.start();
//            }


        } catch (MusicaNaoEncontradaException e) {
            System.out.println("Música não encontrada: " + e);
        }
    }

    public void btnStopOnAction(ActionEvent event) {
        try {
            media = null;
            mediaPlayer.stop();
            lblDisplay.setText(" ");
            sldProgressBar.setValue(0);
        } catch (Exception e) {
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

    public void btnCreateNewPlaylistOnAction(ActionEvent event) {
        Playlist newPlaylist = new Playlist();
        newPlaylist.criarPlaylist(playlistList, newPlaylist);
    }

    public void tgbSwitchModeOnAction(ActionEvent event) {
        if (tgbSwitchMode.isSelected()) {
            btnAddMusic.setVisible(false);
            btnCreateNewPlaylist.setVisible(false);

            btnAddMusicToPlaylist.setVisible(true);
            btnRemoveMusicFromPlaylist.setVisible(true);
            setListViewPlaylists(playlistList);
        } else {
            btnAddMusic.setVisible(true);
            btnCreateNewPlaylist.setVisible(true);

            btnAddMusicToPlaylist.setVisible(false);
            btnRemoveMusicFromPlaylist.setVisible(false);

            setListViewSongs(songList);
        }
    }

    public void btnAddMusicOnAction(ActionEvent event) {
        playlistMySongs.adicionarMusica();

        //System.out.println(file.getName());
    }

    public void btnAddMusicToPlaylistOnAction(ActionEvent event) {
//        currentList =
//        selectedPlaylist.adicionarMusica();
    }

    public void btnRemoveMusicFromPlaylistOnAction(ActionEvent event) {

    }

    public void btnOrderOnAction(ActionEvent event) {
        if (alphabeticOrder){
            FXCollections.sort(currentList, String::compareTo);

            alphabeticOrder = false;
        } else {
            FXCollections.shuffle(currentList);

            alphabeticOrder = true;
        }
    }

    public void lstvListaOnEditStart(ActionEvent event) {

    }

    public void lstvListaOnEditCommit(ActionEvent event) {
        System.out.println(lstvLista.getSelectionModel());
        System.out.println("Teste");
    }
    //Métodos úteis

    public String getNomeDaMusica(Media media) {
        String songName = new File(media.getSource()).getName();
        return songName.substring(0, songName.length() - 4);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void loadDataFromTxt() {
        //Definindo as Playlists
        File arquivo = new File(playlistsPath);
        File files[] = arquivo.listFiles();
        for (int i = 0; i < files.length; i++) {
            String nomePlaylist = files[i].getName().substring(0, files[i].getName().length() - 4);
            //A pasta 'utils' entra no meio dos arquivos. Apenas criei a excecao
            if (!nomePlaylist.equals("u")) {
                Playlist playlist = new Playlist();
                playlist.setNome(nomePlaylist);
                playlist.loadSongsFromTxt();
                //Adicionando cada playlist ao txt
                playlistList.add(playlist);
            }
        }
        definirLeitor("resources/playlists/utils/playlistMySongs.txt");
        String linha = "";
        do {
            try {
                linha = leitor.readLine();
                if (linha != null) {
                    songList.add(linha);
                }
            } catch (IOException e) {
                System.out.println("Erro ao transferir os dados do arquivo txt para o songList");
            }
        } while (linha != null);
    }

    public void definirEscritor(String uri) {
        try {
            fileWriter = new FileWriter(uri, true);
            escritor = new BufferedWriter(fileWriter);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de texto não encontrado!");
        } catch (IOException e) {
            System.out.println("aaa: " + e.getMessage());
        }
    }

    public void definirLeitor(String uri) {
        try {
            fileReader = new FileReader(uri);
            leitor = new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de texto nao encontrado!");
        }
    }

    public void setListViewPlaylists(ArrayList<Playlist> playlists) {
        currentList.removeAll();
        ArrayList<String> playlistListNames = new ArrayList<>();
        for (Playlist aPlaylistList : playlists) playlistListNames.add(aPlaylistList.getNome());
        currentList = FXCollections.observableArrayList(playlistListNames);
        lstvLista.setItems(currentList);
    }

    public void setListViewSongs(ArrayList<String> songs) {
        currentList.removeAll();
        ArrayList<String> songNames = new ArrayList<>();
        for (String song : songs) songNames.add(song);
        currentList = FXCollections.observableArrayList(songNames);
        lstvLista.setItems(currentList);
    }

    public void setMusica(String nomeMusica) throws MusicaNaoEncontradaException {
        nomeMusica = getItemSelecionadoList();
        File arquivo = new File(songPath + nomeMusica);
        if(!arquivo.exists()){
            throw new MusicaNaoEncontradaException();
        }
        media = new Media(arquivo.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.pause();
        currentSong = nomeMusica;
    }

    private String getItemSelecionadoList(){
        String nomeMusica = lstvLista.getSelectionModel().getSelectedItems().toString();
        nomeMusica = nomeMusica.replaceAll("\\[", "");
        nomeMusica = nomeMusica.replace("]", "");
        return nomeMusica;
    }
}
