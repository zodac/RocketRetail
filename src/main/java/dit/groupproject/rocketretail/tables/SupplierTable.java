package dit.groupproject.rocketretail.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.menus.MenuGui;

public class SupplierTable extends AbstractTable {

    public boolean firstTimeLoadingTable = true;
    public boolean descendingOrderSort = false;
    private String currentSortOption = "Sort by...";

    private static SupplierTable instance = null;

    public static SupplierTable getInstance() {
        if (instance == null) {
            instance = new SupplierTable();
        }
        return instance;
    }

    /**
     * Creates the JMenuItem for "Supplier", and defines the ActionListener for
     * the JMenuItem. <br />
     * The ActionListener calls the {@link #createTableGui()} method.
     * 
     * @return the JMenuItem for the "Database" JMenuItem in
     *         {@link MenuGui#createMenuBar(JMenuBar, boolean)}
     * 
     * @see #createTableGui()
     * @see MenuGui#createMenuBar(JMenuBar, boolean)
     */
    public JMenuItem createMenu(final TableState newState, final boolean manager) {
        final JMenuItem menuItem = new JMenuItem(newState.toString());

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
     * Calls {@link #viewSupplierInfo(Supplier)} when a supplier is selected
     * from the table.
     */
    @Override
    public void createTableGui() {
        setTableState(TableState.SUPPLIER);
        resetGui();

        if (firstTimeLoadingTable) {
            sortItems();
            firstTimeLoadingTable = false;
        }

        final String[] supplierColumnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final Object[][] data = createTableData(Database.getSuppliers());
        final JScrollPane scrollPane = createScrollableTable(data, supplierColumnNames);
        final JComboBox<String> sortOptions = createSortOptions();
        final JPanel buttonPanel = createButtonPanel(TableState.SUPPLIER.toString(), sortOptions);

        updateGui(scrollPane, buttonPanel);
    }

    @Override
    public void sortItems() {
        Comparator<Entity> comparator = Supplier.getComparator(currentSortOption);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getSuppliers(), comparator);
    }

    @Override
    protected void reverseSortOrder() {
        descendingOrderSort = !descendingOrderSort;
    }

    @Override
    protected String getCurrentSortOption() {
        return currentSortOption;
    }

    @Override
    protected void setCurrentSortOption(final String sortOption) {
        this.currentSortOption = sortOption;
    }

    @Override
    protected String[] getSortOptionTitles() {
        return new String[] { "Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added" };
    }

}