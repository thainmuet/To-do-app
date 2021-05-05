import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class App extends Application {

    private static final String APP_NAME = "To-do";
    private static final String APP_WINDOW_PATH = "view/AppWindow.fxml";
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/To_do_app";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        launch(args);
    }

    private void loadDriver() {
        try{
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            conn.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    private void launchApp() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(App.class.getResource(APP_WINDOW_PATH));

        AnchorPane rootLayout = new AnchorPane();
        loader.setRoot(rootLayout);
        Scene scene = new Scene(rootLayout);
        Stage appWindow = new Stage();
        appWindow.setScene(scene);
        appWindow.setTitle(APP_NAME);
//            appWindow.getIcons().add(new Image(APPLICATION_ICON_PATH));
//            appWindow.setOnCloseRequest(e -> {
//                e.consume();
//                closeProgram();
//            });
        appWindow.show();
    }

    @Override
    public void start(Stage window) {
        loadDriver();
        launchApp();
    }

}
