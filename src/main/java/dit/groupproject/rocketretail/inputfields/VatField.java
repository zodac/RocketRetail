package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class VatField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public VatField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isValidInput() {
        // TODO Auto-generated method stub
        return false;
    }
}
