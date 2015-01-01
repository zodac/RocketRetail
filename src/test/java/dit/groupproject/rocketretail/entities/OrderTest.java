package dit.groupproject.rocketretail.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * A class that is used to test <code>completeOrder()</code>
 */
public class OrderTest {

    private final static String TEST_DATE = "21/08/2013";

    private ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();
    private ArrayList<Order> orders = new ArrayList<Order>();

    /**
     * Adds orders to the orders arrayList so that the tests can be run.
     * */
    @Before
    public void setUp() {
        ShopDriver.setCurrentStaff(new Staff(0, "Test", 1, "012345678", "Fake Street", 500, 1, "01/01/1970"));
        orders.add(new Order(562, "15/08/2013", orderedItems, false));
        orders.add(new Order(594, "20/08/2013", orderedItems, false));
    }

    /**
     * Tests that the orders being completed by the completeOrder method, that
     * the expected delivery date is set to the correct value.
     * */
    @Test
    public void testCompleteOrder() {
        final Order order = orders.get(0);
        order.completeOrder(TEST_DATE);
        assertEquals(TEST_DATE, order.getDeliveryDate());
    }

    /**
     * Tests that the orders being completed by the completeOrder method, that
     * the expected delivery date is not set to an incorrect value.
     * */
    @Test
    public void testCompleteOrderFail() {
        final Order order = orders.get(1);
        order.completeOrder(TEST_DATE);
        assertNotSame("", order.getDeliveryDate());
    }
}
