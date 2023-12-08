

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
/**This is the package that contains the Main class
 * @author Jeff Fenwick
 */
/**
 *  Supplied Class Main.java
 */

/**
 * This is the main class which sets the first scene and runs the application. It also contains the method for writing
 * to the login_activity file. I left in the options to set the Locale and Timezone in the main method
 *
 * <p>
 *     The ReadMe File and the login_activity File are both located in the main project folder. I have used many
 *     lambda expressions in creating this application. I will attempt to identify a minimum of 2, but may not identify
 *     all of them.
 * </p>
 */

public class Main extends Application {

    public static void writeToFile(String text) throws IOException {
        Path fileName = Path.of("login_activity.txt");
        Files.writeString(fileName, "\n" +text , StandardOpenOption.APPEND);
    }
    public static Timestamp getTimestamp() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return ts;
    }

    @Override
    public void start(Stage stage) throws IOException {

        /**
         * <b>Lambda Expression!</b>
         * This Lambda expression was just used for fun when I was learning about them. It provides a timestamp for
         * when a user logs in.
         */
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> System.out.println(ts));

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        ResourceBundle rs = ResourceBundle.getBundle("Nat_fr", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("fr")) {
            stage.setTitle(rs.getString("Login"));
        } else {
            stage.setTitle("Login");
        }
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

    }

    public static void main(String[] args) {

        /**
         * Script used to change the Locale and TimeZone
         */
/*
        Locale.setDefault(new Locale("en"));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
*/

        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}