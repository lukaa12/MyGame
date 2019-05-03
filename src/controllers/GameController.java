package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class GameController {
    private ViewController viewController;
    @FXML
    private ImageView playerTransform;
    @FXML
    private Pane newGamePane;
    @FXML
    public void initialize() {
//        playerTransform.setX(450);
//        playerTransform.setRotationAxis(new Point3D((playerTransform.getX()+playerTransform.getFitWidth()),
//                (playerTransform.getY()+playerTransform.getFitHeight()),0.0));
        playerTransform.setRotate(Math.toRadians(90));
        newGamePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
//                if(keyEvent.getCode() == KeyCode.ESCAPE) {
                    System.out.println("ESCAPE"+keyEvent.getCode());
//                }
            }
        });
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }

}
