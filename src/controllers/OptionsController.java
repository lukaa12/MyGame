package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import view.Main;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

public class OptionsController {
    private ViewController viewController;
    @FXML
    private Pane optionsPane;
    @FXML
    private CheckBox menuMusic;
    @FXML
    private Button back;

//    Media sound = new Media(this.getClass().getResource("/resources/menuSong.mp3").toString());
//    MediaPlayer mediaPlayer = new MediaPlayer(sound);

    @FXML
    public void initialize() {
        DocumentBuilder documentBuilder = null;
        Document doc = null;
        Node soundMenu = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = dbFactory.newDocumentBuilder();
            doc = documentBuilder.parse(this.getClass().getResourceAsStream("/resources/settings.xml"));
            doc.getDocumentElement().normalize();
            Main.logger.info(doc.getDocumentElement().getNodeName()+" read by options controller.");
            soundMenu = doc.getElementsByTagName("menuMusic").item(0);
            Main.logger.info(soundMenu.getNodeName()+" : +soundMenu.getAttributes().item(0).getNodeName()");
            if(soundMenu.getAttributes().getNamedItem("enabled").equals("true")) {
                menuMusic.setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node finalSoundMenu = soundMenu;
        finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("false");
        EventHandler<ActionEvent> optionsHandler = actionEvent -> {
            if(actionEvent.getSource().equals(back)) {

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
            if(actionEvent.getSource().equals(menuMusic)) {
                if(menuMusic.isSelected()) {
                    finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("true");
                }
                else {
                    finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("false");
                }
            }
        };
        back.addEventHandler(ActionEvent.ACTION,optionsHandler);
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
