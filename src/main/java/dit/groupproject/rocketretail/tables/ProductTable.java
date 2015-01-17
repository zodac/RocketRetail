package dit.groupproject.rocketretail.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.menus.MenuGui;

/**
 * ProductTable adds the "Product" menu-item to the menu-bar (within
 * "Database"), and shows a JTable for all products. It populates the table with
 * information from {@link ShopDriver#products}, and includes JButtons to allow
 * a user to add, edit or delete a product.<br />
 * Sorting options are available for the table, by Name, ID, Cost Price, Sale
 * Price, Stock Level or SupplierID. Clicking on a row in the table gives more
 * information on the product, including a breakdown of orders/sales by year,
 * which is also displayed on a graph.<br />
 * Users may also place an order for the product on this page.
 */
public class ProductTable extends AbstractTable {

    public static boolean first = true;
    public static boolean descendingOrderSort = false;

    private final static String[] SORT_OPTIONS = { "Sort by...", "ID", "Description", "Stock Level", "Supplier ID", "Cost Price", "Sale Price" };

    private static String sortType = "Sort by...";

    /**
     * Creates the JMenuItem for "Product", and defines the ActionListener for
     * the JMenuItem. <br />
     * The ActionListener calls the {@link #createTableGui()} method.
     * 
     * @return the JMenuItem for the "Database" JMenuItem in
     *         {@link MenuGui#createMenuBar(JMenuBar, boolean)}
     * 
     * @see #createTableGui()
     * @see MenuGui#createMenuBar(JMenuBar, boolean)
     */
    public static JMenuItem createMenu(final TableState newState, final boolean manager) {
        JMenuItem menuItem = new JMenuItem(newState.toString());
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTableGui();
            }
        });

        menuItem.setEnabled(manager);
        return menuItem;
    }

    /**
     * Creates the JTable for Products, using data from
     * {@link ShopDriver#products}.<br />
     * Adds JButtons to add, edit or delete a product, and implements the
     * ActionListeners for each.<br />
     * Calls {@link #viewProductInfo(Product)} when a product is selected from
     * the table. *
     * 
     * @see #addProductPanel()
     * @see #editProductPanel(int)
     * @see #deleteProduct(int, String)
     * @see #viewProductInfo(Product)
     */
    public static void createTableGui() {
        setTableState(TableState.PRODUCT);
        resetGui();

        if (first) {
            sortItems();
            first = false;
        }

        final String[] productColumnNames = { "ID", "Description", "Stock Level", "Max Level", "Supplier ID", "Cost Price", "Sale Price" };
        final Object[][] data = createTableData(Database.getProducts());
        final JScrollPane scrollPane = createScrollableTable(data, productColumnNames);
        final JComboBox<String> sortOptions = createSortOptions();
        final JPanel buttonPanel = createButtonPanel(TableState.PRODUCT.toString(), sortOptions);

        updateGui(scrollPane, buttonPanel);
    }

    private static JComboBox<String> createSortOptions() {
        final JComboBox<String> sortOptions = new JComboBox<String>(SORT_OPTIONS);
        final int index = Arrays.asList(SORT_OPTIONS).indexOf(sortType);
        sortOptions.setSelectedIndex(index);

        sortOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sortOptions.getSelectedItem().equals("Sort by...")) {
                    // Do nothing
                } else {
                    if (sortType.equals((String) sortOptions.getSelectedItem())) {
                        descendingOrderSort ^= descendingOrderSort;
                    } else {
                        sortType = (String) sortOptions.getSelectedItem();
                    }
                    sortItems();
                }
                ProductTable.createTableGui();
            }
        });
        return sortOptions;
    }

    public static void sortItems() {
        Comparator<Entity> comparator = Product.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getProducts(), comparator);
    }
}