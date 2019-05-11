package controllers;

import engine.GameEngine;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import models.Player;
import models.Steerable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class GameController {
    private ViewController viewController;
    private Steerable object;
    private GameEngine gameEngine;
    private volatile boolean inRun = true;
    @FXML
    private Pane colliderContainer;
    @FXML
    private Pane newGamePane;
    @FXML
    public void initialize() {
        Player player = new Player();
        newGamePane.getChildren().add(player.getPlayerTransform());
        object = player;
    }

    void setViewController(ViewController viewController) {
        Timer timer = new Timer();
        this.viewController = viewController;
        gameEngine = new GameEngine();
        Thread engineThread = new Thread(gameEngine);
        gameEngine.addObject(object);
        for(Node i: colliderContainer.getChildren()) {
            gameEngine.addCollisions(i);
        }
        engineThread.start();
        this.viewController.getScene().setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ESCAPE:
                        gameEngine.endGame();
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