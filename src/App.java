import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW = "view/fxml/App.fxml";
    private static final String NEW_TASK_TITLE = "New task";
    private static final String ADMIN_USERNAME = "root";
    private static final String ADMIN_PASSWORD = "toor";

    @FXML protected JFXListView<String> taskList = new JFXListView<>();
    @FXML private Label taskId;
    @FXML private Label sceneTitle;
    @FXML private AnchorPane addTaskPane;
    @FXML private AnchorPane editTaskPane;
    @FXML private TextField newTaskTitle;
    @FXML private TextArea newTaskDescription;
    @FXML private DatePicker taskToEditDueDate;
    @FXML private TextField taskToEditTitle;
    @FXML private TextArea taskToEditDescription;
    @FXML private TextField taskToEditTag;
    @FXML private JFXComboBox<String> taskToEditFlag;
    @FXML private JFXComboBox<String> taskToEditFrequency;
    @FXML private JFXCheckBox isCompleted;

    private User user = null;
    private final AuthorizationManager auth = new AuthorizationManager();
    private final HashMap<Integer, Integer> taskIdMap = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    @FXML public void addTask() {
        String title = this.newTaskTitle.getText();
        String des = this.newTaskDescription.getText();
        if (!title.equals("") || !des.equals("")) {
            String addedDate = formatter.format(LocalDateTime.now());
            Task task = new Task(this.user.getUsername(), title, des, addedDate);
            this.taskList.getItems().add(task.getTitle());
            this.user.addTask(task);
            updateTaskList();
        }
    }

    @FXML public void launchEditTaskPane() {
        String taskTitle = taskList.getSelectionModel().getSelectedItem();
        ArrayList<Task> tasks = user.getTasks();

        int taskId = 0;
        for (Task task : tasks) {
            taskId += 1;
            if (task.getTitle().equals(taskTitle)) {
                this.taskId.setText(Integer.toString(taskId));
                this.taskToEditTitle.setText(task.getTitle());
                this.taskToEditDescription.setText(task.getDescription() == null ? "" : task.getDescription());
                String dueDate = task.getDueDate();
                this.taskToEditDueDate.setValue(dueDate != null ? LocalDate.parse(dueDate, formatter) : LocalDate.parse(LocalDateTime.now().format(formatter)));
                this.taskToEditTag.setText(task.getTag() == null ? "" : task.getTag());
                this.taskToEditFrequency.setValue(task.getFrequency());
                this.taskToEditFlag.getSelectionModel().select(task.getFlag());
                this.isCompleted.setSelected(task.getCompleted());
                break;
            }
        }

        taskList.setVisible(false);
        editTaskPane.setVisible(true);
    }

    @FXML public void editTask() {
        int id = taskIdMap.get(Integer.parseInt(this.taskId.getText()));
        String title = this.taskToEditTitle.getText();
        String des = this.taskToEditDescription.getText();
        String dueDate = (this.taskToEditDueDate.getValue()).format(formatter);
        String frequency = this.taskToEditFrequency.getValue();
        String tag = this.taskToEditTag.getText();
        String flag = this.taskToEditFlag.getValue();
        boolean completed = this.isCompleted.isSelected();

        DatabaseManager.checkDateConstraint(id, dueDate);
        Task task = new Task(id, this.user.getUsername(), title, des, frequency, dueDate, tag, flag, completed);
        DatabaseManager.editTask(task);

        updateTaskList();
    }

    @FXML public void deleteTask() {
        int id = taskIdMap.get(Integer.parseInt(this.taskId.getText()));
        DatabaseManager.deleteTask(id);
        updateTaskList();
    }

    public void updateTaskList() {
        this.user = new User(ADMIN_USERNAME, ADMIN_PASSWORD);
        ArrayList<String> frequencies = DatabaseManager.getFrequencies();
        ArrayList<String> flags = DatabaseManager.getFlags();
        ArrayList<Task> tasks = this.user.getTasks();
        HashSet<Integer> ids = new HashSet<>();

        this.taskIdMap.clear();
        int taskId = 0;
        this.taskList.getItems().clear();

        for (String frequency : frequencies) {
            if (!this.taskToEditFrequency.getItems().contains(frequency)) {
                this.taskToEditFrequency.getItems().add(frequency);
            }
        }
        for (String flag : flags) {
            if (!this.taskToEditFlag.getItems().contains(flag)) {
                this.taskToEditFlag.getItems().add(flag);
            }
        }
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
        this.editTaskPane.setVisible(false);
        this.addTaskPane.setVisible(false);
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
