package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link NumberField}.
 */
public class NumberFieldTest {

    private NumberField numberField = new NumberField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        numberField.setText("");
        final boolean validationResult = numberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenStringWithLettersIsEntered_thenValidationFails() {
        numberField.setText("123w");
        final boolean validationResult = numberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenStringWithSymbolIsEntered_thenValidationFails() {
        numberField.setText("123#");
        final boolean validationResult = numberField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenDigitsOnlyStringIsEntered_thenValidationPasses() {
        numberField.setText("123");
        final boolean validationResult = numberField.isValidInput();
        assertThat(validationResult, is(true));
    }
}
