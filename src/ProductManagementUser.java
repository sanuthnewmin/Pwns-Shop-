import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProductManagementUser {

    private static JFrame frame;
    private static JTextField productNameField, priceField, stockQuantityField, searchField;
    private static JComboBox<String> categoryComboBox;
    private static JTable productTable;
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createUI();
        });
    }

    // Create the main product management UI
    private static void createUI() {
        frame = new JFrame("Product Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBackground(new Color(204, 204, 204));


        panel.add(new JLabel("Product Name:"));
        productNameField = new JTextField();
        panel.add(productNameField);

        panel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(new String[]{"Pet Toys", "Collars", "Cages", "Pet Wash", "Harnesses", "Grooming"});
        panel.add(categoryComboBox);

        panel.add(new JLabel("Price:"));
        priceField = new JTextField();
        panel.add(priceField);

        panel.add(new JLabel("Stock Quantity:"));
        stockQuantityField = new JTextField();
        panel.add(stockQuantityField);


        JButton addButton = new JButton("Add Product");
        addButton.setBackground(new Color(255, 204, 51));
        addButton.addActionListener(e -> addProduct());
        panel.add(addButton);

        panel.add(new JLabel("Search Product:"));
        searchField = new JTextField();
        panel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(255, 204, 51));
        searchButton.addActionListener(e -> searchProduct());
        panel.add(searchButton);


        tableModel = new DefaultTableModel(new String[]{"Product ID", "Name", "Category", "Price", "Stock"}, 0);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(650, 200));


        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.setVisible(true);


        updateProductList();
    }

    // Add a new product to the database
    private static void addProduct() {
        String name = productNameField.getText();
        String category = (String) categoryComboBox.getSelectedItem();
        String priceStr = priceField.getText();
        String stockQuantityStr = stockQuantityField.getText();

        if (name.isEmpty() || priceStr.isEmpty() || stockQuantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Products (product_name, category, price, stock_quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, category);
                stmt.setBigDecimal(3, new java.math.BigDecimal(priceStr));
                stmt.setInt(4, Integer.parseInt(stockQuantityStr));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Product added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                updateProductList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Search for a product in the database
    private static void searchProduct() {
        String name = searchField.getText();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a product name to search.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Products WHERE product_name LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + name + "%");
                ResultSet rs = stmt.executeQuery();
                StringBuilder productDetails = new StringBuilder();
                while (rs.next()) {
                    productDetails.append("Product ID: ").append(rs.getInt("product_id"))
                            .append(", Name: ").append(rs.getString("product_name"))
                            .append(", Category: ").append(rs.getString("category"))
                            .append(", Price: ").append(rs.getBigDecimal("price"))
                            .append(", Stock: ").append(rs.getInt("stock_quantity"))
                            .append("\n");
                }
                if (productDetails.length() > 0) {
                    JOptionPane.showMessageDialog(frame, productDetails.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "No products found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error searching for product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update the product list displayed in the table
    private static void updateProductList() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Products";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                tableModel.setRowCount(0);
                while (rs.next()) {
                    Object[] row = {
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("category"),
                            rs.getBigDecimal("price"),
                            rs.getInt("stock_quantity")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating product list.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private static void clearFields() {
        productNameField.setText("");
        priceField.setText("");
        stockQuantityField.setText("");
        searchField.setText("");
    }

    public static JFrame getFrame() {
        return frame;
    }
}
