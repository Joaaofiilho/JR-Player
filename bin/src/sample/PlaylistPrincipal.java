package sample;

import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class PlaylistPrincipal {
    private final String pathArquivo = "resources/playlists/";
    private final String pathCompleto = pathArquivo + "playlistMySongs.txt";
    private final String pathCompletoBackup = pathArquivo + "backup/playlistMySongs.txt";

    private String nome;

    private FileWriter fileWriter;
    private BufferedWriter escritor;

    private FileReader fileReader;
    private BufferedReader leitor;

    private FileChooser fileChooser;
    //Criar e copiar arquivos
    private DataOutputStream data;

    public PlaylistPrincipal(){
        nome = "playlistMySongs";
        fileChooser = new FileChooser();
        //retomarPlaylist();
        //Criando o arquivo txt e definindo os leitores e escritores
        definirEscritor();
        definirLeitor();

        //Definindo que tipo de arquivos o FileChooser pode pegar
        //fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Arquivo MP3", "*.mp3"));
    }

    //A musica vai tanto para o arquivo txt quanto para a pasta songs
    public void adicionarMusica(){
        File arquivo = fileChooser.showOpenDialog(null);
        boolean nulo = false;
        try {
            Files.copy(arquivo.toPath(), new File(pathArquivo + arquivo.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            System.out.println("PICNIC: " + e);
        } catch (NullPointerException e){
            nulo = true;
            System.out.println("Arquivo nulo: " + e);
        }

        if (!nulo){
            addMusicToTxt(arquivo.getName());
        }
    }

    private void addMusicToTxt(String nomeMusica){
            try {
                escritor.write(nomeMusica);
                escritor.newLine();
                escritor.close();
            }catch (IOException e){
                System.out.println("Erro em adicionar musica para o txt: " + e.getMessage());
            }
    }

    private String getNomeMusica(String uri){
        return uri.substring(uri.lastIndexOf('/') + 1, uri.length() - 4);
    }

    public void definirEscritor(){
        try {
            fileWriter = new FileWriter(pathCompleto, true);
            escritor = new BufferedWriter(fileWriter);
        } catch (FileNotFoundException e){
            System.out.println("Arquivo de texto não encontrado!");
        } catch (IOException e) {
            System.out.println("Erro em definir escritor: " + e.getMessage());
        }
    }

    public void definirLeitor(){
        try {
            fileReader = new FileReader(pathCompleto);
            leitor = new BufferedReader(fileReader);
        } catch (FileNotFoundException e){
            System.out.println("Arquivo de texto nao encontrado!");
        }
    }
}
