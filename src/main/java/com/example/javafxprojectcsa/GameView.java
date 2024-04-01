package com.example.javafxprojectcsa;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.QuadCurve;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {
    private double angle;
    private double[] pathX;
    private double[] pathY;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load hoop image
        Image hoopImg = new Image(getClass().getResourceAsStream("/Image/hoopImage.png"));
        hoopImage.setImage(hoopImg);

        // Load basketball image
        Image ballImg = new Image(getClass().getResourceAsStream("/Image/basketball.png"));
        ballImage.setImage(ballImg);
    }

    @FXML
    private void onAngleBtnClick() {
        shootBtn.setVisible(false);
        errorLabel.setText("");

        String input = angleText.getText();
        int number = 0;

        try {
            number = Integer.parseInt(input);
            if (number < 1 || number > 90) {
                throw new NumberFormatException();
            } else {
                errorLabel.setText("Angle entered is: " + number);
                angle = Math.toRadians(number); // Convert angle to radians
                shootBtn.setVisible(true);
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid angle input. Please enter a number between 1 and 90.");
        }

        angleText.clear();
    }

    @FXML
    private void onShootBtnClick() {
        double startX = ballImage.getX();
        double startY = 100;
        //double startY = ballImage.getY();
        double endX = hoopImage.getX() + hoopImage.getImage().getWidth() / 2;
        double endY = hoopImage.getY() + hoopImage.getImage().getHeight() / 2;

        calculateShotPath(startX, startY, endX, endY);
        animateShot(ballImage);
    }

    private void calculateShotPath(double startX, double startY, double endX, double endY) {
        int numPoints = 100;
        pathX = new double[numPoints];
        pathY = new double[numPoints];

        double a = -(endY - startY) / Math.pow((endX - startX) / 2, 2);

        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / (numPoints - 1);
            double x = startX + t * (endX - startX);
            double y = a * Math.pow(x - startX, 2) + startY;
            pathX[i] = x;
            pathY[i] = y;
        }
    }

    private void animateShot(ImageView ballImageView) {
        QuadCurve path = new QuadCurve();
        path.setStartX(pathX[0]);
        path.setStartY(pathY[0]);
        path.setControlX(pathX[pathX.length / 2]);
        path.setControlY(pathY[pathY.length / 2] + 50);
        path.setEndX(pathX[pathX.length - 1]);
        path.setEndY(pathY[pathY.length - 1]);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(ballImageView);
        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(2));

        pathTransition.setOnFinished(event -> {
            if (isShotMade()) {
                successfulShots++;
                errorLabel.setText("Shot made! Total shots made: " + successfulShots);
            } else {
                errorLabel.setText("Shot missed!");
            }
            shootBtn.setVisible(false);
        });

        pathTransition.play();
    }

    private boolean isShotMade() {
        // Determine if the ball's final position intersects with the hoop's position
        return ballImage.getBoundsInParent().intersects(hoopImage.getBoundsInParent());
    }
}
