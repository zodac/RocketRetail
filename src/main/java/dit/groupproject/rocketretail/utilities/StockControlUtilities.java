package dit.groupproject.rocketretail.utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * A class that is used to model <code>StockControlUtilities</code>
 */
public class StockControlUtilities {

    static ArrayList<OrderedItem> orderToSupplier;

    /**
     * calculates the percentage of stock level for a product given the current
     * level and maximum allowable level
     * 
     * @param stockLevel
     *            (int)
     * @param maxLevel
     *            (int)
     * */
    public static Double calculatePercentage(int stockLevel, int maxLevel) {
        double percentage;
        double stock = (double) stockLevel;
        double max = (double) maxLevel;
        double percentValue = (stock / max) * 100;

        percentage = new Double(percentValue);

        return percentage;
    }

    /**
     * Replenishes stock levels based on the percentage of stock level they are
     * to be increased to. If a product is below 25% and the user selects 25%
     * that product will be replenished to 25%
     * 
     * @param percentage
     *            (double) current percentage of stock to order
     * @param productsToReplenish
     *            (ArrayList) the list of products that need to be replenished
     * @see StockControlUtilities#createOrderedItem(Double, Product)
     * @see StockControlUtilities#createAndCompleteSupplierOrder(Supplier)
     * */
    public static void replenishStocks(Double percentage, ArrayList<Product> productsToReplenish) {

        orderToSupplier = new ArrayList<OrderedItem>();

        for (Supplier s : ShopDriver.getSuppliers()) {
            for (Product p : productsToReplenish) {
                if (p.getSupplierID() == s.getSupplierID()) {
                    createOrderedItem(percentage, p);
                }
            }

            if (!orderToSupplier.isEmpty()) {
                createAndCompleteSupplierOrder(s);
            }
        }
    }

    /**
     * creates the arrayList of items and quantities to be ordered
     * 
     * @param percentageThreshold
     *            (double)
     * @param p
     *            (Product)
     * @see StockControlUtilities#getOrderAmount(Double, Product)
     * */
    private static void createOrderedItem(Double percentageThreshold, Product p) {

        int numberToOrder = getOrderAmount(percentageThreshold, p);

        OrderedItem item = new OrderedItem(p, numberToOrder);
        orderToSupplier.add(item);
    }

    /**
     * decides the quantity of a product to be ordered based on the current %
     * stock level and the desired % stock level.
     * 
     * @param percentageThreshold
     *            (double)
     * @param p
     *            (Product)
     * */
    private static int getOrderAmount(Double percentageThreshold, Product p) {

        double percentageOfMaxStock = calculatePercentage(p.getStockLevel(), p.getMaxLevel());
        double percentIncreaseToOrder = (percentageThreshold - percentageOfMaxStock) / 100;

        int orderNumber = (int) (p.getMaxLevel() * percentIncreaseToOrder);

        if (((orderNumber + p.getStockLevel()) * 100) / p.getMaxLevel() < percentageThreshold)
            orderNumber++;

        return orderNumber;
    }

    /**
     * creates and completes supplier orders
     * 
     * @param s
     *            (Supplier)
     * */
    private static void createAndCompleteSupplierOrder(Supplier s) {

        Order order = new Order(ShopDriver.getOrders().size(), ShopDriver.currentStaff.getStaffID(), s.getSupplierID(),
                new SimpleDateFormat("dd/MM/yyyy").format(new Date()), orderToSupplier, true);
        ShopDriver.getOrders().add(order);

        for (Order o : ShopDriver.getOrders()) {
            if (o.getOrderID() == order.getOrderID())
                o.completeOrder(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        }

        for (Supplier supp : ShopDriver.getSuppliers()) {
            if (supp.getSupplierID() == s.getSupplierID()) {
                supp.setLastPurchase(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            }
        }

        orderToSupplier = new ArrayList<OrderedItem>();
    }

    /**
     * returns the value of ArrayList orderToSupplier
     * 
     * @return orderToSupplier (ArrayList)
     */
    public static ArrayList<OrderedItem> getOrderToSupplier() {
        return orderToSupplier;
    }

    /**
     * changes the value of ArrayList orderToSupplier
     * 
     * @param orderToSupplier
     *            (ArrayList)
     */
    public static void setOrderToSupplier(ArrayList<OrderedItem> orderToSupplier) {
        StockControlUtilities.orderToSupplier = orderToSupplier;
    }
}
