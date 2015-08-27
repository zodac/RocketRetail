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
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = ((String) this.getSelectedItem());
        final boolean isValid = !inputText.isEmpty();

        if (isValid) {
            setValidBorder();
        } else {
            setInvalidBorder();
        }

        return isValid;
    }

    @Override
    public void setValidBorder() {
        this.setBorder(VALID_BORDER);
    }

    @Override
    public void setInvalidBorder() {
        this.setBorder(INVALID_BORDER);
    }
}
