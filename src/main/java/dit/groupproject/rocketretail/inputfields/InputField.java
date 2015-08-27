package dit.groupproject.rocketretail.inputfields;

import java.awt.Color;
import java.awt.GridBagConstraints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;

public interface InputField {

    public final static int INPUT_FIELD_LENGTH = 20;
    public static final Border INVALID_BORDER = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED);
    public static final Border VALID_BORDER = UIManager.getBorder("TextField.border");

    void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g);

    boolean isValidInput();

    void setValidBorder();

    void setInvalidBorder();
}
