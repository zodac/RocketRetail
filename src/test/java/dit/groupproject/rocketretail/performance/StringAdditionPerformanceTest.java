package dit.groupproject.rocketretail.performance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Stand-alone class which compares the runtime of different method for string
 * concatenation.<br />
 * Compares the "String +=" operation, the "StringBuffer append" operation and
 * the "StringBuilder append" operation.<br />
 * Runs for 100,000,000 iterations.
 * 
 * @see String
 * @see StringBuffer#append(String)
 * @see StringBuilder#append(String)
 */
public class StringAdditionPerformanceTest {

    private final static int N = 100000000;

    /**
     * Main method. Runs the concatenation operation for the three String-type
     * objects for N runs.<br />
     * It then prints out the time (in milliseconds) taken per method.
     */
    @Test
    public void StringAdditionTest() {
        long time;

        StringBuffer sbuff = new StringBuffer();
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            sbuff.append("");
        }

        long sbuffTime = System.currentTimeMillis() - time;

        StringBuilder sbuild = new StringBuilder();
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            sbuild.append("");
        }
        long sbuildTime = System.currentTimeMillis() - time;

        @SuppressWarnings("unused")
        String s = "";
        time = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            s += "";
        }

        long sTime = System.currentTimeMillis() - time;

        boolean result = true;

        if (sTime < sbuildTime || sbuffTime < sbuildTime) {
            result = false;
        }

        assertEquals(result, true);
    }

}
