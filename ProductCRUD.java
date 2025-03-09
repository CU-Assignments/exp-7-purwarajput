import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/db_name";
    private static final String USERNAME = "Purwa"; 
    private static final String PASSWORD = "Purwa@123"; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            while (true) {
                System.out.println("\n--- Product Management System ---");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addProduct(connection, scanner);
                        break;
                    case 2:
                        viewProducts(connection);
                        break;
                    case 3:
                        updateProduct(connection, scanner);
                        break;
                    case 4:
                        deleteProduct(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting... Thank you!");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void addProduct(Connection connection, Scanner scanner) {
        try {
            connection.setAutoCommit(false);
            System.out.print("Enter Product Name: ");
            scanner.nextLine(); 
            String name = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setDouble(2, price);
                stmt.setInt(3, quantity);
                stmt.executeUpdate();
            }

            connection.commit(); 
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Error adding product. Transaction rolled back.");
            e.printStackTrace();
        }
    }
  
    private static void viewProducts(Connection connection) {
        String query = "SELECT * FROM Product";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            System.out.println("\nProductID | ProductName | Price | Quantity");
            System.out.println("------------------------------------------");
            while (resultSet.next()) {
                int id = resultSet.getInt("ProductID");
                String name = resultSet.getString("ProductName");
                double price = resultSet.getDouble("Price");
                int quantity = resultSet.getInt("Quantity");

                System.out.println(id + " | " + name + " | " + price + " | " + quantity);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products.");
            e.printStackTrace();
        }
    }


    private static void updateProduct(Connection connection, Scanner scanner) {
        try {
            connection.setAutoCommit(false); 
            System.out.print("Enter ProductID to update: ");
            int productId = scanner.nextInt();
            System.out.print("Enter new Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new Quantity: ");
            int quantity = scanner.nextInt();

            String query = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setDouble(1, price);
                stmt.setInt(2, quantity);
                stmt.setInt(3, productId);
                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    connection.commit(); 
                    System.out.println("Product updated successfully!");
                } else {
                    System.out.println("No product found with that ID.");
                    connection.rollback(); 
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Error updating product. Transaction rolled back.");
            e.printStackTrace();
        }
    }
    private static void deleteProduct(Connection connection, Scanner scanner) {
        try {
            connection.setAutoCommit(false); 
            System.out.print("Enter ProductID to delete: ");
            int productId = scanner.nextInt();

            String query = "DELETE FROM Product WHERE ProductID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, productId);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    connection.commit(); 
                    System.out.println("Product deleted successfully!");
                } else {
                    System.out.println("No product found with that ID.");
                    connection.rollback(); 
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            System.out.println("Error deleting product. Transaction rolled back.");
            e.printStackTrace();
        }
    }
}
