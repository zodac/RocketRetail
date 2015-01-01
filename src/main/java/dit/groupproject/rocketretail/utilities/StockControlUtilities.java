package dit.groupproject.rocketretail.utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Supplier;

/**
 * A class that is used to model <code>StockControlUtilities</code>
 */
public class StockControlUtilities {

    private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    private static ArrayList<OrderedItem> orderToSupplier;

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

        for (final Supplier supplier : Database.getSuppliers()) {
            for (Product p : productsToReplenish) {
                if (p.getSupplierId() == supplier.getSupplierId()) {
                    createOrderedItem(percentage, p);
                }
            }

            if (!orderToSupplier.isEmpty()) {
                createAndCompleteSupplierOrder(supplier.getSupplierId());
            }
        }
    }

    private static void createOrderedItem(Double percentageThreshold, Product p) {

        int numberToOrder = getOrderAmount(percentageThreshold, p);

        OrderedItem item = new OrderedItem(p, numberToOrder);
        orderToSupplier.add(item);
    }

    private static int getOrderAmount(Double percentageThreshold, Product p) {

        double percentageOfMaxStock = calculatePercentage(p.getStockLevel(), p.getMaxLevel());
        double percentIncreaseToOrder = (percentageThreshold - percentageOfMaxStock) / 100;

        int orderNumber = (int) (p.getMaxLevel() * percentIncreaseToOrder);

        if (((orderNumber + p.getStockLevel()) * 100) / p.getMaxLevel() < percentageThreshold) {
            orderNumber++;
        }

        return orderNumber;
    }

    private static void createAndCompleteSupplierOrder(final int supplierId) {
        final Order newOrder = new Order(supplierId, DATE_FORMATTER.format(new Date()), orderToSupplier, true);
        final String currentDate = DATE_FORMATTER.format(new Date());

        Database.addOrder(newOrder);
        completeOrder(newOrder.getOrderId(), currentDate);
        setSupplierLastPurchaseDate(supplierId, currentDate);

        orderToSupplier = new ArrayList<OrderedItem>();
    }

    private static void completeOrder(final int idOfOrderToComplete, final String currentDate) {
        final Order orderToComplete = Database.getOrderById(idOfOrderToComplete);
        orderToComplete.completeOrder(currentDate);
    }

    private static void setSupplierLastPurchaseDate(final int idOfSupplierToUpdate, final String currentDate) {
        final Supplier supplierToUpdate = Database.getSupplierById(idOfSupplierToUpdate);
        supplierToUpdate.setLastPurchase(currentDate);
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
