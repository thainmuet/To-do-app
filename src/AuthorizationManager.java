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

public class AuthorizationManager {

    private static final String SIGN_UP_WINDOW_PATH = "view/fxml/SignUpWindow.fxml";
    private static final String SIGN_UP_WINDOW_TITLE = "Sign up";
    private static final String SIGN_IN_WINDOW_PATH = "view/fxml/LogInWindow.fxml";
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

    private final User user;
    public boolean isValid;

    public AuthorizationManager() {
        this.user = new User();
        this.isValid = false;
    }

    public AuthorizationManager launchSignInWindow() {
        FXMLLoader signInLoader= new FXMLLoader(getClass().getResource(SIGN_IN_WINDOW_PATH));
        try {
            Parent signInRoot = signInLoader.load();
            Scene scene = new Scene(signInRoot);
            Stage signInStage = new Stage();
            signInStage.setTitle(LOGIN_WINDOW_TITLE);
            signInStage.setScene(scene);
            signInStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signInLoader.getController();
    }

    @FXML
    public void signIn() {
        String username = signInUsername.getText();
        String password = signInPassword.getText();
        Stage signInStage = (Stage) signIn.getScene().getWindow();
        isValid = DatabaseManager.authorize(username, password);
        if (isValid) {
            this.user.setInfo(username, password);
            signInWarning.setVisible(false);
            signInStage.close();
        } else {
            signInWarning.setVisible(true);
        }
    }

    @FXML
    public void signUp() {
        Stage signUpStage = (Stage) signUp.getScene().getWindow();
        String username = signUpUsername.getText();
        String password = signUpPassword.getText();
        isValid = DatabaseManager.createAccount(username, password);
        if (isValid) {
            this.user.setInfo(username, password);
            signUpWarning.setVisible(false);
            signUpStage.close();
        } else {
            signUpWarning.setVisible(true);
        }
    }

    @FXML
    public void launchSignUpWindow() {
        try {
            FXMLLoader signUpLoader= new FXMLLoader(getClass().getResource(SIGN_UP_WINDOW_PATH));
            Parent signUpRoot = signUpLoader.load();
            Scene scene = new Scene(signUpRoot);
            Stage signUpStage = new Stage();
            signUpStage.initModality(Modality.APPLICATION_MODAL);
            signUpStage.setTitle(SIGN_UP_WINDOW_TITLE);
            signUpStage.setScene(scene);
            signUpStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return this.user;
    }
}
