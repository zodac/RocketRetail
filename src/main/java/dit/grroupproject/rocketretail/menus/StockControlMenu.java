package dit.grroupproject.rocketretail.menus;

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

    /**
     * Create submenu and ActionListeners, and passes back to ShopDriver to add
     * to menuBar
     */
    public static JMenu createMenu() {
        JMenuItem checkAllProductLevels = new JMenuItem("Check All Product Levels");
        checkAllProductLevels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkAll();
            }
        });

        // Add menu items to "Stock Control" menu
        JMenu stockControlMenu = new JMenu("Stock Control");
        stockControlMenu.add(checkAllProductLevels);

        // Return submenu
        return stockControlMenu;
    }

    /**
     * Checks the stock levels of all products and displays in a bar chart. If
     * product levels fall low, gives an option to order more stock from
     * suppliers. Order options if stock falls below 25%, 50%, 75% or 100%.
     * */
    public static void checkAll() {

        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.rightPanel);
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.remove(GuiCreator.leftPanel);

        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout());

        productDescription.clear();
        productPercentLevel.clear();
        productsToReplenish.clear();

        for (final Product p : Database.getProducts()) {
            productDescription.add(p.getProductDescription());

            Double percentage = StockControlUtilities.calculatePercentage(p.getStockLevel(), p.getMaxLevel());

            productPercentLevel.add(percentage);

            if (Double.compare(percentage, new Double(25.00)) == -1) {
                productsToReplenish.add(p);
            }
        }
        /**
         * creates a bar chart of products VS % of max stock using JFreePanel
         * */
        ChartPanel p = Graphs.createProductGraph("Product Chart", "Products", "% of Max Stock", productDescription,
                productPercentLevel);

        String[] replenishOptions = new String[5];
        replenishOptions[0] = "Replenish products to...";
        replenishOptions[1] = "Replenish low products to 25%";
        replenishOptions[2] = "Replenish low products to 50%";
        replenishOptions[3] = "Replenish low products to 75%";
        replenishOptions[4] = "Replenish low products to 100%";

        replenishBox = new JComboBox<String>(replenishOptions);

        if (productsToReplenish.isEmpty()) {
            replenishBox.setSelectedIndex(0);
            replenishBox.setEnabled(false);
        }

        replenishBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (replenishBox.getSelectedIndex() == 0) {
                } else {
                    if (replenishBox.getSelectedIndex() == 1)
                        StockControlUtilities.replenishStocks(new Double(25.00), productsToReplenish);
                    if (replenishBox.getSelectedIndex() == 2)
                        StockControlUtilities.replenishStocks(new Double(50.00), productsToReplenish);
                    if (replenishBox.getSelectedIndex() == 3)
                        StockControlUtilities.replenishStocks(new Double(75.00), productsToReplenish);
                    if (replenishBox.getSelectedIndex() == 4)
                        StockControlUtilities.replenishStocks(new Double(100.00), productsToReplenish);

                    GuiCreator.setConfirmationMessage("Products replensihed to " + (replenishBox.getSelectedIndex() * 25)
                            + "%");
                    replenishBox.setSelectedIndex(0);
                    replenishBox.setEnabled(false);
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

    /**
     * returns the productDescription arrayList
     * 
     * @return productDescription (ArrayList)
     */
    public static ArrayList<String> getProductDescription() {
        return productDescription;
    }

    /**
     * changes the ArrayList productDescription
     * 
     * @param productDescription
     */
    public static void setProductDescription(ArrayList<String> productDescription) {
        StockControlMenu.productDescription = productDescription;
    }

    /**
     * returns ArrayList productPercentLevel
     * 
     * @return productPercentLevel (ArrayList)
     */
    public static ArrayList<Double> getProductPercentLevel() {
        return productPercentLevel;
    }

    /**
     * changes the value of ArrayList productPercentLevel
     * 
     * @param productPercentLevel
     *            (ArrayList)
     */
    public static void setProductPercentLevel(ArrayList<Double> productPercentLevel) {
        StockControlMenu.productPercentLevel = productPercentLevel;
    }

    /**
     * returns the ArrayList productsToReplenish
     * 
     * @return productsToReplenish (ArrayList)
     */
    public static ArrayList<Product> getProductsToReplenish() {
        return productsToReplenish;
    }

    /**
     * changes the value of ArrayList productsToReplenish
     * 
     * @param productsToReplenish
     *            (ArrayList)
     */
    public static void setProductsToReplenish(ArrayList<Product> productsToReplenish) {
        StockControlMenu.productsToReplenish = productsToReplenish;
    }

    /**
     * returns the value of ArrayList replenishBox
     * 
     * @return replenishBox (ArrayList)
     */
    public static JComboBox<String> getReplenishBox() {
        return replenishBox;
    }

    /**
     * chages the value of ArrayList replenishBox
     * 
     * @param replenishBox
     *            (ArrayList)
     */
    public static void setReplenishBox(JComboBox<String> replenishBox) {
        StockControlMenu.replenishBox = replenishBox;
    }

}