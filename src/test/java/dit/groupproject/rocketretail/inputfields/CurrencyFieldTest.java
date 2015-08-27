package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link CurrencyField}.
 */
public class CurrencyFieldTest {

    private CurrencyField currencyField = new CurrencyField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        currencyField.setText("");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenStringWithLettersIsEntered_thenValidationFails() {
        currencyField.setText("123w");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenStringWithSymbolIsEntered_thenValidationFails() {
        currencyField.setText("123#");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenDigitsOnlyStringIsEntered_thenValidationPasses() {
        currencyField.setText("123");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenDigitsWithEuroSymbolStringIsEntered_thenValidationPasses() {
        currencyField.setText("€123");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenDigitsWithCommaStringIsEntered_thenValidationPasses() {
        currencyField.setText("1,234");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenDigitsWithEuroSymbolAndCommaStringIsEntered_thenValidationPasses() {
        currencyField.setText("€1,234");
        final boolean validationResult = currencyField.isValidInput();
        assertThat(validationResult, is(true));
    }
}
