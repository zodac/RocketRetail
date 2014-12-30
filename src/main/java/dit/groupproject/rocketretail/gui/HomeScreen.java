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

import dit.groupproject.rocketretail.entities.Order;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.main.ShopDriver;

/**
 * This class creates the Home screen for the application.
 */
public class HomeScreen {

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
        ShopDriver.frame.remove(ShopDriver.mainPanel);
        ShopDriver.frame.repaint();
        ShopDriver.mainPanel = new JPanel(new BorderLayout());
        DecimalFormat doubleFormatter = new DecimalFormat("#,###,##0.00");

        ShopDriver.setCurrentTable(TableState.HOMESCREEN);

        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBackground(ShopDriver.backgroundColour);

        JLabel homeLabel = new JLabel();
        final Staff currentStaff = ShopDriver.getCurrentStaff();

        try {
            final int currentStaffGender = currentStaff.getGender();
            if (currentStaffGender == 1) {
                homeLabel = new JLabel(new ImageIcon(ImageIO.read(ClassLoader
                        .getSystemResource("images/profileMale.png"))));
            } else if (currentStaffGender == 2) {
                homeLabel = new JLabel(new ImageIcon(ImageIO.read(ClassLoader
                        .getSystemResource("images/profileFemale.png"))));
            }
        } catch (IOException e) {
            System.out.println("Error loading profile image.");
        }

        innerPanel.add(homeLabel, BorderLayout.NORTH);

        String level = "";
        final int currentStaffLevel = currentStaff.getStaffLevel();

        if (currentStaffLevel == 1) {
            level = "Manager";
        } else if (currentStaffLevel == 2) {
            level = "Employee";
        }

        String output = "\t" + currentStaff.getStaffName() + "\n" + "\t" + level + " at \"Rocket Retail Inc\" since "
                + currentStaff.getDateAdded() + "\n" + "\t" + "Phone Number is " + currentStaff.getPhoneNumber() + "\n"
                + "\t" + "Annual wage is €" + doubleFormatter.format(currentStaff.getWage());

        JTextArea staffInfo = new JTextArea(output, 10, 20);
        staffInfo.setBackground(ShopDriver.backgroundColour);
        staffInfo.setEditable(false);
        innerPanel.add(staffInfo, BorderLayout.SOUTH);
        innerPanel.setBackground(ShopDriver.backgroundColour);

        double staffTotal = 0, otherTotal = 0;

        for (Order o : ShopDriver.getOrders()) {
            if (!o.isSupplier()) {

                double orderTotal = 0;
                for (int i = 0; i < o.getOrderedItems().size(); i++) {
                    orderTotal += o.getOrderedItems().get(i).getQuantity()
                            * o.getOrderedItems().get(i).getProduct().getSalePrice();
                }

                if (o.getStaffID() == currentStaff.getStaffID()) {
                    staffTotal += orderTotal;
                } else {
                    otherTotal += orderTotal;
                }
            }
        }

        // { Individual sales, non-individual sales
        double[] salesBreakdown = { staffTotal, otherTotal };

        ChartPanel piePanel = Graphs.createPieChart("Staff sales breakdown", currentStaff.getStaffName(),
                salesBreakdown);
        piePanel.setPreferredSize(new Dimension(500, 750));

        piePanel.setBackground(ShopDriver.backgroundColour);
        piePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        ShopDriver.mainPanel.add(innerPanel, BorderLayout.WEST);
        ShopDriver.mainPanel.add(piePanel, BorderLayout.CENTER);

        // Update frame
        ShopDriver.setFrame(false, false, true);
    }
}
