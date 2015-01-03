package dit.groupproject.rocketretail.tables;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

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
}
