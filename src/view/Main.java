package view;

import controllers.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Main extends Application {
    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        DOMConfigurator.configure("log4j2.xml");
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
        logger.info("Log4j appender configuration is successful !!");
        stage.show();
    }
}
