package dit.groupproject.rocketretail.main;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.database.DatabaseException;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

public class LoginHandler {

    private final static int ID_CHARACTER_LIMIT = 3;
    private final static int MAX_LOGON_ATTEMPTS = 3;
    private final static int PIN_CHARACTER_LIMIT = 4;
    private final static String LOGIN_FAILURE_TITLE = "Login Fail";

    /**
     * Creates a dialog prompting user to enter their ID and PIN.
     * <p>
     * User has three (3) attempts to login before program closes.
     * 
     * @return true if user is manager, false if user is employee
     */
    public static boolean loginAsManager() {
        final JPanel myPanel = new JPanel();
        final JTextField idField = new JTextField(5);
        final JPasswordField pinField = new JPasswordField(5);

        idField.setDocument(new JTextFieldLimit(ID_CHARACTER_LIMIT));
        pinField.setDocument(new JTextFieldLimit(PIN_CHARACTER_LIMIT));

        myPanel.add(new JLabel("Staff ID:"));
        myPanel.add(idField);
        myPanel.add(new JLabel("Staff PIN:"));
        myPanel.add(pinField);

        if (!attemptToLogin(myPanel, idField, pinField)) {
            GuiCreator.showFailureMessage(LOGIN_FAILURE_TITLE, "Too many incorrect login attempts!");
        }

        return ShopDriver.getCurrentStaff().getStaffLevel() == 1;
    }

    private static boolean attemptToLogin(final JPanel myPanel, final JTextField idField, final JPasswordField pinField) {
        final JLabel errorLabel = new JLabel();
        boolean validLogin = false;
        int numberOfAttempts = 0;

        while (waitingForSuccessfulLogon(numberOfAttempts, validLogin)) {
            if (numberOfAttempts == 1) {
                myPanel.add(errorLabel);
            }

            final int remainingAttempts = MAX_LOGON_ATTEMPTS - numberOfAttempts;
            if (remainingAttempts == 1) {
                errorLabel.setText("1 attempt remaining");
            } else {
                errorLabel.setText(remainingAttempts + " attempts remaining");
            }

            GuiCreator.showMandatoryDialog("Please enter your staff ID and PIN", myPanel);
            validLogin = matchInputToDatabase(idField, pinField, validLogin);
            numberOfAttempts++;
        }
        return validLogin;
    }

    private static boolean matchInputToDatabase(final JTextField idField, final JPasswordField pinField, boolean validLogin) {
        try {
            final Staff staff = (Staff) Database.getStaffMemberById(Integer.parseInt(idField.getText()));
            if (hasValidLogonCredentials(staff, Integer.parseInt(String.valueOf(pinField.getPassword())))) {
                ShopDriver.setCurrentStaff(staff);
                validLogin = true;
            }
        } catch (final DatabaseException e) {
            GuiCreator.showFailureMessage(LOGIN_FAILURE_TITLE, "Invalid staff ID!");
        } catch (final NumberFormatException e) {

        }
        return validLogin;
    }

    private static boolean waitingForSuccessfulLogon(int numberOfAttempts, boolean validLogin) {
        return numberOfAttempts < MAX_LOGON_ATTEMPTS && !validLogin;
    }

    private static boolean hasValidLogonCredentials(final Staff staff, final int inputPin) {
        return staff.getStaffPin() == inputPin;
    }
}
