import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class AuthorizationManager {

    private static final String LOGIN_WINDOW_PATH = "view/LogInWindow.fxml";
    private static final String SIGNUP_WINDOW_PATH = "view/SignUpWindow.fxml";
    private static final String LOGIN_WINDOW_TITLE = "Log in";

    @FXML
    private Text signUpWarning;
    @FXML
    private TextField signInUsername;
    @FXML
    private TextField signInPassword;
    @FXML
    private TextField signUpUsername;
    @FXML
    private TextField signUpPassword;
    @FXML
    private Button openSignUp;
    @FXML
    private Button signIn;
    @FXML
    private Button signUp;

    @FXML
    public void launchSignInWindow() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(LOGIN_WINDOW_PATH)));
            Scene scene = new Scene(root);
            Stage signInWindow = new Stage();
            signInWindow.initModality(Modality.APPLICATION_MODAL);
            signInWindow.setScene(scene);
            signInWindow.setTitle(LOGIN_WINDOW_TITLE);
            signInWindow.show();
        } catch (Exception e) {
            System.out.print("Can not open log in window");
        }
    }

    @FXML
    public void launchSignUpWindow() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(SIGNUP_WINDOW_PATH)));
            Scene scene = new Scene(root);
            Stage signupWindow = new Stage();
            signupWindow.initModality(Modality.APPLICATION_MODAL);
            signupWindow.setScene(scene);
            signupWindow.setTitle(LOGIN_WINDOW_TITLE);
            signupWindow.show();
        } catch (Exception e) {
            System.out.print("Can not open sign up window");
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
        }
    }
}
