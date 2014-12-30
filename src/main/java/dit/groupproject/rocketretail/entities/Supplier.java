package dit.groupproject.rocketretail.entities;

/**
 * A class that is used to model a <code>Supplier</code>.
 */
public class Supplier {

    private int supplierId;
    private String supplierName, phoneNumber, address, vatNumber;
    private String lastPurchase, dateAdded;

    public Supplier(final String supplierName, final String phoneNumber, final String address, final String vatNumber,
            final String lastPurchase, final String dateAdded) {
        this.supplierId = IdManager.getSupplierIdAndIncrement();
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.vatNumber = vatNumber;
        this.lastPurchase = lastPurchase;
        this.dateAdded = dateAdded;
    }

    public String printDetails() {
        return "Supplier ID:\t" + supplierId + "\nSupplier Name:\t" + supplierName + "\nPhone Number:\t" + phoneNumber
                + "\nAddress:\t" + address + "\nVAT Number:\t" + vatNumber + "\nLast Purchase:\t" + lastPurchase
                + "\nDate Added:\t" + dateAdded + "\n\n";
    }

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
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
