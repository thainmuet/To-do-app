import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW_PATH = "view/AppWindow.fxml";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {
        DatabaseManager.loadDriver();
        DatabaseManager.createDatabase("To_do_app");
        launchApp();
    }

    private void launchApp() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(APP_WINDOW_PATH));

        AnchorPane rootLayout = new AnchorPane();
        loader.setRoot(rootLayout);
        Scene scene = new Scene(rootLayout);
        Stage appWindow = new Stage();
        appWindow.setScene(scene);
        appWindow.setTitle(APP_NAME);
        appWindow.show();
    }
}
