//Nathan Nelson

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Client {

    private String id;
    private String name;
    private String address;
    private String phone;
    private Double balance;
    private List<String> transactions;
    private Wishlist myWishlist;
    // private Orders myOrders;

    // Constructor
    public Client(String name, String address, String phone, Double balance) {
        this.id = UUID.randomUUID().toString();  // Generates a unique ID
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.balance = balance;
        this.transactions = new ArrayList<>();  // Empty list of transactions
        this.myWishlist = new Wishlist(this.id);  // Initialize Wishlist with Client ID       
        // this.myOrders = new Orders();           // Initialize empty Orders
    }

    public void addToWishlist(Product product, int quantity) {
        myWishlist.addProduct(product, quantity);
    }

    public void removeFromWishlist(Product product) {
        myWishlist.removeProduct(product); 
    }

    public Map<Product, Integer> getWishlistWithQuantities() {
        return myWishlist.getProductsWithQuantities();
    }

    public Wishlist getWishlist() {
        return myWishlist;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Get an iterator for transactions
    public Iterator<String> getTransactions() {
        return transactions.iterator();
    }

    // Add a transaction to the list
    public void addTransaction(String transaction) {
        transactions.add(transaction);
    }

    public void addToBalance(Double balance){
        this.balance += balance;
    }

    public Double getBalance(){
        return balance;
    }

    @Override
    public String toString(){
        return "Client{" +
               "name='" + name + '\'' +
               ", address='" + address + '\'' +
               ", phone='" + phone + '\'' +
               ", balance='" + balance + '\'' +
               '}';
    }
}
