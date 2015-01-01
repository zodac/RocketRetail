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
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.main.ShopDriver;

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
    public static void setScreen() {
        // Reset ShopDriver.frame
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout());

        ShopDriver.setCurrentTable(TableState.HOMESCREEN);

        final JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final Staff currentStaff = ShopDriver.getCurrentStaff();

        try {
            final int currentStaffGender = currentStaff.getGender();
            String profileImagePath = "images/profile";
            if (currentStaffGender == 1) {
                profileImagePath += "Male.png";
            } else {
                profileImagePath += "Female.png";
            }
            final JLabel homeLabel = new JLabel(new ImageIcon(ImageIO.read(ClassLoader
                    .getSystemResource(profileImagePath))));
            innerPanel.add(homeLabel, BorderLayout.NORTH);
        } catch (IOException e) {
            System.out.println("Error loading profile image.");
        }

        final String level = currentStaff.getStaffLevel() == 1 ? "Manager" : "Employee";
        final String output = "\t" + currentStaff.getStaffName() + "\n" + "\t" + level
                + " at \"Rocket Retail Inc\" since " + currentStaff.getDateAdded() + "\n" + "\t" + "Phone Number is "
                + currentStaff.getPhoneNumber() + "\n" + "\t" + "Annual wage is €"
                + CURRENCY_FORMATTER.format(currentStaff.getWage());

        final JTextArea staffInfo = new JTextArea(output, 10, 20);
        staffInfo.setBackground(GuiCreator.BACKGROUND_COLOUR);
        staffInfo.setEditable(false);
        innerPanel.add(staffInfo, BorderLayout.SOUTH);
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        double currentStaffTotal = 0;
        double otherStaffTotal = 0;

        for (final Order o : Database.getOrders()) {
            if (!o.isSupplier()) {
                double orderTotal = 0;

                for (int i = 0; i < o.getOrderedItems().size(); i++) {
                    orderTotal += o.getOrderedItems().get(i).getQuantity()
                            * o.getOrderedItems().get(i).getProduct().getSalePrice();
                }

                if (o.getStaffId() == currentStaff.getStaffId()) {
                    currentStaffTotal += orderTotal;
                } else {
                    otherStaffTotal += orderTotal;
                }
            }
        }

        // { Individual sales, non-individual sales
        final double[] salesBreakdown = { currentStaffTotal, otherStaffTotal };
        final ChartPanel piePanel = Graphs.createPieChart("Staff sales breakdown", currentStaff.getStaffName(),
                salesBreakdown);

        piePanel.setPreferredSize(new Dimension(500, 750));
        piePanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        piePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        GuiCreator.mainPanel.add(innerPanel, BorderLayout.WEST);
        GuiCreator.mainPanel.add(piePanel, BorderLayout.CENTER);

        // Update frame
        GuiCreator.setFrame(false, false, true);
    }
}
