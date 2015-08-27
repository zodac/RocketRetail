package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PhoneNumberField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;
    private final static String NUMBERS_ONLY_SEVEN_TO_TEN_DIGITS_PATTERN = "[0-9]{7,10}";

    public PhoneNumberField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = this.getText();
        return inputText.matches(NUMBERS_ONLY_SEVEN_TO_TEN_DIGITS_PATTERN);
    }
}
