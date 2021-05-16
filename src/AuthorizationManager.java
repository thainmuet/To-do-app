import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AuthorizationManager {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW_PATH = "view/fxml/App.fxml";
    private static final String SIGN_UP_WINDOW_PATH = "view/fxml/SignUpWindow.fxml";
    private static final String SIGN_UP_WINDOW_TITLE = "Sign up";

    @FXML
    private Text signUpWarning;
    @FXML
    private Text signInWarning;
    @FXML
    private TextField signInUsername;
    @FXML
    private PasswordField signInPassword;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpPassword;
    @FXML
    public Button signIn;
    @FXML
    private Button signUp;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void launchAppWindow(User user) {
        try {
            FXMLLoader appLoader= new FXMLLoader(getClass().getResource(APP_WINDOW_PATH));
            Parent appRoot = appLoader.load();
            App controller = appLoader.getController();
            controller.setStage(stage);
            controller.setUser(user);
            Scene scene=new Scene(appRoot);

            stage.setTitle(APP_NAME);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signIn() {
        String username = signInUsername.getText();
        String password = signInPassword.getText();
        boolean isAuth = DatabaseManager.authorize(username, password);
        if (isAuth) {
            signInWarning.setVisible(false);
            launchAppWindow(new User(username, password));
        } else {
            signInWarning.setVisible(true);
        }
    }

    @FXML
    public void signUp() {
        Stage signUnStage = (Stage) signUp.getScene().getWindow();
        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        if (!DatabaseManager.createAccount(username, password)) {
            signUpWarning.setVisible(true);
        } else {
            signUpWarning.setVisible(false);
            signUnStage.close();
            launchAppWindow(new User(username, password));
        }
    }

    @FXML
    public void launchSignUpWindow() {
        try {
            FXMLLoader signUpLoader= new FXMLLoader(getClass().getResource(SIGN_UP_WINDOW_PATH));
            Parent signUpRoot = signUpLoader.load();
            Scene scene=new Scene(signUpRoot);

            stage.setTitle(SIGN_UP_WINDOW_TITLE);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
