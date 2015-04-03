package dit.groupproject.rocketretail.inputfields;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class StaffLevelField extends JComboBox<String> {

    private final static String[] STAFF_LEVEL_OPTIONS = { "", "Manager", "Employee" };

    public StaffLevelField() {
        super(STAFF_LEVEL_OPTIONS);
    }
}
