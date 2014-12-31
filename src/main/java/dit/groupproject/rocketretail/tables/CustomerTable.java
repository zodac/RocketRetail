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

    private final static String[] CUSTOMER_COLUMN_NAMES = { "ID", "Name", "Phone Number", "Address", "VAT Number",
            "Last Purchase", "Date Added" };
    /**
     * The type to sort the table by
     */
    static String sortType = "Sort by...";
    /**
     * A flag to decide if it's the first time the table is being loaded.
     */
    public static boolean first = true;
    /**
     * A flag to decide whether <code>Customer</code> records are added to an
     * <code>ArrayList</code in ascending or descending order.
     */
    static boolean descendingOrderSort = false;

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
            sortById();
            first = false;
        }

        final Object[][] data = createTableData();

        final JTable table = createTable(data);

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final JPanel buttonPanel = createButtonPanel();

        GuiCreator.mainPanel.add(scrollPane, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(buttonPanel, BorderLayout.CENTER);

        GuiCreator.setFrame(false, false, true);
    }

    private static Object[][] createTableData() {
        final Object[][] data = new Object[ShopDriver.getCustomers().size()][7];

        for (int i = 0; i < ShopDriver.getCustomers().size(); i++) {
            final Customer customer = ShopDriver.getCustomers().get(i);

            data[i][0] = CUSTOMER_ID_FORMATTER.format(customer.getCustomerId());
            data[i][1] = customer.getCustomerName();
            data[i][2] = customer.getPhoneNumber();
            data[i][3] = customer.getAddress();
            data[i][4] = customer.getVatNumber();
            data[i][5] = customer.getLastPurchase();
            data[i][6] = customer.getDateAdded();
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
                    for (final Customer customer : ShopDriver.getCustomers()) {
                        final int selectedCustomerId = Integer.parseInt((String) table.getValueAt(
                                table.getSelectedRow(), 0));

                        if (customer.getCustomerId() == selectedCustomerId) {
                            showCustomerInfo(customer);
                            break;
                        }
                    }
                }
            }
        });
        return table;
    }

    private static JPanel createButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final String[] customerMemberArrayEdit = new String[ShopDriver.getCustomers().size() + 1];
        customerMemberArrayEdit[0] = "Edit Customer";
        for (int i = 0; i < ShopDriver.getCustomers().size() + 1; i++) {
            if (i < ShopDriver.getCustomers().size())
                customerMemberArrayEdit[i + 1] = "ID: "
                        + CUSTOMER_ID_FORMATTER.format(ShopDriver.getCustomers().get(i).getCustomerId()) + " ("
                        + ShopDriver.getCustomers().get(i).getCustomerName() + ")";
        }

        final String[] customerMemberArrayDelete = new String[ShopDriver.getCustomers().size() + 1];
        customerMemberArrayDelete[0] = "Delete Customer";
        for (int i = 0; i < ShopDriver.getCustomers().size() + 1; i++) {
            if (i < ShopDriver.getCustomers().size())
                customerMemberArrayDelete[i + 1] = "ID: "
                        + CUSTOMER_ID_FORMATTER.format(ShopDriver.getCustomers().get(i).getCustomerId()) + " ("
                        + ShopDriver.getCustomers().get(i).getCustomerName() + ")";
        }

        final JButton addButton = new JButton("Add Customer");
        final JComboBox<String> editBox = new JComboBox<String>(customerMemberArrayEdit);
        final JComboBox<String> deleteBox = new JComboBox<String>(customerMemberArrayDelete);

        final String[] options = { "Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final JComboBox<String> sortOptions = new JComboBox<String>(options);
        final int index = Arrays.asList(options).indexOf(sortType);

        sortOptions.setSelectedIndex(index);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        editBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edit(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 9)));
            }
        });

        deleteBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 9)),
                        ((String) deleteBox.getSelectedItem()).substring(11,
                                ((String) deleteBox.getSelectedItem()).length() - 1));
            }
        });

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
                    sortArrayList();
                }
                createTable();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editBox);
        buttonPanel.add(deleteBox);
        buttonPanel.add(sortOptions);
        return buttonPanel;
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
        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 6;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        dateAddedDay.setSelectedIndex(Integer.parseInt(DAY_FORMATTER.format(new Date())));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(MONTH_FORMATTER.format(new Date())));
        dateAddedYear
                .setSelectedIndex(Integer.parseInt(YEAR_FORMATTER.format(new Date())) - (ShopDriver.yearStart - 1));

        g.gridx = 0;
        g.gridy = 7;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(" "), g);

        JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 8;
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

                boolean valid = checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes);

                if (valid) {
                    ShopDriver
                            .getCustomers()
                            .add(new Customer(custNameField.getText(), phoneNoField.getText(), addressField.getText(),
                                    vatNoField.getText(), lastPurchaseDay.getSelectedItem() + "/"
                                            + lastPurchaseMonth.getSelectedItem() + "/"
                                            + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/"
                                            + dateAddedMonth.getSelectedItem() + "/" + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmMessage("Customer " + custNameField.getText() + " added");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();

                    descendingOrderSort = false;
                    sortById();
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
    public static void edit(int customerID) {

        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        for (Customer c : ShopDriver.getCustomers()) {
            if (customerID == c.getCustomerId()) {
                final int index = ShopDriver.getCustomers().indexOf(c);

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

                final JComboBox<String> lastPurchaseYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
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

                final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.YEARS_AS_NUMBERS);
                innerPanel.add(dateAddedYear, g);

                custIDField.setText("" + c.getCustomerId());
                custNameField.setText(c.getCustomerName());
                phoneNoField.setText(c.getPhoneNumber());
                addressField.setText(c.getAddress());
                vatNoField.setText(c.getVatNumber());
                lastPurchaseDay.setSelectedIndex(Integer.parseInt(c.getLastPurchase().substring(0, 2)));
                lastPurchaseMonth.setSelectedIndex(Integer.parseInt(c.getLastPurchase().substring(3, 5)));
                lastPurchaseYear.setSelectedIndex(Integer.parseInt(c.getLastPurchase().substring(6, 10))
                        - (ShopDriver.yearStart - 1));
                dateAddedDay.setSelectedIndex(Integer.parseInt(c.getDateAdded().substring(0, 2)));
                dateAddedMonth.setSelectedIndex(Integer.parseInt(c.getDateAdded().substring(3, 5)));
                dateAddedYear.setSelectedIndex(Integer.parseInt(c.getDateAdded().substring(6, 10))
                        - (ShopDriver.yearStart - 1));

                g.gridx = 0;
                g.gridy = 8;
                innerPanel.add(new JLabel(" "), g);
                g.gridx = 1;
                g.gridy = 8;
                innerPanel.add(new JLabel(" "), g);

                JButton save = new JButton("Save");
                save.setLayout(new GridBagLayout());
                g = new GridBagConstraints();
                g.insets = new Insets(1, 0, 0, 0);
                g.gridx = 1;
                g.gridy = 9;
                innerPanel.add(save, g);
                JButton cancel = new JButton("Cancel");
                g.gridx = 3;
                g.gridy = 9;
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

                        boolean valid = checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes);

                        if (valid) {
                            ShopDriver.getCustomers()
                                    .add(index,
                                            new Customer(custNameField.getText(), phoneNoField.getText(), addressField
                                                    .getText(), vatNoField.getText(), lastPurchaseDay.getSelectedItem()
                                                    + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                                                    + lastPurchaseYear.getSelectedItem(), dateAddedDay
                                                    .getSelectedItem()
                                                    + "/"
                                                    + dateAddedMonth.getSelectedItem()
                                                    + "/"
                                                    + dateAddedYear.getSelectedItem()));

                            GuiCreator.setConfirmMessage("Customer " + custNameField.getText() + "'s details editted");
                            GuiCreator.frame.remove(GuiCreator.leftPanel);
                            GuiCreator.frame.repaint();
                            GuiCreator.frame.validate();
                            ShopDriver.getCustomers().remove(index + 1);
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
            }
        }

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

        int i = -1;

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            for (final Customer c : ShopDriver.getCustomers()) {
                if (customerId == c.getCustomerId()) {
                    i = ShopDriver.getCustomers().indexOf(c);
                }
            }
        }

        if (i != -1) {
            GuiCreator.setConfirmMessage(customerName + " deleted");
            ShopDriver.getCustomers().remove(i);
        }

        createTable();
        GuiCreator.frame.validate();
    }

    /**
     * When a user clicks on a <code>Customer</code> in the customer table, the
     * <code>Customer</code>'s details are brought up and a table is built
     * showing the orders that they have made.
     * 
     * @param c
     *            Takes in a <code>Customer</code>'s details.
     */
    public static void showCustomerInfo(Customer c) {

        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + c.getCustomerName());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        JLabel customerLabel = new JLabel("Customer");
        JLabel vatNumberLabel = new JLabel("VAT Number");
        JLabel phoneNoLabel = new JLabel("Phone Number");
        JLabel addressLabel = new JLabel("Address");
        JLabel dateAddedLabel = new JLabel("Date Added");
        JLabel titleLabel = new JLabel("Sales to " + c.getCustomerName());

        customerLabel.setFont(new Font(customerLabel.getFont().getFontName(), Font.BOLD, customerLabel.getFont()
                .getSize()));
        vatNumberLabel.setFont(new Font(vatNumberLabel.getFont().getFontName(), Font.BOLD, vatNumberLabel.getFont()
                .getSize()));
        phoneNoLabel
                .setFont(new Font(phoneNoLabel.getFont().getFontName(), Font.BOLD, phoneNoLabel.getFont().getSize()));
        addressLabel
                .setFont(new Font(addressLabel.getFont().getFontName(), Font.BOLD, addressLabel.getFont().getSize()));
        dateAddedLabel.setFont(new Font(dateAddedLabel.getFont().getFontName(), Font.BOLD, dateAddedLabel.getFont()
                .getSize()));
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));

        int textFieldSize = 20;

        JTextField customerField = new JTextField(c.getCustomerName() + " ("
                + CUSTOMER_ID_FORMATTER.format(c.getCustomerId()) + ")", textFieldSize);
        customerField.setEditable(false);
        JTextField vatNumberField = new JTextField(c.getVatNumber(), textFieldSize);
        vatNumberField.setEditable(false);

        JTextField phoneNoField = new JTextField(c.getPhoneNumber(), textFieldSize);
        phoneNoField.setEditable(false);
        JTextField addressField = new JTextField(c.getAddress(), textFieldSize);
        addressField.setEditable(false);
        JTextField dateAddedField = new JTextField(c.getDateAdded(), textFieldSize);
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

        int count = 0;

        for (Order o : ShopDriver.getOrders()) {
            if (o.getTraderId() == c.getCustomerId())
                count++;
        }

        final String[] columnNames = { "Order ID", "Order Date", "Delivery Date", "Total Cost" };
        final Object[][] data = new Object[count + 1][4];
        int indexArray = 0;
        double total = 0;

        for (int i = 0; i < ShopDriver.getOrders().size(); i++) {
            if (c.getCustomerId() == ShopDriver.getOrders().get(i).getTraderId()) {

                data[indexArray][0] = ORDER_ID_FORMATTER.format(ShopDriver.getOrders().get(i).getOrderId());
                data[indexArray][1] = ShopDriver.getOrders().get(i).getOrderDate();
                data[indexArray][2] = ShopDriver.getOrders().get(i).getDeliveryDate();
                data[indexArray][3] = "€" + CURRENCY_FORMATTER.format(ShopDriver.getOrders().get(i).getTotalSale());
                total += ShopDriver.getOrders().get(i).getTotalSale();
                indexArray++;
            }
        }
        data[count][2] = "<html><b>Total Sales</b></html>";
        data[count][3] = "<html><b>€" + CURRENCY_FORMATTER.format(total) + "</b></html>";

        final JTable table = new JTable(data, columnNames);
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

    /**
     * This method sorts the <code>Customer</code>s by their ID.
     * 
     * @param reverse
     *            A boolean to decide whether to add the <code>Customer</code>s
     *            to an <code>ArrayList</code> in ascending or descending order.
     */

    /**
     * This method has a control structure to determine which sorting parameter
     * was chosen to sort the <code>Customer</code>s by.
     */
    public static void sortArrayList() {
        if (sortType.equals("ID")) {
            sortById();
        } else if (sortType.equals("Name")) {
            sortByName();
        } else if (sortType.equals("Address")) {
            sortByAddress();
        } else if (sortType.equals("VAT Number")) {
            sortByVatNumber();
        } else if (sortType.equals("Last Purchase")) {
            sortByLastPurchadeDate();
        } else if (sortType.equals("Date Added")) {
            sortByDateAdded();
        }
    }

    public static void sortById() {
        ArrayList<Customer> tempArrayList = new ArrayList<Customer>();
        int count = IdManager.CUSTOMER_ID_START;
        int offset = 0;
        boolean found = false;

        for (int i = 0; i < ShopDriver.getCustomers().size() + offset; i++) {
            found = false;
            for (Customer p : ShopDriver.getCustomers()) {
                if (count == p.getCustomerId()) {
                    tempArrayList.add(p);
                    found = true;
                }
            }
            if (!found) {
                offset++;
            }
            count++;
        }

        ShopDriver.getCustomers().clear();

        if (!descendingOrderSort) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                ShopDriver.getCustomers().add(tempArrayList.get(i));
            }
        } else if (descendingOrderSort) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                ShopDriver.getCustomers().add(tempArrayList.get(i));
            }
        }
    }

    public static void sortByName() {
        if (descendingOrderSort) {
            Collections.sort(ShopDriver.getCustomers(), Collections.reverseOrder(Customer.compareByName));
        } else {
            Collections.sort(ShopDriver.getCustomers(), Customer.compareByName);
        }
    }

    public static void sortByAddress() {
        if (descendingOrderSort) {
            Collections.sort(ShopDriver.getCustomers(), Collections.reverseOrder(Customer.compareByAddress));
        } else {
            Collections.sort(ShopDriver.getCustomers(), Customer.compareByAddress);
        }
    }

    public static void sortByVatNumber() {
        if (descendingOrderSort) {
            Collections.sort(ShopDriver.getCustomers(), Collections.reverseOrder(Customer.compareByVatNumber));
        } else {
            Collections.sort(ShopDriver.getCustomers(), Customer.compareByVatNumber);
        }
    }

    public static void sortByLastPurchadeDate() {
        if (descendingOrderSort) {
            Collections.sort(ShopDriver.getCustomers(), Collections.reverseOrder(Customer.compareByLastPurchaseDate));
        } else {
            Collections.sort(ShopDriver.getCustomers(), Customer.compareByLastPurchaseDate);
        }
    }

    public static void sortByDateAdded() {
        if (descendingOrderSort) {
            Collections.sort(ShopDriver.getCustomers(), Collections.reverseOrder(Customer.compareByDateAdded));
        } else {
            Collections.sort(ShopDriver.getCustomers(), Customer.compareByDateAdded);
        }
    }
}