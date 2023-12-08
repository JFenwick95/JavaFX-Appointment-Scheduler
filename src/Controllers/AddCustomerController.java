
package Controllers;

import Countries.Country;
import Divisions.Division;
import main.JDBC;
import main.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static Alerts.myAlerts.fillAllFields;
import static Countries.CountryFunctions.*;
import static Customers.CustomerFunctions.generateNewCustomerNumber;
import static Divisions.DivisionFunctions.*;
import static Users.UserFunctions.getCurrentUser;
/**
 * Controller for the AddCustomer scene. There is a Lambda expression here.
 */
public class AddCustomerController implements Initializable {

    @FXML
    private Button customerButton;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<Country> countryBox;

    @FXML
    private TextField customerIDField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField postalCodeField;

    @FXML
    private ComboBox<Division> stateBox;

    @FXML
    void addCustomerButton(ActionEvent event) throws IOException, SQLException {
        if(nameField.getText().equals("") || addressField.getText().equals("") || postalCodeField.getText().equals("")
            || phoneNumberField.getText().equals("") || countryBox.getSelectionModel().getSelectedItem() == null ||
            stateBox.getSelectionModel().getSelectedItem() == null) {
            fillAllFields.showAndWait();
            return;
        }

        Timestamp ts = new Timestamp(System.currentTimeMillis());

        String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date," +
                " Created_By, Last_Update, Last_Updated_By, Division_ID) Values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(customerIDField.getText()));
        ps.setString(2, nameField.getText());
        ps.setString(3, addressField.getText());
        ps.setString(4, postalCodeField.getText());
        ps.setString(5, phoneNumberField.getText());
        ps.setTimestamp(6, ts);
        ps.setString(7, getCurrentUser());
        ps.setTimestamp(8, ts);
        ps.setString(9, getCurrentUser());
        ps.setInt(10, stateBox.getSelectionModel().getSelectedItem().getId());
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
    void cancelButton(ActionEvent event) throws IOException {
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
        customerIDField.setText(String.valueOf(generateNewCustomerNumber()));
        allCountries.clear();
        allDivisions.clear();
        importCountries();
        importDivisions();

        stateBox.setDisable(true);

        ObservableList<Division> divisionBoxList = FXCollections.observableArrayList();
        ObservableList<Country> countryBoxList = FXCollections.observableArrayList();

        for(Country country : getAllCountries()) {
            countryBoxList.add(country);
        }

        /**
         * <b>Lambda Expression!</b>
         * Used to set on-action events for the combo box
         */
        countryBox.setOnAction(e -> {
            divisionBoxList.clear();
            stateBox.setDisable(false);
            for(Division division : getAllDivisions()) {
                if(division.getCountryID() == countryBox.getSelectionModel().getSelectedItem().getId()) {
                    divisionBoxList.add(division);
                }
            }
        });


        stateBox.setItems(divisionBoxList);
        countryBox.setItems(countryBoxList);


    }

    @FXML
    void addressFieldAction(ActionEvent event) {
        customerButton.fire();
    }

    @FXML
    void phoneNumberAction(ActionEvent event) {
        customerButton.fire();
    }

    @FXML
    void postalCodeFieldAction(ActionEvent event) {
        customerButton.fire();
    }

    @FXML
    void nameFieldAction(ActionEvent event) {
        customerButton.fire();
    }
}
