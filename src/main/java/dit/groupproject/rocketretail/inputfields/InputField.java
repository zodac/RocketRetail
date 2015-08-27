package dit.groupproject.rocketretail.inputfields;

import java.awt.GridBagConstraints;

import javax.swing.JPanel;

public interface InputField {

    void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g);

    boolean isValidInput();
}
