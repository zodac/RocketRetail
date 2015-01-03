package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_CURRENT;
import static dit.groupproject.rocketretail.utilities.DateHandler.YEAR_START;

import java.util.ArrayList;
import java.util.Random;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.IdManager;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * This class is a utility class which is used to generate <code>Staff</code>,
 * <code>Supplier</code>s, <code>Product</code>s, <code>Customer</code>s and
 * <code>Order</code>s for the application.
 */
public class InitialiseArray {

    private final static Random RANDOM = new Random();

    /**
     * This method adds a number of <code>Staff</code> to an
     * <code>ArrayList</code>.
     */
    public static void addStaff() {
        Database.addStaffMember(new Staff(1001, "James Richards", 1, "0181244862", "Islandsville", 60000, 1, "16/10/2001"));
        Database.addStaffMember(new Staff(1002, "Jennifer Dariada", 2, "0182124456", "Rickysville", 50000, 1, "16/11/2001"));
        Database.addStaffMember(new Staff(1003, "Kayley Murphy", 2, "0192134456", "Fodderville", 60000, 1, "16/12/2002"));
        Database.addStaffMember(new Staff(1001, "James Ricardo", 1, "0181245863", "Newtown", 30000, 2, "16/10/2001"));
        Database.addStaffMember(new Staff(1002, "Jennifer Duffin", 2, "0184124456", "Downtown", 20000, 2, "16/11/2001"));
        Database.addStaffMember(new Staff(1001, "James Ricardo", 1, "0181245867", "Smallsville", 30000, 2, "16/10/2001"));
        Database.addStaffMember(new Staff(1002, "Jennifer Duffin", 2, "0184124456", "New Dobsville", 20000, 2, "16/11/2001"));
        Database.addStaffMember(new Staff(1003, "Kayley Murtaugh", 2, "0132134456", "Shining Water", 40000, 2, "16/12/2002"));
        Database.addStaffMember(new Staff(1004, "Ricky Dunse", 1, "0134667898", "Kasey's Point", 30000, 2, "16/01/2003"));
        Database.addStaffMember(new Staff(1003, "Kayley Murtaugh", 2, "0132134456", "Dumbcreek", 40000, 2, "16/12/2002"));
        Database.addStaffMember(new Staff(1004, "Ricky Dunse", 1, "0134667894", "Hobtown", 30000, 2, "16/01/2003"));
        Database.addStaffMember(new Staff(1005, "Marcus Porter", 1, "0124566891", "Seedyville", 41000, 2, "16/09/2004"));
        Database.addStaffMember(new Staff(1005, "Marc Johns", 1, "0124565891", "Dirtbagtown", 42000, 2, "16/09/2004"));
        Database.addStaffMember(new Staff(1005, "Marcus Porter", 1, "0124566891", "Nowhereville", 41000, 2, "16/09/2004"));
        Database.addStaffMember(new Staff(1005, "Marc Johns", 1, "0124565891", "Nowhereville", 42000, 2, "16/09/2004"));
    }

