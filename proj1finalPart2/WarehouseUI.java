import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class WarehouseUI {
    private ClientList clientList = new ClientList();
    private Catalog catalog = Catalog.getInstance();

    public WarehouseUI() {
        // Create the main window (JFrame)
        JFrame frame = new JFrame("Warehouse Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Create the panel for holding components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1)); // Grid layout with 6 rows

        // Button for managing clients
        JButton manageClientsBtn = new JButton("Manage Clients");
        manageClientsBtn.addActionListener(e -> showClientManagement());

        // Button for managing products
        JButton manageProductsBtn = new JButton("Manage Products");
        manageProductsBtn.addActionListener(e -> showProductManagement());

        // Button for creating orders
        JButton createOrderBtn = new JButton("Create Order");
        createOrderBtn.addActionListener(e -> showOrderCreation());

        // Button for viewing orders
        JButton viewOrdersBtn = new JButton("View Orders");
        viewOrdersBtn.addActionListener(e -> showOrderView());

        // Add buttons to the panel
        panel.add(manageClientsBtn);
        panel.add(manageProductsBtn);
        panel.add(createOrderBtn);
        panel.add(viewOrdersBtn);

        // Add panel to the frame
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    // Method to show the client management UI
    private void showClientManagement() {
        JFrame clientFrame = new JFrame("Client Management");
        clientFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(20);
        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(15);

        JButton addClientBtn = new JButton("Add Client");
        addClientBtn.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            Double bal = 0.0; //change this if you need

            Client client = new Client(name, address, phone, bal);
            clientList.insertClient(client);
            JOptionPane.showMessageDialog(clientFrame, "Client added successfully!");
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(addressLabel);
        panel.add(addressField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(addClientBtn);

        clientFrame.getContentPane().add(panel);
        clientFrame.setVisible(true);
    }

    // Method to show the product management UI
    private void showProductManagement() {
        JFrame productFrame = new JFrame("Product Management");
        productFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(20);
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField(20);
        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField(20);

        JButton addProductBtn = new JButton("Add Product");
        addProductBtn.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();
            String category = categoryField.getText();

            Product product = new Product(name, price, description, category, 10);
            catalog.addProduct(product);
            JOptionPane.showMessageDialog(productFrame, "Product added successfully!");
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(descriptionLabel);
        panel.add(descriptionField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(addProductBtn);

        productFrame.getContentPane().add(panel);
        productFrame.setVisible(true);
    }

    // Method to show order creation UI
    private void showOrderCreation() {
        JFrame orderFrame = new JFrame("Create Order");
        orderFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel clientIDLabel = new JLabel("Client ID:");
        JTextField clientIDField = new JTextField(20);
        JLabel productIDLabel = new JLabel("Product ID:");
        JTextField productIDField = new JTextField(20);
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(5);

        JButton createOrderBtn = new JButton("Create Order");
        createOrderBtn.addActionListener(e -> {
            String clientID = clientIDField.getText();
            String productID = productIDField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            Client client = clientList.search(clientID);
            Product product = catalog.searchProduct(productID);

            if (client != null && product != null) {
                Order order = new Order(client.getID(), java.time.LocalDate.now());
                order.addItem(product, quantity);
                JOptionPane.showMessageDialog(orderFrame, "Order created successfully!");
            } else {
                JOptionPane.showMessageDialog(orderFrame, "Client or Product not found.");
            }
        });

        panel.add(clientIDLabel);
        panel.add(clientIDField);
        panel.add(productIDLabel);
        panel.add(productIDField);
        panel.add(quantityLabel);
        panel.add(quantityField);
        panel.add(createOrderBtn);

        orderFrame.getContentPane().add(panel);
        orderFrame.setVisible(true);
    }

    // Method to show order viewing UI
    private void showOrderView() {
        JFrame viewOrderFrame = new JFrame("View Orders");
        viewOrderFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JTextArea orderDetails = new JTextArea();
        orderDetails.setEditable(false);

        JButton refreshOrdersBtn = new JButton("Refresh Orders");
        refreshOrdersBtn.addActionListener(e -> {
            // Ideally, we would fetch all orders and display them.
            // You can implement order storage here to get details.
            orderDetails.setText("No orders to display (order management not yet implemented).");
        });

        panel.add(new JScrollPane(orderDetails));
        panel.add(refreshOrdersBtn);

        viewOrderFrame.getContentPane().add(panel);
        viewOrderFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new WarehouseUI();
    }
}
