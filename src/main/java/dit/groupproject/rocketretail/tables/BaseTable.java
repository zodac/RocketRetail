package dit.groupproject.rocketretail.tables;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.TableState;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.utilities.DateHandler;

public abstract class BaseTable {

    protected final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,#00.00");
    protected final static DecimalFormat CUSTOMER_ID_FORMATTER = new DecimalFormat("00000");
    protected final static DecimalFormat ORDER_ID_FORMATTER = new DecimalFormat("0000");
    protected final static DecimalFormat PRODUCT_ID_FORMATTER = new DecimalFormat("00000");
    protected final static DecimalFormat STAFF_ID_FORMATTER = new DecimalFormat("000");
    protected final static DecimalFormat SUPPLIER_ID_FORMATTER = new DecimalFormat("0000");

    protected static int showDialog(final String title, final JPanel myPanel) {
        return JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
    }

    protected static Object[][] createTableData(final ArrayList<Entity> items) {
        final Object[][] data = new Object[items.size()][items.get(0).getNumberOfFields()];
        int dataIndex = 0;

        for (final Entity item : items) {
            data[dataIndex++] = item.getData();
        }
        return data;
    }

    protected static void setTableState(final TableState newState) {
        if (ShopDriver.getCurrentTableState() != newState) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
        }
        ShopDriver.setCurrentTable(newState);
    }

    /**
     * Checks set of input fields for invalid entries. Returns boolean
     * determined by validity of fields - true if not invalid entries, false if
     * any invalid entries and sets a red border around invalid field.<br />
     * Checks for:
     * <ul>
     * <li>JTextField (String) - checks for empty fields</li>
     * <li>JTextField (Integer) - checks for empty fields, or fields with
     * non-Integer inputs</li>
     * <li>JTextField (Double) - checks for empty fields, or fields with
     * non-Double inputs</li>
     * <li>JPasswordField - checks for empty fields, or field with any
     * non-numeric characters</li>
     * <li>JComboBox (Normal) - checks for empty selection</li>
     * <li>JComboBox (Date) - checks for empty selections, or invalid dates
     * (e.g., 29th February, 31st April, etc.)</li>
     * </ul>
     * 
     * @param textFields
     *            An ArrayList of JTextFields (String)
     * @param intFields
     *            An ArrayList of JTextFields (Integer)
     * @param doubleFields
     *            An ArrayList of JTextFields (Double)
     * @param pinFields
     *            An ArrayList of JPasswordFields
     * @param comboBoxes
     *            An ArrayList of JComboBoxes (Normal)
     * @param addedBoxes
     *            An ArrayList of JComboBoxes (Date)
     * @param lastPurchaseBoxes
     *            An ArrayList of JComboBoxes (Date)
     * 
     * @return a boolean set to true if no invalid entries, or else false
     */
    protected static boolean checkFields(final ArrayList<JTextField> textFields, ArrayList<JTextField> intFields, ArrayList<JTextField> doubleFields,
            ArrayList<JPasswordField> pinFields, ArrayList<JComboBox<String>> comboBoxes, ArrayList<JComboBox<String>> addedBoxes,
            ArrayList<JComboBox<String>> lastPurchaseBoxes) {

        boolean valid = true;
        final Border errorBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red);
        final Border validBorder = UIManager.getBorder("TextField.border");

        valid = validateTextFields(textFields, intFields, doubleFields, valid, errorBorder, validBorder);
        valid = validatePinField(pinFields, valid, errorBorder, validBorder);
        valid = validateComboBoxes(comboBoxes, valid, errorBorder, validBorder);
        valid = validateDateBoxes(addedBoxes, lastPurchaseBoxes, valid, errorBorder, validBorder);

        if (!valid) {
            GuiCreator.setConfirmationMessage("Invalid entry!");
        }

        return valid;
    }

    private static boolean validatePinField(ArrayList<JPasswordField> pinFields, boolean valid, final Border errorBorder, final Border validBorder) {
        if (pinFields != null) {
            for (JPasswordField password : pinFields) {
                String input = String.valueOf(password.getPassword());
                if (!input.matches("^\\d*$") || input.length() != 4) {
                    password.setBorder(errorBorder);
                    valid = false;
                } else
                    password.setBorder(validBorder);
            }
        }
        return valid;
    }

    private static boolean validateDateBoxes(ArrayList<JComboBox<String>> addedBoxes, ArrayList<JComboBox<String>> lastPurchaseBoxes, boolean valid,
            final Border errorBorder, final Border validBorder) {
        if (addedBoxes != null) {

            for (final JComboBox<String> addedBox : addedBoxes) {
                if (((String) addedBox.getSelectedItem()).isEmpty()) {
                    addedBox.setBorder(errorBorder);
                    valid = false;
                } else {
                    addedBox.setBorder(validBorder);
                }
            }

            int day = addedBoxes.get(0).getSelectedIndex();
            int month = addedBoxes.get(1).getSelectedIndex();
            int year = addedBoxes.get(2).getSelectedIndex() + (DateHandler.YEAR_START - 1);

            // If month is April, June, September or November, day cannot be 31
            // OR if month is February and is leap year, day cannot be 30 or 31
            // OR if February but not a leap year, day cannot be 29, 30 or 31

            if (isInvalidDate(day, month, year)) {
                addedBoxes.get(0).setBorder(errorBorder);
                valid = false;
            }
        }

        if (lastPurchaseBoxes != null) {

            for (final JComboBox<String> lastPurchaseBox : lastPurchaseBoxes) {
                if (((String) lastPurchaseBox.getSelectedItem()).length() == 0) {
                    lastPurchaseBox.setBorder(errorBorder);
                    valid = false;
                } else {
                    lastPurchaseBox.setBorder(validBorder);
                }
            }

            int day = lastPurchaseBoxes.get(0).getSelectedIndex();
            int month = lastPurchaseBoxes.get(1).getSelectedIndex();
            int year = lastPurchaseBoxes.get(2).getSelectedIndex() + (DateHandler.YEAR_START - 1);

            // If month is April, June, September or November, day cannot be 31
            // OR if month is February and is leap year, day cannot be 30 or 31
            // OR if February but not a leap year, day cannot be 29, 30 or 31

            if (isInvalidDate(day, month, year)) {
                lastPurchaseBoxes.get(0).setBorder(errorBorder);
                valid = false;
            }
        }
        return valid;
    }

    private static boolean validateComboBoxes(ArrayList<JComboBox<String>> comboBoxes, boolean valid, final Border errorBorder,
            final Border validBorder) {
        if (comboBoxes != null) {
            for (final JComboBox<String> stringBox : comboBoxes) {
                if (((String) stringBox.getSelectedItem()).length() == 0) {
                    stringBox.setBorder(errorBorder);
                    valid = false;
                } else {
                    stringBox.setBorder(validBorder);
                }
            }
        }
        return valid;
    }

    private static boolean validateTextFields(final ArrayList<JTextField> textFields, ArrayList<JTextField> intFields,
            ArrayList<JTextField> doubleFields, boolean valid, final Border errorBorder, final Border validBorder) {
        if (textFields.size() > 0) {
            for (JTextField text : textFields) {
                if (text.getText().isEmpty()) {
                    // matching label = red
                    text.setBorder(errorBorder);
                    valid = false;
                } else
                    text.setBorder(validBorder);
            }
        }

        if (intFields != null) {
            for (final JTextField intField : intFields) {
                try {
                    Integer.parseInt(intField.getText());
                    intField.setBorder(validBorder);
                } catch (NumberFormatException e) {
                    intField.setBorder(errorBorder);
                    valid = false;
                }
            }
        }

        if (doubleFields != null) {
            for (final JTextField doubleField : doubleFields) {
                try {
                    Double.parseDouble(doubleField.getText());
                    doubleField.setBorder(validBorder);
                } catch (NumberFormatException e) {
                    doubleField.setBorder(errorBorder);
                    valid = false;
                }
            }
        }
        return valid;
    }

    private static boolean isInvalidDate(int day, int month, int year) {
        return (day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) || (day == 30 && month == 2)
                || (day == 29 && month == 2 && year % 4 != 0);
    }
}
