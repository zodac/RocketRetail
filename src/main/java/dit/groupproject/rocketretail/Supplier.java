package dit.groupproject.rocketretail;

/**
 * A class that is used to model a <code>Product</code>
 */
public class Supplier {
    private int supplierID;
    private String supplierName, phoneNumber, address, vatNumber;
    private String lastPurchase, dateAdded;

    /**
     * Creates the Product object.
     * 
     * @param supplierID
     *            (int) the unique identifier for suppliers
     * @param supplierName
     *            (String) name of the supplier
     * @param phoneNumber
     *            (String) suppliers telephone number
     * @param address
     *            (String) suppliers address
     * @param vatNumber
     *            (String) the unique vatnumber for suppliers
     * @param lastPurchase
     *            (String) the last date product were ordered from the supplier
     * @param dateAdded
     *            (String) the date the supplier was added
     * */
    public Supplier(int supplierID, String supplierName, String phoneNumber, String address, String vatNumber,
            String lastPurchase, String dateAdded) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.vatNumber = vatNumber;
        this.lastPurchase = lastPurchase;
        this.dateAdded = dateAdded;
    }

    /**
     * Prints Supplier details to console.
     * */
    public String printDetails() {
        String output = "Supplier ID:\t" + supplierID + "\nSupplier Name:\t" + supplierName + "\nPhone Number:\t"
                + phoneNumber + "\nAddress:\t" + address + "\nVAT Number:\t" + vatNumber + "\nLast Purchase:\t"
                + lastPurchase + "\nDate Added:\t" + dateAdded + "\n\n";

        return output;
    }

    /**
     * returns the supplierID (int)
     * 
     * @return supplierID (int)
     * */
    public int getSupplierID() {
        return supplierID;
    }

    /**
     * changes the supplierID attribute
     * 
     * @param supplierID
     *            (int)
     * */
    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    /**
     * returns the supplierName (String)
     * 
     * @return supplierName (String)
     * */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * changes the supplierName attribute
     * 
     * @param supplierName
     *            (String)
     * */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * returns the phoneNumber (String)
     * 
     * @return phoneNumber (String)
     * */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * changes the phoneNumber attribute
     * 
     * @param phoneNumber
     *            (String)
     * */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * returns the address (String)
     * 
     * @return address (String)
     * */
    public String getAddress() {
        return address;
    }

    /**
     * changes the address attribute
     * 
     * @param address
     *            (String)
     * */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * returns the vatNumber (String)
     * 
     * @return vatNumber (String)
     * */
    public String getVatNumber() {
        return vatNumber;
    }

    /**
     * changes the vatNumber attribute
     * 
     * @param vatNumber
     *            (String)
     * */
    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    /**
     * returns the lastPurchase (String)
     * 
     * @return lastPurchase (String)
     * */
    public String getLastPurchase() {
        return lastPurchase;
    }

    /**
     * changes the lastPurchase attribute
     * 
     * @param lastPurchase
     *            (String)
     * */
    public void setLastPurchase(String lastPurchase) {
        this.lastPurchase = lastPurchase;
    }

    /**
     * returns the dateAdded (String)
     * 
     * @return dateAdded (String)
     * */
    public String getDateAdded() {
        return dateAdded;
    }

    /**
     * changes the dateAdded attribute
     * 
     * @param dateAdded
     *            (String)
     * */
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
