package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.utilities.Dates.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;
import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.inputfields.AddressField;
import dit.groupproject.rocketretail.inputfields.CurrencyField;
import dit.groupproject.rocketretail.inputfields.GenderField;
import dit.groupproject.rocketretail.inputfields.IdField;
import dit.groupproject.rocketretail.inputfields.NameField;
import dit.groupproject.rocketretail.inputfields.NumberField;
import dit.groupproject.rocketretail.inputfields.PhoneNumberField;
import dit.groupproject.rocketretail.inputfields.PinField;
import dit.groupproject.rocketretail.inputfields.StaffLevelField;
import dit.groupproject.rocketretail.inputfields.SuppliersField;
import dit.groupproject.rocketretail.inputfields.VatField;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

public class EditEntityHelper extends AbstractEntityHelper {

    private final static String[] CUSTOMER_LABELS = { "Customer ID", "Customer Name", "Phone Number", "Address", "VAT Number", "Last Purchase",
            "Date Added" };
    private final static String[] PRODUCT_LABELS = { "Product ID", "Product Name", "Stock Level", "Maximum Level", "Supplier ID", "Cost Price",
            "Sale Price" };
    private final static String[] STAFF_LABELS = { "Staff ID", "Staff PIN", "Name", "Gender", "Phone Number", "Address", "Wage", "Staff Level",
            "Date Added" };
    private final static String[] SUPPLIER_LABELS = { "Supplier ID", "Supplier Name", "Phone Number", "Address", "VAT Number", "Last Purchase",
            "Date Added" };

