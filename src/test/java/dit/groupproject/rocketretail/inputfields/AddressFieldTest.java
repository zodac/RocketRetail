package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link AddressField}.
 */
public class AddressFieldTest {

    private AddressField addressField = new AddressField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        addressField.setText("");
        final boolean validationResult = addressField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenNonEmptyStringIsEntered_thenValidationPasses() {
        addressField.setText("Some text");
        final boolean validationResult = addressField.isValidInput();
        assertThat(validationResult, is(true));
    }

}
