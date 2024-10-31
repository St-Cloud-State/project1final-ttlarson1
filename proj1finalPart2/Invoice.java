import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Invoice {

    private static class InvoiceItem {
        private String productID;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double totalPrice;

        public InvoiceItem(String productID, String productName, int quantity, double unitPrice) {
            this.productID = productID;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.totalPrice = quantity * unitPrice;
        }

        @Override
        public String toString() {
            return "Product ID: " + productID +
                   " | Product: " + productName +
                   " | Quantity: " + quantity +
                   " | Unit Price: " + unitPrice +
                   " | Total: " + totalPrice;
        }
    }

    private String invoiceID;
    private String clientID;
    private String clientName;
    private LocalDate date;
    private List<InvoiceItem> items;
    private double totalAmount;

    private static List<Invoice> allInvoices = new ArrayList<>();

    public Invoice(String clientID, String clientName, LocalDate date) {
        this.invoiceID = UUID.randomUUID().toString();
        this.clientID = clientID;
        this.clientName = clientName;
        this.date = date;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public void addItem(Product product, int quantity) {
        InvoiceItem item = new InvoiceItem(product.getID(), product.getName(), quantity, product.getPrice());
        items.add(item);
        totalAmount += item.totalPrice;
    }

    public void finalizeInvoice() {
        allInvoices.add(this);
    }

    public static List<Invoice> getInvoicesByClientID(String clientID) {
        List<Invoice> clientInvoices = new ArrayList<>();
        for (Invoice invoice : allInvoices) {
            if (invoice.clientID.equals(clientID)) {
                clientInvoices.add(invoice);
            }
        }
        return clientInvoices;
    }

    public String getInvoiceDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Invoice ID: ").append(invoiceID).append("\n")
               .append("Client: ").append(clientName).append(" (ID: ").append(clientID).append(")\n")
               .append("Date: ").append(date).append("\n")
               .append("Items:\n");
        for (InvoiceItem item : items) {
            details.append("  ").append(item.toString()).append("\n");
        }
        details.append("Total Amount: ").append(totalAmount).append("\n");
        return details.toString();
    }
}