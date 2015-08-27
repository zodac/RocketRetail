package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddressField extends JTextField implements InputField {

    public AddressField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = this.getText();
        final boolean isValid = !inputText.isEmpty();

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
