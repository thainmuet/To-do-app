import java.sql.*;

public class DatabaseManager {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Thai02032001";

    private static void printExceptionLog(SQLException e) {
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

    public static void createDatabase(String dbName) {

        try(Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            System.out.println("Connected");
            Statement stmt = conn.createStatement();
            String query = String.format("CREATE DATABASE IF NOT EXISTS %s", dbName);
            stmt.executeUpdate(query);
            System.out.println("Database created successfully!");
        } catch (SQLException e) {
            printExceptionLog(e);
        }
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
