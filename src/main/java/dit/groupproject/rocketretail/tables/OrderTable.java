package dit.groupproject.rocketretail.tables;

import static dit.groupproject.rocketretail.utilities.DateHandler.DATE_FORMATTER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.MenuGUI;
import dit.groupproject.rocketretail.gui.TableState;
import dit.groupproject.rocketretail.main.ShopDriver;

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
public class OrderTable extends BaseTable {

    /**
     * A String used to define how the JTable is sorted. Retains value if JTable
     * re-called.
     * 
     * @see #createTable()
     * @see #sortArrayList()
     */
    static String type = "Sort by...";

    /**
     * A String used to define how the JTable is filtered. Retains value if
     * JTable re-called.
     * 
     * @see #createTable()
     */
    static String filter = "Show All Orders";

    /**
     * A boolean which is set true if table is called for the first time (or
     * first time since system has been re-logged into.<br />
     * If true, sorts table by ID.
     * 
     * @see #SortByID(boolean)
     */
    public static boolean first = true;

    /**
     * A boolean which causes all sort options to sort in reverse order.<br />
     * Set to true if chosen sort option (stored in {@link #type}) is already
     * selected.
     * 
     * @see #sortArrayList()
     * @see #SortByID(boolean)
     * @see #SortByStaffID(boolean)
     * @see #SortByTraderID(boolean)
     * @see #SortByTotalPrice(boolean)
     * @see #SortByOrderDate(boolean)
     * @see #SortByActive(boolean)
     */
    static boolean reverse = false;

    /**
     * An Integer which holds the value of the trader ID for the current order.
     */
    static int traderId = 0;

    /**
     * The JPanel which holds the JTable and JButtons and places them into
     * mainPanel.
     * 
     * @see #createTable()
     * @see ShopDriver#mainPanel
     */
    static JPanel innerPanel;

    /**
     * The JTable which displays the information of all orders.
     */
    static JTable table = new JTable();

    /**
     * The JCombobox which specifies the create options available -
     * Supplier/Customer for Managers, Customer only for Employees.
     */
    static JComboBox<String> createBox;

    // Methods
    /**
     * Creates the JMenuItem for "Order" and defines the ActionListener for the
     * JMenuItem.<br />
     * The ActionListener calls the {@link #createTable()} method.
     * 
     * @return the JMenuItem for the "Database" JMenuItem in
     *         {@link MenuGUI#createMenuBar(JMenuBar, boolean)}
     * 
     * @see #createTable()
     * @see MenuGUI#createMenuBar(JMenuBar, boolean)
     */
    public static JMenuItem createMenu() {
        JMenuItem orderItem = new JMenuItem("Orders");
        orderItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        // Return menuItem
        return orderItem;
    }

    /**
     * Created the JTable for Orders, using data from {@link ShopDriver#orders}.<br />
     * Adds JButtons to create or complete orders, and to sort or filter the
     * table.<br />
     * Calls {@link #showOrderInfo(Order)} when an order is selected from the
     * table.
     */
    public static void createTable() {
        if (!(ShopDriver.getCurrentTableState() == TableState.ORDER)) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
        }

        ShopDriver.setCurrentTable(TableState.ORDER);

        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - Orders");
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        JComboBox<String> tempBox = new JComboBox<String>();

        // When first run, ensure ArrayList (and table) is sorted by ID
        if (first) {
            SortByID(false);
            first = false;
        }

        int activeCount = 0;

        String[] columnNames = { "Order ID", "Staff ID", "Trader ID", "Total Price", "Order Date", "Delivery Date" };

        if (filter.equals("Show All Orders")) {
            Object[][] data = new Object[Database.getOrders().size()][6];
            double totalPrice = 0;
            String delivery = " ";

            for (int i = 0; i < Database.getOrders().size(); i++) {

                totalPrice = 0;

                if (Database.getOrders().get(i).isSupplier())
                    totalPrice = Database.getOrders().get(i).getTotalCost();

                else
                    totalPrice = Database.getOrders().get(i).getTotalSale();

                if (Database.getOrders().get(i).getDeliveryDate().length() == 0)
                    delivery = " ";
                else
                    delivery = Database.getOrders().get(i).getDeliveryDate();

                data[i][0] = ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                data[i][1] = STAFF_ID_FORMATTER.format(Database.getOrders().get(i).getStaffId());
                data[i][2] = Database.getOrders().get(i).getTraderId();
                data[i][3] = "€" + CURRENCY_FORMATTER.format(totalPrice);
                data[i][4] = Database.getOrders().get(i).getOrderDate();
                data[i][5] = delivery;
            }
            table = new JTable(data, columnNames);

            for (Order o : Database.getOrders()) {
                if (o.isActive())
                    activeCount++;
            }

            int arrayIndex = 2;
            String[] orderArrayComplete = new String[activeCount + 2];
            orderArrayComplete[0] = "Complete Order";
            orderArrayComplete[1] = "Complete All Orders";

            for (int i = 0; i < Database.getOrders().size() + 1; i++) {
                if (i < Database.getOrders().size() && Database.getOrders().get(i).isActive()) {
                    orderArrayComplete[arrayIndex] = "ID: " + ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    arrayIndex++;
                }
            }

            tempBox = new JComboBox<String>(orderArrayComplete);
        }

