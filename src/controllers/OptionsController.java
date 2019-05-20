package controllers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class OptionsController {
    Logger logger = Logger.getLogger(OptionsController.class);
    private ViewController viewController;
    @FXML
    private Pane optionsPane;
    @FXML
    private CheckBox menuMusic;
    @FXML
    private Button back;
    @FXML
    private Button saveSettings;
    @FXML
    private Slider volume;

    @FXML
    public void initialize() {
        DOMConfigurator.configure("log4j2.xml");
        Document doc = MenuController.doc;
        Node soundMenu = null;
        Element musicOptions = null;
        int volumeLevel = 100;
        volume.setMax(1.0);
        volume.setMin(0.0);
        volume.setShowTickMarks(true);
        volume.setBlockIncrement(0.1);
        try {
            doc.getDocumentElement().normalize();
            logger.info(doc.getDocumentElement().getNodeName()+" read by options controller.");
            soundMenu = doc.getElementsByTagName("menuMusic").item(0);
            if(soundMenu.getNodeType() == Node.ELEMENT_NODE) {
                musicOptions = (Element) soundMenu;
            }
            logger.info(soundMenu.getAttributes().getNamedItem("enabled").toString());
            if(musicOptions.getAttribute("enabled").equals("true")) {
                menuMusic.setSelected(true);
            }
            logger.info("Volume: "+soundMenu.getAttributes().getNamedItem("volume"));
            volumeLevel = Integer.parseInt(musicOptions.getAttribute("volume"));
            volume.setValue(volumeLevel/100.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document finalDoc = doc;
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
                menuController.doc = doc;
                menuController.setViewController(viewController);
                viewController.mainStackPane.getChildren().add(menuPane);
            }
            if(actionEvent.getSource().equals(saveSettings)) {
                logger.info("SAVE button pressed");
                Transformer transformer = null;
                try {
                    transformer = TransformerFactory.newInstance().newTransformer();
                    Result output = new StreamResult(new File("C:/Users/Public/ProjektyJava/MyGame/src/resources/settings.xml"));
                    Source input = new DOMSource(finalDoc);
                    transformer.transform(input, output);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            }
        };
        back.addEventHandler(ActionEvent.ACTION,optionsHandler);
        saveSettings.addEventHandler(ActionEvent.ACTION,optionsHandler);
        Node finalSoundMenu = soundMenu;
        ChangeListener<Boolean> checkHandler = (observableValue, aBoolean, t1) -> {
            if(menuMusic.isSelected()) {
                finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("true");
                logger.info("sound ON: "+finalSoundMenu.getAttributes().getNamedItem("enabled").toString());
            }
            else {
                finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("false");
                logger.info("sound OFF: "+finalSoundMenu.getAttributes().getNamedItem("enabled").toString());
            }
        };
        menuMusic.selectedProperty().addListener(checkHandler);
        volume.valueProperty().addListener((observableValue, number, t1) -> {
            logger.debug("Volume set to: "+volume.getValue()+100+"%");
            finalSoundMenu.getAttributes().getNamedItem("volume").setTextContent(String.valueOf((int) (volume.getValue()*100)));
        });
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
