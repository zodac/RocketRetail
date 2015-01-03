package dit.groupproject.rocketretail.tables;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

/**
 * A class that is used to display a table with multiple <code>Customer</code>
 * entries. It offers sorting options and options to add, edit and delete
 * customers
 */
public class CustomerTable extends BaseTable {

    public static boolean first = true;
    private final static String[] SORT_OPTIONS = { "Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added" };

    private static String sortType = "Sort by...";
    public static boolean descendingOrderSort = false;

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
                createTable();
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
    public static void createTable() {
        setTableState(TableState.CUSTOMER);
        resetGui();

        if (first) {
            sortItems();
            first = false;
        }

        final String[] customerColumnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final Object[][] data = createTableData(Database.getCustomers());
        final JTable table = createTable(data, customerColumnNames);
        final JPanel buttonPanel = createButtonPanel();

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);

        GuiCreator.mainPanel.add(scrollPane, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(buttonPanel, BorderLayout.CENTER);
        GuiCreator.setFrame(false, false, true);
    }

    private static JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final ArrayList<Entity> customers = Database.getCustomers();

        final String[] itemsToEdit = new String[customers.size() + 1];
        final String[] itemsToDelete = new String[itemsToEdit.length];
        itemsToEdit[0] = "Edit Customer";
        itemsToDelete[0] = "Delete Customer";

        int editAndDeleteIndex = 1;
        for (final Entity customer : customers) {
            itemsToEdit[editAndDeleteIndex] = "ID: " + CUSTOMER_ID_FORMATTER.format(customer.getId()) + " ("
                    + ((Customer) customer).getCustomerName() + ")";
            itemsToDelete[editAndDeleteIndex] = itemsToDelete[editAndDeleteIndex++];
        }
        final JComboBox<String> sortOptions = new JComboBox<String>(SORT_OPTIONS);
        final int index = Arrays.asList(SORT_OPTIONS).indexOf(sortType);

        sortOptions.setSelectedIndex(index);

        final JButton addButton = createAddButton("Add Customer");
        final JComboBox<String> editSelection = createEditBox(itemsToEdit);
        final JComboBox<String> deleteSelection = createDeleteBox(itemsToDelete);

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
                createTable();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editSelection);
        buttonPanel.add(deleteSelection);
        buttonPanel.add(sortOptions);
        return buttonPanel;
    }

    private static void resetGui() {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + ShopDriver.getCurrentTableState().toString());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));
    }

    public static void sortItems() {
        Comparator<Entity> comparator = Customer.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getCustomers(), comparator);
    }
}