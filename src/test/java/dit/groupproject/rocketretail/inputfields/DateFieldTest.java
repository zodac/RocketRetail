package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

/**
 * A class that is used to test {@link DateField}.
 */
public class DateFieldTest {

    private DateField dateField = new DateField();

    @Test
    public void whenCurrentDateIsSet_thenDateIsCorrectlySet() {
        dateField.setCurrentDate();
        final String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.UK).format(new Date());

        final String setDate = dateField.getDate();

        assertThat(setDate, is(equalTo(currentDate)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenEmptyStringIsSelected_thenIllegalArgumentExceptionIsThrown() {
        dateField.setDate("");
        dateField.isValidInput();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInvalidDateFormatIsSelected_thenIllegalArgumentExceptionIsThrown() {
        dateField.setDate("01-01-2000");
        dateField.isValidInput();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDateWithInvalidDayIsSelected_thenIllegalArgumentExceptionIsThrown() {
        dateField.setDate("99/01/2000");
        dateField.isValidInput();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDateWithInvalidMonthIsSelected_thenIllegalArgumentExceptionIsThrown() {
        dateField.setDate("01/99/2000");
        dateField.isValidInput();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDateWithYearBeforeSystemStartsIsSelected_thenIllegalArgumentExceptionIsThrown() {
        dateField.setDate("01/01/1999");
        dateField.isValidInput();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDateWithYearAfterSystemEndsIsSelected_thenIllegalArgumentExceptionIsThrown() {
        dateField.setDate("01/01/2031");
        dateField.isValidInput();
    }

    @Test
    public void whenLeapDaySelected_butNotALeapYear_thenValidationFails() {
        dateField.setDate("29/02/2001");
        final boolean validationResult = dateField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenLeapDaySelected_andIsALeapYear_thenValidationPasses() {
        dateField.setDate("29/02/2004");
        final boolean validationResult = dateField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenValidDateIsSelected_thenValidationPasses() {
        dateField.setDate("01/01/2000");
        final boolean validationResult = dateField.isValidInput();
        assertThat(validationResult, is(true));
    }

}
