package dit.groupproject.rocketretail.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.gui.HomeScreen;
import dit.groupproject.rocketretail.main.LoginHandler;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;
import dit.groupproject.rocketretail.tables.CustomerTable;
import dit.groupproject.rocketretail.tables.OrderTable;
import dit.groupproject.rocketretail.tables.ProductTable;
import dit.groupproject.rocketretail.tables.StaffTable;
import dit.groupproject.rocketretail.tables.SupplierTable;
import dit.groupproject.rocketretail.utilities.InitialiseArray;

public class MainMenu {

    private final CustomerTable customerTable = CustomerTable.getInstance();
    private final ProductTable productTable = ProductTable.getInstance();
    private final OrderTable orderTable = OrderTable.getInstance();
    private final StaffTable staffTable = StaffTable.getInstance();
    private final SupplierTable supplierTable = SupplierTable.getInstance();

    /**
     * Create submenu and ActionListeners, and passes back to ShopDriver to add
     * to menuBar
     */
    public JMenu createMenu() {
        final JMenu clearMenu = createClearMenu();
        final JMenu generateOrderMenu = createGenerateOrdersMenu();
        final JMenu mainMenu = createMainMenu(clearMenu, generateOrderMenu);

        return mainMenu;
    }

    private JMenu createClearMenu() {
        final JMenuItem clearAll = new JMenuItem("Clear All");
        clearAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.leftPanel);
                GuiCreator.frame.remove(GuiCreator.rightPanel);
                GuiCreator.frame.remove(GuiCreator.mainPanel);

                ShopDriver.setCurrentTable(TableState.NONE);

                staffTable.first = true;
                productTable.first = true;
                customerTable.first = true;
                supplierTable.first = true;

                HomeScreen.setHomeScreen();
                GuiCreator.frame.setTitle("Rocket Retail Inc");
                GuiCreator.frame.validate();
            }
        });
        final JMenuItem clearLeft = new JMenuItem("Clear Left Panel");
        clearLeft.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.leftPanel);
                GuiCreator.frame.validate();
            }
        });
        final JMenuItem clearRight = new JMenuItem("Clear Right Panel");
        clearRight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.rightPanel);
                GuiCreator.frame.validate();
            }
        });
        final JMenuItem clearCenter = new JMenuItem("Clear Center Panel");
        clearCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.remove(GuiCreator.mainPanel);
                staffTable.first = true;
                productTable.first = true;
                supplierTable.first = true;
                customerTable.first = true;
                GuiCreator.frame.setTitle("Rocket Retail Inc");

                HomeScreen.setHomeScreen();
            }
        });

        final JMenu clearMenu = new JMenu("Clear Screen");
        clearMenu.add(clearAll);
        clearMenu.add(clearLeft);
        clearMenu.add(clearRight);
        clearMenu.add(clearCenter);

        return clearMenu;
    }

    private JMenu createGenerateOrdersMenu() {
        final JMenuItem generateOrdersOne = new JMenuItem("1 Order");
        generateOrdersOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(1, true, true);
                createCurrentTable();
                clearRightPanel();
            }
        });
        final JMenuItem generateOrdersFive = new JMenuItem("5 Orders");
        generateOrdersFive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(5, true, true);
                createCurrentTable();
                clearRightPanel();
            }
        });
        final JMenuItem generateOrdersTen = new JMenuItem("10 Orders");
        generateOrdersTen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(10, true, true);
                createCurrentTable();
                clearRightPanel();
            }
        });
        final JMenuItem generateOrdersRandom = new JMenuItem("Random Orders");
        generateOrdersRandom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InitialiseArray.generateOrders(0, true, true);
                createCurrentTable();
                clearRightPanel();
            }
        });

        final JMenu generateOrderMenu = new JMenu("Generate Orders");
        generateOrderMenu.add(generateOrdersOne);
        generateOrderMenu.add(generateOrdersFive);
        generateOrderMenu.add(generateOrdersTen);
        generateOrderMenu.add(generateOrdersRandom);

        return generateOrderMenu;
    }

    private JMenu createMainMenu(final JMenu clearMenu, final JMenu generateOrderMenu) {
        final JMenuItem resetArrays = new JMenuItem("Reset Arrays");
        resetArrays.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Database.initialiseDatabase();
                GuiCreator.frame.setTitle("Rocket Retail Inc");
                createCurrentTable();
            }
        });
        final JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GuiCreator.frame.setTitle("Rocket Retail Inc");
                LoginHandler.logout();
            }
        });
        final JMenuItem closeProgram = new JMenuItem("Close Program");
        closeProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        final JMenu mainMenu = new JMenu("Main Menu");
        mainMenu.add(clearMenu);
        mainMenu.add(generateOrderMenu);
        mainMenu.add(resetArrays);
        mainMenu.add(logout);
        mainMenu.add(closeProgram);

        return mainMenu;
    }

    private static void clearRightPanel() {
        if (GuiCreator.rightPanel.getComponentCount() > 0) {
            GuiCreator.rightPanel.validate();
            GuiCreator.frame.validate();
        }
    }

    private void createCurrentTable() {
        if (ShopDriver.getCurrentTableState().equals(TableState.STAFF)) {
            staffTable.createTableGui();
        } else if (ShopDriver.getCurrentTableState().equals(TableState.PRODUCT)) {
            productTable.createTableGui();
        } else if (ShopDriver.getCurrentTableState().equals(TableState.CUSTOMER)) {
            customerTable.createTableGui();
        } else if (ShopDriver.getCurrentTableState().equals(TableState.SUPPLIER)) {
            supplierTable.createTableGui();
        } else if (ShopDriver.getCurrentTableState().equals(TableState.ORDER)) {
            orderTable.first = true;
            orderTable.createTableGui();
        }
    }
}
