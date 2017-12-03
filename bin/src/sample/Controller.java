package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import sample.exceptions.MusicaJaExisteException;
import sample.exceptions.MusicaNaoEncontradaException;
import sample.exceptions.MusicaNaoSelecionadaException;
import sample.exceptions.PlaylistNaoSelecionadaException;


import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
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
    public Button btnLoadPlaylist;
    public Button btnAddMusicToPlaylist = new Button();
    public Button btnRemoveMusicFromPlaylist = new Button();
    public Button btnOrder;
    public Button btnConfirmSong;
    public Button btnRemovePlaylist;
    public Button btnCancelSong;

    public ToggleButton tgbSwitchMode;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public Label lblDisplay;
    public Label lblStatus;

    public AnchorPane ancpBackground;

    private MyThread threadBarra;

    public ListView<String> lstvLista = new ListView<>();

    private boolean isPlaying;
    public boolean alphabeticOrder;
    public boolean songSelected;
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
        alphabeticOrder = true;
        songSelected = false;

        //Criando a playlist MySongs
        playlistMySongs = new PlaylistPrincipal();
        playlistList = new ArrayList<>();
        songList = new ArrayList<>();
        currentList = FXCollections.observableArrayList(songList);
        loadDataFromTxt();

    }

    public void initialize() {
        tgbSwitchMode.fire();
        tgbSwitchMode.fire();
    }

    public void btnPlayPauseOnAction(ActionEvent event) {
        try {
            if (!songSelected) {
                setSong(getSelectedListItem());
                songSelected = true;
            }
            if (!getSelectedListItem().equalsIgnoreCase(getNomeDaMusica(media))) {
                lblDisplay.setText("");
                mediaPlayer.stop();
                setSong(getSelectedListItem());
                songSelected = true;
            }

            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.play();
                isPlaying = true;
                threadBarra = new MyThread(this, mediaPlayer, sldProgressBar);
                threadBarra.start();
                lblDisplay.setText("Now playing: " + getNomeDaMusica(media).substring(0, getNomeDaMusica(media).length() - 4));
            }
//            if (isPlaying && getSelectedListItem().equals(currentSong)) {
//                System.out.println("aaaaaaaa");
//                mediaPlayer.pause();
//                isPlaying = false;
//            }
//
//            if(isPlaying && !getSelectedListItem().equals(currentSong)){
//                System.out.println("Teste!");
//                mediaPlayer.stop();
//                threadBarra.stop();
//
//                setSong(getSelectedListItem());
//                lblDisplay.setText("Now playing: " + getNomeDaMusica(media));
//
//                mediaPlayer.play();
//                isPlaying = true;
//            }
//            if(!isPlaying && getSelectedListItem().equals(currentSong)){
//                System.out.println("bbbbbbbbbb");
//                mediaPlayer.play();
//                isPlaying = true;
//                threadBarra = new MyThread(this, mediaPlayer, sldProgressBar);
//                threadBarra.start();
//            }


        } catch (MusicaNaoEncontradaException e) {
            System.out.println("Música não foi encontrada!");
        } catch (MediaException e) {
            System.out.println("Nenhuma música foi selecionada!");
        }
    }

    public void btnStopOnAction(ActionEvent event) {
        try {
            songSelected = false;
            media = null;
            mediaPlayer.stop();
            lblDisplay.setText(" ");
            sldProgressBar.setValue(0);
        } catch (Exception e) {
            System.out.println("Nenhuma música está tocando!");
        }
        isPlaying = false;
    }

    public void btnForwardOnAction(ActionEvent event) {
        try {
            avancarMusica(currentSong);
        }catch (PlaylistNaoSelecionadaException e){
            System.out.println("Nenhuma playlist foi selecionada!");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Não há uma música posterior!");
        }
    }

    public void btnBackwardOnAction(ActionEvent event) {
        try {
            voltarMusica(currentSong);
            threadBarra = new MyThread(this, mediaPlayer, sldProgressBar);
            threadBarra.start();
            mediaPlayer.play();
        }catch (PlaylistNaoSelecionadaException e){
            System.out.println("Nenhuma playlist foi selecionada!");
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Não há uma música anterior!");
        }
    }

    public void avancarMusica(String nomeMusica) throws PlaylistNaoSelecionadaException, ArrayIndexOutOfBoundsException{
        if(lstvLista.getItems() == null){
            throw new PlaylistNaoSelecionadaException();
        }else{
            for (int i = 0; i < lstvLista.getItems().size(); i++) {
                try {
                    //Aqui eu pego a lstvView inteira e vejo qual a musica anterior e seto o som com base no nome da
                    //musica que eu achei
                    if (getSelectedListItem().equalsIgnoreCase(nomeMusica)) {
                        if(!(lstvLista.getSelectionModel().getSelectedIndex() == lstvLista.getItems().size()-1)) {
                            mediaPlayer.stop();
                            setSong(lstvLista.getItems().get(lstvLista.getSelectionModel().getSelectedIndex() + 1));
                        }
                    }else throw new ArrayIndexOutOfBoundsException();
                }catch (MusicaNaoEncontradaException e){
                    System.out.println("Não existe uma música posterior!");
                }
            }
            isPlaying = false;
        }
    }

    public void voltarMusica(String nomeMusica) throws PlaylistNaoSelecionadaException, ArrayIndexOutOfBoundsException{
        if(lstvLista.getItems() == null){
            throw new PlaylistNaoSelecionadaException();
        }else{
            for (int i = 0; i < lstvLista.getItems().size(); i++) {
                try {
                    //Aqui eu pego a lstvView inteira e vejo qual a musica anterior e seto o som com base no nome da
                    //musica que eu achei
                    if (getSelectedListItem().equalsIgnoreCase(nomeMusica)) {
                        if(lstvLista.getSelectionModel().getSelectedIndex() > 0) {
                            mediaPlayer.stop();
                            setSong(lstvLista.getItems().get(lstvLista.getSelectionModel().getSelectedIndex() - 1));
                        }
                    }else throw new ArrayIndexOutOfBoundsException();
                }catch (MusicaNaoEncontradaException e){
                    System.out.println("Não existe uma música anterior!");
                }
            }
            isPlaying = false;
        }
    }

    public void btnCreateNewPlaylistOnAction(ActionEvent event) {
        Playlist newPlaylist = new Playlist();
        newPlaylist.criarPlaylist(playlistList, newPlaylist);
        setListViewPlaylists(playlistList);
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

    public void atualizarSongList() {
        definirLeitor(songListPath);
        String linha = "";
        songList = new ArrayList<>();
        do {
            try {
                linha = leitor.readLine();
                if (linha != null) {
                    songList.add(linha);
                }
            } catch (IOException e) {
                System.out.println("Erro ao atualizar o songList.");
            }
        } while (linha != null);
    }

    public void atualizarPlaylistList() {
        definirLeitor(playlistListPath);
        playlistList = new ArrayList<>();
        String linha;
        try {
            do {
                linha = leitor.readLine();
                if (linha != null) {
                    Playlist playlist = new Playlist();
                    playlist.setNome(linha);
                    playlist.loadSongsFromTxt();
                    playlistList.add(playlist);
                }
            } while (linha != null);
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo txt da playlist para removê-la!");
        }

    }


    public void btnCarregarPlaylistOnAction(ActionEvent event) {
        if (getSelectedListItem().equalsIgnoreCase("") || getSelectedListItem().equalsIgnoreCase(" ")) {
            selectedPlaylist = loadPlaylist(getSelectedListItem());
            setListViewPlaylists(playlistList);
            System.out.println("Nada selecionado!");
        } else {
            setListViewSongsFromPlaylist(getSelectedListItem());

            setLayout(1);
            lblStatus.setText("Playlist atual: " + selectedPlaylist.getNome());
            tgbSwitchMode.setSelected(true);

            btnAddMusicToPlaylist.setVisible(true);
            btnRemoveMusicFromPlaylist.setVisible(true);
        }
    }

    public void btnAddMusicToPlaylistOnAction(ActionEvent event) {
        try {
            adicionarMusicaToPlaylist();
        } catch (PlaylistNaoSelecionadaException e) {
            System.out.println("Nao ha uma playlist selecionada!");
        }
    }

    public void btnConfirmSongOnAction(ActionEvent event) {
        try {
            selectedPlaylist.adicionarMusica(getSelectedListItem());
        } catch (MusicaNaoSelecionadaException e) {
            System.out.println("Nenhuma musica esta selecionada! Por favor, selecione!");
        } catch (MusicaJaExisteException e) {
            System.out.println("A música selecionada já existe na playlist!");
        }
        ordenarTxt(playlistsPath + selectedPlaylist.getNome());
        lblStatus.setText("Playlist atual: " + selectedPlaylist.getNome());
        setListViewSongsFromPlaylist(selectedPlaylist.getNome());

        btnConfirmSong.setVisible(false);
        btnCancelSong.setVisible(false);
        tgbSwitchMode.setDisable(false);
        btnAddMusicToPlaylist.setDisable(false);
        btnRemoveMusicFromPlaylist.setDisable(false);
        btnPlayPause.setDisable(false);
        btnBackward.setDisable(false);
        btnForward.setDisable(false);
        btnStop.setDisable(false);
        btnOrder.setDisable(false);
    }

    public void btnRemoveMusicFromPlaylistOnAction(ActionEvent event) {
        try {
            removerMusicaDaPlaylist();
            setListViewSongsFromPlaylist(selectedPlaylist.getNome());
        } catch (PlaylistNaoSelecionadaException e) {
            System.out.println("Nenhuma playlist carregada!");
        }
    }

    public void removerMusicaDaPlaylist() throws PlaylistNaoSelecionadaException {
        if (selectedPlaylist == null) {
            throw new PlaylistNaoSelecionadaException();
        } else {
            selectedPlaylist.removeSong(getSelectedListItem());
        }
    }

    public void btnOrderOnAction(ActionEvent event) {
        if (tgbSwitchMode.isSelected()) {
            if (alphabeticOrder && tgbSwitchMode.isSelected()) {
                FXCollections.sort(currentList, String::compareTo);

                alphabeticOrder = false;
            } else if (tgbSwitchMode.isSelected()) {
                FXCollections.shuffle(currentList);

                alphabeticOrder = true;
            }
        }
    }

    public void lstvListaOnEditStart(ActionEvent event) {

    }

    public void lstvListaOnEditCommit(ActionEvent event) {
        System.out.println(lstvLista.getSelectionModel());
        System.out.println("Teste");
    }

    public void btnRemovePlaylistOnAction(ActionEvent event) {
        try {
            selectedPlaylist = loadPlaylist(getSelectedListItem());
            removePlaylist(getSelectedListItem());
            atualizarPlaylistList();
            setListViewPlaylists(playlistList);
        } catch (PlaylistNaoSelecionadaException e) {
            System.out.println("Nenhuma playlist foi selecionada!");
        }
    }

    public void removePlaylist(String nomePlaylist) throws PlaylistNaoSelecionadaException {
        if (selectedPlaylist == null) {
            throw new PlaylistNaoSelecionadaException();
        } else {
            definirLeitor(playlistListPath);
            ArrayList<String> newPlaylistList = new ArrayList<>();
            String linha;
            try {
                do {
                    linha = leitor.readLine();
                    if (linha != null && !linha.equalsIgnoreCase(nomePlaylist)) {
                        newPlaylistList.add(linha);
                    }
                } while (linha != null);
                leitor.close();
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo txt da playlist para removê-la!");
            }
            try {
                fileWriter = new FileWriter(playlistListPath, false);
                escritor = new BufferedWriter(fileWriter);

                for (String playlistName :
                        newPlaylistList) {
                    escritor.write(playlistName);
                    escritor.newLine();
                }
                escritor.close();
            } catch (IOException e) {
                System.out.println("Erro ao escrever no arquivo txt da playlist para removê-la!");
            }
            escritor = new BufferedWriter(fileWriter);

            try{
                System.out.println(playlistsPath + nomePlaylist + ".txt");
                Files.delete(new File(playlistsPath + nomePlaylist + ".txt").toPath());
            }catch (IOException e){
                System.out.println("Erro ao deletar o arquivo txt da pasta de playlists!");
            }
        }
    }

    public void btnCancelSongOnAction(ActionEvent event) {
        btnConfirmSong.setVisible(false);
        btnCancelSong.setVisible(false);
        tgbSwitchMode.setDisable(false);
        btnAddMusicToPlaylist.setDisable(false);
        btnRemoveMusicFromPlaylist.setDisable(false);
        btnPlayPause.setDisable(false);
        btnBackward.setDisable(false);
        btnForward.setDisable(false);
        btnStop.setDisable(false);
        btnOrder.setDisable(false);

        setListViewSongsFromPlaylist(selectedPlaylist.getNome());
    }


    //Métodos úteis

    public String getNomeDaMusica(Media media) {
        return new File(media.getSource()).getName();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    private void loadDataFromTxt() {
        //Definindo as Playlists
        File arquivo = new File(playlistsPath);
        File files[] = arquivo.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().endsWith(".txt")) {
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

    public void setSong(String nomeMusica) throws MusicaNaoEncontradaException {
        nomeMusica = getSelectedListItem();
        File arquivo = new File(songPath + nomeMusica);
        if (!arquivo.exists()) {
            throw new MusicaNaoEncontradaException();
        }
        media = new Media(arquivo.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        currentSong = nomeMusica;
    }

    private String getSelectedListItem() {
        String nomeMusica = lstvLista.getSelectionModel().getSelectedItems().toString();
        nomeMusica = nomeMusica.replaceAll("\\[", "");
        nomeMusica = nomeMusica.replace("]", "");
        return nomeMusica;
    }

    private void setListViewSongsFromPlaylist(String nomePlaylist) {
        definirLeitor(playlistsPath + nomePlaylist + ".txt");
        ArrayList<String> songsNames = new ArrayList<>();

        String linha = "";
        do {
            try {
                linha = leitor.readLine();
                if (linha != null) {
                    songsNames.add(linha);
                }
            } catch (IOException e) {
                System.out.println("Erro ao mudar a listview para exibir as musicas da playlist");
            }
        } while (linha != null);
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
    }

    //Setar a playlist selecionada ao clicar em "Carregar playlist"
    private Playlist loadPlaylist(String playlistName) {
        for (int i = 0; i < playlistList.size(); i++) {
            if (playlistList.get(i).getNome().equalsIgnoreCase(playlistName)) {
                return playlistList.get(i);
            }
        }
        return null;
    }

    public void setLayout(int num) {
        btnAddMusicToPlaylist.setVisible(false);
        btnRemoveMusicFromPlaylist.setVisible(false);

        switch (num) {
            case 1:
                btnAddMusic.setVisible(false);
                btnCreateNewPlaylist.setVisible(false);
                btnLoadPlaylist.setVisible(false);
                btnRemovePlaylist.setVisible(false);
                break;
            case 2:
                btnAddMusic.setVisible(true);
                btnCreateNewPlaylist.setVisible(true);
                btnLoadPlaylist.setVisible(true);
                btnRemovePlaylist.setVisible(true);
                break;
        }
    }

    public void adicionarMusicaToPlaylist() throws PlaylistNaoSelecionadaException {
        if (selectedPlaylist == null) {
            throw new PlaylistNaoSelecionadaException();
        } else {
            setListViewSongs(songList);
            btnCancelSong.setVisible(true);
            btnConfirmSong.setVisible(true);
            tgbSwitchMode.setDisable(true);
            btnAddMusicToPlaylist.setDisable(true);
            btnRemoveMusicFromPlaylist.setDisable(true);
            btnPlayPause.setDisable(true);
            btnBackward.setDisable(true);
            btnForward.setDisable(true);
            btnStop.setDisable(true);
            btnOrder.setDisable(true);
        }
    }
}
