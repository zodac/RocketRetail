package dit.groupproject.rocketretail.tables;

import static dit.groupproject.rocketretail.gui.GuiCreator.BOLD_LABEL_FONT;
import static dit.groupproject.rocketretail.utilities.Formatters.DATE_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.menus.MenuGui;

/**
 * OrderTable adds the "Order" menu-item to the menu-bar (within "Database"),
 * and shows a JTable for all orders. It is accessible by all staff - both
 * Managers and Employees.<br />
 * It populates the table with information from {@link ShopDriver#orders}, and
 * includes JButtons to allow a user to create customer/supplier orders,
 * complete orders, or filter the table to show only customer/supplier/active
 * orders.<br />
 * Sorting options are available for the table, by Order ID, Staff ID, Trader
 * ID, Total order Cost or Order Date.<br />
 * Clicking on a row in the table gives more information on the order, including
 * a breakdown of the ordered items, their cost and the quantity.<br />
 */
public class OrderTable extends AbstractTable {

    private static final String ORDER_COMPLETED_MESSAGE_FORMAT = "Order #%s completed";
    private static final String ORDER_CONFIRMATION_MESSAGE_FORMAT = "Are you sure you want to complete order #%s?";
    private static final String CUSTOMER_ORDER_CREATED_MESSAGE_FORMAT = "Order #%s created for customer \"%s\"";
    private static final String SUPPLIER_ORDER_CREATED_MESSAGE_FORMAT = "Order #%s created for supplier \"%s\"";
    public boolean first = true;
    public boolean descendingOrderSort = false;

    private String sortType = "Sort by...";
    private String orderFilter = "Show All Orders";

    private JPanel innerPanel;
    private JComboBox<String> createBox;
    private JComboBox<String> tempBox = new JComboBox<String>();
    private int validOrders = 0;

    private static OrderTable instance = null;

    public static OrderTable getInstance() {
        if (instance == null) {
            instance = new OrderTable();
        }
        return instance;
    }

