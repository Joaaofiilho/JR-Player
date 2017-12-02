package sample;

import javafx.collections.ObservableList;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Playlist {
    private String nome;

    //Ler e escrever em arquivos de texto
    private FileWriter fileWriter;
    private BufferedWriter escritor;

    private FileReader fileReader;
    private BufferedReader leitor;

    public void definirEscritor(String uri){
        try {
            fileWriter = new FileWriter(uri, true);
            escritor = new BufferedWriter(fileWriter);
        } catch (FileNotFoundException e){
            System.out.println("Arquivo de texto não encontrado!");
        } catch (IOException e) {
            System.out.println("aaa: " + e.getMessage());
        }
    }

    public void definirLeitor(String uri){
        try {
            fileReader = new FileReader(uri);
            leitor = new BufferedReader(fileReader);
        } catch (FileNotFoundException e){
            System.out.println("Arquivo de texto nao encontrado!");
        }
    }

    public void criarPlaylist (ArrayList<Playlist> playlistList, Playlist playlist) {
        String playlistName;
        boolean repeat = false;
        String digiteONome;

        do {
            if (!repeat){
                digiteONome = "Digite o nome da playlist:";
            } else {
                digiteONome = "Nome já existe, digite outro:";
            }

            try {
                playlistName = JOptionPane.showInputDialog(null, digiteONome, "Nome da playlist", JOptionPane.QUESTION_MESSAGE);
                if (playlistName == null){
                    repeat = false;
                    throw new NullPointerException();
                } else {
                    this.definirLeitor("resources/playlists/playlistList.txt");

                    //Pesquisar no playlistList.txt se ha alguma linha igual a playlistName
                    //Se houver, repeat = true
                    repeat = false;

                    if (!repeat){
                        playlist.setNome(playlistName);
                        playlist.escreverNaPlaylistList(playlistName);
                        playlist.escreverNoArray(playlistList, playlist);
                    }
                }
            } catch (NullPointerException e){
                System.out.println("Nome nulo, playlist nao criada: " + e);
            } catch (Exception e) {
                System.out.println("Playlist ja existe: " + e);
            }
        } while (repeat);
    }

    public void escreverNaPlaylistList(String nome) {
        this.definirEscritor("resources/playlists/playlistList.txt");
        try {
            escritor.write(nome);
            escritor.newLine();
            escritor.close();
        } catch (Exception e){
            System.out.println("Erro escrevendo na lista de playlists: " + e);
        }
    }

    public void escreverNoArray (ArrayList list, Playlist playlist){
        list.add(playlist);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
