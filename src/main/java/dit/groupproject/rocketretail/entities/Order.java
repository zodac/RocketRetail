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

    public Order(final int traderId, final String orderDate, final ArrayList<OrderedItem> orderedItems, final boolean isActive) {

        this.orderId = IdManager.getOrderIdAndIncrement();
        this.staffId = ShopDriver.getCurrentStaff().getId();
        this.traderId = traderId;
        this.orderDate = orderDate;
        this.orderedItems = orderedItems;
        this.isActive = isActive;
        this.isSupplier = traderId >= IdManager.SUPPLIER_ID_START && traderId < IdManager.CUSTOMER_ID_START;

        for (final OrderedItem orderedItem : orderedItems) {
            if (!isSupplier) {
                if (orderedItem.getProduct().getStockLevel() >= orderedItem.getQuantity()) {
                    orderedItem.getProduct().setStockLevel(orderedItem.getProduct().getStockLevel() - orderedItem.getQuantity());
                }
            }
        }

        if (!isActive) {
            this.deliveryDate = orderDate;

            if (isSupplier) {
                for (final OrderedItem orderedItem : orderedItems) {
                    final Product product = (Product) Database.getProductById(orderedItem.getProduct().getId());
                    product.setStockLevel(product.getStockLevel() + orderedItem.getQuantity());
                }
            }
        }

        if (isSupplier) {
            final Supplier supplier = (Supplier) Database.getSupplierById(traderId);
            try {
                if (DATE_FORMATTER.parse(supplier.getLastPurchase()).compareTo(DATE_FORMATTER.parse(orderDate)) < 0) {
                    supplier.setLastPurchase(orderDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            final Customer customer = (Customer) Database.getCustomerById(traderId);
            try {
                if (DATE_FORMATTER.parse(customer.getLastPurchase()).compareTo(DATE_FORMATTER.parse(orderDate)) < 0)
                    customer.setLastPurchase(orderDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public String printDetails() {
        final String suppOrCustLabel = isSupplier ? "Supplier" : "Customer";

        String output = "Order ID:\t" + orderId + "\nStaff ID:\t" + staffId + "\n" + suppOrCustLabel + " ID:\t" + traderId + "\nOrder Date:\t"
                + orderDate;

        for (final OrderedItem oi : orderedItems) {
            output += "\nProduct ID:\t" + oi.getProduct().getId() + "\nQuantity:\t" + oi.getQuantity();
        }

        return output + "\n\n";
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

        for (final OrderedItem oi : this.orderedItems) {
            if (oi.getProduct().getId() == product.getId()) {
                return true;
            }
        }

        return false;
    }

    public void completeOrder(final String deliveryDate) {

        this.deliveryDate = deliveryDate;

        for (final OrderedItem orderedItem : orderedItems) {
            if (isSupplier()) {
                orderedItem.getProduct().setStockLevel(orderedItem.getProduct().getStockLevel() + orderedItem.getQuantity());
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
