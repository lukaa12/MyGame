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
import models.*;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Vector;


public class GameController {
    private Logger logger = Logger.getLogger(GameController.class);
    private boolean paused = false;
    private ViewController viewController;
    private Vector<Steerable> objects = new Vector<>();
    private Steerable player;
    private GameEngine gameEngine = new GameEngine();
    private Renderer renderer;
    private Timeline timeline;
    private Timeline rendererTimeline;
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
        objects.add(player);
        objects.add(new Vechicle(car2));
        objects.add(new Vechicle(car));
        for(Steerable obj: objects) {
            if (obj instanceof Vechicle) {
                logger.info("Adding car: "+obj.toString());
                Vechicle auto = (Vechicle) obj;
//                auto.setImage(car);
                auto.scene = newGamePane;
                gameEngine.addUsable(auto);
                gameEngine.addObject(auto); //maybe
                colliderContainer.getChildren().add(auto.getBounds());
            }
            else if (obj instanceof Player) {
                gameEngine.addObject(player);
                logger.info("Adding person: "+obj.toString());
                Player human = (Player) obj;
                human.scene = newGamePane;
            }
        }
        renderer = new Renderer(player, this);
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
        viewController = aViewController;

        for(Node i: colliderContainer.getChildren()) {
            gameEngine.addCollisions(i);
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> gameEngine.run()));
        if(player == null) {
            logger.info("Error player is null!");
        }
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
                        player.setUp(true);
                        break;
                    case A:
                        player.setLeft(true);
                        break;
                    case S:
                        player.setDown(true);
                        break;
                    case D:
                        player.setRight(true);
                        break;
                    case SPACE:
                        player.setJump(true);
                        break;
                    case SHIFT:
                        player.setSprint(true);
                        break;

                }
            }
        });
        this.viewController.getScene().setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    player.setUp(false);
                    break;
                case A:
                    player.setLeft(false);
                case S:
                    player.setDown(false);
                    break;
                case D:
                    player.setRight(false);
                    break;
                case SPACE:
                    player.setJump(false);
                    break;
                case SHIFT:
                    player.setSprint(false);
                    break;
                case F:
                    gameEngine.interaction();
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
                           int i=1;
                            for(Steerable object: objects) {
                               if(object instanceof Player) {
                                   logger.info("Loading player");
                                   object.loadCoords(Double.valueOf(nodeTmp.getAttributes().getNamedItem("playerX").getNodeValue()),
                                           Double.valueOf(nodeTmp.getAttributes().getNamedItem("playerY").getNodeValue()),
                                           Integer.valueOf(nodeTmp.getAttributes().getNamedItem("playerRotation").getNodeValue()));
                               }
                               else if(object instanceof Vechicle) {
                                   logger.info("Loading car "+i);
                                   double tmp =  Double.valueOf(nodeTmp.getAttributes().getNamedItem("vechicle"+i+"Rotation").getNodeValue());
                                   object.loadCoords(Double.valueOf(nodeTmp.getAttributes().getNamedItem("vechicle"+i+"X").getNodeValue()),
                                           Double.valueOf(nodeTmp.getAttributes().getNamedItem("vechicle"+i+"Y").getNodeValue()), ((int) tmp));
                                    ++i;
                               }
                           }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Steerable getPlayer() { return  player; }

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
        pausePane.setTranslateX(player.getX()-300.0);
        pausePane.setTranslateY(player.getY()-250.0);
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
                    int no = 0;
                    for(Steerable object: objects) {
                        saveObject(savegame,object,no);
                        ++no;
                    }
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
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    String path = this.getClass().getResource("/resources/savegames.xml").toString();
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

    private void saveObject(org.w3c.dom.Element savegame, Steerable object, int no) {
        Vector<String> values = object.stateToSave();
        if(object instanceof Player) {
            savegame.setAttribute("playerRotation",values.get(0));
            savegame.setAttribute("playerX",values.get(1));
            savegame.setAttribute("playerY",values.get(2));
        }
        else if(object instanceof Vechicle) {
            savegame.setAttribute("vechicle"+no+"Rotation",values.get(0));
            savegame.setAttribute("vechicle"+no+"X",values.get(1));
            savegame.setAttribute("vechicle"+no+"Y",values.get(2));
        }
    }

    public void getInCar(Vechicle aCar) {

            player = aCar;
            gameEngine.addObject(aCar);
            renderer.object = aCar;
            colliderContainer.getChildren().remove(aCar.getBounds());
            gameEngine.removeCollisions(aCar.getBounds());
            playerTransform.setVisible(false);
    }

    public void getOutCar() {
        gameEngine.addCollisions(((Vechicle) player).getBounds());
        for(Steerable obj: objects) {
            if(obj instanceof Player) {
                ((Player) obj).setX(player.getX());
                ((Player) obj).setY(player.getY());
                renderer.object = obj;
                player = obj;
                playerTransform.setVisible(true);
            }
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