    /**
     * Returns an ActionListener which adds an Entity edit panel to the left
     * side of the GUI screen.
     * <p>
     * Checks the current table state of the system to determine which Entity
     * table to use.
     */
    public ActionListener editEntityPanel(final JComboBox<String> editBox) {
        final TableState currentState = ShopDriver.getCurrentTableState();

        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final int selectedIdIndex = Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 10));

                if (currentState == TableState.CUSTOMER) {
                    editCustomerPanel(selectedIdIndex);
                } else if (currentState == TableState.ORDER) {
                    System.out.println("Not yet implemented!");
                } else if (currentState == TableState.PRODUCT) {
                    editProductPanel(selectedIdIndex);
                } else if (currentState == TableState.STAFF) {
                    editStaffPanel(selectedIdIndex);
                } else if (currentState == TableState.SUPPLIER) {
                    editSupplierPanel(selectedIdIndex);
                } else {
                    throw new IllegalArgumentException("No panel available for current table state [" + currentState.toString() + "]!");
                }
            }
        };
    }

    private void editCustomerPanel(final int customerId) {
        resetLeftPanel();

        final Customer customer = (Customer) Database.getCustomerById(customerId);
        final int index = Database.getIndexOfCustomer(customer);
        final JPanel innerPanel = addLabelsToPanel(CUSTOMER_LABELS);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final IdField customerIdField = new IdField();
        innerPanel.add(customerIdField, g);
        g.gridy = 1;
        final NameField customerNameField = new NameField();
        innerPanel.add(customerNameField, g);
        g.gridy = 2;
        final PhoneNumberField customerPhoneField = new PhoneNumberField();
        innerPanel.add(customerPhoneField, g);
        g.gridy = 3;
        final AddressField customerAddressField = new AddressField();
        innerPanel.add(customerAddressField, g);
        g.gridy = 4;
        final VatField customerVatField = new VatField();
        innerPanel.add(customerVatField, g);

        g.gridy = 5;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> lastPurchaseDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);

        g.gridy = 5;
        g.gridx = 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);

        g.gridy = 5;
        g.gridx = 3;
        final JComboBox<String> lastPurchaseYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 6;
        g.gridx = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 6;
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 6;
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        customerIdField.setText(ID_FORMATTER.format(customer.getId()));
        customerNameField.setText(customer.getName());
        customerPhoneField.setText(customer.getPhoneNumber());
        customerAddressField.setText(customer.getAddress());
        customerVatField.setText(customer.getVatNumber());
        lastPurchaseDay.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(0, 2)));
        lastPurchaseMonth.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(3, 5)));
        lastPurchaseYear.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(6, 10)) - (YEAR_START - 1));
        dateAddedDay.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(0, 2)));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(3, 5)));
        dateAddedYear.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(6, 10)) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 8;
        innerPanel.add(new JLabel(""), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(""), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 9;
        innerPanel.add(save, g);
        final JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final ArrayList<JTextField> textFields = new ArrayList<>();
                textFields.add(customerNameField);
                textFields.add(customerPhoneField);
                textFields.add(customerAddressField);
                textFields.add(customerVatField);
                final ArrayList<JComboBox<String>> addedBoxes = new ArrayList<>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                final ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (FieldValidator.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    final Customer editedCustomer = new Customer(customerNameField.getText(), customerPhoneField.getText(), customerAddressField
                            .getText(), customerVatField.getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem()
                            + "/" + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                            + dateAddedYear.getSelectedItem());
                    editedCustomer.setId(index + IdManager.CUSTOMER_ID_START);
                    Database.addCustomerByIndex(index, editedCustomer);

                    GuiCreator.setConfirmationMessage("Customer " + customerNameField.getText() + "'s details editted");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();
                    Database.removeCustomerByIndex(index + 1);
                    customerTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private void editSupplierPanel(final int supplierId) {
        resetLeftPanel();

        final Supplier supplier = (Supplier) Database.getSupplierById(supplierId);
        final int index = Database.getIndexOfSupplier(supplier);
        final JPanel innerPanel = addLabelsToPanel(SUPPLIER_LABELS);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final IdField supplierIdField = new IdField();
        innerPanel.add(supplierIdField, g);
        g.gridy = 1;
        final NameField supplierNameField = new NameField();
        innerPanel.add(supplierNameField, g);
        g.gridy = 2;
        final PhoneNumberField supplierPhoneField = new PhoneNumberField();
        innerPanel.add(supplierPhoneField, g);
        g.gridy = 3;
        final AddressField addressField = new AddressField();
        innerPanel.add(addressField, g);
        g.gridy = 4;
        final VatField vatNoField = new VatField();
        innerPanel.add(vatNoField, g);

        g.gridy = 5;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> lastPurchaseDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);

        g.gridy = 5;
        g.gridx = 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);

        g.gridy = 5;
        g.gridx = 3;
        final JComboBox<String> lastPurchaseYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 6;
        g.gridx = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 6;
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 6;
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        supplierIdField.setText(ID_FORMATTER.format(supplier.getId()));
        supplierNameField.setText(supplier.getName());
        supplierPhoneField.setText(supplier.getPhoneNumber());
        addressField.setText(supplier.getAddress());
        vatNoField.setText(supplier.getVatNumber());
        lastPurchaseDay.setSelectedIndex(Integer.parseInt(supplier.getLastPurchase().substring(0, 2)));
        lastPurchaseMonth.setSelectedIndex(Integer.parseInt(supplier.getLastPurchase().substring(3, 5)));
        lastPurchaseYear.setSelectedIndex(Integer.parseInt(supplier.getLastPurchase().substring(6, 10)) - (YEAR_START - 1));
        dateAddedDay.setSelectedIndex(Integer.parseInt(supplier.getDateAdded().substring(0, 2)));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(supplier.getDateAdded().substring(3, 5)));
        dateAddedYear.setSelectedIndex(Integer.parseInt(supplier.getDateAdded().substring(6, 10)) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 8;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
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
                    final Supplier editedSupplier = new Supplier(supplierNameField.getText(), supplierPhoneField.getText(), addressField.getText(),
                            vatNoField.getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                                    + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem()
                                    + "/" + dateAddedYear.getSelectedItem());
                    editedSupplier.setId(index + IdManager.SUPPLIER_ID_START);
                    Database.addSupplierByIndex(index, editedSupplier);

                    GuiCreator.setConfirmationMessage("Supplier \"" + supplierNameField.getText() + "'s details editted");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();
                    Database.removeSupplierByIndex(index + 1);
                    supplierTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private void editStaffPanel(int staffId) {
        resetLeftPanel();

        final Staff staff = (Staff) Database.getStaffMemberById(staffId);
        final int index = Database.getIndexOfStaff(staff);
        final JPanel innerPanel = addLabelsToPanel(STAFF_LABELS);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final IdField staffIdField = new IdField();
        innerPanel.add(staffIdField, g);
        g.gridy = 1;
        final PinField pinField = new PinField();
        innerPanel.add(pinField, g);
        g.gridy = 2;
        final NameField staffNameField = new NameField();
        innerPanel.add(staffNameField, g);
        g.gridy = 3;
        final GenderField genderField = new GenderField();
        innerPanel.add(genderField, g);
        g.gridy = 4;
        final PhoneNumberField staffPhoneField = new PhoneNumberField();
        innerPanel.add(staffPhoneField, g);
        g.gridy = 5;
        final AddressField addressField = new AddressField();
        innerPanel.add(addressField, g);
        g.gridy = 6;
        final CurrencyField wageField = new CurrencyField();
        innerPanel.add(wageField, g);
        g.gridy = 7;
        final StaffLevelField staffLevelField = new StaffLevelField();
        innerPanel.add(staffLevelField, g);

        g.gridy = 8;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 8;
        g.gridx = 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 8;
        g.gridx = 3;
        final JComboBox<String> dateAddedYear = new JComboBox<>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        staffIdField.setText(ID_FORMATTER.format(staffId));
        pinField.setText(String.valueOf(staff.getStaffPin()));
        staffNameField.setText(staff.getName());
        genderField.setSelectedIndex(staff.getGender());
        staffPhoneField.setText(staff.getPhoneNumber());
        addressField.setText(staff.getAddress());
        wageField.setText(String.valueOf(staff.getWage()));
        staffLevelField.setSelectedIndex(staff.getStaffLevel());
        dateAddedDay.setSelectedIndex(Integer.parseInt(staff.getDateAdded().substring(0, 2)));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(staff.getDateAdded().substring(3, 5)));
        dateAddedYear.setSelectedIndex(Integer.parseInt(staff.getDateAdded().substring(6, 10)) - (YEAR_START - 1));

        g.gridx = 0;
        g.gridy = 9;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(" "), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 10;
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
                    final Staff editedStaff = new Staff(Integer.parseInt(String.valueOf(pinField.getPassword())), staffNameField.getText(),
                            genderField.getSelectedIndex(), staffPhoneField.getText(), addressField.getText(),
                            Double.parseDouble(wageField.getText()), staffLevelField.getSelectedIndex(), dateAddedDay.getSelectedItem() + "/"
                                    + dateAddedMonth.getSelectedItem() + "/" + dateAddedYear.getSelectedItem());
                    editedStaff.setId(index + IdManager.STAFF_ID_START);
                    Database.addStaffMemberByIndex(index, editedStaff);

                    GuiCreator.setConfirmationMessage("Staff member " + staffNameField.getText() + "'s details editted");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.validate();
                    Database.removeStaffByIndex(index + 1);
                    staffTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }

    private void editProductPanel(int productId) {
        resetLeftPanel();

        final Product product = (Product) Database.getProductById(productId);
        final int index = Database.getIndexOfProduct(product);
        final JPanel innerPanel = addLabelsToPanel(PRODUCT_LABELS);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final IdField productIdField = new IdField();
        productIdField.setEditable(false);
        innerPanel.add(productIdField, g);
        g.gridy = 1;
        final NameField productNameField = new NameField();
        innerPanel.add(productNameField, g);
        g.gridy = 2;
        final NumberField stockLevelField = new NumberField();
        innerPanel.add(stockLevelField, g);
        g.gridy = 3;
        final NumberField maxLevelField = new NumberField();
        innerPanel.add(maxLevelField, g);
        g.gridy = 4;

        final SuppliersField suppIdBox = new SuppliersField();
        innerPanel.add(suppIdBox, g);
        g.gridy = 5;
        final CurrencyField costPriceField = new CurrencyField();
        innerPanel.add(costPriceField, g);
        g.gridy = 6;
        final CurrencyField salePriceField = new CurrencyField();
        innerPanel.add(salePriceField, g);

        productIdField.setText(ID_FORMATTER.format(product.getId()));
        productNameField.setText(product.getName());
        stockLevelField.setText(String.valueOf(product.getCurrentStockLevel()));
        maxLevelField.setText(String.valueOf(product.getMaxStockLevel()));
        suppIdBox.setSelectedIndex(product.getSupplierId() - IdManager.SUPPLIER_ID_START + 1);
        costPriceField.setText(String.valueOf(product.getCostPrice()));
        salePriceField.setText(String.valueOf(product.getSalePrice()));

        g.gridx = 0;
        g.gridy = 7;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(" "), g);

        final JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 8;
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
                comboBoxes.add(suppIdBox);

                if (FieldValidator.checkFields(textFields, intFields, doubleFields, null, comboBoxes, null, null)) {
                    final Product editedProduct = new Product(productNameField.getText(), Integer.parseInt(stockLevelField.getText()), Integer
                            .parseInt(maxLevelField.getText()),
                            Integer.parseInt(((String) suppIdBox.getSelectedItem()).split("\\(")[1].split("\\)")[0]), Double
                                    .parseDouble(costPriceField.getText()), Double.parseDouble(salePriceField.getText()));
                    editedProduct.setId(index + IdManager.PRODUCT_ID_START);
                    Database.addProductByIndex(index, editedProduct);

                    GuiCreator.setConfirmationMessage("Product " + productNameField.getText() + "'s details editted");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();
                    Database.removeProductByIndex(index + 1);
                    productTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(cancelListener);
        updateLeftPanel(innerPanel);
    }
}
