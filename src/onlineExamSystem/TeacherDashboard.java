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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TeacherDashboard extends JFrame {
    private int teacherId;

    public TeacherDashboard(int teacherId) {      
        this.teacherId = teacherId;
        System.out.println("Teacher Id : " + teacherId);
        setTitle("Teacher Frame");
        setSize(1400, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.white);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel heading = new JLabel("Teacher Dashboard");
        heading.setBounds(550, 20, 400, 30);
        heading.setFont(heading.getFont().deriveFont(20.0f));
        add(heading);
        
        try {
            Connection con = ConnectionProvider.getCon();
            String query = "SELECT username FROM padmin WHERE teacher_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
    String teacherName = rs.getString("username");
    JLabel teacherNameLabel = new JLabel("Logged In As : " + teacherName);
    
    teacherNameLabel.setBounds(10, 10, 250, 60);
    teacherNameLabel.setFont(new Font("Arial", Font.BOLD, 18));
    teacherNameLabel.setBackground(Color.BLACK);
    teacherNameLabel.setForeground(Color.WHITE);
    teacherNameLabel.setOpaque(true);
    
    // Set horizontal alignment to center the text
    teacherNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
    // Adding padding on left and right (adjust as needed)
    teacherNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); 

    add(teacherNameLabel);
} else {
    System.out.println("No Teacher Name");
}
            
            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e);
        }

        JButton button1 = new JButton("Add new Question");
        JButton button2 = new JButton("Add Marks ");
        JButton button3 = new JButton("All Questions");
        JButton button4 = new JButton("Delete and update Question");
        JButton button5 = new JButton("All Student Results");
        JButton button8 = new JButton("Delete Exam");
        JButton button6 = new JButton("Logout");
        JButton button7 = new JButton("Exit");

        JButton[] buttons = {button1, button2, button3, button4, button5, button6, button7, button8};
        for (JButton button : buttons) {
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.PLAIN, 16));
        }

        int buttonWidth = 200;
        int buttonHeight = 50;
        int xPos = (getWidth() - buttonWidth) / 2;

       button1.setBounds(xPos, 100, buttonWidth, buttonHeight);
button2.setBounds(xPos, 170, buttonWidth, buttonHeight);
button3.setBounds(xPos, 240, buttonWidth, buttonHeight);
button4.setBounds(xPos, 310, buttonWidth, buttonHeight);
button5.setBounds(xPos, 380, buttonWidth, buttonHeight);
button8.setBounds(xPos, 450, buttonWidth, buttonHeight); 
button6.setBounds(xPos, 520, buttonWidth, buttonHeight); 
button7.setBounds(xPos, 590, buttonWidth, buttonHeight);

        for (JButton button : buttons) {
            add(button);
        }

      button1.addActionListener((ActionEvent e) -> {
    dispose();
    JFrame questionOptionFrame = new JFrame("Question Choosing Frame");
    questionOptionFrame.setSize(1000, 720);
    questionOptionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    questionOptionFrame.setLocationRelativeTo(null);  // Center the frame
    questionOptionFrame.setLayout(null);

    JLabel infoLabel = new JLabel("Select a Subject to Add Questions:");
    infoLabel.setBounds(50, 20, 400, 30);
    questionOptionFrame.add(infoLabel);

    JComboBox<String> subjectDropdown = new JComboBox<>();
    subjectDropdown.setBounds(50, 70, 300, 30); // Position the dropdown
    questionOptionFrame.add(subjectDropdown);

    try {
        Connection con = ConnectionProvider.getCon();
        String query = "SELECT sub_name FROM assignsubject WHERE teacher_id = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, teacherId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String subjectName = rs.getString("sub_name");
            subjectDropdown.addItem(subjectName);
        }

        rs.close();
        pstmt.close();
        con.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error fetching subjects: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    int SetbuttonWidth = 300; // Same width for both buttons
    int SetbuttonHeight = 50; // Height for the buttons

    JButton multipleChoiceButton = new JButton("Multiple Choice Question");
    multipleChoiceButton.setBounds(350, 300, SetbuttonWidth, SetbuttonHeight);  // Center the button
    multipleChoiceButton.setBackground(Color.BLACK);
    multipleChoiceButton.setForeground(Color.WHITE);
    multipleChoiceButton.setFocusPainted(false);  // Remove border when clicked
    multipleChoiceButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedSubject = (String) subjectDropdown.getSelectedItem();
            if (selectedSubject == null) {
                JOptionPane.showMessageDialog(null, "Please select a subject first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            new AddMultipleChoiceQuestion(teacherId);
        }
    });

    JButton goBackButton = new JButton("Go Back");
    goBackButton.setBounds(350, 380, buttonWidth, buttonHeight); 
    goBackButton.setBackground(Color.BLACK);
    goBackButton.setForeground(Color.WHITE);
    goBackButton.setFocusPainted(false);  
    goBackButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            questionOptionFrame.dispose();
            new TeacherDashboard(teacherId);
        }
    });

    questionOptionFrame.add(subjectDropdown);
    questionOptionFrame.add(multipleChoiceButton);
    questionOptionFrame.add(goBackButton);

    questionOptionFrame.setVisible(true);
});


        button2.addActionListener((ActionEvent e) -> {
            new AddMarks().setVisible(true);
        });

        button3.addActionListener((ActionEvent e) -> {
            new AddAllQuestion().setVisible(true);
        });

        button4.addActionListener((ActionEvent e) -> {
            new deleteQuestion(teacherId).setVisible(true);
        });

        button5.addActionListener((ActionEvent e) -> {
            new ShowAllStudentMarks().setVisible(true);
        });

        button6.addActionListener((ActionEvent e) -> {
            int option = JOptionPane.showConfirmDialog(TeacherDashboard.this,
                    "Are you sure you want to logout?", "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (option == JOptionPane.YES_OPTION) {
                new home().setVisible(true);
            }
        });

        button8.addActionListener((ActionEvent e) -> {
            new deleteExam(teacherId).setVisible(true);
        });

        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(TeacherDashboard.this,
                        "Are you sure you want to exit?", "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }
}
