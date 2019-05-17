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
    }

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
