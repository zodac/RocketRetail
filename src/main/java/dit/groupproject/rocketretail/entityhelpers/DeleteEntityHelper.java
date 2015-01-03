package dit.groupproject.rocketretail.entityhelpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;

public class DeleteEntityHelper extends EntityHelper {

    /**
     * Returns an ActionListener which adds an Entity delete panel to the left
     * side of the GUI screen.
     * <p>
     * Checks the current table state of the system to determine which Entity
     * table to use.
     */
    public static ActionListener deleteEntityPanel(final JComboBox<String> deleteBox) {
        final TableState currentState = ShopDriver.getCurrentTableState();

        if (currentState == TableState.CUSTOMER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DeleteEntityHelper.deleteCustomer(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 9)),
                            ((String) deleteBox.getSelectedItem()).substring(11, ((String) deleteBox.getSelectedItem()).length() - 1));
                }
            };
        } else if (currentState == TableState.ORDER) {

        } else if (currentState == TableState.PRODUCT) {

        } else if (currentState == TableState.STAFF) {

        } else if (currentState == TableState.SUPPLIER) {

        }
        throw new IllegalArgumentException("No panel available for current table state [" + currentState.toString() + "]!");
    }

    /**
     * This method shows a dialog box asking a user if they want to delete a
     * <code>Customer</code>. This method also has logic to remove the
     * <code>Customer</code> from system records.
     */
    public static void deleteCustomer(final int customerId, final String customerName) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + customerName + "?"));

        int indexToRemove = -1;

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            final Entity customer = Database.getCustomerById(customerId);
            indexToRemove = Database.getIndexOfCustomer(customer);
        }

        if (indexToRemove != -1) {
            GuiCreator.setConfirmationMessage(customerName + " deleted");
            Database.removeCustomerByIndex(indexToRemove);
        }

        CustomerTable.createTable();
        GuiCreator.frame.validate();
    }

    private static int showDialog(final String title, final JPanel myPanel) {
        return JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
    }
}
