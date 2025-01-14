import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlaceOrderCashier {

    private static JFrame orderFrame;
    private static JTextField totalAmountField, quantityField;
    private static JComboBox<String> paymentStatusComboBox, productComboBox, customerComboBox;
    private static JTable orderTable;
    private static DefaultTableModel orderTableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createOrderUI();
        });
    }

    private static void createOrderUI() {
        orderFrame = new JFrame("Order Management");
        orderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderFrame.setSize(1000, 600);
        orderFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBackground(new Color(204, 204, 204));

        panel.add(new JLabel("Customer Name:"));
        customerComboBox = new JComboBox<>();
        loadCustomerNames(); // Load customer names
        panel.add(customerComboBox);

        panel.add(new JLabel("Product Name:"));
        productComboBox = new JComboBox<>();
        loadProductNames(); // Load product names
        panel.add(productComboBox);

        panel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        quantityField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                updateTotalAmount();
            }
        });
        panel.add(quantityField);

        panel.add(new JLabel("Total Amount:"));
        totalAmountField = new JTextField();
        totalAmountField.setEditable(false); // Make total amount non-editable
        panel.add(totalAmountField);

        panel.add(new JLabel("Payment Status:"));
        paymentStatusComboBox = new JComboBox<>(new String[]{"Pending", "Paid"});
        panel.add(paymentStatusComboBox);

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.setBackground(new Color(255, 204, 51));
        placeOrderButton.addActionListener(e -> placeOrder());
        panel.add(placeOrderButton);

        JButton refreshButton = new JButton("Refresh Order List");
        refreshButton.setBackground(new Color(255, 204, 51));
        refreshButton.addActionListener(e -> updateOrderList());
        panel.add(refreshButton);

        orderTableModel = new DefaultTableModel(new String[]{"Order ID", "Customer Name", "Product Name", "Quantity", "Total Amount", "Payment Status"}, 0);
        orderTable = new JTable(orderTableModel);
        JScrollPane tableScrollPane = new JScrollPane(orderTable);
        tableScrollPane.setPreferredSize(new Dimension(750, 200));

        orderFrame.setLayout(new BorderLayout());
        orderFrame.add(panel, BorderLayout.NORTH);
        orderFrame.add(tableScrollPane, BorderLayout.CENTER);
        orderFrame.setVisible(true);

        updateOrderList();
    }

    private static void loadCustomerNames() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT full_name FROM Customers";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    customerComboBox.addItem(rs.getString("full_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(orderFrame, "Error loading customer names.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void loadProductNames() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT product_name FROM Products";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    productComboBox.addItem(rs.getString("product_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(orderFrame, "Error loading product names.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateTotalAmount() {

        String productName = (String) productComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();


        if (productName == null || quantityStr.isEmpty()) {
            totalAmountField.setText("");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            totalAmountField.setText("");
            return;
        }


        double pricePerUnit = getProductPrice(productName);
        if (pricePerUnit == 0) {
            totalAmountField.setText("");
            return;
        }

        // Calculate and display the total amount
        double totalAmount = pricePerUnit * quantity;
        totalAmountField.setText(String.format("%.2f", totalAmount));
    }

    private static double getProductPrice(String productName) {
        double price = 0;
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT price FROM Products WHERE product_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, productName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        price = rs.getDouble("price");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return price;
    }

    private static void placeOrder() {
        String customerName = (String) customerComboBox.getSelectedItem();
        String productName = (String) productComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();
        String paymentStatus = (String) paymentStatusComboBox.getSelectedItem();

        if (customerName == null || productName == null || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(orderFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(orderFrame, "Quantity must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int customerId = getCustomerId(customerName);
        int productId = getProductId(productName);
        double pricePerUnit = getProductPrice(productName);

        if (customerId == 0 || productId == 0 || pricePerUnit == 0) {
            JOptionPane.showMessageDialog(orderFrame, "Invalid customer or product data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalAmount = pricePerUnit * quantity;
        LocalDate currentDate = LocalDate.now();
        String orderDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Insert into Orders table
            String insertOrderQuery = "INSERT INTO orders (customer_id, order_date, total_amount, payment_status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, customerId);
                orderStmt.setString(2, orderDate);
                orderStmt.setDouble(3, totalAmount);
                orderStmt.setString(4, paymentStatus);
                orderStmt.executeUpdate();

                ResultSet generatedKeys = orderStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);

                    // Insert into OrderDetails table
                    String insertOrderDetailsQuery = "INSERT INTO orderdetails (order_id, product_id, quantity, price_per_unit) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement detailsStmt = conn.prepareStatement(insertOrderDetailsQuery)) {
                        detailsStmt.setInt(1, orderId);
                        detailsStmt.setInt(2, productId);
                        detailsStmt.setInt(3, quantity);
                        detailsStmt.setDouble(4, pricePerUnit);
                        detailsStmt.executeUpdate();
                    }

                    // Insert into OrderSummary table
                    String insertOrderSummaryQuery = "INSERT INTO ordersummary (order_id, customer_name, product_name, quantity, total_amount, payment_status) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement summaryStmt = conn.prepareStatement(insertOrderSummaryQuery)) {
                        summaryStmt.setInt(1, orderId);
                        summaryStmt.setString(2, customerName);
                        summaryStmt.setString(3, productName);
                        summaryStmt.setInt(4, quantity);
                        summaryStmt.setDouble(5, totalAmount);
                        summaryStmt.setString(6, paymentStatus);
                        summaryStmt.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(orderFrame, "Order placed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearOrderFields();
                updateOrderList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(orderFrame, "Error placing order.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static int getCustomerId(String customerName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT customer_id FROM Customers WHERE full_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, customerName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("customer_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static int getProductId(String productName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT product_id FROM Products WHERE product_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, productName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("product_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static void updateOrderList() {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String query = "SELECT os.customer_name, os.product_name, os.order_id, os.quantity, os.total_amount, os.payment_status " +
                    "FROM ordersummary os";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                orderTableModel.setRowCount(0);
                while (rs.next()) {
                    String customerName = rs.getString("customer_name");
                    String productName = rs.getString("product_name");
                    int orderId = rs.getInt("order_id");
                    int quantity = rs.getInt("quantity");
                    double totalAmount = rs.getDouble("total_amount");
                    String paymentStatus = rs.getString("payment_status");


                    orderTableModel.addRow(new Object[]{customerName, productName, orderId, quantity, totalAmount, paymentStatus});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void clearOrderFields() {
        customerComboBox.setSelectedIndex(0);
        productComboBox.setSelectedIndex(0);
        quantityField.setText("");
        paymentStatusComboBox.setSelectedIndex(0);
    }

    public static JFrame getFrame() {
        return getFrame();
    }
}
