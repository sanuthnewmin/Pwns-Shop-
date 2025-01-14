import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SupplierManagement {
    private static JFrame frame;
    private JTextField supplierNameField, contactPersonField, contactNumberField, supplierEmailField, supplierAddressField;
    private JComboBox<String> categoryDropdown;
    private JTable supplierTable;
    private DefaultTableModel supplierTableModel;
    private Connection connection;

    private final String DB_URL = "jdbc:mysql://localhost:3306/pawsshop";
    private final String USER = "root";
    private final String PASS = "20020216";

    public SupplierManagement() {
        frame = new JFrame("Supplier Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        connectToDatabase();

        // Set global frame background color
        frame.getContentPane().setBackground(Color.decode("#cccccc"));

        // Input Panel
        JPanel supplierInputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        supplierInputPanel.setBackground(Color.decode("#cccccc"));
        supplierNameField = createStyledTextField();
        contactPersonField = createStyledTextField();
        contactNumberField = createStyledTextField();
        supplierEmailField = createStyledTextField();
        supplierAddressField = createStyledTextField();

        categoryDropdown = new JComboBox<>();
        loadCategories();
        categoryDropdown.setBackground(Color.WHITE);
        categoryDropdown.setFont(new Font("Arial", Font.PLAIN, 14));

        supplierInputPanel.add(createStyledLabel("Supplier Name:"));
        supplierInputPanel.add(supplierNameField);
        supplierInputPanel.add(createStyledLabel("Contact Person:"));
        supplierInputPanel.add(contactPersonField);
        supplierInputPanel.add(createStyledLabel("Contact Number:"));
        supplierInputPanel.add(contactNumberField);
        supplierInputPanel.add(createStyledLabel("Email:"));
        supplierInputPanel.add(supplierEmailField);
        supplierInputPanel.add(createStyledLabel("Address:"));
        supplierInputPanel.add(supplierAddressField);
        supplierInputPanel.add(createStyledLabel("Category:"));
        supplierInputPanel.add(categoryDropdown);

        frame.add(supplierInputPanel, BorderLayout.NORTH);

        // Table design
        supplierTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Contact Person", "Contact Number", "Email", "Address", "Category"}, 0);
        supplierTable = new JTable(supplierTableModel);
        supplierTable.getTableHeader().setBackground(Color.WHITE);
        supplierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        supplierTable.setBackground(Color.decode("#ffcc33"));
        supplierTable.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(supplierTable), BorderLayout.CENTER);

        // Buttons
        JPanel supplierButtons = new JPanel();
        supplierButtons.setBackground(Color.decode("#ffcc33"));
        JButton addSupplierButton = createStyledButton("Add Supplier");
        JButton updateSupplierButton = createStyledButton("Update Supplier");
        JButton deleteSupplierButton = createStyledButton("Delete Supplier");

        supplierButtons.add(addSupplierButton);
        supplierButtons.add(updateSupplierButton);
        supplierButtons.add(deleteSupplierButton);

        frame.add(supplierButtons, BorderLayout.SOUTH);

        // Button Actions
        addSupplierButton.addActionListener(e -> addSupplier());
        updateSupplierButton.addActionListener(e -> updateSupplier());
        deleteSupplierButton.addActionListener(e -> deleteSupplier());

        loadSupplierData();
        frame.setVisible(true);
    }

    public static void createUI() {
    }

    public static JFrame getFrame() {
        return frame;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.WHITE);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error connecting to the database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCategories() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT category FROM products")) {

            categoryDropdown.removeAllItems();
            while (rs.next()) {
                categoryDropdown.addItem(rs.getString("category"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading categories: " + e.getMessage(), "Category Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSupplier() {
        String name = supplierNameField.getText();
        String contactPerson = contactPersonField.getText();
        String contactNumber = contactNumberField.getText();
        String email = supplierEmailField.getText();
        String address = supplierAddressField.getText();
        String category = (String) categoryDropdown.getSelectedItem();

        if (name.isEmpty() || contactPerson.isEmpty() || contactNumber.isEmpty() || email.isEmpty() || address.isEmpty() || category == null) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO suppliers (supplier_name, contact_person, contact_number, email, address, category) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, contactPerson);
            stmt.setString(3, contactNumber);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, category);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Supplier added successfully!");
            loadSupplierData();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error adding supplier: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSupplierData() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM suppliers")) {

            supplierTableModel.setRowCount(0);
            while (rs.next()) {
                supplierTableModel.addRow(new Object[]{
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("contact_person"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("category")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading supplier data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a supplier to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (int) supplierTableModel.getValueAt(selectedRow, 0);
        String name = supplierNameField.getText();
        String contactPerson = contactPersonField.getText();
        String contactNumber = contactNumberField.getText();
        String email = supplierEmailField.getText();
        String address = supplierAddressField.getText();
        String category = (String) categoryDropdown.getSelectedItem();

        if (name.isEmpty() || contactPerson.isEmpty() || contactNumber.isEmpty() || email.isEmpty() || address.isEmpty() || category == null) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "UPDATE suppliers SET supplier_name = ?, contact_person = ?, contact_number = ?, email = ?, address = ?, category = ? WHERE supplier_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, contactPerson);
            stmt.setString(3, contactNumber);
            stmt.setString(4, email);
            stmt.setString(5, address);
            stmt.setString(6, category);
            stmt.setInt(7, supplierId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Supplier updated successfully!");
            loadSupplierData();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error updating supplier: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a supplier to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (int) supplierTableModel.getValueAt(selectedRow, 0);
        String query = "DELETE FROM suppliers WHERE supplier_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Supplier deleted successfully!");
            loadSupplierData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting supplier: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        supplierNameField.setText("");
        contactPersonField.setText("");
        contactNumberField.setText("");
        supplierEmailField.setText("");
        supplierAddressField.setText("");
        categoryDropdown.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        new SupplierManagement();
    }
}
