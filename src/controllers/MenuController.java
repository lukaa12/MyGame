package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import view.Main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class MenuController {
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

    Media sound = new Media(this.getClass().getResource("/resources/menuSong.mp3").toString());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);

    @FXML
    public void initialize() {
        DocumentBuilder documentBuilder = null;
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = dbFactory.newDocumentBuilder();
            doc = documentBuilder.parse(this.getClass().getResourceAsStream("/resources/settings.xml"));
            doc.getDocumentElement().normalize();
            Main.logger.info(doc.getDocumentElement().getNodeName()+" read.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        NodeList musicSetting = doc.getElementsByTagName("menuMusic");
        if(musicSetting.item(0).getNodeType() == Node.ELEMENT_NODE) {
            Element musicOptions = (Element) musicSetting.item(0);
            Main.logger.info("Music ON?" + musicOptions.getAttribute("enabled"));
            if(musicOptions.getAttribute("enabled").equals("true")) {
//                Timeline playMusic = new Timeline(new KeyFrame(Duration.seconds(108),e ->{
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayer.play();
//                }))
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
        };
        exit.addEventHandler(ActionEvent.ACTION,menuHandler);
        settings.addEventHandler(ActionEvent.ACTION,menuHandler);
        newGame.addEventHandler(ActionEvent.ACTION,menuHandler);
    }


    void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
