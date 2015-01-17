package dit.groupproject.rocketretail.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * A class that is used to test <code>testCreateReport()</code>
 */
public class ProfitLossTest {

    /**
     * Adds ordered items to the orderedItems arrayList and adds orders to the
     * orders arrayList so that the tests can be run
     * */
    @BeforeClass
    public static void setUp() {
        final Product oranges = new Product("Oranges", 10, 30, 4321, 0.2, 0.6);
        final Product apples = new Product("Apples", 15, 30, 4321, 0.2, 0.6);
        final Product bananas = new Product("Bananas", 10, 30, 4321, 0.2, 0.6);
        final Product grapes = new Product("Grapes", 10, 30, 4321, 0.2, 0.6);

        final ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();
        orderedItems.add(new OrderedItem(oranges, 10));
        orderedItems.add(new OrderedItem(apples, 15));
        orderedItems.add(new OrderedItem(bananas, 10));
        orderedItems.add(new OrderedItem(grapes, 10));

        Database.addProduct(oranges);
        Database.addProduct(apples);
        Database.addProduct(bananas);
        Database.addProduct(grapes);

        Database.addStaffMember(new Staff(1001, "TestStaff", 1, "012345678", "Fake Street", 100, 1, "01/01/1970"));
        ShopDriver.setCurrentStaff((Staff) Database.getStaffMemberByIndex(0));
        Database.addSupplier(new Supplier("TestSupplier", "012345678", "Fake Street", "VATNUMBER", "01/01/1970", "01/01/1970"));
        Database.addCustomer(new Customer("TestCustomer", "012345678", "Fake Street", "VATNUMBER", "01/01/1970", "01/01/1970"));

        Database.addOrder(new Order(Database.getRandomCustomer().getId(), "20/08/2013", orderedItems, false));
        Database.addOrder(new Order(Database.getRandomCustomer().getId(), "20/08/2013", orderedItems, false));
        Database.addOrder(new Order(Database.getRandomSupplier().getId(), "20/08/2013", orderedItems, false));
        Database.addOrder(new Order(Database.getRandomSupplier().getId(), "20/08/2013", orderedItems, false));
    }

    /**
     * Tests that the report completed by the createSimpleReport method, that
     * the expected total Sales, Purchases and Gross Profit is set to the
     * correct value.
     * */
    @Test
    public void testCreateReport() {
        final String expectedReport = "Totals\n=====\nSales:\t\t€54.00\nPurchases:\t€18.00" + "\n============================\nGross Profit:\t€36.00";
        assertEquals(expectedReport, ProfitLoss.createTotals());
    }

    /**
     * Tests that the report completed by the createSimpleReport method, that
     * the expected total Sales, Purchases and Gross Profit is set to the
     * incorrect value.
     * */
    @Test
    public void testCreateReportFail() {
        final String expectedReport = "Totals\n=====\nSales:\t\t€50.00\nPurchases:\t€20.00" + "\n============================\nGross Profit:\t€40.00";
        assertNotSame(expectedReport, ProfitLoss.createTotals());
    }

    @AfterClass
    public static void teardown() {
        Database.clearDatabase();
    }
}
