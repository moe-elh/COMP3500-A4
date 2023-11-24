import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.time.LocalDate;
import javax.naming.spi.DirStateFactory.Result;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;

public class PostgreSQLJDBCConnection {

    // JDBC & Database credentials
    private static final String url = "jdbc:postgresql://localhost:5432/A4";
    private static final String user = "postgres";
    private static final String password = "postgres";

    //returns all students from database
    public static void getAllStudents() {
        try { // connect to database
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {

                Statement stmt = conn.createStatement();
                String SQL = "SELECT * FROM students";
                ResultSet rs = stmt.executeQuery(SQL);

                while (rs.next()) {
                    System.out.printf("Student id: %-3d Name: %-11s Email: %-23s Date Enrolled: %s%n",
                            rs.getInt("student_id"),
                            rs.getString("first_name") + " " + rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getDate("enrollment_date"));
                }
                stmt.close();
                conn.close();
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //adds new student to table given first name, last name, email and enrollment date
    public static void addStudent(String first_name, String last_name, String email, java.sql.Date enrollment_date) {
        try { // connect to database
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {

                String SQL = "SELECT * FROM students WHERE email = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
                    pstmt.setString(1, email);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        System.out.println("email " + rs.getString("email")
                                + " already exists in database please try again using a different email");
                    } else {
                        String insertSQL = "INSERT INTO Students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement pstmt2 = conn.prepareStatement(insertSQL)) {
                            pstmt2.setString(1, first_name);
                            pstmt2.setString(2, last_name);
                            pstmt2.setString(3, email);
                            pstmt2.setDate(4, enrollment_date);
                            pstmt2.executeUpdate();
                            System.out.println("Student added successfully");
                        }
                    }
                }
            } else {
                System.out.println("Failed to establish connection.");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //updates student's email in table given their student id
    public static void updateStudentEmail(int student_id, String email) {
        try { // Connect to database
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                String insertSQL = "UPDATE Students SET email = ? where student_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setString(1, email);
                    pstmt.setInt(2, student_id);
                    if (pstmt.executeUpdate() == 0) {
                        System.out.println("student id does not exist try again");
                    } else {
                        System.out.println("Email successfully updated.");
                    }
                }
            } else {
                System.out.println("Failed to establish connection.");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //deletes student from table given their student id
    public static void deleteStudent(int student_id) {

        try { // Connect to database
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                String insertSQL = "DELETE FROM Students where student_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                    pstmt.setInt(1, student_id);
                    if (pstmt.executeUpdate() == 0) {
                        System.out.println("student id does not exist try again");
                    } else {
                        System.out.println("Sucessfully deleted student.");
                    }
                }
            } else {
                System.out.println("Failed to establish connection.");
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Scanner read = new Scanner(System.in);

        int n = 0;
        while (n >= 0) {
            System.out.print(
                    "Select from options below: \n (1) Display Students \n (2) Add Student \n (3) Update Student Information \n (4) Delete a student \n (-1) Exit \nChoice: ");
            n = read.nextInt();
            switch (n) {
                case -1:
                    break;

                case 1:
                    read.nextLine();
                    getAllStudents();
                    break;

                case 2:
                    String first_name = "";
                    String last_name = "";

                    read.nextLine();
                    boolean lettersOnly = false;
                    // checks if name input is numerical
                    while (!lettersOnly) {
                        try {
                            System.out.print("Enter students first name: ");
                            first_name = read.nextLine();
                            Integer.parseInt(first_name);

                            System.out.println("Name not valid try again with non-numerical input");
                        } catch (NumberFormatException e) {
                            lettersOnly = true;
                        }
                    }

                    lettersOnly = false;
                    while (!lettersOnly) {
                        // checks if name input is numerical

                        try {
                            System.out.print("Enter students last name: ");
                            last_name = read.nextLine();
                            Integer.parseInt(last_name);
                            System.out.println("Name not valid try again with non-numerical input");
                        } catch (NumberFormatException e) {
                            lettersOnly = true;
                        }
                    }

                    System.out.print("Enter students email: ");
                    String email = read.nextLine();

                    // checks if user input is email
                    while ((!email.contains("@")) || !email.contains(".")) {
                        System.out.print("please enter a valid email:");
                        email = read.nextLine();
                    }
                    try {// converts input date string to date object
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        dateFormat.setLenient(false); // doesnt allow user to enter date not in format above
                        System.out.print("Enter students enrollement date (yyyy/mm/dd): ");
                        String date = read.nextLine();
                        java.util.Date enrolled = dateFormat.parse(date);
                        addStudent(first_name, last_name, email, new java.sql.Date(enrolled.getTime()));
                    } catch (ParseException e) {
                        System.out.println("Date in wrong format");
                    }
                    break;

                case 3:
                    read.nextLine();
                    System.out.println("Enter student id and new email below to change students existing email");
                    boolean isNum = false;

                    while (!isNum) {// checks if student id user input is a number

                        System.out.print("Student id: ");
                        try { // catches errors coverting number input string to int
                            int id = Integer.parseInt(read.nextLine());
                            isNum = true;
                            System.out.print("New Email: ");
                            String email2 = read.nextLine();
                            // checks if user input is an email
                            while ((!email2.contains("@")) || !email2.contains(".")) {
                                System.out.print("please enter a valid email:");
                                email2 = read.nextLine();
                            }
                            updateStudentEmail(id, email2);
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a student id (number)");
                        }
                    }
                    break;

                case 4:
                    read.nextLine();
                    boolean isNum2 = false;

                    while (!isNum2) {// checks if student id user input is a number

                        System.out.println("Enter student id of student you would like to delete:");

                        try {
                            deleteStudent(Integer.parseInt(read.nextLine()));
                            isNum2 = true;
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a student id (number)");

                        }
                    }
                default:
                    System.out.println("Please pick a valid option");

            }
        }
        read.close();
    }
}
