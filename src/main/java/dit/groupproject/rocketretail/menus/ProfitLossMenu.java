package dit.groupproject.rocketretail.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.HomeScreen;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.utilities.ProfitLoss;

/**
 * ProfitLossMenu adds the "Profit & Loss" option to the menu-bar, with options
 * for a simple or advanced P/L report. The report is shown in the right panel
 * of the screen, in one of two forms:
 * <ul>
 * <li>Totals only</li>
 * <li>Breakdown by year and month, with Totals below</li>
 * </ul>
 */
public class ProfitLossMenu {

    private final static int TEXT_AREA_HEIGHT = 23;
    private final static int TEST_AREA_WIDTH = 45;
    private final static int NUMBER_OF_ROWS = 7;

    /**
     * Creates submenu for "Profit & Loss" and defines ActionListeners.<br />
     * ActionListeners call one of:
     * <ul>
     * <li>{@link #simpleReport()}</li>
     * <li>{@link #advancedReport()}</li>
     * </ul>
     * 
     * @return the JMenu "Profit & Loss" to be added directly to the menu-bar
     * 
     * @param manager
     *            a boolean which enables manager options if true
     * 
     * @see #simpleReport()
     * @see #advancedReport()
     * @see MenuGUI#createMenuBar(JMenuBar, boolean)
     */
    public static JMenu createMenu(boolean manager) {
        JMenuItem simpleReport = new JMenuItem("Create Simple P/L Report");
        simpleReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simpleReport();
            }
        });
        JMenuItem advancedReport = new JMenuItem("Create Advanced P/L Report");
        advancedReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                advancedReport();
            }
        });

        JMenu profitLossMenu = new JMenu("Profit & Loss");
        // Add menu items to "Profit & Loss" menu
        profitLossMenu.add(simpleReport);
        profitLossMenu.add(advancedReport);

        // Define as manager-only
        profitLossMenu.setEnabled(manager);

        return profitLossMenu;
    }

    /**
     * Defines a JTextArea for the total sales, purchases and gross profit of
     * the store, and places it in the right panel of the screen.<br />
     * Calls a string from the {@link ProfitLoss#createTotals()} method and sets
     * the JTextArea to that string.
     * 
     * Removes Homescreen if showing, else keeps main and left panels as they
     * are.
     * 
     * @see ProfitLoss#createTotals()
     * @see HomeScreen#HomeScreen()
     */
    public static void simpleReport() {
        // Reset frame
        GuiCreator.frame.remove(GuiCreator.rightPanel);
        GuiCreator.frame.repaint();
        GuiCreator.rightPanel = new JPanel();

        // ActionListener code
        JTextArea plReportTextArea = new JTextArea(ProfitLoss.createTotals(), TEXT_AREA_HEIGHT, TEST_AREA_WIDTH);
        plReportTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(plReportTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);
        GuiCreator.rightPanel.add(scrollPane);
        if (ShopDriver.getCurrentTableState() == TableState.HOMESCREEN) {
            GuiCreator.mainPanel.removeAll();
        }

        // Update frame
        GuiCreator.setFrame(false, true, false);
    }

    /**
     * Defines a JTextArea for the sales, purchases and gross profit of the
     * store broken down by both month and year. Places JTextArea in the right
     * panel of the screen, and then adds another JTextArea underneath
     * containing the total amounts (similar to {@link #simpleReport()}).<br />
     * Calls a string from the {@link ProfitLoss#createAdvancedReport()} method
     * and sets the first JTextArea to that string, then calls the
     * {@link ProfitLoss#createTotals()} method and sets the second JTextArea to
     * that string.
     * 
     * Removes Homescreen if showing, else keeps main and left panels as they
     * are.
     * 
     * @see #simpleReport()
     * @see ProfitLoss#createAdvancedReport()
     * @see ProfitLoss#createTotals()
     * @see HomeScreen#HomeScreen()
     */
    public static void advancedReport() {
        // Reset frame
        GuiCreator.frame.remove(GuiCreator.rightPanel);
        GuiCreator.frame.repaint();
        GuiCreator.rightPanel = new JPanel(new BorderLayout());

        // ActionListener code
        JTextArea plReportTextArea = new JTextArea(ProfitLoss.createAdvancedReport(), TEXT_AREA_HEIGHT, TEST_AREA_WIDTH);
        plReportTextArea.setEditable(false);

        JTextArea plReportTotal = new JTextArea(ProfitLoss.createTotals(), NUMBER_OF_ROWS, TEST_AREA_WIDTH);
        plReportTotal.setEditable(false);

        Border border = BorderFactory.createLineBorder(Color.gray);
        plReportTotal.setBorder(border);

        JScrollPane scrollPane = new JScrollPane(plReportTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);

        if (ShopDriver.getCurrentTableState() == TableState.HOMESCREEN) {
            GuiCreator.mainPanel.removeAll();
        }

        GuiCreator.rightPanel.add(scrollPane, BorderLayout.CENTER);
        GuiCreator.rightPanel.add(plReportTotal, BorderLayout.SOUTH);

        // Update frame
        GuiCreator.setFrame(false, true, false);
    }
}
