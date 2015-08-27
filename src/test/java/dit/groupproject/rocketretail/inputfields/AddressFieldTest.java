package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link AddressField}.
 */
public class AddressFieldTest {

    private final static String BLANK_INPUT = "";
    private final static String VALID_INPUT = "Some text";

    private AddressField addressField = new AddressField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        addressField.setText(BLANK_INPUT);
        final boolean validationResult = addressField.isValidInput();
        assertThat(String.format("Input [%s] passed validation", BLANK_INPUT), validationResult, is(false));
    }

    @Test
    public void whenNonEmptyStringIsEntered_thenValidationPasses() {
        addressField.setText(VALID_INPUT);
        final boolean validationResult = addressField.isValidInput();
        assertThat(String.format("Input [%s] failed validation", VALID_INPUT), validationResult, is(true));
    }

}
