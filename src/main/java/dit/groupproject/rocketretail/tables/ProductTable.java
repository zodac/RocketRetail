package dit.groupproject.rocketretail.tables;

import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_CURRENT;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

import org.jfree.chart.ChartPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.Graphs;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.grroupproject.rocketretail.menus.MenuGUI;

/**
 * ProductTable adds the "Product" menu-item to the menu-bar (within
 * "Database"), and shows a JTable for all products. It populates the table with
 * information from {@link ShopDriver#products}, and includes JButtons to allow
 * a user to add, edit or delete a product.<br />
 * Sorting options are available for the table, by Name, ID, Cost Price, Sale
 * Price, Stock Level or SupplierID. Clicking on a row in the table gives more
 * information on the product, including a breakdown of orders/sales by year,
 * which is also displayed on a graph.<br />
 * Users may also place an order for the product on this page.
 */
public class ProductTable extends BaseTable {

    /**
     * A String used to define how the JTable is sorted. Retains value if JTable
     * re-called.
     * 
     * @see #createTable()
     * @see #sortArrayList()
     */
    static String type = "Sort by...";

    /**
     * A boolean which is set true if table is called for the first time (or
     * first time since system has been re-logged into.<br />
     * If true, sorts table by ID.
     * 
     * @see #sortById(boolean)
     */
    public static boolean first = true;

    /**
     * A boolean which causes all sort options to sort in reverse order.<br />
     * Set to true if chosen sort option (stored in {@link #type}) is already
     * selected.
     * 
     * @see #sortById(boolean)
     * @see #sortByDescription(boolean)
     * @see #sortByStockLevel(boolean)
     * @see #sortBySupplierId(boolean)
     * @see #sortByCostPrice(boolean)
     * @see #sortBySalePrice(boolean)
     */
    static boolean reverse = false;

    // Methods
    /**
     * Creates the JMenuItem for "Product", and defines the ActionListener for
     * the JMenuItem. <br />
     * The ActionListener calls the {@link #createTable()} method.
     * 
     * @return the JMenuItem for the "Database" JMenuItem in
     *         {@link MenuGUI#createMenuBar(JMenuBar, boolean)}
     * 
     * @see #createTable()
     * @see MenuGUI#createMenuBar(JMenuBar, boolean)
     */
    public static JMenuItem createMenu(boolean manager) {
        JMenuItem productItem = new JMenuItem("Product");
        productItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        productItem.setEnabled(manager);

        // Return menuItem
        return productItem;
    }

    /**
     * Creates the JTable for Products, using data from
     * {@link ShopDriver#products}.<br />
     * Adds JButtons to add, edit or delete a product, and implements the
     * ActionListeners for each.<br />
     * Calls {@link #showProductInfo(Product)} when a product is selected from
     * the table. *
     * 
     * @see #add()
     * @see #edit(int)
     * @see #delete(int, String)
     * @see #showProductInfo(Product)
     */
    public static void createTable() {
        if (!(ShopDriver.getCurrentTableState() == TableState.PRODUCT)) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
        }

        ShopDriver.setCurrentTable(TableState.PRODUCT);

        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - Products");
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        // When first run, ensure ArrayList (and table) is sorted by ID
        if (first) {
            sortById(false);
            first = false;
        }

        String[] columnNames = { "ID", "Description", "Stock Level", "Max Level", "Supplier ID", "Cost Price", "Sale Price" };
        Object[][] data = new Object[Database.getProducts().size()][7];

        for (int i = 0; i < Database.getProducts().size(); i++) {
            data[i][0] = PRODUCT_ID_FORMATTER.format(Database.getProducts().get(i).getProductId());
            data[i][1] = Database.getProducts().get(i).getProductDescription();
            data[i][2] = Database.getProducts().get(i).getStockLevel();
            data[i][3] = Database.getProducts().get(i).getMaxLevel();
            data[i][4] = Database.getProducts().get(i).getSupplierId();
            data[i][5] = "€" + CURRENCY_FORMATTER.format(Database.getProducts().get(i).getCostPrice());
            data[i][6] = "€" + CURRENCY_FORMATTER.format(Database.getProducts().get(i).getSalePrice());
        }

