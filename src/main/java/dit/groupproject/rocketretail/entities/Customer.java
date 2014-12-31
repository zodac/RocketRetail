package dit.groupproject.rocketretail.entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * A class that is used to model a <code>Customer</code>.
 */
public class Customer {

    private final static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

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

    public static Comparator<Customer> compareById = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getCustomerId() - s2.getCustomerId();
        }
    };

    public static Comparator<Customer> compareByName = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getCustomerName().compareToIgnoreCase(s2.getCustomerName());
        }
    };

    public static Comparator<Customer> compareByAddress = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getAddress().compareToIgnoreCase(s2.getAddress());
        }
    };

    public static Comparator<Customer> compareByVatNumber = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            return s1.getVatNumber().compareToIgnoreCase(s2.getVatNumber());
        }
    };

    public static Comparator<Customer> compareByLastPurchaseDate = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            try {
                return DATE_FORMATTER.parse(s1.getLastPurchase()).compareTo(DATE_FORMATTER.parse(s2.getLastPurchase()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    public static Comparator<Customer> compareByDateAdded = new Comparator<Customer>() {
        public int compare(final Customer s1, final Customer s2) {
            try {
                return DATE_FORMATTER.parse(s1.getDateAdded()).compareTo(DATE_FORMATTER.parse(s2.getDateAdded()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };
}
