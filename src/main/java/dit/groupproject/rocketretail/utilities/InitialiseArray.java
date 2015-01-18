package dit.groupproject.rocketretail.utilities;

import static dit.groupproject.rocketretail.utilities.Dates.YEAR_CURRENT;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;

import java.util.ArrayList;
import java.util.Random;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Customer;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Product;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.entities.Supplier;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * This class is a utility class which is used to generate {@link Staff}>,
 * {@link Supplier}s, {@link Product}s, {@link Customer}s and {@link Order}s for
 * the application.
 */
public class InitialiseArray {

    private final static Random RANDOM = new Random();

    /**
     * This method adds a number of {@link Staff} to the {@link Database}.
     */
    public static void addStaff() {
        Database.addStaffMember(new Staff(1001, "James Richards", 1, "0181244862", "Islandsville", 60000, 1, "16/10/2001"));
        Database.addStaffMember(new Staff(1002, "Jennifer Dariada", 2, "0182124456", "Rickysville", 50000, 1, "16/11/2001"));
        Database.addStaffMember(new Staff(1003, "Kayley Murphy", 2, "0192134456", "Fodderville", 60000, 1, "16/12/2002"));
        Database.addStaffMember(new Staff(1001, "James Ricardo", 1, "0181245863", "Newtown", 30000, 2, "16/10/2001"));
        Database.addStaffMember(new Staff(1002, "Jennifer Duffin", 2, "0184124456", "Downtown", 20000, 2, "16/11/2001"));
        Database.addStaffMember(new Staff(1001, "Alex Ricardo", 1, "0181245867", "Smallsville", 30000, 2, "16/10/2001"));
        Database.addStaffMember(new Staff(1002, "Jennifer Duffin", 2, "0184124456", "New Dobsville", 20000, 2, "16/11/2001"));
        Database.addStaffMember(new Staff(1003, "Kayley Harris", 2, "0132134456", "Shining Water", 40000, 2, "16/12/2002"));
        Database.addStaffMember(new Staff(1004, "Ricky Dunse", 1, "0134667898", "Kasey's Point", 30000, 2, "16/01/2003"));
        Database.addStaffMember(new Staff(1003, "Kayley Murtaugh", 2, "0132134456", "Dumbcreek", 40000, 2, "16/12/2002"));
        Database.addStaffMember(new Staff(1004, "Richard Phillips", 1, "0134667894", "Hobtown", 30000, 2, "16/01/2003"));
        Database.addStaffMember(new Staff(1005, "Marcus Porter", 1, "0124566891", "Seedyville", 41000, 2, "16/10/2004"));
        Database.addStaffMember(new Staff(1005, "Marc Johns", 1, "0124565891", "Dirtbagtown", 42000, 2, "16/09/2004"));
        Database.addStaffMember(new Staff(1005, "Nicholas Ennis", 1, "0124566891", "Nowheretown", 41000, 2, "16/09/2004"));
        Database.addStaffMember(new Staff(1005, "Marc Johnson", 1, "0124565891", "Nowhereville", 42000, 2, "16/09/2004"));
    }

