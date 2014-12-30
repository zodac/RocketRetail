package dit.groupproject.rocketretail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainMenu {

    /**
     * Create submenu and ActionListeners, and passes back to ShopDriver to add
     * to menuBar
     */
    public static JMenu createMenu() {
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.frame.setTitle("Rocket Retail Inc");
                ShopDriver.Logout();
            }
        });
        JMenuItem resetArrays = new JMenuItem("Reset Arrays");
        resetArrays.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.initialiseArrays();
                ShopDriver.frame.setTitle("Rocket Retail Inc");
                if (ShopDriver.currentTable.equals("Staff"))
                    StaffTable.staff();
                if (ShopDriver.currentTable.equals("Product"))
                    ProductTable.product();
                if (ShopDriver.currentTable.equals("Customer"))
                    CustomerTable.customer();
                if (ShopDriver.currentTable.equals("Supplier"))
                    SupplierTable.supplier();
                if (ShopDriver.currentTable.equals("Order"))
                    OrderTable.order();
            }
        });
        JMenuItem generateOrdersOne = new JMenuItem("1 Order");
        generateOrdersOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(1, true, true);
                if (ShopDriver.currentTable.equals("Staff"))
                    StaffTable.staff();
                if (ShopDriver.currentTable.equals("Product"))
                    ProductTable.product();
                if (ShopDriver.currentTable.equals("Customer"))
                    CustomerTable.customer();
                if (ShopDriver.currentTable.equals("Supplier"))
                    SupplierTable.supplier();
                if (ShopDriver.currentTable.equals("Order")) {
                    OrderTable.first = true;
                    OrderTable.order();
                }

                if (ShopDriver.rightPanel.getComponentCount() > 0) {
                    ShopDriver.rightPanel.validate();
                    ShopDriver.frame.validate();
                }
            }
        });
        JMenuItem generateOrdersFive = new JMenuItem("5 Orders");
        generateOrdersFive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(5, true, true);
                if (ShopDriver.currentTable.equals("Staff"))
                    StaffTable.staff();
                if (ShopDriver.currentTable.equals("Product"))
                    ProductTable.product();
                if (ShopDriver.currentTable.equals("Customer"))
                    CustomerTable.customer();
                if (ShopDriver.currentTable.equals("Supplier"))
                    SupplierTable.supplier();
                if (ShopDriver.currentTable.equals("Order")) {
                    OrderTable.first = true;
                    OrderTable.order();
                }

                if (ShopDriver.rightPanel.getComponentCount() > 0) {
                    ShopDriver.rightPanel.validate();
                    ShopDriver.frame.validate();
                }
            }
        });
        JMenuItem generateOrdersTen = new JMenuItem("10 Orders");
        generateOrdersTen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(10, true, true);
                if (ShopDriver.currentTable.equals("Staff"))
                    StaffTable.staff();
                if (ShopDriver.currentTable.equals("Product"))
                    ProductTable.product();
                if (ShopDriver.currentTable.equals("Customer"))
                    CustomerTable.customer();
                if (ShopDriver.currentTable.equals("Supplier"))
                    SupplierTable.supplier();
                if (ShopDriver.currentTable.equals("Order")) {
                    OrderTable.first = true;
                    OrderTable.order();
                }

                if (ShopDriver.rightPanel.getComponentCount() > 0) {
                    ShopDriver.rightPanel.validate();
                    ShopDriver.frame.validate();
                }
            }
        });
        JMenuItem generateOrdersRandom = new JMenuItem("Random Orders");
        generateOrdersRandom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(0, true, true);
                if (ShopDriver.currentTable.equals("Staff"))
                    StaffTable.staff();
                if (ShopDriver.currentTable.equals("Product"))
                    ProductTable.product();
                if (ShopDriver.currentTable.equals("Customer"))
                    CustomerTable.customer();
                if (ShopDriver.currentTable.equals("Supplier"))
                    SupplierTable.supplier();
                if (ShopDriver.currentTable.equals("Order")) {
                    OrderTable.first = true;
                    OrderTable.order();
                }

                if (ShopDriver.rightPanel.getComponentCount() > 0) {
                    ShopDriver.rightPanel.validate();
                    ShopDriver.frame.validate();
                }
            }
        });

        JMenuItem clearAll = new JMenuItem("Clear All");
        clearAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.frame.remove(ShopDriver.leftPanel);
                ShopDriver.frame.remove(ShopDriver.rightPanel);
                ShopDriver.frame.remove(ShopDriver.mainPanel);

                ShopDriver.currentTable = "";

                StaffTable.first = true;
                ProductTable.first = true;
                CustomerTable.first = true;
                SupplierTable.first = true;

                HomeScreen.setScreen();
                ShopDriver.frame.setTitle("Rocket Retail Inc");
                ShopDriver.frame.validate();
            }
        });
        JMenuItem clearLeft = new JMenuItem("Clear Left Panel");
        clearLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.frame.remove(ShopDriver.leftPanel);
                ShopDriver.frame.validate();
            }
        });
        JMenuItem clearRight = new JMenuItem("Clear Right Panel");
        clearRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.frame.remove(ShopDriver.rightPanel);
                ShopDriver.frame.validate();
            }
        });
        JMenuItem clearCenter = new JMenuItem("Clear Center Panel");
        clearCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShopDriver.frame.remove(ShopDriver.mainPanel);
                StaffTable.first = true;
                ProductTable.first = true;
                SupplierTable.first = true;
                CustomerTable.first = true;
                ShopDriver.frame.setTitle("Rocket Retail Inc");

                HomeScreen.setScreen();
            }
        });
        JMenuItem closeProgram = new JMenuItem("Close Program");
        closeProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add JMenuItems to JMenu, then add to menuBar
        JMenu clearMenu = new JMenu("Clear Screen");
        clearMenu.add(clearAll);
        clearMenu.add(clearLeft);
        clearMenu.add(clearRight);
        clearMenu.add(clearCenter);

        JMenu generateOrderMenu = new JMenu("Generate Orders");
        generateOrderMenu.add(generateOrdersOne);
        generateOrderMenu.add(generateOrdersFive);
        generateOrderMenu.add(generateOrdersTen);
        generateOrderMenu.add(generateOrdersRandom);

        JMenu mainMenu = new JMenu("Main Menu");
        mainMenu.add(clearMenu);
        mainMenu.add(generateOrderMenu);
        mainMenu.add(resetArrays);
        mainMenu.add(logout);
        mainMenu.add(closeProgram);

        return mainMenu;
    }
}
