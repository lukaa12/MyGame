package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

public class MenuController {
    private Logger logger = Logger.getLogger(MenuController.class);
    private ViewController viewController;
    @FXML
    private Pane menuPane;
    @FXML
    private Button exit;
    @FXML
    private Button newGame;
    @FXML
    private Button loadGame;
    @FXML
    private Button settings;
    static Document doc = null;
    Media sound = null;
    MediaPlayer mediaPlayer = null;

    @FXML
    public void initialize() {
        if(mediaPlayer == null || sound == null) {
            sound = new Media(this.getClass().getResource("/resources/menuSong.mp3").toString());
            mediaPlayer = new MediaPlayer(sound);

        }
        ChoiceBox<String> loadChoice = new ChoiceBox<>(FXCollections.observableArrayList("SAVE1","SAVE2","SAVE3","SAVE4","SAVE5"));
        loadChoice.setVisible(false);
        menuPane.getChildren().add(loadChoice);
        loadChoice.setTranslateY(480);
        loadChoice.setTranslateX(560);
        DOMConfigurator.configure("log4j2.xml");
        DocumentBuilder documentBuilder = null;
        try {
           if(doc == null) {
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               documentBuilder = dbFactory.newDocumentBuilder();
               doc = documentBuilder.parse(this.getClass().getResourceAsStream("/resources/settings.xml"));
           }
            doc.getDocumentElement().normalize();
            logger.info(doc.getDocumentElement().getNodeName()+" read.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        NodeList musicSetting = doc.getElementsByTagName("menuMusic");
        if(musicSetting.item(0).getNodeType() == Node.ELEMENT_NODE) {
            Element musicOptions = (Element) musicSetting.item(0);
            logger.info("Music ON? " + musicOptions.getAttribute("enabled"));
            double volume = Integer.parseInt(musicOptions.getAttribute("volume"))/100.0;
            if(musicOptions.getAttribute("enabled").equals("true")) {
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayer.setVolume(volume);
                    mediaPlayer.play();
            }
        }
        EventHandler<ActionEvent> menuHandler = actionEvent -> {
            if(actionEvent.getSource().equals(exit)) {
                Platform.exit();
            }
            if(actionEvent.getSource().equals(settings)) {
                mediaPlayer.stop();
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/OptionsScreen.fxml"));
                Pane optionsPane = null;
                try {
                    optionsPane = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OptionsController controller = loader.getController();
                controller.setViewController(viewController);
                viewController.mainStackPane.getChildren().clear();
                viewController.mainStackPane.getChildren().add(optionsPane);
            }
            if(actionEvent.getSource().equals(newGame)) {
                mediaPlayer.stop();
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/NewGameScreen.fxml"));
                Pane newGamePane = null;
                try {
                    newGamePane = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GameController controller = loader.getController();
                controller.setViewController(viewController);
                viewController.mainStackPane.getChildren().clear();
                viewController.mainStackPane.getChildren().add(newGamePane);
            }
            if(actionEvent.getSource().equals(loadGame)) {
                loadChoice.setVisible(true);
                if(loadChoice.getValue()==null)
                    return;
                mediaPlayer.stop();
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/NewGameScreen.fxml"));
                Pane newGamePane = null;
                try {
                    newGamePane = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GameController controller = loader.getController();
                controller.saveGameToPick = loadChoice.getValue();
                controller.setViewController(viewController);
                viewController.mainStackPane.getChildren().clear();
                viewController.mainStackPane.getChildren().add(newGamePane);
            }
        };
        exit.addEventHandler(ActionEvent.ACTION,menuHandler);
        settings.addEventHandler(ActionEvent.ACTION,menuHandler);
        newGame.addEventHandler(ActionEvent.ACTION,menuHandler);
        loadGame.addEventHandler(ActionEvent.ACTION,menuHandler);
    }


    void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
