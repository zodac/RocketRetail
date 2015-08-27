package dit.groupproject.rocketretail.entityhelpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

public class DeleteEntityHelper extends BaseEntityHelper {

    /**
     * Returns an ActionListener which adds an Entity delete panel to the left
     * side of the GUI screen.
     * <p>
     * Checks the current table state of the system to determine which Entity
     * table to use.
     */
    public ActionListener deleteEntityPanel(final JComboBox<String> deleteBox) {
        final TableState currentState = ShopDriver.getCurrentTableState();

        if (currentState == TableState.CUSTOMER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final int customerId = Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 10));
                    final String customerName = ((String) deleteBox.getSelectedItem()).substring(12,
                            ((String) deleteBox.getSelectedItem()).length() - 1);

                    deleteCustomer(customerId, customerName);
                }
            };
        } else if (currentState == TableState.ORDER) {

        } else if (currentState == TableState.PRODUCT) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final int productId = Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 10));
                    final String productName = ((String) deleteBox.getSelectedItem()).substring(12,
                            ((String) deleteBox.getSelectedItem()).length() - 1);

                    deleteProduct(productId, productName);
                }
            };
        } else if (currentState == TableState.STAFF) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final int staffId = Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 10));
                    final String staffName = ((String) deleteBox.getSelectedItem())
                            .substring(12, ((String) deleteBox.getSelectedItem()).length() - 1);

                    if (staffId == ShopDriver.getCurrentStaff().getId()) {
                        JOptionPane.showMessageDialog(null, "You can't delete yourself!", "Deletion Error", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        deleteStaff(staffId, staffName);
                    }
                }
            };
        } else if (currentState == TableState.SUPPLIER) {
            return new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    final int supplierId = Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 10));
                    final String supplierName = ((String) deleteBox.getSelectedItem()).substring(12,
                            ((String) deleteBox.getSelectedItem()).length() - 1);

                    deleteSupplier(supplierId, supplierName);
                }
            };
        }
        throw new IllegalArgumentException("No panel available for current table state [" + currentState.toString() + "]!");
    }

    private void deleteCustomer(final int customerId, final String customerName) {
        if (GuiCreator.getConfirmationResponse("Do you want to delete " + customerName + "?")) {
            resetLeftPanel();
            Database.removeCustomerById(customerId);
        }
        customerTable.createTableGui();
    }

    private void deleteSupplier(final int supplierId, final String supplierName) {
        if (GuiCreator.getConfirmationResponse("Do you want to delete " + supplierName + "?")) {
            resetLeftPanel();
            Database.removeSupplierById(supplierId);
        }
        supplierTable.createTableGui();
    }

    private void deleteStaff(final int staffId, final String staffName) {
        if (GuiCreator.getConfirmationResponse("Do you want to delete " + staffName + "?")) {
            resetLeftPanel();
            Database.removeStaffById(staffId);
        }
        staffTable.createTableGui();
    }

    private void deleteProduct(final int productId, final String productName) {
        if (GuiCreator.getConfirmationResponse("Do you want to delete " + productName + "?")) {
            resetLeftPanel();
            Database.removeProductById(productId);
        }
        productTable.createTableGui();
    }
}
