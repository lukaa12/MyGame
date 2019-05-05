package view;

import controllers.ViewController;
import engine.GameEngine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainView.fxml"));
        Pane mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("MyGame");
        Image icon = new Image("/resources/icon.png");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        ViewController viewController = loader.getController();
        viewController.setScene(scene);
        stage.show();
    }
}
