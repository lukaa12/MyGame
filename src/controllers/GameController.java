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
import javafx.util.Duration;
import models.Doors;
import models.Player;
import models.Steerable;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.awt.*;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class GameController {
    private Logger logger = Logger.getLogger(GameController.class);
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
    private ImageView garageDoors1;
    @FXML
    private ImageView garageDoors2;
    @FXML
    private ImageView garageDoors3;
    @FXML
    private ImageView garageDoors4;
    @FXML
    private ImageView playerTransform;
    @FXML
    ImageView roof;
    @FXML
    ImageView garageRoof;
    @FXML
    public void initialize() {
        DOMConfigurator.configure("log4j2.xml");
        Player player = new Player(playerTransform);
        player.scene = newGamePane;
        object = player;
        Doors doors = new Doors(mainDoors);
        gameEngine.addUsable(doors);
        colliderContainer.getChildren().add(doors.collisionBox);
        logger.info("mainDoors: "+mainDoors.getX()+" "+mainDoors.getY());
        logger.info("RectDoor: "+doors.collisionBox.toString());
        doors = new Doors(garageDoors1,false,1);
        doors.adjustCollision(15.0,30.0);
        gameEngine.addUsable(doors);
        colliderContainer.getChildren().add(doors.collisionBox);
        doors = new Doors(garageDoors2,true,3);
        doors.adjustCollision(30.0,-30.0);
        gameEngine.addUsable(doors);
        colliderContainer.getChildren().add(doors.collisionBox);
        doors = new Doors(garageDoors3,false,1);
        doors.adjustCollision(15.0,30.0);
        gameEngine.addUsable(doors);
        colliderContainer.getChildren().add(doors.collisionBox);
        doors = new Doors(garageDoors4,true,3);
        doors.adjustCollision(30.0,-30.0);
        gameEngine.addUsable(doors);
        colliderContainer.getChildren().add(doors.collisionBox);
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
                    case F:
                        gameEngine.interaction();
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
        timer.schedule(new Renderer(object, this),0L,timeStep);
    }

}

class Renderer extends TimerTask {
    private Steerable object;
    GameController gameController;
    Renderer(Steerable st, GameController gc) {
        gameController =gc;
        object = st;
    }
    @Override
    public void run() {
        object.drawMe();
        if(gameController.roof.contains(object.getX(),object.getY())) {
            gameController.roof.setVisible(false);
        } else {
            gameController.roof.setVisible(true);
        }
        if(gameController.garageRoof.contains(object.getX(),object.getY())) {
            gameController.garageRoof.setVisible(false);
        } else {
            gameController.garageRoof.setVisible(true);
        }
    }
}