package dit.groupproject.rocketretail.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.main.ShopDriver;

public class Order {

    public ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();

    private final static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    private int orderId;
    private int staffId;
    private int traderId;
    private String orderDate;
    private String deliveryDate = "";
    private boolean isSupplier;
    private boolean isActive = true;

    public Order(final int traderId, final String orderDate, final ArrayList<OrderedItem> orderedItems,
            final boolean isActive) {

        this.orderId = IdManager.getOrderIdAndIncrement();
        this.staffId = ShopDriver.getCurrentStaff().getStaffId();
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
            for (final OrderedItem orderedItem : orderedItems) {
                for (Product p : Database.getProducts()) {
                    if (orderedItem.getProduct().getProductId() == p.getProductId())
                        p.setStockLevel(p.getStockLevel() + orderedItem.getQuantity());
                }
            }
        }

        for (final Supplier supplier : Database.getSuppliers()) {
            if (supplier.getSupplierId() == traderId) {
                try {
                    if (DATE_FORMATTER.parse(supplier.getLastPurchase()).compareTo(DATE_FORMATTER.parse(orderDate)) < 0)
                        supplier.setLastPurchase(orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for (final Customer customer : Database.getCustomers()) {
            if (customer.getCustomerId() == traderId) {
                try {
                    if (DATE_FORMATTER.parse(customer.getLastPurchase()).compareTo(DATE_FORMATTER.parse(orderDate)) < 0)
                        customer.setLastPurchase(orderDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        if (!isActive)
            this.deliveryDate = orderDate;
    }

    public String printDetails() {
        final String suppOrCustLabel = isSupplierId() ? "Supplier" : "Customer";

        String output = "Order ID:\t" + orderId + "\nStaff ID:\t" + staffId + "\n" + suppOrCustLabel + " ID:\t"
                + traderId + "\nOrder Date:\t" + orderDate;

        for (final OrderedItem oi : orderedItems) {
            output += "\nProduct ID:\t" + oi.getProduct().getProductId() + "\nQuantity:\t" + oi.getQuantity();
        }

        return (output + "\n\n");
    }

    private boolean isSupplierId() {
        return traderId >= IdManager.SUPPLIER_ID_START && traderId < IdManager.CUSTOMER_ID_START;
    }

    public double getTotalCost() {

        double totalCost = 0;

        for (final OrderedItem oi : this.orderedItems) {
            totalCost += oi.getQuantity() * oi.getProduct().getCostPrice();
        }

        return totalCost;
    }

    public double getTotalSale() {
        double totalSale = 0;

        for (final OrderedItem oi : this.orderedItems) {
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

        for (final OrderedItem orderedItem : orderedItems) {
            if (isSupplier()) {
                orderedItem.getProduct().setStockLevel(
                        orderedItem.getProduct().getStockLevel() + orderedItem.getQuantity());
            }
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
