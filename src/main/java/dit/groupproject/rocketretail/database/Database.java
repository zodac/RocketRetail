package dit.groupproject.rocketretail.database;

import java.util.ArrayList;
import java.util.Random;

import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

public class Database {

    private final static Random RANDOM = new Random();

    private static ArrayList<Entity> staffMembers = new ArrayList<>();
    private static ArrayList<Entity> products = new ArrayList<>();
    private static ArrayList<Entity> suppliers = new ArrayList<>();
    private static ArrayList<Entity> customers = new ArrayList<>();
    private static ArrayList<Entity> orders = new ArrayList<>();

    public static Entity getStaffMemberByIndex(final int index) {
        return staffMembers.get(index);
    }

    public static Entity getStaffMemberById(final int staffId) {
        for (final Entity staff : staffMembers) {
            if (staff.getId() == staffId) {
                return staff;
            }
        }
        throw new IllegalArgumentException("No staff member with that ID found!");
    }

    public static Entity getProductById(final int productId) {
        for (final Entity product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }
        throw new IllegalArgumentException("No product with that ID found!");
    }

    public static Entity getSupplierByName(final String supplierName) {
        for (final Entity supplier : suppliers) {
            if (supplier.getName().equals(supplierName)) {
                return supplier;
            }
        }
        throw new IllegalArgumentException("No supplier with that name found!");
    }

    public static Entity getCustomerByName(final String customerName) {
        for (final Entity customer : customers) {
            if (customer.getName().equals(customerName)) {
                return customer;
            }
        }
        throw new IllegalArgumentException("No customer with that name found!");
    }

    public static Entity getCustomerById(final int customerId) {
        for (final Entity customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        throw new IllegalArgumentException("No customer with that ID found!");
    }

    public static Entity getProductByIndex(final int index) {
        return products.get(index);
    }

    public static Entity getCustomerByIndex(final int index) {
        return customers.get(index);
    }

    public static Entity getSupplierById(final int supplierId) {
        for (final Entity supplier : suppliers) {
            if (supplier.getId() == supplierId) {
                return supplier;
            }
        }
        throw new IllegalArgumentException("No supplier with that ID found!");
    }

    public static Entity getOrderBySupplierId(final int traderId) {
        for (final Entity order : orders) {
            if (((Order) order).getTraderId() == traderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("No order with that trader ID found!");
    }

    public static Entity getOrderById(final int orderId) {
        for (final Entity order : orders) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        throw new IllegalArgumentException("No order with that ID found!");
    }

    public static Entity getRandomStaffMember() {
        final int index = RANDOM.nextInt(staffMembers.size());
        return staffMembers.get(index);
    }

    public static Entity getRandomSupplier() {
        final int index = RANDOM.nextInt(suppliers.size());
        return suppliers.get(index);
    }

    public static Entity getRandomCustomer() {
        final int index = RANDOM.nextInt(customers.size());
        return customers.get(index);
    }

    public static Entity getRandomProduct() {
        final int index = RANDOM.nextInt(products.size());
        return products.get(index);
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

        ShopDriver.setCurrentStaff((Staff) Database.getStaffMemberByIndex(0));
    }

    public static ArrayList<Entity> getProducts() {
        return products;
    }

    public static ArrayList<Entity> getSuppliers() {
        return suppliers;
    }

    public static ArrayList<Entity> getCustomers() {
        return customers;
    }

    public static ArrayList<Entity> getOrders() {
        return orders;
    }

    public static ArrayList<Entity> getStaffMembers() {
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

    public static void addStaffMember(final Entity staff) {
        staffMembers.add(staff);
    }

    public static void addCustomerByIndex(final int index, final Entity customer) {
        customers.add(index, customer);
    }

    public static void removeCustomerByIndex(final int index) {
        customers.remove(index);
    }

    public static int getIndexOfCustomer(final Entity customer) {
        return customers.indexOf(customer);
    }

    public static int getIndexOfSupplier(final Entity supplier) {
        return suppliers.indexOf(supplier);
    }

    public static void addSupplierByIndex(final int index, final Entity supplier) {
        suppliers.add(index, supplier);
    }

    public static void removeSupplierByIndex(final int index) {
        suppliers.remove(index);
    }

    public static int getIndexOfStaff(final Entity staff) {
        return staffMembers.indexOf(staff);
    }

    public static void addStaffMemberByIndex(final int index, final Entity staff) {
        staffMembers.add(index, staff);
    }

    public static void removeStaffByIndex(final int index) {
        staffMembers.remove(index);
    }

    public static Entity getSupplierByIndex(final int index) {
        return suppliers.get(index);
    }

    public static void addProductByIndex(final int index, final Entity product) {
        products.add(index, product);
    }

    public static void removeProductByIndex(final int index) {
        products.remove(index);
    }

    public static int getIndexOfProduct(final Entity product) {
        return products.indexOf(product);
    }

    public static void addOrderByIndex(final int index, final Entity order) {
        orders.add(index, order);
    }

    public static void removeOrderByIndex(final int index) {
        orders.remove(index);
    }

    public static int getIndexOfOrder(final Entity order) {
        return orders.indexOf(order);
    }
}
