package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.utilities.DateHandler.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.DAY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.DateHandler.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.MONTH_FORMATTER;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_FORMATTER;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

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
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.gui.FieldValidator;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.tables.CustomerTable;

public class AddEntityHelper extends EntityHelper {

    /**
     * This method builds a form for adding a <code>Customer</code>. It also has
     * the logic to add a <code>Customer</code> to an <code>ArrayList</code> of
     * customers.
     */
    public static void addCustomerPanel() {
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();

        JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        innerPanel.add(new JLabel("Customer Name:"), g);
        g.gridy = 1;
        innerPanel.add(new JLabel("Phone Number:"), g);
        g.gridy = 2;
        innerPanel.add(new JLabel("Address:"), g);
        g.gridy = 3;
        innerPanel.add(new JLabel("VAT Number:"), g);
        g.gridy = 4;
        innerPanel.add(new JLabel("Last Purchase:"), g);
        g.gridy = 5;
        innerPanel.add(new JLabel("Date Added:"), g);

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
        final JComboBox<String> lastPurchaseDay = new JComboBox<String>(DAYS_AS_NUMBERS);
        innerPanel.add(lastPurchaseDay, g);
        g.gridx = 2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(MONTHS_AS_NUMBERS);
        innerPanel.add(lastPurchaseMonth, g);
        g.gridx = 3;
        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(YEARS_AS_NUMBERS);
        innerPanel.add(lastPurchaseYear, g);

        g.gridy = 5;
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
        g.gridy = 6;
        innerPanel.add(new JLabel(" "), g);
        g.gridx = 1;
        innerPanel.add(new JLabel(" "), g);

        JButton save = new JButton("Save");
        save.setLayout(new GridBagLayout());
        g = new GridBagConstraints();
        g.insets = new Insets(1, 0, 0, 0);
        g.gridx = 1;
        g.gridy = 7;
        innerPanel.add(save, g);
        JButton cancel = new JButton("Cancel");
        g.gridx = 3;
        innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                textFields.add(custNameField);
                textFields.add(phoneNoField);
                textFields.add(addressField);
                textFields.add(vatNoField);
                ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
                addedBoxes.add(dateAddedDay);
                addedBoxes.add(dateAddedMonth);
                addedBoxes.add(dateAddedYear);
                ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<JComboBox<String>>();
                lastPurchaseBoxes.add(lastPurchaseDay);
                lastPurchaseBoxes.add(lastPurchaseMonth);
                lastPurchaseBoxes.add(lastPurchaseYear);

                if (FieldValidator.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes)) {
                    Database.addCustomer(new Customer(custNameField.getText(), phoneNoField.getText(), addressField.getText(), vatNoField.getText(),
                            lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/" + lastPurchaseYear.getSelectedItem(),
                            dateAddedDay.getSelectedItem() + "/" + dateAddedMonth.getSelectedItem() + "/" + dateAddedYear.getSelectedItem()));

                    GuiCreator.setConfirmationMessage("Customer " + custNameField.getText() + " added");
                    GuiCreator.frame.remove(GuiCreator.leftPanel);
                    GuiCreator.frame.repaint();
                    GuiCreator.frame.validate();

                    CustomerTable.descendingOrderSort = false;
                    CustomerTable.sortItems();
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
