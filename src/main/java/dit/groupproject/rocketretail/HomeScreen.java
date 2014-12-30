package dit.groupproject.rocketretail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.jfree.chart.ChartPanel;

/**
 * This class creates the Home screen for the application.
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

        ShopDriver.currentTable = "Homescreen";

        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setBackground(ShopDriver.backgroundColour);

        JLabel homeLabel = new JLabel();
        if (ShopDriver.currentStaff.getGender() == 1)
            homeLabel = new JLabel(new ImageIcon("src/res/profileMale.png"));
        else if (ShopDriver.currentStaff.getGender() == 2)
            homeLabel = new JLabel(new ImageIcon("src/res/profileFemale.png"));

        innerPanel.add(homeLabel, BorderLayout.NORTH);

        String level = "";
        if (ShopDriver.currentStaff.getStaffLevel() == 1)
            level = "Manager";
        else if (ShopDriver.currentStaff.getStaffLevel() == 2)
            level = "Employee";

        String output = "\t" + ShopDriver.currentStaff.getStaffName() + "\n" + "\t" + level
                + " at \"Rocket Retail Inc\" since " + ShopDriver.currentStaff.getDateAdded() + "\n" + "\t"
                + "Phone Number is " + ShopDriver.currentStaff.getPhoneNumber() + "\n" + "\t" + "Annual wage is €"
                + doubleFormatter.format(ShopDriver.currentStaff.getWage());

        JTextArea staffInfo = new JTextArea(output, 10, 20);
        staffInfo.setBackground(ShopDriver.backgroundColour);
        staffInfo.setEditable(false);
        innerPanel.add(staffInfo, BorderLayout.SOUTH);
        innerPanel.setBackground(ShopDriver.backgroundColour);

        double staffTotal = 0, otherTotal = 0;

        for (Order o : ShopDriver.orders) {
            if (!o.isSupplier()) {

                double orderTotal = 0;
                for (int i = 0; i < o.getOrderedItems().size(); i++) {
                    orderTotal += o.getOrderedItems().get(i).getQuantity()
                            * o.getOrderedItems().get(i).getProduct().getSalePrice();
                }

                if (o.getStaffID() == ShopDriver.currentStaff.getStaffID())
                    staffTotal += orderTotal;
                else
                    otherTotal += orderTotal;

            }
        }

        // { Individual sales, non-individual sales
        double[] salesBreakdown = { staffTotal, otherTotal };

        ChartPanel piePanel = Graphs.createPieChart("Staff sales breakdown", ShopDriver.currentStaff.getStaffName(),
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
