package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.Dates.MONTHS;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_CURRENT;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;
import static dit.groupproject.rocketretail.utilities.Formatters.CURRENCY_FORMATTER;
import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * ProfitLoss contains the methods to create P/L reports.<br />
 * Return strings to be displayed on-screen in JTextAreas.
 */
public class ProfitLoss {

    /**
     * Cycles through {@link ShopDriver#orders} and adds the total values for
     * sales, purchases and gross profit to a String.
     * 
     * @return the String with the total values of sales, purchases and gross
     *         profit of the store
     */
    public static String createTotals() {
        double loss = 0;
        double profit = 0;

        for (final Entity o : Database.getOrders()) {
            final Order order = (Order) o;

            if (order.isSupplier()) {
                for (final OrderedItem oi : order.getOrderedItems()) {
                    loss += oi.getProduct().getCostPrice() * oi.getQuantity();
                }
            } else {
                for (final OrderedItem oi : order.getOrderedItems()) {
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
        result.append("Totals\n=====\nSales:\t\t€" + CURRENCY_FORMATTER.format(profit) + "\nPurchases:\t€" + CURRENCY_FORMATTER.format(loss)
                + "\n============================" + profitOutput.toString());

        return result.toString();
    }

    /**
     * Cycles through the two 2D arrays {@link DateSort#customerOrderDates} and
     * {@link DateSort#supplierOrderDates}, and appends the monthly totals to a
     * StringBuilder.
     * 
     * @return a StringBuilder with the breakdown of sales and purchases by year
     *         and month
     */
    public static String createAdvancedReport() {
        final char tab = '\t';
        final char newLine = '\n';
        final char euroSign = '€';

        final StringBuilder budget = new StringBuilder();
        budget.append("Year").append(tab).append("Month").append(tab).append("Purchases").append(tab).append("Sales").append(newLine)
                .append("_____________________________________").append(newLine);
        DateSort.sortDate(YEAR_START, YEAR_CURRENT + 1);

        int yearIndex = YEAR_START;
        for (int year = 0; year <= YEAR_CURRENT - YEAR_START; year++) {

            for (int month = 0; month < 12; month++) {
                final double purchase = -1 * DateSort.supplierOrderDates[year][month];
                final double sale = DateSort.customerOrderDates[year][month];

                if (month == 0) {
                    budget.append(yearIndex++);
                }

                budget.append(tab).append(MONTHS[month]).append(tab).append(euroSign).append(CURRENCY_FORMATTER.format(purchase));

                if (purchase >= 1000) {
                    budget.append(tab);
                }

                budget.append(tab).append(euroSign).append(CURRENCY_FORMATTER.format(sale)).append(newLine);
            }
        }
        return budget.toString();
    }
}