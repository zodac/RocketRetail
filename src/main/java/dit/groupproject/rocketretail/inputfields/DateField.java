package dit.groupproject.rocketretail.inputfields;

import static dit.groupproject.rocketretail.utilities.Dates.DAYS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.MONTHS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEARS_AS_NUMBERS;
import static dit.groupproject.rocketretail.utilities.Dates.YEAR_START;
import static dit.groupproject.rocketretail.utilities.Formatters.DAY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.MONTH_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.YEAR_FORMATTER;

import java.awt.GridBagConstraints;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DateField extends JComponent implements InputField {

    private final static String DATE_SEPARATOR = "/";

    private final JComboBox<String> dayBox = new JComboBox<>(DAYS_AS_NUMBERS);
    private final JComboBox<String> monthBox = new JComboBox<>(MONTHS_AS_NUMBERS);
    private final JComboBox<String> yearBox = new JComboBox<>(YEARS_AS_NUMBERS);

    public String getDate() {
        return String.format("%s/%s/%s", dayBox.getSelectedItem(), monthBox.getSelectedItem(), yearBox.getSelectedItem());
    }

    public void setDate(final String date) {
        final String[] dateValues = date.split(DATE_SEPARATOR);

        if (dateValues.length != 3 || Integer.parseInt(dateValues[2]) - (YEAR_START - 1) == 0) {
            throw new IllegalArgumentException("Invalid input date: " + date);
        }

        dayBox.setSelectedIndex(Integer.parseInt(dateValues[0]));
        monthBox.setSelectedIndex(Integer.parseInt(dateValues[1]));
        yearBox.setSelectedIndex(Integer.parseInt(dateValues[2]) - (YEAR_START - 1));
    }

    public void setCurrentDate() {
        final Date currentDate = new Date();
        dayBox.setSelectedIndex(Integer.parseInt(DAY_FORMATTER.format(currentDate)));
        monthBox.setSelectedIndex(Integer.parseInt(MONTH_FORMATTER.format(currentDate)));
        yearBox.setSelectedIndex(Integer.parseInt(YEAR_FORMATTER.format(currentDate)) - (YEAR_START - 1));
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
        final int year = yearBox.getSelectedIndex() + YEAR_START - 1;

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);

        final boolean is31stOfFebAprilJunSeptNov = day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11);
        final boolean is30thOfFeb = day == 30 && month == 2;
        final boolean isLeapDay = day == 29 && month == 2;
        final boolean isLeapYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
        final boolean isInvalidInput = day == 0 || month == 0 || year == YEAR_START - 1;

        boolean isInvalidDate = isInvalidInput || is31stOfFebAprilJunSeptNov || is30thOfFeb || (isLeapDay && !isLeapYear);
        final boolean isValid = !isInvalidDate;

        if (isValid) {
            setValidBorder();
        } else {
            setInvalidBorder();
        }

        return isValid;
    }

    @Override
    public void setValidBorder() {
        dayBox.setBorder(VALID_BORDER);
        monthBox.setBorder(VALID_BORDER);
        yearBox.setBorder(VALID_BORDER);
    }

    @Override
    public void setInvalidBorder() {
        dayBox.setBorder(INVALID_BORDER);
        monthBox.setBorder(INVALID_BORDER);
        yearBox.setBorder(INVALID_BORDER);
    }
}
