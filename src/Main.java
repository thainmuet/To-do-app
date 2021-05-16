import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    private static final String SIGN_IN_WINDOW_PATH = "view/fxml/LogInWindow.fxml";
    private static final String LOGIN_WINDOW_TITLE = "Log in";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DatabaseManager.connect();
        DatabaseManager.createDatabase();
        launchSignInWindow(stage);
    }

    private void launchSignInWindow(Stage stage) {
        try {
            FXMLLoader signInLoader= new FXMLLoader(getClass().getResource(SIGN_IN_WINDOW_PATH));
            Parent signInRoot = signInLoader.load();
            AuthorizationManager controller= signInLoader.getController();
            controller.setStage(stage);
            Scene scene=new Scene(signInRoot);

            stage.setTitle(LOGIN_WINDOW_TITLE);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> DatabaseManager.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
