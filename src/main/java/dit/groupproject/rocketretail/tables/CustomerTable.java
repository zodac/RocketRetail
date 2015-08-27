package dit.groupproject.rocketretail.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.main.TableState;

/**
 * A class that is used to display a table with multiple {@link Customer}
 * entries. It offers sorting options and options to add, edit and delete
 * customers.
 */
public class CustomerTable extends AbstractTable {

    public boolean firstTimeLoadingTable = true;
    public boolean descendingOrderSort = false;
    private String currentSortOption = "Sort by...";

    private static CustomerTable instance = null;

    public static CustomerTable getInstance() {
        if (instance == null) {
            instance = new CustomerTable();
        }
        return instance;
    }

    /**
     * This method creates a Customer menu item and adds an
     * <code>ActionListener</code> to it.
     * 
     * @return A <code>JMenuItem</code> to be added to a <code>JMenu</code>.
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
     * This method prepares and places all the GUI components into a
     * <code>JPanel</code>. It adds the <code>JPanel</code> to the
     * <code>JFrame</code> using <code>ShopDriver</code>'s
     * <code>setFrame()</code> method.
     * 
     * This method adds <code>ActionListener</code>s to GUI components such as
     * the <code>JTable</code> and multiple <code>JComboBox</code>.
     */
    @Override
    public void createTableGui() {
        setTableState(TableState.CUSTOMER);
        resetGui();

        if (firstTimeLoadingTable) {
            sortItems();
            firstTimeLoadingTable = false;
        }

        final String[] customerColumnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final Object[][] data = createTableData(Database.getCustomers());
        final JScrollPane scrollPane = createScrollableTable(data, customerColumnNames);
        final JComboBox<String> sortOptions = createSortOptions();
        final JPanel buttonPanel = createButtonPanel(TableState.CUSTOMER.toString(), sortOptions);

        updateGui(scrollPane, buttonPanel);
    }

    @Override
    public void sortItems() {
        Comparator<Entity> comparator = Customer.getComparator(currentSortOption);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getCustomers(), comparator);
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