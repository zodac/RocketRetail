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

public class MenuGUI {

    /**
     * Creates a menubar and adds databaseMenus including staff, product,
     * supplier, customer, order, stock control, profitloss and graphs
     * 
     * @param menuBar
     *            (JMenuBar)
     * @param manager
     *            (boolean)
     * */
    public static void createMenuBar(final JMenuBar menuBar, final boolean manager) {

        // Add "Main Menu" menu to menuBar
        menuBar.add(MainMenu.createMenu());

        // Add database options to "Database" menu
        final JMenu databaseMenu = new JMenu("Database");
        databaseMenu.add(StaffTable.createMenu(manager));
        databaseMenu.add(ProductTable.createMenu(manager));
        databaseMenu.add(SupplierTable.createMenu(TableState.SUPPLIER, manager));
        databaseMenu.add(CustomerTable.createMenu(TableState.CUSTOMER, manager));
        databaseMenu.add(OrderTable.createMenu());

        // Add "Database" menu to menuBar
        menuBar.add(databaseMenu);

        // Add "Stock Control" menu to menuBar
        menuBar.add(StockControlMenu.createMenu());

        // Add "Profit & Loss" menu to menuBar
        menuBar.add(ProfitLossMenu.createMenu(manager));

        // Add "Graphs" menu to menuBar
        menuBar.add(Graphs.createMenu());
    }
}
