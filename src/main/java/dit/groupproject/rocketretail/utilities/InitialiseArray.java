package dit.groupproject.rocketretail.utilities;

import java.util.ArrayList;
import java.util.Random;

import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * This class is a utility class which is used to generate <code>Staff</code>,
 * <code>Supplier</code>s, <code>Product</code>s, <code>Customer</code>s and
 * <code>Order</code>s for the application.
 * 
 * @author Sheikh
 * @author Tony
 * @author Jason
 * @author Alan
 * @author Jessica
 * 
 * @version 2.0
 * @since 1.0
 */
public class InitialiseArray {

    /**
     * This <code>Random</code> object is used for random number generation in
     * the generateOrders() and addProducts() methods.
     */
    static Random rand = new Random();

    /**
     * This method adds a number of <code>Staff</code> to an
     * <code>ArrayList</code>.
     */
    public static void addStaff() {
        ShopDriver.addStaffMember(new Staff(0, 1001, "James Richards", 1, "0181244862", "Islandsville", 60000, 1,
                "16/10/2001"));
        ShopDriver.addStaffMember(new Staff(1, 1002, "Jennifer Dariada", 2, "0182124456", "Rickysville", 50000, 1,
                "16/11/2001"));
        ShopDriver.addStaffMember(new Staff(2, 1003, "Kayley Murphy", 2, "0192134456", "Fodderville", 60000, 1,
                "16/12/2002"));
        ShopDriver.addStaffMember(new Staff(3, 1001, "James Ricardo", 1, "0181245863", "Newtown", 30000, 2,
                "16/10/2001"));
        ShopDriver.addStaffMember(new Staff(4, 1002, "Jennifer Duffin", 2, "0184124456", "Downtown", 20000, 2,
                "16/11/2001"));
        ShopDriver.addStaffMember(new Staff(5, 1001, "James Ricardo", 1, "0181245867", "Smallsville", 30000, 2,
                "16/10/2001"));
        ShopDriver.addStaffMember(new Staff(6, 1002, "Jennifer Duffin", 2, "0184124456", "New Dobsville", 20000, 2,
                "16/11/2001"));
        ShopDriver.addStaffMember(new Staff(7, 1003, "Kayley Murtaugh", 2, "0132134456", "Shining Water", 40000, 2,
                "16/12/2002"));
        ShopDriver.addStaffMember(new Staff(8, 1004, "Ricky Dunse", 1, "0134667898", "Kasey's Point", 30000, 2,
                "16/01/2003"));
        ShopDriver.addStaffMember(new Staff(9, 1003, "Kayley Murtaugh", 2, "0132134456", "Dumbcreek", 40000, 2,
                "16/12/2002"));
        ShopDriver
                .addStaffMember(new Staff(10, 1004, "Ricky Dunse", 1, "0134667894", "Hobtown", 30000, 2, "16/01/2003"));
        ShopDriver.addStaffMember(new Staff(11, 1005, "Marcus Porter", 1, "0124566891", "Seedyville", 41000, 2,
                "16/09/2004"));
        ShopDriver.addStaffMember(new Staff(12, 1005, "Marc Johns", 1, "0124565891", "Dirtbagtown", 42000, 2,
                "16/09/2004"));
        ShopDriver.addStaffMember(new Staff(13, 1005, "Marcus Porter", 1, "0124566891", "Nowhereville", 41000, 2,
                "16/09/2004"));
        ShopDriver.addStaffMember(new Staff(14, 1005, "Marc Johns", 1, "0124565891", "Nowhereville", 42000, 2,
                "16/09/2004"));
    }

