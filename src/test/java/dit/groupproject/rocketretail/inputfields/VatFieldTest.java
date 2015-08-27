package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link VatField}.
 */
public class VatFieldTest {

    private VatField vatField = new VatField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        vatField.setText("");
        final boolean validationResult = vatField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputDoesNotHaveSevenDigits_thenValidationFails() {
        vatField.setText("A123456");
        final boolean validationResult = vatField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputDoesNotStartWithALetter_thenValidationFails() {
        vatField.setText("11234567");
        final boolean validationResult = vatField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputContainsSymbols_thenValidationFails() {
        vatField.setText("A*234567");
        final boolean validationResult = vatField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenInputStartsWithALetterAndHasSevenDigits_thenValidationPasses() {
        vatField.setText("A1234567");
        final boolean validationResult = vatField.isValidInput();
        assertThat(validationResult, is(true));
    }
}
