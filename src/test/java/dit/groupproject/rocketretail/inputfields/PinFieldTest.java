package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link PinField}.
 */
public class PinFieldTest {

    private PinField pinField = new PinField();

    @Test
    public void whenPinHasLetters_thenValidationFails() {
        pinField.setText("abcd");
        final boolean validationResult = pinField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenPinHasSymbols_thenValidationFails() {
        pinField.setText("#+=!");
        final boolean validationResult = pinField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenPinIsShorterThanFourDigits_thenValidationFails() {
        pinField.setText("123");
        final boolean validationResult = pinField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenPinIsLongerThanFourDigits_thenValidationFails() {
        pinField.setText("12345");
        final boolean validationResult = pinField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenPinIsFourDigitsAndHasOnlyNumbers_thenValidationPasses() {
        pinField.setText("1234");
        final boolean validationResult = pinField.isValidInput();
        assertThat(validationResult, is(true));
    }
}
