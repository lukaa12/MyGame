package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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

    @FXML
    public void initialize() {
        DocumentBuilder documentBuilder;
        Document doc = null;
        Node soundMenu = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = dbFactory.newDocumentBuilder();
            doc = documentBuilder.parse(this.getClass().getResourceAsStream("/resources/settings.xml"));
            doc.getDocumentElement().normalize();
            Main.logger.info(doc.getDocumentElement().getNodeName()+" read by options controller.");
            soundMenu = doc.getElementsByTagName("menuMusic").item(0);
            Main.logger.info(soundMenu.getAttributes().getNamedItem("enabled").toString());
            if(soundMenu.getAttributes().getNamedItem("enabled").toString().equals("enabled=\"true\"")) {
                menuMusic.setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("false");
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
        };
        back.addEventHandler(ActionEvent.ACTION,optionsHandler);
        Node finalSoundMenu = soundMenu;
        Document finalDoc = doc;
        ChangeListener<Boolean> checkHandler = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
//                try {
//                    TransformerFactory transFact = TransformerFactory.newInstance();
//                    Transformer transformer = transFact.newTransformer();
//                    DOMSource source = new DOMSource(finalDoc);
//                    StreamResult streamResult = new StreamResult(new File("/resources/settings.xml"));
//                    transformer.transform(source,streamResult);
//                } catch (TransformerException e) {
//                    e.printStackTrace();
//                }
                if(menuMusic.isSelected()) {
                    finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("true");
                }
                else {
                    finalSoundMenu.getAttributes().getNamedItem("enabled").setTextContent("false");
                }

            }
        };
        menuMusic.selectedProperty().addListener(checkHandler);
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
