import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW = "view/fxml/App.fxml";
    private static final String NEW_TASK_TITLE = "New task";
    private static final String ADMIN_USERNAME = "root";
    private static final String ADMIN_PASSWORD = "toor";

    @FXML private Label taskId;
    @FXML private Label sceneTitle;
    @FXML private JFXListView<String> taskList = new JFXListView<>();
    @FXML private AnchorPane addTaskPane;
    @FXML private AnchorPane editTaskPane;
    @FXML private TextField newTaskTitle;
    @FXML private TextArea newTaskDescription;
    @FXML private DatePicker taskToEditDueDate;
    @FXML private TextField taskToEditTitle;
    @FXML private TextArea taskToEditDescription;
    @FXML private TextField taskToEditTag;
    @FXML private TextField taskToEditFlag;
    @FXML private JFXCheckBox isCompleted;

    private User user = null;
    private final AuthorizationManager auth = new AuthorizationManager();
    private HashMap<Integer, Integer> taskIdMap = new HashMap<>();

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

    @FXML public void launchAddTaskPane() {
        this.sceneTitle.setText(NEW_TASK_TITLE);
        this.addTaskPane.setVisible(true);
        this.newTaskTitle.setText("");
        this.newTaskDescription.setText("");
        this.editTaskPane.setVisible(false);
        this.taskList.setVisible(false);
    }

    @FXML public void closeAddTaskPane() {
        this.addTaskPane.setVisible(false);
        this.taskList.setVisible(true);
    }

    @FXML public void addTask() {
        String title = this.newTaskTitle.getText();
        String des = this.newTaskDescription.getText();
        String addedDate = LocalDateTime.now().toString();
        if (!title.equals("") || !des.equals("")) {
            Task task = new Task(this.user.getUsername(), title, des, addedDate);
            this.taskList.getItems().add(task.getTitle());
            this.user.addTask(task);
            closeAddTaskPane();
        }
    }

    @FXML public void showTasks() {
        this.user = new User(ADMIN_USERNAME, ADMIN_PASSWORD);
        loadUserTasks();
    }

    @FXML public void launchEditTaskPane() {
        taskList.setVisible(false);
        String taskTitle = taskList.getSelectionModel().getSelectedItem();
        ArrayList<Task> tasks = user.getTasks();
        int taskId = 0;
        for (Task task : tasks) {
            taskId += 1;
            if (task.getTitle().equals(taskTitle)) {
                this.taskId.setText(Integer.toString(taskId));
                this.taskToEditTitle.setText(task.getTitle());
                this.taskToEditDescription.setText(task.getDescription());
                this.taskToEditTag.setText(task.getTag());
                this.taskToEditFlag.setText(task.getFlag());
                this.isCompleted.setSelected(task.getCompleted());
                break;
            }
        }
        editTaskPane.setVisible(true);
    }

    @FXML public void editTask() {
        int id = taskIdMap.get(Integer.parseInt(this.taskId.getText()));
        String title = this.taskToEditTitle.getText();
        String des = this.taskToEditDescription.getText();
        String dueDate = this.taskToEditDueDate.getChronology().toString();
        String tag = this.taskToEditTag.getText();
        String flag = this.taskToEditFlag.getText();
        boolean completed = this.isCompleted.isSelected();
        Task task = new Task(id, this.user.getUsername(), title, des, dueDate, tag, flag, completed);
        DatabaseManager.editTask(task);
        updateTaskList();
        closeEditTaskPane();
    }

    @FXML public void closeEditTaskPane() {
        editTaskPane.setVisible(false);
        taskList.setVisible(true);
    }

    private void loadUserTasks() {
        updateTaskList();
        this.taskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.taskList.setVisible(true);
    }

    public void updateTaskList() {
        this.user = new User(ADMIN_USERNAME, ADMIN_PASSWORD);
        ArrayList<Task> tasks = this.user.getTasks();
        HashSet<Integer> ids = new HashSet<>();
        this.taskIdMap.clear();
        int taskId = 0;
        this.taskList.getItems().clear();
        for (Task task : tasks) {
            if (!ids.contains(task.getId())) {
                taskId += 1;
                ids.add(task.getId());
                this.taskIdMap.put(taskId, task.getId());
                this.taskList.getItems().add(task.getTitle());
            }
        }
        this.taskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.taskList.setVisible(true);
    }

    private void launchAppWindow() {
        try {
            FXMLLoader appLoader= new FXMLLoader(getClass().getResource(APP_WINDOW));
            Parent appRoot = appLoader.load();
            Scene appScene = new Scene(appRoot);
            Stage appStage = new Stage();
            appStage.setScene(appScene);
            appStage.setTitle(APP_NAME);
            appStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
