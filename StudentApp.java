Model: Student.java

public class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }

    @Override
    public String toString() {
        return studentID + " | " + name + " | " + department + " | " + marks;
    }
}

Controller: StudentController.java

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB";
    private static final String USERNAME = "Purwa"; 
    private static final String PASSWORD = "Purwa@123"; 

    public StudentController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
  
    public void addStudent(String name, String department, double marks) {
        String query = "INSERT INTO Students (Name, Department, Marks) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, department);
            stmt.setDouble(3, marks);
            stmt.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                students.add(new Student(rs.getInt("StudentID"), rs.getString("Name"),
                        rs.getString("Department"), rs.getDouble("Marks")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public void updateStudent(int studentID, double marks) {
        String query = "UPDATE Students SET Marks = ? WHERE StudentID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, marks);
            stmt.setInt(2, studentID);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("No student found with ID: " + studentID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    public void deleteStudent(int studentID) {
        String query = "DELETE FROM Students WHERE StudentID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, studentID);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with ID: " + studentID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

View: StudentView.java

  import java.util.List;
import java.util.Scanner;

public class StudentView {
    private StudentController controller;
    private Scanner scanner;

    public StudentView(StudentController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Student Management System ---");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    updateStudent();
                    break;
                case 4:
                    deleteStudent();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void addStudent() {
        System.out.print("Enter Student Name: ");
        scanner.nextLine();
        String name = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Marks: ");
        double marks = scanner.nextDouble();

        controller.addStudent(name, department, marks);
    }

    private void viewStudents() {
        List<Student> students = controller.getAllStudents();
        System.out.println("\nStudentID | Name | Department | Marks");
        System.out.println("-------------------------------------");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void updateStudent() {
        System.out.print("Enter StudentID to update marks: ");
        int studentID = scanner.nextInt();
        System.out.print("Enter new Marks: ");
        double marks = scanner.nextDouble();

        controller.updateStudent(studentID, marks);
    }

    private void deleteStudent() {
        System.out.print("Enter StudentID to delete: ");
        int studentID = scanner.nextInt();

        controller.deleteStudent(studentID);
    }
}

StudentApp.java
    public class StudentApp {
    public static void main(String[] args) {
        StudentController controller = new StudentController();
        StudentView view = new StudentView(controller);
        view.displayMenu();
    }
}
