package dit.groupproject.rocketretail.inputfields;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddressField extends JTextField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public AddressField() {
        super(INPUT_FIELD_LENGTH);
    }
}
