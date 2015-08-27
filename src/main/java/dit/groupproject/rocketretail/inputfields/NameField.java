package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NameField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;
    private final static String NAME_PATTERN = "^([A-Z]+[a-zA-Z]*[ ])+[A-Z]+[a-zA-Z]*";

    public NameField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = this.getText();
        return inputText.matches(NAME_PATTERN);
    }
}
