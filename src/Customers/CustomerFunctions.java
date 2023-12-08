
package Customers;

import Appointments.Appointment;
import main.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Alerts.myAlerts.cantDeleteCustomer;
import static Alerts.myAlerts.nothingDeletedAlert;
import static Appointments.AppointmentFunctions.getAllAppointments;
import static main.JDBC.connection;
/**
 * The CustomerFunctions class contains all the methods used that mainly involve customers.
 */

public class CustomerFunctions {

    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public static void addCustomer(Customer newCustomer) {
        allCustomers.add(newCustomer);
    }

    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public static void importCustomers() {
        String sql = "SELECT * FROM customers";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while(rs.next()) {
                addCustomer(new Customer(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getTimestamp(6), rs.getString(7), rs.getTimestamp(8),
                        rs.getString(9), rs.getInt(10)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printCustomerInfo() {
        for (Customer cust : getAllCustomers()) {
            System.out.println(cust.getName() + ", " + cust.getAddress() +
                    ", " + cust.getPhone());
        }

    }
    public static int generateNewCustomerNumber() {
        int maxNumber = 0;
        for(Customer customer : getAllCustomers()) {
            if(customer.getId() > maxNumber) {
                maxNumber = customer.getId();
            }
        }
        return (maxNumber + 1);
    }
    public static void removeCustomer(Customer customer) throws SQLException {
        if(customer == null) {
            nothingDeletedAlert.showAndWait();
            return;
        }
        for(Appointment appointment : getAllAppointments()) {
            if (appointment.getCustID() == customer.getId()) {
                cantDeleteCustomer.showAndWait();
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove " + customer.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customer.getId());
            ps.executeUpdate();
            Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION, customer.getName() + " Deleted", ButtonType.OK);
            deletedAlert.showAndWait();
        }
    }
    public static Customer getCustomerFromCustomerID (Integer customerID) {
        for(Customer customer : getAllCustomers()) {
            if(customer.getId() == customerID) {
                return customer;
            }
        }
        return null;
    }
}