        final JTable table = new JTable(data, columnNames);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0) { // Invalid selections return
                                                   // -1
                    Product input = null;

                    for (Product p : Database.getProducts()) {
                        if (p.getProductId() == Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)))
                            input = p;
                    }
                    showProductInfo(input);
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        String[] productMemberArrayEdit = new String[Database.getProducts().size() + 1];
        productMemberArrayEdit[0] = "Edit Product";
        for (int i = 0; i < Database.getProducts().size() + 1; i++) {
            if (i < Database.getProducts().size())
                productMemberArrayEdit[i + 1] = "ID: " + PRODUCT_ID_FORMATTER.format(Database.getProducts().get(i).getProductId()) + " ("
                        + Database.getProducts().get(i).getProductDescription() + ")";
        }

        String[] productMemberArrayDelete = new String[Database.getProducts().size() + 1];
        productMemberArrayDelete[0] = "Delete Product";
        for (int i = 0; i < Database.getProducts().size() + 1; i++) {
            if (i < Database.getProducts().size())
                productMemberArrayDelete[i + 1] = "ID: " + PRODUCT_ID_FORMATTER.format(Database.getProducts().get(i).getProductId()) + " ("
                        + Database.getProducts().get(i).getProductDescription() + ")";
        }

        JButton addButton = new JButton("Add Product");
        final JComboBox<String> editBox = new JComboBox<String>(productMemberArrayEdit);
        final JComboBox<String> deleteBox = new JComboBox<String>(productMemberArrayDelete);

        String[] options = { "Sort by...", "ID", "Description", "Stock Level", "Supplier ID", "Cost Price", "Sale Price" };
        final JComboBox<String> sortOptions = new JComboBox<String>(options);
        int index = 0;

        if (type.equals("Sort by..."))
            index = 0;
        if (type.equals("ID"))
            index = 1;
        if (type.equals("Description"))
            index = 2;
        if (type.equals("Stock Level"))
            index = 3;
        if (type.equals("Supplier ID"))
            index = 4;
        if (type.equals("Cost Price"))
            index = 5;
        if (type.equals("Sale Price"))
            index = 6;
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
                        ((String) deleteBox.getSelectedItem()).substring(11, ((String) deleteBox.getSelectedItem()).length() - 1));
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

        buttonPanel.add(addButton);
        buttonPanel.add(editBox);
        buttonPanel.add(deleteBox);
        buttonPanel.add(sortOptions);

        final JScrollPane scrollPane = new JScrollPane(table);
        GuiCreator.mainPanel.add(scrollPane, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Update ShopDriver.frame
        GuiCreator.setFrame(false, false, true);
    }

    /**
     * Opens a form to input information for a new Product in the left Panel of
     * the screen. Uses GridBagLayout.<br />
     * Adds JButtons to cancel, or save new product and re-runs
     * {@link #createTable()}
     * 
     * @see #createTable()
     */
    public static void add() {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        // Panel items
        final JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        GridBagConstraints g = new GridBagConstraints();

        // JLabels with GridBagLayout
        g.gridx = 0;
        g.gridy = 0;
        innerPanel.add(new JLabel("Product ID:"), g);
        g.gridy = 1;
        innerPanel.add(new JLabel("Product Description:"), g);
        g.gridy = 2;
        innerPanel.add(new JLabel("Stock Level:"), g);
        g.gridy = 3;
        innerPanel.add(new JLabel("Maximum Level"), g);
        g.gridy = 4;
        innerPanel.add(new JLabel("Supplier ID:"), g);
        g.gridy = 5;
        innerPanel.add(new JLabel("Cost Price:"), g);
        g.gridy = 6;
        innerPanel.add(new JLabel("Sale Price:"), g);

        g.insets = new Insets(1, 10, 0, 5); // Border to left, and small one on
                                            // top as a spacer between rows
        g.fill = GridBagConstraints.HORIZONTAL;
        // JTextFields with GridBagLayout
        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField prodIDField = new JTextField(null, 20);
        prodIDField.setText("");
        prodIDField.setEditable(false);
        innerPanel.add(prodIDField, g);
        g.gridy = 1;
        g.gridwidth = 3;
        final JTextField prodDescField = new JTextField(null, 20);
        innerPanel.add(prodDescField, g);
        g.gridy = 2;
        g.gridwidth = 3;
        final JTextField stockLevelField = new JTextField(null, 20);
        innerPanel.add(stockLevelField, g);
        g.gridy = 3;
        g.gridwidth = 3;
        final JTextField maxLevelField = new JTextField(null, 20);
        innerPanel.add(maxLevelField, g);
        g.gridy = 4;
        g.gridwidth = 3;

        String[] supplierOptions = new String[Database.getSuppliers().size() + 1];

        supplierOptions[0] = "";
        for (int i = 1; i < supplierOptions.length; i++) {
            supplierOptions[i] = Database.getSuppliers().get(i - 1).getSupplierName() + " (" + Database.getSuppliers().get(i - 1).getSupplierId()
                    + ")";
        }

        final JComboBox<String> suppIDBox = new JComboBox<String>(supplierOptions);
        innerPanel.add(suppIDBox, g);
        g.gridy = 5;
        g.gridwidth = 3;
        final JTextField costPriceField = new JTextField(null, 20);
        innerPanel.add(costPriceField, g);
        g.gridy = 6;
        g.gridwidth = 3;
        final JTextField salePriceField = new JTextField(null, 20);
        innerPanel.add(salePriceField, g);

        // Spacer
        g.gridx = 0;
        g.gridy = 7;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        g.gridy = 7;
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
        g.gridy = 8;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                textFields.add(prodDescField);
                ArrayList<JTextField> intFields = new ArrayList<JTextField>();
                intFields.add(stockLevelField);
                intFields.add(maxLevelField);
                ArrayList<JTextField> doubleFields = new ArrayList<JTextField>();
                doubleFields.add(costPriceField);
                doubleFields.add(salePriceField);
                ArrayList<JComboBox<String>> comboBoxes = new ArrayList<JComboBox<String>>();
                comboBoxes.add(suppIDBox);

                boolean valid = FieldValidator.checkFields(textFields, intFields, doubleFields, null, comboBoxes, null, null);

                if (valid) {

                    Database.getProducts().add(
                            new Product(prodDescField.getText(), Integer.parseInt(stockLevelField.getText()), Integer.parseInt(maxLevelField
                                    .getText()), Integer.parseInt(((String) suppIDBox.getSelectedItem()).substring(
                                    ((String) suppIDBox.getSelectedItem()).length() - 5, ((String) suppIDBox.getSelectedItem()).length() - 1)),
                                    Double.parseDouble(costPriceField.getText()), Double.parseDouble(salePriceField.getText())));

                    GuiCreator.setConfirmationMessage("Product " + prodDescField.getText() + " added");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();

                    sortById(false);
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

        // Add innerPanel
        GuiCreator.leftPanel.add(innerPanel);

        // Update ShopDriver.frame
        GuiCreator.setFrame(true, false, false);
    }

    /**
     * Opens a form for the chosen product in the left Panel of the screen.
     * Automatically fills form with current product information.<br />
     * Adds JButtons to cancel, or save product changes and re-runs
     * {@link #createTable()}
     * 
     * @param productId
     *            an Integer specifying the product to edit
     * 
     * @see #createTable()
     */
    public static void edit(int productId) {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        for (final Product p : Database.getProducts()) {
            if (productId == p.getProductId()) {

                final int index = Database.getProducts().indexOf(p);
                // Panel items
                JPanel innerPanel = new JPanel(new GridBagLayout());
                innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
                GridBagConstraints g = new GridBagConstraints();

                // JLabels with GridBagLayout
                g.gridx = 0;
                g.gridy = 0;
                innerPanel.add(new JLabel("Product ID:"), g);
                g.gridy = 1;
                innerPanel.add(new JLabel("Product Description:"), g);
                g.gridy = 2;
                innerPanel.add(new JLabel("Stock Level:"), g);
                g.gridy = 3;
                innerPanel.add(new JLabel("Maximum Level"), g);
                g.gridy = 4;
                innerPanel.add(new JLabel("Supplier ID:"), g);
                g.gridy = 5;
                innerPanel.add(new JLabel("Cost Price:"), g);
                g.gridy = 6;
                innerPanel.add(new JLabel("Sale Price:"), g);

                g.insets = new Insets(1, 10, 0, 5);

                // JTextFields with GridBagLayout
                g.gridx = 1;
                g.gridy = 0;
                g.gridwidth = 3;
                final JTextField prodIDField = new JTextField(null, 20);
                prodIDField.setEditable(false);
                innerPanel.add(prodIDField, g);
                g.gridy = 1;
                g.gridwidth = 3;
                final JTextField prodDescField = new JTextField(null, 20);
                innerPanel.add(prodDescField, g);
                g.gridy = 2;
                g.gridwidth = 3;
                final JTextField stockLevelField = new JTextField(null, 20);
                innerPanel.add(stockLevelField, g);
                g.gridy = 3;
                g.gridwidth = 3;
                final JTextField maxLevelField = new JTextField(null, 20);
                innerPanel.add(maxLevelField, g);
                g.gridy = 4;
                g.gridwidth = 3;
                String[] supplierOptions = new String[Database.getSuppliers().size() + 1];

                supplierOptions[0] = "";
                for (int i = 1; i < supplierOptions.length; i++) {
                    supplierOptions[i] = Database.getSuppliers().get(i - 1).getSupplierName() + " ("
                            + Database.getSuppliers().get(i - 1).getSupplierId() + ")";
                }
                final JComboBox<String> suppIDBox = new JComboBox<String>(supplierOptions);
                innerPanel.add(suppIDBox, g);
                g.gridy = 5;
                g.gridwidth = 3;
                final JTextField costPriceField = new JTextField(null, 20);
                innerPanel.add(costPriceField, g);
                g.gridy = 6;
                g.gridwidth = 3;
                final JTextField salePriceField = new JTextField(null, 20);
                innerPanel.add(salePriceField, g);

                // Set JTextFields with current data
                prodIDField.setText("" + p.getProductId());
                prodDescField.setText(p.getProductDescription());
                stockLevelField.setText("" + p.getStockLevel());
                maxLevelField.setText("" + p.getMaxLevel());
                suppIDBox.setSelectedIndex(p.getSupplierId() - IdManager.SUPPLIER_ID_START + 1);
                costPriceField.setText("" + p.getCostPrice());
                salePriceField.setText("" + p.getSalePrice());

                // Spacer
                g.gridx = 0;
                g.gridy = 7;
                innerPanel.add(new JLabel(" "), g);
                g.gridx = 1;
                g.gridy = 7;
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
                g.gridy = 8;
                innerPanel.add(cancel, g);

                save.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                        textFields.add(prodDescField);
                        ArrayList<JTextField> intFields = new ArrayList<JTextField>();
                        intFields.add(stockLevelField);
                        intFields.add(maxLevelField);
                        ArrayList<JTextField> doubleFields = new ArrayList<JTextField>();
                        doubleFields.add(costPriceField);
                        doubleFields.add(salePriceField);
                        ArrayList<JComboBox<String>> comboBoxes = new ArrayList<JComboBox<String>>();
                        comboBoxes.add(suppIDBox);

                        boolean valid = FieldValidator.checkFields(textFields, intFields, doubleFields, null, comboBoxes, null, null);

                        if (valid) {
                            // Add the staff at this index
                            Database.getProducts().add(
                                    index,
                                    new Product(prodDescField.getText(), Integer.parseInt(stockLevelField.getText()), Integer.parseInt(maxLevelField
                                            .getText()),
                                            Integer.parseInt(((String) suppIDBox.getSelectedItem()).substring(
                                                    ((String) suppIDBox.getSelectedItem()).length() - 5,
                                                    ((String) suppIDBox.getSelectedItem()).length() - 1)), Double.parseDouble(costPriceField
                                                    .getText()), Double.parseDouble(salePriceField.getText())));

                            GuiCreator.setConfirmationMessage("Product " + prodDescField.getText() + "'s details editted");
                            GuiCreator.frame.remove(GuiCreator.leftPanel);
                            GuiCreator.frame.repaint();
                            GuiCreator.frame.validate();
                            Database.getProducts().remove(index + 1);
                            createTable();
                        }
                    }// actionPerformed
                });

                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        GuiCreator.frame.remove(GuiCreator.leftPanel);
                        GuiCreator.frame.repaint();
                        GuiCreator.frame.validate();
                    }
                });
                // Add innerPanel
                GuiCreator.leftPanel.add(innerPanel);
            }
        }

        // Update ShopDriver.frame
        GuiCreator.setFrame(true, false, false);
    }

    /**
     * Opens a confirmation panel asking the user to confirm whether to delete
     * the selected product or not.<br />
     * Both parameters are passed using the selected option in the deletion
     * JComboBox.<br />
     * Re-runs {@link #createTable()} on completion.
     * 
     * @param productId
     *            an Integer specifying the ID number of the product chosen to
     *            be deleted
     * @param productName
     *            a String specifying the name/description of the product chosen
     *            to be deleted
     * 
     * @see #createTable()
     */
    public static void delete(final int productId, final String productName) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + productName + "?"));

        int i = -1; // Holds index of object to be deleted

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            // Reset ShopDriver.frame
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            for (final Product s : Database.getProducts()) {
                if (productId == s.getProductId())
                    i = Database.getProducts().indexOf(s); // Object can't be
                // removed while being
                // accessed - save its
                // index location for
                // now
            }
        }

        if (i != -1) { // If an object has been found, we can now remove it from
                       // the ArrayList
            GuiCreator.setConfirmationMessage(productName + " deleted");
            Database.getProducts().remove(i);
        }

        // Update ShopDriver.frame
        createTable();

        GuiCreator.frame.validate();
    }

    /**
     * Creates a new page on-screen with a detailed breakdown of an individual
     * product.<br />
     * Displays basic information (Description, ID, Supplier Name & ID, Cost
     * Price, Sale Price, Stock Level) just as the table, but also includes a
     * JTable showing all-time sales/purchases, and a graph visually
     * representing the same information.<br />
     * Includes a JButton to order the product, which calls
     * {@link OrderTable#createSupplierOrder(int)}, passing an Integer to
     * pre-select the appropriate Supplier in the JComboBox.<br />
     * It also includes a JButton which returns to {@link #createTable()}.
     * 
     * @param p
     *            the Product whose information is displayed on-screen
     * 
     * @see Product
     * @see OrderTable#createSupplierOrder(int)
     */
    public static void showProductInfo(final Product p) {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + p.getProductDescription());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        JLabel productLabel = new JLabel("Product");
        JLabel stockLabel = new JLabel("Stock Level");
        JLabel supplierLabel = new JLabel("Supplier");

        JLabel costLabel = new JLabel("Cost Price");
        JLabel saleLabel = new JLabel("Sale Price");
        JLabel profitLabel = new JLabel("Profit Per Unit");

        productLabel.setFont(new Font(productLabel.getFont().getFontName(), Font.BOLD, productLabel.getFont().getSize()));
        stockLabel.setFont(new Font(stockLabel.getFont().getFontName(), Font.BOLD, stockLabel.getFont().getSize()));
        supplierLabel.setFont(new Font(supplierLabel.getFont().getFontName(), Font.BOLD, supplierLabel.getFont().getSize()));
        costLabel.setFont(new Font(costLabel.getFont().getFontName(), Font.BOLD, costLabel.getFont().getSize()));
        saleLabel.setFont(new Font(saleLabel.getFont().getFontName(), Font.BOLD, saleLabel.getFont().getSize()));
        profitLabel.setFont(new Font(profitLabel.getFont().getFontName(), Font.BOLD, profitLabel.getFont().getSize()));

        int textFieldSize = 20;
        String supplier = "";

        for (Supplier s : Database.getSuppliers()) {
            if (s.getSupplierId() == p.getSupplierId())
                supplier = s.getSupplierName() + " (" + s.getSupplierId() + ")";
        }

        JTextField productField = new JTextField(p.getProductDescription() + " (" + p.getProductId() + ")", textFieldSize);
        productField.setEditable(false);
        JTextField stockField = new JTextField(p.getStockLevel() + "/" + p.getMaxLevel(), textFieldSize);
        stockField.setEditable(false);
        JTextField supplierField = new JTextField(supplier, textFieldSize);
        supplierField.setEditable(false);

        JTextField costField = new JTextField("€" + CURRENCY_FORMATTER.format(p.getCostPrice()), textFieldSize);
        costField.setEditable(false);
        JTextField saleField = new JTextField("€" + CURRENCY_FORMATTER.format(p.getSalePrice()), textFieldSize);
        saleField.setEditable(false);
        JTextField profitField = new JTextField("€" + CURRENCY_FORMATTER.format(p.getSalePrice() - p.getCostPrice()), textFieldSize);
        profitField.setEditable(false);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        titlePanel.add(productLabel, g);
        g.gridx = 1;
        titlePanel.add(productField, g);
        g.gridx = 2;
        titlePanel.add(stockLabel, g);
        g.gridx = 3;
        titlePanel.add(stockField, g);
        g.gridx = 4;
        titlePanel.add(supplierLabel, g);
        g.gridx = 5;
        titlePanel.add(supplierField, g);

        g.gridy = 1;
        g.gridx = 0;
        titlePanel.add(costLabel, g);
        g.gridx = 1;
        titlePanel.add(costField, g);
        g.gridx = 2;
        titlePanel.add(saleLabel, g);
        g.gridx = 3;
        titlePanel.add(saleField, g);
        g.gridx = 4;
        titlePanel.add(profitLabel, g);
        g.gridx = 5;
        titlePanel.add(profitField, g);

        JPanel myPanel = new JPanel(new BorderLayout());

        double[] yearData = new double[YEAR_CURRENT - YEAR_START + 1];
        double[] productData = new double[YEAR_CURRENT - YEAR_START + 1];

        String[] columnNames = { "Year", "Total Qty Bought", "Total Qty Sold", "Year Change" };
        Object[][] data = new Object[YEAR_CURRENT - YEAR_START + 1 + 1][4];

        for (int i = 0; i < YEAR_CURRENT - YEAR_START + 2; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = 0;
            }
        }

        data[0][2] = "<html><b>Start Level</b></html>";
        data[0][3] = "<html><b>" + p.getStartLevel() + "</html></b>";

        for (int i = 0; i < YEAR_CURRENT - YEAR_START + 1; i++) {
            for (Order o : Database.getOrders()) {
                if (Integer.parseInt(o.getOrderDate().substring(6, 10)) == i + YEAR_START) {

                    for (OrderedItem oi : o.getOrderedItems()) {
                        if (oi.getProduct().getProductId() == p.getProductId() && o.isSupplier() && !o.isActive())
                            data[i + 1][1] = (int) data[i + 1][1] + oi.getQuantity();
                        else if (oi.getProduct().getProductId() == p.getProductId() && !o.isSupplier())
                            data[i + 1][2] = (int) data[i + 1][2] + oi.getQuantity();
                    }

                }
                data[i + 1][0] = (i + YEAR_START);
                if ((int) data[i + 1][1] - (int) data[i + 1][2] > 0)
                    data[i + 1][3] = "<html><b><font color=\"blue\">" + ((int) data[i + 1][1] - (int) data[i + 1][2]) + "</font></b></html>";
                else if ((int) data[i + 1][1] - (int) data[i + 1][2] < 0)
                    data[i + 1][3] = "<html><b><font color=\"red\">" + ((int) data[i + 1][1] - (int) data[i + 1][2]) + "</font></b></html>";
            }
        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        myPanel.add(scrollPane, BorderLayout.WEST);
        int runningTotal = p.getStartLevel();

        for (int j = 0; j < YEAR_CURRENT - YEAR_START + 1; j++) {
            yearData[j] = j + YEAR_START;
            runningTotal += (int) data[j + 1][1] - (int) data[j + 1][2];
            productData[j] = runningTotal;
        }

        double[][] inputdata = { yearData, productData };
        ChartPanel chartPanel = Graphs.createLineChart("Past Stock levels", p.getProductDescription(), inputdata);
        chartPanel.setPreferredSize(new Dimension(500, 750));

        myPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        myPanel.add(chartPanel, BorderLayout.CENTER);

        innerPanel.add(myPanel, BorderLayout.CENTER);

        JButton orderButton = new JButton("Order " + p.getProductDescription());
        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrderTable.createSupplierOrder(p.getSupplierId());
            }
        });

        JButton backButton = new JButton("Back to Products");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createTable();
            }
        });

        buttonPanel.add(orderButton);
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
     * @see #sortById(boolean)
     * @see #sortByDescription(boolean)
     * @see #sortByStockLevel(boolean)
     * @see #sortBySupplierId(boolean)
     * @see #sortByCostPrice(boolean)
     * @see #sortBySalePrice(boolean)
     */
    public static void sortArrayList() {

        if (type.equals("ID"))
            sortById(reverse);
        if (type.equals("Description"))
            sortByDescription(reverse);
        if (type.equals("Stock Level"))
            sortByStockLevel(reverse);
        if (type.equals("Supplier ID"))
            sortBySupplierId(reverse);
        if (type.equals("Cost Price"))
            sortByCostPrice(reverse);
        if (type.equals("Sale Price"))
            sortBySalePrice(reverse);
    }

    /**
     * Sorts {@link ShopDriver#products} by product description in alphabetical
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void sortByDescription(boolean reverse) {
        if (!reverse) {
            Collections.sort(Database.getProducts(), new Comparator<Product>() {
                public int compare(Product s1, Product s2) {
                    return s1.getProductDescription().compareToIgnoreCase(s2.getProductDescription());
                }
            });
        }

        if (reverse) {
            Collections.sort(Database.getProducts(), new Comparator<Product>() {
                public int compare(Product s1, Product s2) {
                    return s2.getProductDescription().compareToIgnoreCase(s1.getProductDescription());
                }
            });
        }
    }

    /**
     * Sorts {@link ShopDriver#products} by product ID in ascending order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void sortById(boolean reverse) {
        ArrayList<Product> tempArrayList = new ArrayList<Product>();
        int count = IdManager.PRODUCT_ID_START;
        int offset = 0;
        boolean found = false;

        for (int i = 0; i < Database.getProducts().size() + offset; i++) {
            found = false;
            for (Product p : Database.getProducts()) {
                if (count == p.getProductId()) {
                    tempArrayList.add(p);
                    found = true;
                }
            }
            if (!found)
                offset++;
            count++;
        }

        Database.getProducts().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#products} by product cost price in ascending
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void sortByCostPrice(boolean reverse) {
        double lowestPrice = 999999999;
        ArrayList<Product> tempArrayList = new ArrayList<Product>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;

        while (Database.getProducts().size() != 0) {
            // go through the array list and find the lowest wage listed
            for (Product p : Database.getProducts()) {
                if (lowestPrice > p.getCostPrice()) {
                    lowestPrice = p.getCostPrice();
                }
            }

            found = false;
            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Product p : Database.getProducts()) {
                if (lowestPrice == p.getCostPrice() && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(p);

                    // note the index of this entry
                    indexL = Database.getProducts().indexOf(p);

                    found = true;
                }
                if (found)
                    break;
            }
            Database.getProducts().remove(indexL);

            lowestPrice = 99999999;
        }

        Database.getProducts().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#products} by product sale price in ascending
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void sortBySalePrice(boolean reverse) {
        double lowestPrice = 999999999;
        ArrayList<Product> tempArrayList = new ArrayList<Product>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;

        while (Database.getProducts().size() != 0) {
            // go through the array list and find the lowest wage listed
            for (Product p : Database.getProducts()) {
                if (lowestPrice > p.getSalePrice()) {
                    lowestPrice = p.getSalePrice();
                }
            }

            found = false;
            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Product p : Database.getProducts()) {
                if (lowestPrice == p.getSalePrice() && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(p);

                    // note the index of this entry
                    indexL = Database.getProducts().indexOf(p);

                    found = true;
                }
                if (found)
                    break;
            }
            Database.getProducts().remove(indexL);

            lowestPrice = 99999999;
        }

        Database.getProducts().clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#products} by product stock level in descending
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void sortByStockLevel(boolean reverse) {
        int lowestLevel = 999999999;
        ArrayList<Product> tempArrayList = new ArrayList<Product>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;

        while (Database.getProducts().size() != 0) {
            // go through the array list and find the lowest wage listed
            for (Product p : Database.getProducts()) {
                if (lowestLevel > p.getStockLevel()) {
                    lowestLevel = p.getStockLevel();
                }
            }

            found = false;
            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Product p : Database.getProducts()) {
                if (lowestLevel == p.getStockLevel() && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(p);

                    // note the index of this entry
                    indexL = Database.getProducts().indexOf(p);

                    found = true;
                }
                if (found)
                    break;
            }
            Database.getProducts().remove(indexL);

            lowestLevel = 99999999;
        }

        Database.getProducts().clear();

        if (reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        } else if (!reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#products} by product supplier ID in ascending
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void sortBySupplierId(boolean reverse) {
        int lowestID = 999999999;
        ArrayList<Product> tempArrayList = new ArrayList<Product>();
        int indexL = 0;

        tempArrayList.clear();
        boolean found;
        while (Database.getProducts().size() != 0) {
            // go through the array list and find the lowest wage listed
            for (Product p : Database.getProducts()) {
                if (lowestID > p.getSupplierId())
                    lowestID = p.getSupplierId();
            }
            found = false;

            // find an entry in the arrayList with a wage matching the lowest
            // wage found
            for (Product p : Database.getProducts()) {
                if (lowestID == p.getSupplierId() && !found) {
                    // when a matching entry is found, add to tempArrayList
                    tempArrayList.add(p);

                    // note the index of this entry
                    indexL = Database.getProducts().indexOf(p);

                    found = true;
                }
                if (found)
                    break;
            }
            Database.getProducts().remove(indexL);
            lowestID = 99999999;
        }
        Database.getProducts().clear();
        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                Database.getProducts().add(tempArrayList.get(i));
            }
        }
    }
}