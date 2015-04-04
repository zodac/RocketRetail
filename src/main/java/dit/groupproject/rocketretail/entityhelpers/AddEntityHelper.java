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
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.inputfields.AddressField;
import dit.groupproject.rocketretail.inputfields.CurrencyField;
import dit.groupproject.rocketretail.inputfields.GenderField;
import dit.groupproject.rocketretail.inputfields.NameField;
import dit.groupproject.rocketretail.inputfields.NumberField;
import dit.groupproject.rocketretail.inputfields.PhoneNumberField;
import dit.groupproject.rocketretail.inputfields.PinField;
import dit.groupproject.rocketretail.inputfields.StaffLevelField;
import dit.groupproject.rocketretail.inputfields.SuppliersField;
import dit.groupproject.rocketretail.inputfields.VatField;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

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
    public ActionListener addEntityPanel() {
        final TableState currentState = ShopDriver.getCurrentTableState();

        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (currentState == TableState.CUSTOMER) {
                    addCustomerPanel();
                } else if (currentState == TableState.ORDER) {
                    System.out.println("Not yet implemented!");
                } else if (currentState == TableState.PRODUCT) {
                    addProductPanel();
                } else if (currentState == TableState.STAFF) {
                    addStaffPanel();
                } else if (currentState == TableState.SUPPLIER) {
                    addSupplierPanel();
                } else {
                    throw new IllegalArgumentException("No panel available for current table state [" + currentState.toString() + "]!");
                }
            }
        };
    }

    private void addCustomerPanel() {
        resetLeftPanel();

        final JPanel innerPanel = addLabelsToPanel(CUSTOMER_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;

        final NameField customerNameField = new NameField();
        innerPanel.add(customerNameField, g);
        g.gridy = 1;
        final PhoneNumberField customerPhoneField = new PhoneNumberField();
        innerPanel.add(customerPhoneField, g);
        g.gridy = 2;
        final AddressField addressField = new AddressField();
        innerPanel.add(addressField, g);
        g.gridy = 3;
        final VatField vatNoField = new VatField();
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
                textFields.add(customerNameField);
                textFields.add(customerPhoneField);
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
                    Database.addCustomer(new Customer(customerNameField.getText(), customerPhoneField.getText(), addressField.getText(), vatNoField
                            .getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                            + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                            + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage("Customer " + customerNameField.getText() + " added");
                    removeLeftPanel();

                    customerTable.descendingOrderSort = false;
                    customerTable.sortItems();
                    customerTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private void addSupplierPanel() {
        resetLeftPanel();
        final JPanel innerPanel = addLabelsToPanel(SUPPLIER_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final NameField supplierNameField = new NameField();
        innerPanel.add(supplierNameField, g);
        g.gridy = 1;
        final PhoneNumberField supplierPhoneField = new PhoneNumberField();
        innerPanel.add(supplierPhoneField, g);
        g.gridy = 2;
        final AddressField addressField = new AddressField();
        innerPanel.add(addressField, g);
        g.gridy = 3;
        final VatField vatNoField = new VatField();
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
                textFields.add(supplierNameField);
                textFields.add(supplierPhoneField);
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
                    Database.addSupplier(new Supplier(supplierNameField.getText(), supplierPhoneField.getText(), addressField.getText(), vatNoField
                            .getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                            + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                            + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage(String.format("New Supplier \"%s\" added", supplierNameField.getText()));
                    removeLeftPanel();

                    supplierTable.descendingOrderSort = false;
                    supplierTable.sortItems();
                    supplierTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private void addStaffPanel() {
        resetLeftPanel();
        final JPanel innerPanel = addLabelsToPanel(STAFF_LABELS);

        final GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final PinField pinField = new PinField();
        innerPanel.add(pinField, g);
        g.gridy = 1;
        final NameField staffNameField = new NameField();
        innerPanel.add(staffNameField, g);
        g.gridy = 2;

        final GenderField genderField = new GenderField();
        innerPanel.add(genderField, g);
        g.gridy = 3;
        final PhoneNumberField staffPhoneField = new PhoneNumberField();
        innerPanel.add(staffPhoneField, g);
        g.gridy = 4;
        final AddressField addressField = new AddressField();
        innerPanel.add(addressField, g);
        g.gridy = 5;
        final CurrencyField wageField = new CurrencyField();
        innerPanel.add(wageField, g);
        g.gridy = 6;
        final StaffLevelField staffLevelField = new StaffLevelField();
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
                textFields.add(staffNameField);
                textFields.add(staffPhoneField);
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
                    Database.addStaffMember(new Staff(Integer.parseInt(String.valueOf(pinField.getPassword())), staffNameField.getText(), genderField
                            .getSelectedIndex(), staffPhoneField.getText(), addressField.getText(), Double.parseDouble(wageField.getText()),
                            staffLevelField.getSelectedIndex(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                                    + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage("Staff member " + staffNameField.getText() + " added");
                    removeLeftPanel();

                    staffTable.descendingOrderSort = false;
                    staffTable.sortItems();
                    staffTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private void addProductPanel() {
        resetLeftPanel();
        final JPanel innerPanel = addLabelsToPanel(PRODUCT_LABELS);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final NameField productNameField = new NameField();
        innerPanel.add(productNameField, g);
        g.gridy = 1;
        final NumberField stockLevelField = new NumberField();
        innerPanel.add(stockLevelField, g);
        g.gridy = 2;
        final NumberField maxLevelField = new NumberField();
        innerPanel.add(maxLevelField, g);
        g.gridy = 3;

        final SuppliersField supplierBox = new SuppliersField();
        innerPanel.add(supplierBox, g);
        g.gridy = 4;
        final CurrencyField costPriceField = new CurrencyField();
        innerPanel.add(costPriceField, g);
        g.gridy = 5;
        final CurrencyField salePriceField = new CurrencyField();
        innerPanel.add(salePriceField, g);

        g.gridx = 0;
        g.gridy = 6;
        innerPanel.add(new JLabel(""), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(""), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
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
                textFields.add(productNameField);
                final ArrayList<JTextField> intFields = new ArrayList<>();
                intFields.add(stockLevelField);
                intFields.add(maxLevelField);
                final ArrayList<JTextField> doubleFields = new ArrayList<>();
                doubleFields.add(costPriceField);
                doubleFields.add(salePriceField);
                final ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
                comboBoxes.add(supplierBox);

                if (FieldValidator.checkFields(textFields, intFields, doubleFields, null, comboBoxes, null, null)) {
                    final Product product = new Product(productNameField.getText(), Integer.parseInt(stockLevelField.getText()), Integer
                            .parseInt(maxLevelField.getText()), Integer.parseInt(((String) supplierBox.getSelectedItem()).substring(
                            ((String) supplierBox.getSelectedItem()).length() - 5, ((String) supplierBox.getSelectedItem()).length() - 1)), Double
                            .parseDouble(costPriceField.getText()), Double.parseDouble(salePriceField.getText()));

                    Database.addProduct(product);

                    GuiCreator.setConfirmationMessage("Product " + productNameField.getText() + " added");
                    removeLeftPanel();

                    productTable.descendingOrderSort = false;
                    productTable.sortItems();
                    productTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }
}
