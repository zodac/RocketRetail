package dit.groupproject.rocketretail.tables;

import static dit.groupproject.rocketretail.utilities.DateHandler.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.DAY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.DateHandler.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.MONTH_FORMATTER;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_FORMATTER;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

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
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.TableState;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * A class that is used to display a table with multiple <code>Customer</code>
 * entries. It offers sorting options and options to add, edit and delete
 * customers
 */
public class CustomerTable extends BaseTable {

    public static boolean first = true;

    private final static String[] CUSTOMER_COLUMN_NAMES = { "ID", "Name", "Phone Number", "Address", "VAT Number",
            "Last Purchase", "Date Added" };
    private final static String[] ORDER_COLUMN_NAMES = { "Order ID", "Order Date", "Delivery Date", "Total Cost" };
    private final static String[] SORT_OPTIONS = { "Sort by...", "ID", "Name", "Address", "VAT Number",
            "Last Purchase", "Date Added" };

    private static String sortType = "Sort by...";
    private static boolean descendingOrderSort = false;

    /**
     * This method creates a Customer menu item and adds an
     * <code>ActionListener</code> to it.
     * 
     * @return A <code>JMenuItem</code> to be added to a <code>JMenu</code>.
     */
    public static JMenuItem createMenu(boolean manager) {
        final JMenuItem customerItem = new JMenuItem("Customer");

        customerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        customerItem.setEnabled(manager);
        return customerItem;
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
        setTableState();
        resetGui();

        if (first) {
            sortItems();
            first = false;
        }

        final Object[][] data = createTableData();
        final JTable table = createTable(data);
        final JPanel buttonPanel = createButtonPanel();

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);

        GuiCreator.mainPanel.add(scrollPane, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(buttonPanel, BorderLayout.CENTER);
        GuiCreator.setFrame(false, false, true);
    }

    private static Object[][] createTableData() {
        final ArrayList<Customer> customers = Database.getCustomers();
        final Object[][] data = new Object[customers.size()][customers.get(0).getNumberOfFields()];
        int dataIndex = 0;

        for (final Customer customer : customers) {
            data[dataIndex++] = customer.getData();
        }
        return data;
    }

