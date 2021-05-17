import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private HashMap<Integer, Task> tasks;
    private ArrayList<Project> projects;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new HashMap<>();
        tasks = DatabaseManager.getTasks(username);
        this.projects = new ArrayList<>();
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
        DatabaseManager.addNewTask(task, this.username);
    }

    public void deleteTask(int taskId) {
        this.tasks.remove(taskId);
        DatabaseManager.deleteTask(taskId);
    }

    public HashMap<Integer, Task> getTasks() {
        return this.tasks;
    }
}
