//Nathan Nelson

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientList {

    // List to store clients
    private List<Client> clients;

    // Constructor
    public ClientList() {
        clients = new ArrayList<>();
    }

    // Search for a client by ID
    public Client search(String clientID) {
        for (Client client : clients) {
            if (client.getID().equals(clientID)) {
                return client;
            }
        }
        return null;  // Client not found
    }

    // Insert a new client
    public boolean insertClient(Client client) {
        return clients.add(client);
    }

    // Get an iterator for the clients
    public Iterator<Client> getClients() {
        return clients.iterator();
    }
}
