package dit.groupproject.rocketretail.inputfields;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class GenderField extends JComboBox<String> {

    private final static String[] GENDER_OPTIONS = { "", "Male", "Female" };

    public GenderField() {
        super(GENDER_OPTIONS);
    }
}
