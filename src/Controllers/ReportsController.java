
package Controllers;

import Appointments.Appointment;
import Contacts.Contact;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static Appointments.AppointmentFunctions.*;
import static Contacts.ContactFunctions.*;
import static Customers.CustomerFunctions.*;
import static Users.UserFunctions.allUsers;
import static Users.UserFunctions.importUsers;
import static main.JDBC.connection;
/**
 * Controller for the Reports scene. This is the scene with the generated reports.
 */
public class ReportsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<Appointment> contactTable;

    @FXML
    private TableColumn<Contact, Integer> contactCustomerIDColumn;

    @FXML
    private TableColumn<Contact, String> contactDescriptionColumn;

    @FXML
    private TableColumn<Contact, Timestamp> contactEndColumn;

    @FXML
    private TableColumn<Contact, Integer> contactIDColumn;

    @FXML
    private TableColumn<Contact, Timestamp> contactStartColumn;

    @FXML
    private TableColumn<Contact, String> contactTitleColumn;

    @FXML
    private TableColumn<Contact, String> contactTypeColumn;

    @FXML
    private ComboBox<Contact> selectContactBox;

    @FXML
    private ListView<String> appointmentListViewType;
    @FXML
    private ListView<String> appointmentListViewMonth;
    @FXML
    private ListView<String> appointmentListViewCount;

    @FXML
    private ListView<String> userListUsers;
    @FXML
    private ListView<String> userListTotal;

    @FXML
    void backButtonPress(ActionEvent event) throws IOException {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allCustomers.clear();
        allAppointments.clear();
        allContacts.clear();
        allUsers.clear();
        importCustomers();
        importAppointments();
        importContacts();
        importUsers();

        ObservableList<Appointment> contactList = FXCollections.observableArrayList();

        selectContactBox.setItems(getAllContacts());

        selectContactBox.setOnAction(e -> {
            contactList.clear();
            for(Appointment appointment : getAllAppointments()) {
                if(appointment.getContactID() == selectContactBox.getValue().getId()) {
                    contactList.add(appointment);
                }
            }
        });

        contactIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        contactTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        contactEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        contactCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("custID"));

        contactTable.setItems(contactList);

        ObservableList<String> appointmentMonth = FXCollections.observableArrayList();
        ObservableList<String> appointmentType = FXCollections.observableArrayList();
        ObservableList<String> appointmentCount = FXCollections.observableArrayList();
        ObservableList<String> userUsers = FXCollections.observableArrayList();
        ObservableList<String> userCount = FXCollections.observableArrayList();


        String sql = "SELECT Type, monthname(Start), count(*)\n" +
                "FROM client_schedule.appointments \n" +
                "GROUP BY monthname(Start), Type";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                appointmentType.add(rs.getString(1));
                appointmentMonth.add(rs.getString(2));
                appointmentCount.add(String.valueOf(rs.getInt(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i <= appointmentCount.toArray().length - 1; i++) {
            appointmentListViewType.getItems().add(appointmentType.get(i));
            appointmentListViewMonth.getItems().add(appointmentMonth.get(i));
            appointmentListViewCount.getItems().add(appointmentCount.get(i));
        }

        String sql1 = "SELECT User_ID, count(*)\n" +
                "FROM client_schedule.appointments \n" +
                "GROUP BY User_ID";
        try {
            Statement st = connection.createStatement();
            st.execute(sql1);
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                userUsers.add(rs.getString(1));
                userCount.add(rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i <=userCount.toArray().length - 1; i++) {
            userListUsers.getItems().add(userUsers.get(i));
            userListTotal.getItems().add(userCount.get(i));
        }
    }
}
