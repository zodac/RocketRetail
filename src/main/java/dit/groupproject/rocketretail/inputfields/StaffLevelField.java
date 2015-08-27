package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class StaffLevelField extends JComboBox<String> implements InputField {

    private final static String[] STAFF_LEVEL_OPTIONS = { "", "Manager", "Employee" };

    public StaffLevelField() {
        super(STAFF_LEVEL_OPTIONS);
    }

    @Override
    public void addToPanel(JPanel panelToBeAddedTo, GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = ((String) this.getSelectedItem());
        return !inputText.isEmpty();
    }
}
