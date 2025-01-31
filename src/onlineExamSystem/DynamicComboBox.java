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

public class DynamicComboBox {
    private JComboBox<String> jComboBox1;
    private JComboBox<String> jComboBox2;
    private JButton startExamButton;
    private JButton backButton;

    public DynamicComboBox(String regno) {
        JFrame frame = new JFrame("Teacher-Subject Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new GridBagLayout());

        JLabel teacherLabel = new JLabel("Select Teacher:");
        JLabel subjectLabel = new JLabel("Assigned Subjects:");
        jComboBox1 = new JComboBox<>();
        jComboBox2 = new JComboBox<>();
        startExamButton = new JButton("Start Exam");
        backButton = new JButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(teacherLabel, gbc);

        gbc.gridx = 1;
        frame.add(jComboBox1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(subjectLabel, gbc);

        gbc.gridx = 1;
        frame.add(jComboBox2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(startExamButton, gbc);

        gbc.gridy = 3;
        frame.add(backButton, gbc);

        populateTeacherComboBox();

        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedTeacher = (String) jComboBox1.getSelectedItem();
                if (selectedTeacher != null) {
                    populateSubjectComboBox(selectedTeacher);
                }
            }
        });

        startExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String teacher = (String) jComboBox1.getSelectedItem();
                String subject = (String) jComboBox2.getSelectedItem();

                if (teacher == null || subject == null) {
                    JOptionPane.showMessageDialog(frame, "Please select both a teacher and a subject.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    new instructions(teacher, subject, regno).setVisible(true);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new option().setVisible(true);
            }
        });

        // Display the frame
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    private void populateTeacherComboBox() {
        try {
            Connection con = ConnectionProvider.getCon();
            String query = "SELECT DISTINCT username FROM assignSubject";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String teacherName = resultSet.getString("username");
                jComboBox1.addItem(teacherName);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching teachers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateSubjectComboBox(String teacher) {
        jComboBox2.removeAllItems(); // Clear previous subjects
        try {
            Connection con = ConnectionProvider.getCon();
            String query = "SELECT sub_name FROM assignSubject WHERE username = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, teacher);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String subjectName = resultSet.getString("sub_name");
                jComboBox2.addItem(subjectName);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching subjects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void setVisible(boolean b) {
    }
}




