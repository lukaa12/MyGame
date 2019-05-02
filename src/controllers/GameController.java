package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GameController {
    private ViewController viewController;
    @FXML
    private ImageView playerTransform;

    public void setViewController(ViewController viewController) {
        this.viewController = viewController;
    }
}
