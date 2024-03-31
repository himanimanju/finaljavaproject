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
import javafx.scene.shape.QuadCurve;

import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable{
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
    Label shotsMadeLabel; // need to add label in scene builder to display successful shots :((((
  //  @FXML
  //  private BorderPane borderPane;

    ///trying
    private double[] pathX;
    private double[] pathY;
    //private int pathIndex = 0;

    @FXML
    public void initialize(URL location, ResourceBundle resources)
    {
        File file = new File("src/main/resources/Image/hoopImage.png");
        Image image = new Image(file.toURI().toString());
        hoopImage.setImage(image);

        File file1 = new File("src/main/resources/Image/basketball.png");
        Image image1 = new Image(file1.toURI().toString());
        ballImage.setImage(image1);

/*TRYING TO FIGURE OUT HOW TO ADD THE BBALL IMAGE IDK HOW
        Image image = new Image(new File("src/main/resources/Image/basketball.png").toURI().toString());
        ballImage.setImage(image);

        // Create an ImageView with the loaded image
        ImageView imageView = new ImageView(image);

        // Set additional properties of the ImageView (position, size, etc.)
        imageView.setX(100);
        imageView.setY(100);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        // Add the ImageView to the rootPane
        borderPane.getChildren().add(imageView);
        */
    }
    //does the action for the angle button on the screen
    @FXML
    private void onAngleBtnClick() {
        System.out.println("Enter");
        shootBtn.setVisible(false);
        errorLabel.setText("");
        //need to hide the shoot button
        //need to get text/number from textbox
        String input = angleText.getText();
        System.out.println(input);
        int number = 0;
        //check if it is a valid angle number from 0 to 90
        //convert text to number
        //if it is a valid angle, show shoot button and allow the person to shoot
        try {
            number = Integer.parseInt(input);
            System.out.println("Converted integer: " + number);
            if(number < 0 || number > 90)
            {
                System.out.println("Invalid angle input. Please enter a number from 0 to 90");
                errorLabel.setText("Invalid angle input. Please enter a number from 0 to 90");
            }
            else
            {
                errorLabel.setText("Angle entered is: " + number);
                //want to show button only if angle is correct
                shootBtn.setVisible(true);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid integer input. Please enter a number");
            errorLabel.setText("Invalid integer input. Please enter a number");
        }

        angle = number*Math.PI/180;
        angleText.clear();
    }
    @FXML
    private void onShootBtnClick(){
        System.out.println("shoot");
        //need to calculate shot using angle and then move the image
        double endY;
        double endX;
        double midX;
        double midY;
        double startX = ballImage.getX();
        double startY = ballImage.getY();
        int v1 = 20;
        endX = 100*(2*Math.pow(v1, 2)*Math.cos(angle)*Math.sin(angle))/9.8;
        endY = 0;
        System.out.println(endX + ", " + endY);
        //double y = startX/(v1*Math.cos(angle));
        //midX = (startX + endX)/2;
        //midY = startY - (startX*Math.tan(angle) + 0.5*9.8*Math.pow(y, 2));

        //trying
        // Calculate shot path
        calculateShotPath(startX, startY, endX, endY);
        traceShotPath(ballImage);
/*ANOTHER TRY TO ADD THE BBALL IMAGE - CONFUSED
        // Create basketball image
        Image ballImage = new Image("basketball.png");
        ImageView ballImageView = new ImageView(ballImage);
        ballImageView.setFitWidth(20);
        ballImageView.setFitHeight(20);
        ballImageView.setX(startX);
        ballImageView.setY(startY);

        // Add basketball image to the scene
        // Replace rootPane with the name of your layout container
        borderPane.getChildren().add(ballImageView);

        // Trace the shot path with the basketball image
        traceShotPath(ballImageView);
*/
        /////////////////////himani added to see if we could identify if shot is made and record it
        if (isShotMade(endX, endY)) {
            System.out.println("Shot made!");
            successfulShots++;
            updateShotsMadeLabel();
        } else {
            System.out.println("Shot missed!");
        }
        //note only for testing if the label works
        successfulShots++;
        shotsMadeLabel.setText("Shots Made: " + successfulShots);
        shootBtn.setVisible(false);
    }

    //trying
    private void calculateShotPath(double startX, double startY, double endX, double endY) {
        // Calculate the number of points along the shot path
        //should actually base it on the time it takes the ball to fall
        //we can calculate that using the formula to find how much time it would actually take for the
        //ball to fall
        //then use maybe 100 points divided from that time in order to calculate for x and y
        //cuz then it will be more accurate
        //not sure if we wnat to go through the trouble or not though
        int numPoints = 100;
        pathX = new double[numPoints];
        pathY = new double[numPoints];

        // Calculate coordinates of each point along the shot path
        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / (numPoints - 1);
            //x should increase a constant amount every time
            double x = (1 - t) * startX + t * endX;
            //y should increase, then decrease because it is a parabola
            //so maybe do an if statement--if i <= numpoints/2, then y increase
            //but if i > numpoints/2, then y decrease
            //idk how the pathX and pathY functions work so im not gonna do anything about that
            double y = (1 - t) * startY + t * endY;
            pathX[i] = x;
            pathY[i] = y;
        }
    }

    private void traceShotPath(ImageView ballImageView) {
        // Create a PathTransition to animate the basketball along the shot path
        PathTransition pathTransition = new PathTransition();
        pathTransition.setNode(ballImageView);

        // Create a Line object representing the shot path
        Line path = new Line();
        path.setStartX(pathX[0]);
        path.setStartY(pathY[0]);
        path.setEndX(pathX[1]);
        path.setEndY(pathY[1]);

        // Set the duration of the path transition
        pathTransition.setDuration(Duration.seconds(2)); // Adjust duration as needed

        // Set the path of the path transition
        pathTransition.setPath(path);

        // Play the path transition animation
        pathTransition.play();
    }

    /////////////////used chatgpt for this to get started - one way to identify if shot is made or not
    private boolean isShotMade(double endX, double endY) {
        // Logic to determine if the shot is made based on end coordinates
        // Example: Check if the end coordinates are within the hoop area
        // You need to implement this method according to your game's logic
        // For demonstration purposes, let's assume if endX is within a certain range, the shot is made
        //return endX >= 200 && endX <= 300;

        // Hypothetical game logic: Consider the shot made if it falls within a certain distance from the center of the hoop

        // Coordinates of the center of the hoop (hypothetical values for demonstration)
        //BRO IDK HOW TO FIND THE CENTER OF THE HOOP AHSHIAHISHIAHIHSIJAINBIAHISHI
        //double hoopCenterX = 50;
        //double hoopCenterY = 200;
        //double hoopCenterX = 250;
        //double hoopCenterY = 150;
        double hoopCenterX = 168;
        double hoopCenterY = 318;

        // Calculate the distance between the end coordinates of the shot and the center of the hoop
        double distance = Math.sqrt(Math.pow(endX - hoopCenterX, 2) + Math.pow(endY - hoopCenterY, 2));

        // Define a threshold distance within which the shot is considered made (hypothetical value for demonstration)
        double thresholdDistance = 60; // Adjust this value based on your game's requirements

        // If the calculated distance is less than or equal to the threshold distance, consider the shot made
        return distance <= thresholdDistance;
    }
    //don't really need this
    /*private void updateShotsMadeLabel() {
        // Update the label to display the number of successful shots
        shotsMadeLabel.setText("Shots Made: " + successfulShots);
    }
*/
}

//Himani crying over:
//TRYING TO FIGURE OUT HOW TO ADD THE BBALL IMAGE IDK HOW
//NEED TO FIGURE OUT HOW TO HAVE PROGRAM IDENTIFY IF SHOT IS MADE OR MISSED AND KEEP COUNT OF IT
//RN ITS ONLY SHOTS MISSED KMS
//BRO IDK HOW TO FIND THE CENTER OF THE HOOP AHSHIAHISHIAHIHSIJAINBIAHISHI
//NEED TO
//need to still scale this up to fit any screen size but at this point that is a last priority because scene builder
//will be the end of me
