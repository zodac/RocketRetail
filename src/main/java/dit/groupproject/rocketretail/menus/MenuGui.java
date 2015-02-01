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

    private final MainMenu menu = new MainMenu();
    private final CustomerTable customerTable = CustomerTable.getInstance();
    private final ProductTable productTable = ProductTable.getInstance();
    private final OrderTable orderTable = OrderTable.getInstance();
    private final StaffTable staffTable = StaffTable.getInstance();
    private final SupplierTable supplierTable = SupplierTable.getInstance();

    /**
     * Creates a menubar and adds databaseMenus including staff, product,
     * supplier, customer, order, stock control, profitloss and graphs
     * 
     * @param menuBar
     *            (JMenuBar)
     * @param manager
     *            (boolean)
     */
    public void createMenuBar(final JMenuBar menuBar, final boolean manager) {
        menuBar.add(menu.createMenu());
        menuBar.add(createDatabaseMenu(manager));
        menuBar.add(StockControlMenu.createMenu());
        menuBar.add(ProfitLossMenu.createMenu(manager));
        menuBar.add(Graphs.createMenu());
    }

    private JMenu createDatabaseMenu(final boolean manager) {
        final JMenu databaseMenu = new JMenu("Database");
        databaseMenu.add(staffTable.createMenu(TableState.STAFF, manager));
        databaseMenu.add(productTable.createMenu(TableState.PRODUCT, manager));
        databaseMenu.add(supplierTable.createMenu(TableState.SUPPLIER, manager));
        databaseMenu.add(customerTable.createMenu(TableState.CUSTOMER, manager));
        databaseMenu.add(orderTable.createMenu(TableState.ORDER, manager));

        return databaseMenu;
    }
}
