package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class VatField extends JTextField implements InputField {

    private final static int INPUT_FIELD_LENGTH = 20;
    // Valid Irish pattern would be "(IE)?[0-9]S[0-9]{5}L"
    private final static String VAT_NUMBER_PATTERN = "^[A-Z]?[0-9]{7}$";

    public VatField() {
        super(INPUT_FIELD_LENGTH);
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {

    }

    @Override
    public boolean isValidInput() {
        final String inputText = this.getText();
        return inputText.matches(VAT_NUMBER_PATTERN);
    }
}
