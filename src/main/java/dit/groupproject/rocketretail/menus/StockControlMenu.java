package dit.groupproject.rocketretail.menus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.gui.Graphs;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.utilities.StockControlUtilities;

/**
 * A class that is used to model a <code>StockControlMenu</code>
 */
public class StockControlMenu {

    static ArrayList<String> productDescription = new ArrayList<String>();
    static ArrayList<Double> productPercentLevel = new ArrayList<Double>();
    static ArrayList<Product> productsToReplenish = new ArrayList<Product>();
    static JComboBox<String> replenishBox;

    private final static int NUMBER_OF_REPLENISH_OPTIONS = 5;
    private final static double BASE_STOCK_INCREASE_PERCENTAGE = 100 / (NUMBER_OF_REPLENISH_OPTIONS - 1);
    private final static String REPLENISH_PRODUCTS_OPTION_FORMAT = "Replenish low products to %s";

    /**
     * Create submenu and ActionListeners, and passes back to ShopDriver to add
     * to menuBar.
     */
    public static JMenu createMenu() {
        JMenuItem checkAllProductLevels = new JMenuItem("Check All Product Levels");
        checkAllProductLevels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAll();
            }
        });

        JMenu stockControlMenu = new JMenu("Stock Control");
        stockControlMenu.add(checkAllProductLevels);

        return stockControlMenu;
    }

    /**
     * Checks the stock levels of all products and displays in a bar chart. If
     * product levels fall low, gives an option to order more stock from
     * suppliers. Order options if stock falls below 25%, 50%, 75% or 100%.
     * */
    public static void checkAll() {

        GuiCreator.frame.remove(GuiCreator.rightPanel);
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.remove(GuiCreator.leftPanel);

        GuiCreator.frame.repaint();
        GuiCreator.frame.setTitle("Rocket Retail Inc - Current Stock Levels");
        GuiCreator.mainPanel = new JPanel(new BorderLayout());

        productDescription.clear();
        productPercentLevel.clear();
        productsToReplenish.clear();

        for (final Entity p : Database.getProducts()) {
            final Product product = (Product) p;
            productDescription.add(product.getName());

            final Double percentage = StockControlUtilities.calculatePercentage(product.getCurrentStockLevel(), product.getMaxStockLevel());

            productPercentLevel.add(percentage);

            if (Double.compare(percentage, new Double(BASE_STOCK_INCREASE_PERCENTAGE)) == -1) {
                productsToReplenish.add(product);
            }
        }

        // creates a bar chart of products VS % of max stock using JFreePanel
        ChartPanel p = Graphs.createProductGraph("Product Chart", "Products", "% of Max Stock", productDescription, productPercentLevel);

        final String[] replenishOptions = new String[NUMBER_OF_REPLENISH_OPTIONS];
        replenishOptions[0] = String.format(REPLENISH_PRODUCTS_OPTION_FORMAT, "...");

        for (int i = 1; i < replenishOptions.length; i++) {
            replenishOptions[i] = String.format(REPLENISH_PRODUCTS_OPTION_FORMAT, (int) (BASE_STOCK_INCREASE_PERCENTAGE * i) + "%");
        }

        replenishBox = new JComboBox<String>(replenishOptions);

        if (productsToReplenish.isEmpty()) {
            replenishBox.setSelectedIndex(0);
            replenishBox.setEnabled(false);
        }

        replenishBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                final int selectedIndex = replenishBox.getSelectedIndex();

                if (selectedIndex != 0) {
                    final double percentageToIncrease = BASE_STOCK_INCREASE_PERCENTAGE * selectedIndex;

                    StockControlUtilities.replenishStocks(new Double(percentageToIncrease), productsToReplenish);

                    GuiCreator.setConfirmationMessage("Products replenished to " + (int) percentageToIncrease + "%");
                    replenishBox.setSelectedIndex(0);
                    replenishBox.setEnabled(false);
                    checkAll();
                }

            }
        });

        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        comboPanel.add(replenishBox);
        GuiCreator.mainPanel.add(comboPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(p, BorderLayout.CENTER);

        GuiCreator.setFrame(false, false, true);
    }
}
