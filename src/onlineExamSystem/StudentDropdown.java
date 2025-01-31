/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlineExamSystem;

/**
 *
 * @author Qazi Hammad
 */
import java.sql.*;
import javax.swing.*;

public class StudentDropdown {

    public static void main(String[] args) {
        

        JFrame frame = new JFrame("Student Dropdown");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel label = new JLabel("Select a Student:");
        label.setBounds(50, 30, 150, 30);
        frame.add(label);

        JComboBox<String> studentDropdown = new JComboBox<>();
        studentDropdown.setBounds(50, 70, 200, 30);
        frame.add(studentDropdown);

        try {
            // Establish database connection
            Connection connection = ConnectionProvider.getCon();

            // SQL query to fetch student names
            String query = "SELECT regno FROM std_login";
            PreparedStatement statement = connection.prepareStatement(query);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Populate the dropdown list
            while (resultSet.next()) {
                String studentName = resultSet.getString("regno");
                studentDropdown.addItem(studentName);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching student data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        frame.setVisible(true);
    }
}

