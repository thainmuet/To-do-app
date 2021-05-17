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
            String query = String.format("SELECT * FROM %s WHERE username='%s';", "task", username);
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                Task task = new Task(resultSet.getInt("task_id"),
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

    public static int getFlagPoint(String flag) {
        String query = String.format("SELECT point FROM importance WHERE flag='%s'", flag);
        int point = 0;
        try {
            statement = connection.createStatement();
            point = statement.executeUpdate(query);
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return point;
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
        String title = task.getTitle();
        String des = task.getDescription();
        String addedDate = task.getAddedDate();
        String frequency = task.getFrequency();
        String dueDate = task.getDueDate();
        String tag = task.getTag();
        String flag = task.getFlag();
        boolean completed = task.getCompleted();

        String format = "UPDATE task SET title='%s', description='%s', added_date='%s', due_date='%s', frequency='%s', tag='%s', flag='%s', completed=%b WHERE task_id=%d";
        String query = String.format(format, title, des, addedDate, dueDate, frequency, tag, flag, completed, id);

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

    public static int getDateDif(int taskId, String dueDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String query = String.format("SELECT added_date FROM task WHERE task_id=%d", taskId);
        String addedDate = null;

        System.out.println("201: " + query);
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                addedDate = resultSet.getString("added_date");
                System.out.println("208: " + addedDate);
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }

        assert addedDate != null;
        LocalDate addedLocalDate = LocalDate.parse(addedDate, formatter);
        LocalDate dueLocalDate = LocalDate.parse(dueDate, formatter);
        return dueLocalDate.compareTo(addedLocalDate);
    }

    public static int getHighestTaskId() {
        String query = String.format("SELECT MAX(task_id) FROM %s", "task");
        int id = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                id = resultSet.getInt("MAX(task_id)");
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }

        return id;
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
