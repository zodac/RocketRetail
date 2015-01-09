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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.menus.MenuGui;

public class SupplierTable extends BaseTable {

    public static boolean first = true;
    public static boolean descendingOrderSort = false;

    private final static String[] SORT_OPTIONS = { "Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added" };

    private static String sortType = "Sort by...";

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
     * Creates the JTable for Products, using data from
     * {@link ShopDriver#products}.<br />
     * Adds JButtons to add, edit or delete a product, and implements the
     * ActionListeners for each.<br />
     * Calls {@link #viewSupplierInfo(Supplier)} when a supplier is selected
     * from the table. *
     */
    public static void createTableGui() {
        setTableState(TableState.SUPPLIER);
        resetGui();

        if (first) {
            sortItems();
            first = false;
        }

        final String[] supplierColumnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final Object[][] data = createTableData(Database.getSuppliers());
        final JTable table = createTable(data, supplierColumnNames);
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

        final ArrayList<Entity> suppliers = Database.getSuppliers();

        final String[] itemsToEdit = new String[suppliers.size() + 1];
        final String[] itemsToDelete = new String[itemsToEdit.length];
        itemsToEdit[0] = "Edit Supplier";
        itemsToDelete[0] = "Delete Supplier";

        int editAndDeleteIndex = 1;
        for (final Entity supplier : suppliers) {
            itemsToEdit[editAndDeleteIndex] = "ID: " + SUPPLIER_ID_FORMATTER.format(supplier.getId()) + " ("
                    + ((Supplier) supplier).getSupplierName() + ")";
            itemsToDelete[editAndDeleteIndex] = itemsToEdit[editAndDeleteIndex++];
        }

        final JButton addButton = createAddButton("Add Supplier");
        final JComboBox<String> editSelection = createEditBox(itemsToEdit);
        final JComboBox<String> deleteSelection = createDeleteBox(itemsToDelete);

        final JComboBox<String> sortOptions = new JComboBox<String>(SORT_OPTIONS);
        final int index = Arrays.asList(SORT_OPTIONS).indexOf(sortType);
        sortOptions.setSelectedIndex(index);

        sortOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sortOptions.getSelectedItem().equals("Sort by...")) {

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

        buttonPanel.add(addButton);
        buttonPanel.add(editSelection);
        buttonPanel.add(deleteSelection);
        buttonPanel.add(sortOptions);
        return buttonPanel;
    }

    public static void sortItems() {
        Comparator<Entity> comparator = Supplier.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getSuppliers(), comparator);
    }

}