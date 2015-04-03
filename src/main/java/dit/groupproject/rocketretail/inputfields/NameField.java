package dit.groupproject.rocketretail.inputfields;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NameField extends JTextField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public NameField() {
        super(INPUT_FIELD_LENGTH);
    }
}
