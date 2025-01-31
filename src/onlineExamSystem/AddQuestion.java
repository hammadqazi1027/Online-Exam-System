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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddQuestion extends JFrame {

    public AddQuestion() {
        setTitle("Add New Question");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(null);

    
        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setBounds(20, 30, 100, 30);
        add(questionLabel);


        JTextArea questionField = new JTextArea();
        questionField.setBounds(100, 30, 250, 100);
        add(questionField);

       
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 150, 100, 30);
        add(submitButton);

    
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String question = questionField.getText().trim();
                if (question.isEmpty()) {
                    JOptionPane.showMessageDialog(AddQuestion.this, "Question cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    saveQuestionToDatabase(question);
                }
            }
        });

   
        setVisible(true);
    }

    private void saveQuestionToDatabase(String question) {
        Connection con = ConnectionProvider.getCon();
        if (con != null) {
            try {
                // SQL query to insert the question
                String sql = "INSERT INTO shortquestion (question) VALUES (?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, question);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Question added successfully!");
                }

                con.close();

              
                dispose();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saving question: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Database connection is not available.", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new AddQuestion().setVisible(true);
    }
}

