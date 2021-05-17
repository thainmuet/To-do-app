import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Project> projects;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new HashMap<>();
        tasks = DatabaseManager.getTasks(username);
        this.projects = new HashMap<>();
        projects = DatabaseManager.getProjects(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
        DatabaseManager.addNewTask(task, username);
    }

    public void addProject(Project project) {
        this.projects.put(project.getId(), project);
        DatabaseManager.addNewProject(project, username);
    }

    public void deleteTask(int taskId) {
        this.tasks.remove(taskId);
        DatabaseManager.deleteTask(taskId);
    }

    public HashMap<Integer, Task> getTasks() {
        return DatabaseManager.getTasks(this.username);
    }

    public HashMap<Integer, Project> getProjects() {
        return DatabaseManager.getProjects(this.username);
    }
}
