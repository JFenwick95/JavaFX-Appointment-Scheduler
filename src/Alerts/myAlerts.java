
package Alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * The only class file in the ALerts package. Most of the alerts used throughout the application are stored here.
 */
public class myAlerts {

    /*
     * Login Screen Alerts
     */

    static String localeNotRecognizedString = "Locale Not Recognized";
    static String noUsernameString = "Enter a Username";
    static String enterAPasswordString = "Enter a Password";
    static String invalidUsernameString = "Invalid Username";
    static String incorrectPasswordString = "Incorrect Password";

    public static String translateAlerts(String tString) {
        String translatedString = "";
        ResourceBundle rs = ResourceBundle.getBundle("Nat_fr", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            ArrayList tokens = new ArrayList<String>();
            StringTokenizer tok = new StringTokenizer(tString);
            while(tok.hasMoreTokens()) {
                tokens.add(rs.getString(tok.nextToken()));
            }
            return String.join(" ", tokens);
        }
        return tString;
    }

    public static Alert localeNotRecognized = new Alert(Alert.AlertType.ERROR,
            translateAlerts(localeNotRecognizedString), ButtonType.OK);

    public static Alert noUsername = new Alert(Alert.AlertType.ERROR,
            translateAlerts(noUsernameString), ButtonType.OK);

    public static Alert noPassword = new Alert(Alert.AlertType.ERROR,
            translateAlerts(enterAPasswordString), ButtonType.OK);

    public static Alert invalidUsername = new Alert(Alert.AlertType.ERROR,
            translateAlerts(invalidUsernameString), ButtonType.OK);

    public static Alert passIncorrect = new Alert(Alert.AlertType.ERROR,
            translateAlerts(incorrectPasswordString), ButtonType.OK);


    /*
     * Other Alerts
     */


    public static Alert nothingDeletedAlert = new Alert(Alert.AlertType.ERROR,
            "Nothing Selected", ButtonType.OK);

    public static Alert cantDeleteCustomer = new Alert(Alert.AlertType.ERROR,
            "Delete All Customer Appointments First", ButtonType.OK);

    public static Alert noDivisionSelected = new Alert(Alert.AlertType.ERROR,
            "No Division Selected", ButtonType.OK);

    public static Alert overlapAlert = new Alert(Alert.AlertType.ERROR,
            "Appointment Overlap", ButtonType.OK);

    public static Alert fillAllFields = new Alert(Alert.AlertType.ERROR,
            "Fill Out All Fields", ButtonType.OK);


}
