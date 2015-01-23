package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.utilities.Dates.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;
import static dit.groupproject.rocketretail.utilities.Formatters.DAY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.MONTH_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.YEAR_FORMATTER;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;
import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

public class AddEntityHelper extends AbstractEntityHelper {

    private final static String[] CUSTOMER_LABELS = { "Customer Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
    private final static String[] SUPPLIER_LABELS = { "Supplier Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
    private final static String[] STAFF_LABELS = { "Staff PIN", "Name", "Gender", "Phone Number", "Address", "Wage", "Staff Level", "Date Added" };
    private final static String[] PRODUCT_LABELS = { "Product Description", "Stock Level", "Maximum Lavel", "Supplier ID", "Cost Price", "Sale Price" };

    /**
     * Returns an ActionListener which adds an Entity addition panel to the left
     * side of the GUI screen.
     * <p>
     * Checks the current table state of the system to determine which Entity
     * table to use.
     */
    public static ActionListener addEntityPanel() {
        final TableState currentState = ShopDriver.getCurrentTableState();

        if (currentState == TableState.CUSTOMER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addCustomerPanel();
                }
            };
        } else if (currentState == TableState.ORDER) {

        } else if (currentState == TableState.PRODUCT) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addProductPanel();
                }
            };
        } else if (currentState == TableState.STAFF) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addStaffPanel();
                }
            };
        } else if (currentState == TableState.SUPPLIER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addSupplierPanel();
                }
            };
        }
        throw new IllegalArgumentException("No panel available for current table state [" + currentState.toString() + "]!");
    }

    private static void addCustomerPanel() {
        resetLeftPanel();

        final JPanel innerPanel = addLabelsToPanel(CUSTOMER_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
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
        final JComboBox<String> lastPurchaseDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);
        g.gridx = 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);
        g.gridx = 3;
        final JComboBox<String> lastPurchaseYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 5;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        dateAddedDay.setSelectedIndex(Integer.parseInt(DAY_FORMATTER.format(new Date())));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(MONTH_FORMATTER.format(new Date())));
        dateAddedYear.setSelectedIndex(Integer.parseInt(YEAR_FORMATTER.format(new Date())) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 6;
        innerPanel.add(new JLabel(""), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(""), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 7;
        innerPanel.add(save, g);
        final JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final ArrayList<JTextField> textFields = new ArrayList<>();
                textFields.add(custNameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                textFields.add(vatNoField);
                final ArrayList<JComboBox<String>> addedBoxes = new ArrayList<>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                final ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (FieldValidator.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    Database.addCustomer(new Customer(custNameField.getText(), phoneNoField.getText(), addressField.getText(), vatNoField.getText(),
                            lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/" + lastPurchaseYear.getSelectedItem(),
                            dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/" + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage("Customer " + custNameField.getText() + " added");
                    removeLeftPanel();

                    CustomerTable.descendingOrderSort = false;
                    CustomerTable.sortItems();
                    CustomerTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private static void addSupplierPanel() {
        resetLeftPanel();
        final JPanel innerPanel = addLabelsToPanel(SUPPLIER_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField suppNameField = new JTextField(null, 20);
        innerPanel.add(suppNameField, g);
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
        final JComboBox<String> lastPurchaseDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);

        g.gridy = 4;
        g.gridx = 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);

        g.gridy = 4;
        g.gridx = 3;
        final JComboBox<String> lastPurchaseYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 5;
        g.gridx = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 5;
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 5;
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        dateAddedDay.setSelectedIndex(Integer.parseInt(DAY_FORMATTER.format(new Date())));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(MONTH_FORMATTER.format(new Date())));
        dateAddedYear.setSelectedIndex(Integer.parseInt(YEAR_FORMATTER.format(new Date())) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 6;
        innerPanel.add(new JLabel(""), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(""), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 7;
        innerPanel.add(save, g);
        final JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final ArrayList<JTextField> textFields = new ArrayList<>();
                textFields.add(suppNameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                textFields.add(vatNoField);
                final ArrayList<JComboBox<String>> addedBoxes = new ArrayList<>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                final ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (FieldValidator.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    Database.addSupplier(new Supplier(suppNameField.getText(), phoneNoField.getText(), addressField.getText(), vatNoField.getText(),
                            lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/" + lastPurchaseYear.getSelectedItem(),
                            dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/" + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage("New Supplier \"" + suppNameField.getText() + "\" added");
                    removeLeftPanel();

                    SupplierTable.descendingOrderSort = false;
                    SupplierTable.sortItems();
                    SupplierTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private static void addStaffPanel() {
        resetLeftPanel();
        final JPanel innerPanel = addLabelsToPanel(STAFF_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        final String[] genderOptions = { "", "Male", "Female" };
        final String[] staffLevelOptions = { "", "Manager", "Employee" };
        g.fill = GridBagConstraints.HORIZONTAL;
        // JTextFields with GridBagLayout
        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JPasswordField pinField = new JPasswordField(null, 20);
        pinField.setDocument(new JTextFieldLimit(4));
        innerPanel.add(pinField, g);
        g.gridy = 1;
        final JTextField nameField = new JTextField(null, 20);
        innerPanel.add(nameField, g);
        g.gridy = 2;

        final JComboBox<String> genderField = new JComboBox<String>(genderOptions);
        innerPanel.add(genderField, g);
        g.gridy = 3;
        final JTextField phoneNoField = new JTextField(null, 20);
        innerPanel.add(phoneNoField, g);
        g.gridy = 4;
        final JTextField addressField = new JTextField(null, 20);
        innerPanel.add(addressField, g);
        g.gridy = 5;
        final JTextField wageField = new JTextField(null, 20);
        innerPanel.add(wageField, g);
        g.gridy = 6;
        final JComboBox<String> staffLevelField = new JComboBox<String>(staffLevelOptions);
        innerPanel.add(staffLevelField, g);

        g.gridy = 7;
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
        g.gridy = 8;
        innerPanel.add(new JLabel(""), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(""), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 9;
        innerPanel.add(save, g);
        final JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                final ArrayList<JTextField> textFields = new ArrayList<>();
                textFields.add(nameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                final ArrayList<JTextField> doubleFields = new ArrayList<>();
                doubleFields.add(wageField);
                final ArrayList<JPasswordField> pinFields = new ArrayList<>();
                pinFields.add(pinField);
                final ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
                comboBoxes.add(genderField);
                comboBoxes.add(staffLevelField);
                final ArrayList<JComboBox<String>> addedBoxes = new ArrayList<>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);

                if (FieldValidator.checkFields(textFields, null, doubleFields, pinFields, comboBoxes, addedBoxes, null)) {
                    Database.addStaffMember(new Staff(Integer.parseInt(String.valueOf(pinField.getPassword())), nameField.getText(), genderField
                            .getSelectedIndex(), phoneNoField.getText(), addressField.getText(), Double.parseDouble(wageField.getText()),
                            staffLevelField.getSelectedIndex(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                                    + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage("Staff member " + nameField.getText() + " added");
                    removeLeftPanel();

                    StaffTable.descendingOrderSort = false;
                    StaffTable.sortItems();
                    StaffTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private static void addProductPanel() {
        resetLeftPanel();
        final JPanel innerPanel = addLabelsToPanel(PRODUCT_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField prodDescField = new JTextField(null, 20);
        innerPanel.add(prodDescField, g);
        g.gridy = 1;
        final JTextField stockLevelField = new JTextField(null, 20);
        innerPanel.add(stockLevelField, g);
        g.gridy = 2;
        final JTextField maxLevelField = new JTextField(null, 20);
        innerPanel.add(maxLevelField, g);
        g.gridy = 3;

        final String[] supplierNamesAndIds = getSupplierNamesAndIds();
        final JComboBox<String> supplierBox = new JComboBox<String>(supplierNamesAndIds);
        innerPanel.add(supplierBox, g);
        g.gridy = 4;
        final JTextField costPriceField = new JTextField(null, 20);
        innerPanel.add(costPriceField, g);
        g.gridy = 5;
        final JTextField salePriceField = new JTextField(null, 20);
        innerPanel.add(salePriceField, g);

        g.gridx = 0;
        g.gridy = 6;
        innerPanel.add(new JLabel(""), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(""), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 7;
        innerPanel.add(save, g);
        final JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final ArrayList<JTextField> textFields = new ArrayList<>();
                textFields.add(prodDescField);
                final ArrayList<JTextField> intFields = new ArrayList<>();
                intFields.add(stockLevelField);
                intFields.add(maxLevelField);
                final ArrayList<JTextField> doubleFields = new ArrayList<>();
                doubleFields.add(costPriceField);
                doubleFields.add(salePriceField);
                final ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
                comboBoxes.add(supplierBox);

                if (FieldValidator.checkFields(textFields, intFields, doubleFields, null, comboBoxes, null, null)) {
                    final Product product = new Product(prodDescField.getText(), Integer.parseInt(stockLevelField.getText()), Integer
                            .parseInt(maxLevelField.getText()), Integer.parseInt(((String) supplierBox.getSelectedItem()).substring(
                            ((String) supplierBox.getSelectedItem()).length() - 5, ((String) supplierBox.getSelectedItem()).length() - 1)), Double
                            .parseDouble(costPriceField.getText()), Double.parseDouble(salePriceField.getText()));

                    Database.addProduct(product);

                    GuiCreator.setConfirmationMessage("Product " + prodDescField.getText() + " added");
                    removeLeftPanel();

                    ProductTable.descendingOrderSort = false;
                    ProductTable.sortItems();
                    ProductTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private static String[] getSupplierNamesAndIds() {
        final ArrayList<Entity> suppliers = Database.getSuppliers();
        final String[] supplierNamesAndIds = new String[suppliers.size() + 1];

        supplierNamesAndIds[0] = "";
        int supplierIndex = 1;

        for (final Entity supplier : suppliers) {
            supplierNamesAndIds[supplierIndex++] = supplier.getName() + " (" + supplier.getId() + ")";
        }
        return supplierNamesAndIds;
    }
}
