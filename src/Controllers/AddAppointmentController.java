
package Controllers;

import Appointments.Appointment;
import Contacts.Contact;
import Customers.Customer;
import Divisions.Division;
import Users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.JDBC;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LongSummaryStatistics;
import java.util.ResourceBundle;

import static Alerts.myAlerts.fillAllFields;
import static Alerts.myAlerts.overlapAlert;
import static Appointments.AppointmentFunctions.generateNewAppointmentNumber;
import static Appointments.AppointmentFunctions.getAllAppointments;
import static Contacts.ContactFunctions.getAllContacts;
import static Customers.CustomerFunctions.*;
import static Divisions.DivisionFunctions.getAllDivisions;
import static Time.TimeFunctions.*;
import static Users.UserFunctions.getAllUsers;
import static Users.UserFunctions.getCurrentUser;
/**
 * Controller for the AddAppointment scene. There are a couple of Lambda expressions here.
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private Button addAppointmentButton;

    @FXML
    private TextField appointmentIDField;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<Contact> contactBox;

    @FXML
    private ComboBox<Customer> customerBox;

    @FXML
    private TextField descriptionField;

    @FXML
    private DatePicker endDateBox;

    @FXML
    private ComboBox<String> endTimeBox;

    @FXML
    private TextField locationField;

    @FXML
    private DatePicker startDateBox;

    @FXML
    private ComboBox<String> startTimeBox;

    @FXML
    private TextField titleField;

    @FXML
    private TextField typeField;

    @FXML
    private ComboBox<User> userBox;

    @FXML
    private Label startHoursLabel;

    @FXML
    private Label endHoursLabel;

    @FXML
    void addAppointmentButtonPress(ActionEvent event) throws IOException, SQLException {
        if(titleField.getText().equals("") || descriptionField.getText().equals("") ||
                locationField.getText().equals("") || typeField.getText().equals("") ||
                startTimeBox.getSelectionModel().getSelectedItem() == null ||
                startDateBox.getValue() == (null) || endTimeBox.getSelectionModel().getSelectedItem() == null ||
                endDateBox.getValue() == (null) || customerBox.getSelectionModel().getSelectedItem() == null ||
                contactBox.getSelectionModel().getSelectedItem() == null ||
                userBox.getSelectionModel().getSelectedItem() == null) {
            fillAllFields.showAndWait();
            return;
        }

        if(LocalDateTime.of(startDateBox.getValue(), LocalTime.parse(startTimeBox.getValue())).isBefore
                (openTime(startDateBox.getValue())) || LocalDateTime.of(startDateBox.getValue(),
                LocalTime.parse(endTimeBox.getValue())).isAfter(closeTime(startDateBox.getValue()))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            Alert outsideBusinessHours = new Alert(Alert.AlertType.ERROR,
                    "Please schedule within business hours: " + openTime(startDateBox.getValue()).format(formatter) +
                            " - " + closeTime(startDateBox.getValue()).format(formatter));
            outsideBusinessHours.showAndWait();
            return;
        }

        if(LocalTime.parse(startTimeBox.getValue()).isAfter(LocalTime.parse(endTimeBox.getValue()))) {
            Alert endTimeBeforeStart = new Alert(Alert.AlertType.ERROR,
                    "End time before Start");
            endTimeBeforeStart.showAndWait();
            return;
        }

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(startDateBox.getValue(),
                LocalTime.parse(startTimeBox.getValue())));
        Timestamp endTimeStamp = (Timestamp.valueOf(LocalDateTime.of(startDateBox.getValue(),
                LocalTime.parse(endTimeBox.getValue()))));

        for (Appointment appointment : getAllAppointments()) {
            if (appointment.getCustID() == customerBox.getValue().getId()) {
                if ((startTimestamp.after(appointment.getStart()) || startTimestamp.equals(appointment.getStart()))
                        & startTimestamp.before(appointment.getEnd())) {
                    overlapAlert.showAndWait();
                    return;
                }
                if (endTimeStamp.after(appointment.getStart()) &
                        (endTimeStamp.before(appointment.getEnd()) || endTimeStamp.equals(appointment.getEnd()))) {
                    overlapAlert.showAndWait();
                    return;
                }
                if ((startTimestamp.before(appointment.getStart()) || startTimestamp.equals(appointment.getStart())) &
                        (endTimeStamp.after(appointment.getEnd()) || endTimeStamp.equals(appointment.getEnd()))) {
                    overlapAlert.showAndWait();
                    return;
                }

            }

        }

        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start," +
                " end, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(appointmentIDField.getText()));
        ps.setString(2, titleField.getText());
        ps.setString(3, descriptionField.getText());
        ps.setString(4, locationField.getText());
        ps.setString(5, typeField.getText());
        ps.setTimestamp(6, startTimestamp);
        ps.setTimestamp(7, endTimeStamp);
        ps.setTimestamp(8, ts);
        ps.setString(9, getCurrentUser());
        ps.setTimestamp(10, ts);
        ps.setString(11, getCurrentUser());
        ps.setInt(12, customerBox.getValue().getId());
        ps.setInt(13, userBox.getValue().getId());
        ps.setInt(14, contactBox.getValue().getId());
        ps.executeUpdate();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void cancelButtonPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void descriptionFieldAction(ActionEvent event) {
        addAppointmentButton.fire();
    }

    @FXML
    void locationFieldAction(ActionEvent event) {
        addAppointmentButton.fire();
    }

    @FXML
    void titleFieldAction(ActionEvent event) {
        addAppointmentButton.fire();
    }

    @FXML
    void typeFieldAction(ActionEvent event) {
        addAppointmentButton.fire();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIDField.setText(String.valueOf(generateNewAppointmentNumber()));
        allCustomers.clear();
        importCustomers();

        ObservableList<String> startTimeBoxList = FXCollections.observableArrayList();
        ObservableList<String> endTimeBoxList = FXCollections.observableArrayList();
        ObservableList<Customer> customerBoxList = FXCollections.observableArrayList();
        ObservableList<User> userBoxList = FXCollections.observableArrayList();
        ObservableList<Contact> contactBoxList = FXCollections.observableArrayList();


        endDateBox.setDisable(true);
        startTimeBox.setDisable(true);
        endTimeBox.setDisable(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        startHoursLabel.setText(String.valueOf(openTime(ts.toLocalDateTime().toLocalDate()).format(formatter)));
        endHoursLabel.setText(String.valueOf(closeTime(ts.toLocalDateTime().toLocalDate()).format(formatter)));

        /**
         * <b>Lambda Expression!</b>
         * There are 2 Lambda expressions used here to provide on-action events for the combo boxes. There was probably
         * a way to do this through SceneBuilder, but I wanted to try using Lambda expressions.
         */

        startDateBox.setOnAction(e -> {
            endDateBox.setDisable(false);
            startTimeBox.setDisable(false);
            endTimeBox.setDisable(false);

            LocalDateTime start = LocalDateTime.of(startDateBox.getValue(), (LocalTime.parse("00:00", formatter)));
            LocalDateTime end = LocalDateTime.of(startDateBox.getValue(), (LocalTime.parse("00:30", formatter)));

            for(int i = 0; i < 47; i++) {
                startTimeBoxList.add(start.format(formatter));
                start = start.plusMinutes(30);
                endTimeBoxList.add(end.format(formatter));
                end = end.plusMinutes(30);
            }
            endDateBox.setValue(startDateBox.getValue());
            endDateBox.setDayCellFactory(datePicker -> new DateCell() {
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate start = startDateBox.getValue();
                    setDisable(empty || date.compareTo(start) < 0);
                }
            });
        });



        for(Customer customer : getAllCustomers()) {
            customerBoxList.add(customer);
        }
        for(User user : getAllUsers()) {
            userBoxList.add(user);
        }
        for(Contact contact : getAllContacts()) {
            contactBoxList.add(contact);
        }

        startTimeBox.setItems(startTimeBoxList);
        endTimeBox.setItems(endTimeBoxList);
        customerBox.setItems(customerBoxList);
        userBox.setItems(userBoxList);
        contactBox.setItems(contactBoxList);
    }
}
