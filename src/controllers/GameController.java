package controllers;

import engine.GameEngine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import models.Doors;
import models.Player;
import models.Steerable;
import models.Vechicle;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;


public class GameController {
    private Logger logger = Logger.getLogger(GameController.class);
    private boolean paused = false;
    private ViewController viewController;
    private Steerable object;
    private GameEngine gameEngine = new GameEngine();
    private Renderer renderer;
    private Timeline timeline;
    private Timeline rendererTimeline;
    private Vechicle passat;
    private Vechicle auto2;
    private Player player;
    public String saveGameToPick;
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
    private ImageView car;
    @FXML
    private ImageView car2;
    @FXML
    ImageView roof;
    @FXML
    ImageView garageRoof;
    @FXML
    public void initialize() {
        DOMConfigurator.configure("log4j2.xml");
        gameEngine.gameController = this;
        player = new Player(playerTransform);
        passat = new Vechicle();
        auto2 = new Vechicle();
        passat.setImage(car);
        renderer = new Renderer(player, this);
        auto2.setImage(car2);
        player.scene = newGamePane;
        passat.scene = newGamePane;
        auto2.scene = newGamePane;
        object = player;
        Doors doors = new Doors(mainDoors);
        gameEngine.addUsable(doors);
        gameEngine.addUsable(passat);
        gameEngine.addUsable(auto2);
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
        logger.info(passat.getBounds());
        colliderContainer.getChildren().add(passat.getBounds());
    }

