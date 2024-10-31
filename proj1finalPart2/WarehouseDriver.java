//Nathan Nelson


import java.util.Iterator;

public class WarehouseDriver {
    public static void main(String[] args) {
        // Create a ClientList object
        ClientList clientList = new ClientList();

        // Create and add Client objects
        Client client1 = new Client("Nathan Nelson", "101 Main St", "555-1234", 252.0);
        Client client2 = new Client("Group Twelve", "999 Oak St", "555-5678", 612.0);
        Client client3 = new Client("Tsion Nidaw", "150 Park Ave", "555-8765", 2151.0);
        Client client4 = new Client("Morgan Rassatt", "202 Lakeview Dr", "555-4321", 367.0);
        Client client5 = new Client("Larson Triston", "303 Maple St", "555-6789", 752.0);

        // Insert clients into the client list
        clientList.insertClient(client1);
        clientList.insertClient(client2);
        clientList.insertClient(client3);
        clientList.insertClient(client4);
        clientList.insertClient(client5);

        // Display all clients
        System.out.println("All clients:");
        Iterator<Client> clientIterator = clientList.getClients();
        while (clientIterator.hasNext()) {
            Client client = clientIterator.next();
            System.out.println("Client ID: " + client.getID() + ", Name: " + client.getName() +
                               ", Address: " + client.getAddress() + ", Phone: " + client.getPhone() + client.getBalance());
        }

        // Search for a specific client by ID (Example: client1)
        System.out.println("\nSearching for client with ID: " + client1.getID());
        Client foundClient = clientList.search(client1.getID());
        if (foundClient != null) {
            System.out.println("Found client: " + foundClient.getName());
        } else {
            System.out.println("Client not found.");
        }
    }
}
