import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Catalog {
    private static Catalog catalog;
    private List<Product> products;

    private Catalog() {
        this.products = new ArrayList<>();
    }

    public static Catalog getInstance() {
        if (catalog == null) {
            catalog = new Catalog();
        }
        return catalog;
    }

    public boolean addProduct(Product product) {
        return products.add(product);
    }

    public boolean removeProduct(String productID) {
        for (Product product : products) {
            if (product.getID().equals(productID)) {
                return products.remove(product);
            }
        }
        return false;
    }

    public Product searchProduct(String productID) {
        for (Product product : products) {
            if (product.getID().equals(productID)) {
                return product;
            }
        }
        return null;
    }

    public Iterator<Product> getAllProducts() {
        return products.iterator();
    }
}
