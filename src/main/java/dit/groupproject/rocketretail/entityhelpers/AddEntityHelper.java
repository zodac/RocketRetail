package dit.groupproject.rocketretail.entityhelpers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.inputfields.CurrencyField;
import dit.groupproject.rocketretail.inputfields.DateField;
import dit.groupproject.rocketretail.inputfields.GenderField;
import dit.groupproject.rocketretail.inputfields.InputField;
import dit.groupproject.rocketretail.inputfields.NameField;
import dit.groupproject.rocketretail.inputfields.NumberField;
import dit.groupproject.rocketretail.inputfields.PhoneNumberField;
import dit.groupproject.rocketretail.inputfields.PinField;
import dit.groupproject.rocketretail.inputfields.StaffLevelField;
import dit.groupproject.rocketretail.inputfields.SuppliersField;
import dit.groupproject.rocketretail.inputfields.TextField;
import dit.groupproject.rocketretail.inputfields.VatField;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

public class AddEntityHelper extends BaseEntityHelper {

    private final static String[] CUSTOMER_LABELS = { "Customer Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };
    private final static String[] PRODUCT_LABELS = { "Product Description", "Stock Level", "Maximum Lavel", "Supplier ID", "Cost Price", "Sale Price" };
    private final static String[] STAFF_LABELS = { "Staff PIN", "Name", "Gender", "Phone Number", "Address", "Wage", "Staff Level", "Date Added" };
    private final static String[] SUPPLIER_LABELS = { "Supplier Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added" };

    private final static String CUSTOMER_ADDED_FORMAT = "Customer \"%s\" added";
    private final static String PRODUCT_ADDED_FORMAT = "Product %s added";
    private final static String STAFF_MEMBER_ADDED_FORMAT = "Staff member \"%s\" added";
    private final static String SUPPLIER_ADDED_FORMAT = "New Supplier \"%s\" added";

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
        g.gridwidth = 3;

        g.gridy = 0;
        final NameField customerNameField = new NameField();
        innerPanel.add(customerNameField, g);
        g.gridy = 1;
        final PhoneNumberField customerPhoneField = new PhoneNumberField();
        innerPanel.add(customerPhoneField, g);
        g.gridy = 2;
        final TextField customerAddressField = new TextField();
        innerPanel.add(customerAddressField, g);
        g.gridy = 3;
        final VatField customerVatField = new VatField();
        innerPanel.add(customerVatField, g);

        final DateField lastPurchaseDateField = new DateField();
        lastPurchaseDateField.addToPanel(innerPanel, g);
        final DateField dateAddedField = new DateField();
        dateAddedField.setCurrentDate();
        dateAddedField.addToPanel(innerPanel, g);

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
                final List<InputField> inputFields = new ArrayList<>();
                inputFields.add(customerNameField);
                inputFields.add(customerPhoneField);
                inputFields.add(customerAddressField);
                inputFields.add(customerVatField);
                inputFields.add(lastPurchaseDateField);
                inputFields.add(dateAddedField);

                if (FieldValidator.checkFields(inputFields)) {
                    final Customer customer = new Customer(customerNameField.getText(), customerPhoneField.getText(), customerAddressField.getText(),
                            customerVatField.getText(), lastPurchaseDateField.getDate(), dateAddedField.getDate());

                    Database.addCustomer(customer);
                    GuiCreator.setConfirmationMessage(String.format(CUSTOMER_ADDED_FORMAT, customerNameField.getText()));
                    removeLeftPanel();

                    customerTable.descendingOrderSort = false;
                    customerTable.sortItems();
                    customerTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
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
        final TextField supplierAddressField = new TextField();
        innerPanel.add(supplierAddressField, g);
        g.gridy = 3;
        final VatField supplierVatField = new VatField();
        innerPanel.add(supplierVatField, g);

        final DateField lastPurchaseDateField = new DateField();
        lastPurchaseDateField.addToPanel(innerPanel, g);
        final DateField dateAddedField = new DateField();
        dateAddedField.setCurrentDate();
        dateAddedField.addToPanel(innerPanel, g);

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
                final List<InputField> inputFields = new ArrayList<>();
                inputFields.add(supplierNameField);
                inputFields.add(supplierPhoneField);
                inputFields.add(supplierAddressField);
                inputFields.add(supplierVatField);
                inputFields.add(lastPurchaseDateField);
                inputFields.add(dateAddedField);

                if (FieldValidator.checkFields(inputFields)) {
                    final Supplier supplier = new Supplier(supplierNameField.getText(), supplierPhoneField.getText(), supplierAddressField.getText(),
                            supplierVatField.getText(), lastPurchaseDateField.getDate(), dateAddedField.getDate());

                    Database.addSupplier(supplier);
                    GuiCreator.setConfirmationMessage(String.format(SUPPLIER_ADDED_FORMAT, supplierNameField.getText()));
                    removeLeftPanel();

                    supplierTable.descendingOrderSort = false;
                    supplierTable.sortItems();
                    supplierTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
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
        final TextField staffAddressField = new TextField();
        innerPanel.add(staffAddressField, g);
        g.gridy = 5;
        final CurrencyField wageField = new CurrencyField();
        innerPanel.add(wageField, g);
        g.gridy = 6;
        final StaffLevelField staffLevelField = new StaffLevelField();
        innerPanel.add(staffLevelField, g);

        final DateField dateAddedField = new DateField();
        dateAddedField.setCurrentDate();
        dateAddedField.addToPanel(innerPanel, g);

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

                final List<InputField> inputFields = new ArrayList<>();
                inputFields.add(staffNameField);
                inputFields.add(staffPhoneField);
                inputFields.add(staffAddressField);
                inputFields.add(wageField);
                inputFields.add(pinField);
                inputFields.add(genderField);
                inputFields.add(staffLevelField);
                inputFields.add(dateAddedField);

                if (FieldValidator.checkFields(inputFields)) {
                    final Staff staff = new Staff(pinField.getPinValue(), staffNameField.getText(), genderField.getSelectedIndex(), staffPhoneField
                            .getText(), staffAddressField.getText(), wageField.getValue(), staffLevelField.getSelectedIndex(), dateAddedField
                            .getDate());

                    Database.addStaffMember(staff);
                    GuiCreator.setConfirmationMessage(String.format(STAFF_MEMBER_ADDED_FORMAT, staffNameField.getText()));
                    removeLeftPanel();

                    staffTable.descendingOrderSort = false;
                    staffTable.sortItems();
                    staffTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
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
        final TextField productNameField = new TextField();
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

                final List<InputField> inputFields = new ArrayList<>();
                inputFields.add(productNameField);
                inputFields.add(stockLevelField);
                inputFields.add(maxLevelField);
                inputFields.add(costPriceField);
                inputFields.add(salePriceField);
                inputFields.add(supplierBox);

                if (FieldValidator.checkFields(inputFields)) {
                    final Product product = new Product(productNameField.getText(), stockLevelField.getValue(), maxLevelField.getValue(), supplierBox
                            .getSelectedSupplierId(), costPriceField.getValue(), salePriceField.getValue());

                    Database.addProduct(product);
                    GuiCreator.setConfirmationMessage(String.format(PRODUCT_ADDED_FORMAT, productNameField.getText()));
                    removeLeftPanel();

                    productTable.descendingOrderSort = false;
                    productTable.sortItems();
                    productTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
        updateLeftPanel(innerPanel);
    }
}
