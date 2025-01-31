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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class deleteExam extends JFrame {
    private JComboBox<String> subjectDropdown;
    private JButton deleteExamButton, backButton;
    private int teacherId;

    public deleteExam(int teacherId) {
        this.teacherId = teacherId;

        setTitle("Delete Exam");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel heading = new JLabel("Delete Exam");
        heading.setBounds(50, 10, 400, 30);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(heading);

        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setBounds(50, 60, 200, 30);
        add(subjectLabel);

        subjectDropdown = new JComboBox<>();
        subjectDropdown.setBounds(200, 60, 250, 30);
        add(subjectDropdown);
        loadSubjects();

        deleteExamButton = new JButton("Delete Exam");
        deleteExamButton.setBounds(50, 150, 150, 40);
        deleteExamButton.setBackground(Color.RED);
        deleteExamButton.setForeground(Color.WHITE);
        deleteExamButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(deleteExamButton);

        backButton = new JButton("Back");
        backButton.setBounds(250, 150, 150, 40);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(backButton);

        deleteExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteExam();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new TeacherDashboard(teacherId);
            }
        });

        setVisible(true);
    }

  private void loadSubjects() {
    try {
        Connection con = ConnectionProvider.getCon();
        
        // Correct SQL query to fetch subjects assigned to the specific teacher
        String getSubjectQuery = "SELECT a.assignSubjectId, a.sub_name " +
                                 "FROM assignSubject a " +
                                 "WHERE a.teacher_id = ?";
        
        PreparedStatement pstmt = con.prepareStatement(getSubjectQuery);
        pstmt.setInt(1, teacherId); // Set the teacher ID parameter

        ResultSet rs = pstmt.executeQuery();
        subjectDropdown.removeAllItems();
        while (rs.next()) {
            // Add subject name along with assignSubjectId for reference
            subjectDropdown.addItem(rs.getString("sub_name") + "|" + rs.getInt("assignSubjectId")); 
        }

        rs.close();
        pstmt.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void deleteExam() {
    String selectedItem = (String) subjectDropdown.getSelectedItem();
    if (selectedItem == null || selectedItem.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a subject to delete exams.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Split the selected item to extract subject name and assignSubjectId
    String[] selectedSubjectDetails = selectedItem.split("\\|");
    String selectedSubject = selectedSubjectDetails[0];
    int assignSubjectId = Integer.parseInt(selectedSubjectDetails[1]);

    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to delete the exam for the selected subject?", 
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        Connection con = ConnectionProvider.getCon();

        // Step 1: Check if questions exist in `examquestion` for the assignSubjectId and teacher
        String checkQuery = "SELECT COUNT(*) AS questionCount FROM examquestion WHERE assignSubjectId = ? AND teacher_id = ?";
        PreparedStatement checkStmt = con.prepareStatement(checkQuery);
        checkStmt.setInt(1, assignSubjectId);
        checkStmt.setInt(2, teacherId);

        ResultSet checkRs = checkStmt.executeQuery();
        if (checkRs.next() && checkRs.getInt("questionCount") > 0) {
            // Step 2: Delete questions
            String deleteQuery = "DELETE FROM examquestion WHERE assignSubjectId = ? AND teacher_id = ?";
            PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, assignSubjectId);
            deleteStmt.setInt(2, teacherId);

            int rowsAffected = deleteStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, rowsAffected + " questions deleted successfully.");
                loadSubjects(); // Refresh subject list if necessary
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete questions. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            deleteStmt.close();
        } else {
            JOptionPane.showMessageDialog(this, "No questions found for the selected subject.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        checkStmt.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error deleting exam: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}




}
