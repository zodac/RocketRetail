package dit.groupproject.rocketretail.entityhelpers;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;

public class ViewEntityHelper extends EntityHelper {

    private final static String[] ORDER_COLUMN_NAMES_FROM_CUSTOMER_TABLE = { "Order ID", "Order Date", "Delivery Date", "Total Cost" };

    /**
     * Returns a MouseAdapter which opens a subtable with additional Entity
     * information.
     * <p>
     * Checks the current table state of the system to determine which Entity
     * table to use.
     */
    public static MouseAdapter viewEntityTable(final JTable entitySubTable) {
        final TableState currentState = ShopDriver.getCurrentTableState();

        if (currentState == TableState.CUSTOMER) {
            return new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (entitySubTable.getSelectedRow() >= 0) {
                        final int selectedId = Integer.parseInt((String) entitySubTable.getValueAt(entitySubTable.getSelectedRow(), 0));
                        final Customer customer = (Customer) Database.getCustomerById(selectedId);

                        viewCustomerInfo(customer);
                    }
                }
            };
        } else if (currentState == TableState.ORDER) {

        } else if (currentState == TableState.PRODUCT) {

        } else if (currentState == TableState.STAFF) {
            return new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (entitySubTable.getSelectedRow() >= 0) {
                        final int selectedId = Integer.parseInt((String) entitySubTable.getValueAt(entitySubTable.getSelectedRow(), 0));
                        final Staff staff = (Staff) Database.getStaffMemberById(selectedId);

                        viewStaffInfo(staff);
                    }
                }
            };
        } else if (currentState == TableState.SUPPLIER) {
            return new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (entitySubTable.getSelectedRow() >= 0) {
                        final int selectedId = Integer.parseInt((String) entitySubTable.getValueAt(entitySubTable.getSelectedRow(), 0));
                        final Supplier supplier = (Supplier) Database.getSupplierById(selectedId);

                        viewSupplierInfo(supplier);
                    }
                }
            };
        }
        throw new IllegalArgumentException("No sub-table available for current table state [" + currentState.toString() + "]!");
    }

    private static void viewCustomerInfo(final Customer customer) {
        final String customerName = customer.getCustomerName();

        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + customerName);
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        final JPanel titlePanel = new JPanel(new GridBagLayout());
        final JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        final JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final JLabel customerLabel = new JLabel("Customer");
        final JLabel vatNumberLabel = new JLabel("VAT Number");
        final JLabel phoneNoLabel = new JLabel("Phone Number");
        final JLabel addressLabel = new JLabel("Address");
        final JLabel dateAddedLabel = new JLabel("Date Added");
        final JLabel titleLabel = new JLabel("Sales to " + customerName);

        final Font currentFont = new JLabel().getFont();
        final Font labelFont = new Font(currentFont.getFontName(), Font.BOLD, currentFont.getSize());

        customerLabel.setFont(labelFont);
        vatNumberLabel.setFont(labelFont);
        phoneNoLabel.setFont(labelFont);
        addressLabel.setFont(labelFont);
        dateAddedLabel.setFont(labelFont);
        titleLabel.setFont(labelFont);

        final int textFieldSize = 20;

        final JTextField customerField = new JTextField(customerName + " (" + CUSTOMER_ID_FORMATTER.format(customer.getId()) + ")", textFieldSize);
        customerField.setEditable(false);
        JTextField vatNumberField = new JTextField(customer.getVatNumber(), textFieldSize);
        vatNumberField.setEditable(false);

        JTextField phoneNoField = new JTextField(customer.getPhoneNumber(), textFieldSize);
        phoneNoField.setEditable(false);
        JTextField addressField = new JTextField(customer.getAddress(), textFieldSize);
        addressField.setEditable(false);
        JTextField dateAddedField = new JTextField(customer.getDateAdded(), textFieldSize);
        dateAddedField.setEditable(false);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        titlePanel.add(customerLabel, g);
        g.gridx = 1;
        titlePanel.add(customerField, g);
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

        ArrayList<Order> customerOrders = new ArrayList<>();

        for (final Order order : Database.getOrders()) {
            if (order.getTraderId() == customer.getId()) {
                customerOrders.add(order);
            }
        }
        final int numberOfCustomerOrders = customerOrders.size();

        final Object[][] data = new Object[numberOfCustomerOrders + 1][4];
        int indexArray = 0;
        double total = 0;

        for (final Order customerOrder : customerOrders) {
            data[indexArray][0] = ORDER_ID_FORMATTER.format(customerOrder.getOrderId());
            data[indexArray][1] = customerOrder.getOrderDate();
            data[indexArray][2] = customerOrder.getDeliveryDate();
            data[indexArray++][3] = "€" + CURRENCY_FORMATTER.format(customerOrder.getTotalSale());
            total += customerOrder.getTotalSale();
        }

        data[numberOfCustomerOrders][2] = "<html><b>Total Sales</b></html>";
        data[numberOfCustomerOrders][3] = "<html><b>€" + CURRENCY_FORMATTER.format(total) + "</b></html>";

        final JTable table = new JTable(data, ORDER_COLUMN_NAMES_FROM_CUSTOMER_TABLE);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        final JScrollPane scrollPane = new JScrollPane(table);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        final JButton backButton = new JButton("Back to Customers");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CustomerTable.createTableGui();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);

        GuiCreator.setFrame(false, false, true);
    }

    private static void viewSupplierInfo(final Supplier supplier) {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + supplier.getSupplierName());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        JLabel supplierLabel = new JLabel("Supplier");
        JLabel vatNumberLabel = new JLabel("VAT Number");
        JLabel phoneNoLabel = new JLabel("Phone Number");
        JLabel addressLabel = new JLabel("Address");
        JLabel dateAddedLabel = new JLabel("Date Added");
        JLabel titleLabel = new JLabel("Products supplied by " + supplier.getSupplierName());

        supplierLabel.setFont(new Font(supplierLabel.getFont().getFontName(), Font.BOLD, supplierLabel.getFont().getSize()));
        vatNumberLabel.setFont(new Font(vatNumberLabel.getFont().getFontName(), Font.BOLD, vatNumberLabel.getFont().getSize()));
        phoneNoLabel.setFont(new Font(phoneNoLabel.getFont().getFontName(), Font.BOLD, phoneNoLabel.getFont().getSize()));
        addressLabel.setFont(new Font(addressLabel.getFont().getFontName(), Font.BOLD, addressLabel.getFont().getSize()));
        dateAddedLabel.setFont(new Font(dateAddedLabel.getFont().getFontName(), Font.BOLD, dateAddedLabel.getFont().getSize()));
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));

        int textFieldSize = 20;

        JTextField supplierField = new JTextField(supplier.getSupplierName() + " (" + SUPPLIER_ID_FORMATTER.format(supplier.getId()) + ")",
                textFieldSize);
        supplierField.setEditable(false);
        JTextField vatNumberField = new JTextField(supplier.getVatNumber(), textFieldSize);
        vatNumberField.setEditable(false);

        JTextField phoneNoField = new JTextField(supplier.getPhoneNumber(), textFieldSize);
        phoneNoField.setEditable(false);
        JTextField addressField = new JTextField(supplier.getAddress(), textFieldSize);
        addressField.setEditable(false);
        JTextField dateAddedField = new JTextField(supplier.getDateAdded(), textFieldSize);
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

        for (final Product p : Database.getProducts()) {
            if (supplier.getId() == p.getSupplierId()) {
                count++;
            }
        }

        final String[] columnNames = { "Product Description", "Product ID", "Unit Cost Price", "Current Stock" };
        final Object[][] data = new Object[count][4];
        int indexArray = 0;

        for (int i = 0; i < Database.getProducts().size(); i++) {
            if (supplier.getId() == Database.getProducts().get(i).getSupplierId()) {
                data[indexArray][0] = Database.getProducts().get(i).getProductDescription();
                data[indexArray][1] = Database.getProducts().get(i).getProductId();
                data[indexArray][2] = "€" + CURRENCY_FORMATTER.format(Database.getProducts().get(i).getCostPrice());
                data[indexArray][3] = Database.getProducts().get(i).getStockLevel() + "/" + Database.getProducts().get(i).getMaxLevel();
                indexArray++;
            }
        }

        final JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        final JScrollPane scrollPane = new JScrollPane(table);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        final JButton backButton = new JButton("Back to Suppliers");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SupplierTable.createTableGui();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);

        // Update frame
        GuiCreator.setFrame(false, false, true);
    }

    private static void viewStaffInfo(final Staff staff) {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + staff.getStaffName());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        JPanel titlePanel = new JPanel(new GridBagLayout());
        JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        JPanel myPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        myPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final String level = staff.getStaffLevel() == 1 ? "Manager" : "Employee";

        JLabel staffLabel = new JLabel("Staff");
        JLabel levelLabel = new JLabel("Staff Level");
        JLabel wageLabel = new JLabel("Wage");
        JLabel phoneNoLabel = new JLabel("Phone Number");
        JLabel addressLabel = new JLabel("Address");
        JLabel dateAddedLabel = new JLabel("Date Added");
        JLabel titleLabel = new JLabel("Orders placed by " + staff.getStaffName());
        staffLabel.setFont(new Font(staffLabel.getFont().getFontName(), Font.BOLD, staffLabel.getFont().getSize()));
        levelLabel.setFont(new Font(levelLabel.getFont().getFontName(), Font.BOLD, levelLabel.getFont().getSize()));
        wageLabel.setFont(new Font(wageLabel.getFont().getFontName(), Font.BOLD, wageLabel.getFont().getSize()));
        phoneNoLabel.setFont(new Font(phoneNoLabel.getFont().getFontName(), Font.BOLD, phoneNoLabel.getFont().getSize()));
        addressLabel.setFont(new Font(addressLabel.getFont().getFontName(), Font.BOLD, addressLabel.getFont().getSize()));
        dateAddedLabel.setFont(new Font(dateAddedLabel.getFont().getFontName(), Font.BOLD, dateAddedLabel.getFont().getSize()));
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));

        int textFieldSize = 15;

        JTextField staffField = new JTextField(staff.getStaffName() + " (" + STAFF_ID_FORMATTER.format(staff.getId()) + ")", textFieldSize);
        staffField.setEditable(false);
        JTextField levelField = new JTextField(level, textFieldSize);
        levelField.setEditable(false);
        JTextField wageField = new JTextField("€" + CURRENCY_FORMATTER.format(staff.getWage()), textFieldSize);
        wageField.setEditable(false);

        JTextField phoneNoField = new JTextField(staff.getPhoneNumber(), textFieldSize);
        phoneNoField.setEditable(false);
        JTextField addressField = new JTextField(staff.getAddress(), textFieldSize);
        addressField.setEditable(false);
        JTextField dateAddedField = new JTextField(staff.getDateAdded(), textFieldSize);
        dateAddedField.setEditable(false);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(1, 10, 0, 5);
        titlePanel.add(staffLabel, g);
        g.gridx = 1;
        titlePanel.add(staffField, g);
        g.gridx = 2;
        titlePanel.add(levelLabel, g);
        g.gridx = 3;
        titlePanel.add(levelField, g);
        g.gridx = 4;
        titlePanel.add(wageLabel, g);
        g.gridx = 5;
        titlePanel.add(wageField, g);

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

        int numberOfCustomerOrders = 0, numberOfSupplierOrders = 0;
        int arrayIndex = 0;

        for (final Order order : Database.getOrders()) {
            if (order.getStaffId() == staff.getId()) {
                if (order.isSupplier()) {
                    numberOfSupplierOrders++;
                } else {
                    numberOfCustomerOrders++;
                }
            }
        }

        String[] columnNames = { "Order ID", "Trader", "Total Cost" };
        Object[][] data = new Object[numberOfCustomerOrders + 1][3];
        double total = 0;

        for (int i = 0; i < Database.getOrders().size(); i++) {

            if (!Database.getOrders().get(i).isSupplier() && Database.getOrders().get(i).getStaffId() == staff.getId()) {

                double totalOrderPrice = 0;
                for (int j = 0; j < Database.getOrders().get(i).getOrderedItems().size(); j++) {
                    totalOrderPrice += Database.getOrders().get(i).getOrderedItems().get(j).getQuantity()
                            * Database.getOrders().get(i).getOrderedItems().get(j).getProduct().getSalePrice();
                }

                String name = "";

                for (Entity c : Database.getCustomers()) {
                    if (Database.getOrders().get(i).getTraderId() == c.getId()) {
                        name = ((Customer) c).getCustomerName();
                    }
                }

                data[arrayIndex][0] = ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                data[arrayIndex][1] = name + " (" + Database.getOrders().get(i).getTraderId() + ")";
                data[arrayIndex][2] = "€" + CURRENCY_FORMATTER.format(totalOrderPrice);
                arrayIndex++;
                total += totalOrderPrice;
            }
        }
        data[numberOfCustomerOrders][1] = "<html><b>Customer Total</b></html>";
        data[numberOfCustomerOrders][2] = "<html><b>€" + CURRENCY_FORMATTER.format(total) + "</b></html>";

        JTable customerTable = new JTable(data, columnNames);
        customerTable.setFillsViewportHeight(true);
        customerTable.setEnabled(false);

        String[] columnNames2 = { "Order ID", "Trader", "Total Cost" };
        Object[][] data2 = new Object[numberOfSupplierOrders + 1][3];
        total = arrayIndex = 0;

        for (int i = 0; i < Database.getOrders().size(); i++) {

            if (Database.getOrders().get(i).isSupplier() && Database.getOrders().get(i).getStaffId() == staff.getId()) {

                double totalOrderPrice = 0;
                for (int j = 0; j < Database.getOrders().get(i).getOrderedItems().size(); j++) {
                    totalOrderPrice += Database.getOrders().get(i).getOrderedItems().get(j).getQuantity()
                            * Database.getOrders().get(i).getOrderedItems().get(j).getProduct().getCostPrice();
                }

                String name = "";

                for (Entity supp : Database.getSuppliers()) {
                    if (Database.getOrders().get(i).getTraderId() == supp.getId())
                        name = ((Supplier) supp).getSupplierName();
                }

                data2[arrayIndex][0] = ORDER_ID_FORMATTER.format(Database.getOrders().get(i).getOrderId());
                data2[arrayIndex][1] = name + " (" + Database.getOrders().get(i).getTraderId() + ")";
                data2[arrayIndex][2] = "€" + CURRENCY_FORMATTER.format(totalOrderPrice);
                arrayIndex++;
                total += totalOrderPrice;
            }
        }
        data2[numberOfSupplierOrders][1] = "<html><b>Supplier Total</b></html>";
        data2[numberOfSupplierOrders][2] = "<html><b>€" + CURRENCY_FORMATTER.format(total) + "</b></html>";

        JTable supplierTable = new JTable(data2, columnNames2);
        supplierTable.setFillsViewportHeight(true);
        supplierTable.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(customerTable);
        myPanel.add(scrollPane, BorderLayout.WEST);

        JScrollPane scrollPane2 = new JScrollPane(supplierTable);
        myPanel.add(scrollPane2, BorderLayout.EAST);

        JButton backButton = new JButton("Back to Staff Members");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StaffTable.createTableGui();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(myPanel, BorderLayout.NORTH);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);

        // Update frame
        GuiCreator.setFrame(false, false, true);
    }
}
