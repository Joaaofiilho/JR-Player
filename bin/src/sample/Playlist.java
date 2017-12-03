package sample;

import sample.exceptions.MusicaNaoEncontradaException;
import sample.exceptions.MusicaNaoSelecionadaException;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

public class Playlist {
    private String nome;

    //Ler e escrever em arquivos de texto
    private FileWriter fileWriter;
    private BufferedWriter escritor;

    private FileReader fileReader;
    private BufferedReader leitor;

    private ArrayList<String> songNames;

    private final String pathPlaylists = "resources/playlists/";

    public Playlist() {
        songNames = new ArrayList<>();
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

    public void criarPlaylist(ArrayList<Playlist> playlistList, Playlist playlist) {
        String playlistName;
        boolean repeat = false;
        String digiteONome;

        do {
            if (!repeat) {
                digiteONome = "Digite o nome da playlist:";
            } else {
                digiteONome = "Nome já existe, digite outro:";
            }
            repeat = false;
            try {
                playlistName = JOptionPane.showInputDialog(null, digiteONome, "Nome da playlist", JOptionPane.QUESTION_MESSAGE);
                //Pesquisar no playlistList.txt se ha alguma linha igual a playlistName
                //Se houver, repeat = true
                this.definirLeitor("resources/playlists/utils/playlistList.txt");
                String linha;
                do {
                    linha = leitor.readLine();
                    if (linha != null && linha.equalsIgnoreCase(playlistName)) {
                        repeat = true;
                    }
                } while (linha != null);

                if (!repeat && (playlistName != null && !playlistName.equals(""))) {
                    playlist.setNome(playlistName);
                    playlist.escreverNaPlaylistList(playlistName);
                    playlist.escreverNoArray(playlistList, playlist);
                    File arquivo = new File("resources/playlists/" + playlistName + ".txt");
                    Files.createFile(arquivo.toPath());
//                    try {
//                        Files.copy(arquivo.toPath(), new File("resources/playlists/" + arquivo.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    } catch (IOException e) {
//                        System.out.println("PICNIC: " + e);
//                    }
                    repeat = false;
                }
            } catch (NullPointerException e) {
                System.out.println("Nome nulo, playlist nao criada: " + e);
            } catch (Exception e) {
                System.out.println("Playlist ja existe: " + e);
            }
        } while (repeat);
    }

    public void escreverNaPlaylistList(String nome) {
        this.definirEscritor("resources/playlists/utils/playlistList.txt");
        try {
            escritor.write(nome);
            escritor.newLine();
            escritor.close();
        } catch (Exception e) {
            System.out.println("Erro escrevendo na lista de playlists: " + e);
        }
    }

    public void loadSongsFromTxt() {
        definirEscritor(pathPlaylists + this.getNome() + ".txt");
        definirLeitor(pathPlaylists + this.getNome() + ".txt");
        String linha;
        try {
            do {
                linha = leitor.readLine();
                if (linha != null) {
                    songNames.add(linha);
                }
            } while (linha != null);
        } catch (IOException e) {
            System.out.println("Erro ao carregar as musicas para a playlist pelo arquivo txt: " + e);
        }
        Collections.sort(songNames);
    }

    public void adicionarMusica(String nomeMusica) throws MusicaNaoSelecionadaException{
        if(nomeMusica == null || nomeMusica.equals("")){
            throw new MusicaNaoSelecionadaException();
        }else {
            definirEscritor(pathPlaylists + this.getNome() + ".txt");
            definirLeitor(pathPlaylists + this.getNome() + ".txt");
            try {
                escritor.write(nomeMusica);
                escritor.newLine();
                escritor.close();
            } catch (IOException e) {
                System.out.println("Erro ao adicionar uma musica na playlist");
            }
        }
    }

//    public void setlistView(ArrayList<Playlist> playlistList){
//        ArrayList<String> playlistListNames = new ArrayList<>();
//        for (Playlist aPlaylistList : playlistList) playlistListNames.add(aPlaylistList.getNome());
//        currentList = FXCollections.observableArrayList(playlistListNames);
//    }

    public void escreverNoArray(ArrayList list, Playlist playlist) {
        list.add(playlist);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