        else if (filter.equals("Show Supplier Orders")) {
            int supplierSize = 0;
            for (Order o : Database.getOrders()) {
                if (o.isSupplier())
                    supplierSize++;
            }

            Object[][] data = new Object[supplierSize][6];
            String delivery = " ";
            int supplierIndex = 0;
            for (int i = 0; i < Database.getOrders().size(); i++) {

                if (Database.getOrders().get(i).isSupplier()) {

                    if (Database.getOrders().get(i).getDeliveryDate().length() == 0)
                        delivery = " ";
                    else
                        delivery = Database.getOrders().get(i).getDeliveryDate();

                    data[supplierIndex][0] = ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    data[supplierIndex][1] = STAFF_ID_FORMATTER.format(Database.getOrders().get(i).getStaffId());
                    data[supplierIndex][2] = Database.getOrders().get(i).getTraderId();
                    data[supplierIndex][3] = "€" + CURRENCY_FORMATTER.format(Database.getOrders().get(i).getTotalCost());
                    data[supplierIndex][4] = Database.getOrders().get(i).getOrderDate();
                    data[supplierIndex][5] = delivery;
                    supplierIndex++;
                }
            }
            table = new JTable(data, columnNames);
            for (Order o : Database.getOrders()) {
                if (o.isActive() && o.isSupplier())
                    activeCount++;
            }

            int arrayIndex = 2;
            String[] orderArrayComplete = new String[activeCount + 2];
            orderArrayComplete[0] = "Complete Supplier Orders";
            orderArrayComplete[1] = "Complete All Supplier Orders";

            for (int i = 0; i < Database.getOrders().size() + 1; i++) {
                if (i < Database.getOrders().size() && Database.getOrders().get(i).isActive() && Database.getOrders().get(i).isSupplier()) {
                    orderArrayComplete[arrayIndex] = "ID: " + ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    arrayIndex++;
                }
            }

            tempBox = new JComboBox<String>(orderArrayComplete);

        }

        else if (filter.equals("Show Customer Orders")) {
            int customerSize = 0;
            for (Order o : Database.getOrders()) {
                if (!o.isSupplier())
                    customerSize++;
            }

            Object[][] data = new Object[customerSize][6];
            String delivery = " ";
            int customerIndex = 0;
            for (int i = 0; i < Database.getOrders().size(); i++) {

                if (!Database.getOrders().get(i).isSupplier()) {

                    if (Database.getOrders().get(i).getDeliveryDate() == null)
                        delivery = " ";
                    else
                        delivery = Database.getOrders().get(i).getDeliveryDate();

                    data[customerIndex][0] = ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    data[customerIndex][1] = STAFF_ID_FORMATTER.format(Database.getOrders().get(i).getStaffId());
                    data[customerIndex][2] = Database.getOrders().get(i).getTraderId();
                    data[customerIndex][3] = "€" + CURRENCY_FORMATTER.format(Database.getOrders().get(i).getTotalSale());
                    data[customerIndex][4] = Database.getOrders().get(i).getOrderDate();
                    data[customerIndex][5] = delivery;
                    customerIndex++;
                }
            }
            table = new JTable(data, columnNames);

            for (Order o : Database.getOrders()) {
                if (o.isActive() && !o.isSupplier())
                    activeCount++;
            }

            int arrayIndex = 2;
            String[] orderArrayComplete = new String[activeCount + 2];
            orderArrayComplete[0] = "Complete Customer Orders";
            orderArrayComplete[1] = "Complete All Customer Orders";

            for (int i = 0; i < Database.getOrders().size() + 1; i++) {
                if (i < Database.getOrders().size() && Database.getOrders().get(i).isActive() && !Database.getOrders().get(i).isSupplier()) {
                    orderArrayComplete[arrayIndex] = "ID: " + ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    arrayIndex++;
                }
            }
            tempBox = new JComboBox<String>(orderArrayComplete);
        }

