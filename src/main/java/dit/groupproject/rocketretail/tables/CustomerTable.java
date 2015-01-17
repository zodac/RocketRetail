package dit.groupproject.rocketretail.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
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
public class CustomerTable extends BaseTable {

    public static boolean first = true;
    public static boolean descendingOrderSort = false;

    private final static String[] SORT_OPTIONS = { "Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added" };

    private static String sortType = "Sort by...";

    /**
     * This method creates a Customer menu item and adds an
     * <code>ActionListener</code> to it.
     * 
     * @return A <code>JMenuItem</code> to be added to a <code>JMenu</code>.
     */
    public static JMenuItem createMenu(final TableState newState, final boolean manager) {
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
    public static void createTableGui() {
        setTableState(TableState.CUSTOMER);
        resetGui();

        if (first) {
            sortItems();
            first = false;
        }

        final String[] customerColumnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final Object[][] data = createTableData(Database.getCustomers());
        final JScrollPane scrollPane = createScrollableTable(data, customerColumnNames);
        final JComboBox<String> sortOptions = createSortOptions();
        final JPanel buttonPanel = createButtonPanel(TableState.CUSTOMER.toString(), sortOptions);

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
                        descendingOrderSort = !descendingOrderSort;
                    } else {
                        sortType = (String) sortOptions.getSelectedItem();
                    }
                    sortItems();
                }
                createTableGui();
            }
        });
        return sortOptions;
    }

    public static void sortItems() {
        Comparator<Entity> comparator = Customer.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getCustomers(), comparator);
    }
}