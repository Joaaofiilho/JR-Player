package sample;

import java.io.*;

public class Playlist {
    private String nome;
    //Ler e escrever em arquivos de texto
    private FileWriter fileWriter;
    private BufferedWriter escritor;

    private FileReader fileReader;
    private BufferedReader leitor;

    public Playlist(String nome) {
        this.nome = nome;
    }

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
}