    private static JTable createTable(final Object[][] data) {
        final JTable table = new JTable(data, CUSTOMER_COLUMN_NAMES);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0) {
                    final int selectedCustomerId = Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0));

                    final Customer customer = Database.getCustomerById(selectedCustomerId);
                    showCustomerInfo(customer);
                }
            }
        });
        return table;
    }

    private static JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final ArrayList<Customer> customers = Database.getCustomers();

        final String[] itemsToEdit = new String[customers.size() + 1];
        final String[] itemsToDelete = new String[itemsToEdit.length];
        itemsToEdit[0] = "Edit Customer";
        itemsToDelete[0] = "Delete Customer";

        int editAndDeleteIndex = 1;
        for (final Customer customer : customers) {
            itemsToEdit[editAndDeleteIndex] = "ID: " + CUSTOMER_ID_FORMATTER.format(customer.getCustomerId()) + " ("
                    + customer.getCustomerName() + ")";
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
                add();
            }
        });
        return addButton;
    }

    private static JComboBox<String> createEditBox(final String[] itemsToEdit) {
        final JComboBox<String> editBox = new JComboBox<String>(itemsToEdit);

        editBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edit(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 9)));
            }
        });
        return editBox;
    }

    private static JComboBox<String> createDeleteBox(final String[] itemsToDelete) {
        final JComboBox<String> deleteBox = new JComboBox<String>(itemsToDelete);

        deleteBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 9)),
                        ((String) deleteBox.getSelectedItem()).substring(11,
                                ((String) deleteBox.getSelectedItem()).length() - 1));
            }
        });
        return deleteBox;
    }

    private static void resetGui() {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - Customers");
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));
    }

    private static void setTableState() {
        if (ShopDriver.getCurrentTableState() != TableState.CUSTOMER) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
        }
        ShopDriver.setCurrentTable(TableState.CUSTOMER);
    }

    /**
     * This method builds a form for adding a <code>Customer</code>. It also has
     * the logic to add a <code>Customer</code> to an <code>ArrayList</code> of
     * customers.
     */
    public static void add() {
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        innerPanel.add(new JLabel("Customer Name:"), g);
        g.gridy = 1;
        innerPanel.add(new JLabel("Phone Number:"), g);
        g.gridy = 2;
        innerPanel.add(new JLabel("Address:"), g);
        g.gridy = 3;
        innerPanel.add(new JLabel("VAT Number:"), g);
        g.gridy = 4;
        innerPanel.add(new JLabel("Last Purchase:"), g);
        g.gridy = 5;
        innerPanel.add(new JLabel("Date Added:"), g);

        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField custNameField = new JTextField(null, 20);
        innerPanel.add(custNameField, g);
        g.gridy = 1;
        final JTextField phoneNoField = new JTextField(null, 20);
        innerPanel.add(phoneNoField, g);
        g.gridy = 2;
        final JTextField addressField = new JTextField(null, 20);
        innerPanel.add(addressField, g);
        g.gridy = 3;
        final JTextField vatNoField = new JTextField(null, 20);
        innerPanel.add(vatNoField, g);

        g.gridy = 4;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> lastPurchaseDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);
        g.gridx = 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);
        g.gridx = 3;
        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 5;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        dateAddedDay.setSelectedIndex(Integer.parseInt(DAY_FORMATTER.format(new Date())));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(MONTH_FORMATTER.format(new Date())));
        dateAddedYear.setSelectedIndex(Integer.parseInt(YEAR_FORMATTER.format(new Date())) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 6;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(" "), g);

        JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 7;
        innerPanel.add(save, g);
        JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                textFields.add(custNameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                textFields.add(vatNoField);
                ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<JComboBox<String>>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    Database.addCustomer(new Customer(custNameField.getText(), phoneNoField.getText(), addressField
                            .getText(), vatNoField.getText(), lastPurchaseDay.getSelectedItem() + "/"
                            + lastPurchaseMonth.getSelectedItem() + "/" + lastPurchaseYear.getSelectedItem(),
                            dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                                    + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmMessage("Customer " + custNameField.getText() + " added");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();

                    descendingOrderSort = false;
                    sortItems();
                    createTable();
                }
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.leftPanel);
                GuiCreator.frame.repaint();
                GuiCreator.frame.validate();
            }
        });

        GuiCreator.leftPanel.add(innerPanel);

        GuiCreator.setFrame(true, false, false);
    }

    /**
     * This method builds a form for updating a <code>Customer</code>'s details.
     * It also has the logic to update a <code>Customer</code>'s details in the
     * <code>ArrayList</code> of customers.
     */
    public static void edit(final int customerId) {

        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        final Customer customer = Database.getCustomerById(customerId);
        final int index = Database.getIndexOfCustomer(customer);

        final JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        GridBagConstraints g = new GridBagConstraints();

        g.gridx = 0;
        g.gridy = 0;
        innerPanel.add(new JLabel("Customer ID:"), g);
        g.gridy = 1;
        innerPanel.add(new JLabel("Customer Name:"), g);
        g.gridy = 2;
        innerPanel.add(new JLabel("Phone Number:"), g);
        g.gridy = 3;
        innerPanel.add(new JLabel("Address:"), g);
        g.gridy = 4;
        innerPanel.add(new JLabel("VAT Number:"), g);
        g.gridy = 5;
        innerPanel.add(new JLabel("Last Purchase:"), g);
        g.gridy = 6;
        innerPanel.add(new JLabel("Date Added:"), g);

        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField custIDField = new JTextField(null, 20);
        custIDField.setEditable(false);
        innerPanel.add(custIDField, g);
        g.gridy = 1;
        g.gridwidth = 3;
        final JTextField custNameField = new JTextField(null, 20);
        innerPanel.add(custNameField, g);
        g.gridy = 2;
        g.gridwidth = 3;
        final JTextField phoneNoField = new JTextField(null, 20);
        innerPanel.add(phoneNoField, g);
        g.gridy = 3;
        g.gridwidth = 3;
        final JTextField addressField = new JTextField(null, 20);
        innerPanel.add(addressField, g);
        g.gridy = 4;
        g.gridwidth = 3;
        final JTextField vatNoField = new JTextField(null, 20);
        innerPanel.add(vatNoField, g);

        g.gridy = 5;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> lastPurchaseDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);

        g.gridy = 5;
        g.gridx = 2;
        g.gridwidth = 1 - 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);

        g.gridy = 5;
        g.gridx = 3;
        g.gridwidth = 2 - 3;

        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 6;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 6;
        g.gridx = 2;
        g.gridwidth = 1 - 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 6;
        g.gridx = 3;
        g.gridwidth = 2 - 3;

        final JComboBox<String> dateAddedYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        custIDField.setText("" + customer.getCustomerId());
        custNameField.setText(customer.getCustomerName());
        phoneNoField.setText(customer.getPhoneNumber());
        addressField.setText(customer.getAddress());
        vatNoField.setText(customer.getVatNumber());
        lastPurchaseDay.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(0, 2)));
        lastPurchaseMonth.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(3, 5)));
        lastPurchaseYear.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(6, 10))
                - (YEAR_START - 1));
        dateAddedDay.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(0, 2)));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(3, 5)));
        dateAddedYear.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(6, 10)) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 8;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        g.gridy = 8;
        innerPanel.add(new JLabel(" "), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 9;
        innerPanel.add(save, g);
        final JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        g.gridy = 9;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {

                final ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                textFields.add(custNameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                textFields.add(vatNoField);
                final ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                final ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<JComboBox<String>>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    final Customer editedCustomer = new Customer(custNameField.getText(), phoneNoField.getText(),
                            addressField.getText(), vatNoField.getText(), lastPurchaseDay.getSelectedItem() + "/"
                                    + lastPurchaseMonth.getSelectedItem() + "/" + lastPurchaseYear.getSelectedItem(),
                            dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                                    + dateAddedYear.getSelectedItem());
                    editedCustomer.setCustomerId(index + IdManager.CUSTOMER_ID_START);
                    Database.addCustomerByIndex(index, editedCustomer);

                    GuiCreator.setConfirmMessage("Customer " + custNameField.getText() + "'s details editted");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();
                    Database.removeCustomerByIndex(index + 1);
                    createTable();
                }
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.leftPanel);
                GuiCreator.frame.repaint();
                GuiCreator.frame.validate();
            }
        });

        GuiCreator.leftPanel.add(innerPanel);

        GuiCreator.setFrame(true, false, false);
    }

    /**
     * This method shows a dialog box asking a user if they want to delete a
     * <code>Customer</code>. This method also has logic to remove the
     * <code>Customer</code> from system records.
     */
    public static void delete(final int customerId, final String customerName) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + customerName + "?"));

        int indexToRemove = -1;

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            final Customer customer = Database.getCustomerById(customerId);
            indexToRemove = Database.getIndexOfCustomer(customer);
        }

        if (indexToRemove != -1) {
            GuiCreator.setConfirmMessage(customerName + " deleted");
            Database.removeCustomerByIndex(indexToRemove);
        }

        createTable();
        GuiCreator.frame.validate();
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

        final JTextField customerField = new JTextField(customerName + " ("
                + CUSTOMER_ID_FORMATTER.format(customer.getCustomerId()) + ")", textFieldSize);
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
            if (order.getTraderId() == customer.getCustomerId()) {
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

    private static void sortItems() {
        Comparator<Customer> comparator = Customer.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getCustomers(), comparator);
    }
}