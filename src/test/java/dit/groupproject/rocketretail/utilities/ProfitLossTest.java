package dit.groupproject.rocketretail.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;

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
        ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();
        orderedItems.add(new OrderedItem(new Product("Oranges", 10, 30, 4321, 0.2, 0.6), 10));
        orderedItems.add(new OrderedItem(new Product("Apples", 15, 30, 4321, 0.2, 0.6), 15));
        orderedItems.add(new OrderedItem(new Product("Bananas", 10, 30, 4321, 0.2, 0.6), 10));
        orderedItems.add(new OrderedItem(new Product("Grapes", 10, 30, 4321, 0.2, 0.6), 10));

        Database.addOrder(new Order(1001, "20/08/2013", orderedItems, false));
        Database.addOrder(new Order(1007, "20/08/2013", orderedItems, false));
        Database.addOrder(new Order(10005, "20/08/2013", orderedItems, false));
        Database.addOrder(new Order(10009, "20/08/2013", orderedItems, false));
    }

    /**
     * Tests that the report completed by the createSimpleReport method, that
     * the expected total Sales, Purchases and Gross Profit is set to the
     * correct value.
     * */
    @Test
    public void testCreateReport() {
        final String expectedReport = "Totals\n=====\nSales:\t\t€54.00\nPurchases:\t€18.00"
                + "\n============================\nGross Profit:\t€36.00";
        assertEquals(expectedReport, ProfitLoss.createTotals());
    }

    /**
     * Tests that the report completed by the createSimpleReport method, that
     * the expected total Sales, Purchases and Gross Profit is set to the
     * incorrect value.
     * */
    @Test
    public void testCreateReportFail() {
        final String expectedReport = "Totals\n=====\nSales:\t\t€50.00\nPurchases:\t€20.00"
                + "\n============================\nGross Profit:\t€40.00";
        assertNotSame(expectedReport, ProfitLoss.createTotals());
    }
}
