package com.example.javafxprojectcsa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.QuadCurve;

public class GameView {
    private int score;
    private double angle;

    @FXML
    TextField angleText;
    @FXML
    Label errorLabel;
    @FXML
    Button shootBtn;

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
            number = Integer.valueOf(input);
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
    private void onShootBtnCLick(){
        System.out.println("shoot");
        //need to calculate shot using angle and then move the image
        double endY;
        double endX;
        double midX;
        double midY;
        int startX = 50;
        int startY = 200;
        int v1 = 20;
        endX = 100*(2*Math.pow(v1, 2)*Math.cos(angle)*Math.sin(angle))/9.8;
        endY = 0;
        System.out.println(endX + ", " + endY);,
        double y = startX/(v1*Math.cos(angle));
        midX = (startX + endX)/2;
        midY = startY - (startX*Math.tan(angle) + 0.5*9.8*Math.pow(y, 2));
        Circle circ1 = new

    }

}
