import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ShowUsers {

    private static DefaultTableModel userTableModel;
    private static JTable userTable;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Manage Users");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setBackground(new Color(204, 204, 204));
            frame.setSize(1000, 600);


            frame.getContentPane().setBackground(Color.decode("#FFCC99"));

            userTableModel = new DefaultTableModel(new String[]{
                    "User ID", "Username", "Role", "Full Name", "Contact Number", "Email", "Created At"
            }, 0);

            userTable = new JTable(userTableModel);
            userTable.setFillsViewportHeight(true);

            JScrollPane scrollPane = new JScrollPane(userTable);

            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton refreshButton = new JButton("Refresh");
            JButton updateButton = new JButton("Update");
            JButton deleteButton = new JButton("Delete");

            refreshButton.setBackground(Color.decode("#FFCC33"));
            updateButton.setBackground(Color.decode("#FFCC33"));
            deleteButton.setBackground(Color.decode("#FFCC33"));

            refreshButton.addActionListener(e -> updateUserList());
            updateButton.addActionListener(e -> updateUser());
            deleteButton.addActionListener(e -> deleteUser());

            buttonPanel.add(refreshButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);

            frame.setLayout(new BorderLayout());
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            updateUserList();

            frame.setVisible(true);
        });
    }

    private static void updateUserList() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT user_id, username, role, full_name, contact_number, email, created_at FROM users";

            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                userTableModel.setRowCount(0);

                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String username = rs.getString("username");
                    String role = rs.getString("role");
                    String fullName = rs.getString("full_name");
                    String contactNumber = rs.getString("contact_number");
                    String email = rs.getString("email");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    userTableModel.addRow(new Object[]{userId, username, role, fullName, contactNumber, email, createdAt});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a user to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        String username = (String) userTableModel.getValueAt(selectedRow, 1);
        String role = (String) userTableModel.getValueAt(selectedRow, 2);
        String fullName = (String) userTableModel.getValueAt(selectedRow, 3);
        String contactNumber = (String) userTableModel.getValueAt(selectedRow, 4);
        String email = (String) userTableModel.getValueAt(selectedRow, 5);

        JTextField usernameField = new JTextField(username);
        JTextField roleField = new JTextField(role);
        JTextField fullNameField = new JTextField(fullName);
        JTextField contactNumberField = new JTextField(contactNumber);
        JTextField emailField = new JTextField(email);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Role:"));
        panel.add(roleField);
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Contact Number:"));
        panel.add(contactNumberField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "UPDATE users SET username = ?, role = ?, full_name = ?, contact_number = ?, email = ? WHERE user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, usernameField.getText());
                    pstmt.setString(2, roleField.getText());
                    pstmt.setString(3, fullNameField.getText());
                    pstmt.setString(4, contactNumberField.getText());
                    pstmt.setString(5, emailField.getText());
                    pstmt.setInt(6, userId);
                    pstmt.executeUpdate();
                    updateUserList();
                    JOptionPane.showMessageDialog(null, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private static void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM users WHERE user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, userId);
                    pstmt.executeUpdate();
                    updateUserList();
                    JOptionPane.showMessageDialog(null, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
