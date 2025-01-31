/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package onlineExamSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Qazi Hammad
 */
public class Teacherlogin extends javax.swing.JFrame {
private int teacherId;
    /**
     * Creates new form login
     */
    public Teacherlogin() {
        initComponents();

    }
    public int getUserId() {
    return teacherId;
}

public void setUserId(int teacherId) {
    this.teacherId = teacherId;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        tlogin = new javax.swing.JButton();
        jexit = new javax.swing.JButton();
        tpassword = new javax.swing.JPasswordField();
        jshowpass = new javax.swing.JCheckBox();
        jreset = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Teacher login");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, 219, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Username");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 260, 72, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("password");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 300, -1, -1));

        username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameActionPerformed(evt);
            }
        });
        getContentPane().add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 260, 207, -1));

        tlogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tlogin.setText("Login");
        tlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tloginActionPerformed(evt);
            }
        });
        getContentPane().add(tlogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 450, -1, -1));

        jexit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jexit.setText("Back");
        jexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jexitActionPerformed(evt);
            }
        });
        getContentPane().add(jexit, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 450, -1, -1));
        getContentPane().add(tpassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 300, 207, -1));

        jshowpass.setText("Show password");
        jshowpass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jshowpassActionPerformed(evt);
            }
        });
        getContentPane().add(jshowpass, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 330, -1, -1));

        jreset.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jreset.setText("Reset");
        jreset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jresetActionPerformed(evt);
            }
        });
        getContentPane().add(jreset, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 450, -1, -1));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/onlineExamSystem/login Background_1.PNG"))); // NOI18N
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameActionPerformed

    private void tloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tloginActionPerformed
        // TODO add your handling code here:
        
        try 
        {
            String user = username.getText();
            String password = tpassword.getText();
            Connection con = ConnectionProvider.getCon();
            String query = "Select * from padmin where username = '"+user+"' and pass = '"+password+"'";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);
          
            if(rs.next())
            {
               JOptionPane.showMessageDialog(this, "Login Successfull");
               
               dispose();
                 teacherId = rs.getInt("teacher_id");
                 
               new TeacherDashboard(teacherId).setVisible(true);
               
            }
            else if(username.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please fill out Reg No");
            }
            else if(tpassword.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Please fill out password");
            }
            else
            {
              JOptionPane.showMessageDialog(null, "Invalid username and password!!","Message",JOptionPane.ERROR_MESSAGE);
            }
           
            con.close();
            
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        
        
        
        
    }//GEN-LAST:event_tloginActionPerformed

    private void jshowpassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jshowpassActionPerformed
        if(jshowpass.isSelected())
        {
            tpassword.setEchoChar((char)0);
        }
        else
        {
            tpassword.setEchoChar('*');
        }
    }//GEN-LAST:event_jshowpassActionPerformed

    private void jexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jexitActionPerformed
        dispose();
        new home().setVisible(true);
    }//GEN-LAST:event_jexitActionPerformed

    private void jresetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jresetActionPerformed
        // TODO add your handling code here:
        username.setText("");
        tpassword.setText("");
    }//GEN-LAST:event_jresetActionPerformed

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
            java.util.logging.Logger.getLogger(Teacherlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Teacherlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Teacherlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Teacherlogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Teacherlogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton jexit;
    private javax.swing.JButton jreset;
    private javax.swing.JCheckBox jshowpass;
    private javax.swing.JButton tlogin;
    private javax.swing.JPasswordField tpassword;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}

