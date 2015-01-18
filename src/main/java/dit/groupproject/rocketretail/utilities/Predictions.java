package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;

/**
 * A class that is used to generate prediction forecasts
 */
public class Predictions {

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

    private static double[][] createArrayOneYear(int selectedYear) {
        double[][] dataArray = new double[12][3]; // 12 months, 3 entries per
                                                  // month (sales, purchases,
                                                  // profit/loss)
        DateSort.sortDate(selectedYear, selectedYear + 1);

        for (int i = 0; i < 12; i++) {
            double custTotal = 0, suppTotal = 0;
            custTotal += DateSort.customerOrderDates[selectedYear - YEAR_START][i];
            suppTotal += DateSort.supplierOrderDates[selectedYear - YEAR_START][i];

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

    private static double[][] append(double[][] a, double[][] b) {
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
