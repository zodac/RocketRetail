package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class IdField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public IdField() {
        super(INPUT_FIELD_LENGTH);
        this.setEditable(false);
    }

    @Override
    public void addToPanel(JPanel panelToBeAddedTo, GridBagConstraints g) {
        
    }

    @Override
    public boolean isValidInput() {
        // IDs are system-generated - no validation needed (for now)
        return true;
    }
}
