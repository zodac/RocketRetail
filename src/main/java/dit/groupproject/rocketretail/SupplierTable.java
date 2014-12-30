package dit.groupproject.rocketretail;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

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

public class SupplierTable {
    /**
     * A DecimalFormatter which formats Integers into Strings with the given
     * formatting.<br />
     * Formats Supplier IDs to always have five digits.
     * 
     * @see Supplier
     */
    static DecimalFormat doubleFormatter = new DecimalFormat("0000");
    /**
     * A DecimalFormatter which formats Integers into Strings with the given
     * formatting.<br />
     * Formats doubles to have commas, always show to two decimal places, and
     * have at least two digits before the decimal point.
     */
    static DecimalFormat doubleFormatter2 = new DecimalFormat("#,###,#00.00");
    /**
     * A String used to define how the JTable is sorted. Retains value if JTable
     * re-called.
     * 
     * @see #supplier()
     * @see #sortArrayList()
     */
    static String type = "Sort by...";
    /**
     * A boolean which is set true if table is called for the first time (or
     * first time since system has been re-logged into.<br />
     * If true, sorts table by ID.
     * 
     * @see #SortByID(boolean)
     */
    static boolean first = true;
    /**
     * A boolean which causes all sort options to sort in reverse order.<br />
     * Set to true if chosen sort option (stored in {@link #type}) is already
     * selected.
     * 
     * @see #SortByID(boolean)
     * @see #SortByName(boolean)
     * @see #SortByAddress(boolean)
     * @see #SortByVatNumber(boolean)
     * @see #SortByDate(boolean, boolean)
     */
    static boolean reverse = false;

