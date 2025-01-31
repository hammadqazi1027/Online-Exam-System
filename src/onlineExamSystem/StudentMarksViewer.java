/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlineExamSystem;

/**
 *
 * @author Qazi Hammad
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class StudentMarksViewer {
    private JFrame frame;
    private JTextField regnoField;
    private JTextArea resultArea;

    public StudentMarksViewer() {
        frame = new JFrame("Student Marks Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel regnoLabel = new JLabel("Enter Registration Number:");
        regnoField = new JTextField(20);
        JButton fetchButton = new JButton("Fetch Marks");

        inputPanel.add(regnoLabel);
        inputPanel.add(regnoField);
        inputPanel.add(fetchButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.add(scrollPane, BorderLayout.CENTER);

        // Action Listener for Fetch Button
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regno = regnoField.getText().trim();
                if (regno.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Registration number cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    fetchStudentMarks(regno);
                }
            }
        });

        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    private void fetchStudentMarks(String regno) {
        resultArea.setText(""); // Clear previous results

        String query = "SELECT sub_name, marks FROM marks WHERE regno = ?";
        try (Connection con = ConnectionProvider.getCon();
             PreparedStatement statement = con.prepareStatement(query)) {

            statement.setString(1, regno);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                resultArea.setText("No marks found for the given registration number.");
            } else {
                StringBuilder results = new StringBuilder("Subject-wise Marks:\n\n");
                while (resultSet.next()) {
                    String subject = resultSet.getString("sub_name");
                    int marks = resultSet.getInt("marks");
                    results.append("Subject: ").append(subject).append(" - Marks: ").append(marks).append("\n");
                }
                resultArea.setText(results.toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error fetching marks: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentMarksViewer::new);
    }

    void setVisible(boolean b) {
    }
}

