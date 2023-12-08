
package Users;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static main.JDBC.connection;
/**
 * The UserFunctions class contains all the methods used that mainly involve Users.
 */
public class UserFunctions {

    private static String currentUser = null;
    public static ObservableList<User> allUsers = FXCollections.observableArrayList();

    public static void addUser(User newUser) {
        allUsers.add(newUser);
    }

    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    public static void importUsers() {
        String sql = "SELECT * FROM users";
        try {
            Statement st = connection.createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            while (rs.next()) {
                addUser(new User(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getTimestamp(4), rs.getString(5),
                        rs.getTimestamp(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printUserInfo() {
        for (User user : getAllUsers()) {
            System.out.println(user.getName() + ", " + user.getPassword() +
                    ", " + user.getId());
        }
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static String getCurrentUser() {
        return currentUser;
    }
    public static User getUserFromUserID (Integer userID) {
        for(User user : getAllUsers()) {
            if(user.getId() == userID) {
                return user;
            }
        }
        return null;
    }
}
