import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrderSummary {

    private static DefaultTableModel orderTableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Summary");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1000, 600);


            frame.getContentPane().setBackground(Color.decode("#FFCC99"));


            orderTableModel = new DefaultTableModel(new String[]{
                    "Customer Name", "Product Name", "Order ID", "Quantity", "Total Amount", "Payment Status"
            }, 0);


            JTable orderTable = new JTable(orderTableModel);
            orderTable.setFillsViewportHeight(true);


            JScrollPane scrollPane = new JScrollPane(orderTable);


            JButton refreshButton = new JButton("Refresh");
            refreshButton.setBackground(Color.decode("#FFCC33"));
            refreshButton.addActionListener(e -> updateOrderList());


            frame.setLayout(new BorderLayout());
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(refreshButton, BorderLayout.SOUTH);


            updateOrderList();

            frame.setVisible(true);
        });
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
            JOptionPane.showMessageDialog(null, "Error fetching data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static JFrame getFrame() {
        JFrame Frame = null;
        return Frame;
    }
}

