package dit.groupproject.rocketretail.entities;


/**
 * A class that is used to model a <code>Customer</code>.
 */
public class Customer {

    private int customerId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String vatNumber;
    private String lastPurchase;
    private String dateAdded;

    public Customer(final String customerName, final String phoneNumber, final String address, final String vatNumber,
            final String lastPurchase, final String dateAdded) {
        this.customerId = IdManager.getCustomerIdAndIncrement();
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.vatNumber = vatNumber;
        this.lastPurchase = lastPurchase;
        this.dateAdded = dateAdded;
    }

    public String printDetails() {
        return "Customer ID:\t" + customerId + "\nCustomer Name:\t" + customerName + "\nPhone Number:\t" + phoneNumber
                + "\nAddress:\t" + address + "\nVat Number:\t" + vatNumber + "\nLast Purchase:\t" + lastPurchase
                + "\nDate Added:\t" + dateAdded + "\n\n";
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public String getLastPurchase() {
        return lastPurchase;
    }

    public void setLastPurchase(final String lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
