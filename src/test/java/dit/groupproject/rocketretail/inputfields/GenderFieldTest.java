package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link GenderField}.
 */
public class GenderFieldTest {

    private GenderField genderField = new GenderField();

    @Test
    public void whenEmptyInputIsSelected_thenValidationFails() {
        genderField.setSelectedItem("");
        final boolean validationResult = genderField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenNonEmptyStringIsSelected_thenValidationPasses() {
        genderField.setSelectedItem("Male");
        final boolean validationResult = genderField.isValidInput();
        assertThat(validationResult, is(true));
    }
}
