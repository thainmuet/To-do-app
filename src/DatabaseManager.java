import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManager {

    private static final String DB_NAME = "to_do";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/to_do";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Thai02032001";

    private static Connection connection = null;
    private static Statement statement = null;

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

    public static ArrayList<Task> getTasks(String username) {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            statement  = connection.createStatement();
            String query = String.format("SELECT * FROM %s WHERE username='%s';", "task", username);
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                Task task = new Task(rs.getInt("task_id"),
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("added_date"),
                        rs.getString("added_date"),
                        rs.getString("tag"),
                        rs.getString("flag"),
                        rs.getBoolean("completed"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return tasks;
    }

    public static void addNewTask(Task task, String username) {
        try {
            statement = connection.createStatement();
            String title = task.getTitle();
            String des = task.getDescription();
            String query = String.format("INSERT INTO task (username, title, description) VALUES ('%s', '%s', '%s')", username, title, des);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }

    }

    public static void editTask(Task task) {
        int id = task.getId();
        String username = task.getUsername();
        String title = task.getTitle();
        String des = task.getDescription();
        String dueDate = task.getDueDate();
        String tag = task.getTag();
        String flag = task.getFlag();
        boolean completed = task.getCompleted();

        String format = "UPDATE task SET title='%s', description='%s', tag='%s', flag='%s', completed=%b WHERE task_id=%d AND username='%s'";
        String query = String.format(format, title, des, tag, flag, completed, id, username);

        try {
            statement = connection.createStatement();
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

    public static void executeUpdate(String query) {
        Statement st = null;
        ResultSet rs = null;

        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected");
            st = conn.createStatement();
            st.executeUpdate(query);
            System.out.println("Updated OK!");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
