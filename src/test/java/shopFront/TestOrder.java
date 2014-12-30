package shopFront;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
/**
 * A class that is used to test
 * <code>completeOrder()</code>
 */
public class TestOrder{
	private static ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();
	private static ArrayList<Order> orders = new ArrayList<Order>();
	/**
	 * adds orders to the orders arrayList so that the tests can be run
	 * */
	@BeforeClass
	public static void setUpBeforeClass(){
		orders.add(new Order(001, 201, 562, "15/08/2013", orderedItems, false));
		orders.add(new Order(002, 225, 594, "20/08/2013", orderedItems, false));
	}
	/**
	 * tests that the orders being completed by the completeOrder method, that the expected
	 * delivery date is set to the correct value - expecting pass
	 * */
	@Test
	public void testCompleteOrder(){
		orders.get(0).completeOrder("21/08/2013");
		assertEquals(orders.get(0).getDeliveryDate(), "21/08/2013");
	}
	/**
	 * tests that the orders being completed by the completeOrder method, that the expected
	 * delivery date is not set to an incorrect value - expecting fail -> pass
	 * */
	@Test
	public void testCompleteOrderFail(){
		orders.get(1).completeOrder("21/08/2013");
		assertNotSame(orders.get(1).getDeliveryDate(), "");
	}
}
