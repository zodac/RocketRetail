package dit.groupproject.rocketretail.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JMenuBar;

import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.HomeScreen;
import dit.groupproject.rocketretail.gui.MenuGUI;
import dit.groupproject.rocketretail.gui.TableState;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

/**
 * The main driver for the store program.
 */
public class ShopDriver {

    public final static String[] YEARS_AS_NUMBERS = { "", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
            "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
            "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" };

    private static ArrayList<Staff> staffMembers = new ArrayList<Staff>();
    private static ArrayList<Product> products = new ArrayList<Product>();
    private static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
    private static ArrayList<Customer> customers = new ArrayList<Customer>();
    private static ArrayList<Order> orders = new ArrayList<Order>();

    // public static JFrame frame = new JFrame(); // Main GUI frame
    // static JPanel headerPanel;
    // public static JPanel mainPanel;
    // public static JPanel leftPanel;
    // public static JPanel rightPanel;
    // static JPanel bottomPanel;
    // static JLabel confirmationLabel;

    public static final int yearStart = Integer.parseInt(YEARS_AS_NUMBERS[1]);
    public static final int yearCurrent = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));

    private static Staff currentStaff;
    private static TableState currentTable = TableState.NONE;

    public static void main(String[] args) {
        initialiseArrays();
        currentStaff = staffMembers.get(0);
        InitialiseArray.generateOrders(10, false, false);
        GuiCreator.createGui();
        new ShopDriver();
    }

    /**
     * The ShopDriver constructor. Called at the start of the program (again if
     * user chooses to logout).<br />
     * Calls the {@link #logon()} method, creates the menu-bar, and sets
     * {@link #mainPanel} to the homescreen.<br />
     * Then shows the GUI on-screen.
     * 
     * @see #logon()
     * @see MenuGUI#createMenuBar(JMenuBar, boolean)
     * @see HomeScreen#setScreen()
     * @see #showGui(JMenuBar)
     */
    public ShopDriver() {
        // final boolean manager = logon();
        final boolean manager = true;

        JMenuBar menuBar = new JMenuBar();
        MenuGUI.createMenuBar(menuBar, manager);
        HomeScreen.setScreen();
        GuiCreator.showGui(menuBar);
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
    public static void initialiseArrays() {
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
    }

    public static TableState getCurrentTableState() {
        return currentTable;
    }

    public static void setCurrentTable(final TableState newTable) {
        currentTable = newTable;
    }

    public static Staff getCurrentStaff() {
        return currentStaff;
    }

    public static void setCurrentStaff(final Staff newStaff) {
        currentStaff = newStaff;
    }

    // TODO Should not be returning all items in methods below - refactor to
    // take in param and
    // return what's needed
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