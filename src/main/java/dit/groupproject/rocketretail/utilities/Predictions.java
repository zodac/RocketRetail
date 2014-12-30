package dit.groupproject.rocketretail.utilities;

import java.util.ArrayList;

import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * A class that is used to generate prediction forecasts
 */
public class Predictions {

    static double custDates[][] = new double[51][12];
    static double suppDates[][] = new double[51][12];

    private static ArrayList<Order> tempOrdersArrayList = new ArrayList<Order>();

    /**
     * A method to sort the tempOrdersArrayList by date
     * 
     * @param yearStart
     *            An integer used to determine the start year to work from
     * @param yearEnd
     *            An integer used to determine the year to work until
     */
    public static void SortDate(int yearStart, int yearEnd) {

        tempOrdersArrayList = new ArrayList<Order>();
        for (int i = 0; i < ShopDriver.getOrders().size(); i++) {
            tempOrdersArrayList.add(ShopDriver.getOrders().get(i));
        }

        custDates = new double[51][12];
        suppDates = new double[51][12];

        sortMonthOrderDate();
        sortYearOrderDate();
        indexTotal(yearStart, yearEnd);
    }

    // This method should probably be used as the Profit/Loss advanced report
    /**
     * A method used to print the order dates to console. -Divided into customer
     * orders and supplier orders.
     * 
     * @param yearStart
     *            An integer used to determine the start year to work from
     * @param yearEnd
     *            An integer used to determine the year to work until
     */
    public static void printDatesOrders(int yearStart, int yearEnd) {
        for (int i = yearStart - ShopDriver.yearStart; i < yearEnd - ShopDriver.yearStart; i++) {
            System.out.println("Year " + (i + ShopDriver.yearStart) + "\n===============");
            for (int j = 0; j < 12; j++) {
                if (custDates[i][j] != 0)
                    System.out.println("Customer: " + custDates[i][j]);
                if (suppDates[i][j] != 0)
                    System.out.println("Supplier: " + suppDates[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * A method to sort an arrayList by orderDate -by year
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
     * A method to sort an arrayList by orderDate -by month
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
     * A method used to fill the Customer 2D array and Supplier 2D arrays with
     * their relevant totals.
     * 
     * @param startYear
     *            An integer to define the start year of the method
     * @param endYear
     *            An integer to define the end year of the method
     */
    public static void indexTotal(int startYear, int endYear) {

        ArrayList<Order> yearList = new ArrayList<Order>();
        ArrayList<Order> monthList = new ArrayList<Order>();

        double custTotal = 0;
        double suppTotal = 0;
        for (int i = (startYear - ShopDriver.yearStart); i < (endYear - ShopDriver.yearStart); i++) {
            yearList = new ArrayList<Order>();
            for (int x = 0; x < tempOrdersArrayList.size(); x++) {
                if (Integer.parseInt(tempOrdersArrayList.get(x).getOrderDate().substring(6, 10)) - ShopDriver.yearStart == i) {
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

    /**
     * Creates a 2D double array for 5 years based on the start date passed into
     * it. Calls the createArrayOneYear method for each of the 5 years and
     * appends the data into the one array.
     * 
     * @param selectedYear
     *            An integer determining the end year the data is required for
     * @return Returns the 5 years data in a 2D double array
     * @see Predictions#createArrayOneYear(int)
     */
    public static double[][] createArrayFiveYear(int selectedYear) {
        double[][] array = new double[60][3];

        array = append(createArrayOneYear(selectedYear - 4), createArrayOneYear(selectedYear - 3));
        array = append(array, createArrayOneYear(selectedYear - 2));
        array = append(array, createArrayOneYear(selectedYear - 1));
        array = append(array, createArrayOneYear(selectedYear));
        return array;
    }

    /**
     * Creates a 2D double array for 1 year based on the start date passed into
     * it. (Forms an array of data for the prediction model to work on. Creates
     * an array of customer totals supplier totals and totals by month for one
     * year.)
     * 
     * @param selectedYear
     *            An integer determining the year the data is required for.
     * @return Returns the 2D double array of years data.
     */
    public static double[][] createArrayOneYear(int selectedYear) {
        double[][] dataArray = new double[12][3]; // 12 months, 3 entries per
                                                  // month (sales, purchases,
                                                  // profit/loss)
        DateSort.SortDate(selectedYear, selectedYear + 1);

        for (int i = 0; i < 12; i++) {
            double custTotal = 0, suppTotal = 0;
            custTotal += DateSort.custDates[selectedYear - ShopDriver.yearStart][i];
            suppTotal += DateSort.suppDates[selectedYear - ShopDriver.yearStart][i];

            dataArray[i][0] = custTotal;
            dataArray[i][1] = suppTotal; // Comes into this method as a negative
                                         // value
            dataArray[i][2] = custTotal + suppTotal;
        }
        return dataArray;
    }

    /**
     * A method used to determine the prediction data. Bases prediction on
     * previous 5 years data. Averages all Januarys, Februarys, Marches, Aprils,
     * etc. To develop a prediction for the first 6 months of next year.
     * 
     * @param dataArray
     *            The array of information to base the prediction on.
     * @return The 2D double data array of prediction information to model.
     */
    public static double[][] sixMthPrediction(double[][] dataArray) {
        double[][] predictionArray = new double[6][3];
        int k, j = 0;
        double total = 0;
        for (k = 0; k < 12; k++) {
            for (j = 0; j < 3; j++) {
                total += dataArray[k][j];
            }
        }
        if (total != 0) {
            for (int i = 0; i < 6; i++) {
                predictionArray[i][0] = (dataArray[i][0] + dataArray[i + 12][0] + dataArray[i + 24][0]
                        + dataArray[i + 36][0] + dataArray[i + 48][0]) / 5;
                predictionArray[i][1] = (dataArray[i][1] + dataArray[i + 12][1] + dataArray[i + 24][0]
                        + dataArray[i + 36][0] + dataArray[i + 48][0]) / 5;
                predictionArray[i][2] = (dataArray[i][2] + dataArray[i + 12][2] + dataArray[i + 24][0]
                        + dataArray[i + 36][0] + dataArray[i + 48][0]) / 5;
            }
        } else {
            for (k = 0; k < 6; k++) {
                for (j = 0; j < 3; j++) {
                    predictionArray[k][j] = dataArray[k][j];
                }
            }
        }
        return predictionArray;
    }

    /**
     * A method used to determine the prediction data. Bases prediction on
     * previous 5 years data. Averages all Januarys, Februarys, Marches, Aprils,
     * etc. To develop a prediction for the next 2 years.
     * 
     * @param dataArray
     *            The array of information to base the prediction on.
     * @return The 2D double data array of prediction information to model.
     */
    public static double[][] twoYrPrediction(double[][] dataArray) {
        double[][] predictionArray = new double[24][3];
        for (int i = 0; i < 12; i++) {
            predictionArray[i][0] = (dataArray[i][0] + dataArray[i + 12][0] + dataArray[i + 24][0]
                    + dataArray[i + 36][0] + dataArray[i + 48][0]) / 5;
            predictionArray[i][1] = (dataArray[i][1] + dataArray[i + 12][1] + dataArray[i + 24][1]
                    + dataArray[i + 36][1] + dataArray[i + 48][1]) / 5;
            predictionArray[i][2] = (dataArray[i][2] + dataArray[i + 12][2] + dataArray[i + 24][2]
                    + dataArray[i + 36][2] + dataArray[i + 48][2]) / 5;
        }

        double[][] newDataArray = append(dataArray, predictionArray);

        for (int j = 12; j < 24; j++) {
            predictionArray[j][0] = (newDataArray[j][0] + newDataArray[j + 12][0] + newDataArray[j + 24][0]
                    + newDataArray[j + 36][0] + newDataArray[j + 48][0]) / 5;
            predictionArray[j][1] = (newDataArray[j][1] + newDataArray[j + 12][1] + newDataArray[j + 24][1]
                    + newDataArray[j + 36][1] + newDataArray[j + 48][1]) / 5;
            predictionArray[j][2] = (newDataArray[j][2] + newDataArray[j + 12][2] + newDataArray[j + 24][2]
                    + newDataArray[j + 36][2] + newDataArray[j + 48][2]) / 5;
        }

        double[][] newPredictionArray = new double[2][3];
        double custTotal = 0, suppTotal = 0, totTotal = 0;
        for (int k = 0; k < 12; k++) {
            custTotal += predictionArray[k][0];
            suppTotal += predictionArray[k][1];
            totTotal += predictionArray[k][2];
        }
        newPredictionArray[0][0] = custTotal;
        newPredictionArray[0][1] = suppTotal;
        newPredictionArray[0][2] = totTotal;

        for (int k = 12; k < 24; k++) {
            custTotal += predictionArray[k][0];
            suppTotal += predictionArray[k][1];
            totTotal += predictionArray[k][2];
        }
        newPredictionArray[1][0] = custTotal;
        newPredictionArray[1][1] = suppTotal;
        newPredictionArray[1][2] = totTotal;

        return newPredictionArray;
    }

    /**
     * A method used to append one 2D double array to another.
     * 
     * @param a
     *            A 2D double array at needs another 2D double array attached.
     * @param b
     *            A 2D double array that needs to be attached to the end of
     *            another 2D double array.
     * @return The 2D double array created when the second parameter is appended
     *         to the first.
     */
    public static double[][] append(double[][] a, double[][] b) {
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    /**
     * A method used to print a 2D double array to console.
     * 
     * @param array
     *            The 2D double array to be printed to console.
     */
    public static void print2DArray(double[][] array) {
        for (int k = 0; k < array.length; k++) {
            System.out.println("row# " + k);
            for (int b = 0; b < 3; b++) {
                System.out.print("\t" + array[k][b]);
            }
            System.out.println("\n");
        }
    }
}
