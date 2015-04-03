package dit.groupproject.rocketretail.inputfields;

@SuppressWarnings("serial")
public class IdField extends BaseField {

    private final static int INPUT_FIELD_LENGTH = 20;

    public IdField() {
        super(INPUT_FIELD_LENGTH);
        this.setEditable(false);
    }
}
