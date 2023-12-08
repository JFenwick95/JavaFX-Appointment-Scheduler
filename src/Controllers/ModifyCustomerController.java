
package Controllers;

import Countries.Country;
import Customers.Customer;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static Alerts.myAlerts.fillAllFields;
import static Alerts.myAlerts.noDivisionSelected;
import static Countries.CountryFunctions.*;
import static Divisions.DivisionFunctions.*;
import static Users.UserFunctions.getCurrentUser;
/**
 * Controller for the ModifyCustomer scene. This allows the user to modify customer attributes.
 */
public class ModifyCustomerController implements Initializable {

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<Country> countryBox;

    @FXML
    private Button customerButton;

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
    void modifyCustomerButton(ActionEvent event) throws SQLException, IOException {
        if(stateBox.getSelectionModel().isEmpty()) {
            noDivisionSelected.showAndWait();
            return;
        }
        if(nameField.getText().equals("") || addressField.getText().equals("") || postalCodeField.getText().equals("")
                || phoneNumberField.getText().equals("") || countryBox.getSelectionModel().getSelectedItem() == null ||
                stateBox.getSelectionModel().getSelectedItem() == null) {
            fillAllFields.showAndWait();
            return;
        }
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?," +
                " Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, nameField.getText());
        ps.setString(2, addressField.getText());
        ps.setString(3, postalCodeField.getText());
        ps.setString(4, phoneNumberField.getText());
        ps.setTimestamp(5, ts);
        ps.setString(6, getCurrentUser());
        ps.setTimestamp(7, ts);
        ps.setString(8, getCurrentUser());
        ps.setInt(9, stateBox.getValue().getId());
        ps.setInt(10, Integer.parseInt(customerIDField.getText()));

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
    void addressFieldAction(ActionEvent event) {

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

    @FXML
    void nameFieldAction(ActionEvent event) {

    }

    @FXML
    void phoneNumberAction(ActionEvent event) {

    }

    @FXML
    void postalCodeFieldAction(ActionEvent event) {

    }

    public void initData(Customer customer) {
        Customer selectedCustomer = customer;
        customerIDField.setText(String.valueOf(customer.getId()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        phoneNumberField.setText(customer.getPhone());
        postalCodeField.setText(customer.getPostalCode());

        ObservableList<Country> countryBoxList = FXCollections.observableArrayList();
        countryBox.setValue(getCountryFromCustomerID(customer.getId()));
        for(Country country : getAllCountries()) {
            countryBoxList.add(country);
        }
        countryBox.setItems(countryBoxList);

        ObservableList<Division> divisionBoxList = FXCollections.observableArrayList();
        stateBox.setValue(getDivisionFromCustomerID(customer.getId()));
        for(Division division : getAllDivisions()) {
            if(division.getCountryID() == stateBox.getValue().getCountryID()) {
                divisionBoxList.add(division);
            }
        }
        stateBox.setItems(divisionBoxList);

        countryBox.setOnAction(e -> {
            divisionBoxList.clear();
            stateBox.valueProperty().set(null);
            for(Division division : getAllDivisions()) {
                if(division.getCountryID() == countryBox.getSelectionModel().getSelectedItem().getId()) {
                    divisionBoxList.add(division);
                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allCountries.clear();
        allDivisions.clear();

        importCountries();
        importDivisions();

    }
}
