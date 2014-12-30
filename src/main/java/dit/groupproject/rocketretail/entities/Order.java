package dit.groupproject.rocketretail.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dit.groupproject.rocketretail.main.ShopDriver;

public class Order {

    private int orderId;
    private int staffId;
    private int traderId;
    private String orderDate;
    private String deliveryDate = "";
    private boolean isSupplier;
    private boolean isActive = true;
    public ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();

    public Order(final int staffId, final int traderId, final String orderDate,
            final ArrayList<OrderedItem> orderedItems, final boolean isActive) {

        this.orderId = IdManager.getOrderIdAndIncrement();
        this.staffId = staffId;
        this.traderId = traderId;
        this.orderDate = orderDate;
        this.orderedItems = orderedItems;
        this.isActive = isActive;

        if (traderId >= IdManager.CUSTOMER_ID_START && traderId < IdManager.PRODUCT_ID_START) {
            isSupplier = false;
        } else {
            isSupplier = true;
        }

        for (OrderedItem x : orderedItems) {
            // If Customer order, reduce stock level
            if (!isSupplier) {
                if (x.getProduct().getStockLevel() >= x.getQuantity()) {
                    x.getProduct().setStockLevel(x.getProduct().getStockLevel() - x.getQuantity());
                }
            }
        }

        if (!isActive && isSupplier) {
            for (OrderedItem oi : orderedItems) {
                for (Product p : ShopDriver.getProducts()) {
                    if (oi.getProduct().getProductId() == p.getProductId())
                        p.setStockLevel(p.getStockLevel() + oi.getQuantity());
                }
            }
        }

        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        for (Supplier s : ShopDriver.getSuppliers()) {
            if (s.getSupplierId() == traderId) {
                try {
                    if (f.parse(s.getLastPurchase()).compareTo(f.parse(orderDate)) < 0)
                        s.setLastPurchase(orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Customer c : ShopDriver.getCustomers()) {
            if (c.getCustomerId() == traderId) {
                try {
                    if (f.parse(c.getLastPurchase()).compareTo(f.parse(orderDate)) < 0)
                        c.setLastPurchase(orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        if (!isActive)
            this.deliveryDate = orderDate;
    }

    public String printDetails() {
        String suppOrCustLabel = "";

        if (traderId >= IdManager.SUPPLIER_ID_START && traderId < IdManager.CUSTOMER_ID_START) {
            suppOrCustLabel = "Supplier";
        } else if (traderId >= IdManager.CUSTOMER_ID_START && traderId < IdManager.PRODUCT_ID_START) {
            suppOrCustLabel = "Customer";
        }

        String output = "Order ID:\t" + orderId + "\nStaff ID:\t" + staffId + "\n" + suppOrCustLabel + " ID:\t"
                + traderId + "\nOrder Date:\t" + orderDate;

        for (OrderedItem oi : orderedItems) {
            output = output + "\nProduct ID:\t" + oi.getProduct().getProductId() + "\nQuantity:\t" + oi.getQuantity();
        }

        return (output + "\n\n");
    }

    public double getTotalCost() {

        double totalCost = 0;

        for (OrderedItem oi : this.orderedItems) {
            totalCost += oi.getQuantity() * oi.getProduct().getCostPrice();
        }

        return totalCost;
    }

    public double getTotalSale() {
        double totalSale = 0;

        for (OrderedItem oi : this.orderedItems) {
            totalSale += oi.getQuantity() * oi.getProduct().getSalePrice();
        }

        return totalSale;
    }

    public boolean includesProduct(final Product product) {

        for (OrderedItem oi : this.orderedItems) {
            if (oi.getProduct().getProductId() == product.getProductId()) {
                return true;
            }
        }

        return false;
    }

    public void completeOrder(final String deliveryDate) {

        this.deliveryDate = deliveryDate;

        for (OrderedItem oi : orderedItems) {
            if (this.isSupplier()) // ifSupplier
                oi.getProduct().setStockLevel(oi.getProduct().getStockLevel() + oi.getQuantity());
        }

        this.isActive = false;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getTraderId() {
        return traderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public ArrayList<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public boolean isSupplier() {
        return isSupplier;
    }
}
