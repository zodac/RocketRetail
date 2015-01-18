package dit.groupproject.rocketretail.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Supplier;

public class InitialiseArrayTest {

    private final static String EXPECTED_STAFF_NAME = "James Richards";
    private final static String EXPECTED_SUPPLIER_NAME = "Hurstons";
    private final static String EXPECTED_CUSTOMER_NAME = "James Adams";
    private final static String EXPECTED_PRODUCT_NAME = "Paint Stripper";
    private final static String EXPECTED_EXTRA_PRODUCT_NAME = "Hose Pipe";
    private final static int NUMBER_OF_ORDERS_TO_GENERATE = 10;

    @Test
    public void testAddStaffMembers() {
        InitialiseArray.addStaff();
        assertEquals(EXPECTED_STAFF_NAME, Database.getStaffMemberByIndex(0).getName());
    }

    @Test
    public void testAddSuppliers() {
        InitialiseArray.addSuppliers();
        assertEquals(EXPECTED_SUPPLIER_NAME, Database.getSupplierByIndex(0).getName());
    }

    @Test
    public void testAddCustomers() {
        InitialiseArray.addCustomers();
        assertEquals(EXPECTED_CUSTOMER_NAME, Database.getCustomerByIndex(0).getName());
    }

    @Test
    public void testAddProducts() {
        InitialiseArray.addProducts();
        assertEquals(EXPECTED_PRODUCT_NAME, Database.getProductByIndex(0).getName());
    }

    @Test
    public void testAddOrdersExtra() {
        setUpDatabaseForOrders();
        InitialiseArray.addOrders(true);
        final OrderedItem item = ((Order) Database.getOrders().get(0)).orderedItems.get(0);

        assertEquals(EXPECTED_EXTRA_PRODUCT_NAME, item.getProduct().getName());
    }

    @Test
    public void testAddOrdersNoExtra() {
        setUpDatabaseForOrders();
        InitialiseArray.addOrders(false);
        final OrderedItem item = ((Order) Database.getOrders().get(0)).orderedItems.get(0);

        assertEquals(EXPECTED_PRODUCT_NAME, item.getProduct().getName());
    }

    @Test
    public void testGenerateOrders() {
        Database.initialiseDatabase();
        final int numberOfInitialOrders = Database.getOrders().size();

        InitialiseArray.generateOrders(NUMBER_OF_ORDERS_TO_GENERATE, false, true);

        assertEquals(NUMBER_OF_ORDERS_TO_GENERATE, Database.getOrders().size() - numberOfInitialOrders);
    }

    private void setUpDatabaseForOrders() {
        Database.clearDatabase();
        InitialiseArray.addStaff();
        Database.addCustomer(new Customer("James Adams", "0185664246", "Islandsville", "D5345446", "16/11/2012", "01/10/2000"));
        Database.addSupplier(new Supplier("Hurstons", "018214485", "Shelby Town", "R1223456", "16/09/2013", "16/10/2001"));
        InitialiseArray.addProducts();
    }
}
