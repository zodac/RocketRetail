package shopFront;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class MenuGUI {
	/** 
     * creates a menubar and adds databaseMenus 
     * including staff, product, supplier, customer, order, stock control, profitloss and graphs
     * @param menuBar (JMenuBar) 
     * @param manager (boolean) 
     * */
	public static void createMenuBar(JMenuBar menuBar, boolean manager) {
		
		//Add "Main Menu" menu to menuBar
		menuBar.add(MainMenu.createMenu());
		
		//Add database options to "Database" menu
		JMenu databaseMenu = new JMenu("Database");		
		databaseMenu.add(StaffTable.createMenu(manager));
		databaseMenu.add(ProductTable.createMenu(manager));
		databaseMenu.add(SupplierTable.createMenu(manager));
		databaseMenu.add(CustomerTable.createMenu(manager));
		databaseMenu.add(OrderTable.createMenu());
		
		//Add "Database" menu to menuBar
		menuBar.add(databaseMenu);
		
		//Add "Stock Control" menu to menuBar
		menuBar.add(StockControlMenu.createMenu());

		//Add "Profit & Loss" menu to menuBar
		menuBar.add(ProfitLossMenu.createMenu(manager));
		
		//Add "Graphs" menu to menuBar
		menuBar.add(Graphs.createMenu());
	}
}
