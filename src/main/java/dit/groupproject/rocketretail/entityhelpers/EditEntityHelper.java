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
import dit.groupproject.rocketretail.inputfields.NameField;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

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

        if (currentState == TableState.CUSTOMER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editCustomerPanel(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 10)));
                }
            };
        } else if (currentState == TableState.ORDER) {

        } else if (currentState == TableState.PRODUCT) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editProductPanel(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 10)));
                }
            };
        } else if (currentState == TableState.STAFF) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editStaffPanel(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 10)));
                }
            };
        } else if (currentState == TableState.SUPPLIER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editSupplierPanel(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 10)));
                }
            };
        }
        throw new IllegalArgumentException("No panel available for current table state [" + currentState.toString() + "]!");
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
        final JTextField custIdField = new JTextField(null, 20);
        custIdField.setEditable(false);
        innerPanel.add(custIdField, g);
        g.gridy = 1;
        final NameField customerNameField = new NameField();
        innerPanel.add(customerNameField, g);
        g.gridy = 2;
        final JTextField phoneNoField = new JTextField(null, 20);
        innerPanel.add(phoneNoField, g);
        g.gridy = 3;
        final JTextField addressField = new JTextField(null, 20);
        innerPanel.add(addressField, g);
        g.gridy = 4;
        final JTextField vatNoField = new JTextField(null, 20);
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

        custIdField.setText("" + customer.getId());
        customerNameField.setText(customer.getName());
        phoneNoField.setText(customer.getPhoneNumber());
        addressField.setText(customer.getAddress());
        vatNoField.setText(customer.getVatNumber());
        lastPurchaseDay.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(0, 2)));
        lastPurchaseMonth.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(3, 5)));
        lastPurchaseYear.setSelectedIndex(Integer.parseInt(customer.getLastPurchase().substring(6, 10)) - (YEAR_START - 1));
        dateAddedDay.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(0, 2)));
        dateAddedMonth.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(3, 5)));
        dateAddedYear.setSelectedIndex(Integer.parseInt(customer.getDateAdded().substring(6, 10)) - (YEAR_START - 1));

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
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final ArrayList<JTextField> textFields = new ArrayList<>();
                textFields.add(customerNameField);
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
                    final Customer editedCustomer = new Customer(customerNameField.getText(), phoneNoField.getText(), addressField.getText(), vatNoField
                            .getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                            + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
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
        final JTextField suppIdField = new JTextField(null, 20);
        suppIdField.setEditable(false);
        innerPanel.add(suppIdField, g);
        g.gridy = 1;
        final JTextField suppNameField = new JTextField(null, 20);
        innerPanel.add(suppNameField, g);
        g.gridy = 2;
        final JTextField phoneNoField = new JTextField(null, 20);
        innerPanel.add(phoneNoField, g);
        g.gridy = 3;
        final JTextField addressField = new JTextField(null, 20);
        innerPanel.add(addressField, g);
        g.gridy = 4;
        final JTextField vatNoField = new JTextField(null, 20);
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

        suppIdField.setText("" + supplier.getId());
        suppNameField.setText(supplier.getName());
        phoneNoField.setText(supplier.getPhoneNumber());
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
                    final Supplier editedSupplier = new Supplier(suppNameField.getText(), phoneNoField.getText(), addressField.getText(), vatNoField
                            .getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                            + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                            + dateAddedYear.getSelectedItem());
                    editedSupplier.setId(index + IdManager.SUPPLIER_ID_START);
                    Database.addSupplierByIndex(index, editedSupplier);

                    GuiCreator.setConfirmationMessage("Supplier \"" + suppNameField.getText() + "'s details editted");
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

        final String[] genderOptions = { "", "Male", "Female" };
        final String[] staffLevelOptions = { "", "Manager", "Employee" };
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField idField = new JTextField(null, 20);
        idField.setEditable(false);
        innerPanel.add(idField, g);
        g.gridy = 1;
        final JPasswordField pinField = new JPasswordField(null, 20);
        pinField.setDocument(new JTextFieldLimit(4));
        innerPanel.add(pinField, g);
        g.gridy = 2;
        final JTextField nameField = new JTextField(null, 20);
        innerPanel.add(nameField, g);
        g.gridy = 3;
        final JComboBox<String> genderField = new JComboBox<>(genderOptions);
        innerPanel.add(genderField, g);
        g.gridy = 4;
        final JTextField phoneNoField = new JTextField(null, 20);
        innerPanel.add(phoneNoField, g);
        g.gridy = 5;
        final JTextField addressField = new JTextField(null, 20);
        innerPanel.add(addressField, g);
        g.gridy = 6;
        final JTextField wageField = new JTextField(null, 20);
        innerPanel.add(wageField, g);
        g.gridy = 7;
        final JComboBox<String> staffLevelField = new JComboBox<>(staffLevelOptions);
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

        idField.setText(ID_FORMATTER.format(staffId));
        pinField.setText("" + staff.getStaffPin());
        nameField.setText(staff.getName());
        genderField.setSelectedIndex(staff.getGender());
        phoneNoField.setText(staff.getPhoneNumber());
        addressField.setText(staff.getAddress());
        wageField.setText("" + staff.getWage());
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
                    final Staff editedStaff = new Staff(Integer.parseInt(String.valueOf(pinField.getPassword())), nameField.getText(), genderField
                            .getSelectedIndex(), phoneNoField.getText(), addressField.getText(), Double.parseDouble(wageField.getText()),
                            staffLevelField.getSelectedIndex(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                                    + dateAddedYear.getSelectedItem());
                    editedStaff.setId(index + IdManager.STAFF_ID_START);
                    Database.addStaffMemberByIndex(index, editedStaff);

                    GuiCreator.setConfirmationMessage("Staff member " + nameField.getText() + "'s details editted");
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
        final JTextField prodIdField = new JTextField(null, 20);
        prodIdField.setEditable(false);
        innerPanel.add(prodIdField, g);
        g.gridy = 1;
        final JTextField prodDescField = new JTextField(null, 20);
        innerPanel.add(prodDescField, g);
        g.gridy = 2;
        final JTextField stockLevelField = new JTextField(null, 20);
        innerPanel.add(stockLevelField, g);
        g.gridy = 3;
        final JTextField maxLevelField = new JTextField(null, 20);
        innerPanel.add(maxLevelField, g);
        g.gridy = 4;

        final String[] supplierOptions = new String[Database.getSuppliers().size() + 1];
        supplierOptions[0] = "";

        for (int i = 1; i < supplierOptions.length; i++) {
            supplierOptions[i] = Database.getSupplierByIndex(i - 1).getName() + " (" + Database.getSupplierByIndex(i - 1).getId() + ")";
        }
        final JComboBox<String> suppIdBox = new JComboBox<String>(supplierOptions);
        innerPanel.add(suppIdBox, g);
        g.gridy = 5;
        final JTextField costPriceField = new JTextField(null, 20);
        innerPanel.add(costPriceField, g);
        g.gridy = 6;
        final JTextField salePriceField = new JTextField(null, 20);
        innerPanel.add(salePriceField, g);

        // Set JTextFields with current data
        prodIdField.setText("" + product.getId());
        prodDescField.setText(product.getName());
        stockLevelField.setText("" + product.getCurrentStockLevel());
        maxLevelField.setText("" + product.getMaxStockLevel());
        suppIdBox.setSelectedIndex(product.getSupplierId() - IdManager.SUPPLIER_ID_START + 1);
        costPriceField.setText("" + product.getCostPrice());
        salePriceField.setText("" + product.getSalePrice());

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
                textFields.add(prodDescField);
                final ArrayList<JTextField> intFields = new ArrayList<>();
                intFields.add(stockLevelField);
                intFields.add(maxLevelField);
                final ArrayList<JTextField> doubleFields = new ArrayList<>();
                doubleFields.add(costPriceField);
                doubleFields.add(salePriceField);
                final ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
                comboBoxes.add(suppIdBox);

                if (FieldValidator.checkFields(textFields, intFields, doubleFields, null, comboBoxes, null, null)) {
                    final Product editedProduct = new Product(prodDescField.getText(), Integer.parseInt(stockLevelField.getText()), Integer
                            .parseInt(maxLevelField.getText()),
                            Integer.parseInt(((String) suppIdBox.getSelectedItem()).split("\\(")[1].split("\\)")[0]), Double
                                    .parseDouble(costPriceField.getText()), Double.parseDouble(salePriceField.getText()));
                    editedProduct.setId(index + IdManager.PRODUCT_ID_START);
                    Database.addProductByIndex(index, editedProduct);

                    GuiCreator.setConfirmationMessage("Product " + prodDescField.getText() + "'s details editted");
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
