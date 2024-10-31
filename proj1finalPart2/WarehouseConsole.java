import java.util.Scanner;
import java.util.Iterator;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class WarehouseConsole {
    private ClientList clientList = new ClientList();
    private Catalog catalog = Catalog.getInstance();
    private Integer clientCount = 0;
    private Integer productCount = 0;

    public void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        displayOptions();
        while (true) {
            System.out.print("Select a command, 19 for Help: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 
    
            switch (choice) {
                case 1:
                    manageClients(scanner);
                    break;
                case 2:
                    manageProducts(scanner);
                    break;
                case 3:
                    manageClientWishlist(scanner);
                    break;
                case 4:
                    placeOrderForClient(scanner);
                    break;
                case 5:
                    viewClientWishlist(scanner);
                    break;
                case 6:
                    viewProductWaitlist(scanner);
                    break;
                case 7:
                    recordClientPayment(scanner);
                    break;
                case 8:
                    receiveProductShipment(scanner);
                    break;
                case 9:
                    displayClients();
                    break;
                case 10:
                    displayProducts();
                    break;
                case 11:
                    displayInvoiceByClient(scanner);
                    break;
                case 19:
                    displayOptions();
                    break;
                case 20:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void displayOptions() {
        System.out.println("Warehouse Management System");
        System.out.println("1. Add Client");
        System.out.println("2. Add Product");
        System.out.println("3. Manage Client Wishlist");
        System.out.println("4. Place Order for Client");
        System.out.println("5. View Client Wishlist");
        System.out.println("6. View Product Waitlist");
        System.out.println("7. Record Client Payment");
        System.out.println("8. Receive Product Shipment");
        System.out.println("9. View All Clients");
        System.out.println("10. View All Products");
        System.out.println("11. View Invoices");
        System.out.println("19. Help");
        System.out.println("20. Exit");
    }    

    private void placeOrderForClient(Scanner scanner){
        System.out.print("Enter client number to place order: ");
        int clientNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        Client client = getClientByIndex(clientNumber);
        if (client != null) {
            processWishlistPurchase(client);
            System.out.println("Order placed for client: " + client.getName());
        } else {
            System.out.println("Client not found.");
        }
    }
    
    private void displayInvoiceByClient(Scanner scanner) {
        System.out.print("Enter client number to view invoice: ");
        int clientNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        Client client = getClientByIndex(clientNumber);
        if (client != null) {
            List<Invoice> temp = Invoice.getInvoicesByClientID(client.getID());
            for(Invoice in : temp) {
                System.out.println(in.getInvoiceDetails());
            }
        } else {
            System.out.println("Client not found.");
        }
    }

    private void viewClientWishlist(Scanner scanner) {
        System.out.print("Enter client number to view wishlist: ");
        int clientNumber = scanner.nextInt();
        scanner.nextLine(); 
    
        Client client = getClientByIndex(clientNumber);
        if (client != null) {
            System.out.println(client.getName() + "'s wishlist:");
            displayClientWishlist(client);
        } else {
            System.out.println("Client not found.");
        }
    }

    private void viewProductWaitlist(Scanner scanner) {
        System.out.print("Enter product number to view waitlist: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); 
    
        Product product = getProductByIndex(productIndex);
        if (product != null) {
            System.out.println("Waitlist for " + product.getName() + ": " + product.getWaitlist());
        } else {
            System.out.println("Product not found.");
        }
    }

    private void recordClientPayment(Scanner scanner) {
        System.out.print("Enter client number to record payment: ");
        int clientNumber = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.print("Enter payment amount: ");
        double payment = scanner.nextDouble();
        scanner.nextLine(); 
    
        Client client = getClientByIndex(clientNumber);
        if (client != null) {
            client.addToBalance(payment);
            System.out.println("Recorded payment of $" + payment + " for client: " + client.getName());
        } else {
            System.out.println("Client not found.");
        }
    }

    private void receiveProductShipment(Scanner scanner) {
        System.out.print("Enter product number to receive shipment: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter quantity received: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product product = getProductByIndex(productIndex);
        if (product != null) {
            product.addToQuantity(quantity);
            fulfillWaitlist(product);
            System.out.println("Received shipment of " + quantity + " for product: " + product.getName());
        } else {
            System.out.println("Product not found.");
        }
    }

private void fulfillWaitlist(Product product) {
    List<String> unfullfilled = new ArrayList<>();
    while (product.getQuantity() > 0 && !product.getWaitlistQueue().isEmpty()) {
        String clientId = product.getWaitlistQueue().poll(); // Get next client on the waitlist
        Client client = clientList.search(clientId);

        if (client != null) {
            Wishlist wishlist = client.getWishlist();
            Integer desiredQty = wishlist.getProductsWithQuantities().getOrDefault(product, 0);
            Map<Product, Integer> temp = new HashMap<>();

            if (desiredQty <= product.getQuantity() && client.getBalance() >= desiredQty * product.getPrice()) {
                // Fulfill entire quantity and update product and wishlist
                product.addToQuantity(-desiredQty);
                client.addToWishlist(product, -desiredQty);
                client.addToBalance(-desiredQty*product.getPrice());
                temp.put(product, desiredQty);
                generateInvoice(client, temp);
                System.out.println("Fulfilled waitlist for client " + client.getName() + ": " + desiredQty + " of " + product.getName());
            } else {
                int purchasableQty = desiredQty;
                if(purchasableQty > product.getQuantity()){
                    unfullfilled.add(clientId);
                }
                while(purchasableQty > 0 && (purchasableQty * product.getPrice() > client.getBalance() || purchasableQty > product.getQuantity())){
                    purchasableQty--;
                }
                if(purchasableQty != 0){
                    // Partially fulfill the order and update remaining desired quantity in wishlist
                    client.addToWishlist(product, -purchasableQty);
                    System.out.println("Partial fulfillment for client " + client.getName() + ": " + purchasableQty + " of " + product.getName());
                    product.addToQuantity(-purchasableQty); // All available quantity is allocated
                    client.addToBalance(-purchasableQty*product.getPrice());
                    temp.put(product, purchasableQty);
                    generateInvoice(client, temp);
                }
            }
        }
    }
    for( String s : unfullfilled){
        product.getWaitlistQueue().add(s);
    }

    if (product.getQuantity() > 0) {
        System.out.println("Remaining stock after fulfilling waitlist: " + product.getQuantity());
    } else {
        System.out.println("All available stock was used to fulfill the waitlist.");
    }
}



    private void manageClientWishlist(Scanner scanner) {
        System.out.println("Which client do you want to manage: ");
        displayClients();
        boolean valid = false;
        Integer clientIndex = 0;
    
        while (!valid) {
            System.out.print("Enter client's number you want to manage: ");
            String input = scanner.nextLine();

            try {
                clientIndex = Integer.parseInt(input);
                if(clientIndex > 0 && clientIndex <= clientCount) valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid client number.");
            }
        }

        Client client = getClientByIndex(clientIndex);

        System.out.println("Client chosen: " + client + " and their wishlist is: ");
        displayClientWishlist(client);

        System.out.println("Do you want to add or remove items from this wishlist? Enter 0 to return.");
        valid = false;
    
        while (!valid) {
            System.out.print("Enter 'add', 'remove', 'buyall', or 0 to return: ");
            String input = scanner.nextLine();
        
            if(input.equals("0")) return;

            switch(input.toLowerCase()) {
                case "buyall":
                    processWishlistPurchase(client);
                    return;
                
                case "add":
                    addProductToWishlist(scanner, client);
                    valid = true;
                    break;

                case "remove":
                    removeProductFromWishlist(scanner, client);
                    valid = true;
                    break;

                default:
                    System.out.println("Invalid option. Please enter 'add', 'remove', 'buyall', or 0 to return.");
            }
        }
    }

    private void displayClientWishlist(Client client) {
        Map<Product, Integer> wishlist = client.getWishlistWithQuantities();
        int index = 1;
        double totalPrice = 0.0;
    
        System.out.println(client.getName() + "'s Wishlist:");
        for (Map.Entry<Product, Integer> entry : wishlist.entrySet()) {
            Product product = entry.getKey();
            int desiredQuantity = entry.getValue();
            double itemTotal = product.getPrice() * desiredQuantity;
            totalPrice += itemTotal;
    
            System.out.println(index++ + ". " + product.getName() + " - Desired Quantity: " + desiredQuantity +
                               ", Price per unit: $" + product.getPrice() + ", Total for item: $" + itemTotal);
        }
        System.out.println("Total Price of Wishlist: $" + totalPrice);
    }        

    private void addProductToWishlist(Scanner scanner, Client client) {
        System.out.println("Which product would you like to add?");
        displayProducts();

        System.out.print("Enter product number you wish to add: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter quantity desired: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); 

        Product product = getProductByIndex(productIndex);
        if(product != null) {
            client.addToWishlist(product, quantity);
            System.out.println("Added " + quantity + " of " + product.getName() + " to " + client.getName() + "'s wishlist.");
        }
    }

    private void removeProductFromWishlist(Scanner scanner, Client client) {
        displayClientWishlist(client);

        System.out.print("Enter product number you wish to remove: ");
        int productIndex = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Product product = getProductByIndex(productIndex);
        if(product != null) {
            client.removeFromWishlist(product);
            System.out.println("Removed " + product.getName() + " from " + client.getName() + "'s wishlist.");
        } else {
            System.out.println("Invalid product selection.");
        }
    }

    private void processWishlistPurchase(Client client) {
        Map<Product, Integer> wishlist = client.getWishlist().getProductsWithQuantities();
        double totalCost = 0.0;
        Map<Product, Integer> temp = new HashMap<>();
    
        for (Map.Entry<Product, Integer> entry : wishlist.entrySet()) {
            Product product = entry.getKey();
            int desiredQty = entry.getValue();
    
            if (product.getQuantity() >= desiredQty && client.getBalance() >= product.getPrice() * desiredQty) {
                totalCost += product.getPrice() * desiredQty;
                product.addToQuantity(-desiredQty);
                client.removeFromWishlist(product);
                System.out.println("Bought " + desiredQty + " of " + product.getName());
                temp.put(product, desiredQty);
            } else {
                int purchasableQty = product.getQuantity();
                while(purchasableQty > 0 && purchasableQty * product.getPrice() > client.getBalance()){
                    purchasableQty--;
                }
                if(purchasableQty <= 0){
                    System.out.println("Could not afford " + product.getName() + ".");
                }else{
                    if(purchasableQty == product.getQuantity()){
                        product.addToWaitlist(client.getID());
                    }
                    temp.put(product, purchasableQty);
                    totalCost += product.getPrice() * purchasableQty;
                    product.addToQuantity(-purchasableQty);
                    client.getWishlist().addProduct(product, -(purchasableQty));
                    System.out.println("Only " + purchasableQty + " of " + product.getName() + " purchased; added remainder, if any, to waitlist.");
                }
            }
        }
        
        client.addToBalance(-totalCost);
        System.out.println(client.getName() + " has been debited $" + totalCost);
        generateInvoice(client, temp);
    }

    private void generateInvoice(Client client, Map<Product, Integer> items) {
        Invoice invoice = new Invoice(client.getID(), client.getName(), LocalDate.now());
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            invoice.addItem(product, quantity);
        }
        invoice.finalizeInvoice();
        System.out.println("Invoice generated for " + client.getName() + ":\n" + invoice.getInvoiceDetails());
    }

    private void displayProducts(){
        Iterator<Product> pIterator = catalog.getAllProducts();
        int inc = 0;
        while (pIterator.hasNext()) { 
            inc++;
            Product product = pIterator.next(); 
            System.out.println(inc + " " + product.getProductInfo());
        }
    }

    private Client getClientByIndex(int index) {
        Iterator<Client> cIterator = clientList.getClients();
        Client client = null;
        int currentIndex = 1;
    
        while (cIterator.hasNext()) {
            client = cIterator.next();
            if (currentIndex == index) {
                return client; // Return the client when index matches
            }
            currentIndex++;
        }
    
        return null; // Return null if index is out of bounds
    }    

    private void displayClients(){
        Iterator<Client> cIterator = clientList.getClients();
        int inc = 0;
        while (cIterator.hasNext()) { 
            inc++;
            Client client = cIterator.next(); 
            System.out.println(inc + " " + client);
        }
    }

    // Method to manage clients
    private void manageClients(Scanner scanner) {
        System.out.println("Manage Clients");
        System.out.print("Enter Client Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Client Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Client Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter starting balance: ");
        boolean valid = false;
        Double bal = 0.0;
        while (!valid) {
            System.out.print("Enter clients balance: ");
            String input = scanner.nextLine();

            try {
                bal = Double.parseDouble(input); // Attempt to parse it as an integer
                valid = true;
                System.out.println("You entered the integer: " + bal);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        Client client = new Client(name, address, phone, bal);
        clientList.insertClient(client);
        System.out.println("Client added successfully!");
        clientCount++;
    }

    // Method to manage products
    private void manageProducts(Scanner scanner) {
        System.out.println("Manage Products");
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();  // Consume the newline character
        System.out.print("Enter Product Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Product Category: ");
        String category = scanner.nextLine();
        boolean valid = false;
        Integer bal = 0;
        while (!valid) {
            System.out.print("Enter the quantity: ");
            String input = scanner.nextLine();

            try {
                bal = Integer.parseInt(input); // Attempt to parse it as an integer
                valid = true;
                System.out.println("You entered the integer: " + bal);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        Product product = new Product(name, price, description, category, bal);
        catalog.addProduct(product);
        System.out.println("Product added successfully!");
        productCount++;
    }

    // // Method to create an order
    // private void createOrder(Scanner scanner) {
    //     System.out.println("Create Order");
    //     System.out.print("Enter Client ID: ");
    //     String clientID = scanner.nextLine();
    //     System.out.print("Enter Product ID: ");
    //     String productID = scanner.nextLine();
    //     System.out.print("Enter Quantity: ");
    //     int quantity = scanner.nextInt();

    //     Client client = clientList.search(clientID);
    //     Product product = catalog.searchProduct(productID);

    //     if (client != null && product != null) {
    //         Order order = new Order(client.getID(), java.time.LocalDate.now());
    //         order.addItem(product, quantity);
    //         System.out.println("Order created successfully!");
    //     } else {
    //         System.out.println("Client or Product not found.");
    //     }
    // }

    // // Method to view orders (Placeholder)
    // private void viewOrders() {
    //     System.out.println("View Orders");
    //     // Orders functionality would go here
    //     System.out.println("No orders to display (order management not yet implemented).");
    // }

    // private void placeOrder(Client client) {
    //     Map<Product, Integer> wishlist = client.getWishlist().getProductsWithQuantities();
    //     double totalCost = 0.0;
    
    //     for (Map.Entry<Product, Integer> entry : wishlist.entrySet()) {
    //         Product product = entry.getKey();
    //         int desiredQty = entry.getValue();
    
    //         if (product.getQuantity() >= desiredQty) {
    //             totalCost += product.getPrice() * desiredQty;
    //             product.addToQuantity(-desiredQty);
    //             System.out.println("Bought " + desiredQty + " of " + product.getName());
    //         } else {
    //             int purchasableQty = product.getQuantity();
    //             totalCost += product.getPrice() * purchasableQty;
    //             product.addToQuantity(-purchasableQty);
    //             client.getWishlist().addProduct(product, desiredQty - purchasableQty); // Remaining quantity goes back to wishlist
    //             product.addToWaitlist(client.getID());
    //             System.out.println("Only " + purchasableQty + " of " + product.getName() + " purchased; added remainder to waitlist.");
    //         }
    //     }
    
    //     client.addToBalance(-totalCost);
    //     System.out.println(client.getName() + " has been debited $" + totalCost);
    // }

    private Product getProductByIndex(int index) {
        Iterator<Product> pIterator = catalog.getAllProducts();
        Product product = null;
        for (int i = 0; i < index && pIterator.hasNext(); i++) {
            product = pIterator.next();
        }
        return product;
    }




    public void testWarehouseConsole() {
        System.out.println("Testing Warehouse Console...");

        System.out.println("\n--- Adding Clients C1 to C5 ---");
        clientList.insertClient(new Client("C1", "Address1", "123-456-7890", 100.0));
        clientList.insertClient(new Client("C2", "Address2", "234-567-8901", 100.0));
        clientList.insertClient(new Client("C3", "Address3", "345-678-9012", 100.0));
        clientList.insertClient(new Client("C4", "Address4", "456-789-0123", 100.0));
        clientList.insertClient(new Client("C5", "Address5", "567-890-1234", 100.0));
        clientCount += 5;

        System.out.println("\n--- Displaying All Clients ---");
        displayClients();

        System.out.println("\n--- Adding Products P1 to P5 ---");
        catalog.addProduct(new Product("P1", 1.0, "Description1", "Category1", 10));
        catalog.addProduct(new Product("P2", 2.0, "Description2", "Category2", 20));
        catalog.addProduct(new Product("P3", 3.0, "Description3", "Category3", 30));
        catalog.addProduct(new Product("P4", 4.0, "Description4", "Category4", 40));
        catalog.addProduct(new Product("P5", 5.0, "Description5", "Category5", 50));
        productCount += 5;

        System.out.println("\n--- Displaying All Products ---");
        displayProducts();

        Client c1 = getClientByIndex(1);
        Product p1 = getProductByIndex(1);
        Product p3 = getProductByIndex(3);
        Product p5 = getProductByIndex(5);
        if (c1 != null && p1 != null && p3 != null && p5 != null) {
            System.out.println("\n--- Adding Items to C1's Wishlist ---");
            c1.addToWishlist(p1, 5);
            c1.addToWishlist(p3, 5);
            c1.addToWishlist(p5, 5);
            displayClientWishlist(c1);
        }

        Client c2 = getClientByIndex(2);
        Product p2 = getProductByIndex(2);
        Product p4 = getProductByIndex(4);
        if (c2 != null && p1 != null && p2 != null && p4 != null) {
            System.out.println("\n--- Adding Items to C2's Wishlist ---");
            c2.addToWishlist(p1, 7);
            c2.addToWishlist(p2, 7);
            c2.addToWishlist(p4, 7);
            displayClientWishlist(c2);
        }

        Client c3 = getClientByIndex(3);
        if (c3 != null && p1 != null && p2 != null && p5 != null) {
            System.out.println("\n--- Adding Items to C3's Wishlist ---");
            c3.addToWishlist(p1, 6);
            c3.addToWishlist(p2, 6);
            c3.addToWishlist(p5, 6);
            displayClientWishlist(c3);
        }

        System.out.println("\n--- Placing Order for C2 ---");
        if (c2 != null) {
            processWishlistPurchase(c2);
        }

        System.out.println("\n--- Updated Client Balances ---");
        displayClients();

        System.out.println("\n--- Placing Order for C3 ---");
        if (c3 != null) {
            processWishlistPurchase(c3);
        }

        System.out.println("\n--- Updated Client Balances ---");
        displayClients();

        System.out.println("\n--- C2's Wishlist Post-Order ---");
        displayClientWishlist(c2);
        System.out.println("\n--- C3's Wishlist Post-Order ---");
        displayClientWishlist(c3);

        System.out.println("\n--- P1's Waitlist ---");
        System.out.println(p1.getWaitlist());
        System.out.println("\n--- P2's Waitlist ---");
        System.out.println(p2.getWaitlist());

        System.out.println("\n--- Placing Order for C1 ---");
        if (c1 != null) {
            processWishlistPurchase(c1);
        }

        System.out.println("\n--- Updated Client Balances ---");
        displayClients();

        System.out.println("\n--- C1's Wishlist Post-Order ---");
        displayClientWishlist(c1);

        System.out.println("\n--- Recording $100 Payment for C1 and C2 ---");
        if (c1 != null) {
            c1.addToBalance(100.0);
        }
        if (c2 != null) {
            c2.addToBalance(100.0);
        }

        System.out.println("\n--- Updated Client Balances After Payment ---");
        displayClients();

        System.out.println("\n--- Receiving Shipment of 100 Items for P1 ---");
        if (p1 != null) {
            p1.addToQuantity(100);
            p1.fulfillWaitlist(100);
        }

        System.out.println("\n--- Updated Product Quantities After Shipment ---");
        displayProducts();

        System.out.println("\n--- Final Client Balances ---");
        displayClients();
    }

    public static void main(String[] args) {
        WarehouseConsole app = new WarehouseConsole();
        app.showMainMenu();
    }
}
