import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/shivi_bank";
            String user = "root"; String pass = "Pranu@123";
             return DriverManager.getConnection(url, user, pass);
        }
        catch (Exception e) {
            System.out.println("Database Connection Failed: " + e);
            return null;
        }
    }
}
