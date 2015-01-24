package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.gui.GuiCreator.BOLD_FONT_HTML_FORMATTING_TAGS_WITH_COLOUR;
import static dit.groupproject.rocketretail.gui.GuiCreator.BOLD_HTML_FORMATTING_TAGS;
import static dit.groupproject.rocketretail.gui.GuiCreator.BOLD_LABEL_FONT;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_CURRENT;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;
import static dit.groupproject.rocketretail.utilities.Formatters.CURRENCY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.Graphs;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.OrderTable;
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;

public class ViewEntityHelper extends AbstractEntityHelper {

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
            return new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (entitySubTable.getSelectedRow() >= 0) {
                        final int selectedId = Integer.parseInt((String) entitySubTable.getValueAt(entitySubTable.getSelectedRow(), 0));
                        final Order order = (Order) Database.getOrderById(selectedId);

                        viewOrderInfo(order);
                    }
                }
            };
        } else if (currentState == TableState.PRODUCT) {
            return new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (entitySubTable.getSelectedRow() >= 0) {
                        final int selectedId = Integer.parseInt((String) entitySubTable.getValueAt(entitySubTable.getSelectedRow(), 0));
                        final Product product = (Product) Database.getProductById(selectedId);

                        viewProductInfo(product);
                    }
                }
            };
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
        final String customerName = customer.getName();

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

        customerLabel.setFont(BOLD_LABEL_FONT);
        vatNumberLabel.setFont(BOLD_LABEL_FONT);
        phoneNoLabel.setFont(BOLD_LABEL_FONT);
        addressLabel.setFont(BOLD_LABEL_FONT);
        dateAddedLabel.setFont(BOLD_LABEL_FONT);
        titleLabel.setFont(BOLD_LABEL_FONT);

        final int textFieldSize = 20;

        final JTextField customerField = new JTextField(customerName + " (" + ID_FORMATTER.format(customer.getId()) + ")", textFieldSize);
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

        ArrayList<Entity> customerOrders = Database.getAllOrdersByCustomerId(customer.getId());
        final int numberOfCustomerOrders = customerOrders.size();

        final Object[][] data = new Object[numberOfCustomerOrders + 1][4];
        int indexArray = 0;
        double total = 0;

        for (final Entity cOrder : customerOrders) {
            final Order customerOrder = (Order) cOrder;
            final double totalSale = customerOrder.getTotalPrice();

            data[indexArray][0] = ID_FORMATTER.format(customerOrder.getId());
            data[indexArray][1] = customerOrder.getOrderDate();
            data[indexArray][2] = customerOrder.getDeliveryDate();
            data[indexArray++][3] = "€" + CURRENCY_FORMATTER.format(totalSale);
            total += totalSale;
        }

        data[numberOfCustomerOrders][2] = String.format(BOLD_HTML_FORMATTING_TAGS, "Total Sales");
        data[numberOfCustomerOrders][3] = String.format(BOLD_HTML_FORMATTING_TAGS, "€" + CURRENCY_FORMATTER.format(total));

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
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + supplier.getName());
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
        JLabel titleLabel = new JLabel("Products supplied by " + supplier.getName());

        supplierLabel.setFont(BOLD_LABEL_FONT);
        vatNumberLabel.setFont(BOLD_LABEL_FONT);
        phoneNoLabel.setFont(BOLD_LABEL_FONT);
        addressLabel.setFont(BOLD_LABEL_FONT);
        dateAddedLabel.setFont(BOLD_LABEL_FONT);
        titleLabel.setFont(BOLD_LABEL_FONT);

        final int textFieldSize = 20;

        JTextField supplierField = new JTextField(supplier.getName() + " (" + ID_FORMATTER.format(supplier.getId()) + ")", textFieldSize);
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

        int numberOfSupplierOrders = 0;

        for (final Entity p : Database.getProducts()) {
            if (supplier.getId() == ((Product) p).getSupplierId()) {
                numberOfSupplierOrders++;
            }
        }

        final String[] columnNames = { "Product Description", "Product ID", "Unit Cost Price", "Current Stock" };
        final Object[][] data = new Object[numberOfSupplierOrders][4];
        int indexArray = 0;

        for (final Entity p : Database.getProducts()) {
            final Product product = (Product) p;

            if (supplier.getId() == product.getSupplierId()) {
                data[indexArray][0] = product.getName();
                data[indexArray][1] = product.getId();
                data[indexArray][2] = "€" + CURRENCY_FORMATTER.format(product.getCostPrice());
                data[indexArray][3] = product.getStockLevel() + "/" + product.getMaxLevel();
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
        GuiCreator.setFrame(false, false, true);
    }

    private static void viewStaffInfo(final Staff staff) {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + staff.getName());
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
        JLabel titleLabel = new JLabel("Orders placed by " + staff.getName());
        staffLabel.setFont(new Font(staffLabel.getFont().getFontName(), Font.BOLD, staffLabel.getFont().getSize()));
        levelLabel.setFont(new Font(levelLabel.getFont().getFontName(), Font.BOLD, levelLabel.getFont().getSize()));
        wageLabel.setFont(new Font(wageLabel.getFont().getFontName(), Font.BOLD, wageLabel.getFont().getSize()));
        phoneNoLabel.setFont(new Font(phoneNoLabel.getFont().getFontName(), Font.BOLD, phoneNoLabel.getFont().getSize()));
        addressLabel.setFont(new Font(addressLabel.getFont().getFontName(), Font.BOLD, addressLabel.getFont().getSize()));
        dateAddedLabel.setFont(new Font(dateAddedLabel.getFont().getFontName(), Font.BOLD, dateAddedLabel.getFont().getSize()));
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));

        final int textFieldSize = 15;

        JTextField staffField = new JTextField(staff.getName() + " (" + ID_FORMATTER.format(staff.getId()) + ")", textFieldSize);
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

        for (final Entity o : Database.getOrders()) {
            final Order order = (Order) o;

            if (order.getStaffId() == staff.getId()) {
                if (order.isSupplier()) {
                    numberOfSupplierOrders++;
                } else {
                    numberOfCustomerOrders++;
                }
            }
        }

        String[] columnNames = { "Order ID", "Trader", "Total Cost" };
        Object[][] customerOrderdata = new Object[numberOfCustomerOrders + 1][3];
        double totalSaleValue = 0;

        for (final Entity o : Database.getOrders()) {
            final Order order = (Order) o;

            if (!order.isSupplier() && order.getStaffId() == staff.getId()) {
                double totalSalePrice = 0;

                for (final OrderedItem orderedItem : order.getOrderedItems()) {
                    totalSalePrice += orderedItem.getQuantity() * orderedItem.getProduct().getSalePrice();
                }

                final int customerId = order.getTraderId();
                final Customer customer = (Customer) Database.getCustomerById(customerId);
                final String customerName = customer.getName();

                customerOrderdata[arrayIndex][0] = ID_FORMATTER.format(order.getId());
                customerOrderdata[arrayIndex][1] = customerName + " (" + ID_FORMATTER.format(customerId) + ")";
                customerOrderdata[arrayIndex++][2] = "€" + CURRENCY_FORMATTER.format(totalSalePrice);
                totalSalePrice += totalSalePrice;
            }
        }

        customerOrderdata[numberOfCustomerOrders][1] = String.format(BOLD_HTML_FORMATTING_TAGS, "Customer Total");
        customerOrderdata[numberOfCustomerOrders][2] = String.format(BOLD_HTML_FORMATTING_TAGS, "€" + CURRENCY_FORMATTER.format(totalSaleValue));

        JTable customerTable = new JTable(customerOrderdata, columnNames);
        customerTable.setFillsViewportHeight(true);
        customerTable.setEnabled(false);

        String[] columnNames2 = { "Order ID", "Trader", "Total Cost" };
        Object[][] supplierOrderData = new Object[numberOfSupplierOrders + 1][3];
        double totalCostValue = 0;
        arrayIndex = 0;

        for (final Entity o : Database.getOrders()) {
            final Order order = (Order) o;

            if (order.isSupplier() && order.getStaffId() == staff.getId()) {
                double totalCostPrice = 0;

                for (final OrderedItem orderedItem : order.getOrderedItems()) {
                    totalCostPrice += orderedItem.getQuantity() * orderedItem.getProduct().getCostPrice();
                }

                final int supplierId = order.getTraderId();
                final Supplier supplier = (Supplier) Database.getSupplierById(supplierId);
                final String supplierName = supplier.getName();

                supplierOrderData[arrayIndex][0] = ID_FORMATTER.format(order.getId());
                supplierOrderData[arrayIndex][1] = supplierName + " (" + ID_FORMATTER.format(supplierId) + ")";
                supplierOrderData[arrayIndex++][2] = "€" + CURRENCY_FORMATTER.format(totalCostPrice);
                totalCostValue += totalCostPrice;
            }
        }

        supplierOrderData[numberOfSupplierOrders][1] = String.format(BOLD_HTML_FORMATTING_TAGS, "Supplier Total");
        supplierOrderData[numberOfSupplierOrders][2] = String.format(BOLD_HTML_FORMATTING_TAGS, "€" + CURRENCY_FORMATTER.format(totalCostValue));

        JTable supplierTable = new JTable(supplierOrderData, columnNames2);
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
        GuiCreator.setFrame(false, false, true);
    }

    public static void viewProductInfo(final Product product) {
        final String productName = product.getName();

        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + productName);
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        final JPanel titlePanel = new JPanel(new GridBagLayout());
        final JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        final JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final JLabel productLabel = new JLabel("Product");
        final JLabel stockLabel = new JLabel("Stock Level");
        final JLabel supplierLabel = new JLabel("Supplier");
        final JLabel costLabel = new JLabel("Cost Price");
        final JLabel saleLabel = new JLabel("Sale Price");
        final JLabel profitLabel = new JLabel("Profit Per Unit");

        productLabel.setFont(BOLD_LABEL_FONT);
        stockLabel.setFont(BOLD_LABEL_FONT);
        supplierLabel.setFont(BOLD_LABEL_FONT);
        costLabel.setFont(BOLD_LABEL_FONT);
        saleLabel.setFont(BOLD_LABEL_FONT);
        profitLabel.setFont(BOLD_LABEL_FONT);

        final int textFieldSize = 20;

        final Entity productSupplier = Database.getSupplierById(product.getSupplierId());
        final String supplier = productSupplier.getName() + " (" + productSupplier.getId() + ")";

        final JTextField productField = new JTextField(productName + " (" + ID_FORMATTER.format(product.getId()) + ")", textFieldSize);
        productField.setEditable(false);
        final JTextField stockField = new JTextField(product.getStockLevel() + "/" + product.getMaxLevel(), textFieldSize);
        stockField.setEditable(false);
        final JTextField supplierField = new JTextField(supplier, textFieldSize);
        supplierField.setEditable(false);

        final JTextField costField = new JTextField("€" + CURRENCY_FORMATTER.format(product.getCostPrice()), textFieldSize);
        costField.setEditable(false);
        final JTextField saleField = new JTextField("€" + CURRENCY_FORMATTER.format(product.getSalePrice()), textFieldSize);
        saleField.setEditable(false);
        final JTextField profitField = new JTextField("€" + CURRENCY_FORMATTER.format(product.getSalePrice() - product.getCostPrice()), textFieldSize);
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

        final JPanel myPanel = new JPanel(new BorderLayout());
        final int numberOfYears = YEAR_CURRENT - YEAR_START;

        double[] yearData = new double[numberOfYears + 1];
        double[] productData = new double[numberOfYears + 1];

        String[] columnNames = { "Year", "Total Qty Bought", "Total Qty Sold", "Year Change" };
        Object[][] data = new Object[numberOfYears + 2][4];

        for (int i = 0; i < numberOfYears + 2; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = 0;
            }
        }

        int productLevel = product.getStartLevel();
        data[0][2] = String.format(BOLD_HTML_FORMATTING_TAGS, "Start Level");
        data[0][3] = String.format(BOLD_HTML_FORMATTING_TAGS, productLevel);

        for (int i = 0; i < numberOfYears + 1; i++) {
            for (Entity o : Database.getOrders()) {
                final Order order = (Order) o;

                if (Integer.parseInt(order.getOrderDate().substring(6, 10)) == i + YEAR_START) {
                    for (final OrderedItem oi : order.getOrderedItems()) {
                        if (oi.getProduct().getId() == product.getId() && order.isSupplier() && !order.isActive()) {
                            data[i + 1][1] = (int) data[i + 1][1] + oi.getQuantity();
                        } else if (oi.getProduct().getId() == product.getId() && !order.isSupplier()) {
                            data[i + 1][2] = (int) data[i + 1][2] + oi.getQuantity();
                        }
                    }

                }
                data[i + 1][0] = (i + YEAR_START);
                final int productQuantityDelta = (int) data[i + 1][1] - (int) data[i + 1][2];
                final String productQuantityDeltaColour = productQuantityDelta >= 0 ? "blue" : "red";

                data[i + 1][3] = String.format(BOLD_FONT_HTML_FORMATTING_TAGS_WITH_COLOUR, productQuantityDeltaColour, productQuantityDelta);
            }

            yearData[i] = i + YEAR_START;
            productLevel += (int) data[i + 1][1] - (int) data[i + 1][2];
            productData[i] = productLevel;
        }

        final JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        final JScrollPane scrollPane = new JScrollPane(table);
        myPanel.add(scrollPane, BorderLayout.WEST);

        double[][] inputdata = { yearData, productData };
        final ChartPanel chartPanel = Graphs.createLineChart("Past Stock levels", productName, inputdata);
        chartPanel.setPreferredSize(new Dimension(500, 750));

        myPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        myPanel.add(chartPanel, BorderLayout.CENTER);

        innerPanel.add(myPanel, BorderLayout.CENTER);

        final JButton orderButton = new JButton("Order " + productName);
        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrderTable.createSupplierOrder(product.getSupplierId());
            }
        });

        final JButton backButton = new JButton("Back to Products");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProductTable.createTableGui();
            }
        });

        buttonPanel.add(orderButton);
        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);
        GuiCreator.setFrame(false, false, true);
    }

    private static void viewOrderInfo(final Order order) {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - Order #" + ID_FORMATTER.format(order.getId()));
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));

        final JPanel titlePanel = new JPanel(new GridBagLayout());
        final JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
        final JPanel buttonPanel = new JPanel();
        titlePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final boolean isSupplier = order.isSupplier();
        final String traderTitle = isSupplier ? "Supplier" : "Customer";
        final Staff staff = (Staff) Database.getStaffMemberById(order.getStaffId());
        final String staffName = staff.getName();
        String traderName = "";

        if (isSupplier) {
            final Entity supplier = Database.getSupplierById(order.getTraderId());
            traderName = supplier.getName();
        } else {
            final Entity customer = Database.getCustomerById(order.getTraderId());
            traderName = customer.getName();
        }

        final JLabel orderLabel = new JLabel("Order ID");
        final JLabel staffLabel = new JLabel("Staff");
        final JLabel traderLabel = new JLabel(traderTitle);
        final JLabel orderDateLabel = new JLabel("Order Date");
        final JLabel deliveryDateLabel = new JLabel("Delivery Date");
        final JLabel titleLabel = new JLabel("Ordered Items");

        orderLabel.setFont(BOLD_LABEL_FONT);
        staffLabel.setFont(BOLD_LABEL_FONT);
        traderLabel.setFont(BOLD_LABEL_FONT);
        orderDateLabel.setFont(BOLD_LABEL_FONT);
        deliveryDateLabel.setFont(BOLD_LABEL_FONT);
        titleLabel.setFont(BOLD_LABEL_FONT);

        final int textFieldSize = 20;

        JTextField orderField = new JTextField(ID_FORMATTER.format(order.getId()), textFieldSize);
        orderField.setEditable(false);
        JTextField staffField = new JTextField(staffName + " (" + ID_FORMATTER.format(order.getStaffId()) + ")", textFieldSize);
        staffField.setEditable(false);
        JTextField traderField = new JTextField(traderName + " (" + order.getTraderId() + ")", textFieldSize);
        traderField.setEditable(false);
        JTextField orderDateField = new JTextField(order.getOrderDate(), textFieldSize);
        orderDateField.setEditable(false);
        JTextField deliveryDateField = new JTextField(order.getDeliveryDate(), textFieldSize);
        deliveryDateField.setEditable(false);

        final GridBagConstraints g = new GridBagConstraints();
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

        if (StringUtils.isNotBlank(order.getDeliveryDate())) {
            g.gridx = 4;
            titlePanel.add(deliveryDateLabel, g);
            g.gridx = 5;
            titlePanel.add(deliveryDateField, g);
        }

        g.gridy = 2;
        g.gridx = 2;
        g.gridwidth = 2;
        titlePanel.add(titleLabel, g);

        final String[] columnNames = { "Ordered Product", "Unit Price", "Order Quantity", "Product Total" };
        final ArrayList<OrderedItem> orderedItems = order.getOrderedItems();
        final int numberOfOrderedItems = orderedItems.size();

        final Object[][] data = new Object[numberOfOrderedItems + 1][4];
        double totalOfAllOrders = 0;
        int orderedItemIndex = 0;

        for (final OrderedItem orderedItem : orderedItems) {
            final Product orderedProduct = orderedItem.getProduct();
            final double unitPrice = isSupplier ? orderedProduct.getCostPrice() : orderedProduct.getSalePrice();
            final double totalPrice = unitPrice * orderedItem.getQuantity();

            data[orderedItemIndex][0] = orderedProduct.getName() + " (" + orderedProduct.getId() + ")";
            data[orderedItemIndex][1] = "€" + CURRENCY_FORMATTER.format(unitPrice);
            data[orderedItemIndex][2] = orderedItem.getQuantity();
            data[orderedItemIndex++][3] = "€" + CURRENCY_FORMATTER.format(totalPrice);
            totalOfAllOrders += totalPrice;
        }
        data[numberOfOrderedItems][2] = String.format(BOLD_HTML_FORMATTING_TAGS, "Order Total");
        data[numberOfOrderedItems][3] = String.format(BOLD_HTML_FORMATTING_TAGS, "€" + CURRENCY_FORMATTER.format(totalOfAllOrders));

        final JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        final JScrollPane scrollPane = new JScrollPane(table);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        final JButton backButton = new JButton("Back to Orders");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrderTable.createTableGui();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);
        GuiCreator.setFrame(false, false, true);
    }
}
