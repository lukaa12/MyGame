package controllers;

import engine.GameEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.Player;
import models.Steerable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class GameController {
    private ViewController viewController;
    private Steerable object;
    private GameEngine gameEngine = new GameEngine();
    @FXML
    private ImageView mainDoors;
    @FXML
    private Pane colliderContainer;
    @FXML
    private Pane newGamePane;
    @FXML
    public void initialize() {
        Player player = new Player();
        newGamePane.getChildren().add(player.getPlayerTransform());
        object = player;
//        colliderContainer.getChildren().add(new Rectangle())
    }

    void setViewController(ViewController aViewController) {
        Timer timer = new Timer();
        viewController = aViewController;
        gameEngine.addObject(object);
        for(Node i: colliderContainer.getChildren()) {
            gameEngine.addCollisions(i);
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> gameEngine.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        this.viewController.getScene().setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ESCAPE:
                        timeline.stop();
                        timer.cancel();
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
                        object.setUp(true);
                        break;
                    case A:
                        object.setLeft(true);
                        break;
                    case S:
                        object.setDown(true);
                        break;
                    case D:
                        object.setRight(true);
                        break;
                    case SPACE:
                        object.setJump(true);
                        break;
                    case SHIFT:
                        object.setSprint(true);
                        break;
                }
            }
        });
        this.viewController.getScene().setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    object.setUp(false);
                    break;
                case A:
                    object.setLeft(false);
                case S:
                    object.setDown(false);
                    break;
                case D:
                    object.setRight(false);
                    break;
                case SPACE:
                    object.setJump(false);
                    break;
                case SHIFT:
                    object.setSprint(false);
                    break;
            }
        });
        int timeStep =  16;
        timer.schedule(new Renderer(object),0L,timeStep);
    }
}

class Renderer extends TimerTask {
    private Steerable object;
    Renderer(Steerable st) {
        object = st;
    }
    @Override
    public void run() {
        object.drawMe();
    }
}