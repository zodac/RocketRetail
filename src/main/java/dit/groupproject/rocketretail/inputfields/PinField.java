package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

@SuppressWarnings("serial")
public class PinField extends JPasswordField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;
    private final static String NUMBERS_ONLY_FOUR_DIGITS_PATTERN = "[0-9]{4}";

    public PinField() {
        super(null, INPUT_FIELD_LENGTH);
        super.setDocument(new JTextFieldLimit(4));
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String pinInput = String.valueOf(this.getPassword());
        return pinInput.matches(NUMBERS_ONLY_FOUR_DIGITS_PATTERN);
    }
}
