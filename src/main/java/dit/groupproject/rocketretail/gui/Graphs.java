//Main
package dit.groupproject.rocketretail.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;

import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.utilities.DateSort;
import dit.groupproject.rocketretail.utilities.Predictions;

public class Graphs {
    /**
     * An array of Strings for the graphs).
     */
    static String[] titleArray = { "Sales", "Purchases", "Gross Profit" };
    static int startYearIndex;
    /**
     * A DecimalFormatter which formats Integers into Strings with the given
     * formatting. Formats doubles to have commas, always show to two decimal
     * places, and have at least two digits before the decimal point.
     */
    static DecimalFormat doubleFormatter = new DecimalFormat("#,###,#00.00");
    /**
     * A Color object containing the colour the colour representing the sales.
     */
    static Color colourSales = new Color(109, 209, 186);
    /**
     * A Color object containing the colour the colour representing the
     * purchases.
     */
    static Color colourPurchases = new Color(142, 209, 109);
    /**
     * A Color object containing the colour representing the profit.
     */
    static Color colourProfit = new Color(209, 142, 109);

    // current year finder
    // public static int yearInt = Calendar.getInstance().get(Calendar.YEAR);
    // **************
    // Replaced with ShopDriver.yearCurrent - we needed a reference to the
    // current year in the rest of the project, so just combining it into one
    // place
    /**
     * creates new items in the menubar with actionlisteners attached
     */
    public static JMenu createMenu() {

        JMenu graphMenu = new JMenu("Graphs");

        JMenuItem barGraphFiveYearItem = new JMenuItem("Create 5-Year Bar Graph");
        barGraphFiveYearItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int firstYear = 2009;
                int secondYear = 2013;
                startYearIndex = 10;
                double[][] inputArray = createArrayMultipleYears(firstYear, secondYear);
                // startYearIndex = firstYear.getSelectedIndex();
                BarGraph5Year("Title", inputArray, "Years", true);
            }
        });

        JMenuItem barGraphTwelveMonthItem = new JMenuItem("Create 12-Month Bar Graph");
        barGraphTwelveMonthItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int singleYear = 2013;
                startYearIndex = 14;
                double[][] inputArray = createArraySingleYear(singleYear);
                BarGraph12Month("Title", inputArray, "" + "Months", false);
            }
        });

        JMenuItem predBarGraphTwoYearItem = new JMenuItem("Create 2-Year Prediction Bar Graph");
        predBarGraphTwoYearItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int firstYear = 0, secondYear = 0;
                firstYear = ShopDriver.yearCurrent - 4;
                secondYear = ShopDriver.yearCurrent;

                double[][] inputArray = createArrayMultipleYears(firstYear, secondYear);
                BarGraphLeft5Year("Title", inputArray, "Years", true);

                double[][] twoYearPredictionArray = Predictions.twoYrPrediction(Predictions
                        .createArrayFiveYear(ShopDriver.yearCurrent));
                twoYearPredictionBarGraph("Two Year Prediction", twoYearPredictionArray, "Years", true);
            }
        });

        JMenuItem predBarGraphSixMonthItem = new JMenuItem("Create 6-Month Prediction Bar Graph");
        predBarGraphSixMonthItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // also show 12 month for this year
                double[][] inputArray = createArraySingleYear(ShopDriver.yearCurrent);
                BarGraphLeft("Title", inputArray, "" + "Months", false);

                double[][] sixMonthPredictionArray = Predictions.sixMthPrediction(Predictions
                        .createArrayFiveYear(ShopDriver.yearCurrent));
                sixMonthPredictionBarGraph("Six Month Prediction", sixMonthPredictionArray, "Months", true);

            }
        });

        graphMenu.add(barGraphFiveYearItem);
        graphMenu.add(barGraphTwelveMonthItem);
        graphMenu.add(predBarGraphTwoYearItem);
        graphMenu.add(predBarGraphSixMonthItem);
        // Return menu
        return graphMenu;
    }

    /**
     * creates an 2d array list includes profit, loss and balance
     * 
     * @param firstYear
     *            (int)
     * @param secondYear
     *            (int)
     * */
    public static double[][] createArrayMultipleYears(int firstYear, int secondYear) {

        double[][] inputArray = new double[(secondYear - firstYear) + 1][3]; // 5
                                                                             // years
                                                                             // (by
                                                                             // month),
                                                                             // 3
                                                                             // entries
                                                                             // per
                                                                             // month
                                                                             // (sales,
                                                                             // purchases,
                                                                             // profit/loss)
        DateSort.SortDate(firstYear, (secondYear + 1));

        for (int i = 0; i < (secondYear - firstYear) + 1; i++) {
            double custTotal = 0, suppTotal = 0;

            for (int j = 0; j < 12; j++) {
                custTotal += DateSort.custDates[i + (firstYear - ShopDriver.yearStart)][j];
                suppTotal += DateSort.suppDates[i + (firstYear - ShopDriver.yearStart)][j];
            }

            inputArray[i][0] = custTotal;
            inputArray[i][1] = suppTotal; // Comes into this method as a
                                          // negative value
            inputArray[i][2] = custTotal + suppTotal;
        }
        return inputArray;
    }

    /**
     * creates an 2d array list for 12 months includes profit, loss and balance
     * 
     * @param selectedYear
     *            (int)
     * */
    public static double[][] createArraySingleYear(int selectedYear) {
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
     * creates an 3D bargraph set size and position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void BarGraphLeft(String title, double[][] dataArray, String xAxisTitle, boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Graphs");
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                createDataset(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        String yearTitle = "Plot for " + (ShopDriver.yearCurrent);
        chart.addSubtitle(new TextTitle(yearTitle, new Font("SansSerif", Font.PLAIN, 18), new Color(82, 89, 110),
                RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.WEST);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * creates an 3D bargraph plotting 5 years set size and position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void BarGraphLeft5Year(String title, double[][] dataArray, String xAxisTitle, boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Graphs");
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                fiveYearCreateDataset(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        String yearTitle = "Plot for " + (ShopDriver.yearCurrent - 5) + "-" + "" + (ShopDriver.yearCurrent);
        chart.addSubtitle(new TextTitle(yearTitle, new Font("SansSerif", Font.PLAIN, 18), new Color(82, 89, 110),
                RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.WEST);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * creates an 3D bargraph predicting 6 months profit and loss set size and
     * position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void sixMonthPredictionBarGraph(String title, double[][] dataArray, String xAxisTitle, boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Prediction");
        ShopDriver.frame.repaint();
        // ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                createDatasetMth(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(470, 270));

        String yearTitle = "" + (ShopDriver.yearCurrent + 1);
        chart.addSubtitle(new TextTitle("Prediction for " + yearTitle, new Font("SansSerif", Font.PLAIN, 18),
                new Color(82, 89, 110), RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP,
                RectangleInsets.ZERO_INSETS));

        GradientPaint gradientpaint0 = new GradientPaint(0.0F, 0.0F, colourSales, 0.0F, 0.0F, colourSales);
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, colourPurchases, 0.0F, 0.0F, colourPurchases);
        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, colourProfit, 0.0F, 0.0F, colourProfit);

        BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0, gradientpaint0);
        r.setSeriesPaint(1, gradientpaint1);
        r.setSeriesPaint(2, gradientpaint2);

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.EAST);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * creates an 3D bargraph predicting 2 years profit and loss set size and
     * position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void twoYearPredictionBarGraph(String title, double[][] dataArray, String xAxisTitle, Boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Prediction");
        ShopDriver.frame.repaint();
        // ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                twoYearCreateDataset(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        String yearTitle = "Predictions for " + (ShopDriver.yearCurrent + 1) + "-" + (ShopDriver.yearCurrent + 2);
        chart.addSubtitle(new TextTitle(yearTitle, new Font("SansSerif", Font.PLAIN, 18), new Color(82, 89, 110),
                RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS));

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 270));

        GradientPaint gradientpaint0 = new GradientPaint(0.0F, 0.0F, colourSales, 0.0F, 0.0F, colourSales);
        GradientPaint gradientpaint1 = new GradientPaint(0.0F, 0.0F, colourPurchases, 0.0F, 0.0F, colourPurchases);
        GradientPaint gradientpaint2 = new GradientPaint(0.0F, 0.0F, colourProfit, 0.0F, 0.0F, colourProfit);

        BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0, gradientpaint0);
        r.setSeriesPaint(1, gradientpaint1);
        r.setSeriesPaint(2, gradientpaint2);

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.EAST);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * Creates a bargraph set size and position
     * 
     * @param title
     * @param xAxis
     * @param yAxis
     * @param productDescription
     * @param productPercentLevel
     * @return the ChartPanel showing the product levels
     */
    public static ChartPanel createProductGraph(String title, String xAxis, String yAxis,
            ArrayList<String> productDescription, ArrayList<Double> productPercentLevel) {

        JFreeChart chart = ChartFactory.createBarChart(null, xAxis, yAxis,
                createProductDataset(productDescription, productPercentLevel), PlotOrientation.VERTICAL, true, true,
                false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setTickLabelFont(
                new java.awt.Font("Dialog", java.awt.Font.PLAIN, 9));

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        GradientPaint gradientpaint0 = new GradientPaint(0.0F, 0.0F, new Color(51, 153, 255), 0.0F, 0.0F, new Color(51,
                153, 255));

        BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
        r.setSeriesPaint(0, gradientpaint0);

        ChartPanel c = new ChartPanel(chart);

        c.setPreferredSize(new Dimension(500, 270));

        return c;

    }

    /**
     * creates a data set including products and stock
     * 
     * @param productDescription
     *            (ArrayList<String>)
     * @param productPercentLevel
     *            (ArrayList<Double>)
     * */
    private static DefaultCategoryDataset createProductDataset(ArrayList<String> productDescription,
            ArrayList<Double> productPercentLevel) {
        DefaultCategoryDataset dcd = new DefaultCategoryDataset();

        for (int i = 0; i < productDescription.size(); i++) {
            dcd.addValue(productPercentLevel.get(i), "Product", productDescription.get(i));
        }
        return dcd;
    }

    /**
     * creates a data set including months and years
     * 
     * @param dataArray
     *            (double [][])
     * @param isYear
     *            (boolean)
     * */
    private static DefaultCategoryDataset createDataset(double[][] dataArray, boolean isYear) {
        DefaultCategoryDataset dcd = new DefaultCategoryDataset();
        String xaxis = "";
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        for (int i = 0; i < dataArray.length; i++) {
            if (isYear)
                xaxis = ShopDriver.YEARS_AS_NUMBERS[i + startYearIndex];
            else
                xaxis = months[i];

            for (int j = 0; j < dataArray[1].length; j++) {
                dcd.addValue(dataArray[i][j], titleArray[j], xaxis);
            }
        }
        return dcd;
    }

    /**
     * creates a data set including months and years
     * 
     * @param dataArray
     *            (double [][])
     * @param isYear
     *            (boolean)
     * */
    private static DefaultCategoryDataset twoYearCreateDataset(double[][] dataArray, boolean isYear) {
        DefaultCategoryDataset dcdY = new DefaultCategoryDataset();
        String xaxis = "";
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan",
                "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        String[] years = { "" + (ShopDriver.yearCurrent + 1), "" + (ShopDriver.yearCurrent + 2) };

        for (int i = 0; i < dataArray.length; i++) {
            if (isYear) {
                xaxis = years[i];
            } else {
                xaxis = months[i];
            }

            for (int j = 0; j < dataArray[1].length; j++) {
                dcdY.addValue(dataArray[i][j], titleArray[j], xaxis);
            }
        }
        return dcdY;
    }

    /**
     * creates a data set including years and months
     * 
     * @param dataArray
     *            (double [][])
     * @param isYear
     *            (boolean)
     * */
    private static DefaultCategoryDataset fiveYearCreateDataset(double[][] dataArray, boolean isYear) {
        DefaultCategoryDataset dcdY = new DefaultCategoryDataset();
        String xaxis = "";
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan",
                "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        String[] years = { "" + (ShopDriver.yearCurrent - 4), "" + (ShopDriver.yearCurrent - 3),
                "" + (ShopDriver.yearCurrent - 2), "" + (ShopDriver.yearCurrent - 1),
                "" + (ShopDriver.yearCurrent - 0), "" + ShopDriver.yearCurrent };

        for (int i = 0; i < dataArray.length; i++) {
            if (isYear) {
                xaxis = years[i];
            } else {
                xaxis = months[i];
            }

            for (int j = 0; j < dataArray[1].length; j++) {
                dcdY.addValue(dataArray[i][j], titleArray[j], xaxis);
            }
        }
        return dcdY;
    }

    /**
     * creates a data set including years and months
     * 
     * @param dataArray
     *            (double [][])
     * @param isYear
     *            (boolean)
     * */
    private static DefaultCategoryDataset createDatasetMth(double[][] dataArray, boolean isYear) {
        DefaultCategoryDataset dcdM = new DefaultCategoryDataset();
        String xaxis = "";
        String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        for (int i = 0; i < dataArray.length; i++) {
            if (isYear)
                xaxis = months[i];
            else
                xaxis = ShopDriver.YEARS_AS_NUMBERS[i];

            for (int j = 0; j < dataArray[1].length; j++) {
                dcdM.addValue(dataArray[i][j], titleArray[j], xaxis);
            }
        }
        return dcdM;
    }

    /**
     * creates an 3D bargraph plotting 12 months profit and loss set size and
     * position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void BarGraph12Month(String title, double[][] dataArray, String xAxisTitle, boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Orders");
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                createDataset(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        String yearTitle = "Plot for " + (startYearIndex + 1999);
        chart.addSubtitle(new TextTitle(yearTitle, new Font("SansSerif", Font.PLAIN, 18), new Color(82, 89, 110),
                RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS));

        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setBackground(ShopDriver.backgroundColour);
        GridBagConstraints g = new GridBagConstraints();

        ChartPanel chartPanel = new ChartPanel(chart);
        if (isYear) {
            chartPanel.setPreferredSize(new Dimension(500, 270));

            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 4;
            myPanel.add(new JLabel("Please select the start year:"), g);

            g.gridx = 0;
            g.gridy = 1;
            final JComboBox<String> firstYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
            firstYear.setSelectedIndex(startYearIndex);
            myPanel.add(firstYear, g);

            firstYear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (firstYear.getSelectedIndex() != 0) {
                        double[][] inputArray = createArrayMultipleYears(firstYear.getSelectedIndex()
                                + (ShopDriver.yearStart - 1), (firstYear.getSelectedIndex() + 4)
                                + (ShopDriver.yearStart - 1));
                        startYearIndex = firstYear.getSelectedIndex();
                        BarGraph12Month("Title", inputArray, "Months", true);
                    }
                }
            });

            ShopDriver.mainPanel.add(myPanel, BorderLayout.CENTER);
        }

        else {
            chartPanel.setPreferredSize(new Dimension(650, 270));

            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 4;
            myPanel.add(new JLabel("Please select a year:"), g);

            g.gridx = 0;
            g.gridy = 1;
            final JComboBox<String> singleYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
            singleYear.setSelectedIndex(startYearIndex);
            myPanel.add(singleYear, g);

            singleYear.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (singleYear.getSelectedIndex() != 0) {
                        startYearIndex = singleYear.getSelectedIndex();
                        double[][] inputArray = createArraySingleYear((singleYear.getSelectedIndex() + (ShopDriver.yearStart - 1)));
                        BarGraph12Month("Title", inputArray, "Years", false);
                    }
                }
            });
            ShopDriver.mainPanel.add(myPanel, BorderLayout.CENTER);
        }

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.CENTER);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * creates an 3D bargraph plotting 5 years Profit and Loss set size and
     * position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void BarGraph5Year(String title, double[][] dataArray, String xAxisTitle, boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Orders");
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                createDataset(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        String yearTitle = "Plot of " + (1999 + startYearIndex) + " - " + (1999 + startYearIndex + 4);
        chart.addSubtitle(new TextTitle(yearTitle, new Font("SansSerif", Font.PLAIN, 18), new Color(82, 89, 110),
                RectangleEdge.TOP, HorizontalAlignment.CENTER, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS));

        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setBackground(ShopDriver.backgroundColour);
        GridBagConstraints g = new GridBagConstraints();

        ChartPanel chartPanel = new ChartPanel(chart);
        if (isYear) {
            chartPanel.setPreferredSize(new Dimension(500, 270));

            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 4;
            myPanel.add(new JLabel("Please select the start year:"), g);

            g.gridx = 0;
            g.gridy = 1;
            final JComboBox<String> firstYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
            firstYear.setSelectedIndex(startYearIndex);
            myPanel.add(firstYear, g);

            firstYear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (firstYear.getSelectedIndex() != 0) {
                        double[][] inputArray = createArrayMultipleYears(firstYear.getSelectedIndex()
                                + (ShopDriver.yearStart - 1), (firstYear.getSelectedIndex() + 4)
                                + (ShopDriver.yearStart - 1));
                        startYearIndex = firstYear.getSelectedIndex();
                        BarGraph5Year("Title", inputArray, "Years", true);
                    }
                }
            });

            ShopDriver.mainPanel.add(myPanel, BorderLayout.CENTER);
        }

        else {
            chartPanel.setPreferredSize(new Dimension(650, 270));

            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 4;
            myPanel.add(new JLabel("Please select a year:"), g);

            g.gridx = 0;
            g.gridy = 1;
            final JComboBox<String> singleYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
            singleYear.setSelectedIndex(startYearIndex);
            myPanel.add(singleYear, g);

            singleYear.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (singleYear.getSelectedIndex() != 0) {
                        startYearIndex = singleYear.getSelectedIndex();
                        double[][] inputArray = createArraySingleYear((singleYear.getSelectedIndex() + (ShopDriver.yearStart - 1)));
                        BarGraph5Year("Title", inputArray, "Months", false);
                    }
                }
            });
            ShopDriver.mainPanel.add(myPanel, BorderLayout.CENTER);
        }

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.CENTER);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * creates an 3D bargraph plotting Profit and Loss set size and position
     * 
     * @param title
     *            (String)
     * @param dataArray
     *            (double [][])
     * @param xAxisTitle
     *            (String)
     * @param isYear
     *            (boolean)
     * */
    public static void BarGraph(String title, double[][] dataArray, String xAxisTitle, boolean isYear) {
        // Reset frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Orders");
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel();

        JFreeChart chart = ChartFactory.createBarChart3D(null, xAxisTitle, "Euro (€)",
                createDataset(dataArray, isYear), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(ShopDriver.backgroundColour);

        ((CategoryPlot) chart.getPlot()).getDomainAxis().setLowerMargin(0.0);
        ((CategoryPlot) chart.getPlot()).getDomainAxis().setUpperMargin(0.0);

        JPanel myPanel = new JPanel(new GridBagLayout());
        myPanel.setBackground(ShopDriver.backgroundColour);
        GridBagConstraints g = new GridBagConstraints();

        ChartPanel chartPanel = new ChartPanel(chart);
        if (isYear) {
            chartPanel.setPreferredSize(new Dimension(500, 270));

            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 4;
            myPanel.add(new JLabel("Please select the start year:"), g);

            g.gridx = 0;
            g.gridy = 1;
            final JComboBox<String> firstYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
            firstYear.setSelectedIndex(startYearIndex);
            myPanel.add(firstYear, g);

            firstYear.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (firstYear.getSelectedIndex() != 0) {
                        double[][] inputArray = createArrayMultipleYears(firstYear.getSelectedIndex()
                                + (ShopDriver.yearStart - 1), (firstYear.getSelectedIndex() + 4)
                                + (ShopDriver.yearStart - 1));
                        startYearIndex = firstYear.getSelectedIndex();
                        BarGraph("Title", inputArray, firstYear.getSelectedItem() + "-"
                                + (firstYear.getSelectedIndex() + ShopDriver.yearStart + 4), true);
                    }
                }
            });

            ShopDriver.mainPanel.add(myPanel, BorderLayout.CENTER);
        }

        else {
            chartPanel.setPreferredSize(new Dimension(650, 270));

            g.gridx = 0;
            g.gridy = 0;
            g.gridwidth = 4;
            myPanel.add(new JLabel("Please select a year:"), g);

            g.gridx = 0;
            g.gridy = 1;
            final JComboBox<String> singleYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
            singleYear.setSelectedIndex(startYearIndex);
            myPanel.add(singleYear, g);

            singleYear.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    if (singleYear.getSelectedIndex() != 0) {
                        startYearIndex = singleYear.getSelectedIndex();
                        double[][] inputArray = createArraySingleYear((singleYear.getSelectedIndex() + (ShopDriver.yearStart - 1)));
                        BarGraph("Title", inputArray, (String) singleYear.getSelectedItem(), false);
                    }
                }
            });
            ShopDriver.mainPanel.add(myPanel, BorderLayout.CENTER);
        }

        ShopDriver.mainPanel.add(chartPanel, BorderLayout.CENTER);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * 
     * creates a pie chart plotting sales breakdown set size and position
     * 
     * @param chartTitle
     * @param staffID
     * @param salesBreakdown
     * @return the ChartPanel containing the graph
     */
    public static ChartPanel createPieChart(String chartTitle, String staffID, double[] salesBreakdown) {

        DefaultPieDataset dpd = createDataset(staffID, salesBreakdown);
        JFreeChart chart = ChartFactory.createPieChart(chartTitle, dpd, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);

        return chartPanel;
    }

    /**
     * creates a data set including staff and sales
     * 
     * @param salesBreakdown
     *            (double [])
     * @param staffID
     *            (String)
     * */
    private static DefaultPieDataset createDataset(String staffID, double[] salesBreakdown) {
        DefaultPieDataset dpd = new DefaultPieDataset();

        dpd.setValue(staffID + "\n €" + doubleFormatter.format(salesBreakdown[0]), salesBreakdown[0]);
        dpd.setValue("Others" + "\n€" + doubleFormatter.format(salesBreakdown[1]), salesBreakdown[1]);

        return dpd;
    }

    /**
     * creates a line chart plotting stock level
     * 
     * @param title
     *            (String)
     * @param seriesName
     *            (String)
     * @param data
     *            (double [][])
     * */
    public static ChartPanel createLineChart(String title, String seriesName, double[][] data) {

        XYDataset ds = createDataset(data, seriesName);
        JFreeChart chart = ChartFactory.createXYLineChart(title, "Year", "Stock Level", ds, PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel c = new ChartPanel(chart);

        return c;

    }

    /**
     * creates a data set
     * 
     * @param data
     *            (double [][])
     * @param seriesName
     *            (String)
     * */
    private static XYDataset createDataset(double[][] data, String seriesName) {

        DefaultXYDataset ds = new DefaultXYDataset();

        ds.addSeries(seriesName, data);

        return ds;
    }
}
