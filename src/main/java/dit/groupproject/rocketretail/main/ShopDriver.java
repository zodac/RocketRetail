package dit.groupproject.rocketretail.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JMenuBar;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.HomeScreen;
import dit.groupproject.rocketretail.gui.MenuGUI;
import dit.groupproject.rocketretail.gui.TableState;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

/**
 * The main driver for the store program.
 */
public class ShopDriver {

    public final static String[] YEARS_AS_NUMBERS = { "", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
            "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
            "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" };

    public static final int yearStart = Integer.parseInt(YEARS_AS_NUMBERS[1]);
    public static final int yearCurrent = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

    private static Staff currentStaff;
    private static TableState currentTable = TableState.NONE;

    public static void main(String[] args) {
        Database.initialiseDatabase();
        InitialiseArray.generateOrders(10, false, false);
        GuiCreator.createGui();
        new ShopDriver();
    }

    /**
     * The ShopDriver constructor. Called at the start of the program (again if
     * user chooses to logout).<br />
     * Calls the {@link #logon()} method, creates the menu-bar, and sets
     * {@link #mainPanel} to the homescreen.<br />
     * Then shows the GUI on-screen.
     * 
     * @see #logon()
     * @see MenuGUI#createMenuBar(JMenuBar, boolean)
     * @see HomeScreen#setScreen()
     * @see #showGui(JMenuBar)
     */
    public ShopDriver() {
        // final boolean manager = logon();
        final boolean manager = true;

        JMenuBar menuBar = new JMenuBar();
        MenuGUI.createMenuBar(menuBar, manager);
        HomeScreen.setScreen();
        GuiCreator.showGui(menuBar);
    }

    public static TableState getCurrentTableState() {
        return currentTable;
    }

    public static void setCurrentTable(final TableState newTable) {
        currentTable = newTable;
    }

    public static Staff getCurrentStaff() {
        return currentStaff;
    }

    public static void setCurrentStaff(final Staff newStaff) {
        currentStaff = newStaff;
    }

    // TODO Should not be returning all items in methods below - refactor to
    // take in param and
    // return what's needed
}