    void setViewController(ViewController aViewController) {
        viewController = aViewController;
        gameEngine.addObject(object);
        for(Node i: colliderContainer.getChildren()) {
            gameEngine.addCollisions(i);
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> gameEngine.run()));
        rendererTimeline = new Timeline(new KeyFrame(Duration.millis(16), e -> renderer.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        rendererTimeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        rendererTimeline.play();
        this.viewController.getScene().setOnKeyPressed(new EventHandler<>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case ESCAPE:
                        pauseGame();
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
                case F:
                    gameEngine.interaction();
                    if(passat.isUsed)
                        getInCar();
                    break;
            }
        });
        if(saveGameToPick!=null) {
            DocumentBuilder documentBuilder = null;
            Document savegames = viewController.savegames;
            try {
                if(savegames==null) {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    documentBuilder = dbFactory.newDocumentBuilder();
                    savegames = documentBuilder.parse(this.getClass().getResourceAsStream("/resources/savegames.xml"));
                    viewController.savegames = savegames;
                }
                savegames.getDocumentElement().normalize();
                org.w3c.dom.Node sejwy = savegames.getChildNodes().item(0);
                for(int index=0; index<sejwy.getChildNodes().getLength();++index) {
                    org.w3c.dom.Node nodeTmp= sejwy.getChildNodes().item(index);
                    if(nodeTmp.hasAttributes()){
                        logger.info(nodeTmp.getAttributes().getNamedItem("name"));
                        logger.info("name=\""+saveGameToPick+"\"");
                        logger.info(nodeTmp.getAttributes().getNamedItem("name").toString().equals("name=\""+saveGameToPick+"\""));
                        if(nodeTmp.getAttributes().getNamedItem("name").toString().equals("name=\""+saveGameToPick+"\"")) {
//                            Player player = (Player) object;
                            double x,y,r;
                            x = Double.valueOf(nodeTmp.getAttributes().getNamedItem("playerX").getNodeValue());
                            y = Double.valueOf(nodeTmp.getAttributes().getNamedItem("playerY").getNodeValue());
                            r = Double.valueOf(nodeTmp.getAttributes().getNamedItem("playerRotation").getNodeValue());
                            player.loadCoords(x,y,(int) r);
                            x = Double.valueOf(nodeTmp.getAttributes().getNamedItem("vechicleX").getNodeValue());
                            y = Double.valueOf(nodeTmp.getAttributes().getNamedItem("vechicleY").getNodeValue());
                            r = Double.valueOf(nodeTmp.getAttributes().getNamedItem("vechicleRotation").getNodeValue());
                            passat.loadCoords(x,y,(int) r);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void pauseGame() {
        if(paused)
            return;
        paused = true;
        timeline.pause();
        rendererTimeline.pause();
        FXMLLoader pauseLoader = new FXMLLoader(this.getClass().getResource("/fxml/PausePane.fxml"));
        AnchorPane pausePane = null;
        try {
            pausePane = pauseLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pausePane.setTranslateX(object.getX()-300.0);
        pausePane.setTranslateY(object.getY()-250.0);
        ChoiceBox<String> savegamePicker = new ChoiceBox<>(FXCollections.observableArrayList("SAVE1","SAVE2","SAVE3","SAVE4","SAVE5"));
        pausePane.getChildren().add(savegamePicker);
        savegamePicker.setTranslateX(400.0);
        savegamePicker.setTranslateY(200.0);
        newGamePane.getChildren().add(pausePane);
        AnchorPane finalPausePane = pausePane;
        EventHandler<ActionEvent> optionsHandler = actionEvent -> {
            logger.info(actionEvent.getSource());
            if(actionEvent.getSource().toString().equals("Button[id=continueButton, styleClass=button]\'WZNÓW\'")) {
              logger.info("Powrót do gry");
              newGamePane.getChildren().remove(finalPausePane);
              timeline.play();
              rendererTimeline.play();
              paused = false;
            }
            if(actionEvent.getSource().toString().equals("Button[id=saveButton, styleClass=button]\'ZAPISZ\'")) {
                logger.info("SAVE");

               // finalPausePane.getChildren().add(savegamePicker);
                DocumentBuilder documentBuilder = null;
                Document savegames = viewController.savegames;
                try {
                    if(savegames==null) {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        documentBuilder = dbFactory.newDocumentBuilder();
                        savegames = documentBuilder.parse(this.getClass().getResourceAsStream("/resources/savegames.xml"));
                        viewController.savegames = savegames;
                    }
                    savegames.getDocumentElement().normalize();
                    logger.info(savegames.getDocumentElement().getNodeName()+" read.");
                    org.w3c.dom.Node sejwy = savegames.getChildNodes().item(0);
                    logger.info(sejwy.getChildNodes().getLength());
                    logger.info(sejwy.getChildNodes().item(0).toString());
                    org.w3c.dom.Element savegame = savegames.createElement("savegame");
                    logger.info(savegamePicker.getValue());
                    if(savegamePicker.getValue()==null) {
                        return;
                    }
                    savegame.setAttribute("name",savegamePicker.getValue());
                    savegame.setAttribute("playerX",Double.toString(playerTransform.getX()));
                    savegame.setAttribute("playerY",Double.toString(playerTransform.getY()));
                    savegame.setAttribute("playerRotation",Double.toString(playerTransform.getRotate()));
                    savegame.setAttribute("vechicleX", Double.toString(passat.getX()));
                    savegame.setAttribute("vechicleY", Double.toString(passat.getY()));
                    savegame.setAttribute("vechicleRotation", Double.toString(car.getRotate()));
                    boolean nadPisz = false;
                    for(int index=0; index<sejwy.getChildNodes().getLength();++index) {
                        org.w3c.dom.Node nodeTmp= sejwy.getChildNodes().item(index);
                        if(nodeTmp.hasAttributes()){
                            logger.info(nodeTmp.getAttributes().getNamedItem("name"));
                            logger.info("name=\""+savegamePicker.getValue()+"\"");
                            logger.info(nodeTmp.getAttributes().getNamedItem("name").toString().equals("name=\""+savegamePicker.getValue()+"\""));
                            if(nodeTmp.getAttributes().getNamedItem("name").toString().equals("name=\""+savegamePicker.getValue()+"\"")) {
                                nadPisz = true;
                                sejwy.replaceChild(savegame,nodeTmp);
                                break;
                            }
                        }
                    }
                    if(!nadPisz) {
                        sejwy.appendChild(savegame);
                    }
//                     transformer = null;
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    String path = new String(this.getClass().getResource("/resources/savegames.xml").toString());
                    logger.info(path);
                    path = path.replaceFirst("out/production/MyGame/resources/savegames.xml","src/resources/savegames.xml");
                    path = path.replaceFirst("file:/C","C");
                    logger.info(path);
                    Result output = new StreamResult(new File(path));
                    Source input = new DOMSource(savegames);
                    transformer.transform(input, output);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(actionEvent.getSource().toString().equals("Button[id=exitButton, styleClass=button]\'WYJDŹ\'")) {
                        logger.info("WYJDŹ");
                        timeline.stop();
                        rendererTimeline.stop();
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
        };
        Node btn1 = pausePane.getChildren().get(0);
        Button btn = null;
        if(btn1 instanceof Button) {
            btn = (Button) btn1;
            btn.addEventHandler(ActionEvent.ACTION,optionsHandler);
            logger.info("Wznów is button");
        }
        btn1 = pausePane.getChildren().get(1);
        if(btn1 instanceof Button) {
            btn = (Button) btn1;
            btn.addEventHandler(ActionEvent.ACTION,optionsHandler);
            logger.info("Zapisz is button");
        }
        btn1 = pausePane.getChildren().get(2);
        if(btn1 instanceof Button) {
            btn = (Button) btn1;
            btn.addEventHandler(ActionEvent.ACTION,optionsHandler);
            logger.info("Wyjdź is button");
        }
    }
    public void getInCar() {
        if(passat.isUsed) {
            object = passat;
            gameEngine.addObject(passat);
            renderer.object = passat;
            colliderContainer.getChildren().remove(passat.getBounds());
            gameEngine.removeCollisions(passat.getBounds());
            playerTransform.setVisible(false);
        }
        else {
            object = player;
            gameEngine.removeObject(passat);
            gameEngine.removeObject(passat);
            renderer.object = player;
            gameEngine.addCollisions(passat.getBounds());
            player.setX(passat.getX()-70);
            player.setY(passat.getY()+150);
            playerTransform.setVisible(true);
        }
    }
}

class Renderer extends TimerTask {
    Steerable object;
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