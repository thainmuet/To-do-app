import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {

    private static final String DB_NAME = "to_do_app";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/to_do_app";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Thai02032001";

    private static void printExceptionLog(SQLException e) {
        e.printStackTrace();
        System.out.println("SQLException: " + e.getMessage());
        System.out.println("SQLState: " + e.getSQLState());
        System.out.println("VendorError: " + e.getErrorCode());
    }

    public static void loadDriver() {
        try{
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDatabase() {

        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected");
            Statement stmt = conn.createStatement();
            String query = String.format("CREATE DATABASE IF NOT EXISTS %s;", DB_NAME);
            stmt.executeUpdate(query);
            System.out.println("Database created successfully!");
        } catch (SQLException e) {
            printExceptionLog(e);
        }
    }

    private static HashMap<String, String> getUsers() {
        HashMap<String, String> users = new HashMap<>();
        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            Statement stmt = conn.createStatement();
            String query = String.format("SELECT username, password FROM %s;", "user");
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                users.put(rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            printExceptionLog(e);
        }
        return users;
    }

    public static boolean logInAuthorize(String username, String password) {
        HashMap<String, String> userList = getUsers();
        if (userList.containsKey(username)) {
            return userList.get(username).equals(password);
        }
        return false;
    }

    public static boolean createAccount(String username, String password) {
        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (getUsers().containsKey(username)) {
                return false;
            }
            Statement stmt = conn.createStatement();
            String query = String.format("INSERT INTO %s (username, password) VALUES ('%s', '%s')", "user", username, password);
            stmt.executeUpdate(query);
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
}
