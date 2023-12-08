

package Controllers;

import Appointments.Appointment;
import Customers.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import main.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static Alerts.myAlerts.nothingDeletedAlert;
import static Appointments.AppointmentFunctions.*;
import static Contacts.ContactFunctions.*;
import static Countries.CountryFunctions.allCountries;
import static Countries.CountryFunctions.importCountries;
import static Customers.CustomerFunctions.*;
import static Divisions.DivisionFunctions.*;

/**
 * Controller for the Home scene. This is the main scene in the application with the most code.
 */
public class HomeController implements Initializable {

    @FXML
    private TableView<Customer> CustomersTable;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;

    @FXML
    private TableColumn<Customer, String> customerDivisionColumn;

    @FXML
    private TableColumn<Customer, String> customerCountryColumn;

    @FXML
    private TableColumn<Customer, String> customerPostalColumn;

    @FXML
    private TableColumn<Customer, Integer> customerIDColumn;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableColumn<Appointment, Integer> Appointment_ID_Column;

    @FXML
    private TableView<Appointment> AppointmentsTable;

    @FXML
    private TableColumn<Appointment, String> ContactColumn;

    @FXML
    private TableColumn<Appointment, Integer> Customer_ID_Column;

    @FXML
    private TableColumn<Appointment, String> DescriptionColumn;

    @FXML
    private TableColumn<Appointment, Timestamp> EndDateAndTimeColumn;

    @FXML
    private TableColumn<Appointment, String> LocationColumn;

    @FXML
    private TableColumn<Appointment, Timestamp> StartDateAndTimeColumn;

    @FXML
    private TableColumn<Appointment, String> TitleColumn;

    @FXML
    private TableColumn<Appointment, String> TypeColumn;

    @FXML
    private TableColumn<Appointment, Integer> User_ID_Column;

    @FXML
    private ToggleGroup appointmentRadioGroup;

    @FXML
    private Pane paneBottom;

    @FXML
    private Label timeLabel;

    /*
     * Customer Buttons
     */
    @FXML
    void homeAddCustomerButtonPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddCustomer.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void homeRemoveCustomerButtonPress(ActionEvent event) throws SQLException {
        removeCustomer(CustomersTable.getSelectionModel().getSelectedItem());
        allCustomers.clear();
        importCustomers();
    }

    @FXML
    void homeModifyCustomerButtonPress(ActionEvent event) throws IOException {
        if(CustomersTable.getSelectionModel().isEmpty()) {
            nothingDeletedAlert.showAndWait();
            return;
        }
        if(!CustomersTable.getSelectionModel().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/ModifyCustomer.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            ModifyCustomerController controller = fxmlLoader.getController();
            controller.initData(CustomersTable.getSelectionModel().getSelectedItem());
            stage.setTitle("Modify Customer");
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }

    /*
     * Appointment Buttons
     */

    @FXML
    void homeAddAppointmentButtonPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/AddAppointment.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void homeRemoveAppointmentButtonPress(ActionEvent event) throws SQLException {
        removeAppointment(AppointmentsTable.getSelectionModel().getSelectedItem());
        allAppointments.clear();
        importAppointments();
    }

    @FXML
    void homeModifyAppointmentButtonPress(ActionEvent event) throws IOException {
        if(AppointmentsTable.getSelectionModel().isEmpty()) {
            nothingDeletedAlert.showAndWait();
            return;
        }
        if(!AppointmentsTable.getSelectionModel().isEmpty()) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/ModifyAppointment.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            ModifyAppointmentController controller = fxmlLoader.getController();
            controller.initDataAppointment(AppointmentsTable.getSelectionModel().getSelectedItem());
            stage.setTitle("Modify Appointment");
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    void thisMonthButtonPress(ActionEvent event) {
        getAllAppointments().clear();
        importAppointments();

        LocalDate today = LocalDate.now();
        LocalDate first = today;
        while(first.getDayOfMonth() != 1) {
            first = first.minusDays(1);
        }
        today.plusDays(1);

        LocalDate last = today;
        while(last.getDayOfMonth() != today.getMonth().length(today.isLeapYear())) {
            last = last.plusDays(1);
        }

        ObservableList<Appointment> weekList = FXCollections.observableArrayList();

        for(Appointment appointment : getAllAppointments()) {
            LocalDate startDate = appointment.getStart().toLocalDateTime().toLocalDate();
            if((first.isBefore(startDate) || first.equals(startDate)) &
                    (last.isAfter(startDate)) || last.equals(startDate)) {
                weekList.add(appointment);
            }
        }
        AppointmentsTable.setItems(weekList);
    }
    @FXML
    void thisWeekButtonPress(ActionEvent event) throws SQLException {
        getAllAppointments().clear();
        importAppointments();

        LocalDate today = LocalDate.now();
        LocalDate sunday = today;
        while(sunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
            sunday = sunday.minusDays(1);
        }
        today.plusDays(1);
        LocalDate saturday = today;
        while(saturday.getDayOfWeek() != DayOfWeek.SATURDAY) {
            saturday = saturday.plusDays(1);
        }

        ObservableList<Appointment> weekList = FXCollections.observableArrayList();

        for(Appointment appointment : getAllAppointments()) {
            LocalDate startDate = appointment.getStart().toLocalDateTime().toLocalDate();
            if((sunday.isBefore(startDate) || sunday.equals(startDate)) &
                    (saturday.isAfter(startDate)) || saturday.equals(startDate)) {
                weekList.add(appointment);
            }
        }
        AppointmentsTable.setItems(weekList);
    }

    @FXML
    void viewAllButtonPress(ActionEvent event) {
        getAllAppointments().clear();
        importAppointments();
        AppointmentsTable.setItems(getAllAppointments());
    }

    @FXML
    void reportButtonPress(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Reports");
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        allCustomers.clear();
        allAppointments.clear();
        allContacts.clear();
        allDivisions.clear();
        allCountries.clear();
        importCustomers();
        importAppointments();
        importContacts();
        importDivisions();
        importCountries();

        CustomersTable.setItems(getAllCustomers());
        AppointmentsTable.setItems(getAllAppointments());

        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        ContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        Appointment_ID_Column.setCellValueFactory(new PropertyValueFactory<>("id"));
        TitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        StartDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        Customer_ID_Column.setCellValueFactory(new PropertyValueFactory<>("custID"));
        User_ID_Column.setCellValueFactory(new PropertyValueFactory<>("userID"));

        convertDivisionsColumnToCountry(customerCountryColumn);
        convertDivisionsColumn(customerDivisionColumn);
        convertContactColumn(ContactColumn);
        wrapColumns(TitleColumn);
        wrapColumns(DescriptionColumn);
        wrapColumns(LocationColumn);
        wrapColumns(TypeColumn);

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        timeLabel.setText(ts.toLocalDateTime().format(formatter));
    }
}
