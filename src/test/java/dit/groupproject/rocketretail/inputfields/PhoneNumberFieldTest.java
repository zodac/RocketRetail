package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link PhoneNumberField}.
 */
public class PhoneNumberFieldTest {

    private PhoneNumberField phoneNumberField = new PhoneNumberField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        phoneNumberField.setText("");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputContainsLetters_thenValidationFails() {
        phoneNumberField.setText("A123456");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputContainsSymbols_thenValidationFails() {
        phoneNumberField.setText("!123456");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputIsLongerThanTenDigits_thenValidationFails() {
        phoneNumberField.setText("01234567890");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputIsShorterThanSevenDigits_thenValidationFails() {
        phoneNumberField.setText("123456");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputIsValidWithSpaces_thenValidationPasses() {
        phoneNumberField.setText("0123 45 67");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenInputIsValidWithHyphens_thenValidationPasses() {
        phoneNumberField.setText("0123-45-67");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenInputOnlyContainsSevenDigits_thenValidationPasses() {
        phoneNumberField.setText("0123456");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenInputOnlyContainsTenDigits_thenValidationPasses() {
        phoneNumberField.setText("0123456789");
        final boolean validationResult = phoneNumberField.isValidInput();
        assertThat(validationResult, is(true));
    }

}
