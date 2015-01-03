package dit.groupproject.rocketretail.tables;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entityhelpers.AddEntityHelper;
import dit.groupproject.rocketretail.entityhelpers.DeleteEntityHelper;
import dit.groupproject.rocketretail.entityhelpers.EditEntityHelper;
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

    private final static String[] ORDER_COLUMN_NAMES = { "Order ID", "Order Date", "Delivery Date", "Total Cost" };
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

    private static JTable createTable(final Object[][] data, final String[] columnNames) {
        final JTable table = new JTable(data, columnNames);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0) {
                    final int selectedId = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));

                    final Customer customer = (Customer) Database.getCustomerById(selectedId);
                    showCustomerInfo(customer);
                }
            }
        });
        return table;
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

    private static JButton createAddButton(final String addButtonTitle) {
        final JButton addButton = new JButton(addButtonTitle);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddEntityHelper.addCustomerPanel();
            }
        });
        return addButton;
    }

    private static JComboBox<String> createEditBox(final String[] itemsToEdit) {
        final JComboBox<String> editBox = new JComboBox<String>(itemsToEdit);

        editBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EditEntityHelper.editCustomerPanel(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 9)));
            }
        });
        return editBox;
    }

    private static JComboBox<String> createDeleteBox(final String[] itemsToDelete) {
        final JComboBox<String> deleteBox = new JComboBox<String>(itemsToDelete);

        deleteBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteEntityHelper.deleteCustomer(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 9)),
                        ((String) deleteBox.getSelectedItem()).substring(11, ((String) deleteBox.getSelectedItem()).length() - 1));
            }
        });
        return deleteBox;
    }

    private static void resetGui() {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + ShopDriver.getCurrentTableState().toString());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));
    }

    /**
     * When a user clicks on a <code>Customer</code> in the customer table, the
     * <code>Customer</code>'s details are brought up and a table is built
     * showing the orders that they have made.
     * 
     * @param customer
     *            Takes in a <code>Customer</code>'s details.
     */
    public static void showCustomerInfo(Customer customer) {

        final String customerName = customer.getCustomerName();

        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + customerName);
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        final JPanel titlePanel = new JPanel(new GridBagLayout());
        final JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        final JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final JLabel customerLabel = new JLabel("Customer");
        final JLabel vatNumberLabel = new JLabel("VAT Number");
        final JLabel phoneNoLabel = new JLabel("Phone Number");
        final JLabel addressLabel = new JLabel("Address");
        final JLabel dateAddedLabel = new JLabel("Date Added");
        final JLabel titleLabel = new JLabel("Sales to " + customerName);

        final Font currentFont = new JLabel().getFont();
        final Font labelFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());

        customerLabel.setFont(labelFont);
        vatNumberLabel.setFont(labelFont);
        phoneNoLabel.setFont(labelFont);
        addressLabel.setFont(labelFont);
        dateAddedLabel.setFont(labelFont);
        titleLabel.setFont(labelFont);

        final int textFieldSize = 20;

        final JTextField customerField = new JTextField(customerName + " (" + CUSTOMER_ID_FORMATTER.format(customer.getId()) + ")", textFieldSize);
        customerField.setEditable(false);
        JTextField vatNumberField = new JTextField(customer.getVatNumber(), textFieldSize);
        vatNumberField.setEditable(false);

        JTextField phoneNoField = new JTextField(customer.getPhoneNumber(), textFieldSize);
        phoneNoField.setEditable(false);
        JTextField addressField = new JTextField(customer.getAddress(), textFieldSize);
        addressField.setEditable(false);
        JTextField dateAddedField = new JTextField(customer.getDateAdded(), textFieldSize);
        dateAddedField.setEditable(false);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        titlePanel.add(customerLabel, g);
        g.gridx = 1;
        titlePanel.add(customerField, g);
        g.gridx = 4;
        titlePanel.add(vatNumberLabel, g);
        g.gridx = 5;
        titlePanel.add(vatNumberField, g);

        g.gridy = 1;
        g.gridx = 0;
        titlePanel.add(phoneNoLabel, g);
        g.gridx = 1;
        titlePanel.add(phoneNoField, g);
        g.gridx = 2;
        titlePanel.add(addressLabel, g);
        g.gridx = 3;
        titlePanel.add(addressField, g);
        g.gridx = 4;
        titlePanel.add(dateAddedLabel, g);
        g.gridx = 5;
        titlePanel.add(dateAddedField, g);

        g.gridy = 2;
        g.gridx = 2;
        g.gridwidth = 2;
        titlePanel.add(titleLabel, g);

        ArrayList<Order> customerOrders = new ArrayList<>();

        for (final Order order : Database.getOrders()) {
            if (order.getTraderId() == customer.getId()) {
                customerOrders.add(order);
            }
        }
        final int numberOfCustomerOrders = customerOrders.size();

        final Object[][] data = new Object[numberOfCustomerOrders + 1][4];
        int indexArray = 0;
        double total = 0;

        for (final Order customerOrder : customerOrders) {
            data[indexArray][0] = ORDER_ID_FORMATTER.format(customerOrder.getOrderId());
            data[indexArray][1] = customerOrder.getOrderDate();
            data[indexArray][2] = customerOrder.getDeliveryDate();
            data[indexArray++][3] = "€" + CURRENCY_FORMATTER.format(customerOrder.getTotalSale());
            total += customerOrder.getTotalSale();
        }

        data[numberOfCustomerOrders][2] = "<html><b>Total Sales</b></html>";
        data[numberOfCustomerOrders][3] = "<html><b>€" + CURRENCY_FORMATTER.format(total) + "</b></html>";

        final JTable table = new JTable(data, ORDER_COLUMN_NAMES);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        final JScrollPane scrollPane = new JScrollPane(table);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        final JButton backButton = new JButton("Back to Customers");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);

        GuiCreator.setFrame(false, false, true);
    }

    public static void sortItems() {
        Comparator<Entity> comparator = Customer.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getCustomers(), comparator);
    }
}