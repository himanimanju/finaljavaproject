package com.example.javafxprojectcsa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurve;

import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {
    private int score;
    private double angle;
    private int successfulShots = 0;

    @FXML
    TextField angleText;
    @FXML
    Label errorLabel;
    @FXML
    Button shootBtn;
    @FXML
    ImageView hoopImage;
    @FXML
    ImageView ballImage;
    @FXML
    Label shotsMadeLabel;
    @FXML
    ImageView personImage;
    @FXML
    QuadCurve testCurve;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label shotResultLabel;

    ///trying
    private double[] pathX;
    private double[] pathY;
    //private int pathIndex = 0;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("src/main/resources/Image/hoopImage.png");
        Image image = new Image(file.toURI().toString());
        hoopImage.setImage(image);

        File file1 = new File("src/main/resources/Image/basketball.png");
        Image image1 = new Image(file1.toURI().toString());
        ballImage.setImage(image1);

        File file2 = new File("src/main/resources/Image/personImage.png");
        Image image2 = new Image(file2.toURI().toString());
        personImage.setImage(image2);
    }

    @FXML
    private void onAngleBtnClick() {
        shotResultLabel.setVisible(false);
        errorLabel.setText("");
        String input = angleText.getText();

        try {
            int number = Integer.parseInt(input);

            if (number < 0 || number > 90) {
                errorLabel.setText("Invalid input. Enter a number from 0 to 90");
            } else {
                errorLabel.setText("Angle entered is: " + number);
                shootBtn.setVisible(true);

                angle = number * Math.PI / 180;
                angleText.clear();
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid input. Please enter an integer");
        }
        errorLabel.setVisible(true);
    }

    @FXML
    private void onShootBtnClick(){

    }

    private void calculateShotPath(double startX, double startY, double endX, double endY, int v1) {

    }

    private boolean isShotMade(double endX, double endY) {
        return false;
    }
}


