package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;

@SuppressWarnings("serial")
public class SuppliersField extends JComboBox<String> implements InputField {

    private final static String SUPPLIER_NAME_FORMAT = "%s (%d)";

    private static String[] supplierNamesAndIds;

    static {
        final ArrayList<Entity> suppliers = Database.getSuppliers();
        supplierNamesAndIds = new String[suppliers.size() + 1];

        supplierNamesAndIds[0] = "";
        int supplierIndex = 1;

        for (final Entity supplier : suppliers) {
            supplierNamesAndIds[supplierIndex++] = String.format(SUPPLIER_NAME_FORMAT, supplier.getName(), supplier.getId());
        }
    }

    public SuppliersField() {
        super(supplierNamesAndIds);
    }

    public int getSelectedSupplierId() {
        return Integer.parseInt(((String) this.getSelectedItem()).split("\\(")[1].split("\\)")[0]);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = ((String) this.getSelectedItem());
        final boolean isValid = !inputText.isEmpty();

        if (isValid) {
            setValidBorder();
        } else {
            setInvalidBorder();
        }

        return isValid;
    }

    @Override
    public void setValidBorder() {
        this.setBorder(VALID_BORDER);
    }

    @Override
    public void setInvalidBorder() {
        this.setBorder(INVALID_BORDER);
    }
}
