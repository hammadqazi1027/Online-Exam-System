/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlineExamSystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AddMarks extends JFrame {

    private JTextField registrationNumberField;
    private JTextArea resultArea;
    private JTextField marksField;
    private JButton loadDataButton, submitMarksButton;
    JScrollPane js;

    public AddMarks() {
        setTitle("View and Add Marks");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame
        setLayout(null);

        // Registration Number Label and Field
        JLabel registrationNumberLabel = new JLabel("Registration Number:");
        registrationNumberLabel.setBounds(30, 30, 150, 30);
        add(registrationNumberLabel);

        registrationNumberField = new JTextField();
        registrationNumberField.setBounds(180, 30, 200, 30);
        add(registrationNumberField);

        // Load Data Button
        loadDataButton = new JButton("Load Data");
        loadDataButton.setBounds(400, 30, 100, 30);
        styleButton(loadDataButton);
        add(loadDataButton);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        js = new JScrollPane(resultArea);
        js.setBounds(30, 80, 540, 250);
        add(js);

        // Marks Label and Text Field
        JLabel marksLabel = new JLabel("Enter Marks (Out of 50):");
        marksLabel.setBounds(30, 350, 200, 30);
        add(marksLabel);

        marksField = new JTextField();
        marksField.setBounds(180, 350, 100, 30);
        add(marksField);

        // Submit Marks Button
        submitMarksButton = new JButton("Submit Marks");
        submitMarksButton.setBounds(250, 350, 150, 30);  // Centered
        styleButton(submitMarksButton);
        add(submitMarksButton);

        // Action Listener for Load Data Button
        loadDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String registrationNumber = registrationNumberField.getText().trim();
                if (registrationNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(AddMarks.this, "Please enter a registration number.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                loadStudentData(registrationNumber);
            }
        });

        // Action Listener for Submit Marks Button
        submitMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String registrationNumber = registrationNumberField.getText().trim();
                String marksText = marksField.getText().trim();

                if (registrationNumber.isEmpty() || marksText.isEmpty()) {
                    JOptionPane.showMessageDialog(AddMarks.this, "Please complete all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int marks = Integer.parseInt(marksText);
                    if (marks < 0 || marks > 50) {
                        JOptionPane.showMessageDialog(AddMarks.this, "Marks should be between 0 and 50.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    saveMarks(registrationNumber, marks);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AddMarks.this, "Invalid marks value.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    // Method to load MCQ marks, questions, and answers for the student
    private void loadStudentData(String registrationNumber) {
        Connection connection = ConnectionProvider.getCon();

        if (connection != null) {
            try {
                // Load MCQ Marks
                String marksQuery = "SELECT marks FROM std_login WHERE regno = ?";
                PreparedStatement marksStmt = connection.prepareStatement(marksQuery);
                marksStmt.setString(1, registrationNumber);
                ResultSet marksRs = marksStmt.executeQuery();

                StringBuilder resultBuilder = new StringBuilder();
                if (marksRs.next()) {
                    int marks = marksRs.getInt("marks");
                    resultBuilder.append("MCQ Marks: ").append(marks).append("\n\n");
                } else {
                    resultBuilder.append("No MCQ marks found for Registration Number: ").append(registrationNumber).append("\n\n");
                }

                // Load Questions and Answers
                String qaQuery = """
                    SELECT q.question, sa.answer 
                    FROM shortquestion q
                    JOIN submitanswers sa ON q.question_id = sa.question_id
                    WHERE sa.roll_no = ?
                """;
                PreparedStatement qaStmt = connection.prepareStatement(qaQuery);
                qaStmt.setString(1, registrationNumber);
                ResultSet qaRs = qaStmt.executeQuery();

                resultBuilder.append("Questions and Answers:\n");
                while (qaRs.next()) {
                    String question = qaRs.getString("question");
                    String answer = qaRs.getString("answer");

                    resultBuilder.append("Q: ").append(question).append("\n");
                    resultBuilder.append("A: ").append(answer).append("\n\n");
                }
                resultArea.setText(resultBuilder.toString());

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection is not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to save marks to the database
    private void saveMarks(String registrationNumber, int marks) {
        Connection connection = ConnectionProvider.getCon();

        if (connection != null) {
            try {
                String updateQuery = "UPDATE std_login SET marks = ? WHERE regno = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, marks);
                updateStmt.setString(2, registrationNumber);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Marks updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update marks. Check registration number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saving marks: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection is not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to style the buttons
    private void styleButton(JButton button) {
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);  // Remove border when clicked
        button.setPreferredSize(new Dimension(150, 30));  // Set preferred size
    }
}