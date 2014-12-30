package dit.groupproject.rocketretail.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

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
        orders.add(new Order(201, 562, "15/08/2013", orderedItems, false));
        orders.add(new Order(225, 594, "20/08/2013", orderedItems, false));
    }

    /**
     * Tests that the orders being completed by the completeOrder method, that
     * the expected delivery date is set to the correct value.
     * */
    @Test
    public void testCompleteOrder() {
        orders.get(0).completeOrder(TEST_DATE);
        assertEquals(TEST_DATE, orders.get(0).getDeliveryDate());
    }

    /**
     * Tests that the orders being completed by the completeOrder method, that
     * the expected delivery date is not set to an incorrect value.
     * */
    @Test
    public void testCompleteOrderFail() {
        orders.get(1).completeOrder(TEST_DATE);
        assertNotSame("", orders.get(1).getDeliveryDate());
    }
}
