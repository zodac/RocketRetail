package dit.groupproject.rocketretail.entityhelpers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.OrderTable;
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;

public class BaseEntityHelper {

    protected final CustomerTable customerTable = CustomerTable.getInstance();
    protected final ProductTable productTable = ProductTable.getInstance();
    protected final OrderTable orderTable = OrderTable.getInstance();
    protected final StaffTable staffTable = StaffTable.getInstance();
    protected final SupplierTable supplierTable = SupplierTable.getInstance();

    protected final static ActionListener cancelListener = new ActionListener() {
        public void actionPerformed(final ActionEvent e) {
            removeLeftPanel();
        }
    };

    protected static void removeLeftPanel() {
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.frame.validate();
    }

    protected static void resetLeftPanel() {
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();
    }

    protected static void updateLeftPanel(final JPanel innerPanel) {
        GuiCreator.leftPanel.add(innerPanel);
        GuiCreator.setFrame(true, false, false);
    }

    protected static JPanel addLabelsToPanel(final String[] labels) {
        final JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        int labelIndex = 0;

        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(1, 10, 0, 5);
        gridBagConstraints.gridx = 0;

        for (final String label : labels) {
            gridBagConstraints.gridy = labelIndex++;
            innerPanel.add(new JLabel(label + ":"), gridBagConstraints);
        }
        return innerPanel;
    }
}