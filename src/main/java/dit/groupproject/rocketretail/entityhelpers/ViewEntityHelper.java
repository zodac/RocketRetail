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
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;

public class ViewEntityHelper extends EntityHelper {

    private final static String[] ORDER_COLUMN_NAMES_FROM_CUSTOMER_TABLE = { "Order ID", "Order Date", "Delivery Date", "Total Cost" };

    /**
     * Returns a MouseAdapter which opens a subtable with additonal Entity
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
                        ViewEntityHelper.viewCustomerInfo(customer);
                    }
                }
            };
        } else if (currentState == TableState.ORDER) {

        } else if (currentState == TableState.PRODUCT) {

        } else if (currentState == TableState.STAFF) {

        } else if (currentState == TableState.SUPPLIER) {

        }
        throw new IllegalArgumentException("No sub-table available for current table state [" + currentState.toString() + "]!");
    }

    private static void viewCustomerInfo(Customer customer) {
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
                CustomerTable.createTable();
            }
        });

        buttonPanel.add(backButton);
        innerPanel.add(buttonPanel, BorderLayout.SOUTH);

        GuiCreator.mainPanel.add(titlePanel, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(innerPanel, BorderLayout.CENTER);

        GuiCreator.setFrame(false, false, true);
    }
}
