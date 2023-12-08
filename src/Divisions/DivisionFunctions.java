
package Divisions;

import Countries.Country;
import Customers.Customer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Countries.CountryFunctions.getAllCountries;
import static Customers.CustomerFunctions.getAllCustomers;
import static main.JDBC.connection;
/**
 * The CustomerFunctions class contains all the methods used that mainly involve divisions.
 */
public class DivisionFunctions {
    public static ObservableList<Division> allDivisions = FXCollections.observableArrayList();

    public static void addDivision(Division newDivision) {
        allDivisions.add(newDivision);
    }

    public static ObservableList<Division> getAllDivisions() {
        return allDivisions;
    }

    public static void importDivisions() {
        String sql = "SELECT * FROM first_level_divisions";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                addDivision(new Division(rs.getInt(1), rs.getString(2),
                        rs.getTimestamp(3), rs.getString(4), rs.getTimestamp(5),
                        rs.getString(6), rs.getInt(7)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void convertDivisionsColumn (TableColumn column) {
        column.setCellFactory(f -> {
            TableCell<Customer, Integer> cell = new TableCell<Customer, Integer>() {
                Text text = new Text();

                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    Platform.runLater(() -> {
                        if (empty) {
                            setGraphic(null);
                        } else {
                            for(Division division : getAllDivisions()) {
                                if(item.equals(division.getId())) {
                                    text.setText(division.getName());
                                }
                            }
                            text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                            setGraphic(text);
                        }
                    });
                }
            };
            return cell;
        });
    }
    public static void convertDivisionsColumnToCountry (TableColumn column) {
        column.setCellFactory(f -> {
            TableCell<Customer, Integer> cell = new TableCell<Customer, Integer>() {
                Text text = new Text();

                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    Platform.runLater(() -> {
                        if (empty) {
                            setGraphic(null);
                        } else {
                            for(Division division : getAllDivisions()) {
                                if(item.equals(division.getId())) {
                                    for(Country country : getAllCountries()) {
                                        if(division.getCountryID() == country.getId()) {
                                            text.setText(country.getName());
                                        }
                                    }
                                }
                            }
                            text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                            setGraphic(text);
                        }
                    });
                }
            };
            return cell;
        });
    }
    public static Division getDivisionFromDivisionID (Integer divisionID) {
        for(Division division : getAllDivisions()) {
            if(division.getId() == divisionID) {
                return division;
            }
        }
        return null;
    }
    public static Country getCountryFromCustomerID (Integer customerID) {
        for(Customer customer : getAllCustomers()) {
            if(customer.getId() == customerID) {
                for(Division division : getAllDivisions()) {
                    if(customer.getDivisionID() == division.getId()) {
                        for(Country country : getAllCountries()) {
                            if(division.getCountryID() == country.getId()) {
                                return country;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Division getDivisionFromCustomerID (Integer customerID) {
        for(Customer customer : getAllCustomers()) {
            if(customer.getId() == customerID) {
                for(Division division : getAllDivisions()) {
                    if(customer.getDivisionID() == division.getId()) {
                        return division;
                    }
                }
            }
        }
        return null;
    }
}
