package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.Formatters.DATE_FORMATTER;

import java.util.ArrayList;
import java.util.Date;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Supplier;

/**
 * A class that is used to model <code>StockControlUtilities</code>
 */
public class StockControlUtilities {

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
    public static double calculatePercentage(final int stockLevel, final int maxLevel) {
        return (stockLevel / maxLevel) * 100;
    }

    /**
     * Replenishes stock levels based on the percentage of stock level they are
     * to be increased to. If a product is below 25% and the user selects 25%
     * that product will be replenished to 25%
     * 
     * @param percentagetoOrder
     *            (double) current percentage of stock to order
     * @param productsToReplenish
     *            (ArrayList) the list of products that need to be replenished
     * @see StockControlUtilities#createOrderedItem(double, Product)
     * @see StockControlUtilities#createAndCompleteSupplierOrder(Supplier)
     * */
    public static void replenishStocks(final double percentagetoOrder, final ArrayList<Product> productsToReplenish) {

        orderToSupplier = new ArrayList<OrderedItem>();

        for (final Entity supplier : Database.getSuppliers()) {
            for (final Product p : productsToReplenish) {
                if (p.getSupplierId() == supplier.getId()) {
                    createOrderedItem(percentagetoOrder, p);
                }
            }

            if (!orderToSupplier.isEmpty()) {
                createAndCompleteSupplierOrder(supplier.getId());
            }
        }
    }

    private static void createOrderedItem(final double percentageThreshold, final Product product) {
        final int numberToOrder = getOrderAmount(percentageThreshold, product);

        OrderedItem item = new OrderedItem(product, numberToOrder);
        orderToSupplier.add(item);
    }

    private static int getOrderAmount(Double percentageThreshold, Product p) {

        double percentageOfMaxStock = calculatePercentage(p.getCurrentStockLevel(), p.getMaxStockLevel());
        double percentIncreaseToOrder = (percentageThreshold - percentageOfMaxStock) / 100;

        int orderNumber = (int) (p.getMaxStockLevel() * percentIncreaseToOrder);

        if (((orderNumber + p.getCurrentStockLevel()) * 100) / p.getMaxStockLevel() < percentageThreshold) {
            orderNumber++;
        }

        return orderNumber;
    }

    private static void createAndCompleteSupplierOrder(final int supplierId) {
        final Order newOrder = new Order(supplierId, DATE_FORMATTER.format(new Date()), orderToSupplier, true);
        final String currentDate = DATE_FORMATTER.format(new Date());

        Database.addOrder(newOrder);
        completeOrder(newOrder.getId(), currentDate);
        setSupplierLastPurchaseDate(supplierId, currentDate);

        orderToSupplier = new ArrayList<OrderedItem>();
    }

    private static void completeOrder(final int idOfOrderToComplete, final String currentDate) {
        final Order orderToComplete = (Order) Database.getOrderById(idOfOrderToComplete);
        orderToComplete.completeOrder(currentDate);
    }

    private static void setSupplierLastPurchaseDate(final int idOfSupplierToUpdate, final String currentDate) {
        final Supplier supplierToUpdate = (Supplier) Database.getSupplierById(idOfSupplierToUpdate);
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
    public static void setOrderToSupplier(final ArrayList<OrderedItem> orderToSupplier) {
        StockControlUtilities.orderToSupplier = orderToSupplier;
    }
}
