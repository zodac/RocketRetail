package dit.groupproject.rocketretail.main;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

public class LoginHandler {

    /**
     * Creates a {@link #showDialog(String, JPanel)} pop-up asking for a user's
     * ID and PIN. Matches ID and PIN to values in {@link #staffMembers}.<br />
     * After three unsuccessfully attempts, the program closes.
     */
    public static boolean logon() {
        JPanel myPanel = new JPanel();
        int count = 0; // Counter for attempts
        JTextField idField = new JTextField(5); // JTextField for ID input
        JPasswordField pinField = new JPasswordField(5); // JPasswordField for
                                                         // PIN input
        boolean found = false;

        // Restricts input fields to certain character length
        idField.setDocument(new JTextFieldLimit(3));
        pinField.setDocument(new JTextFieldLimit(4));

        // Add JLabels and JTextFields to panel
        myPanel.add(new JLabel("Staff ID:"));
        myPanel.add(idField);
        myPanel.add(new JLabel("Staff PIN:"));
        myPanel.add(pinField);
        JLabel errorLabel = new JLabel();

        while (count < 3 && !found) {
            if (count == 1) {
                myPanel.add(errorLabel);
                errorLabel.setText("2 attempts remaining");
            } else if (count == 2)
                errorLabel.setText("1 attempt remaining");

            if (showDialog("Please enter your staff ID and PIN", myPanel) == JOptionPane.OK_OPTION) {
                for (final Staff staff : Database.getStaffMembers()) {
                    if (hasValidLogonCredentials(idField, pinField, staff)) {
                        ShopDriver.setCurrentStaff(staff);
                        found = true;
                    }
                }
            } else {
                System.exit(0);
            }
            count++;
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "Too many incorrect logon attempts!", "Logon Fail",
                    JOptionPane.PLAIN_MESSAGE); // Prompts user before closing
            System.exit(0);
        }

        return ShopDriver.getCurrentStaff().getStaffLevel() == 1;
    }

    private static int showDialog(final String title, final JPanel myPanel) {
        return JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null);
    }

    private static boolean hasValidLogonCredentials(JTextField idField, JPasswordField pinField, Staff s) {
        return s.getStaffId() == Integer.parseInt(idField.getText())
                && s.getStaffPin() == Integer.parseInt(String.valueOf(pinField.getPassword()));
    }

    /**
     * Method which allows user to logout, and restarts application at the logon
     * screen
     */
    public static void logout() {
        GuiCreator.clearScreen();

        new ShopDriver();
    }
}
