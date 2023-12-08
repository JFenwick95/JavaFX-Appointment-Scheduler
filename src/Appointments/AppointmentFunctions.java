package Appointments;

import Customers.Customer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import main.JDBC;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static Alerts.myAlerts.cantDeleteCustomer;
import static Alerts.myAlerts.nothingDeletedAlert;
import static Time.TimeFunctions.utcToSYS;
import static main.JDBC.connection;
/**
 * The AppointmentFunctions class contains all the methods used that mainly involve appointments. There is also a lambda
 * expression here
 */
public class AppointmentFunctions {
    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static void addAppointment(Appointment newAppointment) {
        allAppointments.add(newAppointment);
    }

    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public static void importAppointments() {
        String sql = "SELECT * FROM appointments";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                addAppointment(new Appointment(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        (rs.getTimestamp(6)), (rs.getTimestamp(7)),
                        (rs.getTimestamp(8)), rs.getString(9),
                        (rs.getTimestamp(10)), rs.getString(11),
                        rs.getInt(12), rs.getInt(13), rs.getInt(14)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void printAppointmentInfo() {
        for (Appointment appointment : getAllAppointments()) {
            System.out.println(appointment.getDescription() + ", " + appointment.getStart() +
                    ", " + appointment.getEnd());
        }
    }

    /**
     * <b>
     *     Lambda Expression!
     * </b>
     * This was convenient when developing a method for wrap the columns on my "Home" page. I was able to come up with
     * this method through research and trial and error and it works dynamically for the columns when they are resized.
     */
    public static void wrapColumns (TableColumn column) {
        column.setCellFactory(f -> {
            TableCell<Appointment, String> cell = new TableCell<Appointment, String>() {
                Text text = new Text();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    Platform.runLater(() -> {
                        if (empty) {
                            setGraphic(null);
                        } else {
                            text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                            text.setText(item);
                            setGraphic(text);
                        }
                    });
                }
            };
            return cell;
        });
    }

    public static int generateNewAppointmentNumber() {
        int maxNumber = 0;
        for(Appointment appointment : getAllAppointments()) {
            if(appointment.getId() > maxNumber) {
                maxNumber = appointment.getId();
            }
        }
        return (maxNumber + 1);
    }

    public static void removeAppointment(Appointment appointment) throws SQLException {
        if(appointment == null) {
            nothingDeletedAlert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove " + appointment.getId() + ", "
                + appointment.getType() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, appointment.getId());
            ps.executeUpdate();
            Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION, appointment.getId() +
                    " " + appointment.getType() + " canceled", ButtonType.OK);
            deleteAlert.showAndWait();
        }
    }
}