    /**
     * This method adds a number of <code>Supplier</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addSuppliers() {
        Database.addSupplier(new Supplier("Hurstons", "018214485", "Shelby Town", "R1223456", "16/09/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Simmerstons", "011214485", "Nurtenville", "T1223478", "14/10/2013", "16/10/2002"));
        Database.addSupplier(new Supplier("Colbys", "012214485", "Duve Town", "R2223456", "15/11/2013", "16/10/2003"));
        Database.addSupplier(new Supplier("McGlones", "013214485", "Shimmy Point", "G1223456", "16/10/2013", "16/10/2004"));
        Database.addSupplier(new Supplier("Smestones", "014214485", "Shayders Point", "B1223456", "16/10/2013", "15/10/2001"));
        Database.addSupplier(new Supplier("Seegers", "016214485", "Donetsk", "C1223456", "16/10/2013", "13/10/2001"));
        Database.addSupplier(new Supplier("Thurstone Products", "017214485", "Durker Point", "F1223456", "17/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Rocket Men", "016214485", "Shelby Town", "N1223456", "19/10/2013", "16/09/2001"));
        Database.addSupplier(new Supplier("Devil's", "018244485", "Nurtenville", "D5223456", "20/10/2013", "14/10/2001"));
        Database.addSupplier(new Supplier("Radier Co-op", "018215485", "Durker Point", "S1223456", "16/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Dulby's Ltd", "017621445", "Shelby Town", "T1223356", "16/09/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Holkers", "016514485", "Durker Point", "R1223456", "16/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Covertons'", "018214485", "Nurtenville", "R2223456", "15/11/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("McGrainers", "013214485", "Shelby Town", "G1223456", "16/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Shifferstids", "018214485", "Nurtenville", "B1223456", "14/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Seellyron", "018214485", "Durker Point", "C1223456", "09/10/2013", "16/09/2001"));
        Database.addSupplier(new Supplier("James Products", "018214485", "Shelby Town", "F1223456", "13/10/2013", "11/10/2001"));
        Database.addSupplier(new Supplier("Grifters & Sons", "018416485", "Newtown", "N1223456", "16/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Smertons", "018314485", "Shining Water", "D5223456", "12/10/2013", "16/10/2005"));
        Database.addSupplier(new Supplier("Radenso Ltd", "017214485", "Dirtbagtown", "S1223456", "11/10/2013", "16/10/2001"));
    }

    /**
     * This method adds a number of <code>Customer</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addCustomers() {
        Database.addCustomer(new Customer("James Adams", "0185664246", "Islandsville", "D5345446", "16/11/2012", "01/10/2000"));
        Database.addCustomer(new Customer("Jennifer Weeds", "0953124456", "New Dobsville", "E5890446", "06/11/2012", "11/01/2001"));
        Database.addCustomer(new Customer("John Murphy", "0192752456", "Fodderville", "J5312346", "16/01/2013", "16/02/2001"));
        Database.addCustomer(new Customer("Ricky Martin", "0132169789", "Kasey's Point", "P5354354", "16/02/2013", "16/04/2001"));
        Database.addCustomer(new Customer("Marcus Murphy", "0159267891", "Nowhereville", "L5436755", "16/03/2013", "16/05/2001"));
        Database.addCustomer(new Customer("Tom Murphy", "0159267891", "Dirtbagtown", "A7546868", "16/03/2013", "17/05/2001"));
        Database.addCustomer(new Customer("Dick Murphy", "0153218491", "Durverville", "S9663446", "17/03/2013", "01/06/2001"));
        Database.addCustomer(new Customer("Harry Murphy", "0754920075", "Hobtown", "D8656943", "18/03/2013", "11/06/2001"));
        Database.addCustomer(new Customer("Marcus Douglas", "0154967891", "Dirtbagtown", "V0643452", "18/03/2013", "16/06/2001"));
        Database.addCustomer(new Customer("Adam Tony", "0174348291", "Nowhereville", "C1465446", "20/03/2013", "18/06/2001"));
        Database.addCustomer(new Customer("Tony Adam", "0196348491", "Seedyville", "E8588546", "21/03/2013", "20/06/2001"));
        Database.addCustomer(new Customer("Albert Adam", "0198657561", "Seedyville", "R5648905", "22/03/2013", "22/06/2001"));
        Database.addCustomer(new Customer("Albert Winsor", "0676545891", "Fodderville", "T6956865", "23/03/2013", "24/06/2001"));
        Database.addCustomer(new Customer("Elizabeth Winsor", "0678758981", "Dumbcreek", "Z1747454", "24/03/2013", "29/06/2001"));
        Database.addCustomer(new Customer("William Winsor", "0157879691", "Kasey's Point", "B5647632", "25/03/2013", "30/06/2001"));
        Database.addCustomer(new Customer("Liam Winsor", "0156587991", "Dublin", "M8795079", "26/03/2013", "02/07/2001"));
        Database.addCustomer(new Customer("Harry Winsor", "0658377891", "Cork", "N0723434", "27/03/2013", "07/07/2001"));
        Database.addCustomer(new Customer("Tony Winsor", "0678675561", "Kerry", "L5894354", "28/03/2013", "11/07/2001"));
        Database.addCustomer(new Customer("Tom Dickens", "0687562791", "Monaghan", "I5635523", "29/03/2013", "12/07/2001"));
        Database.addCustomer(new Customer("Tony Gray", "0785766678", "Galway", "P3542153", "30/03/2013", "17/07/2001"));
        Database.addCustomer(new Customer("Adam Gray", "6678899877", "Wexford", "Y4723854", "31/03/2013", "20/07/2001"));
        Database.addCustomer(new Customer("Jennifer Green", "0843437564", "Belfast", "U7894561", "02/04/2013", "02/08/2001"));
        Database.addCustomer(new Customer("Tony Durkin", "0764567856", "Mayo", "K1234567", "11/04/2013", "20/08/2001"));
    }

    /**
     * This method adds a number of <code>Product</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addProducts() {
        final String[] productNames = { "Paint Stripper", "Indian Beer", "Milk", "Hose Pipe", "Bottled Water", "A4 Pad", "Stapler", "Toothbrush",
                "Colgate Toothpaste", "Corn Flakes", "Salted Butter", "Basmati Rice", "Al Grigio Wine", "Pen", "Bread", "Mouse Trap", "Keyboard",
                "Shoes", "Socks", "Hoodie", "Car Air Freshners", "Rocket Booster" };

        for (final String productName : productNames) {
            final int stockLevel = (RANDOM.nextInt(10) + 1) * 25;
            final int maxLevel = (RANDOM.nextInt(6) + 5) * 100;
            final double costPrice = (RANDOM.nextInt(100) + 1) * 0.25;
            final double salePrice = (RANDOM.nextInt(50) + (Math.ceil(costPrice) / 0.25)) * 0.25;

            Database.addProduct(new Product(productName, stockLevel, maxLevel, Database.getRandomSupplier().getSupplierId(), costPrice, salePrice));
        }
    }

    /**
     * This method adds a number of <code>Order</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addOrders(boolean extra) {
        final ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();

        if (!extra) {
            items.add(new OrderedItem(Database.getProductByIndex(0), 20));
            items.add(new OrderedItem(Database.getProductByIndex(1), 10));
            items.add(new OrderedItem(Database.getProductByIndex(2), 15));

            ShopDriver.setCurrentStaff(Database.getStaffMemberByIndex(0));
            Database.addOrder(new Order(1000, "10/03/2004", items, false));
            ShopDriver.setCurrentStaff(Database.getRandomStaffMember());
            Database.addOrder(new Order(10001, "04/10/2008", items, false));
            ShopDriver.setCurrentStaff(Database.getRandomStaffMember());
            Database.addOrder(new Order(10002, "19/12/2009", items, false));
        }

        if (extra) {
            items.add(new OrderedItem(Database.getProductByIndex(3), 10));
            items.add(new OrderedItem(Database.getProductByIndex(4), 5));

            ShopDriver.setCurrentStaff(Database.getStaffMemberByIndex(0));
            Database.addOrder(new Order(10003, "20/05/2011", items, false));
            ShopDriver.setCurrentStaff(Database.getRandomStaffMember());
            Database.addOrder(new Order(10001, "11/02/2013", items, false));
            ShopDriver.setCurrentStaff(Database.getRandomStaffMember());
            Database.addOrder(new Order(10004, "01/07/2013", items, false));
        }
    }

    /**
     * This method generates a set of random <code>Order</code>s.
     * 
     * @param amount
     *            An integer defining the number of orders to generate.
     * @param confirm
     *            A boolean to decide if a confirmation message is needed.
     * @param orderInCurrentYear
     *            A boolean to decide if randDate should be set to the current
     *            year.
     */
    public static void generateOrders(int amount, boolean confirm, boolean orderInCurrentYear) {
        ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();
        int ordersToCreate = 0;

        if (amount == 0) {
            ordersToCreate = RANDOM.nextInt(16) + 5;
        } else {
            ordersToCreate = amount;
        }

        int i = 0, loops = 0;
        while (i < ordersToCreate && loops < 50) {
            items = new ArrayList<OrderedItem>();
            int itemsToCreate = RANDOM.nextInt(Database.getProducts().size()) + 1;
            final Staff currentStaff = ShopDriver.getCurrentStaff();

            int traderId = 0, check = 0;

            final int randomDay = RANDOM.nextInt(31 - 1) + 1;
            final int randomMonth = RANDOM.nextInt(12 - 1) + 1;
            final int randomYear = RANDOM.nextInt(YEAR_CURRENT - YEAR_START) + YEAR_START;

            String date = (randomDay < 10) ? "0" + randomDay + "/" : randomDay + "/";
            date += (randomMonth < 10) ? "0" + randomMonth + "/" : randomMonth + "/";
            date += orderInCurrentYear ? YEAR_CURRENT : randomYear;

            check = RANDOM.nextInt(2) + 1;

            if (orderInCurrentYear && currentStaff.getStaffLevel() == 2) {
                check = 2;
            }

            if (check == 1) {
                traderId = Database.getRandomSupplier().getSupplierId();
            } else if (check == 2) {
                traderId = Database.getRandomCustomer().getId();
            }

            ArrayList<Integer> productsCreated = new ArrayList<Integer>();
            int productId = Database.getRandomProduct().getProductId();
            boolean unique = false;
            productsCreated.add(productId);

            for (int j = 0; j < itemsToCreate; j++) {

                int whileLoop = 0;

                while (!unique && whileLoop < 40) {
                    productId = Database.getRandomProduct().getProductId();
                    unique = true;

                    for (int x : productsCreated) {
                        if (x == productId) {
                            unique = false;
                        }
                    }

                    whileLoop++;
                }

                unique = false;

                final Product product = Database.getProductById(productId);
                if (product.getStockLevel() > 1) {
                    items.add(new OrderedItem(product, RANDOM.nextInt(product.getStockLevel() / 2) + 1));
                }

                if (whileLoop < 40) {
                    productsCreated.add(productId);
                }
            }

            if (!items.isEmpty()) {

                boolean valid = true;

                if (traderId > IdManager.SUPPLIER_ID_START && traderId <= IdManager.CUSTOMER_ID_START)
                    for (OrderedItem oi : items) {
                        if (oi.getProduct().getStockLevel() + oi.getQuantity() > oi.getProduct().getMaxLevel()) {
                            valid = false;
                        }
                    }

                boolean activeOrder = orderInCurrentYear && (RANDOM.nextInt(2) + 1) == 1;

                if (valid) {
                    Database.addOrder(new Order(traderId, date, items, activeOrder));
                    i++;
                }
            }
            loops++;
        }

        final StringBuilder output = new StringBuilder();
        output.append((i == 1) ? "1 order created" : i + " orders created");
        output.append((loops == 50) ? " - stock levels getting low" : "");

        if (confirm) {
            GuiCreator.setConfirmationMessage(output.toString());
        }
    }
}