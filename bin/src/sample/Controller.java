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
import sample.exceptions.MusicaNaoSelecionadaException;
import sample.exceptions.PlaylistNaoSelecionadaException;


import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
    public Button btnCarregarPlaylist;
    public Button btnAddMusicToPlaylist = new Button();
    public Button btnRemoveMusicFromPlaylist = new Button();
    public Button btnOrder;
    public Button btnConfirmSong;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public Label lblDisplay;
    public Label lblStatus;

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
    private final String playlistListPath = "resources/playlists/utils/playlistList.txt";
    private final String playlistsPath = "resources/playlists/";
    private final String songPath = "resources/songs/";
    private final String songListPath = "resources/playlists/utils/playlistMySongs.txt";

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
        loadDataFromTxt();
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

            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
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
        ordenarTxt(playlistListPath);
    }

    public void tgbSwitchModeOnAction(ActionEvent event) {
        if (tgbSwitchMode.isSelected()) {
            setLayout(1);
            setListViewSongs(songList);
            lblStatus.setText("Minhas músicas");
        } else {
            setLayout(2);
            setListViewPlaylists(playlistList);
            lblStatus.setText("Playlists");
        }
    }

    public void btnAddMusicOnAction(ActionEvent event) {
        playlistMySongs.adicionarMusica();
        atualizarSongList();
        ordenarTxt(songListPath);
        //System.out.println(file.getName());
    }

    public void atualizarSongList(){
        definirLeitor(songListPath);
        String linha = "";
        songList = new ArrayList<>();
        do{
            try {
                linha = leitor.readLine();
                if (linha != null) {
                    songList.add(linha);
                }
            }catch (IOException e){
                System.out.println("Erro ao atualizar o songList.");
            }
        }while (linha != null);
    }

    public void btnCarregarPlaylistOnAction(ActionEvent event){
        setListViewSongsFromPlaylist(getItemSelecionadoList());
        setLayout(1);
        lblStatus.setText("Playlist atual: " + selectedPlaylist.getNome());
    }

    public void btnAddMusicToPlaylistOnAction(ActionEvent event) {
        try {
            adicionarMusicaToPlaylist();
        }catch (PlaylistNaoSelecionadaException e){
            System.out.println("Nao ha uma playlist selecionada!");
        }
    }

    public void btnConfirmSongOnAction(ActionEvent event){
        try {
            selectedPlaylist.adicionarMusica(getItemSelecionadoList());
        }catch (MusicaNaoSelecionadaException e){
            System.out.println("Nenhuma musica esta selecionada! Por favor, selecione!");
        }
        ordenarTxt(playlistsPath + selectedPlaylist.getNome());
        lblStatus.setText("Playlist atual: " + selectedPlaylist.getNome());
        setListViewSongsFromPlaylist(selectedPlaylist.getNome());

        btnConfirmSong.setVisible(false);
        tgbSwitchMode.setDisable(false);
        btnAddMusicToPlaylist.setDisable(false);
        btnRemoveMusicFromPlaylist.setDisable(false);
    }

    public void btnRemoveMusicFromPlaylistOnAction(ActionEvent event) {

    }

    public void btnOrderOnAction(ActionEvent event) {
        if (alphabeticOrder) {
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

    public void lstvListaOnDragDone(ActionEvent event) {
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
            if(files[i].getName().endsWith(".txt")) {
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
        if (!arquivo.exists()) {
            throw new MusicaNaoEncontradaException();
        }
        media = new Media(arquivo.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        currentSong = nomeMusica;
    }

    private String getItemSelecionadoList() {
        String nomeMusica = lstvLista.getSelectionModel().getSelectedItems().toString();
        nomeMusica = nomeMusica.replaceAll("\\[", "");
        nomeMusica = nomeMusica.replace("]", "");
        return nomeMusica;
    }

    private void setListViewSongsFromPlaylist(String nomePlaylist){
        definirLeitor(playlistsPath + nomePlaylist + ".txt");
        ArrayList<String> songsNames = new ArrayList<>();

        String linha = "";
        do{
            try{
                linha = leitor.readLine();
                if(linha != null){
                    songsNames.add(linha);
                }
            }catch (IOException e){
                System.out.println("Erro ao mudar a listview para exibir as musicas da playlist");
            }
        }while (linha != null);
        currentList = FXCollections.observableArrayList(songsNames);
        selectedPlaylist = loadPlaylist(nomePlaylist);
        lstvLista.setItems(currentList);
    }

    private void ordenarTxt(String uri) {
        ArrayList<String> arquivoTxtArray = new ArrayList<>();
        definirLeitor(uri);

        String linha = "";
        do {
            try {
                linha = leitor.readLine();
                if (linha != null) {
                    arquivoTxtArray.add(linha);
                }
            } catch (IOException e) {
                System.out.println("Erro ao ordenar o arquivo txt!");
            }
        } while (linha != null);
        Collections.sort(arquivoTxtArray);

        //Pedindo pro escritor reescrever em cima do arquivo
        try {
            fileWriter = new FileWriter(uri, false);
            escritor = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            System.out.println("Impossivel deletar o arquivo de texto para organiza-lo");
        }
        try {
            for (String linhaTxt :
                    arquivoTxtArray) {
                escritor.write(linhaTxt);
                escritor.newLine();
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Impossivel escrever no arquivo de texto para organiza-lo");
        }
        System.out.println("Organizado!");
    }

    //Setar a playlist selecionada ao clicar em "Carregar playlist"
    private Playlist loadPlaylist(String playlistName){
        for (int i = 0; i < playlistList.size(); i++) {
            if(playlistList.get(i).getNome().equalsIgnoreCase(playlistName)){
                return playlistList.get(i);
            }
        }
        return null;
    }

    public void setLayout(int num){
        switch (num){
            case 1:
                btnAddMusic.setVisible(false);
                btnCreateNewPlaylist.setVisible(false);
                btnCarregarPlaylist.setVisible(false);

                btnAddMusicToPlaylist.setVisible(true);
                btnRemoveMusicFromPlaylist.setVisible(true);
                break;
            case 2:
                btnAddMusic.setVisible(true);
                btnCreateNewPlaylist.setVisible(true);
                btnCarregarPlaylist.setVisible(true);

                btnAddMusicToPlaylist.setVisible(false);
                btnRemoveMusicFromPlaylist.setVisible(false);
                break;
        }
    }

    public void adicionarMusicaToPlaylist() throws PlaylistNaoSelecionadaException{
        if(selectedPlaylist == null){
            throw new PlaylistNaoSelecionadaException();
        }else{
            setListViewSongs(songList);
            btnConfirmSong.setVisible(true);
            tgbSwitchMode.setDisable(true);
            btnAddMusicToPlaylist.setDisable(true);
            btnRemoveMusicFromPlaylist.setDisable(true);
        }
    }
}
