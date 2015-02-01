package dit.groupproject.rocketretail.performance;

import javax.swing.JPanel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

/**
 * This is a testing class and it tests that various operations are completed
 * within specified time limits.
 */
public class PerformanceTest {

    private final static int LONG_TIMEOUT_IN_MS = 100;
    private final static int SHORT_TIMEOUT_IN_MS = 50;

    /**
     * This method creates a <code>JPanel</code> and initialises the driver
     * arrays before the tests are run.
     */
    @Before
    public void setUp() {
        GuiCreator.mainPanel = new JPanel();
        GuiCreator.leftPanel = new JPanel();
        Database.initialiseDatabase();
        ShopDriver.setCurrentStaff((Staff) Database.getStaffMemberByIndex(0));
    }

    /**
     * This test is testing that the arrays are initialised within the
     * smallLimit.
     */
    @Test(timeout = SHORT_TIMEOUT_IN_MS)
    public void initialiseArraysTest() {
        Database.initialiseDatabase();
    }

    /**
     * This test is testing that the given amount of orders are generated within
     * the smallLimit.
     */
    @Test(timeout = SHORT_TIMEOUT_IN_MS)
    public void generateTenOrdersTest() {
        InitialiseArray.generateOrders(10, false, true);
    }

    /**
     * This test is testing that the <code>staff()</code> method will complete
     * within the bigLimit.
     */
    @Test(timeout = LONG_TIMEOUT_IN_MS)
    public void staffTest() {
        final StaffTable staffTable = StaffTable.getInstance();
        staffTable.createTableGui();
    }

    /**
     * This test is testing that the <code>product()</code> method will complete
     * within the bigLimit.
     */
    @Test(timeout = LONG_TIMEOUT_IN_MS)
    public void productTest() {
        final ProductTable productTable = ProductTable.getInstance();
        productTable.createTableGui();
    }

    /**
     * This test is testing that the <code>supplier()</code> method will
     * complete within the bigLimit.
     */
    @Test(timeout = LONG_TIMEOUT_IN_MS)
    public void supplierTest() {
        final SupplierTable supplierTable = SupplierTable.getInstance();
        supplierTable.createTableGui();
    }

    /**
     * This test is testing that the <code>customer()</code> method will
     * complete within the bigLimit.
     */
    @Test(timeout = LONG_TIMEOUT_IN_MS)
    public void customerTest() {
        final CustomerTable customerTable = CustomerTable.getInstance();
        customerTable.createTableGui();
    }

    @AfterClass
    public static void tearDown() {
        Database.clearDatabase();
    }
}
