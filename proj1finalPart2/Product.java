import java.util.UUID;
import java.util.Queue;
import java.util.LinkedList;


public class Product {
    private String id;
    private String name;
    private Double price;
    private String description;
    private String category;
    private Integer quantity;
    private Queue<String> waitlist;

    public Product(String name, Double price, String description, String category, Integer quantity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.waitlist = new LinkedList<>();
    }

    // Add this method to access the actual waitlist queue
    public Queue<String> getWaitlistQueue() {
        return waitlist;
    }

    public void addToWaitlist(String clientID) {
        waitlist.add(clientID);
    }

    public void fulfillWaitlist(int receivedQuantity) {
        while (!waitlist.isEmpty() && receivedQuantity > 0) {
            String clientID = waitlist.poll();
            System.out.println("Fulfilling order, if possible, for client: " + clientID);
            receivedQuantity--;
        }
    }    

   public String getWaitlist() {
        return waitlist.toString();
    }

    public void addToQuantity(Integer addition){
        quantity += addition;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductInfo() {
        return "Product ID: " + id + "\n" +
               "Name: " + name + "\n" +
               "Price: " + price + "\n" +
               "Description: " + description + "\n" +
               "Category: " + category + "\n" +
               "Quantity: " + quantity;
    }

    public boolean updateProduct(double price, String description, String category, Integer quantity) {
        this.price = price;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        return true;
    }

    public boolean removeProduct() {
        this.name = null;
        this.price = 0.0;
        this.description = null;
        this.category = null;
        this.quantity = 0;
        return true;
    }

    @Override
    public String toString(){
        return name;
    }
}