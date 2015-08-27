package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NameField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public NameField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {
        panelToBeAddedTo.add(this, g);
    }

    @Override
    public boolean isValidInput() {
        return true;
    }
}
