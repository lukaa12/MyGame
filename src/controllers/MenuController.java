package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

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
    @FXML
    public void initialize() {
        EventHandler<ActionEvent> menuHandler = actionEvent -> {
            if(actionEvent.getSource().equals(exit)) {
                Platform.exit();
            }
            if(actionEvent.getSource().equals(settings)) {
//                System.out.println("Ustawienia");
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


    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
