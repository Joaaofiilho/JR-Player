package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;

import java.io.*;

public class Controller {

    @FXML
    public Button btnPlayPause;
    public Button btnStop;
    public Button btnForward;
    public Button btnBackward;

    public Slider sldProgressBar;
    public Slider sldVolumeBar;

    public AnchorPane ancpBackground;


    public void btnPlayPauseOnAction(ActionEvent event) {

    }

    public void btnStopOnAction(ActionEvent event) {

    }

    public void btnForwardOnAction(ActionEvent event) {

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
