package dit.groupproject.rocketretail.main;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

/**
 * The main driver for the store program.
 */
public class ShopDriver {

    private static Staff currentStaff;
    private static TableState currentTable = TableState.NONE;

    public static void main(final String[] args) {
        Database.initialiseDatabase();
        InitialiseArray.generateOrders(10, false, false);
        GuiCreator.createGui();
        GuiCreator.launchGui();
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
}