    // Methods
    /**
     * Creates the JMenuItem for "Supplier", and defines the ActionListener for
     * the JMenuItem. <br />
     * The ActionListener calls the {@link #supplier()} method.
     * 
     * @return the JMenuItem for the "Database" JMenuItem in
     *         {@link MenuGUI#createMenuBar(JMenuBar, boolean)}
     * 
     * @see #supplier()
     * @see MenuGUI#createMenuBar(JMenuBar, boolean)
     */
    public static JMenuItem createMenu(boolean manager) {
        JMenuItem supplierItem = new JMenuItem("Supplier");
        supplierItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supplier();
            }
        });

        supplierItem.setEnabled(manager);

        // Return menuItem
        return supplierItem;
    }

    /**
     * Creates the JTable for Products, using data from
     * {@link ShopDriver#products}.<br />
     * Adds JButtons to add, edit or delete a product, and implements the
     * ActionListeners for each.<br />
     * Calls {@link #showSupplierInfo(Supplier)} when a supplier is selected
     * from the table. *
     * 
     * @see #add()
     * @see #edit(int)
     * @see #delete(int, String)
     * @see #showSupplierInfo(Supplier)
     */
    public static void supplier() {
        if (!ShopDriver.currentTable.equals("Supplier"))
            ShopDriver.frame.remove(ShopDriver.leftPanel);

        ShopDriver.currentTable = "Supplier";

        // Reset ShopDriver.frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - Suppliers");
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel(new BorderLayout(0, 1));

        // When first run, ensure ArrayList (and table) is sorted by ID
        if (first) {
            SortByID(false);
            first = false;
        }

        String[] columnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
        Object[][] data = new Object[ShopDriver.suppliers.size()][7];

        for (int i = 0; i < ShopDriver.suppliers.size(); i++) {

            data[i][0] = doubleFormatter.format(ShopDriver.suppliers.get(i).getSupplierID());
            data[i][1] = ShopDriver.suppliers.get(i).getSupplierName();
            data[i][2] = ShopDriver.suppliers.get(i).getPhoneNumber();
            data[i][3] = ShopDriver.suppliers.get(i).getAddress();
            data[i][4] = ShopDriver.suppliers.get(i).getVatNumber();
            data[i][5] = ShopDriver.suppliers.get(i).getLastPurchase();
            data[i][6] = ShopDriver.suppliers.get(i).getDateAdded();
        }

        final JTable table = new JTable(data, columnNames);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (table.getSelectedRow() >= 0) { // Invalid selections return
                                                   // -1
                    Supplier input = null;

                    for (Supplier s : ShopDriver.suppliers) {
                        if (s.getSupplierID() == Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)))
                            input = s;
                    }
                    showSupplierInfo(input);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(ShopDriver.backgroundColour);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(ShopDriver.backgroundColour);

        String[] supplierMemberArrayEdit = new String[ShopDriver.suppliers.size() + 1];
        supplierMemberArrayEdit[0] = "Edit Supplier";
        for (int i = 0; i < ShopDriver.suppliers.size() + 1; i++) {
            if (i < ShopDriver.suppliers.size())
                supplierMemberArrayEdit[i + 1] = "ID: "
                        + doubleFormatter.format(ShopDriver.suppliers.get(i).getSupplierID()) + " ("
                        + ShopDriver.suppliers.get(i).getSupplierName() + ")";
        }

        String[] supplierMemberArrayDelete = new String[ShopDriver.suppliers.size() + 1];
        supplierMemberArrayDelete[0] = "Delete Supplier";
        for (int i = 0; i < ShopDriver.suppliers.size() + 1; i++) {
            if (i < ShopDriver.suppliers.size())
                supplierMemberArrayDelete[i + 1] = "ID: "
                        + doubleFormatter.format(ShopDriver.suppliers.get(i).getSupplierID()) + " ("
                        + ShopDriver.suppliers.get(i).getSupplierName() + ")";
        }

        JButton addButton = new JButton("Add Supplier");
        final JComboBox<String> editBox = new JComboBox<String>(supplierMemberArrayEdit);
        final JComboBox<String> deleteBox = new JComboBox<String>(supplierMemberArrayDelete);

        String[] options = { "Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added" };
        final JComboBox<String> sortOptions = new JComboBox<String>(options);
        int index = 0;

        if (type.equals("Sort by..."))
            index = 0;
        if (type.equals("ID"))
            index = 1;
        if (type.equals("Name"))
            index = 2;
        if (type.equals("Address"))
            index = 3;
        if (type.equals("VAT Number"))
            index = 4;
        if (type.equals("Last Purchase"))
            index = 5;
        if (type.equals("Date Added"))
            index = 6;
        sortOptions.setSelectedIndex(index);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        editBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edit(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 8)));
            }
        });

        deleteBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                delete(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 8)),
                        ((String) deleteBox.getSelectedItem()).substring(10,
                                ((String) deleteBox.getSelectedItem()).length() - 1));
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
                supplier();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editBox);
        buttonPanel.add(deleteBox);
        buttonPanel.add(sortOptions);

        // scrollPane.add(table);
        ShopDriver.mainPanel.add(scrollPane, BorderLayout.NORTH);
        ShopDriver.mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Update ShopDriver.frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * Opens a form to input information for a new Supplier in the left Panel of
     * the screen. Uses GridBagLayout.<br />
     * Adds JButtons to cancel, or save new supplier and re-runs
     * {@link #supplier()}
     * 
     * @see #supplier()
     */
    public static void add() {
        // Reset ShopDriver.frame
        ShopDriver.frame.remove(ShopDriver.leftPanel);
        ShopDriver.frame.repaint();
        ShopDriver.leftPanel = new JPanel();

        // Panel items
        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(ShopDriver.backgroundColour);
        GridBagConstraints g = new GridBagConstraints();

        // JLabels with GridBagLayout
        g.gridx = 0;
        g.gridy = 0;
        innerPanel.add(new JLabel("Supplier ID:"), g);
        g.gridy = 1;
        innerPanel.add(new JLabel("Supplier Name:"), g);
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

        g.insets = new Insets(1, 10, 0, 5); // Border to left, and small one on
                                            // top as a spacer between rows

        // JTextFields with GridBagLayout
        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField suppIDField = new JTextField(null, 20);
        suppIDField.setText("" + firstAvailableID());
        suppIDField.setEditable(false);
        innerPanel.add(suppIDField, g);
        g.gridy = 1;
        g.gridwidth = 3;
        final JTextField suppNameField = new JTextField(null, 20);
        innerPanel.add(suppNameField, g);
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
        g.gridy = 5;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> lastPurchaseDay = new JComboBox<String>(ShopDriver.days);
        innerPanel.add(lastPurchaseDay, g);

        g.gridy = 5;
        g.gridx = 2;
        g.gridwidth = 1 - 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(ShopDriver.months);
        innerPanel.add(lastPurchaseMonth, g);

        g.gridy = 5;
        g.gridx = 3;
        g.gridwidth = 2 - 3;

        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(ShopDriver.years);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 6;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(ShopDriver.days);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 6;
        g.gridx = 2;
        g.gridwidth = 1 - 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(ShopDriver.months);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 6;
        g.gridx = 3;
        g.gridwidth = 2 - 3;

        final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.years);
        innerPanel.add(dateAddedYear, g);

        dateAddedDay.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(
                0, 2)));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date())
                .substring(3, 5)));
        dateAddedYear.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date())
                .substring(6, 10)) - (ShopDriver.yearStart - 1));

        // Spacer
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
                textFields.add(suppNameField);
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

                boolean valid = ShopDriver.checkFields(textFields, null, null, null, null, addedBoxes,
                        lastPurchaseBoxes);

                if (valid) {
                    ShopDriver.suppliers.add(new Supplier(Integer.parseInt(suppIDField.getText()), suppNameField
                            .getText(), phoneNoField.getText(), addressField.getText(), vatNoField.getText(),
                            lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                                    + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/"
                                    + dateAddedMonth.getSelectedItem() + "/" + dateAddedYear.getSelectedItem()));

                    ShopDriver.setConfirmMessage("New Supplier \"" + suppNameField.getText() + "\" added");
                    ShopDriver.frame.remove(ShopDriver.leftPanel);
                    ShopDriver.frame.repaint();
                    ShopDriver.frame.validate();
                    SortByID(false);
                    supplier();
                }
            }// actionPerformed
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.frame.remove(ShopDriver.leftPanel);
                ShopDriver.frame.repaint();
                ShopDriver.frame.validate();
            }
        });

        // Add innerPanel
        ShopDriver.leftPanel.add(innerPanel);

        // Update ShopDriver.frame
        ShopDriver.setFrame(true, false, false);
    }

    /**
     * Opens a form for the chosen product in the left Panel of the screen.
     * Automatically fills form with current supplier information.<br />
     * Adds JButtons to cancel, or save supplier changes and re-runs
     * {@link #supplier()}
     * 
     * @param supplierID
     *            an Integer specifying the supplier to edit
     * 
     * @see #supplier()
     */
    public static void edit(int supplierID) {
        // Reset ShopDriver.frame
        ShopDriver.frame.remove(ShopDriver.leftPanel);
        ShopDriver.frame.repaint();
        ShopDriver.leftPanel = new JPanel();

        for (Supplier s : ShopDriver.suppliers) {
            if (supplierID == s.getSupplierID()) {
                final int index = ShopDriver.suppliers.indexOf(s);
                // Panel items
                final JPanel innerPanel = new JPanel(new GridBagLayout());
                innerPanel.setBackground(ShopDriver.backgroundColour);
                GridBagConstraints g = new GridBagConstraints();
                g.insets = new Insets(1, 10, 0, 5);

                // JLabels with GridBagLayout
                g.gridx = 0;
                g.gridy = 0;
                innerPanel.add(new JLabel("Supplier ID:"), g);
                g.gridy = 1;
                innerPanel.add(new JLabel("Supplier Name:"), g);
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

                // JTextFields with GridBagLayout
                g.gridx = 1;
                g.gridy = 0;
                g.gridwidth = 3;
                final JTextField suppIDField = new JTextField(null, 20);
                suppIDField.setEditable(false);
                innerPanel.add(suppIDField, g);
                g.gridy = 1;
                g.gridwidth = 3;
                final JTextField suppNameField = new JTextField(null, 20);
                innerPanel.add(suppNameField, g);
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
                final JComboBox<String> lastPurchaseDay = new JComboBox<String>(ShopDriver.days);
                innerPanel.add(lastPurchaseDay, g);

                g.gridy = 5;
                g.gridx = 2;
                g.gridwidth = 1 - 2;
                final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(ShopDriver.months);
                innerPanel.add(lastPurchaseMonth, g);

                g.gridy = 5;
                g.gridx = 3;
                g.gridwidth = 2 - 3;

                final JComboBox<String> lastPurchaseYear = new JComboBox<String>(ShopDriver.years);
                innerPanel.add(lastPurchaseYear, g);

                g.gridy = 6;
                g.gridx = 1;
                g.gridwidth = 1;
                final JComboBox<String> dateAddedDay = new JComboBox<String>(ShopDriver.days);
                innerPanel.add(dateAddedDay, g);

                g.gridy = 6;
                g.gridx = 2;
                g.gridwidth = 1 - 2;
                final JComboBox<String> dateAddedMonth = new JComboBox<String>(ShopDriver.months);
                innerPanel.add(dateAddedMonth, g);

                g.gridy = 6;
                g.gridx = 3;
                g.gridwidth = 2 - 3;

                final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.years);
                innerPanel.add(dateAddedYear, g);

                // Set JTextFields with current data
                suppIDField.setText("" + s.getSupplierID());
                suppNameField.setText(s.getSupplierName());
                phoneNoField.setText(s.getPhoneNumber());
                addressField.setText(s.getAddress());
                vatNoField.setText(s.getVatNumber());
                lastPurchaseDay.setSelectedIndex(Integer.parseInt(s.getLastPurchase().substring(0, 2)));
                lastPurchaseMonth.setSelectedIndex(Integer.parseInt(s.getLastPurchase().substring(3, 5)));
                lastPurchaseYear.setSelectedIndex(Integer.parseInt(s.getLastPurchase().substring(6, 10))
                        - (ShopDriver.yearStart - 1));
                dateAddedDay.setSelectedIndex(Integer.parseInt(s.getDateAdded().substring(0, 2)));
                dateAddedMonth.setSelectedIndex(Integer.parseInt(s.getDateAdded().substring(3, 5)));
                dateAddedYear.setSelectedIndex(Integer.parseInt(s.getDateAdded().substring(6, 10))
                        - (ShopDriver.yearStart - 1));

                JLabel spaceLabel1 = new JLabel();
                spaceLabel1.setText(" ");
                g.gridx = 0;
                g.gridy = 8;
                innerPanel.add(spaceLabel1, g);
                JLabel spaceLabel2 = new JLabel();
                spaceLabel2.setText(" ");
                g.gridx = 1;
                g.gridy = 8;
                innerPanel.add(spaceLabel2, g);

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
                        textFields.add(suppNameField);
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

                        boolean valid = ShopDriver.checkFields(textFields, null, null, null, null, addedBoxes,
                                lastPurchaseBoxes);

                        if (valid) {
                            ShopDriver.suppliers.add(
                                    index,
                                    new Supplier(Integer.parseInt(suppIDField.getText()), suppNameField.getText(),
                                            phoneNoField.getText(), addressField.getText(), vatNoField.getText(),
                                            lastPurchaseDay.getSelectedItem() + "/"
                                                    + lastPurchaseMonth.getSelectedItem() + "/"
                                                    + lastPurchaseYear.getSelectedItem(), dateAddedDay
                                                    .getSelectedItem()
                                                    + "/"
                                                    + dateAddedMonth.getSelectedItem()
                                                    + "/"
                                                    + dateAddedYear.getSelectedItem()));

                            ShopDriver.setConfirmMessage("Supplier \"" + suppNameField.getText() + "'s details editted");
                            ShopDriver.frame.remove(ShopDriver.leftPanel);
                            ShopDriver.frame.validate();
                            ShopDriver.suppliers.remove(index + 1);
                            supplier();
                        }
                    }
                });// actionPerformed

                cancel.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ShopDriver.frame.remove(ShopDriver.leftPanel);
                        ShopDriver.frame.validate();
                    }
                });
                // Add innerPanel
                ShopDriver.leftPanel.add(innerPanel);
            }
        }
        // Update ShopDriver.frame
        ShopDriver.setFrame(true, false, false);
    }

    /**
     * Opens a confirmation panel asking the user to confirm whether to delete
     * the selected supplier or not.<br />
     * Both parameters are passed using the selected option in the deletion
     * JComboBox.<br />
     * Re-runs {@link #supplier()} on completion.
     * 
     * @param supplierID
     *            an Integer specifying the ID number of the supplier chosen to
     *            be deleted
     * @param supplierName
     *            a String specifying the name/description of the supplier
     *            chosen to be deleted
     * 
     * @see #supplier()
     */
    public static void delete(int supplierID, String supplierName) {
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + supplierName + "?"));

        int i = -1; // Holds index of object to be deleted

        if (ShopDriver.showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            // Reset ShopDriver.frame
            ShopDriver.frame.remove(ShopDriver.leftPanel);
            ShopDriver.frame.repaint();
            ShopDriver.leftPanel = new JPanel();

            for (Supplier c : ShopDriver.suppliers) {
                if (supplierID == c.getSupplierID())
                    i = ShopDriver.suppliers.indexOf(c); // Object can't be
                                                         // removed while being
                                                         // accessed - save its
                                                         // index location for
                                                         // now
            }
        }

        if (i != -1) // If an object has been found, we can now remove it from
                     // the ArrayList
            ShopDriver.suppliers.remove(i);
        ShopDriver.setConfirmMessage(supplierName + " deleted");

        // Update ShopDriver.frame
        supplier();

        ShopDriver.frame.validate();
    }

    /**
     * Creates a new page on-screen with a detailed breakdown of purchases from
     * an individual supplier.<br />
     * Displays basic information (description, Product ID, stock level, current
     * stock) just as the table, but also includes a JTable showing all-time
     * purchases.<br />
     * passing an Integer to pre-select the appropriate Supplier in the
     * JComboBox.<br />
     * It also includes a JButton which returns to {@link #supplier()}.
     * 
     * @param s
     *            the Supplier whose information is displayed on-screen
     * 
     * @see SupplierTable#supplier()
     */
    public static void showSupplierInfo(Supplier s) {
        // Reset ShopDriver.frame
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.setTitle("Rocket Retail Inc - " + s.getSupplierName());
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel(new BorderLayout(0, 1));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(ShopDriver.backgroundColour);
        innerPanel.setBackground(ShopDriver.backgroundColour);
        buttonPanel.setBackground(ShopDriver.backgroundColour);

        JLabel supplierLabel = new JLabel("Supplier");
        JLabel vatNumberLabel = new JLabel("VAT Number");
        JLabel phoneNoLabel = new JLabel("Phone Number");
        JLabel addressLabel = new JLabel("Address");
        JLabel dateAddedLabel = new JLabel("Date Added");
        JLabel titleLabel = new JLabel("Products supplied by " + s.getSupplierName());

        supplierLabel.setFont(new Font(supplierLabel.getFont().getFontName(), Font.BOLD, supplierLabel.getFont()
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

        JTextField supplierField = new JTextField(s.getSupplierName() + " ("
                + doubleFormatter.format(s.getSupplierID()) + ")", textFieldSize);
        supplierField.setEditable(false);
        JTextField vatNumberField = new JTextField(s.getVatNumber(), textFieldSize);
        vatNumberField.setEditable(false);

        JTextField phoneNoField = new JTextField(s.getPhoneNumber(), textFieldSize);
        phoneNoField.setEditable(false);
        JTextField addressField = new JTextField(s.getAddress(), textFieldSize);
        addressField.setEditable(false);
        JTextField dateAddedField = new JTextField(s.getDateAdded(), textFieldSize);
        dateAddedField.setEditable(false);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        titlePanel.add(supplierLabel, g);
        g.gridx = 1;
        titlePanel.add(supplierField, g);
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

        for (Product p : ShopDriver.products) {
            if (s.getSupplierID() == p.getSupplierID())
                count++;
        }

        String[] columnNames = { "Product Description", "Product ID", "Unit Cost Price", "Current Stock" };
        Object[][] data = new Object[count][4];
        int indexArray = 0;

        for (int i = 0; i < ShopDriver.products.size(); i++) {

            if (s.getSupplierID() == ShopDriver.products.get(i).getSupplierID()) {

                data[indexArray][0] = ShopDriver.products.get(i).getProductDesc();
                data[indexArray][1] = ShopDriver.products.get(i).getProductID();
                data[indexArray][2] = "€" + doubleFormatter2.format(ShopDriver.products.get(i).getCostPrice());
                data[indexArray][3] = ShopDriver.products.get(i).getStockLevel() + "/"
                        + ShopDriver.products.get(i).getMaxLevel();
                indexArray++;
            }

        }

        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Suppliers");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supplier();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        ShopDriver.mainPanel.add(titlePanel, BorderLayout.NORTH);
        ShopDriver.mainPanel.add(innerPanel, BorderLayout.CENTER);

        // Update frame
        ShopDriver.setFrame(false, false, true);
    }

    /**
     * Calculates the first available ID number to use when creating a new
     * supplier in {@link #add()}.
     * 
     * @return the integer to be used as the ID for a new supplier
     * 
     * @see #add()
     */
    public static int firstAvailableID() {
        type = "ID";
        SortByID(false);
        int output = 0, i = ShopDriver.supplierIDStart;
        boolean found = false;

        while (!found && i < (ShopDriver.suppliers.size() + ShopDriver.supplierIDStart)) {
            if (ShopDriver.suppliers.get(i - ShopDriver.supplierIDStart).getSupplierID() == i) {
            } else {
                output = i;
                found = true;
            }
            i++;
        }

        if (found)
            return output;
        else
            return ShopDriver.suppliers.size() + ShopDriver.supplierIDStart;
    }

    /**
     * A switch-like method which decides how to sort the JTable.<br />
     * Uses class variable {@link #type} and calls appropriate sorting method
     * based on its value.
     * 
     * @see #type
     * @see #SortByID(boolean)
     * @see #SortByName(boolean)
     * @see #SortByAddress(boolean)
     * @see #SortByVatNumber(boolean)
     * @see #SortByDate(boolean, boolean)
     */
    public static void sortArrayList() {
        if (type.equals("ID"))
            SortByID(reverse);
        if (type.equals("Name"))
            SortByName(reverse);
        if (type.equals("Address"))
            SortByAddress(reverse);
        if (type.equals("VAT Number"))
            SortByVatNumber(reverse);
        if (type.equals("Last Purchase"))
            SortByDate(false, reverse);
        if (type.equals("Date Added"))
            SortByDate(true, reverse);
    }

    /**
     * Sorts {@link ShopDriver#suppliers} by supplier name in alphabetical
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByName(boolean reverse) {
        if (!reverse) {
            Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
                public int compare(Supplier s1, Supplier s2) {
                    return s1.getSupplierName().compareToIgnoreCase(s2.getSupplierName());
                }
            });
        }

        if (reverse) {
            Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
                public int compare(Supplier s1, Supplier s2) {
                    return s2.getSupplierName().compareToIgnoreCase(s1.getSupplierName());
                }
            });
        }
    }

    /**
     * Sorts {@link ShopDriver#suppliers} by supplier address in alphabetical
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByAddress(boolean reverse) {
        if (!reverse) {
            Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
                public int compare(Supplier s1, Supplier s2) {
                    return s1.getAddress().compareToIgnoreCase(s2.getAddress());
                }
            });
        }

        if (reverse) {
            Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
                public int compare(Supplier s1, Supplier s2) {
                    return s2.getAddress().compareToIgnoreCase(s1.getAddress());
                }
            });
        }
    }

    /**
     * Sorts {@link ShopDriver#suppliers} by supplier VAT Number in alphabetical
     * order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByVatNumber(boolean reverse) {
        if (!reverse) {
            Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
                public int compare(Supplier s1, Supplier s2) {
                    return s1.getVatNumber().compareToIgnoreCase(s2.getVatNumber());
                }
            });
        }

        if (reverse) {
            Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
                public int compare(Supplier s1, Supplier s2) {
                    return s2.getVatNumber().compareToIgnoreCase(s1.getVatNumber());
                }
            });
        }
    }

    /**
     * Sorts {@link ShopDriver#suppliers} by supplier ID in alphabetical order.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByID(boolean reverse) {
        ArrayList<Supplier> tempArrayList = new ArrayList<Supplier>();
        int count = ShopDriver.supplierIDStart, offset = 0;
        boolean found = false;

        for (int i = 0; i < ShopDriver.suppliers.size() + offset; i++) {
            found = false;
            for (Supplier p : ShopDriver.suppliers) {
                if (count == p.getSupplierID()) {
                    tempArrayList.add(p);
                    found = true;
                }
            }
            if (!found)
                offset++;
            count++;
        }

        ShopDriver.suppliers.clear();

        if (!reverse) {
            for (int i = 0; i < tempArrayList.size(); i++) {
                ShopDriver.suppliers.add(tempArrayList.get(i));
            }
        } else if (reverse) {
            for (int i = tempArrayList.size() - 1; i >= 0; i--) {
                ShopDriver.suppliers.add(tempArrayList.get(i));
            }
        }
    }

    /**
     * Sorts {@link ShopDriver#suppliers} by supplier Date Added.
     * 
     * @param reverse
     *            a boolean which specifies whether to sort in reverse order or
     *            not
     */
    public static void SortByDate(final boolean DateAdded, final boolean reverse) {

        Collections.sort(ShopDriver.suppliers, new Comparator<Supplier>() {
            DateFormat f = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public int compare(Supplier s1, Supplier s2) {
                try {
                    String string1 = "";
                    String string2 = "";

                    if (DateAdded) {
                        string1 = s1.getDateAdded();
                        string2 = s2.getDateAdded();
                    } else {
                        string1 = s1.getLastPurchase();
                        string2 = s2.getLastPurchase();
                    }
                    if (!reverse)
                        return f.parse(string1).compareTo(f.parse(string2));
                    else
                        return f.parse(string2).compareTo(f.parse(string1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

    }

}