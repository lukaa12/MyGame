package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MenuController {
    @FXML
    private Pane menuPane;
    @FXML
    private Button exit;
    @FXML
    public void initialize() {
        EventHandler<ActionEvent> menuHandler = actionEvent -> {
            if(actionEvent.getSource().equals(exit)) {
                Platform.exit();
            }
        };
        exit.addEventHandler(ActionEvent.ACTION,menuHandler);
    }


}
