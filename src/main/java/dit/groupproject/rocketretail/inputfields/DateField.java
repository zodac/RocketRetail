package dit.groupproject.rocketretail.inputfields;

import static dit.groupproject.rocketretail.utilities.Dates.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEARS_AS_NUMBERS;

import java.awt.GridBagConstraints;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DateField extends JComponent {

    final JComboBox<String> lastPurchaseDay = new JComboBox<>(DAYS_AS_NUMBERS);
    final JComboBox<String> lastPurchaseMonth = new JComboBox<>(MONTHS_AS_NUMBERS);
    final JComboBox<String> lastPurchaseYear = new JComboBox<>(YEARS_AS_NUMBERS);

    public DateField() {

    }

    public String getValue() {
        return lastPurchaseDay.getSelectedItem() + "/" + lastPurchaseMonth.getSelectedItem() + "/" + lastPurchaseYear.getSelectedItem();
    }

    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {
        g.gridx = 1;
        g.gridy++;
        g.gridwidth = 1;
        panelToBeAddedTo.add(lastPurchaseDay, g);
        g.gridx++;
        panelToBeAddedTo.add(lastPurchaseMonth, g);
        g.gridx++;
        panelToBeAddedTo.add(lastPurchaseYear, g);
    }
}
