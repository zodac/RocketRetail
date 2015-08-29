package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

@SuppressWarnings("serial")
public class PinField extends JPasswordField implements InputField {

    private final static String NUMBERS_ONLY_FOUR_DIGITS_PATTERN = "[0-9]{4}";

    public PinField() {
        super(null, INPUT_FIELD_LENGTH);
        super.setDocument(new JTextFieldLimit(4));
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    public int getPinValue() {
        return Integer.parseInt(String.valueOf(getPassword()));
    }

    @Override
    public boolean isValidInput() {
        final String pinInput = String.valueOf(this.getPassword());
        final boolean isValid = pinInput.matches(NUMBERS_ONLY_FOUR_DIGITS_PATTERN);

        if (isValid) {
            setValidBorder();
        } else {
            setInvalidBorder();
        }

        return isValid;
    }

    @Override
    public void setValidBorder() {
        this.setBorder(VALID_BORDER);
    }

    @Override
    public void setInvalidBorder() {
        this.setBorder(INVALID_BORDER);
    }
}
