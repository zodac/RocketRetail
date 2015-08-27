package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link NameField}.
 */
public class NameFieldTest {

    private NameField nameField = new NameField();

    @Test
    public void whenEmptyStringIsEntered_thenValidationFails() {
        nameField.setText("");
        final boolean validationResult = nameField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenSurnameDoesNotStartWithCapitalLetter_thenValidationFails() {
        nameField.setText("Firstname lastname");
        final boolean validationResult = nameField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenNameContainsNoLetters_thenValidationFails() {
        nameField.setText("1234 !£$^");
        final boolean validationResult = nameField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenNameHasNoSurname_thenValidationFails() {
        nameField.setText("Firstname");
        final boolean validationResult = nameField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenNameHasSurname_thenValidationPasses() {
        nameField.setText("Firstname Surname");
        final boolean validationResult = nameField.isValidInput();
        assertThat(validationResult, is(true));
    }

    @Test
    public void whenNameHasSurnameAndMiddleName_thenValidationPasses() {
        nameField.setText("Firstname Middlename Surname");
        final boolean validationResult = nameField.isValidInput();
        assertThat(validationResult, is(true));
    }

}
