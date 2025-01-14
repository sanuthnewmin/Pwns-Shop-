import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    private static final String URL = "jdbc:mysql://localhost:3306/PawsShop";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "20020216";


    public static Connection getConnection() {
        Connection connection = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Please add the driver to the classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed. Check the database credentials and URL.");
            e.printStackTrace();
        }
        return connection;
    }


    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to close the connection.");
                e.printStackTrace();
            }
        }
    }

    public static void createOrdersTable() {
    }

    public static void insertOrder(int i, String date, double totalAmount, String pending) {
    }

    public static void updatePaymentStatus(int i, String paid) {
    }
}
