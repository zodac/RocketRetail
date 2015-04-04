package dit.groupproject.rocketretail.inputfields;

import java.util.ArrayList;

import javax.swing.JComboBox;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;

@SuppressWarnings("serial")
public class SuppliersField extends JComboBox<String> {

    private static String[] supplierNamesAndIds;

    static {
        final ArrayList<Entity> suppliers = Database.getSuppliers();
        supplierNamesAndIds = new String[suppliers.size() + 1];

        supplierNamesAndIds[0] = "";
        int supplierIndex = 1;

        for (final Entity supplier : suppliers) {
            supplierNamesAndIds[supplierIndex++] = String.format("%s (%d)", supplier.getName(), supplier.getId());
        }
    }

    public SuppliersField() {
        super(supplierNamesAndIds);
    }
}
