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

    public static Comparator<Customer> getComparator(final String sortType) {
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

    private static Comparator<Customer> compareById = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getId() - s2.getId();
        }
    };

    private static Comparator<Customer> compareByName = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getCustomerName().compareToIgnoreCase(s2.getCustomerName());
        }
    };

    private static Comparator<Customer> compareByAddress = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getAddress().compareToIgnoreCase(s2.getAddress());
        }
    };

    private static Comparator<Customer> compareByVatNumber = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getVatNumber().compareToIgnoreCase(s2.getVatNumber());
        }
    };

    private static Comparator<Customer> compareByLastPurchaseDate = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            try {
                return DATE_FORMATTER.parse(s1.getLastPurchase()).compareTo(DATE_FORMATTER.parse(s2.getLastPurchase()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    private static Comparator<Customer> compareByDateAdded = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            try {
                return DATE_FORMATTER.parse(s1.getDateAdded()).compareTo(DATE_FORMATTER.parse(s2.getDateAdded()));
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
