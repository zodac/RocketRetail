package dit.groupproject.rocketretail.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartPanel;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.OrderedItem;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

/**
 * This class creates the Home screen for the application.
 */
public class HomeScreen {

    private final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,##0.00");

    /**
     * This method displays the details of the staff member who is currently
     * logged in.
     * 
     * This method also displays a pie chart with the staff sales breakdown.
     * Showing the current staff member's sales versus those of the rest of the
     * employees of the store.
     */
    public static void setHomeScreen() {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout());

        ShopDriver.setCurrentTable(TableState.HOMESCREEN);
        final Staff currentStaff = ShopDriver.getCurrentStaff();

        final JPanel innerPanel = loadProfileImage(currentStaff);
        final JTextArea staffInfo = loadStaffDetails(currentStaff);
        innerPanel.add(staffInfo, BorderLayout.SOUTH);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final double[] salesBreakdown = getSalesValues(currentStaff);
        final ChartPanel piePanel = createSalesPieGraph(currentStaff, salesBreakdown);

        GuiCreator.mainPanel.add(innerPanel, BorderLayout.WEST);
        GuiCreator.mainPanel.add(piePanel, BorderLayout.CENTER);

        GuiCreator.setFrame(false, false, true);
    }

    private static JTextArea loadStaffDetails(final Staff currentStaff) {
        final String level = currentStaff.getStaffLevel() == 1 ? "Manager" : "Employee";
        final String output = "\t" + currentStaff.getStaffName() + "\n" + "\t" + level + " at \"Rocket Retail Inc\" since "
                + currentStaff.getDateAdded() + "\n" + "\t" + "Phone Number is " + currentStaff.getPhoneNumber() + "\n" + "\t" + "Annual wage is €"
                + CURRENCY_FORMATTER.format(currentStaff.getWage());

        final JTextArea staffInfo = new JTextArea(output, 10, 20);
        staffInfo.setBackground(GuiCreator.BACKGROUND_COLOUR);
        staffInfo.setEditable(false);
        return staffInfo;
    }

    private static JPanel loadProfileImage(final Staff currentStaff) {
        final JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        try {
            final int currentStaffGender = currentStaff.getGender();
            String profileImagePath = "images/profile";
            if (currentStaffGender == 1) {
                profileImagePath += "Male.png";
            } else {
                profileImagePath += "Female.png";
            }
            final JLabel homeLabel = new JLabel(new ImageIcon(ImageIO.read(ClassLoader.getSystemResource(profileImagePath))));
            innerPanel.add(homeLabel, BorderLayout.NORTH);
        } catch (IOException e) {
            System.out.println("Error loading profile image.");
        }

        return innerPanel;
    }

    private static double[] getSalesValues(final Staff currentStaff) {
        double currentStaffTotal = 0;
        double otherStaffTotal = 0;

        for (final Order order : Database.getOrders()) {
            final boolean currentStaffOrder = order.getStaffId() == currentStaff.getStaffId();

            if (!order.isSupplier()) {
                double orderTotal = 0;

                for (final OrderedItem orderedItem : order.getOrderedItems()) {
                    orderTotal += orderedItem.getQuantity() * orderedItem.getProduct().getSalePrice();
                }

                if (currentStaffOrder) {
                    currentStaffTotal += orderTotal;
                } else {
                    otherStaffTotal += orderTotal;
                }
            }
        }

        return new double[] { currentStaffTotal, otherStaffTotal };
    }

    private static ChartPanel createSalesPieGraph(final Staff currentStaff, final double[] salesBreakdown) {
        final ChartPanel piePanel = Graphs.createPieChart("Staff sales breakdown", currentStaff.getStaffName(), salesBreakdown);

        piePanel.setPreferredSize(new Dimension(500, 750));
        piePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        piePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        return piePanel;
    }
}