    /**
     * This method adds a number of {@link Supplier}s to the {@link Database}.
     */
    public static void addSuppliers() {
        Database.addSupplier(new Supplier("Hurstons", "018214485", "Shelby Town", "R1223456", "16/09/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Simmerstons", "011214485", "Nurtenville", "T1223478", "14/10/2013", "16/10/2002"));
        Database.addSupplier(new Supplier("Colbys", "012214485", "Duve Town", "R2223456", "15/11/2013", "16/10/2003"));
        Database.addSupplier(new Supplier("McGlones", "013214485", "Shimmy Point", "G1223456", "16/10/2013", "16/10/2004"));
        Database.addSupplier(new Supplier("Smestones", "014214485", "Shayders Point", "B1223456", "16/10/2013", "15/10/2001"));
        Database.addSupplier(new Supplier("Seegers", "016214485", "Donetsk", "C1223456", "16/10/2013", "13/10/2001"));
        Database.addSupplier(new Supplier("Thurstone Products", "017214485", "Durker Heights", "F1223456", "17/10/2013", "16/10/2001"));
        Database.addSupplier(new Supplier("Rocket Men", "016214485", "Shelby Town", "N1223456", "19/10/2013", "16/09/2001"));
        Database.addSupplier(new Supplier("Devil's", "0182378485", "Nurtenville", "D5223456", "20/10/2013", "14/10/2001"));
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
     * This method adds a number of {@link Customer}s to the {@link Database}.
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
     * This method adds a number of {@link Product}s to the {@link Database}.
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

            Database.addProduct(new Product(productName, stockLevel, maxLevel, Database.getRandomSupplierId(), costPrice, salePrice));
        }
    }

    /**
     * This method adds a number of {@link Order}s to the {@link Database}.
     */
    public static void addOrders(boolean extra) {
        final ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();
        ShopDriver.setCurrentStaff((Staff) Database.getStaffMemberByIndex(0));

        if (extra) {
            items.add(new OrderedItem((Product) Database.getProductByIndex(3), 10));
            items.add(new OrderedItem((Product) Database.getProductByIndex(4), 5));

            Database.addOrder(new Order(Database.getRandomCustomerOrSupplierId(), "20/05/2011", items, false));
            ShopDriver.setCurrentStaff((Staff) Database.getRandomStaffMember());
            Database.addOrder(new Order(Database.getRandomCustomerOrSupplierId(), "11/02/2013", items, false));
            ShopDriver.setCurrentStaff((Staff) Database.getRandomStaffMember());
            Database.addOrder(new Order(Database.getRandomCustomerOrSupplierId(), "01/07/2013", items, false));
        } else {
            items.add(new OrderedItem((Product) Database.getProductByIndex(0), 20));
            items.add(new OrderedItem((Product) Database.getProductByIndex(1), 10));
            items.add(new OrderedItem((Product) Database.getProductByIndex(2), 15));

            Database.addOrder(new Order(Database.getRandomCustomerOrSupplierId(), "10/03/2004", items, false));
            ShopDriver.setCurrentStaff((Staff) Database.getRandomStaffMember());
            Database.addOrder(new Order(Database.getRandomCustomerOrSupplierId(), "04/10/2008", items, false));
            ShopDriver.setCurrentStaff((Staff) Database.getRandomStaffMember());
            Database.addOrder(new Order(Database.getRandomCustomerOrSupplierId(), "19/12/2009", items, false));
        }
    }

    /**
     * This method generates a set of random {@link Order}s.
     * 
     * @param numberOfOrders
     *            the number of orders to generate
     * @param displayConfirmationMessage
     *            boolean to decide if a confirmation message is needed
     * @param orderInCurrentYear
     *            boolean to decide if order is in to the current year
     */
    public static void generateOrders(final int numberOfOrders, final boolean displayConfirmationMessage, final boolean orderInCurrentYear) {
        final int ordersToCreate = (numberOfOrders == 0) ? RANDOM.nextInt(16) + 5 : numberOfOrders;
        final int maxLoop = 50;
        final int maxWhile = 40;

        int index = 0;
        int loops = 0;
        ArrayList<OrderedItem> items = new ArrayList<OrderedItem>();

        while (index < ordersToCreate && loops < maxLoop) {
            items = new ArrayList<OrderedItem>();
            int itemsToCreate = RANDOM.nextInt(Database.getProducts().size()) + 1;
            final Staff currentStaff = ShopDriver.getCurrentStaff();

            int traderId = 0;

            final int randomDay = RANDOM.nextInt(31 - 1) + 1;
            final int randomMonth = RANDOM.nextInt(12 - 1) + 1;
            final int randomYear = RANDOM.nextInt(YEAR_CURRENT - YEAR_START) + YEAR_START;

            String date = randomDay < 10 ? "0" + randomDay + "/" : randomDay + "/";
            date += randomMonth < 10 ? "0" + randomMonth + "/" : randomMonth + "/";
            date += orderInCurrentYear ? YEAR_CURRENT : randomYear;

            boolean isSupplier = Math.random() < 0.5;
            if (orderInCurrentYear && currentStaff.getStaffLevel() == 2) {
                isSupplier = false;
            }

            if (isSupplier) {
                traderId = Database.getRandomSupplierId();
            } else {
                traderId = Database.getRandomCustomerId();
            }

            final ArrayList<Integer> productsCreated = new ArrayList<Integer>();
            int productId = Database.getRandomProduct().getId();
            boolean unique = false;
            productsCreated.add(productId);

            for (int j = 0; j < itemsToCreate; j++) {

                int whileLoop = 0;

                while (!unique && whileLoop < maxWhile) {
                    productId = Database.getRandomProduct().getId();
                    unique = true;

                    for (final int product : productsCreated) {
                        if (product == productId) {
                            unique = false;
                        }
                    }

                    whileLoop++;
                }

                unique = false;

                final Product product = (Product) Database.getProductById(productId);
                if (product.getStockLevel() > 1) {
                    items.add(new OrderedItem(product, RANDOM.nextInt(product.getStockLevel() / 2) + 1));
                }

                if (whileLoop < 40) {
                    productsCreated.add(productId);
                }
            }

            if (!items.isEmpty()) {
                boolean valid = true;

                if (isSupplier) {
                    for (final OrderedItem oi : items) {
                        final Product orderedProduct = oi.getProduct();
                        if (orderedProduct.getStockLevel() + oi.getQuantity() > orderedProduct.getMaxLevel()) {
                            valid = false;
                        }
                    }
                }

                final boolean activeOrder = orderInCurrentYear && Math.random() < 0.5;

                if (valid) {
                    Database.addOrder(new Order(traderId, date, items, activeOrder));
                    index++;
                }
            }
            loops++;
        }

        final StringBuilder output = new StringBuilder();
        output.append((index == 1) ? "1 order created" : index + " orders created");
        output.append((loops == 50) ? " - stock levels getting low" : "");

        if (displayConfirmationMessage) {
            GuiCreator.setConfirmationMessage(output.toString());
        }
    }
}