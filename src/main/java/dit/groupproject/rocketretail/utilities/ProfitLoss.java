package dit.groupproject.rocketretail.utilities;

import java.text.DecimalFormat;

import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * ProfitLoss contains the methods to create P/L reports.<br />
 * Return strings to be displayed on-screen in JTextAreas.
 */
public class ProfitLoss {

    // Class variables
    /**
     * Array of month names as Strings.
     */
    private static String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",
            "Dec" };

    // Methods
    /**
     * Cycles through {@link ShopDriver#orders} and adds the total values for
     * sales, purchases and gross profit to a String.
     * 
     * @return the String with the total values of sales, purchases and gross
     *         profit of the store
     * 
     * @see ShopDriver#orders
     */
    public static String createTotals() {
        DecimalFormat doubleFormatter = new DecimalFormat("#,###,##0.00");
        double loss = 0;
        double profit = 0;

        for (Order o : ShopDriver.getOrders()) {

            // Supplier
            if (o.isSupplier()) {
                for (OrderedItem oi : o.getOrderedItems()) {
                    loss += oi.getProduct().getCostPrice() * oi.getQuantity();
                }
            }

            // Customer
            else if (!o.isSupplier()) {
                for (OrderedItem oi : o.getOrderedItems()) {
                    profit += oi.getProduct().getSalePrice() * oi.getQuantity();
                }
            }
        }

        String profitOutput = "";
        if (profit - loss < 0)
            profitOutput = "\nGross Profit:\t-€" + doubleFormatter.format((-1) * (profit - loss));
        else
            profitOutput = "\nGross Profit:\t€" + doubleFormatter.format(profit - loss);

        String result = "Totals\n=====\nSales:\t\t€" + doubleFormatter.format(profit) + "\nPurchases:\t€"
                + doubleFormatter.format(loss) + "\n============================" + profitOutput;

        return result;

    }

    /**
     * Cycles through the two 2D arrays {@link DateSort#custDates} and
     * {@link DateSort#suppDates}, and appends the monthly totals to a
     * StringBuilder.
     * 
     * @return a StringBuilder with the breakdown of sales and purchases by year
     *         and month
     * 
     * @see DateSort#custDates
     * @see DateSort#suppDates
     * @see DateSort#SortDate(int, int)
     */
    public static StringBuilder createAdvancedReport() {
        DecimalFormat doubleFormatter = new DecimalFormat("#,###,##0.00");
        StringBuilder budget = new StringBuilder();
        budget.append("Year\tMonth\tPurchases\tSales\n_____________________________________\n");

        int year = ShopDriver.yearStart;

        DateSort.SortDate(year, ShopDriver.yearCurrent + 1);

        for (int i = 0; i < ShopDriver.yearCurrent - ShopDriver.yearStart + 1; i++) {

            for (int j = 0; j < 12; j++) {
                double purchase = -1 * DateSort.suppDates[i][j];
                double sale = DateSort.custDates[i][j];

                if (j == 0)
                    budget.append(year);

                if (doubleFormatter.format(purchase).length() == 6)
                    budget.append("\t" + months[j] + "\t€" + doubleFormatter.format(purchase) + "\t\t€"
                            + doubleFormatter.format(sale) + "\n");

                else if (doubleFormatter.format(purchase).length() >= 8)
                    budget.append("\t" + months[j] + "\t€" + doubleFormatter.format(purchase) + "\t€"
                            + doubleFormatter.format(sale) + "\n");

                else
                    budget.append("\t" + months[j] + "\t€" + doubleFormatter.format(purchase) + "\t\t€"
                            + doubleFormatter.format(sale) + "\n");
            }
            year++;
        }
        return budget;
    }
}