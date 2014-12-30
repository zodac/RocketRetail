package shopFront;
/**
 * A class that is used to model
 * a <code>Customer</code>
 * 
 * @author Sheikh
 * @author Tony
 * @author Alan
 * @author Jessica
 * @author Jason
 * @version 2.0
 * @since 1.0
 * 
 */
public class Customer {
	/**
	 *  The customer id.
	 */
 	private int customerID;
 	/**
	 *  The customer's name.
	 */
	private String customerName;
	/**
	 *  The customer's phone number. 
	 */
	private String phoneNumber; 
	/**
	 *  The customer's address.
	 */
	private String address;
	/**
	 *  The customer's vat number.
	 */
	private String vatNumber;
	/**
	 *  The customer's last purchase date.
	 */
	private String lastPurchase;
	/**
	 *  The customer's add date to the system.
	 */
	private String dateAdded;	
	
	
	/**
	 * Constructs a new <code>Customer</code> with the given parameters.
	 * 
	 * @param customerID
	 * @param customerName
	 * @param phoneNumber
	 * @param address
	 * @param vatNumber
	 * @param lastPurchase
	 * @param dateAdded
	 */
	public Customer(int customerID, String customerName, String phoneNumber, String address, String vatNumber, String lastPurchase, String dateAdded){
		this.customerID = customerID;
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.vatNumber = vatNumber;
		this.lastPurchase = lastPurchase;
		this.dateAdded = dateAdded;
	}
	
	/**
	 * This method prints out the <code>Customer</code>'s details.
	 * 
	 * @return A <code>String</code> with a <code>Customer</code>'s details.
	 */
	public String printDetails(){
		String output = "Customer ID:\t" + customerID +
						"\nCustomer Name:\t" + customerName +
						"\nPhone Number:\t" + phoneNumber +
						"\nAddress:\t" + address + 
						"\nVat Number:\t" + vatNumber +
						"\nLast Purchase:\t" + lastPurchase +
						"\nDate Added:\t" + dateAdded + "\n\n";
						
		return output;
	}

	/**
	 * @return The <code>Customer</code> id.
	 */
	public int getCustomerID() {
		return customerID;
	}
    /**
     * @param customerID Sets the <code>Customer</code> ID.
     */
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	/**
	 * @return The <code>Customer</code> name.
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
     * @param customerName Sets the <code>Customer</code> name.
     */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return The <code>Customer</code> phone number.
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
     * @param phoneNumber Sets the <code>Customer</code> phone number.
     */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return The <code>Customer</code> address.
	 */
	public String getAddress() {
		return address;
	}
	/**
     * @param address Sets the <code>Customer</code> address.
     */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return The <code>Customer</code> vat number.
	 */
	public String getVatNumber() {
		return vatNumber;
	}
	/**
     * @param vatNumber Sets the customer vat number
     */
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	/**
	 * @return The <code>Customer</code> last purchase date.
	 */
	public String getLastPurchase() {
		return lastPurchase;
	}
	 /**
     * @param lastPurchase Sets the <code>Customer</code> last purchase date.
     */	
	public void setLastPurchase(String lastPurchase) {
		this.lastPurchase = lastPurchase;
	}
	/**
	 * @return The <code>Customer</code> add date.
	 */
	public String getDateAdded() {
		return dateAdded;
	}
	 /**
     * @param dateAdded Sets the <code>Customer</code> add date.
     */		
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
}
