package dit.groupproject.rocketretail.gui;

import java.util.List;

import dit.groupproject.rocketretail.inputfields.InputField;

public class FieldValidator {

    /**
     * Validates all input fields. If any are invalid, their border will be
     * marked, and a notification will be displayed.
     * 
     * @param inputFields
     * @return true if all fields have a valid input
     */
    public static boolean checkFields(final List<InputField> inputFields) {
        boolean valid = true;

        for (final InputField inputField : inputFields) {
            valid = inputField.isValidInput() && valid;
        }

        if (!valid) {
            GuiCreator.setConfirmationMessage("Invalid entry!");
        }

        return valid;
    }

}
