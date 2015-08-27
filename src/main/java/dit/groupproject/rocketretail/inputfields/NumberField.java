package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.math.NumberUtils;

@SuppressWarnings("serial")
public class NumberField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public NumberField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputNumber = this.getText();
        return NumberUtils.isNumber(inputNumber);
    }
}
