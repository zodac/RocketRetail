package dit.groupproject.rocketretail.entities;

import static dit.groupproject.rocketretail.utilities.Formatters.CURRENCY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.DATE_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.main.ShopDriver;

public class Order implements Entity {

    public ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();

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
        this.isSupplier = traderId >= IdManager.SUPPLIER_ID_START && traderId < IdManager.SUPPLIER_ID_START + IdManager.MAX_ID_RANGE;

        for (final OrderedItem orderedItem : orderedItems) {
            if (!isSupplier) {
                final Product orderedProduct = orderedItem.getProduct();
                final int stockLevel = orderedProduct.getCurrentStockLevel();
                final int quantity = orderedItem.getQuantity();

                if (stockLevel >= quantity) {
                    orderedProduct.setCurrentStockLevel(stockLevel - quantity);
                }
            }
        }

        if (!isActive) {
            this.deliveryDate = orderDate;

            if (isSupplier) {
                for (final OrderedItem orderedItem : orderedItems) {
                    final Product product = (Product) Database.getProductById(orderedItem.getProduct().getId());
                    product.setCurrentStockLevel(product.getCurrentStockLevel() + orderedItem.getQuantity());
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

    public double getTotalPrice() {
        double totalValue = 0;

        if (isSupplier) {
            for (final OrderedItem oi : this.orderedItems) {
                totalValue += oi.getQuantity() * oi.getProduct().getCostPrice();
            }
        } else {
            for (final OrderedItem oi : this.orderedItems) {
                totalValue += oi.getQuantity() * oi.getProduct().getSalePrice();
            }
        }

        return totalValue;
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
                orderedItem.getProduct().setCurrentStockLevel(orderedItem.getProduct().getCurrentStockLevel() + orderedItem.getQuantity());
            }
        }

        this.isActive = false;
    }

    @Override
    public int getId() {
        return orderId;
    }

    @Override
    public void setId(final int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getName() {
        throw new IllegalArgumentException("Should not be asking for an order's name!");
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

    public static Comparator<Entity> getComparator(final String sortType) {
        if (sortType.equals("Staff ID")) {
            return compareByStaffId;
        } else if (sortType.equals("Trader ID")) {
            return compareByTraderId;
        } else if (sortType.equals("Total Price")) {
            return compareByTotalPrice;
        } else if (sortType.equals("Order Date")) {
            return compareByOrderDate;
        } else if (sortType.equals("Active")) {
            return compareByActive;
        } else {
            return compareById;
        }
    }

    private static Comparator<Entity> compareByStaffId = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Order) s1).getStaffId() - ((Order) s2).getStaffId();
        }
    };

    private static Comparator<Entity> compareByTraderId = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Order) s1).getTraderId() - ((Order) s2).getTraderId();
        }
    };

    private static Comparator<Entity> compareByTotalPrice = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return (int) (((Order) s1).getTotalPrice() - ((Order) s2).getTotalPrice());
        }
    };

    private static Comparator<Entity> compareByOrderDate = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            try {
                return DATE_FORMATTER.parse(((Order) s1).getOrderDate()).compareTo(DATE_FORMATTER.parse(((Order) s2).getOrderDate()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    private static Comparator<Entity> compareByActive = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return (((Order) s1).isActive() == ((Order) s2).isActive() ? 0 : ((Order) s1).isActive() ? 1 : -1);
        }
    };

    @Override
    public Object[] getData() {
        final Object[] data = new Object[getNumberOfFields()];
        data[0] = ID_FORMATTER.format(orderId);
        data[1] = ID_FORMATTER.format(staffId);
        data[2] = traderId;
        data[3] = CURRENCY_FORMATTER.format(getTotalPrice());
        data[4] = orderDate;
        data[5] = deliveryDate.isEmpty() ? " " : deliveryDate;

        return data;
    }

    @Override
    public int getNumberOfFields() {
        return 6;
    }
}
