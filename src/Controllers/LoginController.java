
package Controllers;

import Appointments.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import main.Main;
import Users.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static Alerts.myAlerts.*;
import static Appointments.AppointmentFunctions.getAllAppointments;
import static Appointments.AppointmentFunctions.importAppointments;
import static Users.UserFunctions.*;
import static main.Main.getTimestamp;
import static main.Main.writeToFile;
/**
 * Controller for the Login scene. This is the first scene that is loaded and allows the user to input a username and
 * password to login.
 */
public class LoginController implements Initializable {

    @FXML
    private Label loginTimeZoneLabel;

    @FXML
    private Label loginLocaleLabel;

    @FXML
    private Label loginLocaleTitleLabel;

    @FXML
    private Label loginTimeZoneTitleLabel;

    @FXML
    private Button loginButton;

    @FXML
    private Button quitButton;

    @FXML
    private Label loginTitleLabel;

    @FXML
    private Label loginUsernameLabel;

    @FXML
    private TextField loginUsernameTextField;

    @FXML
    private Label loginPasswordLabel;

    @FXML
    private PasswordField loginPasswordTextField;

    @FXML
    void loginButtonClick(ActionEvent event) throws IOException {

        String username = loginUsernameTextField.getText();
        String password = loginPasswordTextField.getText();

        if(username.isEmpty()) {
            noUsername.showAndWait();
            return;
        }

        if (password.isEmpty()) {
            noPassword.showAndWait();
            return;
        }

        importUsers();
        String errorType = null;
        for(User user : getAllUsers()) {
            if(user.getName().equals(username)) {
                if(user.getPassword().equals(password)) {

                    writeToFile(loginUsernameTextField.getText() + " " + getTimestamp() + " Successful");

                    getAllAppointments().clear();
                    importAppointments();

                    Timestamp ts = new Timestamp(System.currentTimeMillis());

                    Timestamp ts15 = Timestamp.valueOf(ts.toLocalDateTime().plusMinutes(15));

                    ObservableList<Appointment> upcomingAppointmentList = FXCollections.observableArrayList();

                    for (Appointment appointment : getAllAppointments()) {
                        if (appointment.getStart().before(ts15) & appointment.getStart().after(ts)) {
                            upcomingAppointmentList.add(appointment);
                        }
                    }
                    if(upcomingAppointmentList.isEmpty()) {
                        Alert noUpcomingAppointments = new Alert(Alert.AlertType.INFORMATION,
                                "No Upcoming Appointments", ButtonType.OK);
                        noUpcomingAppointments.showAndWait();
                    }
                    if(!upcomingAppointmentList.isEmpty()) {
                        for (Appointment upAppointments : upcomingAppointmentList) {
                            Alert upcomingAppointment = new Alert(Alert.AlertType.INFORMATION, "Upcoming Appointment: " +
                                    upAppointments.getId() + " at " + upAppointments.getStart(), ButtonType.OK);
                            upcomingAppointment.showAndWait();
                        }
                    }

                    setCurrentUser(user.getName());
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Home.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
                    stage.setTitle("Home");
                    stage.setScene(scene);
                    stage.show();
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
                    return;
                } else {
                    errorType = "B";
                }
            } else {
                errorType = "A";

            }
        }

        if(errorType.equals("A")) {
            invalidUsername.showAndWait();
            writeToFile(loginUsernameTextField.getText() + " " + getTimestamp() + " Unsuccessful");
            return;
        }
        if(errorType.equals("B")) {
            passIncorrect.showAndWait();
            writeToFile(loginUsernameTextField.getText() + " " + getTimestamp() + " Unsuccessful");
            return;
        }
    }


    @FXML
    void quitButtonClick(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void loginPasswordText(ActionEvent event) {
        loginButton.fire();
    }

    @FXML
    void loginUsernameText(ActionEvent event) {
        loginButton.fire();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
         * Lambda Expression!
         */

        loginUsernameTextField.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                Platform.exit();
            }
        });

        loginPasswordTextField.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                Platform.exit();
            }
        });

        loginLocaleTitleLabel.setAlignment(Pos.CENTER_RIGHT);
        loginTimeZoneTitleLabel.setAlignment(Pos.CENTER_RIGHT);
        loginTitleLabel.setAlignment(Pos.CENTER);
        loginUsernameLabel.setAlignment(Pos.CENTER_RIGHT);
        loginPasswordLabel.setAlignment(Pos.CENTER_RIGHT);

        ResourceBundle rs = ResourceBundle.getBundle("Nat_fr", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("fr")) {
            loginLocaleTitleLabel.setText(rs.getString(loginLocaleTitleLabel.getText()));
            loginTimeZoneTitleLabel.setText(rs.getString(loginTimeZoneTitleLabel.getText()));
            loginButton.setText(rs.getString(loginButton.getText()));
            loginTitleLabel.setText(rs.getString(loginTitleLabel.getText()));
            loginUsernameLabel.setText(rs.getString(loginUsernameLabel.getText()));
            loginPasswordLabel.setText(rs.getString(loginPasswordLabel.getText()));
            quitButton.setText(rs.getString(quitButton.getText()));
        } else if (Locale.getDefault().getLanguage().equals("en")) {

        } else {
            localeNotRecognized.showAndWait();
            Platform.exit();
        }

        loginLocaleTitleLabel.setText(loginLocaleTitleLabel.getText() + ":");
        loginTimeZoneTitleLabel.setText(loginTimeZoneTitleLabel.getText() + ":");
        loginPasswordLabel.setText(loginPasswordLabel.getText() + ":");
        loginUsernameLabel.setText(loginUsernameLabel.getText() + ":");

        TimeZone tz = TimeZone.getDefault();
        loginTimeZoneLabel.setText(String.valueOf(tz.getID()));
        loginLocaleLabel.setText(String.valueOf(Locale.getDefault()));

    }
}
