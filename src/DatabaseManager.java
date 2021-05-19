import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager {

    private static final String DB_NAME = "to_do";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/to_do";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Thai02032001";

    private static Connection connection = null;
    private static Statement statement = null;
    private static ResultSet resultSet = null;

    private static void printExceptionLog(SQLException e) {
        e.printStackTrace();
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }

    public static void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("Connect successfully!");
        } catch (SQLException ex) {
            printExceptionLog(ex);
        }
    }

    public static void createDatabase() {

        try {
            statement = connection.createStatement();
            String query = String.format("CREATE DATABASE IF NOT EXISTS %s;", DB_NAME);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }
    }

    public static void addNewTask(Task task, String username) {
        try {
            statement = connection.createStatement();
            String title = task.getTitle();
            String des = task.getDescription();
            String addedDate = task.getAddedDate();
            String format = "INSERT INTO task (username, title, description, added_date) VALUES ('%s', '%s', '%s', '%s')";
            String query = String.format(format, username, title, des, addedDate);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }

    }

    public static void editTask(Task task) {
        int id = task.getId();
        int projectId = task.getProject_id();
        String title = task.getTitle();
        String des = task.getDescription();
        String addedDate = task.getAddedDate();
        String frequency = task.getFrequency();
        String dueDate = task.getDueDate();
        String tag = task.getTag();
        String flag = task.getFlag();
        boolean completed = task.getCompleted();

        String format = "UPDATE task SET project_id=%d, title='%s', description='%s', added_date='%s', due_date='%s', frequency='%s', tag='%s', flag='%s', completed=%b WHERE task_id=%d";
        String query = String.format(format, projectId, title, des, addedDate, dueDate, frequency, tag, flag, completed, id);
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }
    }

    public static void editProject(Project project) {
        int id = project.getId();
        String title = project.getTitle();
        String des = project.getDescription();
        String addedDate = project.getAddedDate();
        String dueDate = project.getDueDate();
        String tag = project.getTag();
        String flag = project.getFlag();
        boolean completed = project.getCompleted();

        String format = "UPDATE project SET title='%s', description='%s', added_date='%s', due_date='%s', tag='%s', flag='%s', completed=%b WHERE project_id=%d";
        String query = String.format(format, title, des, addedDate, dueDate, tag, flag, completed, id);
        System.out.println(query);
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }
    }

    public static void deleteTask(int taskId) {
        String query = String.format("DELETE FROM task WHERE task_id=%d", taskId);
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }
    }

    public static void addNewProject(Project project, String username) {
        try {
            statement = connection.createStatement();
            String title = project.getTitle();
            String des = project.getDescription();
            String addedDate = project.getAddedDate();
            String format = "INSERT INTO project (username, title, description, added_date) VALUES ('%s', '%s', '%s', '%s')";
            String query = String.format(format, username, title, des, addedDate);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }

    }

    public static boolean authorize(String username, String password) {
        HashMap<String, String> userList = getUsers();
        if (userList.containsKey(username)) {
            return userList.get(username).equals(password);
        }
        return false;
    }

    public static boolean createAccount(String username, String password) {
        try {
            if (getUsers().containsKey(username)) {
                return false;
            }
            statement = connection.createStatement();
            String query = String.format("INSERT INTO %s (username, password) VALUES ('%s', '%s')", "user", username, password);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return true;
    }

    public static int getDateDif(int id, String dueDate, String table) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String queryColumn = table.equals("task") ? "task_id" : "project_id";
        String query = String.format("SELECT added_date FROM %s WHERE %s=%d", table, queryColumn, id);
        String addedDate = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                addedDate = resultSet.getString("added_date");
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }

        assert addedDate != null;
        LocalDate addedLocalDate = LocalDate.parse(addedDate, formatter);
        LocalDate dueLocalDate = LocalDate.parse(dueDate, formatter);
        return dueLocalDate.compareTo(addedLocalDate);
    }

    public static int getHighestId(String table) {
        String queryColumn = table.equals("task") ? "MAX(task_id)" : "MAX(project_id)";
        String query = String.format("SELECT %s FROM %s", queryColumn, table);
        int id = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                id = resultSet.getInt(queryColumn);
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }

        return id;
    }

    public static String getProjectTitle(int projectId) {
        String title = "";
        String query = String.format("SELECT title FROM project WHERE project_id=%d", projectId);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                title = resultSet.getString("title");
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return title;
    }

    public static HashMap<String, String> getUsers() {
        HashMap<String, String> users = new HashMap<>();
        try {
            statement = connection.createStatement();
            String query = String.format("SELECT username, password FROM %s;", "user");
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                users.put(rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return users;
    }

    public static HashMap<Integer, Task> getTasks(String username) {
        HashMap<Integer, Task> tasks = new HashMap<>();
        try {
            statement  = connection.createStatement();
            String query = String.format("SELECT * FROM %s WHERE username='%s'", "task", username);
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Task task = new Task(resultSet.getInt("task_id"),
                        resultSet.getInt("project_id"),
                        resultSet.getString("username"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("added_date"),
                        resultSet.getString("frequency"),
                        resultSet.getString("due_date"),
                        resultSet.getString("tag"),
                        resultSet.getString("flag"),
                        resultSet.getBoolean("completed"));
                tasks.put(task.getId(), task);
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return tasks;
    }

    public static HashMap<Integer, Task> getProjectTasks(String username, int projectId) {
        HashMap<Integer, Task> tasks = new HashMap<>();
        try {
            statement  = connection.createStatement();
            String query = String.format("SELECT * FROM task WHERE username='%s' AND project_id=%d;", username, projectId);
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Task task = new Task(resultSet.getInt("task_id"),
                        resultSet.getInt("project_id"),
                        resultSet.getString("username"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("added_date"),
                        resultSet.getString("frequency"),
                        resultSet.getString("due_date"),
                        resultSet.getString("tag"),
                        resultSet.getString("flag"),
                        resultSet.getBoolean("completed"));
                tasks.put(task.getId(), task);
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return tasks;
    }

    public static HashMap<Integer,Project> getProjects(String username) {
        HashMap<Integer, Project> projects = new HashMap<>();
        try {
            statement  = connection.createStatement();
            String query = String.format("SELECT * FROM project WHERE username='%s'", username);
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Project project = new Project(resultSet.getInt("project_id"),
                        resultSet.getString("username"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("added_date"),
                        resultSet.getString("due_date"),
                        resultSet.getString("tag"),
                        resultSet.getString("flag"),
                        resultSet.getBoolean("completed"));
                projects.put(project.getId(), project);
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return projects;
    }

    public static ArrayList<String> getFlags() {
        ArrayList<String> flags = new ArrayList<>();
        try {
            statement  = connection.createStatement();
            String query = String.format("SELECT flag FROM %s", "importance");
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                flags.add(resultSet.getString("flag"));
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return flags;
    }

    public static ArrayList<String> getFrequencies() {
        ArrayList<String> frequencies = new ArrayList<>();
        try {
            statement  = connection.createStatement();
            String query = String.format("SELECT frequency FROM %s", "frequency");
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                frequencies.add(resultSet.getString("frequency"));
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return frequencies;
    }

}
