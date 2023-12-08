

package Contacts;

import Appointments.Appointment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.JDBC.connection;
/**
 * The ContactFunctions class contains all the methods used that mainly involve functions. There is also a lambda
 * expression here
 */
public class ContactFunctions {

    public static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    public static void addContact(Contact newContact) {
        allContacts.add(newContact);
    }

    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    public static void importContacts() {
        String sql = "SELECT * FROM contacts";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while(rs.next()) {
                addContact(new Contact(rs.getInt(1), rs.getString(2),
                        rs.getString(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printContactInfo() {
        for (Contact contact : getAllContacts()) {
            System.out.println(contact.getName() + ", " + contact.getId() +
                    ", " + contact.getEmail());
        }

    }

    public static String convertContactIDtoName(Integer contactID) {
        for (Contact contact : getAllContacts()) {
            if(contact.getId() == contactID) {
                return contact.getName();
            }
        }
        return null;
    }

    /**
     * <b>Lambda Expression!</b>
     * This method contains a lambda expression similar to the expression located in AppointmentFunctions, except in
     * addition to wrapping the columns, the Contact ID number is converted to the Contact name.
     * @param column
     */
    public static void convertContactColumn (TableColumn column) {
        column.setCellFactory(f -> {
            TableCell<Appointment, Integer> cell = new TableCell<Appointment, Integer>() {
                Text text = new Text();

                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    Platform.runLater(() -> {
                        if (empty) {
                            setGraphic(null);
                        } else {
                            text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                            text.setText(convertContactIDtoName(item));
                            setGraphic(text);
                        }
                    });
                }
            };
            return cell;
        });
    }

}


