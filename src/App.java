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
import javafx.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW = "view/fxml/App.fxml";

    @FXML protected JFXListView<String> taskList = new JFXListView<>();
    @FXML protected VBox menu;
    @FXML private Label sceneTitle;
    @FXML protected Text dueDateWarning;
    @FXML private AnchorPane addTaskPane;
    @FXML private AnchorPane editTaskPane;
    @FXML protected AnchorPane addProjectPane;
    @FXML private TextField newTaskTitle;
    @FXML private TextArea newTaskDescription;
    @FXML private TextField newProjectTitle;
    @FXML private TextArea newProjectDescription;
    @FXML protected TextField taskToEditAddedDate;
    @FXML private DatePicker taskToEditDueDate;
    @FXML private TextField taskToEditTitle;
    @FXML private TextArea taskToEditDescription;
    @FXML private TextField taskToEditTag;
    @FXML private JFXComboBox<String> taskToEditFlag;
    @FXML private JFXComboBox<String> taskToEditFrequency;
    @FXML private JFXCheckBox isCompleted;

    private static User user = null;
    private static int taskId = 0;
    private final AuthorizationManager auth = new AuthorizationManager();
    private final HashMap<Integer, Pair<Integer, Task>> taskMap = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage stage) {
        DatabaseManager.connect();
        DatabaseManager.createDatabase();
        auth.launchSignInWindow();
    }

    @FXML public void launchAddTaskPane() {
        sceneTitle.setText("New task");
        newTaskTitle.setText("");
        newTaskDescription.setText("");

        addTaskPane.setVisible(true);
        addProjectPane.setVisible(false);
        editTaskPane.setVisible(false);
        taskList.setVisible(false);
    }

    @FXML public void launchAddProjectPane() {
        sceneTitle.setText("New project");
        newProjectTitle.setText("");
        newProjectDescription.setText("");

        addProjectPane.setVisible(true);
        addTaskPane.setVisible(false);
        editTaskPane.setVisible(false);
        taskList.setVisible(false);
    }

    @FXML public void launchEditTaskPane() {
        if (!taskList.getItems().isEmpty()) {
            int taskIndex = taskList.getSelectionModel().getSelectedIndex();

            int taskId = taskMap.get(taskIndex).getKey();
            HashMap<Integer, Task> tasks = user.getTasks();
            Task task = tasks.get(taskId);

            App.taskId = taskId;
            this.taskToEditTitle.setText(task.getTitle());
            this.taskToEditDescription.setText(task.getDescription() == null ? "" : task.getDescription());
            this.taskToEditAddedDate.setText(task.getAddedDate());
            String dueDate = task.getDueDate();
            this.taskToEditDueDate.setValue(dueDate != null ? LocalDate.parse(dueDate, formatter) : LocalDate.parse(LocalDateTime.now().format(formatter)));
            this.taskToEditTag.setText(task.getTag() == null ? "" : task.getTag());
            this.taskToEditFrequency.setValue(task.getFrequency());
            this.taskToEditFlag.getSelectionModel().select(task.getFlag());
            this.isCompleted.setSelected(task.getCompleted());

            taskList.setVisible(false);
            editTaskPane.setVisible(true);
        }
    }

    @FXML public void addTask() {
        int id = DatabaseManager.getHighestId("task") + 1;
        String title = newTaskTitle.getText();
        String des = newTaskDescription.getText();
        if (!title.equals("")) {
            String addedDate = formatter.format(LocalDateTime.now());
            Task task = new Task(id, user.getUsername(), title, des, addedDate);
            user.addTask(task);
            updateTaskList();
        }
    }

    @FXML public void editTask() {
        int id = App.taskId;
        String title = this.taskToEditTitle.getText();
        String des = this.taskToEditDescription.getText();
        String addedDate = this.taskToEditAddedDate.getText();
        String dueDate = (this.taskToEditDueDate.getValue()).format(formatter);
        String frequency = this.taskToEditFrequency.getValue();
        String tag = this.taskToEditTag.getText();
        String flag = this.taskToEditFlag.getValue();
        boolean completed = this.isCompleted.isSelected();

        if (DatabaseManager.getDateDif(id, dueDate) < 0) {
            this.dueDateWarning.setVisible(true);
        } else {
            this.dueDateWarning.setVisible(false);
            Task task = new Task(id, App.user.getUsername(), title, des, addedDate, frequency, dueDate, tag, flag, completed);
            Task taskToEdit = user.getTasks().get(id);
            taskToEdit.setTitle(title);
            taskToEdit.setDescription(des);
            taskToEdit.setFrequency(frequency);
            taskToEdit.setDueDate(dueDate);
            taskToEdit.setTag(tag);
            taskToEdit.setFlag(flag);
            taskToEdit.setCompleted(completed);

            DatabaseManager.editTask(task);
            updateTaskList();
        }
    }

    @FXML public void deleteTask() {
        int id = App.taskId;
        user.deleteTask(id);
        updateTaskList();
    }

    @FXML public void addProject() {
        int id = DatabaseManager.getHighestId("project") + 1;
        String title = newProjectTitle.getText();
        String des = newProjectDescription.getText();
        if (!title.equals("")) {
            String addedDate = formatter.format(LocalDateTime.now());
            Project project = new Project(id, user.getUsername(), title, des, addedDate);
            user.addProject(project);
            addProjectPane.setVisible(false);
        }
    }

    public void updateTaskList() {
        ArrayList<String> frequencies = DatabaseManager.getFrequencies();
        for (String frequency : frequencies) {
            if (!this.taskToEditFrequency.getItems().contains(frequency)) {
                this.taskToEditFrequency.getItems().add(frequency);
            }
        }

        ArrayList<String> flags = DatabaseManager.getFlags();
        for (String flag : flags) {
            if (!this.taskToEditFlag.getItems().contains(flag)) {
                this.taskToEditFlag.getItems().add(flag);
            }
        }

        HashMap<Integer, Task> tasks = user.getTasks();
        int taskIndex = 0;
        this.taskList.getItems().clear();

        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Pair<Integer, Task> task = new Pair<>(entry.getKey(), entry.getValue());
            taskMap.put(taskIndex, task);
            this.taskList.getItems().add(entry.getValue().getTitle());
            taskIndex += 1;
        }

        taskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        taskList.setVisible(true);
        editTaskPane.setVisible(false);
        addTaskPane.setVisible(false);
    }

    public void launchAppWindow(User user) {
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
