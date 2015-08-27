package dit.groupproject.rocketretail.inputfields;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;

/**
 * A class that is used to test {@link StaffLevelField}.
 */
public class StaffLevelFieldTest {

    private StaffLevelField staffLevelField = new StaffLevelField();

    @Test
    public void whenEmptyInputIsSelected_thenValidationFails() {
        staffLevelField.setSelectedItem("");
        final boolean validationResult = staffLevelField.isValidInput();
        assertThat(validationResult, is(false));
    }

    @Test
    public void whenNonEmptyStringIsSelected_thenValidationPasses() {
        staffLevelField.setSelectedItem("Manager");
        final boolean validationResult = staffLevelField.isValidInput();
        assertThat(validationResult, is(true));
    }

}
