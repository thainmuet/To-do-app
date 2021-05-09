import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW_PATH = "view/App.fxml";

    AuthorizationManager auth = new AuthorizationManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) {
        DatabaseManager.loadDriver();
        DatabaseManager.createDatabase();
        launchApp();
        auth.launchSignInWindow();
    }

    private void launchApp() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(APP_WINDOW_PATH)));
            Scene scene = new Scene(root);
            Stage appWindow = new Stage();
            appWindow.setTitle(APP_NAME);
            appWindow.setScene(scene);
            appWindow.show();
        } catch (Exception e) {
            System.out.print("Can not load App window");
        }
    }
}
