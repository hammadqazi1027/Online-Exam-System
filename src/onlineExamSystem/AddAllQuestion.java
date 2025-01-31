package onlineExamSystem;

import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class AddAllQuestion extends JFrame {

    private JTable questionsTable;
    private JScrollPane scrollPane;

    public AddAllQuestion() {
        this.setTitle("All Questions");
        this.setSize(1400, 720); 
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Question", "Option 1", "Option 2", "Option 3", "Option 4", "Answer", "Teacher ID", "Teacher Name"}, 0);
        this.questionsTable = new JTable(model);
        this.scrollPane = new JScrollPane(this.questionsTable);
        this.add(this.scrollPane);

     
        questionsTable.setFont(new Font("Arial", Font.PLAIN, 16)); 
        questionsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));


        questionsTable.setRowHeight(40);
        TableColumnModel columnModel = questionsTable.getColumnModel();
        for (int i = 0; i < questionsTable.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = table.getDefaultRenderer(table.getColumnClass(column)).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setFont(new Font("Arial", Font.PLAIN, 16)); 
                    return c;
                }
            });
        }

      
        this.fetchQuestionsFromDatabase(model);

        this.setVisible(true);
    }

    private void fetchQuestionsFromDatabase(DefaultTableModel model) {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectionProvider.getCon(); 
            if (con != null) {
                stmt = con.createStatement();

               
                String query = """
                    SELECT 
                        sq.questionid, 
                        sq.question, 
                        sq.option1,  
                        sq.option2,  
                        sq.option3,  
                        sq.option4,  
                        sq.answer,
                        t.teacher_id, 
                        t.username 
                    FROM 
                        examquestion sq
                    JOIN 
                        padmin t 
                    ON 
                        sq.teacher_id = t.teacher_id
                """;

                rs = stmt.executeQuery(query);

             
                while (rs.next()) {
                    int questionId = rs.getInt("questionid");
                    String question = rs.getString("question");
                    String option1 = rs.getString("option1");
                    String option2 = rs.getString("option2");
                    String option3 = rs.getString("option3");
                    String option4 = rs.getString("option4");
                    String answer = rs.getString("answer");
                    int teacherId = rs.getInt("teacher_id");
                    String teacherName = rs.getString("username");

                    model.addRow(new Object[]{questionId, question, option1, option2, option3, option4, answer, teacherId, teacherName});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
