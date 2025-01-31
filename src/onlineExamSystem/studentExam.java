/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package onlineExamSystem;
import java.util.List; // Correct import
import onlineExamSystem.ConnectionProvider;
import javax.swing.Timer;
import java.sql.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author Qazi Hammad
 */
public class studentExam extends javax.swing.JFrame {
    int assignSubjectId = 0;
    private String maxQuestion;
    public String RegNo, teacher, subject;
    public String questionId = null;
    public String answer;
    public int min = 0;
    public int sec = 0;
    public int marks = 0;
    private Timer time;
    private List<String> questionIds = new ArrayList<>();
    private int currentQuestionIndex = 0;
    /**
     * Creates new form studentExam
     */
    
  public void answerCheck() {
    String studentAnswer = "";

    if (jRadioButton1.isSelected()) {
        studentAnswer = jRadioButton1.getText();
    } else if (jRadioButton2.isSelected()) {
        studentAnswer = jRadioButton2.getText();
    } else if (jRadioButton3.isSelected()) {
        studentAnswer = jRadioButton3.getText();
    } else {
        studentAnswer = jRadioButton4.getText();
    }

    if (studentAnswer.equals(answer)) {
        marks++;
        
    }

    // Check if there are more questions
    if (currentQuestionIndex < questionIds.size() - 1) {
        currentQuestionIndex++;
        loadQuestion(questionIds.get(currentQuestionIndex)); 
    } else {
        stdnext.setVisible(false); 
    }
}
public void question(int assignSubjectId) {
    String query = "SELECT * FROM examquestion "
                 + "JOIN padmin ON examquestion.teacher_id = padmin.teacher_id "
                 + "WHERE padmin.username = ? AND examquestion.assignSubjectId = ?";

    try (Connection con = ConnectionProvider.getCon();
         PreparedStatement pstmt = con.prepareStatement(query)) {

        pstmt.setString(1, teacher.trim());
        pstmt.setInt(2,assignSubjectId);
        System.out.println("Teacher: " + teacher);
        System.out.println("Subject: " + subject);


        System.out.println("Executing query: " + pstmt.toString());  // Debugging log
        ResultSet rs = pstmt.executeQuery();

       if (rs.next()) {
    String questionId = rs.getString("questionid");
    String questionText = rs.getString("question");
    String option1 = rs.getString("option1");
    String option2 = rs.getString("option2");
    String option3 = rs.getString("option3");
    String option4 = rs.getString("option4");
    String correctAnswer = rs.getString("answer");


    System.out.println("Question ID: " + questionId);
    System.out.println("Question: " + questionText);
    System.out.println("Option 1: " + option1);
    System.out.println("Option 2: " + option2);
    System.out.println("Option 3: " + option3);
    System.out.println("Option 4: " + option4);
    System.out.println("Correct Answer: " + correctAnswer);
} else {
    System.out.println("No data found for the given teacher and subject.");
}


        rs.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        e.printStackTrace();  
    }
}

public void loadQuestion(String questionId) {
    String query = "SELECT * FROM examquestion WHERE questionid = ?";

    try (Connection con = ConnectionProvider.getCon();
         PreparedStatement pstmt = con.prepareStatement(query)) {

        pstmt.setString(1, questionId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String questionText = rs.getString("question");
            String option1 = rs.getString("option1");
            String option2 = rs.getString("option2");
            String option3 = rs.getString("option3");
            String option4 = rs.getString("option4");
            answer = rs.getString("answer");

            
            jLabel19.setText(questionText);
            jRadioButton1.setText(option1);
            jRadioButton2.setText(option2);
            jRadioButton3.setText(option3);
            jRadioButton4.setText(option4);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
    }
}
    
    public void submit() {
        try 
        {
            
            Connection con = ConnectionProvider.getCon();
            
            String sql = "insert into marks (regno,teacher_name,sub_name,marks) values(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, RegNo);
            pstmt.setString(2, teacher);
            pstmt.setString(3, subject);
            pstmt.setInt(4,marks );
//            pstmt.setString(3, "");
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data save successfully");
            con.close();
            dispose();
            new option(RegNo).setVisible(true);
            
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    
    
    public studentExam() {
        initComponents();
        
    }
    
    
    
public studentExam(String teacher, String subject, String regno) {
    initComponents();
    this.teacher = teacher;
    this.subject = subject;
    this.RegNo = regno;

    jLabel10.setText(regno);

    try {
        Connection con = ConnectionProvider.getCon();

        // Ensure that subject is being correctly fetched and assigned to assignSubjectId
        String getSubjectIdQuery = "SELECT assignSubjectId FROM assignsubject WHERE sub_name = ? AND teacher_id = (select teacher_id from padmin where username = '"+teacher+"')";
        PreparedStatement pstmtSubject = con.prepareStatement(getSubjectIdQuery);
        pstmtSubject.setString(1, subject);
        ResultSet rsSubject = pstmtSubject.executeQuery();

        if (rsSubject.next()) {
            assignSubjectId = rsSubject.getInt("assignSubjectId");
            System.out.println("AssignSubjectId : " + assignSubjectId);
        } else {
            System.out.println("No Data Found");
        }

        rsSubject.close();
        pstmtSubject.close();

        // Load all question IDs
        String query = "SELECT questionid FROM examquestion " +
                       "JOIN padmin ON examquestion.teacher_id = padmin.teacher_id " +
                       "WHERE padmin.username = ? AND examquestion.assignSubjectId = ?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setString(1, teacher);
        pstmt.setInt(2, assignSubjectId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            questionIds.add(rs.getString("questionid")); // Add question IDs to the list
        }

        if (!questionIds.isEmpty()) {
            loadQuestion(questionIds.get(currentQuestionIndex)); // Load the first question
        } else {
            JOptionPane.showMessageDialog(null, "No questions found for this subject.");
        }

        rs.close();
        pstmt.close();
        con.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }

    // Timer setup (unchanged)
    time = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jLabel8.setText(String.valueOf(sec));
            jLabel7.setText(String.valueOf(min));

            if (sec == 6) {
                sec = 0;
                min++;
                if (min == 1) {
                    time.stop();
                    dispose();
                    new option().setVisible(true);
                }
            }
            sec++;
        }
    });
    time.start();
}




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        stdnext = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 2, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/onlineExamSystem/index student.png"))); // NOI18N
        jLabel1.setText("Welcome");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 26, 192, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Total Time:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(864, 23, 103, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("10 Min");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1005, 23, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Time Taken:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(864, 55, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("00");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1005, 55, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("00");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1032, 55, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("RegNo");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("jLabel10");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("Question demo?");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, 149, -1));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jRadioButton1.setText("jRadioButton1");
        getContentPane().add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, 162, -1));

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jRadioButton2.setText("jRadioButton2");
        getContentPane().add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, -1, -1));

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jRadioButton3.setText("jRadioButton3");
        getContentPane().add(jRadioButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 340, 161, -1));

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jRadioButton4.setText("jRadioButton4");
        getContentPane().add(jRadioButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 420, 161, -1));

        stdnext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        stdnext.setText("Next");
        stdnext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stdnextActionPerformed(evt);
            }
        });
        getContentPane().add(stdnext, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 510, -1, -1));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 510, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("MCQS");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(487, 17, -1, -1));

        jSeparator1.setForeground(new java.awt.Color(255, 51, 51));
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 90, 20));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/onlineExamSystem/oop2-400x600.png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 260, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/onlineExamSystem/pages background student.jpg"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1130, 600));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void stdnextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stdnextActionPerformed
        // TODO add your handling code here:
        answerCheck();
//        question(assignSubjectId);
    }//GEN-LAST:event_stdnextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int a = JOptionPane.showConfirmDialog(null,"do you realy want to submit","select",JOptionPane.YES_NO_OPTION);
        if(a==0)
        {
            
            submit();
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(studentExam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(studentExam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(studentExam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(studentExam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new studentExam().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton stdnext;
    // End of variables declaration//GEN-END:variables
}
