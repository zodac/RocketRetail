package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.DateHandler.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

import java.util.ArrayList;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * The DateSort class fills two 2D arrays ({@link #custDates} and
 * {@link #suppDates}) with sales and purchases by year and month. These two
 * arrays can be used by other classes for graphing or printing out
 * sales/purchases over a period of time.
 */
public class DateSort {

    public static double custDates[][] = new double[YEARS_AS_NUMBERS.length - 1][12];
    public static double suppDates[][] = new double[YEARS_AS_NUMBERS.length - 1][12];

    private static ArrayList<Order> tempOrdersArrayList = new ArrayList<Order>();

    /**
     * Copies orders from {@link ShopDriver#orders} into
     * {@link #tempOrdersArrayList} and sorts by month and year. Initialises
     * {@link #custDates} and {@link #suppDates}, then calls
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
        for (final Order order : Database.getOrders()) {
            tempOrdersArrayList.add(order);
        }

        custDates = new double[YEARS_AS_NUMBERS.length - 1][12];
        suppDates = new double[YEARS_AS_NUMBERS.length - 1][12];

        sortMonthOrderDate();
        sortYearOrderDate();
        indexTotal(yearStart, yearEnd);
    }

    /**
     * Sorts orders in {@link #tempOrdersArrayList} by month.
     */
    public static void sortYearOrderDate() {
        double lowestYear = 9999;
        ArrayList<Order> tempYearArrayList = new ArrayList<Order>();
        int indexY = 0;
        tempYearArrayList.clear();
        boolean found;

        while (tempOrdersArrayList.size() != 0) {
            for (Order s : tempOrdersArrayList) {
                if (lowestYear > Integer.parseInt(s.getOrderDate().substring(6, 10))) {
                    lowestYear = Integer.parseInt(s.getOrderDate().substring(6, 10));
                }
            }
            found = false;
            for (Order s : tempOrdersArrayList) {
                if (lowestYear == Integer.parseInt(s.getOrderDate().substring(6, 10)) && !found) {
                    tempYearArrayList.add(s);
                    indexY = tempOrdersArrayList.indexOf(s);
                    found = true;
                }
                if (found)
                    break;
            }
            tempOrdersArrayList.remove(indexY);
            lowestYear = 9999;
        }
        tempOrdersArrayList.clear();
        for (int i = 0; i < tempYearArrayList.size(); i++) {
            tempOrdersArrayList.add(tempYearArrayList.get(i));
        }
    }

    /**
     * Sorts orders in {@link #tempOrdersArrayList} by year.
     */
    public static void sortMonthOrderDate() {
        double lowestMonth = 9999;
        ArrayList<Order> tempMonthArrayList = new ArrayList<Order>();
        int indexM = 0;
        tempMonthArrayList.clear();
        boolean found;

        while (tempOrdersArrayList.size() != 0) {
            for (Order s : tempOrdersArrayList) {
                if (lowestMonth > Integer.parseInt(s.getOrderDate().substring(3, 5))) {
                    lowestMonth = Integer.parseInt(s.getOrderDate().substring(3, 5));
                }
            }
            found = false;
            for (Order s : tempOrdersArrayList) {
                if (lowestMonth == Integer.parseInt(s.getOrderDate().substring(3, 5)) && !found) {
                    tempMonthArrayList.add(s);
                    indexM = tempOrdersArrayList.indexOf(s);
                    found = true;
                }
                if (found)
                    break;
            }
            tempOrdersArrayList.remove(indexM);
            lowestMonth = 9999;
        }
        tempOrdersArrayList.clear();
        for (int i = 0; i < tempMonthArrayList.size(); i++) {
            tempOrdersArrayList.add(tempMonthArrayList.get(i));
        }
    }

    /**
     * Cycles through {@link ShopDriver#orders} and sorts sales and purchases
     * into {@link #custDates} and {@link #suppDates} by year and month.
     * Purchases are negative.
     * 
     * @param yearStart
     *            an integer defining the start date for sales/purchases to be
     *            accessed
     * @param yearEnd
     *            an integer defining the end date for sales/purchases to be
     *            accessed
     */
    public static void indexTotal(int yearStart, int yearEnd) {

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

                for (Order o : monthList) {
                    for (int k = 0; k < o.getOrderedItems().size(); k++) {
                        if (o.isSupplier())
                            suppTotal += o.getOrderedItems().get(k).getProduct().getCostPrice()
                                    * o.getOrderedItems().get(k).getQuantity();
                        else
                            custTotal += o.getOrderedItems().get(k).getProduct().getSalePrice()
                                    * o.getOrderedItems().get(k).getQuantity();
                    }
                }

                suppDates[i][j] = (-1) * suppTotal;
                custDates[i][j] = custTotal;
            }
        }
    }
}
