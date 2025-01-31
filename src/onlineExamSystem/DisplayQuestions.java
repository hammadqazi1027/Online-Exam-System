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
import onlineExamSystem.ConnectionProvider;
import javax.swing.*;

public class DisplayQuestions {
    private JFrame frame;
    private JLabel questionLabel;
    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;
    private JButton nextButton;
    private ButtonGroup optionsGroup;
    private ResultSet questionSet;
    private Connection con;

    public DisplayQuestions(String teacher, String subject, String regno) {
        try {
            con = ConnectionProvider.getCon();
            // Debugging: Print teacher and subject to ensure correctness
            System.out.println("Selected Teacher: " + teacher);
            System.out.println("Selected Subject: " + subject);

            // Query to fetch questions
            String query = "SELECT * FROM examquestion WHERE teacher_id = (SELECT teacher_id FROM padmin WHERE username = ?) AND sub_id = (SELECT sub_id FROM Subject WHERE sub_name = ?)";
            PreparedStatement statement = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, teacher);
            statement.setString(2, subject);

            questionSet = statement.executeQuery();

            if (!questionSet.isBeforeFirst()) { // No rows in the ResultSet
                JOptionPane.showMessageDialog(null, "No questions available for the selected subject.", "Error", JOptionPane.ERROR_MESSAGE);
                con.close(); // Close the connection
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame = new JFrame("MCQ Exam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Question Label
        questionLabel = new JLabel("Question will appear here.");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(questionLabel, gbc);

        // Radio Buttons
        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();
        optionsGroup = new ButtonGroup();
        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);

        // Add options to the layout
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        frame.add(option1, gbc);
        gbc.gridy = 2;
        frame.add(option2, gbc);
        gbc.gridy = 3;
        frame.add(option3, gbc);
        gbc.gridy = 4;
        frame.add(option4, gbc);

        // Next Button
        nextButton = new JButton("Next");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.add(nextButton, gbc);

        // Load the first question
        loadQuestion();

        // Next Button Action
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAnswer();
                loadQuestion();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadQuestion() {
        try {
            if (!questionSet.next()) {
                JOptionPane.showMessageDialog(frame, "You have completed the exam!", "Exam Finished", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                con.close();
                return;
            }

            String question = questionSet.getString("question");
            String opt1 = questionSet.getString("option1");
            String opt2 = questionSet.getString("option2");
            String opt3 = questionSet.getString("option3");
            String opt4 = questionSet.getString("option4");

            questionLabel.setText("Q: " + question);
            option1.setText(opt1);
            option2.setText(opt2);
            option3.setText(opt3);
            option4.setText(opt4);

            optionsGroup.clearSelection(); // Clear previous selection
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading question: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveAnswer() {
        try {
            String selectedOption = null;
            if (option1.isSelected()) selectedOption = option1.getText();
            if (option2.isSelected()) selectedOption = option2.getText();
            if (option3.isSelected()) selectedOption = option3.getText();
            if (option4.isSelected()) selectedOption = option4.getText();

            if (selectedOption == null) {
                JOptionPane.showMessageDialog(frame, "Please select an answer before proceeding.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int questionId = questionSet.getInt("questionid");
            System.out.println("Question ID: " + questionId + ", Selected Answer: " + selectedOption);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error saving answer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DisplayQuestions("teacher1", "subject1", "regno123"));
    }

    void setVisible(boolean b) {
      }
}
