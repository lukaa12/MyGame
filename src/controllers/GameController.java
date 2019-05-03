package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

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

    }

    void setViewController(ViewController viewController) {
        this.viewController = viewController;
        viewController.getScene().setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
//                  System.out.println("ESCAPE"+keyEvent.getCode());
                    viewController.mainStackPane.getChildren().clear();
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MenuScreen.fxml"));
                    Pane menuPane = null;
                    try {
                        menuPane = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    MenuController menuController = loader.getController();
                    menuController.setViewController(viewController);
                    viewController.mainStackPane.getChildren().add(menuPane);

                }
            }
        });
    }

}
