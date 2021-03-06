package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.inputfields.CurrencyField;
import dit.groupproject.rocketretail.inputfields.DateField;
import dit.groupproject.rocketretail.inputfields.GenderField;
import dit.groupproject.rocketretail.inputfields.IdField;
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

public class EditEntityHelper extends BaseEntityHelper {

    private final static String CUSTOMER_UPDATED_MESSAGE_FORMAT = "Customer %s's details updated";
    private final static String PRODUCT_UPDATED_MESSAGE_FORMAT = "Product %s's details updated";
    private final static String STAFF_UPDATED_MESSAGE_FORMAT = "Staff member %s's details updated";
    private final static String SUPPLIER_UPDATED_MESSAGE_FORMAT = "Supplier %s's details updated";

    private final static String[] CUSTOMER_LABELS = { "Customer ID", "Customer Name", "Phone Number", "Address", "VAT Number", "Last Purchase",
            "Date Added" };
    private final static String[] PRODUCT_LABELS = { "Product ID", "Product Name", "Stock Level", "Maximum Level", "Supplier", "Cost Price",
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
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final TableState currentState = ShopDriver.getCurrentTableState();

                if (editBox.getSelectedIndex() > 0) {
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
                        throw new IllegalArgumentException(String.format("No panel available for current table state [%s]!", currentState.toString()));
                    }
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
        final TextField customerAddressField = new TextField();
        innerPanel.add(customerAddressField, g);
        g.gridy = 4;
        final VatField customerVatField = new VatField();
        innerPanel.add(customerVatField, g);

        final DateField lastPurchaseDateField = new DateField();
        lastPurchaseDateField.addToPanel(innerPanel, g);
        final DateField dateAddedField = new DateField();
        dateAddedField.addToPanel(innerPanel, g);

        customerIdField.setText(ID_FORMATTER.format(customer.getId()));
        customerNameField.setText(customer.getName());
        customerPhoneField.setText(customer.getPhoneNumber());
        customerAddressField.setText(customer.getAddress());
        customerVatField.setText(customer.getVatNumber());
        lastPurchaseDateField.setDate(customer.getLastPurchase());
        dateAddedField.setDate(customer.getDateAdded());

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
                final List<InputField> inputFields = new ArrayList<>();
                inputFields.add(customerNameField);
                inputFields.add(customerPhoneField);
                inputFields.add(customerAddressField);
                inputFields.add(customerVatField);
                inputFields.add(lastPurchaseDateField);
                inputFields.add(dateAddedField);

                if (FieldValidator.checkFields(inputFields)) {
                    final Customer editedCustomer = new Customer(customerNameField.getText(), customerPhoneField.getText(), customerAddressField
                            .getText(), customerVatField.getText(), lastPurchaseDateField.getDate(), dateAddedField.getDate());

                    editedCustomer.setId(index + IdManager.CUSTOMER_ID_START);
                    Database.addCustomerByIndex(index, editedCustomer);
                    GuiCreator.setConfirmationMessage(String.format(CUSTOMER_UPDATED_MESSAGE_FORMAT, editedCustomer.getName()));
                    removeLeftPanel();
                    Database.removeCustomerByIndex(index + 1);
                    customerTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
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
        final TextField supplierAddressField = new TextField();
        innerPanel.add(supplierAddressField, g);
        g.gridy = 4;
        final VatField supplierVatField = new VatField();
        innerPanel.add(supplierVatField, g);

        final DateField lastPurchaseDateField = new DateField();
        lastPurchaseDateField.addToPanel(innerPanel, g);
        final DateField dateAddedField = new DateField();
        dateAddedField.addToPanel(innerPanel, g);

        supplierIdField.setText(ID_FORMATTER.format(supplier.getId()));
        supplierNameField.setText(supplier.getName());
        supplierPhoneField.setText(supplier.getPhoneNumber());
        supplierAddressField.setText(supplier.getAddress());
        supplierVatField.setText(supplier.getVatNumber());
        lastPurchaseDateField.setDate(supplier.getLastPurchase());
        dateAddedField.setDate(supplier.getDateAdded());

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
        g.gridy = 9;
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
                    final Supplier editedSupplier = new Supplier(supplierNameField.getText(), supplierPhoneField.getText(), supplierAddressField
                            .getText(), supplierVatField.getText(), lastPurchaseDateField.getDate(), dateAddedField.getDate());

                    editedSupplier.setId(index + IdManager.SUPPLIER_ID_START);
                    Database.addSupplierByIndex(index, editedSupplier);
                    GuiCreator.setConfirmationMessage(String.format(SUPPLIER_UPDATED_MESSAGE_FORMAT, editedSupplier.getName()));
                    removeLeftPanel();
                    Database.removeSupplierByIndex(index + 1);
                    supplierTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
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
        final TextField staffAddressField = new TextField();
        innerPanel.add(staffAddressField, g);
        g.gridy = 6;
        final CurrencyField wageField = new CurrencyField();
        innerPanel.add(wageField, g);
        g.gridy = 7;
        final StaffLevelField staffLevelField = new StaffLevelField();
        innerPanel.add(staffLevelField, g);

        final DateField dateAddedField = new DateField();
        dateAddedField.addToPanel(innerPanel, g);

        staffIdField.setText(ID_FORMATTER.format(staffId));
        pinField.setText(String.valueOf(staff.getStaffPin()));
        staffNameField.setText(staff.getName());
        genderField.setSelectedIndex(staff.getGender());
        staffPhoneField.setText(staff.getPhoneNumber());
        staffAddressField.setText(staff.getAddress());
        wageField.setText(String.valueOf(staff.getWage()));
        staffLevelField.setSelectedIndex(staff.getStaffLevel());
        dateAddedField.setDate(staff.getDateAdded());

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
                    final Staff editedStaff = new Staff(pinField.getPinValue(), staffNameField.getText(), genderField.getSelectedIndex(),
                            staffPhoneField.getText(), staffAddressField.getText(), wageField.getValue(), staffLevelField.getSelectedIndex(),
                            dateAddedField.getDate());

                    editedStaff.setId(index + IdManager.STAFF_ID_START);
                    Database.addStaffMemberByIndex(index, editedStaff);

                    GuiCreator.setConfirmationMessage(String.format(STAFF_UPDATED_MESSAGE_FORMAT, editedStaff.getName()));
                    removeLeftPanel();
                    Database.removeStaffByIndex(index + 1);
                    staffTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
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
        final TextField productNameField = new TextField();
        innerPanel.add(productNameField, g);
        g.gridy = 2;
        final NumberField stockLevelField = new NumberField();
        innerPanel.add(stockLevelField, g);
        g.gridy = 3;
        final NumberField maxLevelField = new NumberField();
        innerPanel.add(maxLevelField, g);
        g.gridy = 4;

        final SuppliersField supplierBox = new SuppliersField();
        innerPanel.add(supplierBox, g);
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
        supplierBox.setSelectedIndex(product.getSupplierId() - IdManager.SUPPLIER_ID_START + 1);
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

                final List<InputField> inputFields = new ArrayList<>();
                inputFields.add(productNameField);
                inputFields.add(stockLevelField);
                inputFields.add(maxLevelField);
                inputFields.add(costPriceField);
                inputFields.add(salePriceField);
                inputFields.add(supplierBox);

                if (FieldValidator.checkFields(inputFields)) {
                    final Product editedProduct = new Product(productNameField.getText(), stockLevelField.getValue(), maxLevelField.getValue(),
                            supplierBox.getSelectedSupplierId(), costPriceField.getValue(), salePriceField.getValue());

                    editedProduct.setId(index + IdManager.PRODUCT_ID_START);
                    Database.addProductByIndex(index, editedProduct);
                    GuiCreator.setConfirmationMessage(String.format(PRODUCT_UPDATED_MESSAGE_FORMAT, editedProduct.getName()));
                    removeLeftPanel();
                    Database.removeProductByIndex(index + 1);
                    productTable.createTableGui();
                }
            }
        });

        cancel.addActionListener(CANCEL_LISTENER);
        updateLeftPanel(innerPanel);
    }
}
