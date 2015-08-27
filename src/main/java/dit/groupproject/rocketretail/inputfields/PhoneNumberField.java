package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class PhoneNumberField extends JTextField implements InputField {

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
        final boolean isValid = inputText.matches(NUMBERS_ONLY_SEVEN_TO_TEN_DIGITS_PATTERN);

        if (isValid) {
            setValidBorder();
        } else {
            setInvalidBorder();
        }

        return isValid;
    }

    @Override
    public String getText() {
        return super.getText().replaceAll("\\s", "").replace("-", "");
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
