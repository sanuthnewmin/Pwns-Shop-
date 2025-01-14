import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HowToUse {

    public static void main(String[] args) {

        JFrame howToUseFrame = new JFrame("How to Use the Pet Shop System");
        howToUseFrame.setSize(1000, 600);
        howToUseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        howToUseFrame.setLocationRelativeTo(null);


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(255, 204, 51));
        howToUseFrame.add(contentPanel);


        JLabel titleLabel = new JLabel("How to Use the Pet Shop System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        contentPanel.add(titleLabel, BorderLayout.NORTH);


        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setText(
                "Welcome to the Pet Shop System! Here's how you can use the application:\n\n" +
                        "1. **Customer Management**:\n" +
                        "   - Add, edit, or delete customer details.\n" +
                        "   - View customer history and preferences.\n\n" +
                        "2. **Product Management**:\n" +
                        "   - Manage product inventory, including adding, editing, and removing items.\n" +
                        "   - Set alerts for low stock levels to ensure timely restocking.\n\n" +
                        "3. **Supplier Management**:\n" +
                        "   - Track supplier details and manage supplier interactions.\n" +
                        "   - Create and track purchase orders to maintain stock levels.\n\n" +
                        "4. **Order Management**:\n" +
                        "   - Place new orders, view existing orders, and update order statuses.\n" +
                        "   - Assign orders to specific cashiers or employees for processing.\n\n" +
                        "5. **Order Summary**:\n" +
                        "   - View a summary of all orders placed, including payment status and total amounts.\n" +
                        "   - Filter and search orders by date, customer, or status.\n\n" +
                        "6. **Cashier Management**:\n" +
                        "   - Manage cashier roles and their access to the system.\n" +
                        "   - Track cashier performance through detailed transaction reports.\n\n" +
                        "7. **Stock Monitoring**:\n" +
                        "   - Monitor real-time stock levels of all products.\n" +
                        "   - Generate inventory reports for analysis and planning.\n\n" +
                        "8. **Promotions and Discounts**:\n" +
                        "   - Create and apply promotional offers or discounts on products.\n" +
                        "   - Manage seasonal sales campaigns.\n\n" +
                        "9. **Loyalty Programs**:\n" +
                        "   - Enroll customers in loyalty programs to reward frequent purchases.\n" +
                        "   - Track and redeem loyalty points.\n\n" +
                        "10. **Reporting and Analytics**:\n" +
                        "    - Generate sales, customer, and product performance reports.\n" +
                        "    - Use visual dashboards to analyze trends and make data-driven decisions.\n\n" +
                        "11. **User-Friendly Navigation**:\n" +
                        "    - Use the intuitive dashboard buttons to access different features.\n" +
                        "    - Navigate easily with clear labels and real-time system feedback.\n\n" +
                        "12. **Data Security and Backup**:\n" +
                        "    - Ensure your data is secure with user authentication and encrypted storage.\n" +
                        "    - Perform regular backups to avoid data loss.\n\n" +
                        "13. **Help and Support**:\n" +
                        "    - Access this guide anytime by clicking the 'Help' button.\n" +
                        "    - Contact technical support directly from the system for assistance.\n\n" +
                        "Enjoy managing your Pet Shop efficiently with this powerful and user-friendly system!"
        );
        instructionsArea.setFont(new Font("Arial", Font.PLAIN, 14));
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);


        JScrollPane scrollPane = new JScrollPane(instructionsArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);


        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(255, 204, 51));
        backButton.setFocusPainted(false);
        contentPanel.add(backButton, BorderLayout.SOUTH);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                howToUseFrame.dispose();
            }
        });


        howToUseFrame.setVisible(true);
    }
}
