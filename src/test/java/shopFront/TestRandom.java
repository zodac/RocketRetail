package shopFront;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Test class to check robustness of random order creation in InitialiseArray.generateOrders(int, boolean, boolean).
 * 
 * @see InitialiseArray#generateOrders(int, boolean, boolean)
 */
public class TestRandom {
	/**
	 * Tests 100,000 instances of InitialiseArray.generateOrders(int, boolean, boolean) method to catch IllegalArgumentException errors.
	 * 
	 * @see InitialiseArray#generateOrders(int, boolean, boolean)
	 */
	@Test
	public void GenerateOrders() {
		Throwable caught = null;
		try{
			for(int i = 0; i < 100000; i++){
				ShopDriver.initialiseArrays();
				InitialiseArray.generateOrders(25, false, false);
			}
		}
		catch (Throwable t){
			caught = t;
		}
		
		if(caught != null)
			assertNotSame(IllegalArgumentException.class, caught.getClass());
	}
}
