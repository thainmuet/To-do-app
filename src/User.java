import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<Task> tasks;
    private ArrayList<Project> projects;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks = new ArrayList<>();
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
        this.tasks.add(task);
        DatabaseManager.addNewTask(task, this.username);
    }

    public void deleteTask(Task task) {
        this.tasks.remove(task);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}
