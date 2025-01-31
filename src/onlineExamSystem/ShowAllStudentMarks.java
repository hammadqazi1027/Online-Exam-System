/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package onlineExamSystem;

/**
 *
 * @author Qazi Hammad
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ShowAllStudentMarks extends JFrame {

    private JTable marksTable;
    private JButton loadMarksButton;

    public ShowAllStudentMarks() {
        setTitle("All Student Marks");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

    
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        loadMarksButton = new JButton("Load All Marks");
        loadMarksButton.setFont(new Font("Arial", Font.BOLD, 14));
        loadMarksButton.setBackground(new Color(0, 0, 0)); 
        loadMarksButton.setForeground(Color.WHITE);
        loadMarksButton.setFocusPainted(false);
        loadMarksButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        topPanel.add(loadMarksButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

  
        marksTable = new JTable();
        marksTable.setFont(new Font("Arial", Font.PLAIN, 14));
        marksTable.setRowHeight(25);
        marksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        marksTable.getTableHeader().setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(marksTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Marks"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAllStudentMarks();
            }
        });

        setVisible(true);
    }

    private void loadAllStudentMarks() {
        Connection connection = ConnectionProvider.getCon();

        if (connection != null) {
            String query = "SELECT regno, teacher_name, sub_name, marks FROM marks";

            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                DefaultTableModel tableModel = new DefaultTableModel(
                        new Object[]{"Registration Number", "Teacher Name", "Subject", "Marks"}, 0);

                while (rs.next()) {
                    String regno = rs.getString("regno");
                    String teacherName = rs.getString("teacher_name");
                    String subjectName = rs.getString("sub_name");
                    int marks = rs.getInt("marks");

                    tableModel.addRow(new Object[]{regno, teacherName, subjectName, marks});
                }

                marksTable.setModel(tableModel);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading marks: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection is not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
