

package Countries;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.JDBC.connection;
/**
 * The CountryFunctions class contains all the methods used that mainly involve countries.
 */

public class CountryFunctions {
    public static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    public static void addCountry(Country newCountry) {
        allCountries.add(newCountry);
    }

    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    public static void importCountries() {
        String sql = "SELECT * FROM countries";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                addCountry(new Country(rs.getInt(1), rs.getString(2),
                        rs.getTimestamp(3), rs.getString(4), rs.getTimestamp(5),
                        rs.getString(6)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Country getCountryFromDivisionID (Integer divisionCountryID) {
        for(Country country : getAllCountries()) {
            if(country.getId() == divisionCountryID) {
                return country;
            }
        }
        return null;
    }
}
