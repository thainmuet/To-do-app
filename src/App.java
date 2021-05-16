import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class App {

    private static final String NEW_TASK_WINDOW = "view/fxml/NewTask.fxml";
    private static final String NEW_TASK_TITLE = "New task";
    private Stage stage;
    private User user = new User();

    @FXML
    private SubScene addTaskScene;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void launchAddTaskScene() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(NEW_TASK_WINDOW)));
            Scene scene = new Scene(root);
            Stage appWindow = new Stage();
            appWindow.setTitle(NEW_TASK_TITLE);
            appWindow.setScene(scene);
            appWindow.show();
        } catch (Exception e) {
            System.out.print("Can not load App window");
        }
    }

    @FXML
    public void addTask() {
        System.out.println("Username: " + this.username.getText());
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
    }

}
