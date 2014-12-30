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

    public Order(int staffID, int traderID, String orderDate, ArrayList<OrderedItem> orderedItems, boolean isActive) {

        this.orderId = IdManager.nextOrderId.getAndIncrement();
        this.staffId = staffID;
        this.traderId = traderID;
        this.orderDate = orderDate;
        this.orderedItems = orderedItems;
        this.isActive = isActive;

        if (traderID >= IdManager.CUSTOMER_ID_START && traderID < IdManager.PRODUCT_ID_START) {
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
            if (s.getSupplierId() == traderID) {
                try {
                    if (f.parse(s.getLastPurchase()).compareTo(f.parse(orderDate)) < 0)
                        s.setLastPurchase(orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Customer c : ShopDriver.getCustomers()) {
            if (c.getCustomerId() == traderID) {
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

    public boolean includesProduct(Product p) {

        for (OrderedItem oi : this.orderedItems) {
            if (oi.getProduct().getProductId() == p.getProductId())
                return true;
        }

        return false;
    }

    public void completeOrder(String deliveryDate) {

        this.deliveryDate = deliveryDate;

        for (OrderedItem oi : orderedItems) {
            if (this.isSupplier()) // ifSupplier
                oi.getProduct().setStockLevel(oi.getProduct().getStockLevel() + oi.getQuantity());
        }

        this.isActive = false;
    }

    public int getOrderID() {
        return orderId;
    }

    public void setOrderID(int orderID) {
        this.orderId = orderID;
    }

    public int getStaffID() {
        return staffId;
    }

    public void setStaffID(int staffID) {
        this.staffId = staffID;
    }

    public int getTraderID() {
        return traderId;
    }

    public void setTraderID(int traderID) {
        this.traderId = traderID;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public ArrayList<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(ArrayList<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean isSupplier) {
        this.isSupplier = isSupplier;
    }
}
