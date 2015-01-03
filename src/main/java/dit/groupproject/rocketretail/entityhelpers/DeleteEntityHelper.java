package dit.groupproject.rocketretail.entityhelpers;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.tables.CustomerTable;

public class DeleteEntityHelper {
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
