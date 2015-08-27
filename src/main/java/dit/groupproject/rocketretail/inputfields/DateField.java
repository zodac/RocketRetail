package dit.groupproject.rocketretail.inputfields;

import static dit.groupproject.rocketretail.utilities.Dates.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;
import static dit.groupproject.rocketretail.utilities.Formatters.DAY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.MONTH_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.YEAR_FORMATTER;

import java.awt.GridBagConstraints;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DateField extends JComponent implements InputField {

    final JComboBox<String> dayBox = new JComboBox<>(DAYS_AS_NUMBERS);
    final JComboBox<String> monthBox = new JComboBox<>(MONTHS_AS_NUMBERS);
    final JComboBox<String> yearBox = new JComboBox<>(YEARS_AS_NUMBERS);

    public String getDate() {
        return dayBox.getSelectedItem() + "/" + monthBox.getSelectedItem() + "/" + yearBox.getSelectedItem();
    }

    public void setDate(final String date) {
        final String[] dateValues = date.split("/");
        dayBox.setSelectedIndex(Integer.parseInt(dateValues[0]));
        monthBox.setSelectedIndex(Integer.parseInt(dateValues[1]));
        yearBox.setSelectedIndex(Integer.parseInt(dateValues[2]) - (YEAR_START - 1));
    }

    public void setCurrentDate() {
        dayBox.setSelectedIndex(Integer.parseInt(DAY_FORMATTER.format(new Date())));
        monthBox.setSelectedIndex(Integer.parseInt(MONTH_FORMATTER.format(new Date())));
        yearBox.setSelectedIndex(Integer.parseInt(YEAR_FORMATTER.format(new Date())) - (YEAR_START - 1));
    }

    @Override
    public void addToPanel(final JPanel panelToBeAddedTo, final GridBagConstraints g) {
        g.gridx = 1;
        g.gridy++;
        g.gridwidth = 1;

        panelToBeAddedTo.add(dayBox, g);
        g.gridx++;
        panelToBeAddedTo.add(monthBox, g);
        g.gridx++;
        panelToBeAddedTo.add(yearBox, g);
    }

    @Override
    public boolean isValidInput() {
        final int day = dayBox.getSelectedIndex();
        final int month = monthBox.getSelectedIndex();
        final int year = yearBox.getSelectedIndex();

        return (day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) || (day == 30 && month == 2)
                || (day == 29 && month == 2 && year % 4 != 0);
    }
}
