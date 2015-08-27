package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddressField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public AddressField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(JPanel panelToBeAddedTo, GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = this.getText();
        return !inputText.isEmpty();
    }
}
