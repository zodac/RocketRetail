package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.math.NumberUtils;

@SuppressWarnings("serial")
public class NumberField extends JTextField implements InputField {

    public NumberField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputNumber = this.getText();
        final boolean isValid = NumberUtils.isNumber(inputNumber);

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