    /**
     * This method adds a number of <code>Supplier</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addSuppliers() {
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Hurstons",
                "018214485", "Shelby Town", "R1223456", "16/09/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Simmerstons", "011214485", "Nurtenville", "T1223478", "14/10/2013", "16/10/2002"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Colbys",
                "012214485", "Duve Town", "R2223456", "15/11/2013", "16/10/2003"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "McGlones",
                "013214485", "Shimmy Point", "G1223456", "16/10/2013", "16/10/2004"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Smestones",
                "014214485", "Shayders Point", "B1223456", "16/10/2013", "15/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Seegers",
                "016214485", "Donetsk", "C1223456", "16/10/2013", "13/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Thurstone Products", "017214485", "Durker Point", "F1223456", "17/10/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Rocket Men", "016214485", "Shelby Town", "N1223456", "19/10/2013", "16/09/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Devil's",
                "018244485", "Nurtenville", "D5223456", "20/10/2013", "14/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Radier Co-op", "018215485", "Durker Point", "S1223456", "16/10/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Dulby's Ltd", "017621445", "Shelby Town", "T1223356", "16/09/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Holkers",
                "016514485", "Durker Point", "R1223456", "16/10/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Covertons'", "018214485", "Nurtenville", "R2223456", "15/11/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "McGrainers", "013214485", "Shelby Town", "G1223456", "16/10/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Shifferstids", "018214485", "Nurtenville", "B1223456", "14/10/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Seellyron",
                "018214485", "Durker Point", "C1223456", "09/10/2013", "16/09/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "James Products", "018214485", "Shelby Town", "F1223456", "13/10/2013", "11/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Grifters & Sons", "018416485", "Newtown", "N1223456", "16/10/2013", "16/10/2001"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart, "Smertons",
                "018314485", "Shining Water", "D5223456", "12/10/2013", "16/10/2005"));
        ShopDriver.addSupplier(new Supplier(ShopDriver.getSuppliers().size() + ShopDriver.supplierIDStart,
                "Radenso Ltd", "017214485", "Dirtbagtown", "S1223456", "11/10/2013", "16/10/2001"));
    }

    /**
     * This method adds a number of <code>Customer</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addCustomers() {
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "James Adams", "0185664246", "Islandsville", "D5345446", "16/11/2012", "01/10/2000"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Jennifer Weeds", "0953124456", "New Dobsville", "E5890446", "06/11/2012", "11/01/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "John Murphy", "0192752456", "Fodderville", "J5312346", "16/01/2013", "16/02/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Ricky Martin", "0132169789", "Kasey's Point", "P5354354", "16/02/2013", "16/04/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Marcus Murphy", "0159267891", "Nowhereville", "L5436755", "16/03/2013", "16/05/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Tom Murphy", "0159267891", "Dirtbagtown", "A7546868", "16/03/2013", "17/05/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Dick Murphy", "0153218491", "Durverville", "S9663446", "17/03/2013", "01/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Harry Murphy", "0754920075", "Hobtown", "D8656943", "18/03/2013", "11/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Marcus Douglas", "0154967891", "Dirtbagtown", "V0643452", "18/03/2013", "16/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart, "Adam Tony",
                "0174348291", "Nowhereville", "C1465446", "20/03/2013", "18/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart, "Tony Adam",
                "0196348491", "Seedyville", "E8588546", "21/03/2013", "20/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Albert Adam", "0198657561", "Seedyville", "R5648905", "22/03/2013", "22/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Albert Winsor", "0676545891", "Fodderville", "T6956865", "23/03/2013", "24/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Elizabeth Winsor", "0678758981", "Dumbcreek", "Z1747454", "24/03/2013", "29/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "William Winsor", "0157879691", "Kasey's Point", "B5647632", "25/03/2013", "30/06/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Liam Winsor", "0156587991", "Dublin", "M8795079", "26/03/2013", "02/07/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Harry Winsor", "0658377891", "Cork", "N0723434", "27/03/2013", "07/07/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Tony Winsor", "0678675561", "Kerry", "L5894354", "28/03/2013", "11/07/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Tom Dickens", "0687562791", "Monaghan", "I5635523", "29/03/2013", "12/07/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart, "Tony Gray",
                "0785766678", "Galway", "P3542153", "30/03/2013", "17/07/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart, "Adam Gray",
                "6678899877", "Wexford", "Y4723854", "31/03/2013", "20/07/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Jennifer Green", "0843437564", "Belfast", "U7894561", "02/04/2013", "02/08/2001"));
        ShopDriver.addCustomer(new Customer(ShopDriver.getCustomers().size() + ShopDriver.customerIDStart,
                "Tony Durkin", "0764567856", "Mayo", "K1234567", "11/04/2013", "20/08/2001"));
    }

    /**
     * This method adds a number of <code>Product</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addProducts() {
        String[] productNames = { "Paint Stripper", "Indian Beer", "Milk", "Hose Pipe", "Bottled Water", "A4 Pad",
                "Stapler", "Toothbrush", "Colgate Toothpaste", "Corn Flakes", "Salted Butter", "Basmati Rice",
                "Al Grigio Wine", "Pen", "Bread", "Mouse Trap", "Keyboard", "Shoes", "Socks", "Hoodie",
                "Car Air Freshners", "Rocket Booster" };

        for (int i = 0; i < productNames.length; i++) {
            double cost = (rand.nextInt(100) + 1) * 0.25;

            ShopDriver.addProduct(new Product(ShopDriver.getProducts().size() + ShopDriver.productIDStart, // Next
                    // available
                    // productID
                    productNames[i], // Pulls product name from array
                    (rand.nextInt(10) + 1) * 25, // Random stockLevel from 25 to
                                                 // 250 (multiples of 25)
                    (rand.nextInt(6) + 5) * 100, // Random maxLevel 500 to 1000
                                                 // (multiples of 100)
                    rand.nextInt(ShopDriver.getSuppliers().size()) + ShopDriver.supplierIDStart, // Random
                    // supplierID
                    // from
                    // available
                    // IDs
                    cost, // Random cost from €0.05 to €25 (multiples of 0.05)
                    (rand.nextInt(50) + (Math.ceil(cost) / 0.25)) * 0.25 // Random
                                                                         // sale
                                                                         // from
                                                                         // cost
                                                                         // to
                                                                         // cost+€25
                                                                         // (multiples
                                                                         // of
                                                                         // 0.05)
                    ));
        }
    }

    /**
     * This method adds a number of <code>Order</code>s to an
     * <code>ArrayList</code>.
     */
    public static void addOrders(boolean extra) {
        ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();

        if (!extra) {
            items.add(new OrderedItem(ShopDriver.getProducts().get(0), 20));
            items.add(new OrderedItem(ShopDriver.getProducts().get(1), 10));
            items.add(new OrderedItem(ShopDriver.getProducts().get(2), 15));

            ShopDriver.getOrders().add(new Order(ShopDriver.getOrders().size(), 0, 1000, "10/03/2004", items, false));
            ShopDriver.getOrders().add(new Order(ShopDriver.getOrders().size(), 0, 10001, "04/10/2008", items, false));
            ShopDriver.getOrders().add(new Order(ShopDriver.getOrders().size(), 1, 10002, "19/12/2009", items, false));
        }

        if (extra) {
            items.add(new OrderedItem(ShopDriver.getProducts().get(3), 10));
            items.add(new OrderedItem(ShopDriver.getProducts().get(4), 5));
            ShopDriver.getOrders().add(new Order(ShopDriver.getOrders().size(), 1, 10003, "20/05/2011", items, false));
            ShopDriver.getOrders().add(new Order(ShopDriver.getOrders().size(), 2, 10001, "11/02/2013", items, false));
            ShopDriver.getOrders().add(new Order(ShopDriver.getOrders().size(), 2, 10004, "01/07/2013", items, false));
        }
    }

