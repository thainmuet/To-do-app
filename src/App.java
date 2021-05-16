import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW = "view/fxml/App.fxml";
    private static final String NEW_TASK_TITLE = "New task";
    private static final String ADMIN_USERNAME = "root";
    private static final String ADMIN_PASSWORD = "toor";

    @FXML
    private Label sceneTitle;
    @FXML
    private JFXListView<String> taskList = new JFXListView<>();
    @FXML
    private AnchorPane addTaskPane;
    @FXML
    private AnchorPane editTaskPane;
    @FXML
    private TextField newTaskTitle;
    @FXML
    private TextArea newTaskDescription;
    @FXML
    private DatePicker taskDueDate;
    @FXML
    private TextField taskFlag;
    @FXML
    private TextField taskTag;

    private ObservableList<String> tasks = FXCollections.observableArrayList();

    private Stage appStage;
    private final AuthorizationManager auth = new AuthorizationManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        DatabaseManager.connect();
        DatabaseManager.createDatabase();
        auth.launchSignInWindow();
        launchAppWindow();
    }

    private void launchAppWindow() {
        try {
            loadUserTasks(new User(ADMIN_USERNAME, ADMIN_PASSWORD));
            FXMLLoader appLoader= new FXMLLoader(getClass().getResource(APP_WINDOW));
            Parent appRoot = appLoader.load();
            Scene appScene = new Scene(appRoot);
            appStage = new Stage();
            appStage.setScene(appScene);
            appStage.setTitle(APP_NAME);
            appStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void launchAddTaskPane() {
        this.sceneTitle.setText(NEW_TASK_TITLE);
        this.addTaskPane.setVisible(true);
        this.taskList.setVisible(false);
    }

    @FXML
    public void closeAddTaskPane() {
        this.addTaskPane.setVisible(false);
        this.taskList.setVisible(true);
    }

    @FXML
    public void addTask() {
        String title = this.newTaskTitle.getText();
        String des = this.newTaskDescription.getText();
        String addedDate = LocalDateTime.now().toString();
        if (!title.equals("") || !des.equals("")) {
            Task task = new Task(ADMIN_USERNAME, title, des, addedDate);
            closeAddTaskPane();
            this.taskList.getItems().add(task.getTitle());
        }
    }

    private void loadUserTasks(User user) {
        ArrayList<Task> tasks = user.getTasks();
        System.out.println(user.getUsername());
        for (Task task : tasks) {
            System.out.println(task.getTitle());
            this.taskList.getItems().add(task.getTitle());
        }
        this.taskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
}
