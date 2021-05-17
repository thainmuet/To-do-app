import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTreeView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW = "view/fxml/App.fxml";
    private static final String NEW_TASK_TITLE = "New task";

    @FXML protected JFXListView<String> taskList = new JFXListView<>();
    @FXML protected VBox menu;
    @FXML protected JFXTreeView<String> menuTree = new JFXTreeView<>();
    @FXML private Label taskId;
    @FXML private Label sceneTitle;
    @FXML protected Text dueDateWarning;
    @FXML private AnchorPane addTaskPane;
    @FXML private AnchorPane editTaskPane;
    @FXML private TextField newTaskTitle;
    @FXML private TextArea newTaskDescription;
    @FXML protected  TextField taskToEditAddedDate;
    @FXML private DatePicker taskToEditDueDate;
    @FXML private TextField taskToEditTitle;
    @FXML private TextArea taskToEditDescription;
    @FXML private TextField taskToEditTag;
    @FXML private JFXComboBox<String> taskToEditFlag;
    @FXML private JFXComboBox<String> taskToEditFrequency;
    @FXML private JFXCheckBox isCompleted;

    private static User user = null;
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
        String title = newTaskTitle.getText();
        String des = newTaskDescription.getText();
        if (!title.equals("")) {
            String addedDate = formatter.format(LocalDateTime.now());
            Task task = new Task(App.user.getUsername(), title, des, addedDate);
            this.taskList.getItems().add(task.getTitle());
            user.addTask(task);
            updateTaskList();
        }
    }

    @FXML public void launchEditTaskPane() {
        if (!taskList.getItems().isEmpty()) {
            String taskTitle = taskList.getSelectionModel().getSelectedItem();
            HashMap<Integer, Task> tasks = user.getTasks();

            int taskId = 0;
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                Task task = entry.getValue();
                task.printTaskAttributes();
                taskId += 1;
                if (task.getTitle().equals(taskTitle)) {
                    this.taskId.setText(Integer.toString(taskId));
                    this.taskToEditTitle.setText(task.getTitle());
                    this.taskToEditDescription.setText(task.getDescription() == null ? "" : task.getDescription());
                    this.taskToEditAddedDate.setText(task.getAddedDate());
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
    }

    @FXML public void editTask() {
        int id = taskIdMap.get(Integer.parseInt(this.taskId.getText()));
        String title = this.taskToEditTitle.getText();
        String des = this.taskToEditDescription.getText();
        String addedDate = this.taskToEditAddedDate.getText();
        String dueDate = (this.taskToEditDueDate.getValue()).format(formatter);
        String frequency = this.taskToEditFrequency.getValue();
        String tag = this.taskToEditTag.getText();
        String flag = this.taskToEditFlag.getValue();
        boolean completed = this.isCompleted.isSelected();

        System.out.println(DatabaseManager.getDateDif(id, dueDate));
        if (DatabaseManager.getDateDif(id, dueDate) < 0) {
            this.dueDateWarning.setVisible(true);
        } else {
            Task task = new Task(id, App.user.getUsername(), title, des, addedDate, frequency, dueDate, tag, flag, completed);
            DatabaseManager.editTask(task);
            user.deleteTask(id);
            user.addTask(task);

            this.dueDateWarning.setVisible(false);
            updateTaskList();
        }
    }

    @FXML public void deleteTask() {
        int id = taskIdMap.get(Integer.parseInt(taskId.getText()));
        user.deleteTask(id);
        updateTaskList();
    }

    public void updateTaskList() {
        ArrayList<String> frequencies = DatabaseManager.getFrequencies();
        ArrayList<String> flags = DatabaseManager.getFlags();
        HashMap<Integer, Task> tasks = user.getTasks();
        HashSet<Integer> ids = new HashSet<>();

        int taskId = 0;
        taskIdMap.clear();
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
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            if (!ids.contains(entry.getKey())) {
                taskId += 1;
                ids.add(entry.getValue().getId());
                taskIdMap.put(taskId, entry.getKey());
                this.taskList.getItems().add(entry.getValue().getTitle());
            }
        }

        taskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        taskList.setVisible(true);
        editTaskPane.setVisible(false);
        addTaskPane.setVisible(false);
    }

    public static void launchAppWindow(User user) {
        App.user = user;
        try {
            FXMLLoader appLoader= new FXMLLoader(App.class.getResource(APP_WINDOW));
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
