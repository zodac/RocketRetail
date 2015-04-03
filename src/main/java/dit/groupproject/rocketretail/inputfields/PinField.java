package dit.groupproject.rocketretail.inputfields;

import javax.swing.JPasswordField;

import dit.groupproject.rocketretail.utilities.JTextFieldLimit;

@SuppressWarnings("serial")
public class PinField extends JPasswordField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public PinField() {
        super(null, INPUT_FIELD_LENGTH);
        super.setDocument(new JTextFieldLimit(4));
    }
}
