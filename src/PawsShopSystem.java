import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class PawsShopSystem {

    //Set Database Connection

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pawsshop";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "20020216";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showLoginScreen());
    }

    //Encapsulation Usages
    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(600, 400);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        // Login Image Panel
        ImageIcon imageIcon = new ImageIcon("G:\\PawsShop\\login_image\\login1.png");
        Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);

        JLabel imageLabel = new JLabel(imageIcon);
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(200, 400));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField usernameField = new JTextField(15);
        usernameField.setBackground(Color.WHITE);
        formPanel.add(usernameField, gbc);

        // Password Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBackground(Color.WHITE);
        formPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(255, 204, 51));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        loginButton.setOpaque(true);
        loginButton.setContentAreaFilled(true);
        formPanel.add(loginButton, gbc);



        // Add Action to login panel
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Username and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String query = "SELECT role FROM users WHERE username = ? AND password_hash = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashPassword(password));

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    JOptionPane.showMessageDialog(loginFrame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loginFrame.dispose();

                    // Redirect to respective dashboard
                    if ("Admin".equalsIgnoreCase(role)) {
                        showAdminDashboard();
                    } else if ("User".equalsIgnoreCase(role)) {
                        showUserDashboard();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Unknown role.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid Username or Password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginFrame, "Database Error.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        loginFrame.add(imagePanel, BorderLayout.WEST);
        loginFrame.add(formPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }
    private static void showRegisterScreen() {
        JFrame registerFrame = new JFrame("Add User to System");
        registerFrame.setSize(1000, 600);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setLayout(new BorderLayout());


        registerFrame.getContentPane().setBackground(new Color(255, 255, 255));


        ImageIcon imageIcon = new ImageIcon("G:\\PawsShop\\login_image\\cha.png");
        Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);

        JLabel imageLabel = new JLabel(imageIcon);
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(200, 400));


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 1, true));
        formPanel.setBackground(new Color(255, 255, 255));


        formPanel.setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JTextField userIdField = new JTextField(15);
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Admin", "User"});
        JTextField fullNameField = new JTextField(15);
        JTextField contactField = new JTextField(15);
        JTextField emailField = new JTextField(15);


        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");


        registerButton.setBackground(new Color(255, 204, 51));
        backButton.setBackground(new Color(255, 204, 51));


        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));


        registerButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        backButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));


        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Full Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Contact Number:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Role:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(roleComboBox, gbc);


        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(registerButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(backButton, gbc);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(imagePanel, BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);


        registerFrame.add(mainPanel, BorderLayout.CENTER);


        registerButton.addActionListener(e -> {

            JOptionPane.showMessageDialog(registerFrame, "Registration Successful!");

        });

        backButton.addActionListener(e -> {

            registerFrame.dispose();
            showLoginScreen();
        });

        registerFrame.setVisible(true);


        registerButton.addActionListener(e -> {
            String userId = userIdField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            String fullName = fullNameField.getText();
            String contactNumber = contactField.getText();
            String email = emailField.getText();

            if (userId.isEmpty() || username.isEmpty() || password.isEmpty() || role.isEmpty() ||
                    fullName.isEmpty() || contactNumber.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String passwordHash = hashPassword(password); // Hash the password
                String createdAt = new Timestamp(System.currentTimeMillis()).toString();

                String query = "INSERT INTO users (user_id, username, password_hash, role, full_name, contact_number, email, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, passwordHash);
                preparedStatement.setString(4, role);
                preparedStatement.setString(5, fullName);
                preparedStatement.setString(6, contactNumber);
                preparedStatement.setString(7, email);
                preparedStatement.setString(8, createdAt);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(registerFrame, "Registration successful!");
                    registerFrame.dispose();
                    showLoginScreen();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(registerFrame, "Error during registration.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            registerFrame.dispose();
            showLoginScreen();
        });

        registerFrame.add(imagePanel, BorderLayout.WEST);


        registerFrame.setVisible(true);
    }

    private static String hashPassword(String password) {

        return password;
    }

    private static void showAdminDashboard() {
        JFrame adminFrame = new JFrame("Admin Dashboard");
        adminFrame.setSize(1000, 600);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setBackground(new Color(255, 229, 204));
        adminFrame.setLayout(new BorderLayout());


        adminFrame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));


        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(250, 600));
        sidebarPanel.setBackground(new Color(204, 204, 204));

        // Logo at the top of the sidebar
        JLabel logoLabel = new JLabel(new ImageIcon("G:\\PawsShop\\login_image\\login1.png"));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarPanel.add(logoLabel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new GridLayout(6, 1, 10, 10));
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // Create buttons with custom styling and gray border
        JButton customerManagementButton = createStyledButton("Customer Management");
        JButton productManagementButton = createStyledButton("Product Management");
        JButton supplierManagementButton = createStyledButton("Supplier Management");
        JButton makeOrderButton = createStyledButton("Place Order");
        JButton viewOrderSummaryButton = createStyledButton("View Order Summary");
        JButton addUserButton = createStyledButton("Add Cashier");
        JButton showUserButton = createStyledButton("Manage Users");
        JButton howToUseButton = createStyledButton("How to Use System");
        JButton logoutButton = createStyledButton("Log Out");

        mainContentPanel.add(customerManagementButton);
        mainContentPanel.add(productManagementButton);
        mainContentPanel.add(supplierManagementButton);
        mainContentPanel.add(makeOrderButton);
        mainContentPanel.add(viewOrderSummaryButton);
        mainContentPanel.add(howToUseButton);
        mainContentPanel.add(showUserButton);
        mainContentPanel.add(addUserButton);
        mainContentPanel.add(logoutButton);


        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(204, 204, 204));
        JLabel headerLabel = new JLabel("Welcome to Admin Dashboard!", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        headerLabel.setForeground(Color.BLACK);
        headerPanel.add(headerLabel, BorderLayout.CENTER);


        adminFrame.add(sidebarPanel, BorderLayout.WEST);
        adminFrame.add(headerPanel, BorderLayout.NORTH);
        adminFrame.add(mainContentPanel, BorderLayout.CENTER);


        customerManagementButton.addActionListener(e -> {
            // Open Customer Management Page
            adminFrame.setVisible(true);
            CustomerManagement.main(new String[]{});
            CustomerManagement.getFrame().setAlwaysOnTop(true);
            CustomerManagement.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        productManagementButton.addActionListener(e -> {
            // Open Product Management Page
            adminFrame.setVisible(true);
            ProductManagementSystem.main(null);
            ProductManagementSystem.getFrame().setAlwaysOnTop(true);
            ProductManagementSystem.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        supplierManagementButton.addActionListener(e -> {
            // Open Supplier Management Page
            adminFrame.setVisible(true);
            SupplierManagement.main(null);
            SupplierManagement.getFrame().setAlwaysOnTop(true);
            SupplierManagement.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        makeOrderButton.addActionListener(e -> {
            // Open Place Order Page
            adminFrame.setVisible(true);
            PlaceOrder.main(null);
            PlaceOrder.getFrame().setAlwaysOnTop(true);
            PlaceOrder.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        viewOrderSummaryButton.addActionListener(e -> {
            // Open View Order Summary Page
            adminFrame.setVisible(true);
            OrderSummary.main(null);
            OrderSummary.getFrame().setAlwaysOnTop(true);
            OrderSummary.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        howToUseButton.addActionListener(e -> {
            // Open View How to use Page
            adminFrame.setVisible(true);
            HowToUse.main(null);
            OrderSummary.getFrame().setAlwaysOnTop(true);
            OrderSummary.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        addUserButton.addActionListener(e -> {
            // Manage Users
            adminFrame.setVisible(true);
            showRegisterScreen();
            OrderSummary.getFrame().setAlwaysOnTop(true);
            OrderSummary.getFrame().toFront();
            adminFrame.setVisible(true);
        });

            showUserButton.addActionListener(e -> {
            // Open View How to use Page
            adminFrame.setVisible(true);
            ShowUsers.main(null);
            OrderSummary.getFrame().setAlwaysOnTop(true);
            OrderSummary.getFrame().toFront();
            adminFrame.setVisible(true);
        });



        logoutButton.addActionListener(e -> {
            adminFrame.dispose();
            showLoginScreen();
        });

        adminFrame.setVisible(true);
    }


    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 51));
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                button.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        return button;
    }



    private static void showUserDashboard() {
        JFrame adminFrame = new JFrame("User Dashboard");
        adminFrame.setSize(1000, 600);
        adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adminFrame.setBackground(new Color(255, 229, 204));
        adminFrame.setLayout(new BorderLayout());


        adminFrame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));


        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BorderLayout());
        sidebarPanel.setPreferredSize(new Dimension(250, 600));
        sidebarPanel.setBackground(new Color(204, 204, 204));


        JLabel logoLabel = new JLabel(new ImageIcon("G:\\PawsShop\\login_image\\login1.png"));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        sidebarPanel.add(logoLabel, BorderLayout.NORTH);


        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new GridLayout(6, 1, 10, 10));
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));


        JButton customerManagementButton = createStyledButton("Customer Management");
        JButton productManagementButton = createStyledButton("Product Management");
        JButton makeOrderButton = createStyledButton("Place Order");
        JButton howToUseButton = createStyledButton("How to Use System");
        JButton logoutButton = createStyledButton("Log Out");

        mainContentPanel.add(customerManagementButton);
        mainContentPanel.add(productManagementButton);
        mainContentPanel.add(makeOrderButton);
        mainContentPanel.add(howToUseButton);
        mainContentPanel.add(logoutButton);


        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(204, 204, 204));
        JLabel headerLabel = new JLabel("Welcome to User Dashboard!", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        headerLabel.setForeground(Color.BLACK);
        headerPanel.add(headerLabel, BorderLayout.CENTER);


        adminFrame.add(sidebarPanel, BorderLayout.WEST);
        adminFrame.add(headerPanel, BorderLayout.NORTH);
        adminFrame.add(mainContentPanel, BorderLayout.CENTER);


        customerManagementButton.addActionListener(e -> {

            adminFrame.setVisible(true);
            CustomerManagement.main(new String[]{});
            CustomerManagement.getFrame().setAlwaysOnTop(true);
            CustomerManagement.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        productManagementButton.addActionListener(e -> {

            adminFrame.setVisible(true);
            ProductManagementUser.main(null);
            ProductManagementSystem.getFrame().setAlwaysOnTop(true);
            ProductManagementSystem.getFrame().toFront();
            adminFrame.setVisible(true);
        });


        makeOrderButton.addActionListener(e -> {
            // Open Place Order Page
            adminFrame.setVisible(true);
            PlaceOrderCashier.main(null);
            PlaceOrder.getFrame().setAlwaysOnTop(true);
            PlaceOrder.getFrame().toFront();
            adminFrame.setVisible(true);
        });

        howToUseButton.addActionListener(e -> {
            // Open View How to use Page
            adminFrame.setVisible(true);
            HowToUse.main(null);
            OrderSummary.getFrame().setAlwaysOnTop(true);
            OrderSummary.getFrame().toFront();
            adminFrame.setVisible(true);
        });


        logoutButton.addActionListener(e -> {
            adminFrame.dispose();
            showLoginScreen();
        });

        adminFrame.setVisible(true);
    }
}

