package dit.groupproject.rocketretail.utilities;

import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import dit.groupproject.rocketretail.database.Database;

/**
 * Test class to check robustness of random order creation in
 * InitialiseArray.generateOrders(int, boolean, boolean).
 * 
 * @see InitialiseArray#generateOrders(int, boolean, boolean)
 */
public class RandomOrderCreationTest {

    /**
     * Tests 100,000 instances of InitialiseArray.generateOrders(int, boolean,
     * boolean) method to catch IllegalArgumentException errors.
     * 
     * @see InitialiseArray#generateOrders(int, boolean, boolean)
     */
    @Test
    public void generateOrders() {
        Throwable caught = null;
        try {
            for (int i = 0; i < 100000; i++) {
                Database.initialiseDatabase();
                InitialiseArray.generateOrders(25, false, false);
            }
        } catch (Throwable t) {
            caught = t;
        }

        if (caught != null) {
            assertNotSame(IllegalArgumentException.class, caught.getClass());
        }
    }
}
