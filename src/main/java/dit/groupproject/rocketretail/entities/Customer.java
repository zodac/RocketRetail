package dit.groupproject.rocketretail.entities;

import java.text.ParseException;
import java.util.Comparator;

/**
 * A class that is used to model a <code>Customer</code>.
 */
public class Customer implements Entity {

    private int customerId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String vatNumber;
    private String lastPurchase;
    private String dateAdded;

    public Customer(final String customerName, final String phoneNumber, final String address, final String vatNumber, final String lastPurchase,
            final String dateAdded) {
        this.customerId = IdManager.getCustomerIdAndIncrement();
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.vatNumber = vatNumber;
        this.lastPurchase = lastPurchase;
        this.dateAdded = dateAdded;
    }

    @Override
    public int getId() {
        return customerId;
    }

    @Override
    public void setId(final int customerId) {
        this.customerId = customerId;
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

    private static Comparator<Entity> compareById = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return s1.getId() - s2.getId();
        }
    };

    private static Comparator<Entity> compareByName = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Customer) s1).getCustomerName().compareToIgnoreCase(((Customer) s2).getCustomerName());
        }
    };

    private static Comparator<Entity> compareByAddress = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Customer) s1).getAddress().compareToIgnoreCase(((Customer) s2).getAddress());
        }
    };

    private static Comparator<Entity> compareByVatNumber = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Customer) s1).getVatNumber().compareToIgnoreCase(((Customer) s2).getVatNumber());
        }
    };

    private static Comparator<Entity> compareByLastPurchaseDate = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            try {
                return DATE_FORMATTER.parse(((Customer) s1).getLastPurchase()).compareTo(DATE_FORMATTER.parse(((Customer) s2).getLastPurchase()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    private static Comparator<Entity> compareByDateAdded = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            try {
                return DATE_FORMATTER.parse(((Customer) s1).getDateAdded()).compareTo(DATE_FORMATTER.parse(((Customer) s2).getDateAdded()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    @Override
    public Object[] getData() {
        final Object[] data = new Object[getNumberOfFields()];
        data[0] = CUSTOMER_ID_FORMATTER.format(customerId);
        data[1] = customerName;
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
