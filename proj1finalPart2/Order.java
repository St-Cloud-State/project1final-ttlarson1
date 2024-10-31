// Morgan Rassatt


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {// Represents a single order in the system
    private String id;
    private String clientID;
    private LocalDate date;
    private List<OrderItem> items;


    public Order(String clientID, LocalDate date) { // Initializes the order with client ID and date
        this.id = UUID.randomUUID().toString(); // Generates a unique identifier for an order
        this.clientID = clientID;
        this.date = date;
        this.items = new ArrayList<>();
    }

    public String getID() { // Get the order ID
        return id;
    }

    public String getClientID() { // Get the client ID
        return clientID;
    }

    public LocalDate getDate() { // Get the date of the order
        return date;
    }

    public boolean addItem(Product product, int quantity) { // Method to add an item to the order
        for (OrderItem item : items) {
            if (item.getProduct().getID().equals(product.getID())) {
                return false; // Return false if the product already exists
            }
        }
        items.add(new OrderItem(product, quantity));
        return true;
    }

    public boolean updateItem(String productID, int quantity) { // Method to update the quantity of an item already in the order
        for (OrderItem item : items) {
            if (item.getProduct().getID().equals(productID)) {
                item.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(String productID) { // Method to remove an item from the order
        return items.removeIf(item -> item.getProduct().getID().equals(productID));
    }

    public String getOrderDetails() { // Method to get detailed information about the order
        StringBuilder details = new StringBuilder("Order ID: " + id + "\nClient ID: " + clientID + "\nDate: " + date + "\nItems:\n");
        for (OrderItem item : items) {
            details.append(item.getProductInfo()).append("\n");
        }
        return details.toString();
    }

 
    public boolean processOrder() {// Method to process the order
        return true; // Order processing details, checks for stock and payment info
    }
}

class OrderItem { // Class to represent individual items in an order
    private Product product;
    private int quantity;

    
    public OrderItem(Product product, int quantity) { // Constructor to initialize OrderItem with product and quantity
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {// Get the product associated with this order item
        return product;
    }

    public int getQuantity() {// Get the quantity of the product
        return quantity;
    }

    public void setQuantity(int quantity) { // Set a new quantity for the product
        this.quantity = quantity;
    }

    public String getProductInfo() { // Get detailed product information
        return "Product ID: " + product.getID() + ", Name: " + product.getName() + ", Quantity: " + quantity;
    }
}
