package dit.groupproject.rocketretail.inputfields;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class IdField extends JTextField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public IdField() {
        super(INPUT_FIELD_LENGTH);
        this.setEditable(false);
    }
}
