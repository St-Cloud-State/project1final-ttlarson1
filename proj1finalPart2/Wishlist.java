import java.util.HashMap;
import java.util.Map;

public class Wishlist {
    private String clientID;
    private Map<Product, Integer> products; // Stores Product objects and desired quantities.

    public Wishlist(String clientID) {
        this.clientID = clientID;
        this.products = new HashMap<>();
    }

    public boolean addProduct(Product product, int quantity) {
        if (product == null) return false;
        if(products.containsKey(product)){
            int temp = products.get(product) + quantity;
            products.put(product, (temp));
            if( temp < 0 ) {products.remove(product);}
        } else if(quantity > 0){
            products.put(product, quantity);
        } else {return false;}
        return true;
    }

    public boolean removeProduct(Product product) {
        return products.remove(product) != null;
    }

    public Map<Product, Integer> getProductsWithQuantities() {
        return new HashMap<>(products);
    }

    public String getClientID() {
        return clientID;
    }
}