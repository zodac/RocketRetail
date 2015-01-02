package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_CURRENT;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

import java.text.DecimalFormat;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * ProfitLoss contains the methods to create P/L reports.<br />
 * Return strings to be displayed on-screen in JTextAreas.
 */
public class ProfitLoss {

    private final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,##0.00");
    private final static String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec" };

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
        double loss = 0;
        double profit = 0;

        for (final Order o : Database.getOrders()) {

            if (o.isSupplier()) {
                for (final OrderedItem oi : o.getOrderedItems()) {
                    loss += oi.getProduct().getCostPrice() * oi.getQuantity();
                }
            } else {
                for (final OrderedItem oi : o.getOrderedItems()) {
                    profit += oi.getProduct().getSalePrice() * oi.getQuantity();
                }
            }
        }

        final StringBuilder profitOutput = new StringBuilder();
        if (profit - loss < 0) {
            profitOutput.append("\nGross Profit:\t-€" + CURRENCY_FORMATTER.format((-1) * (profit - loss)));
        } else {
            profitOutput.append("\nGross Profit:\t€" + CURRENCY_FORMATTER.format(profit - loss));
        }

        final StringBuilder result = new StringBuilder();
        result.append("Totals\n=====\nSales:\t\t€" + CURRENCY_FORMATTER.format(profit) + "\nPurchases:\t€"
                + CURRENCY_FORMATTER.format(loss) + "\n============================" + profitOutput.toString());

        return result.toString();
    }

    /**
     * Cycles through the two 2D arrays {@link DateSort#customerOrderDates} and
     * {@link DateSort#supplierOrderDates}, and appends the monthly totals to a
     * StringBuilder.
     * 
     * @return a StringBuilder with the breakdown of sales and purchases by year
     *         and month
     * 
     * @see DateSort#customerOrderDates
     * @see DateSort#supplierOrderDates
     * @see DateSort#sortDate(int, int)
     */
    public static String createAdvancedReport() {
        final StringBuilder budget = new StringBuilder();
        budget.append("Year\tMonth\tPurchases\tSales\n_____________________________________\n");

        DateSort.sortDate(YEAR_START, YEAR_CURRENT + 1);

        int year = YEAR_START;
        for (int i = 0; i <= YEAR_CURRENT - YEAR_START; i++) {

            for (int j = 0; j < 12; j++) {
                final double purchase = -1 * DateSort.supplierOrderDates[i][j];
                final double sale = DateSort.customerOrderDates[i][j];

                if (j == 0) {
                    budget.append(year++);
                }

                budget.append("\t").append(MONTHS[j]).append("\t€").append(CURRENCY_FORMATTER.format(purchase));

                if (purchase >= 1000) {
                    budget.append("\t");
                }

                budget.append("\t€").append(CURRENCY_FORMATTER.format(sale)).append("\n");
            }
        }
        return budget.toString();
    }
}