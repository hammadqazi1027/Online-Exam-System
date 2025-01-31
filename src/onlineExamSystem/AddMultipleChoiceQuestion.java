package onlineExamSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class AddMultipleChoiceQuestion extends JFrame {
    private String subject;
    private JComboBox<String> subjectDropdown;
    private JTextArea questionText;
    private JTextField option1Field, option2Field, option3Field, option4Field, answerField;
    private JButton submitButton, backButton;
    private int teacherId;

    public AddMultipleChoiceQuestion(int teacherId) {
        this.teacherId = teacherId;
        setTitle("Add Multiple Choice Question");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Heading
        JLabel heading = new JLabel("Add Multiple Choice Questions");
        heading.setBounds(50, 10, 500, 30);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(heading);

        // Dropdown for assigned subjects
        JLabel subjectLabel = new JLabel("Select Subject:");
        subjectLabel.setBounds(50, 50, 200, 30);
        add(subjectLabel);

        subjectDropdown = new JComboBox<>();
        subjectDropdown.setBounds(200, 50, 300, 30);
        add(subjectDropdown);
        loadAssignedSubjects();

        // Question field
        JLabel questionLabel = new JLabel("Enter the Question:");
        questionLabel.setBounds(50, 100, 200, 30);
        add(questionLabel);

        questionText = new JTextArea();
        questionText.setBounds(50, 130, 500, 50);
        add(questionText);

        // Option fields
        JLabel option1Label = new JLabel("Option 1:");
        option1Label.setBounds(50, 190, 100, 30);
        add(option1Label);

        option1Field = new JTextField();
        option1Field.setBounds(150, 190, 400, 30);
        add(option1Field);

        JLabel option2Label = new JLabel("Option 2:");
        option2Label.setBounds(50, 230, 100, 30);
        add(option2Label);

        option2Field = new JTextField();
        option2Field.setBounds(150, 230, 400, 30);
        add(option2Field);

        JLabel option3Label = new JLabel("Option 3:");
        option3Label.setBounds(50, 270, 100, 30);
        add(option3Label);

        option3Field = new JTextField();
        option3Field.setBounds(150, 270, 400, 30);
        add(option3Field);

        JLabel option4Label = new JLabel("Option 4:");
        option4Label.setBounds(50, 310, 100, 30);
        add(option4Label);

        option4Field = new JTextField();
        option4Field.setBounds(150, 310, 400, 30);
        add(option4Field);

        // Correct answer field
        JLabel answerLabel = new JLabel("Correct Answer:");
        answerLabel.setBounds(50, 350, 200, 30);
        add(answerLabel);

        answerField = new JTextField();
        answerField.setBounds(150, 350, 400, 30);
        add(answerField);

        // Submit button
        submitButton = new JButton("Submit Question");
        submitButton.setBounds(150, 400, 150, 40);
        add(submitButton);

        // Back button
        backButton = new JButton("Back");
        backButton.setBounds(320, 400, 150, 40);
        add(backButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitQuestion();
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

    private void loadAssignedSubjects() {
        try {
            Connection con = ConnectionProvider.getCon();
            String query = "SELECT assignSubjectId, sub_name FROM assignsubject WHERE teacher_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, teacherId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int assignSubjectId = rs.getInt("assignSubjectId");
                String subjectName = rs.getString("sub_name");
                subjectDropdown.addItem(assignSubjectId + " - " + subjectName);
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading subjects: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void submitQuestion() {
    String selectedSubject = (String) subjectDropdown.getSelectedItem();
    if (selectedSubject == null) {
        JOptionPane.showMessageDialog(this, "Please select a subject.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int assignSubjectId = Integer.parseInt(selectedSubject.split(" - ")[0]);
    String question = questionText.getText();
    String opt1 = option1Field.getText();
    String opt2 = option2Field.getText();
    String opt3 = option3Field.getText();
    String opt4 = option4Field.getText();
    String correctAnswer = answerField.getText();

    if (question.isEmpty() || opt1.isEmpty() || opt2.isEmpty() || opt3.isEmpty() || opt4.isEmpty() || correctAnswer.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        Connection con = ConnectionProvider.getCon();
        String query = "INSERT INTO examquestion (question, option1, option2, option3, option4, answer, assignSubjectId, teacher_Id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, question);
        pstmt.setString(2, opt1);
        pstmt.setString(3, opt2);
        pstmt.setString(4, opt3);
        pstmt.setString(5, opt4);
        pstmt.setString(6, correctAnswer);
        pstmt.setInt(7, assignSubjectId);
        pstmt.setInt(8, teacherId); // Include teacherId in the INSERT statement

        int rowsInserted = pstmt.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Question added successfully!");
            resetFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add question.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        pstmt.close();
        con.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void resetFields() {
        questionText.setText("");
        option1Field.setText("");
        option2Field.setText("");
        option3Field.setText("");
        option4Field.setText("");
        answerField.setText("");
    }
}
