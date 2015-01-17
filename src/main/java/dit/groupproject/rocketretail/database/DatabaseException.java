package dit.groupproject.rocketretail.database;

@SuppressWarnings("serial")
public class DatabaseException extends IllegalArgumentException {

    public DatabaseException() {

    }

    public DatabaseException(final String message) {
        super(message);
    }
}
