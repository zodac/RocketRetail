package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.math.NumberUtils;

@SuppressWarnings("serial")
public class CurrencyField extends JTextField implements InputField {

    public CurrencyField() {
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

    public double getValue() {
        return Double.parseDouble(getText());
    }

    @Override
    public String getText() {
        return super.getText().replace("�", "").replace(",", "");
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
