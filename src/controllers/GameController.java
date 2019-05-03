package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import models.Player;

import java.io.IOException;



public class GameController {
    private ViewController viewController;
    private ImageView playerTransform;
    @FXML
    private Pane newGamePane;
    @FXML
    public void initialize() {
        Player player = new Player();
        playerTransform = new ImageView(new Image(this.getClass().getResource("/resources/player.png").toString()));
        newGamePane.getChildren().add(playerTransform);
    }

    void setViewController(ViewController viewController) {
        this.viewController = viewController;
        this.viewController.getScene().setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ESCAPE:
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
                        break;
                    case W:

                        break;
                    case A:
                        break;
                    case S:
                        break;
                    case D:
                        break;
                    case SPACE:
                        break;
                    case SHIFT:
                        break;
                }

            }
        });
    }

}
