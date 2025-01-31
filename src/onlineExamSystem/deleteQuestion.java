package onlineExamSystem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class deleteQuestion extends JFrame {
    private JComboBox<String> questionDropdown;
    private JButton deleteButton, backButton, updateButton;
    private int teacherId;

    public deleteQuestion(int teacherId) {
        this.teacherId = teacherId;
        setTitle("Delete or Update Multiple Choice Question");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel heading = new JLabel("Delete or Update Multiple Choice Questions");
        heading.setBounds(50, 10, 500, 30);
        heading.setFont(new Font("Times New Roman", Font.BOLD, 18));
        add(heading);

        JLabel questionLabel = new JLabel("Select Question:");
        questionLabel.setBounds(50, 50, 200, 30);
        add(questionLabel);

        questionDropdown = new JComboBox<>();
        questionDropdown.setBounds(200, 50, 350, 30);
        add(questionDropdown);
        loadQuestions();

        deleteButton = new JButton("Delete Question");
        deleteButton.setBounds(50, 150, 150, 40);
        deleteButton.setBackground(new Color(0, 0, 0)); 
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setFocusPainted(false);
        add(deleteButton);

        updateButton = new JButton("Update Question");
        updateButton.setBounds(220, 150, 150, 40);
        updateButton.setBackground(new Color(0, 0, 0)); 
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setFocusPainted(false);
        add(updateButton);

        backButton = new JButton("Back");
        backButton.setBounds(390, 150, 150, 40);
        backButton.setBackground(new Color(0, 0, 0)); 
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        add(backButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteQuestion();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateQuestion();
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

    private void loadQuestions() {
        try {
            Connection con = ConnectionProvider.getCon();
            String query = "SELECT questionid, question FROM examquestion WHERE teacher_id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, teacherId);

            ResultSet rs = pstmt.executeQuery();
            questionDropdown.removeAllItems();
            while (rs.next()) {
                int questionId = rs.getInt("questionid");
                String questionText = rs.getString("question");
                questionDropdown.addItem(questionId + " - " + questionText);
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteQuestion() {
        String selectedQuestion = (String) questionDropdown.getSelectedItem();
        if (selectedQuestion == null) {
            JOptionPane.showMessageDialog(this, "Please select a question.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int questionId = Integer.parseInt(selectedQuestion.split(" - ")[0]);

        try {
            Connection con = ConnectionProvider.getCon();
            String deleteQuery = "DELETE FROM examquestion WHERE questionid = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteQuery);
            pstmt.setInt(1, questionId);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question deleted successfully.");
            loadQuestions();

            pstmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateQuestion() {
        String selectedQuestion = (String) questionDropdown.getSelectedItem();
        if (selectedQuestion == null) {
            JOptionPane.showMessageDialog(this, "Please select a question.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int questionId = Integer.parseInt(selectedQuestion.split(" - ")[0]);

        try {
            Connection con = ConnectionProvider.getCon();
            String query = "SELECT question, option1, option2, option3, option4, answer FROM examquestion WHERE questionid = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, questionId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String oldQuestionText = rs.getString("question");
                String oldOption1 = rs.getString("option1");
                String oldOption2 = rs.getString("option2");
                String oldOption3 = rs.getString("option3");
                String oldOption4 = rs.getString("option4");
                String oldAnswer = rs.getString("answer");

                new UpdateQuestionFrame(questionId, oldQuestionText, oldOption1, oldOption2, oldOption3, oldOption4, oldAnswer);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No question found to update.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class UpdateQuestionFrame extends JFrame {
        private JTextField questionField, option1Field, option2Field, option3Field, option4Field, answerField;
        private JButton saveButton;
        private int questionId;

        public UpdateQuestionFrame(int questionId, String oldQuestion, String oldOption1, String oldOption2, String oldOption3, String oldOption4, String oldAnswer) {
            this.questionId = questionId;

            setTitle("Update Question and Choices");
            setSize(800, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);

            // Main container panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(8, 2, 10, 10));

            // Initialize fields
            questionField = new JTextField(oldQuestion);
            option1Field = new JTextField(oldOption1);
            option2Field = new JTextField(oldOption2);
            option3Field = new JTextField(oldOption3);
            option4Field = new JTextField(oldOption4);
            answerField = new JTextField(oldAnswer);

            mainPanel.add(new JLabel("Question:"));
            mainPanel.add(questionField);
            mainPanel.add(new JLabel("Option 1:"));
            mainPanel.add(option1Field);
            mainPanel.add(new JLabel("Option 2:"));
            mainPanel.add(option2Field);
            mainPanel.add(new JLabel("Option 3:"));
            mainPanel.add(option3Field);
            mainPanel.add(new JLabel("Option 4:"));
            mainPanel.add(option4Field);
            mainPanel.add(new JLabel("Answer:"));
            mainPanel.add(answerField);

            saveButton = new JButton("Save Changes");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveChanges();
                }
            });

            add(mainPanel, BorderLayout.CENTER);
            add(saveButton, BorderLayout.SOUTH);

            setVisible(true);
        }

        private void saveChanges() {
            String updatedQuestion = questionField.getText();
            String updatedOption1 = option1Field.getText();
            String updatedOption2 = option2Field.getText();
            String updatedOption3 = option3Field.getText();
            String updatedOption4 = option4Field.getText();
            String updatedAnswer = answerField.getText();

            if (updatedQuestion.isEmpty() || updatedOption1.isEmpty() || updatedOption2.isEmpty() || updatedOption3.isEmpty() || updatedOption4.isEmpty() || updatedAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection con = ConnectionProvider.getCon();
                String query = "UPDATE examquestion SET question = ?, option1 = ?, option2 = ?, option3 = ?, option4 = ?, answer = ? WHERE questionid = ?";
                PreparedStatement pstmt = con.prepareStatement(query);
                pstmt.setString(1, updatedQuestion);
                pstmt.setString(2, updatedOption1);
                pstmt.setString(3, updatedOption2);
                pstmt.setString(4, updatedOption3);
                pstmt.setString(5, updatedOption4);
                pstmt.setString(6, updatedAnswer);
                pstmt.setInt(7, questionId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Question updated successfully.");
                dispose();

                pstmt.close();
                con.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}