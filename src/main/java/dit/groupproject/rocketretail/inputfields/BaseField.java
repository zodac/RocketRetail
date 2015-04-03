package dit.groupproject.rocketretail.inputfields;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class BaseField extends JTextField {

    public BaseField(final int inputFieldLength) {
        super(null, inputFieldLength);
    }

    // abstract boolean isValidInput();

}
