package com.example.javafxprojectcsa;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private void onHelloButtonClick() throws IOException {
        System.out.println("Start");
        welcomeText.setText("Welcome to the Java Basketball Association!");
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game-view.fxml"));
        //Scene sceneHome = new Scene(fxmlLoader.load(), 800, 600);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        //Scene root1 = (Scene) fxmlLoader.load();
        Stage stage = new Stage();
        //stage.initModality(Modality.WINDOW_MODAL);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Game Screen");
        stage.setScene(new Scene(root1));
        stage.show();
        stage.onCloseRequestProperty().setValue(e -> Platform.exit());
        //stage.setTitle("Start Screen");
        //stage.setScene(sceneHome);
        //stage.show();
    }
}