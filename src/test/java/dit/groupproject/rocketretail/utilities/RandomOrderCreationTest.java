package dit.groupproject.rocketretail.utilities;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import dit.groupproject.rocketretail.database.Database;

/**
 * Test class to check robustness of random order creation in
 * InitialiseArray.generateOrders(int, boolean, boolean).
 * 
 * @see InitialiseArray#generateOrders(int, boolean, boolean)
 */
public class RandomOrderCreationTest {

    private final static int NUMBER_OF_RUNS = 100000;

    /**
     * Tests 100,000 instances of InitialiseArray.generateOrders(int, boolean,
     * boolean) method to catch IllegalArgumentException errors.
     * 
     * @see InitialiseArray#generateOrders(int, boolean, boolean)
     */
    @Test
    public void generateOrders() {
        try {
            for (int i = 0; i < NUMBER_OF_RUNS; i++) {
                Database.initialiseDatabase();
                InitialiseArray.generateOrders(25, false, false);
            }
        } catch (final IllegalArgumentException e) {
            fail(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        Database.clearDatabase();
    }
}