    /**
     * Creates the JMenuItem for "Order" and defines the ActionListener for the
     * JMenuItem.<br />
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
     * Created the JTable for Orders, using data from {@link ShopDriver#orders}.<br />
     * Adds JButtons to create or complete orders, and to sort or filter the
     * table.<br />
     * Calls {@link #viewOrderInfo(Order)} when an order is selected from the
     * table.
     */
    public void createTableGui() {
        setTableState(TableState.ORDER);
        resetGui();

        if (first) {
            sortItems();
            first = false;
        }

        final String[] orderColumnNames = { "Order ID", "Staff ID", "Trader ID", "Total Price", "Order Date", "Delivery Date" };
        final Object[][] data = generateTableData(Database.getOrders());
        final JScrollPane scrollPane = createScrollableTable(data, orderColumnNames);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final JComboBox<String> completeOptions = tempBox;
        completeOptions.setEnabled(validOrders != 0);

        String[] options = { "Sort by...", "Order ID", "Staff ID", "Trader ID", "Total Price", "Order Date", "Active" };
        final JComboBox<String> sortOptions = new JComboBox<String>(options);
        int sortIndex = 0;

        if (sortType.equals("Sort by..."))
            sortIndex = 0;
        if (sortType.equals("Order ID"))
            sortIndex = 1;
        if (sortType.equals("Staff ID"))
            sortIndex = 2;
        if (sortType.equals("Trader ID"))
            sortIndex = 3;
        if (sortType.equals("Total Price"))
            sortIndex = 4;
        if (sortType.equals("Order Date"))
            sortIndex = 5;
        if (sortType.equals("Active"))
            sortIndex = 6;
        sortOptions.setSelectedIndex(sortIndex);

        final String[] createOptionsManager = { "Create Order", "Customer Order", "Supplier Order" };
        final String[] createOptionsEmployee = { "Create Order", "Customer Order" };

        if (ShopDriver.getCurrentStaff().getStaffLevel() == 1) {
            createBox = new JComboBox<String>(createOptionsManager);
        } else {
            createBox = new JComboBox<String>(createOptionsEmployee);
        }

        final String[] orderFilterOptions = { "Show All Orders", "Show Supplier Orders", "Show Customer Orders", "Show Active Orders" };
        final JComboBox<String> showOptions = new JComboBox<>(orderFilterOptions);
        showOptions.setSelectedIndex(Arrays.asList(orderFilterOptions).indexOf(orderFilter));

        createBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (createBox.getSelectedItem().equals("Customer Order")) {
                    createCustomerOrder();
                } else if (createBox.getSelectedItem().equals("Supplier Order")) {
                    createSupplierOrder(0);
                }
            }
        });
        completeOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (completeOptions.getSelectedItem().equals("Complete Order")) {

                } else if (completeOptions.getSelectedItem().equals("Complete Customer Order")) {

                } else if (completeOptions.getSelectedItem().equals("Complete Supplier Order")) {

                } else if (completeOptions.getSelectedItem().equals("Complete All Supplier Orders")) {

                    final JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Do you want to complete ALL supplier orders?"));

                    if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
                        int numberOfActiveSupplierOptions = 0;
                        for (final Entity o : Database.getOrders()) {
                            final Order order = (Order) o;
                            if (isActiveSupplierOrder(order)) {
                                order.completeOrder(DATE_FORMATTER.format(new Date()));
                                numberOfActiveSupplierOptions++;
                            }
                        }
                        GuiCreator.setConfirmationMessage(numberOfActiveSupplierOptions + " orders completed");
                        createTableGui();
                    }
                } else if (completeOptions.getSelectedItem().equals("Complete All Customer Orders")) {

                    final JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Do you want to complete ALL customer orders?"));

                    if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
                        int numberOfActiveCustomerOrders = 0;
                        for (final Entity o : Database.getOrders()) {
                            final Order order = (Order) o;
                            if (isActiveCustomerOrder(order)) {
                                order.completeOrder(DATE_FORMATTER.format(new Date()));
                                numberOfActiveCustomerOrders++;
                            }
                        }
                        GuiCreator.setConfirmationMessage(numberOfActiveCustomerOrders + " orders completed");
                        createTableGui();
                    }

                } else if (completeOptions.getSelectedItem().equals("Complete All Orders")) {

                    final JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Do you want to complete ALL orders?"));

                    if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
                        int numberOfActiveOrders = 0;
                        for (final Entity o : Database.getOrders()) {
                            final Order order = (Order) o;
                            if (order.isActive()) {
                                order.completeOrder(DATE_FORMATTER.format(new Date()));
                                numberOfActiveOrders++;
                            }
                        }
                        GuiCreator.setConfirmationMessage(numberOfActiveOrders + " orders completed");
                        createTableGui();
                    }
                } else {
                    completeOrder(Integer.parseInt(((String) completeOptions.getSelectedItem()).substring(4, 10)));
                }
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
                    sortItems();
                }
                createTableGui();
            }
        });

        showOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                orderFilter = (String) showOptions.getSelectedItem();
                createTableGui();
            }
        });

        buttonPanel.add(createBox);
        buttonPanel.add(completeOptions);
        buttonPanel.add(sortOptions);
        buttonPanel.add(showOptions);

        updateGui(scrollPane, buttonPanel);
    }

    private Object[][] generateTableData(final ArrayList<Entity> orders) {
        final int numberOfOrderFields = orders.get(0).getNumberOfFields();
        Object[][] data = null;

        if (orderFilter.equals("Show All Orders")) {
            data = new Object[orders.size()][numberOfOrderFields];
            int dataIndex = 0;

            for (final Entity order : orders) {
                data[dataIndex++] = order.getData();
            }

            int numberOfActiveOrders = 0;

            for (final Entity order : orders) {
                if (((Order) order).isActive()) {
                    numberOfActiveOrders++;
                }
            }

            int arrayIndex = 2;
            final String[] orderArrayComplete = new String[numberOfActiveOrders + 2];
            orderArrayComplete[0] = "Complete Order";
            orderArrayComplete[1] = "Complete All Orders";

            for (final Entity order : orders) {
                if (((Order) order).isActive()) {
                    orderArrayComplete[arrayIndex++] = "ID: " + ID_FORMATTER.format(order.getId());
                }

            }
            validOrders = numberOfActiveOrders;
            tempBox = new JComboBox<String>(orderArrayComplete);
        } else if (orderFilter.equals("Show Supplier Orders")) {
            int numberOfSuppliersWithOrders = 0;

            for (final Entity order : orders) {
                if (((Order) order).isSupplier()) {
                    numberOfSuppliersWithOrders++;
                }
            }

            data = new Object[numberOfSuppliersWithOrders][numberOfOrderFields];
            int supplierIndex = 0;

            for (final Entity order : orders) {
                if (((Order) order).isSupplier()) {
                    data[supplierIndex++] = order.getData();
                }
            }

            int numberOfActiveSupplierOrders = 0;
            for (final Entity order : orders) {
                if (isActiveSupplierOrder(order)) {
                    numberOfActiveSupplierOrders++;
                }
            }

            final String[] orderArrayComplete = new String[numberOfActiveSupplierOrders + 2];
            orderArrayComplete[0] = "Complete Supplier Orders";
            orderArrayComplete[1] = "Complete All Supplier Orders";
            int arrayIndex = 2;

            for (final Entity order : orders) {
                if (isActiveSupplierOrder(order)) {
                    orderArrayComplete[arrayIndex++] = "ID: " + ID_FORMATTER.format(order.getId());
                }
            }
            validOrders = numberOfActiveSupplierOrders;
            tempBox = new JComboBox<String>(orderArrayComplete);
        } else if (orderFilter.equals("Show Customer Orders")) {
            int customerSize = 0;
            for (final Entity order : orders) {
                if (!((Order) order).isSupplier()) {
                    customerSize++;
                }
            }

            data = new Object[customerSize][numberOfOrderFields];
            int customerIndex = 0;

            for (final Entity order : orders) {
                if (!((Order) order).isSupplier()) {
                    data[customerIndex++] = order.getData();
                }
            }

            int numberOfActiveCustomerOrders = 0;
            for (final Entity order : orders) {
                if (isActiveCustomerOrder(order)) {
                    numberOfActiveCustomerOrders++;
                }
            }

            int arrayIndex = 2;
            final String[] orderArrayComplete = new String[numberOfActiveCustomerOrders + 2];
            orderArrayComplete[0] = "Complete Customer Orders";
            orderArrayComplete[1] = "Complete All Customer Orders";

            for (final Entity order : orders) {
                if (isActiveCustomerOrder(order)) {
                    orderArrayComplete[arrayIndex++] = "ID: " + ID_FORMATTER.format(order.getId());
                }
            }

            validOrders = numberOfActiveCustomerOrders;
            tempBox = new JComboBox<String>(orderArrayComplete);
        } else if (orderFilter.equals("Show Active Orders")) {
            int numberOfActiveOrders = 0;

            for (final Entity order : Database.getOrders()) {
                if (((Order) order).isActive()) {
                    numberOfActiveOrders++;
                }
            }

            data = new Object[numberOfActiveOrders][numberOfOrderFields];
            int customerIndex = 0;
            int arrayIndex = 2;
            final String[] orderArrayComplete = new String[numberOfActiveOrders + 2];
            orderArrayComplete[0] = "Complete Active Orders";
            orderArrayComplete[1] = "Complete All Orders";

            for (final Entity order : orders) {
                if (((Order) order).isActive()) {
                    data[customerIndex++] = order.getData();
                    orderArrayComplete[arrayIndex++] = "ID: " + ID_FORMATTER.format(order.getId());
                }
            }

            validOrders = numberOfActiveOrders;
            tempBox = new JComboBox<String>(orderArrayComplete);
        }
        return data;
    }

    private boolean isActiveCustomerOrder(final Entity order) {
        final Order o = (Order) order;
        return o.isActive() && !o.isSupplier();
    }

    private boolean isActiveSupplierOrder(final Entity order) {
        final Order o = (Order) order;
        return o.isActive() && o.isSupplier();
    }

    private void createCustomerOrder() {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        innerPanel = new JPanel(new BorderLayout(0, 2));
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        // Create array of customer names for JComboBox
        String[] customerArray = new String[Database.getCustomers().size() + 2];
        customerArray[0] = "";
        for (int i = 1; i < Database.getCustomers().size() + 1; i++) {
            customerArray[i] = Database.getCustomers().get(i - 1).getName();
        }

        // Add a string to the end of the array
        customerArray[customerArray.length - 1] = "New Customer";

        // Create the JComboBox
        final JComboBox<String> customerOptions = new JComboBox<String>(customerArray);

        customerOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (customerOptions.getSelectedItem().equals("New Customer")) {
                    ShopDriver.setCurrentTable(TableState.CUSTOMER);
                    addEntityHelper.addEntityPanel();
                    ShopDriver.setCurrentTable(TableState.ORDER);
                } else {
                    // Reset ShopDriver.frame
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.leftPanel = new JPanel();

                    // Add labels to panel
                    JLabel productName = new JLabel("Product");
                    JLabel currentStockLevel = new JLabel("Current/Max");
                    JLabel orderAmount = new JLabel("Order Amount");

                    productName.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));
                    currentStockLevel.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));
                    orderAmount.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));

                    JPanel titlePanel = new JPanel(new GridLayout(0, 3));
                    titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    titlePanel.add(productName);
                    titlePanel.add(currentStockLevel);
                    titlePanel.add(orderAmount);

                    final Entity customer = Database.getCustomerByName((String) customerOptions.getSelectedItem());
                    final int traderId = customer.getId();

                    final ArrayList<JLabel> productLabels = new ArrayList<JLabel>();
                    final ArrayList<JTextField> currentStockFields = new ArrayList<JTextField>();
                    final ArrayList<JTextField> orderAmountFields = new ArrayList<JTextField>();

                    JPanel productQuantPanel = new JPanel(new GridLayout(0, 3));
                    productQuantPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

                    int i = 0;
                    for (final Entity p : Database.getProducts()) {
                        final Product product = (Product) p;

                        JLabel pl = new JLabel(product.getName());
                        pl.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
                        productLabels.add(pl);

                        JTextField tF = new JTextField(product.getCurrentStockLevel() + "/" + product.getMaxStockLevel(), 5);
                        tF.setEnabled(false);
                        currentStockFields.add(tF);
                        JTextField orderField = new JTextField("0", 5);
                        orderField.setEnabled(product.getCurrentStockLevel() != 0);
                        orderAmountFields.add(orderField);

                        productQuantPanel.add(productLabels.get(i));
                        productQuantPanel.add(currentStockFields.get(i));
                        productQuantPanel.add(orderAmountFields.get(i));
                        i++;
                    }
                    JScrollPane scroll = new JScrollPane(productQuantPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scroll.setBackground(GuiCreator.BACKGROUND_COLOUR);

                    JButton saveButton = new JButton("Save");
                    saveButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            boolean valid = true;
                            int count = 0;

                            for (JLabel label : productLabels) {
                                if (orderAmountFields.get(productLabels.indexOf(label)).getText().length() == 0
                                        || Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) > ((Product) Database
                                                .getProducts().get(productLabels.indexOf(label))).getCurrentStockLevel()) {
                                    valid = false;
                                    orderAmountFields.get(productLabels.indexOf(label)).setBorder(
                                            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
                                } else {
                                    orderAmountFields.get(productLabels.indexOf(label)).setBorder(UIManager.getBorder("TextField.border"));
                                    count += Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText());
                                }
                            }

                            if (count == 0) {
                                for (JLabel label : productLabels) {
                                    orderAmountFields.get(productLabels.indexOf(label)).setBorder(
                                            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
                                }
                                valid = false;
                            }

                            if (valid) {
                                ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();

                                for (final Entity p : Database.getProducts()) {
                                    final Product product = (Product) p;

                                    for (JLabel label : productLabels) {
                                        if (label.getText().equals(product.getName())
                                                && Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) > 0) {
                                            if (product.getCurrentStockLevel() >= Integer.parseInt(orderAmountFields
                                                    .get(productLabels.indexOf(label)).getText()))
                                                items.add(new OrderedItem(product, Integer.parseInt(orderAmountFields.get(
                                                        productLabels.indexOf(label)).getText())));
                                        }
                                    }
                                }

                                Customer activeCustomer = null;

                                if (items.size() > 0) {

                                    Database.getOrders().add(new Order(traderId, DATE_FORMATTER.format(new Date()), items, true));

                                    for (final Entity customer : Database.getCustomers()) {
                                        if (customer.getId() == traderId) {
                                            activeCustomer = (Customer) customer;
                                            ((Customer) customer).setLastPurchase(DATE_FORMATTER.format(new Date()));
                                        }
                                    }
                                }
                                GuiCreator.setConfirmationMessage(String.format(CUSTOMER_ORDER_CREATED_MESSAGE_FORMAT,
                                        ID_FORMATTER.format(Database.getLastOrderId()), activeCustomer.getName()));

                                GuiCreator.frame.remove(GuiCreator.leftPanel);
                                GuiCreator.mainPanel.validate();
                                GuiCreator.frame.repaint();
                                GuiCreator.frame.validate();
                                createTableGui();
                            }
                        }
                    });

                    JButton cancelButton = new JButton("Cancel");
                    cancelButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            GuiCreator.frame.remove(GuiCreator.leftPanel);
                            GuiCreator.mainPanel.validate();
                            GuiCreator.frame.repaint();
                            GuiCreator.frame.validate();
                        }
                    });
                    innerPanel = new JPanel(new BorderLayout());
                    JPanel outerTitlePanel = new JPanel(new BorderLayout());
                    JPanel buttonPanel = new JPanel(new BorderLayout());
                    JPanel buttonPanel2 = new JPanel(new GridBagLayout());

                    innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    outerTitlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    buttonPanel2.setBackground(GuiCreator.BACKGROUND_COLOUR);

                    buttonPanel.add(new JLabel("Customer: "), BorderLayout.WEST);
                    buttonPanel.add(customerOptions);

                    outerTitlePanel.add(buttonPanel, BorderLayout.NORTH);
                    outerTitlePanel.add(titlePanel, BorderLayout.SOUTH);

                    innerPanel.add(outerTitlePanel, BorderLayout.NORTH);
                    innerPanel.add(scroll, BorderLayout.CENTER);

                    GridBagConstraints g = new GridBagConstraints();
                    g.insets = new Insets(1, 10, 0, 5);

                    g = new GridBagConstraints();
                    g.insets = new Insets(1, 0, 0, 0);
                    g.gridx = 1;
                    g.gridy = 0;
                    buttonPanel2.add(saveButton, g);

                    g.gridx = 3;
                    g.gridy = 0;
                    buttonPanel2.add(cancelButton, g);

                    innerPanel.add(buttonPanel2, BorderLayout.SOUTH);

                    // Add innerPanel
                    GuiCreator.leftPanel.add(innerPanel);

                    // Update ShopDriver.frame
                    GuiCreator.setFrame(true, false, false);
                }
            }
        });
        innerPanel.add(new JLabel("Customer: "), BorderLayout.WEST);
        innerPanel.add(customerOptions);
        GuiCreator.leftPanel.add(innerPanel);

        // Update ShopDriver.frame
        GuiCreator.setFrame(true, false, false);
    }

    public void createSupplierOrder(int supplierId) {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();
        innerPanel = new JPanel(new BorderLayout(0, 2));
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final ArrayList<Entity> suppliers = Database.getSuppliers();
        final int numberOfSuppliers = suppliers.size();
        final String[] supplierArray = new String[numberOfSuppliers + 2];
        supplierArray[0] = "";
        int supplierIndex = 1;

        for (final Entity supplier : suppliers) {
            supplierArray[supplierIndex++] = supplier.getName();
        }

        // Add a string to the end of the array
        supplierArray[supplierArray.length - 1] = "New Supplier";

        // Create the JComboBox
        final JComboBox<String> supplierOptions = new JComboBox<String>(supplierArray);
        if (supplierId != 0) {
            supplierOptions.setSelectedIndex(supplierId - IdManager.SUPPLIER_ID_START + 1);
        }

        supplierOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (supplierOptions.getSelectedItem().equals("New Supplier")) {
                    ShopDriver.setCurrentTable(TableState.SUPPLIER);
                    addEntityHelper.addEntityPanel();
                    ShopDriver.setCurrentTable(TableState.ORDER);
                } else {
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.leftPanel = new JPanel();

                    // Add labels to panel
                    JLabel productName = new JLabel("Product");
                    JLabel currentStockLevel = new JLabel("In Stock");
                    JLabel orderAmount = new JLabel("Order Amount");

                    // Make font bold
                    productName.setFont(BOLD_LABEL_FONT);
                    currentStockLevel.setFont(BOLD_LABEL_FONT);
                    orderAmount.setFont(BOLD_LABEL_FONT);

                    final JPanel titlePanel = new JPanel(new GridLayout(0, 3));
                    titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    titlePanel.add(productName);
                    titlePanel.add(currentStockLevel);
                    titlePanel.add(orderAmount);

                    final ArrayList<JLabel> productLabels = new ArrayList<JLabel>();
                    final ArrayList<JTextField> currentStockFields = new ArrayList<JTextField>();
                    final ArrayList<JTextField> orderAmountFields = new ArrayList<JTextField>();

                    final ArrayList<Product> supplierProducts = new ArrayList<Product>();

                    final Entity supplier = Database.getSupplierByName((String) supplierOptions.getSelectedItem());
                    final int traderId = supplier.getId();
                    final ArrayList<Entity> products = Database.getProducts();

                    for (final Entity p : products) {
                        final Product product = (Product) p;
                        if (product.getSupplierId() == traderId)
                            supplierProducts.add(product);
                    }

                    JPanel productQuantityPanel = new JPanel(new GridLayout(0, 3));
                    productQuantityPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    // sp and pl
                    int i = 0;
                    for (final Product supplierProduct : supplierProducts) {
                        final JLabel productLabel = new JLabel(supplierProduct.getName());
                        productLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
                        productLabels.add(productLabel);

                        final JTextField tF = new JTextField(supplierProduct.getCurrentStockLevel() + "/" + supplierProduct.getMaxStockLevel(), 5);
                        tF.setEnabled(false);
                        currentStockFields.add(tF);
                        JTextField orderField = new JTextField("0", 5);
                        orderField.setEnabled(supplierProduct.getCurrentStockLevel() != supplierProduct.getMaxStockLevel());
                        orderAmountFields.add(orderField);

                        productQuantityPanel.add(productLabels.get(i));
                        productQuantityPanel.add(currentStockFields.get(i));
                        productQuantityPanel.add(orderAmountFields.get(i));
                        i++;
                    }
                    JScrollPane scroll = new JScrollPane(productQuantityPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                    scroll.setBackground(GuiCreator.BACKGROUND_COLOUR);

                    JButton saveButton = new JButton("Save");
                    saveButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {

                            boolean valid = true;
                            int count = 0;

                            for (JLabel label : productLabels) {
                                final int labelIndex = productLabels.indexOf(label);

                                if (orderAmountFields.get(labelIndex).getText().length() == 0
                                        || (Integer.parseInt(orderAmountFields.get(labelIndex).getText()) + ((Product) Database
                                                .getProductByIndex(labelIndex)).getCurrentStockLevel()) > ((Product) Database
                                                .getProductByIndex(labelIndex)).getMaxStockLevel()) {
                                    valid = false;
                                    orderAmountFields.get(productLabels.indexOf(label)).setBorder(
                                            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
                                } else {
                                    orderAmountFields.get(labelIndex).setBorder(UIManager.getBorder("TextField.border"));
                                    count += Integer.parseInt(orderAmountFields.get(labelIndex).getText());
                                }
                            }

                            if (count == 0) {
                                for (JLabel label : productLabels) {
                                    orderAmountFields.get(productLabels.indexOf(label)).setBorder(
                                            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
                                }
                                valid = false;
                            }

                            if (valid) {
                                final ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();

                                for (final Entity p : products) {
                                    final Product product = (Product) p;

                                    for (final JLabel label : productLabels) {
                                        if (label.getText().equals(product.getName())) {
                                            if (Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) > 0) {
                                                items.add(new OrderedItem(product, Integer.parseInt(orderAmountFields.get(
                                                        productLabels.indexOf(label)).getText())));
                                            }
                                        }
                                    }
                                }

                                Entity activeSupplier = null;

                                if (items.size() > 0) {
                                    Database.addOrder(new Order(traderId, DATE_FORMATTER.format(new Date()), items, true));

                                    for (Entity s : suppliers) {
                                        if (s.getId() == traderId) {
                                            ((Supplier) s).setLastPurchase(DATE_FORMATTER.format(new Date()));
                                            activeSupplier = s;
                                        }
                                    }

                                }

                                GuiCreator.setConfirmationMessage(String.format(SUPPLIER_ORDER_CREATED_MESSAGE_FORMAT,
                                        ID_FORMATTER.format(Database.getLastOrderId()), activeSupplier.getName()));

                                GuiCreator.frame.remove(GuiCreator.leftPanel);
                                GuiCreator.frame.repaint();
                                GuiCreator.frame.validate();
                                createTableGui();
                            }
                        }
                    });

                    JButton cancelButton = new JButton("Cancel");
                    cancelButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            GuiCreator.frame.remove(GuiCreator.leftPanel);
                            GuiCreator.mainPanel.validate();
                            GuiCreator.frame.repaint();
                            GuiCreator.frame.validate();
                        }
                    });
                    innerPanel = new JPanel(new BorderLayout());
                    JPanel outerTitlePanel = new JPanel(new BorderLayout());
                    JPanel buttonPanel = new JPanel(new BorderLayout());
                    JPanel buttonPanel2 = new JPanel(new GridBagLayout());

                    innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    outerTitlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    buttonPanel2.setBackground(GuiCreator.BACKGROUND_COLOUR);

                    buttonPanel.add(new JLabel("Supplier: "), BorderLayout.WEST);
                    buttonPanel.add(supplierOptions);

                    outerTitlePanel.add(buttonPanel, BorderLayout.NORTH);
                    outerTitlePanel.add(titlePanel, BorderLayout.SOUTH);

                    innerPanel.add(outerTitlePanel, BorderLayout.NORTH);
                    innerPanel.add(scroll, BorderLayout.CENTER);

                    GridBagConstraints g = new GridBagConstraints();
                    g.insets = new Insets(1, 10, 0, 5);

                    g = new GridBagConstraints();
                    g.insets = new Insets(1, 0, 0, 0);
                    g.gridx = 1;
                    g.gridy = 0;
                    buttonPanel2.add(saveButton, g);

                    g.gridx = 3;
                    g.gridy = 0;
                    buttonPanel2.add(cancelButton, g);

                    innerPanel.add(buttonPanel2, BorderLayout.SOUTH);

                    GuiCreator.leftPanel.add(innerPanel);

                    GuiCreator.setFrame(true, false, false);
                }
            }
        });
        innerPanel.add(new JLabel("Supplier: "), BorderLayout.WEST);
        innerPanel.add(supplierOptions);

        GuiCreator.leftPanel.add(innerPanel);
        GuiCreator.setFrame(true, false, false);
    }

    private void completeOrder(int orderId) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(String.format(ORDER_CONFIRMATION_MESSAGE_FORMAT, ID_FORMATTER.format(orderId))));

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            final Order order = (Order) Database.getOrderById(orderId);
            order.completeOrder(DATE_FORMATTER.format(new Date()));
            GuiCreator.setConfirmationMessage(String.format(ORDER_COMPLETED_MESSAGE_FORMAT, orderId));
        }

        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.validate();
        createTableGui();
    }

    public void sortItems() {
        Comparator<Entity> comparator = Order.getComparator(sortType);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getOrders(), comparator);
    }

    @Override
    protected void reverseSortOrder() {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getCurrentSortOption() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setCurrentSortOption(String sortOption) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String[] getSortOptionTitles() {
        // TODO Auto-generated method stub
        return null;
    }
}
