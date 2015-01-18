package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.Dates.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;

import java.util.ArrayList;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * The DateSort class fills two 2D arrays ({@link #customerOrderDates} and
 * {@link #supplierOrderDates}) with sales and purchases by year and month.
 * These two arrays can be used by other classes for graphing or printing out
 * sales/purchases over a period of time.
 */
public class DateSort {

    public static double customerOrderDates[][];
    public static double supplierOrderDates[][];

    private static ArrayList<Order> tempOrdersArrayList = new ArrayList<>();

    /**
     * Copies orders from {@link ShopDriver#orders} into
     * {@link #tempOrdersArrayList} and sorts by month and year. Initialises
     * {@link #customerOrderDates} and {@link #supplierOrderDates}, then calls
     * {@link #indexTotal(int, int)}
     * 
     * @param yearStart
     *            an integer defining the start date for sales/purchases to be
     *            accessed
     * @param yearEnd
     *            an integer defining the end date for sales/purchases to be
     *            accessed
     * 
     * @see ShopDriver#orders
     * @see #sortMonthOrderDate()
     * @see #sortYearOrderDate()
     * @see #indexTotal(int, int)
     */
    public static void sortDate(int yearStart, int yearEnd) {
        tempOrdersArrayList = new ArrayList<Order>();

        for (final Entity order : Database.getOrders()) {
            tempOrdersArrayList.add((Order) order);
        }

        customerOrderDates = new double[YEARS_AS_NUMBERS.length - 1][12];
        supplierOrderDates = new double[YEARS_AS_NUMBERS.length - 1][12];

        sortMonthOrderDate();
        sortYearOrderDate();
        indexTotal(yearStart, yearEnd);
    }

    private static void sortYearOrderDate() {
        double lowestYear = 9999;
        ArrayList<Order> tempYearArrayList = new ArrayList<Order>();
        int orderMonthIndex = 0;

        while (tempOrdersArrayList.size() != 0) {
            for (final Order order : tempOrdersArrayList) {
                final int orderYear = Integer.parseInt(order.getOrderDate().substring(6, 10));

                if (lowestYear > orderYear) {
                    lowestYear = orderYear;
                }

                if (lowestYear == orderYear) {
                    tempYearArrayList.add(order);
                    orderMonthIndex = tempOrdersArrayList.indexOf(order);
                    break;
                }
            }
            tempOrdersArrayList.remove(orderMonthIndex);
            lowestYear = 9999;
        }

        tempOrdersArrayList.clear();

        for (final Order order : tempYearArrayList) {
            tempOrdersArrayList.add(order);
        }
    }

    private static void sortMonthOrderDate() {
        double lowestMonth = 9999;
        ArrayList<Order> tempMonthArrayList = new ArrayList<Order>();
        int orderMonthIndex = 0;

        while (tempOrdersArrayList.size() != 0) {
            for (final Order order : tempOrdersArrayList) {
                final int orderMonth = Integer.parseInt(order.getOrderDate().substring(3, 5));

                if (lowestMonth > orderMonth) {
                    lowestMonth = orderMonth;
                }

                if (lowestMonth == orderMonth) {
                    tempMonthArrayList.add(order);
                    orderMonthIndex = tempOrdersArrayList.indexOf(order);
                    break;
                }
            }
            tempOrdersArrayList.remove(orderMonthIndex);
            lowestMonth = 9999;
        }
        tempOrdersArrayList.clear();

        for (final Order order : tempMonthArrayList) {
            tempOrdersArrayList.add(order);
        }
    }

    private static void indexTotal(int yearStart, int yearEnd) {
        ArrayList<Order> yearList = new ArrayList<Order>();
        ArrayList<Order> monthList = new ArrayList<Order>();

        double custTotal = 0;
        double suppTotal = 0;
        for (int i = (yearStart - YEAR_START); i < (yearEnd - YEAR_START); i++) {
            yearList = new ArrayList<Order>();
            for (int x = 0; x < tempOrdersArrayList.size(); x++) {
                if (Integer.parseInt(tempOrdersArrayList.get(x).getOrderDate().substring(6, 10)) - YEAR_START == i) {
                    yearList.add(tempOrdersArrayList.get(x));
                }
            }

            for (int j = 0; j < 12; j++) {
                custTotal = 0;
                suppTotal = 0;
                monthList = new ArrayList<Order>();

                for (int x = 0; x < yearList.size(); x++) {
                    if (Integer.parseInt(yearList.get(x).getOrderDate().substring(3, 5)) == (j + 1)) {
                        monthList.add(yearList.get(x));
                    }
                }

                for (final Order o : monthList) {
                    for (int k = 0; k < o.getOrderedItems().size(); k++) {
                        if (o.isSupplier())
                            suppTotal += o.getOrderedItems().get(k).getProduct().getCostPrice() * o.getOrderedItems().get(k).getQuantity();
                        else
                            custTotal += o.getOrderedItems().get(k).getProduct().getSalePrice() * o.getOrderedItems().get(k).getQuantity();
                    }
                }

                supplierOrderDates[i][j] = (-1) * suppTotal;
                customerOrderDates[i][j] = custTotal;
            }
        }
    }
}
