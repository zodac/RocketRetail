package dit.groupproject.rocketretail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

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
        orderedItems.add(new OrderedItem(new Product(1001, "Oranges", 10, 30, 4321, 0.2, 0.6), 10));
        orderedItems.add(new OrderedItem(new Product(1002, "Apples", 15, 30, 4321, 0.2, 0.6), 15));
        orderedItems.add(new OrderedItem(new Product(1003, "Bananas", 10, 30, 4321, 0.2, 0.6), 10));
        orderedItems.add(new OrderedItem(new Product(1004, "Grapes", 10, 30, 4321, 0.2, 0.6), 10));

        ShopDriver.orders.add(new Order(1, 50, 1001, "20/08/2013", orderedItems, false));
        ShopDriver.orders.add(new Order(2, 50, 1007, "20/08/2013", orderedItems, false));
        ShopDriver.orders.add(new Order(3, 50, 10005, "20/08/2013", orderedItems, false));
        ShopDriver.orders.add(new Order(4, 50, 10009, "20/08/2013", orderedItems, false));
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