        else if (filter.equals("Show Active Orders")) {
            int activeSize = 0;
            for (Order o : Database.getOrders()) {
                if (o.isActive())
                    activeSize++;
            }

            Object[][] data = new Object[activeSize][6];
            String delivery = " ";
            int customerIndex = 0;
            for (int i = 0; i < Database.getOrders().size(); i++) {

                if (Database.getOrders().get(i).isActive()) {
                    if (Database.getOrders().get(i).getDeliveryDate() == null)
                        delivery = " ";
                    else
                        delivery = Database.getOrders().get(i).getDeliveryDate();

                    data[customerIndex][0] = ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    data[customerIndex][1] = STAFF_ID_FORMATTER.format(Database.getOrders().get(i).getStaffId());
                    data[customerIndex][2] = Database.getOrders().get(i).getTraderId();
                    data[customerIndex][3] = "€" + CURRENCY_FORMATTER.format(Database.getOrders().get(i).getTotalCost());
                    data[customerIndex][4] = Database.getOrders().get(i).getOrderDate();
                    data[customerIndex][5] = delivery;
                    customerIndex++;
                }
            }
            table = new JTable(data, columnNames);

            for (Order o : Database.getOrders()) {
                if (o.isActive())
                    activeCount++;
            }

            int arrayIndex = 2;
            String[] orderArrayComplete = new String[activeCount + 2];
            orderArrayComplete[0] = "Complete Active Orders";
            orderArrayComplete[1] = "Complete All Orders";

            for (int i = 0; i < Database.getOrders().size() + 1; i++) {
                if (i < Database.getOrders().size() && Database.getOrders().get(i).isActive()) {
                    orderArrayComplete[arrayIndex] = "ID: " + ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                    arrayIndex++;
                }
            }
            tempBox = new JComboBox<String>(orderArrayComplete);
        }

        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0) { // Invalid selections return
                                                   // -1
                    Order input = null;

                    for (Order o : Database.getOrders()) {
                        if (o.getOrderId() == Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)))
                            input = o;
                    }

                    showOrderInfo(input);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        String[] createOptionsManager = { "Create Order", "Customer Order", "Supplier Order" };
        String[] createOptionsEmployee = { "Create Order", "Customer Order" };
        final Staff currentStaff = ShopDriver.getCurrentStaff();

        if (currentStaff.getStaffLevel() == 1) {
            createBox = new JComboBox<String>(createOptionsManager);
        } else if (currentStaff.getStaffLevel() == 2) {
            createBox = new JComboBox<String>(createOptionsEmployee);
        }

        final JComboBox<String> completeOptions = tempBox;
        if (activeCount == 0)
            completeOptions.setEnabled(false);

        String[] options = { "Sort by...", "Order ID", "Staff ID", "Trader ID", "Total Price", "Order Date", "Active" };
        final JComboBox<String> sortOptions = new JComboBox<String>(options);
        int sortIndex = 0;

        if (type.equals("Sort by..."))
            sortIndex = 0;
        if (type.equals("Order ID"))
            sortIndex = 1;
        if (type.equals("Staff ID"))
            sortIndex = 2;
        if (type.equals("Trader ID"))
            sortIndex = 3;
        if (type.equals("Total Price"))
            sortIndex = 4;
        if (type.equals("Order Date"))
            sortIndex = 5;
        if (type.equals("Active"))
            sortIndex = 6;
        sortOptions.setSelectedIndex(sortIndex);

        String[] filterOptions = { "Show All Orders", "Show Supplier Orders", "Show Customer Orders", "Show Active Orders" };
        final JComboBox<String> showOptions = new JComboBox<String>(filterOptions);
        int filterIndex = 0;

        if (filter.equals("Show All Orders"))
            filterIndex = 0;
        if (filter.equals("Show Supplier Orders"))
            filterIndex = 1;
        if (filter.equals("Show Customer Orders"))
            filterIndex = 2;
        if (filter.equals("Show Active Orders"))
            filterIndex = 3;
        showOptions.setSelectedIndex(filterIndex);

        createBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (createBox.getSelectedItem().equals("Customer Order"))
                    createCustomerOrder();
                else if (createBox.getSelectedItem().equals("Supplier Order"))
                    createSupplierOrder(0);
            }
        });
        completeOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (completeOptions.getSelectedItem().equals("Complete Order")) {
                } else if (completeOptions.getSelectedItem().equals("Complete Customer Order")) {
                } else if (completeOptions.getSelectedItem().equals("Complete Supplier Order")) {
                } else if (completeOptions.getSelectedItem().equals("Complete All Supplier Orders")) {

                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Do you want to complete ALL supplier orders?"));

                    if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
                        int count = 0;
                        for (Order o : Database.getOrders()) {
                            if (o.isActive() && o.isSupplier()) {
                                o.completeOrder(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                count++;
                            }
                        }
                        GuiCreator.setConfirmationMessage(count + " orders completed");
                        createTable();
                    }
                } else if (completeOptions.getSelectedItem().equals("Complete All Customer Orders")) {

                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Do you want to complete ALL customer orders?"));

                    if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
                        int count = 0;
                        for (Order o : Database.getOrders()) {
                            if (o.isActive() && !o.isSupplier()) {
                                o.completeOrder(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                count++;
                            }
                        }
                        GuiCreator.setConfirmationMessage(count + " orders completed");
                        createTable();
                    }

                } else if (completeOptions.getSelectedItem().equals("Complete All Orders")) {

                    JPanel myPanel = new JPanel();
                    myPanel.add(new JLabel("Do you want to complete ALL orders?"));

                    if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
                        int count = 0;
                        for (Order o : Database.getOrders()) {
                            if (o.isActive()) {
                                o.completeOrder(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                count++;
                            }
                        }
                        GuiCreator.setConfirmationMessage(count + " orders completed");
                        createTable();
                    }
                } else
                    completeOrder(Integer.parseInt(((String) completeOptions.getSelectedItem()).substring(4, 8)));

            }
        });

        sortOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // If already sorted by type, reverse order
                if (sortOptions.getSelectedItem().equals("Sort by...")) {
                }

                else if (type.equals((String) sortOptions.getSelectedItem())) {
                    if (reverse)
                        reverse = false;
                    else
                        reverse = true;
                    sortArrayList();
                }

                // Else sort in ascending order
                else {
                    type = (String) sortOptions.getSelectedItem();
                    sortArrayList();
                }
                createTable();
            }
        });

        showOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filter = (String) showOptions.getSelectedItem();
                createTable();
            }
        });

        buttonPanel.add(createBox);
        buttonPanel.add(completeOptions);
        buttonPanel.add(sortOptions);
        buttonPanel.add(showOptions);

        GuiCreator.mainPanel.add(scrollPane, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Update ShopDriver.frame
        GuiCreator.setFrame(false, false, true);
    }

    /**
     * Creates a customer order in {@link ShopDriver#leftPanel}. Shows a
     * JComboBox with customer names/IDs, and upon selection, shows the list of
     * available products, and their current/max stock levels.
     * 
     * @see ShopDriver#leftPanel
     */
    public static void createCustomerOrder() {
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
            customerArray[i] = ((Customer) Database.getCustomers().get(i - 1)).getCustomerName();
        }

        // Add a string to the end of the array
        customerArray[customerArray.length - 1] = "New Customer";

        // Create the JComboBox
        final JComboBox<String> customerOptions = new JComboBox<String>(customerArray);

        customerOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (customerOptions.getSelectedItem().equals("New Customer"))
                    CustomerTable.add();
                else {
                    // Reset ShopDriver.frame
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.leftPanel = new JPanel();

                    // Add labels to panel
                    JLabel productName = new JLabel("Product");
                    JLabel currentStockLevel = new JLabel("Current/Max");
                    JLabel orderAmount = new JLabel("Order Amount");

                    // Make font bold
                    productName.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));
                    currentStockLevel.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));
                    orderAmount.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));

                    JPanel titlePanel = new JPanel(new GridLayout(0, 3));
                    titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    titlePanel.add(productName);
                    titlePanel.add(currentStockLevel);
                    titlePanel.add(orderAmount);

                    for (Entity c : Database.getCustomers()) {
                        if (((Customer) c).getCustomerName().equals(customerOptions.getSelectedItem()))
                            traderId = c.getId();
                    }

                    final ArrayList<JLabel> productLabels = new ArrayList<JLabel>();
                    final ArrayList<JTextField> currentStockFields = new ArrayList<JTextField>();
                    final ArrayList<JTextField> orderAmountFields = new ArrayList<JTextField>();

                    JPanel productQuantPanel = new JPanel(new GridLayout(0, 3));
                    productQuantPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

                    int i = 0;
                    for (Product p : Database.getProducts()) {

                        JLabel pl = new JLabel("" + p.getProductDescription());
                        pl.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
                        productLabels.add(pl);

                        JTextField tF = new JTextField("" + p.getStockLevel() + "/" + p.getMaxLevel(), 5);
                        tF.setEnabled(false);
                        currentStockFields.add(tF);
                        JTextField orderField = new JTextField("0", 5);
                        if (p.getStockLevel() == 0)
                            orderField.setEnabled(false);
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
                                        || Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) > Database.getProducts()
                                                .get(productLabels.indexOf(label)).getStockLevel()) {
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

                                for (Product p : Database.getProducts()) {
                                    for (JLabel label : productLabels) {
                                        if (label.getText().equals(p.getProductDescription())
                                                && Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) > 0) {
                                            if (p.getStockLevel() >= Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()))
                                                items.add(new OrderedItem(p, Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label))
                                                        .getText())));
                                        }
                                    }
                                }

                                Customer activeCustomer = null;

                                if (items.size() > 0) {

                                    if (items.size() > 0)
                                        Database.getOrders().add(new Order(traderId, DATE_FORMATTER.format(new Date()), items, true));

                                    for (final Entity customer : Database.getCustomers()) {
                                        if (customer.getId() == traderId) {
                                            activeCustomer = (Customer) customer;
                                            ((Customer) customer).setLastPurchase(DATE_FORMATTER.format(new Date()));
                                        }
                                    }
                                }
                                GuiCreator.setConfirmationMessage("Order #"
                                        + ORDER_ID_FORMATTER.format(Database.getOrders().get(Database.getOrders().size() - 1).getOrderId())
                                        + " created for customer \"" + activeCustomer.getCustomerName() + "\"");

                                // Reset ShopDriver.frame
                                GuiCreator.frame.remove(GuiCreator.leftPanel);
                                GuiCreator.mainPanel.validate();
                                GuiCreator.frame.repaint();
                                GuiCreator.frame.validate();
                                createTable();
                            }
                        }
                    });// submitOrder

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

                    buttonPanel.add(new JLabel("Customer:  "), BorderLayout.WEST);
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
        innerPanel.add(new JLabel("Customer:  "), BorderLayout.WEST);
        innerPanel.add(customerOptions);
        GuiCreator.leftPanel.add(innerPanel);

        // Update ShopDriver.frame
        GuiCreator.setFrame(true, false, false);
    }

    /**
     * Creates a supplier order in {@link ShopDriver#leftPanel}. Shows a
     * JComboBox with supplier names/IDs, and upon selection, shows the list of
     * their products, and the current/max levels we currently hold.
     * 
     * @param supplierIndex
     *            an Integer specifying the supplier's index in the JComboBox
     *            from {@link ProductTable#showProductInfo(Product)}
     */
    public static void createSupplierOrder(int supplierIndex) {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();
        innerPanel = new JPanel(new BorderLayout(0, 2));
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        // Create array of supplier names for JComboBox
        String[] supplierArray = new String[Database.getSuppliers().size() + 2];
        supplierArray[0] = "";
        for (int i = 1; i < Database.getSuppliers().size() + 1; i++) {
            supplierArray[i] = Database.getSuppliers().get(i - 1).getSupplierName();
        }

        // Add a string to the end of the array
        supplierArray[supplierArray.length - 1] = "New Supplier";

        // Create the JComboBox
        final JComboBox<String> supplierOptions = new JComboBox<String>(supplierArray);
        if (supplierIndex != 0) {
            supplierOptions.setSelectedIndex(supplierIndex - IdManager.SUPPLIER_ID_START + 1);
        }

        supplierOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (supplierOptions.getSelectedItem().equals("New Supplier"))
                    SupplierTable.add();
                else {
                    // Reset ShopDriver.frame
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.leftPanel = new JPanel();

                    // Add labels to panel
                    JLabel productName = new JLabel("Product");
                    JLabel currentStockLevel = new JLabel("In Stock");
                    JLabel orderAmount = new JLabel("Order Amount");

                    // Make font bold
                    productName.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));
                    currentStockLevel.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));
                    orderAmount.setFont(new Font(productName.getFont().getFontName(), Font.BOLD, productName.getFont().getSize()));

                    JPanel titlePanel = new JPanel(new GridLayout(0, 3));
                    titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    titlePanel.add(productName);
                    titlePanel.add(currentStockLevel);
                    titlePanel.add(orderAmount);

                    final ArrayList<JLabel> productLabels = new ArrayList<JLabel>();
                    final ArrayList<JTextField> currentStockFields = new ArrayList<JTextField>();
                    final ArrayList<JTextField> orderAmountFields = new ArrayList<JTextField>();

                    // Add products and quantity text fields
                    ArrayList<Product> supplierProducts = new ArrayList<Product>();

                    for (Supplier s : Database.getSuppliers()) {
                        if (s.getSupplierName().equals(supplierOptions.getSelectedItem()))
                            traderId = s.getSupplierId();
                    }

                    for (Product p : Database.getProducts()) {
                        if (p.getSupplierId() == traderId)
                            supplierProducts.add(p);
                    }

                    JPanel productQuantityPanel = new JPanel(new GridLayout(0, 3));
                    productQuantityPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                    // sp and pl
                    int i = 0;
                    for (Product sp : supplierProducts) {
                        JLabel pl = new JLabel("" + sp.getProductDescription());
                        pl.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0));
                        productLabels.add(pl);

                        JTextField tF = new JTextField("" + sp.getStockLevel() + "/" + sp.getMaxLevel(), 5);
                        tF.setEnabled(false);
                        currentStockFields.add(tF);
                        JTextField orderField = new JTextField("0", 5);
                        if (sp.getStockLevel() == sp.getMaxLevel())
                            orderField.setEnabled(false);
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
                                if (orderAmountFields.get(productLabels.indexOf(label)).getText().length() == 0
                                        || (Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) + Database.getProducts()
                                                .get(productLabels.indexOf(label)).getStockLevel()) > Database.getProducts()
                                                .get(productLabels.indexOf(label)).getMaxLevel()) {
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

                                for (Product p : Database.getProducts()) {
                                    for (JLabel label : productLabels) {
                                        if (label.getText().equals(p.getProductDescription())) {
                                            if (Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label)).getText()) > 0)
                                                items.add(new OrderedItem(p, Integer.parseInt(orderAmountFields.get(productLabels.indexOf(label))
                                                        .getText())));
                                        }
                                    }
                                }

                                Supplier activeSupp = null;

                                if (items.size() > 0) {
                                    Database.addOrder(new Order(traderId, DATE_FORMATTER.format(new Date()), items, true));

                                    for (Supplier s : Database.getSuppliers()) {
                                        if (s.getSupplierId() == traderId) {
                                            s.setLastPurchase(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                                            activeSupp = s;
                                        }
                                    }

                                }

                                GuiCreator.setConfirmationMessage("Order #"
                                        + ORDER_ID_FORMATTER.format(Database.getOrders().get(Database.getOrders().size() - 1).getOrderId())
                                        + " created for supplier \"" + activeSupp.getSupplierName() + "\"");

                                // Reset ShopDriver.frame
                                GuiCreator.frame.remove(GuiCreator.leftPanel);
                                GuiCreator.frame.repaint();
                                GuiCreator.frame.validate();
                                createTable();
                            }
                        }
                    });// submitOrder

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

                    buttonPanel.add(new JLabel("Supplier:  "), BorderLayout.WEST);
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

                    // Add innerPanel
                    GuiCreator.leftPanel.add(innerPanel);

                    // Update ShopDriver.frame
                    GuiCreator.setFrame(true, false, false);
                }
            }
        });
        innerPanel.add(new JLabel("Supplier:  "), BorderLayout.WEST);
        innerPanel.add(supplierOptions);

        // Add innerPanel to leftPanel
        GuiCreator.leftPanel.add(innerPanel);

        // Update ShopDriver.frame
        GuiCreator.setFrame(true, false, false);
    }

    /**
     * Completes an order, at the current date. If a supplier order, the stock
     * levels are updated by {@link Order}.
     * 
     * @param orderId
     *            an Integer specifying the order to be completed
     * 
     * @see Order
     */
    public static void completeOrder(int orderId) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Are you sure you want to complete order #" + ORDER_ID_FORMATTER.format(orderId) + "?"));

        final int result = showDialog("Please confirm", myPanel);

        if (result == JOptionPane.OK_OPTION) {
            for (final Order o : Database.getOrders()) {
                if (o.getOrderId() == orderId) {
                    o.completeOrder(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                }

            }
            GuiCreator.setConfirmationMessage("Order #" + orderId + " completed");
        }

        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.validate();
        createTable();
    }

    /**
     * Creates a new page on-screen with a detailed breakdown of an individual
     * order.<br />
     * Displays basic information (Order ID, Staff name/ID, Trader name/ID,
     * Order Date and Delivery Date (if complete).<br />
     * The JTable is updated to include a breakdown of the ordered items, and
     * their unit price, quantity, and total cost.<br />
     * Also includes a JButton which returns to {@link #createTable()}.
     * 
     * @param o
     *            the Order whose information is displayed on-screen
     * 
     * @see OrderTable#createTable()
     */
    public static void showOrderInfo(Order o) {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - Order #" + ORDER_ID_FORMATTER.format(o.getOrderId()));
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        boolean isSupplier = false;

        String staffName = "", traderName = "", traderTitle = "";

        for (Staff s : Database.getStaffMembers()) {
            if (s.getStaffId() == o.getStaffId()) {
                staffName = s.getStaffName();
                break;
            }
        }

        if (o.isSupplier()) {

            traderTitle = "Supplier";
            isSupplier = true;

            for (Supplier s : Database.getSuppliers()) {
                if (s.getSupplierId() == o.getTraderId()) {
                    traderName = s.getSupplierName();
                    break;
                }
            }
        } else if (!o.isSupplier()) {

            isSupplier = false;
            traderTitle = "Customer";

            for (Entity c : Database.getCustomers()) {
                if (c.getId() == o.getTraderId()) {
                    traderName = ((Customer) c).getCustomerName();
                    break;
                }
            }
        }

        // JLabels
        JLabel orderLabel = new JLabel("Order ID");
        JLabel staffLabel = new JLabel("Staff");
        JLabel traderLabel = new JLabel(traderTitle);
        JLabel orderDateLabel = new JLabel("Order Date");
        JLabel deliveryDateLabel = new JLabel("Delivery Date");
        JLabel titleLabel = new JLabel("Ordered Items");
        orderLabel.setFont(new Font(orderLabel.getFont().getFontName(), Font.BOLD, orderLabel.getFont().getSize()));
        staffLabel.setFont(new Font(staffLabel.getFont().getFontName(), Font.BOLD, staffLabel.getFont().getSize()));
        traderLabel.setFont(new Font(traderLabel.getFont().getFontName(), Font.BOLD, traderLabel.getFont().getSize()));
        orderDateLabel.setFont(new Font(orderDateLabel.getFont().getFontName(), Font.BOLD, orderDateLabel.getFont().getSize()));
        deliveryDateLabel.setFont(new Font(deliveryDateLabel.getFont().getFontName(), Font.BOLD, deliveryDateLabel.getFont().getSize()));
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));

        int textFieldSize = 20;
        // JTextFields
        JTextField orderField = new JTextField(ORDER_ID_FORMATTER.format(o.getOrderId()), textFieldSize);
        orderField.setEditable(false);
        JTextField staffField = new JTextField(staffName + " (" + STAFF_ID_FORMATTER.format(o.getStaffId()) + ")", textFieldSize);
        staffField.setEditable(false);
        JTextField traderField = new JTextField(traderName + " (" + o.getTraderId() + ")", textFieldSize);
        traderField.setEditable(false);
        JTextField orderDateField = new JTextField(o.getOrderDate(), textFieldSize);
        orderDateField.setEditable(false);
        JTextField deliveryDateField = new JTextField(o.getDeliveryDate(), textFieldSize);
        deliveryDateField.setEditable(false);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        titlePanel.add(orderLabel, g);
        g.gridx = 1;
        titlePanel.add(orderField, g);
        g.gridx = 2;
        titlePanel.add(staffLabel, g);
        g.gridx = 3;
        titlePanel.add(staffField, g);
        g.gridx = 4;
        titlePanel.add(traderLabel, g);
        g.gridx = 5;
        titlePanel.add(traderField, g);

        g.gridx = 0;
        g.gridy = 1;
        titlePanel.add(orderDateLabel, g);

        g.gridx = 1;
        titlePanel.add(orderDateField, g);

        if (!o.getDeliveryDate().equals("")) {

            g.gridx = 4;
            titlePanel.add(deliveryDateLabel, g);

            g.gridx = 5;
            titlePanel.add(deliveryDateField, g);
        }

        g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        g.gridy = 2;
        g.gridx = 2;
        g.gridwidth = 2;
        titlePanel.add(titleLabel, g);

        String[] columnNames = { "Ordered Product", "Unit Price", "Order Quantity", "Product Total" };
        Object[][] data = new Object[o.getOrderedItems().size() + 1][4];
        double total = 0;

        for (int i = 0; i < o.getOrderedItems().size(); i++) {
            double unitPrice = 0;

            if (isSupplier)
                unitPrice = o.getOrderedItems().get(i).getProduct().getCostPrice();
            else
                unitPrice = o.getOrderedItems().get(i).getProduct().getSalePrice();

            data[i][0] = o.getOrderedItems().get(i).getProduct().getProductDescription() + " ("
                    + o.getOrderedItems().get(i).getProduct().getProductId() + ")";
            data[i][1] = "€" + CURRENCY_FORMATTER.format(unitPrice);
            data[i][2] = o.getOrderedItems().get(i).getQuantity();
            data[i][3] = "€" + CURRENCY_FORMATTER.format(unitPrice * (int) data[i][2]);
            total += (unitPrice * (int) data[i][2]);
        }
        data[o.orderedItems.size()][2] = "<html><b>Order Total</b></html>";
        data[o.orderedItems.size()][3] = "<html><b>€" + CURRENCY_FORMATTER.format(total) + "</b></html>";

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Orders");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);

        // Update frame
        GuiCreator.setFrame(false, false, true);
    }

    /**
     * A switch-like method which decides how to sort the JTable.<br />
     * Uses class variable {@link #type} and calls appropriate sorting method
     * based on its value.
     * 
     * @see #type
     * @see #SortByID(boolean)
     * @see #SortByStaffID(boolean)
     * @see #SortByTraderID(boolean)
     * @see #SortByTotalPrice(boolean)
     * @see #SortByOrderDate(boolean)
     * @see #SortByActive(boolean)
     */
    public static void sortArrayList() {
        if (type.equals("Order ID"))
            SortByID(reverse);
        if (type.equals("Staff ID"))
            SortByStaffID(reverse);
        if (type.equals("Trader ID"))
            SortByTraderID(reverse);
        if (type.equals("Total Price"))
            SortByTotalPrice(reverse);
        if (type.equals("Order Date"))
            SortByOrderDate(reverse);
        if (type.equals("Active"))
            SortByActive(reverse);
    }

    /**
     * Sorts {@link ShopDriver#orders} by Order ID, in ascending order
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByID(boolean reverse) {
        ArrayList<Order> tempArrayList = new ArrayList<Order>();
        int count = 0, offset = 0;
        boolean found = false;

        for (int i = 0; i < Database.getOrders().size() + offset; i++) {
            found = false;
            for (Order o : Database.getOrders()) {
                if (count == o.getOrderId()) {
                    tempArrayList.add(o);
                    found = true;
                }
            }
            if (!found)
                offset++;
            count++;
        }

        Database.getOrders().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        }

    }

    /**
     * Sorts {@link ShopDriver#orders} by Staff ID, in ascending order
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByStaffID(boolean reverse) {
        int lowestID = 999999999;
        ArrayList<Order> tempArrayList = new ArrayList<Order>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;

        while (Database.getOrders().size() != 0) {
            // go through the array list and find the lowest wage listed
            for (Order o : Database.getOrders()) {
                if (lowestID > o.getStaffId())
                    lowestID = o.getStaffId();
            }
            found = false;

            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Order o : Database.getOrders()) {
                if (lowestID == o.getStaffId() && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(o);

                    // note the index of this entry
                    indexL = Database.getOrders().indexOf(o);
                    found = true;
                }
                if (found)
                    break;
            }
            Database.getOrders().remove(indexL);
            lowestID = 99999999;
        }
        Database.getOrders().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#orders} by trader ID in ascending order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByTraderID(boolean reverse) {
        int lowestID = 999999999;
        ArrayList<Order> tempArrayList = new ArrayList<Order>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;

        while (Database.getOrders().size() != 0) {
            // go through the array list and find the lowest wage listed
            for (Order o : Database.getOrders()) {
                if (lowestID > o.getTraderId())
                    lowestID = o.getTraderId();
            }
            found = false;

            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Order o : Database.getOrders()) {
                if (lowestID == o.getTraderId() && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(o);

                    // note the index of this entry
                    indexL = Database.getOrders().indexOf(o);
                    found = true;
                }
                if (found)
                    break;
            }
            Database.getOrders().remove(indexL);
            lowestID = 99999999;
        }
        Database.getOrders().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#orders} by total price in ascending order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByTotalPrice(boolean reverse) {
        double lowestTotal = 999999999;
        ArrayList<Order> tempArrayList = new ArrayList<Order>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;

        while (Database.getOrders().size() != 0) {
            // go through the array list and find the lowest wage listed

            double totalPrice = 0;

            for (Order o : Database.getOrders()) {
                if (o.isSupplier())
                    totalPrice = o.getTotalCost();
                else
                    totalPrice = o.getTotalSale();

                if (lowestTotal > totalPrice)
                    lowestTotal = totalPrice;
            }

            found = false;

            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Order o : Database.getOrders()) {

                if (o.isSupplier())
                    totalPrice = o.getTotalCost();
                else
                    totalPrice = o.getTotalSale();

                if (lowestTotal == totalPrice && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(o);

                    // note the index of this entry
                    indexL = Database.getOrders().indexOf(o);

                    found = true;
                }
                if (found)
                    break;
            }
            Database.getOrders().remove(indexL);

            lowestTotal = 99999999;
        }

        Database.getOrders().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getOrders().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#orders} by Order Date, using the sub-methods.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByOrderDate(final boolean reverse) {
        Collections.sort(Database.getOrders(), new Comparator<Order>() {
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(Order o1, Order o2) {
                try {
                    if (!reverse)
                        return f.parse(o1.getOrderDate()).compareTo(f.parse(o2.getOrderDate()));
                    else
                        return f.parse(o2.getOrderDate()).compareTo(f.parse(o1.getOrderDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    /**
     * Sorts {@link ShopDriver#orders} by active/inactive, with active first
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByActive(boolean reverse) {
        ArrayList<Order> activeArrayList = new ArrayList<Order>();
        ArrayList<Order> inactiveArrayList = new ArrayList<Order>();

        for (Order o : Database.getOrders()) {
            if (o.isActive())
                activeArrayList.add(o);
            else if (!o.isActive())
                inactiveArrayList.add(o);
        }

        Database.getOrders().clear();

        if (!reverse) {
            for (int i = 0; i < activeArrayList.size(); i++) {
                Database.getOrders().add(activeArrayList.get(i));
            }
            for (int i = 0; i < inactiveArrayList.size(); i++) {
                Database.getOrders().add(inactiveArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = 0; i < inactiveArrayList.size(); i++) {
                Database.getOrders().add(inactiveArrayList.get(i));
            }
            for (int i = 0; i < activeArrayList.size(); i++) {
                Database.getOrders().add(activeArrayList.get(i));
            }
        }
    }
}
