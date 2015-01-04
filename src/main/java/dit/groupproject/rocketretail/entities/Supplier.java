package dit.groupproject.rocketretail.entities;

import java.text.ParseException;
import java.util.Comparator;

/**
 * A class that is used to model a <code>Supplier</code>.
 */
public class Supplier implements Entity {

    private int supplierId;
    private String supplierName;
    private String phoneNumber;
    private String address;
    private String vatNumber;
    private String lastPurchase;
    private String dateAdded;

    public Supplier(final String supplierName, final String phoneNumber, final String address, final String vatNumber, final String lastPurchase,
            final String dateAdded) {
        this.supplierId = IdManager.getSupplierIdAndIncrement();
        this.supplierName = supplierName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.vatNumber = vatNumber;
        this.lastPurchase = lastPurchase;
        this.dateAdded = dateAdded;
    }

    public String printDetails() {
        return "Supplier ID:\t" + supplierId + "\nSupplier Name:\t" + supplierName + "\nPhone Number:\t" + phoneNumber + "\nAddress:\t" + address
                + "\nVAT Number:\t" + vatNumber + "\nLast Purchase:\t" + lastPurchase + "\nDate Added:\t" + dateAdded + "\n\n";
    }

    @Override
    public int getId() {
        return supplierId;
    }

    @Override
    public void setId(final int supplierId) {
        this.supplierId = supplierId;
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

    public static Comparator<Entity> getComparator(final String sortType) {
        if (sortType.equals("Name")) {
            return compareByName;
        } else if (sortType.equals("Address")) {
            return compareByAddress;
        } else if (sortType.equals("VAT Number")) {
            return compareByVatNumber;
        } else if (sortType.equals("Last Purchase")) {
            return compareByLastPurchaseDate;
        } else if (sortType.equals("Date Added")) {
            return compareByDateAdded;
        } else {
            return compareById;
        }
    }

    private static Comparator<Entity> compareByName = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Supplier) s1).getSupplierName().compareToIgnoreCase(((Supplier) s2).getSupplierName());
        }
    };

    private static Comparator<Entity> compareByAddress = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Supplier) s1).getAddress().compareToIgnoreCase(((Supplier) s2).getAddress());
        }
    };

    private static Comparator<Entity> compareByVatNumber = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Supplier) s1).getVatNumber().compareToIgnoreCase(((Supplier) s2).getVatNumber());
        }
    };

    private static Comparator<Entity> compareByLastPurchaseDate = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            try {
                return DATE_FORMATTER.parse(((Supplier) s1).getLastPurchase()).compareTo(DATE_FORMATTER.parse(((Supplier) s2).getLastPurchase()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    private static Comparator<Entity> compareByDateAdded = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            try {
                return DATE_FORMATTER.parse(((Supplier) s1).getDateAdded()).compareTo(DATE_FORMATTER.parse(((Supplier) s2).getDateAdded()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    @Override
    public Object[] getData() {
        final Object[] data = new Object[getNumberOfFields()];
        data[0] = SUPPLIER_ID_FORMATTER.format(supplierId);
        data[1] = supplierName;
        data[2] = phoneNumber;
        data[3] = address;
        data[4] = vatNumber;
        data[5] = lastPurchase;
        data[6] = dateAdded;

        return data;
    }

    @Override
    public int getNumberOfFields() {
        return 7;
    }
}
