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
    private ImageView playerTransform;
    @FXML
    public void initialize() {
        DOMConfigurator.configure("log4j2.xml");
        Player player = new Player(playerTransform);
        player.scene = newGamePane;
        object = player;
        Doors mainEntrance = new Doors(mainDoors);
        gameEngine.addUsable(mainEntrance);
        colliderContainer.getChildren().add(mainEntrance.collisionBox);
        logger.info("mainDoors: "+mainDoors.getX()+" "+mainDoors.getY());
        logger.info("RectDoor: "+mainEntrance.collisionBox.toString());
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

    void goOutside()
    {

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