    /**
     * This method generates a set of random <code>Order</code>s.
     * 
     * @param amount
     *            An integer defining the number of orders to generate.
     * @param confirm
     *            A boolean to decide if a confirmation message is needed.
     * @param current
     *            A boolean to decide if randDate should be set to the current
     *            year.
     */
    public static void generateOrders(int amount, boolean confirm, boolean current) {
        ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();
        int ordersToCreate = 0;

        if (amount == 0)
            ordersToCreate = rand.nextInt(16) + 5;
        else
            ordersToCreate = amount;

        int i = 0, loops = 0;
        while (i < ordersToCreate && loops < 50) {
            items = new ArrayList<OrderedItem>();
            int itemsToCreate = rand.nextInt(ShopDriver.getProducts().size()) + 1;
            int staffID = 0;

            if (current)
                staffID = ShopDriver.currentStaff.getStaffID();
            else
                staffID = rand.nextInt((ShopDriver.getStaffMembers().size()));

            int traderID = 0, randDate = 0, check = 0;
            String date = "";

            randDate = rand.nextInt(31 - 1) + 1;
            if (randDate < 10)
                date += "0" + randDate + "/";
            else
                date += randDate + "/";

            randDate = rand.nextInt(12 - 1) + 1;
            if (randDate < 10)
                date += "0" + randDate + "/";
            else
                date += randDate + "/";

            if (current)
                randDate = ShopDriver.yearCurrent;
            else
                randDate = rand.nextInt(ShopDriver.yearCurrent - ShopDriver.yearStart) + ShopDriver.yearStart;
            date += randDate;

            check = rand.nextInt(2) + 1;

            if (current && ShopDriver.currentStaff.getStaffLevel() == 2)
                check = 2;

            if (check == 1)
                traderID = rand.nextInt(ShopDriver.getSuppliers().size()) + ShopDriver.supplierIDStart;
            else if (check == 2)
                traderID = rand.nextInt(ShopDriver.getCustomers().size()) + ShopDriver.customerIDStart;

            ArrayList<Integer> productsCreated = new ArrayList<Integer>();
            int productID = rand.nextInt(ShopDriver.getProducts().size()) + ShopDriver.productIDStart;
            boolean unique = false;
            productsCreated.add(productID);

            for (int j = 0; j < itemsToCreate; j++) {

                int whileLoop = 0;

                while (!unique && whileLoop < 40) {
                    productID = rand.nextInt(ShopDriver.getProducts().size()) + ShopDriver.productIDStart;
                    unique = true;

                    for (int x : productsCreated) {
                        if (x == productID)
                            unique = false;
                    }

                    whileLoop++;
                }

                unique = false;
                for (Product p : ShopDriver.getProducts()) {
                    if (p.getProductID() == productID) {
                        if (p.getStockLevel() > 1)
                            items.add(new OrderedItem(p, rand.nextInt(p.getStockLevel() / 2) + 1));
                    }
                }

                if (whileLoop < 40)
                    productsCreated.add(productID);
            }

            if (!items.isEmpty()) {

                boolean valid = true;

                if (traderID > ShopDriver.supplierIDStart && traderID <= ShopDriver.customerIDStart)
                    for (OrderedItem oi : items) {
                        if (oi.getProduct().getStockLevel() + oi.getQuantity() > oi.getProduct().getMaxLevel())
                            valid = false;
                    }

                boolean active = false;

                if (current && (rand.nextInt(2) + 1) == 1)
                    active = true;

                if (valid) {
                    ShopDriver.getOrders().add(
                            new Order(ShopDriver.getOrders().size(), staffID, traderID, date, items, active));
                    i++;
                }
            }
            loops++;
        }

        String output = "";
        if (i == 1)
            output += "1 order created";
        else
            output += i + " orders created";

        if (loops == 50)
            output += " - stock levels getting low";

        if (confirm)
            ShopDriver.setConfirmMessage(output);
    }
}