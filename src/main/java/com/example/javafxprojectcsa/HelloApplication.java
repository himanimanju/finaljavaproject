package com.example.javafxprojectcsa;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene sceneHome = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Start Screen");
        stage.setScene(sceneHome);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}