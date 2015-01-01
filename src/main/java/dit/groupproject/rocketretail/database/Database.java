package dit.groupproject.rocketretail.database;

import java.util.ArrayList;
import java.util.Random;

import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

public class Database {

    private static ArrayList<Staff> staffMembers = new ArrayList<Staff>();
    private static ArrayList<Product> products = new ArrayList<Product>();
    private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    private static ArrayList<Order> orders = new ArrayList<Order>();

    public static Staff getStaffMemberByIndex(final int index) {
        return staffMembers.get(index);
    }

    public static Product getProductByIndex(final int index) {
        return products.get(index);
    }

    public static Supplier getSupplierById(final int supplierId) {
        for (final Supplier supplier : suppliers) {
            if (supplier.getSupplierId() == supplierId) {
                return supplier;
            }
        }
        throw new IllegalArgumentException("No supplier with that ID found!");
    }

    public static Order getOrderById(final int orderId) {
        for (final Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("No order with that ID found!");
    }

    public static Staff getRandomStaffMember() {
        final int index = new Random().nextInt(staffMembers.size());
        return staffMembers.get(index);
    }

    /**
     * Resets ArrayLists, then calls methods in {@link InitialiseArray} to
     * populate them.
     * 
     * @see InitialiseArray#addStaff()
     * @see InitialiseArray#addSuppliers()
     * @see InitialiseArray#addProducts()
     * @see InitialiseArray#addCustomers()
     * @see InitialiseArray#addOrders(boolean)
     */
    public static void initialiseDatabase() {
        staffMembers.clear();
        InitialiseArray.addStaff();
        suppliers.clear();
        InitialiseArray.addSuppliers();
        products.clear();
        InitialiseArray.addProducts();
        customers.clear();
        InitialiseArray.addCustomers();
        orders.clear();
        InitialiseArray.addOrders(false);
        InitialiseArray.addOrders(true);

        ShopDriver.setCurrentStaff(Database.getStaffMemberByIndex(0));
    }

    public static ArrayList<Product> getProducts() {
        return products;
    }

    public static ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    public static ArrayList<Order> getOrders() {
        return orders;
    }

    public static ArrayList<Staff> getStaffMembers() {
        return staffMembers;
    }

    public static void addProduct(final Product product) {
        products.add(product);
    }

    public static void addSupplier(final Supplier supplier) {
        suppliers.add(supplier);
    }

    public static void addCustomer(final Customer customer) {
        customers.add(customer);
    }

    public static void addOrder(final Order order) {
        orders.add(order);
    }

    public static void addStaffMember(final Staff staff) {
        staffMembers.add(staff);
    }
}
