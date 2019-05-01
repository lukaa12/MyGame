package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        stage.setScene(scene);
        stage.show();
    }
}
