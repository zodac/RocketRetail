package dit.groupproject.rocketretail.utilities;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * JTextFieldLimit is a class which extends PlainDocument and allows an
 * application to limit the number of characters in a JTextField or
 * JPasswordField.
 */
@SuppressWarnings("serial")
public class JTextFieldLimit extends PlainDocument {

    private int maximumCharactersAllowed;

    /**
     * Sets the character limit of the input field.
     * 
     * @param maximumCharactersAllowed
     *            an Integer which defines the maximum characters allowed
     */
    public JTextFieldLimit(final int maximumCharactersAllowed) {
        super();
        this.maximumCharactersAllowed = maximumCharactersAllowed;
    }

    /**
     * Takes input characters from a JTextField or JPasswordField and places
     * them in a new String. Restricts string size to the previously set
     * variable limit.
     * 
     * @param indexToPlaceCharacter
     *            an Integer defining the location to place the input character
     * @param stringToUpdate
     *            the String the input characters are placed into
     * @param attributeSet
     *            the AttributeSet (font, size, etc) of the string
     * 
     * @throws BadLocationException
     *             if accessing incorrect part of input string
     */
    public void insertString(final int indexToPlaceCharacter, String stringToUpdate, AttributeSet attributeSet) throws BadLocationException {
        if ((getLength() + stringToUpdate.length()) <= maximumCharactersAllowed) {
            super.insertString(indexToPlaceCharacter, stringToUpdate, attributeSet);
        }
    }
}