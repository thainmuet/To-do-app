import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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

    private User user;

    @FXML
    public void launchSignInWindow() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(LOGIN_WINDOW_PATH)));
            Scene scene = new Scene(root);
            Stage signInStage = new Stage();
            signInStage.initModality(Modality.APPLICATION_MODAL);
            signInStage.setScene(scene);
            signInStage.setTitle(LOGIN_WINDOW_TITLE);
            signInStage.show();
        } catch (Exception e) {
            System.out.print("Can not open sign in window");
        }
    }

    @FXML
    public void launchSignUpWindow() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(AuthorizationManager.class.getResource(SIGNUP_WINDOW_PATH)));
            Scene scene = new Scene(root);
            Stage signUpStage = new Stage();
            signUpStage.initModality(Modality.APPLICATION_MODAL);
            signUpStage.setScene(scene);
            signUpStage.setTitle(LOGIN_WINDOW_TITLE);
            signUpStage.show();
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

    @FXML
    public void signIn() {
        String username = signInUsername.getText();
        String password = signInPassword.getText();
        boolean isAuth = DatabaseManager.authorize(username, password );
        Stage signInStage = (Stage) signIn.getScene().getWindow();
        if (isAuth) {
            user = new User(username, password);
            signInWarning.setVisible(false);
            signInStage.close();
        } else {
            signInWarning.setVisible(true);
        }
        this.user.getTasks();
    }

    public User getUser() {
        return this.user;
    }
}
