package dit.groupproject.rocketretail.menus;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import dit.groupproject.rocketretail.gui.Graphs;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.OrderTable;
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;

public class MenuGui {

    /**
     * Creates a menubar and adds databaseMenus including staff, product,
     * supplier, customer, order, stock control, profitloss and graphs
     * 
     * @param menuBar
     *            (JMenuBar)
     * @param manager
     *            (boolean)
     */
    public static void createMenuBar(final JMenuBar menuBar, final boolean manager) {
        menuBar.add(MainMenu.createMenu());
        menuBar.add(createDatabaseMenu(manager));
        menuBar.add(StockControlMenu.createMenu());
        menuBar.add(ProfitLossMenu.createMenu(manager));
        menuBar.add(Graphs.createMenu());
    }

    private static JMenu createDatabaseMenu(final boolean manager) {
        final JMenu databaseMenu = new JMenu("Database");
        databaseMenu.add(StaffTable.createMenu(TableState.STAFF, manager));
        databaseMenu.add(ProductTable.createMenu(manager));
        databaseMenu.add(SupplierTable.createMenu(TableState.SUPPLIER, manager));
        databaseMenu.add(CustomerTable.createMenu(TableState.CUSTOMER, manager));
        databaseMenu.add(OrderTable.createMenu());

        return databaseMenu;
    }
}
