import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class EmployeeDataFetcher {
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/db_name";
        String username = "Purwa"; 
        String password = "Purwa@123"; 

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);

            // Create a statement
            statement = connection.createStatement();

            // SQL query to retrieve data from the Employee table
            String query = "SELECT EmpID, Name, Salary FROM Employee";
            resultSet = statement.executeQuery(query);

            // Display data
            System.out.println("EmpID\tName\tSalary");
            System.out.println("-----------------------");
            while (resultSet.next()) {
                int empID = resultSet.getInt("EmpID");
                String name = resultSet.getString("Name");
                double salary = resultSet.getDouble("Salary");

                System.out.println(empID + "\t" + name + "\t" + salary);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection error.");
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
