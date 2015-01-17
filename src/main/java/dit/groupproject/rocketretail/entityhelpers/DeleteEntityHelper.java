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
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;

public class DeleteEntityHelper {

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

    private static void deleteCustomer(final int customerId, final String customerName) {
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
            Database.removeCustomerByIndex(indexToRemove);
            GuiCreator.setConfirmationMessage(customerName + " deleted");
        }

        CustomerTable.createTableGui();
        GuiCreator.frame.validate();
    }

    private static void deleteSupplier(final int supplierId, final String supplierName) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + supplierName + "?"));

        int indexToRemove = -1;

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            final Entity supplier = Database.getSupplierById(supplierId);
            indexToRemove = Database.getIndexOfSupplier(supplier);
        }

        if (indexToRemove != -1) {
            Database.removeSupplierByIndex(indexToRemove);
            GuiCreator.setConfirmationMessage(supplierName + " deleted");

        }

        SupplierTable.createTableGui();
        GuiCreator.frame.validate();
    }

    private static void deleteStaff(final int staffId, final String staffName) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + staffName + "?"));

        int indexToRemove = -1;

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            final Entity staff = Database.getStaffMemberById(staffId);
            indexToRemove = Database.getIndexOfStaff(staff);
        }

        if (indexToRemove != -1) {
            Database.removeStaffByIndex(indexToRemove);
            GuiCreator.setConfirmationMessage(staffName + " deleted");
        }

        StaffTable.createTableGui();
        GuiCreator.frame.validate();
    }

    private static void deleteProduct(final int productId, final String productName) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Do you want to delete " + productName + "?"));

        int indexToRemove = -1;

        if (showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
            GuiCreator.frame.repaint();
            GuiCreator.leftPanel = new JPanel();

            final Entity product = Database.getProductById(productId);
            indexToRemove = Database.getIndexOfProduct(product);
        }

        if (indexToRemove != -1) {
            Database.removeProductByIndex(indexToRemove);
            GuiCreator.setConfirmationMessage(productName + " deleted");
        }

        ProductTable.createTableGui();
        GuiCreator.frame.validate();
    }

    private static int showDialog(final String title, final JPanel myPanel) {
        return JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
    }
}
