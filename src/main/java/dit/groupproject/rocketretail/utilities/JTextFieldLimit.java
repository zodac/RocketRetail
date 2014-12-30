package dit.groupproject.rocketretail.utilities;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * JTextFieldLimit is a class which extends PlainDocument and allows an
 * application to limit the number of characters in a JTextField or
 * JPasswordField.
 */
public class JTextFieldLimit extends PlainDocument {

    // Class variables
    /**
     * Default version ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * An Integer which defines the maximum characters allowed. Set by
     * {@link #JTextFieldLimit(int)}.
     * 
     * @see #JTextFieldLimit(int)
     */
    private int limit;

    // Methods
    /**
     * Sets the character limit of the input field.
     * 
     * @param limit
     *            an Integer which defines the maximum characters allowed
     */
    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    /**
     * Takes input characters from a JTextField or JPasswordField and places
     * them in a new String. Restricts string size to the previously set
     * variable limit.
     * 
     * @param offset
     *            an Integer defining the location to place the input character
     * @param str
     *            the String the input characters are placed into
     * @param attr
     *            the AttributeSet (font, size, etc) of the string
     * 
     * @throws BadLocationException
     *             if accessing incorrect part of input string
     * 
     * @see #JTextFieldLimit(int)
     */
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if ((getLength() + str.length()) <= limit)
            super.insertString(offset, str, attr);
    }
}