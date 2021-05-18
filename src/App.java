import com.jfoenix.controls.JFXButton;
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

    @FXML protected TextField taskToEditProjectTitle;
    @FXML protected JFXListView<String> taskList = new JFXListView<>();
    @FXML protected JFXListView<String> projectList = new JFXListView<>();
    @FXML protected JFXListView<String> projectTaskList = new JFXListView<>();
    @FXML protected VBox menu;
    @FXML private Label sceneTitle;
    @FXML protected Text dueDateWarning;
    @FXML protected Text dueDateProjectWarning;
    @FXML private AnchorPane addTaskPane;
    @FXML private AnchorPane editTaskPane;
    @FXML private AnchorPane editProjectPane;
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
    @FXML protected TextField projectToEditAddedDate;
    @FXML private DatePicker projectToEditDueDate;
    @FXML private TextField projectToEditTitle;
    @FXML private TextArea projectToEditDescription;
    @FXML private TextField projectToEditTag;
    @FXML private JFXComboBox<String> projectToEditFlag;
    @FXML private JFXComboBox<String> taskToEditFlag;
    @FXML private JFXComboBox<String> taskToEditFrequency;
    @FXML private JFXComboBox<Integer> taskToEditProjectID;
    @FXML private JFXCheckBox isCompletedTask;
    @FXML private JFXCheckBox isCompletedProject;
    @FXML private JFXButton editProjectButton;

    private static User user = null;
    private static int taskId = 0;
    private static int projectId = 0;
    private final AuthorizationManager auth = new AuthorizationManager();
    private final HashMap<Integer, Pair<Integer, Task>> taskMap = new HashMap<>();
    private final HashMap<Integer, Pair<Integer, Project>> projectMap = new HashMap<>();
    private final HashMap<Integer, Pair<Integer, Task>> projectTaskMap = new HashMap<>();
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

        setPaneVisible(false,
                false,
                false,
                true,
                false,
                false,
                false,
                false);
    }

    @FXML public void launchAddProjectPane() {
        sceneTitle.setText("New project");
        newProjectTitle.setText("");
        newProjectDescription.setText("");

        setPaneVisible(false,
                false,
                false,
                false,
                true,
                false,
                false,
                false);
    }

    @FXML public void launchEditTaskPane() {
        JFXListView<String> list = new JFXListView<>();
        boolean check = false;
        int taskIndex;
        int taskId = -1;
        if (taskList.isVisible() && !taskList.getItems().isEmpty()) {
            list.getItems().addAll(taskList.getItems());
            taskIndex = taskList.getSelectionModel().getSelectedIndex();
            taskId = taskMap.get(taskIndex).getKey();
            check = true;
        } else if (projectTaskList.isVisible() && !projectTaskList.getItems().isEmpty()) {
            list.getItems().addAll(projectTaskList.getItems());
            taskIndex = projectTaskList.getSelectionModel().getSelectedIndex();
            taskId = projectTaskMap.get(taskIndex).getKey();
            check = true;
        }
        if (check) {
            App.taskId = taskId;

            HashMap<Integer, Task> tasks = user.getTasks();
            Task task = tasks.get(taskId);

            this.taskToEditTitle.setText(task.getTitle());
            this.taskToEditDescription.setText(task.getDescription() == null ? "" : task.getDescription());
            this.taskToEditAddedDate.setText(task.getAddedDate());
            String dueDate = task.getDueDate();
            this.taskToEditDueDate.setValue(dueDate != null ? LocalDate.parse(dueDate, formatter) : LocalDate.parse(LocalDateTime.now().format(formatter)));
            this.taskToEditTag.setText(task.getTag() == null ? "" : task.getTag());
            this.taskToEditFrequency.setValue(task.getFrequency());
            this.taskToEditFlag.setValue(task.getFlag());
            this.taskToEditProjectID.setValue(task.getProject_id());
            this.taskToEditProjectTitle.setText(DatabaseManager.getProjectTitle(taskToEditProjectID.getValue()));
            this.taskToEditProjectID.setOnAction(e -> this.taskToEditProjectTitle.setText(DatabaseManager.getProjectTitle(taskToEditProjectID.getValue())));
            this.isCompletedTask.setSelected(task.getCompleted());

            setPaneVisible(false,
                    false,
                    false,
                    false,
                    false,
                    true,
                    false,
                    false);
        } else {
            System.out.println(4);
        }
    }

    @FXML public void launchEditProjectPane() {
        JFXListView<String> list = new JFXListView<>();
        list.getItems().addAll(projectTaskList.getItems());

        if (!projectList.getItems().isEmpty()) {
            int projectIndex = projectList.getSelectionModel().getSelectedIndex();
            int projectId = projectMap.get(projectIndex).getKey();
            App.projectId = projectId;

            HashMap<Integer, Project> projects = user.getProjects();
            Project project = projects.get(projectId);

            this.projectToEditTitle.setText(project.getTitle());
            this.projectToEditDescription.setText(project.getDescription() == null ? "" : project.getDescription());
            this.projectToEditAddedDate.setText(project.getAddedDate());
            String dueDate = project.getDueDate();
            this.projectToEditDueDate.setValue(dueDate != null ? LocalDate.parse(dueDate, formatter) : LocalDate.parse(LocalDateTime.now().format(formatter)));
            this.projectToEditTag.setText(project.getTag() == null ? "" : project.getTag());
            this.projectToEditFlag.setValue(project.getFlag());
            this.isCompletedProject.setSelected(project.getCompleted());

            setPaneVisible(false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    true,
                    true);
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
        int projectId = this.taskToEditProjectID.getValue();
        String title = this.taskToEditTitle.getText();
        String des = this.taskToEditDescription.getText();
        String addedDate = this.taskToEditAddedDate.getText();
        String dueDate = (this.taskToEditDueDate.getValue()).format(formatter);
        String frequency = this.taskToEditFrequency.getValue();
        String tag = this.taskToEditTag.getText();
        String flag = this.taskToEditFlag.getValue();

        boolean completed = this.isCompletedTask.isSelected();

        if (DatabaseManager.getDateDif(id, dueDate, "task") < 0) {
            this.dueDateWarning.setVisible(true);
        } else {
            this.dueDateWarning.setVisible(false);
            Task task = new Task(id, projectId, App.user.getUsername(), title, des, addedDate, frequency, dueDate, tag, flag, completed);
            Task taskToEdit = user.getTasks().get(id);
            taskToEdit.setProject_id(projectId);
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

    @FXML public void editProject() {
        int id = App.projectId;
        String title = this.projectToEditTitle.getText();
        String des = this.projectToEditDescription.getText();
        String addedDate = this.projectToEditAddedDate.getText();
        String dueDate = (this.projectToEditDueDate.getValue()).format(formatter);
        String tag = this.projectToEditTag.getText();
        String flag = this.projectToEditFlag.getValue();
        boolean completed = this.isCompletedProject.isSelected();

        if (DatabaseManager.getDateDif(id, dueDate, "project") < 0) {
            this.dueDateProjectWarning.setVisible(true);
        } else {
            this.dueDateProjectWarning.setVisible(false);

            Project project = new Project(id, App.user.getUsername(), title, des, addedDate, dueDate, tag, flag, completed);
            Project projectToEdit = user.getProjects().get(id);
            projectToEdit.setId(projectId);
            projectToEdit.setTitle(title);
            projectToEdit.setDescription(des);
            projectToEdit.setDueDate(dueDate);
            projectToEdit.setTag(tag);
            projectToEdit.setFlag(flag);
            projectToEdit.setCompleted(completed);

            DatabaseManager.editProject(project);
            updateProjectList();
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
            updateProjectList();
        }
    }

    @FXML public void closeAddProjectPane() {
        addProjectPane.setVisible(false);
    }

    @FXML public void updateProjectList() {
        HashMap<Integer, Project> projects = user.getProjects();
        int projectIndex = 0;
        this.projectList.getItems().clear();

        for (Map.Entry<Integer, Project> entry : projects.entrySet()) {
            Pair<Integer, Project> project = new Pair<>(entry.getKey(), entry.getValue());
            projectMap.put(projectIndex, project);
            this.projectList.getItems().add(entry.getValue().getTitle());
            projectIndex += 1;
        }

        projectList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setPaneVisible(false,
                true,
                false,
                false,
                false,
                false,
                false,
                false);
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

        HashMap<Integer, Project> projects = DatabaseManager.getProjects(user.getUsername());
        for (Map.Entry<Integer, Project> entry : projects.entrySet()) {
            if (!this.taskToEditProjectID.getItems().contains(entry.getKey())) {
                this.taskToEditProjectID.getItems().add(entry.getKey());
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
        setPaneVisible(true,
                false,
                false,
                false,
                false ,
                false,
                false,
                false);
    }

    @FXML  public void updateProjectTaskList() {
        ArrayList<String> flags = DatabaseManager.getFlags();
        for (String flag : flags) {
            if (!this.projectToEditFlag.getItems().contains(flag)) {
                this.projectToEditFlag.getItems().add(flag);
            }
        }
        if (!projectList.getItems().isEmpty()) {
            this.projectTaskList.getItems().clear();

            int projectIndex = projectList.getSelectionModel().getSelectedIndex();
            int projectId = projectMap.get(projectIndex).getKey();
            HashMap<Integer, Task> tasks = DatabaseManager.getProjectTasks(App.user.getUsername(), projectId);
            int taskIndex = 0;

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                Pair<Integer, Task> task = new Pair<>(entry.getKey(), entry.getValue());
                projectTaskMap.put(taskIndex, task);
                this.projectTaskList.getItems().add(entry.getValue().getTitle());
                taskIndex += 1;
            }

            projectTaskList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            this.editProjectButton.setVisible(true);
            setPaneVisible(false,
                    false,
                    true,
                    false,
                    false,
                    false,
                    false,
                    true);
        }
    }

    public void setPaneVisible(boolean taskListVisible,
                               boolean projectListVisible,
                               boolean projectTaskListVisible,
                               boolean addTaskPaneVisible,
                               boolean addProjectPaneVisible,
                               boolean editTaskPaneVisible,
                               boolean editProjectPaneVisible,
                               boolean editProjectButtonVisible) {
        taskList.setVisible(taskListVisible);
        projectList.setVisible(projectListVisible);
        projectTaskList.setVisible(projectTaskListVisible);

        addProjectPane.setVisible(addProjectPaneVisible);
        addTaskPane.setVisible(addTaskPaneVisible);
        editTaskPane.setVisible(editTaskPaneVisible);
        editProjectPane.setVisible(editProjectPaneVisible);

        editProjectButton.setVisible(editProjectButtonVisible);
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
