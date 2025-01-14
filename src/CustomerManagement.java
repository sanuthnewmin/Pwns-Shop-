import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerManagement {

    private JFrame frame;
    private JTextField customerNameField, customerContactField, customerEmailField, customerAddressField;
    private JTable customerTable;
    private DefaultTableModel customerTableModel;
    private Connection connection;

    public CustomerManagement() {

        frame = new JFrame("Customer Management");
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);



        JPanel customerInputPanel = new JPanel(new GridLayout(4, 2));
        customerNameField = new JTextField();
        customerContactField = new JTextField();
        customerEmailField = new JTextField();
        customerAddressField = new JTextField();

        customerInputPanel.add(new JLabel("Name:"));
        customerInputPanel.add(customerNameField);
        customerInputPanel.add(new JLabel("Contact:"));
        customerInputPanel.add(customerContactField);
        customerInputPanel.add(new JLabel("Email:"));
        customerInputPanel.add(customerEmailField);
        customerInputPanel.add(new JLabel("Address:"));
        customerInputPanel.add(customerAddressField);

        frame.add(customerInputPanel, BorderLayout.NORTH);

        // Create customer table for displaying customer data
        customerTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Contact", "Email", "Address"}, 0);
        customerTable = new JTable(customerTableModel);
        customerTable.setBackground(Color.decode("#ffcc33"));
        frame.add(new JScrollPane(customerTable), BorderLayout.CENTER);

        // Add action buttons (Add, Update, Delete)
        JPanel customerButtons = new JPanel();
        JButton addCustomerButton = new JButton("Add Customer");
        customerButtons.setBackground(Color.decode("#ffcc33"));
        JButton updateCustomerButton = new JButton("Update Customer");
        JButton deleteCustomerButton = new JButton("Delete Customer");


        customerButtons.add(addCustomerButton);
        customerButtons.add(updateCustomerButton);
        customerButtons.add(deleteCustomerButton);

        frame.add(customerButtons, BorderLayout.SOUTH);


        addCustomerButton.addActionListener(e -> addCustomer());
        updateCustomerButton.addActionListener(e -> updateCustomer());
        deleteCustomerButton.addActionListener(e -> deleteCustomer());


        connectToDatabase();


        loadCustomerData();

        frame.setVisible(true);
    }

    public static JFrame getFrame() {
        return getFrame();
    }



    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/pawsshop";
            String username = "root";
            String password = "20020216";

            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to connect to the database: " + e.getMessage());
        }
    }


    private void loadCustomerData() {
        try {
            String query = "SELECT * FROM customers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            customerTableModel.setRowCount(0); // Clear existing data

            while (resultSet.next()) {
                int id = resultSet.getInt("customer_id");
                String name = resultSet.getString("full_name");
                String contact = resultSet.getString("contact_number");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");

                customerTableModel.addRow(new Object[]{id, name, contact, email, address});
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading customer data: " + e.getMessage());
        }
    }
    
    


    private void addCustomer() {
        String name = customerNameField.getText();
        String contact = customerContactField.getText();
        String email = customerEmailField.getText();
        String address = customerAddressField.getText();

        if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return;
        }

        try {
            String query = "INSERT INTO customers (full_name, contact_number, email, address) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, contact);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, address);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Customer added successfully!");
                loadCustomerData();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error adding customer: " + e.getMessage());
        }

        customerNameField.setText("");
        customerContactField.setText("");
        customerEmailField.setText("");
        customerAddressField.setText("");
    }


    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) customerTable.getValueAt(selectedRow, 0);
            String name = customerNameField.getText();
            String contact = customerContactField.getText();
            String email = customerEmailField.getText();
            String address = customerAddressField.getText();

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                return;
            }

            try {
                String query = "UPDATE customers SET full_name = ?, contact_number = ?, email = ?, address = ? WHERE customer_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, contact);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, address);
                preparedStatement.setInt(5, customerId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Customer updated successfully!");
                    loadCustomerData();
                }

                preparedStatement.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error updating customer: " + e.getMessage());
            }

            customerNameField.setText("");
            customerContactField.setText("");
            customerEmailField.setText("");
            customerAddressField.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a customer to update.");
        }
    }

    // Method to delete a customer
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) customerTable.getValueAt(selectedRow, 0);

            try {
                String query = "DELETE FROM customers WHERE customer_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, customerId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Customer deleted successfully!");
                    loadCustomerData();
                }

                preparedStatement.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Error deleting customer: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a customer to delete.");
        }
    }

   


    public static void main(String[] args) {
        new CustomerManagement();
    }
}
