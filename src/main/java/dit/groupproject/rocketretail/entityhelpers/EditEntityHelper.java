package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.utilities.DateHandler.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

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
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.tables.CustomerTable;

public class EditEntityHelper extends EntityHelper {

    /**
     * This method builds a form for updating a <code>Customer</code>'s details.
     * It also has the logic to update a <code>Customer</code>'s details in the
     * <code>ArrayList</code> of customers.
     */
    public static void editCustomerPanel(final int customerId) {

        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        final Customer customer = (Customer) Database.getCustomerById(customerId);
        final int index = Database.getIndexOfCustomer(customer);

        final JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        GridBagConstraints g = new GridBagConstraints();

        g.gridx = 0;
        g.gridy = 0;
        innerPanel.add(new JLabel("Customer ID:"), g);
        g.gridy = 1;
        innerPanel.add(new JLabel("Customer Name:"), g);
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

        g.insets = new Insets(1, 10, 0, 5);

        g.gridx = 1;
        g.gridy = 0;
        g.gridwidth = 3;
        final JTextField custIdField = new JTextField(null, 20);
        custIdField.setEditable(false);
        innerPanel.add(custIdField, g);
        g.gridy = 1;
        g.gridwidth = 3;
        final JTextField custNameField = new JTextField(null, 20);
        innerPanel.add(custNameField, g);
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
        final JComboBox<String> lastPurchaseDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);

        g.gridy = 5;
        g.gridx = 2;
        g.gridwidth = 1 - 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);

        g.gridy = 5;
        g.gridx = 3;
        g.gridwidth = 2 - 3;

        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 6;
        g.gridx = 1;
        g.gridwidth = 1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(dateAddedDay, g);

        g.gridy = 6;
        g.gridx = 2;
        g.gridwidth = 1 - 2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(dateAddedMonth, g);

        g.gridy = 6;
        g.gridx = 3;
        g.gridwidth = 2 - 3;

        final JComboBox<String> dateAddedYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(dateAddedYear, g);

        custIdField.setText("" + customer.getId());
        custNameField.setText(customer.getCustomerName());
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
        g.gridy = 8;
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
            public void actionPerformed(final ActionEvent e) {

                final ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                textFields.add(custNameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                textFields.add(vatNoField);
                final ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                final ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<JComboBox<String>>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (FieldValidator.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    final Customer editedCustomer = new Customer(custNameField.getText(), phoneNoField.getText(), addressField.getText(), vatNoField
                            .getText(), lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/"
                            + lastPurchaseYear.getSelectedItem(), dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/"
                            + dateAddedYear.getSelectedItem());
                    editedCustomer.setId(index + IdManager.CUSTOMER_ID_START);
                    Database.addCustomerByIndex(index, editedCustomer);

                    GuiCreator.setConfirmationMessage("Customer " + custNameField.getText() + "'s details editted");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();
                    Database.removeCustomerByIndex(index + 1);
                    CustomerTable.createTable();
                }
            }
        });

        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.leftPanel);
                GuiCreator.frame.repaint();
                GuiCreator.frame.validate();
            }
        });

        GuiCreator.leftPanel.add(innerPanel);

        GuiCreator.setFrame(true, false, false);
    }

}
