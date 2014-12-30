package shopFront;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * ProfitLossMenu adds the "Profit & Loss" option to the menu-bar, with options for a simple or advanced P/L report.
 * The report is shown in the right panel of the screen, in one of two forms:
 * <ul><li> Totals only </li>
 * <li> Breakdown by year and month, with Totals below </li></ul>
 */
public class ProfitLossMenu {
	
	//Methods
	/**
	 * Creates submenu for "Profit & Loss" and defines ActionListeners.<br />
	 * ActionListeners call one of:
	 * <ul><li>{@link #simpleReport()}</li>
	 * <li>{@link #advancedReport()}</li></ul>
	 * 
	 * @return			the JMenu "Profit & Loss" to be added directly to the menu-bar
	 * 
	 * @param manager	a boolean which enables manager options if true
	 * 
	 * @see				#simpleReport()
	 * @see				#advancedReport()
	 * @see				MenuGUI#createMenuBar(JMenuBar, boolean)
	 */ 
	public static JMenu createMenu(boolean manager){
		JMenuItem simpleReport = new JMenuItem("Create Simple P/L Report");
		simpleReport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				simpleReport();
			}
		});
		JMenuItem advancedReport = new JMenuItem("Create Advanced P/L Report");
		advancedReport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				advancedReport();
			}
		});
	
		JMenu profitLossMenu = new JMenu("Profit & Loss");
		//Add menu items to "Profit & Loss" menu
		profitLossMenu.add(simpleReport);
		profitLossMenu.add(advancedReport);
		
		//Define as manager-only
		profitLossMenu.setEnabled(manager);
		
		return profitLossMenu;		
	}
	
	/**
	 * Defines a JTextArea for the total sales, purchases and gross profit of the store, and places it in the right panel of the
	 * screen.<br />Calls a string from the {@link ProfitLoss#createTotals()} method and sets the JTextArea to that string.
	 * 
	 * Removes Homescreen if showing, else keeps main and left panels as they are.
	 * 
	 * @see		ProfitLoss#createTotals()
	 * @see		HomeScreen#HomeScreen()
	 */
	public static void simpleReport(){
		//Reset frame
		ShopDriver.frame.remove(ShopDriver.rightPanel);
		ShopDriver.frame.repaint();
		ShopDriver.rightPanel = new JPanel();
		
		//ActionListener code
		JTextArea plReportTextArea = new JTextArea(ProfitLoss.createTotals(), ShopDriver.textAreaHeight, ShopDriver.textAreaWidth);
		plReportTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(plReportTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(ShopDriver.backgroundColour);
		ShopDriver.rightPanel.add(scrollPane);
		if("Homescreen".equals(ShopDriver.currentTable))
			ShopDriver.mainPanel.removeAll();
		
		//Update frame
		ShopDriver.setFrame(false, true, false);
	}
	
	/**
	 * Defines a JTextArea for the sales, purchases and gross profit of the store broken down by both month and year.
	 * Places JTextArea in the right panel of the screen, and then adds another JTextArea underneath containing the total amounts
	 * (similar to {@link #simpleReport()}).<br />
	 * Calls a string from the {@link ProfitLoss#createAdvancedReport()} method and sets the first JTextArea to that string, then
	 * calls the {@link ProfitLoss#createTotals()} method and sets the second JTextArea to that string.
	 * 
	 * Removes Homescreen if showing, else keeps main and left panels as they are.
	 * 
	 * @see		#simpleReport()
	 * @see		ProfitLoss#createAdvancedReport()
	 * @see		ProfitLoss#createTotals()
	 * @see		HomeScreen#HomeScreen()
	 */
	public static void advancedReport(){
		//Reset frame
		ShopDriver.frame.remove(ShopDriver.rightPanel);
		ShopDriver.frame.repaint();
		ShopDriver.rightPanel = new JPanel(new BorderLayout());
		
		//ActionListener code
		JTextArea plReportTextArea = new JTextArea(ProfitLoss.createAdvancedReport().toString(), ShopDriver.textAreaHeight, ShopDriver.textAreaWidth);
		plReportTextArea.setEditable(false);
		
		JTextArea plReportTotal = new JTextArea(ProfitLoss.createTotals(), 7, ShopDriver.textAreaWidth);
		plReportTotal.setEditable(false);
		
		Border border = BorderFactory.createLineBorder(Color.gray);
		plReportTotal.setBorder(border);
		
		JScrollPane scrollPane = new JScrollPane(plReportTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(ShopDriver.backgroundColour);
		
		if("Homescreen".equals(ShopDriver.currentTable))
			ShopDriver.mainPanel.removeAll();
		
		ShopDriver.rightPanel.add(scrollPane, BorderLayout.CENTER);
		ShopDriver.rightPanel.add(plReportTotal, BorderLayout.SOUTH);
		
		//Update frame
		ShopDriver.setFrame(false, true, false);
	}
}
