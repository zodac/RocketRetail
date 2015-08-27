package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JComboBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GenderField extends JComboBox<String> implements InputField {

    private final static String[] GENDER_OPTIONS = { "", "Male", "Female" };

    public GenderField() {
        super(GENDER_OPTIONS);
    }

    @Override
    public void addToPanel(JPanel panelToBeAddedTo, GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = (String) this.getSelectedItem();
        return !inputText.isEmpty();
    }
}
