package dit.groupproject.rocketretail.entities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Comparator;

/**
 * A class that is used to model a <code>Staff</code> member.
 */
public class Staff implements Entity {

    private final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,##0.00");

    private int staffId;
    private int staffPin;
    private int gender;
    private int staffLevel;
    private double wage;
    private String staffName;
    private String phoneNumber;
    private String address;
    private String dateAdded;

    public Staff(final int staffPin, final String staffName, final int gender, final String phoneNumber, final String address, final double wage,
            final int staffLevel, final String dateAdded) {
        this.staffId = IdManager.getStaffIdAndIncrement();
        this.staffPin = staffPin;
        this.staffName = staffName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.wage = wage;
        this.staffLevel = staffLevel;
        this.dateAdded = dateAdded;
    }

    @Override
    public int getId() {
        return staffId;
    }

    @Override
    public void setId(final int staffId) {
        this.staffId = staffId;
    }

    public int getStaffPin() {
        return staffPin;
    }

    public int getStaffLevel() {
        return staffLevel;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public double getWage() {
        return wage;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public int getGender() {
        return gender;
    }

    public static Comparator<Entity> getComparator(final String sortType) {
        if (sortType.equals("Name")) {
            return compareByName;
        } else if (sortType.equals("Address")) {
            return compareByAddress;
        } else if (sortType.equals("Wage")) {
            return compareByWage;
        } else if (sortType.equals("Level")) {
            return compareByLevel;
        } else if (sortType.equals("Date Added")) {
            return compareByDateAdded;
        } else {
            return compareById;
        }
    }

    private static Comparator<Entity> compareByName = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Staff) s1).getStaffName().compareToIgnoreCase(((Staff) s2).getStaffName());
        }
    };

    private static Comparator<Entity> compareByAddress = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Staff) s1).getAddress().compareToIgnoreCase(((Staff) s2).getAddress());
        }
    };

    private static Comparator<Entity> compareByWage = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return (int) (((Staff) s1).getWage() - ((Staff) s2).getWage());
        }
    };

    private static Comparator<Entity> compareByLevel = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Staff) s1).getStaffLevel() - ((Staff) s2).getStaffLevel();
        }
    };

    private static Comparator<Entity> compareByDateAdded = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            try {
                return DATE_FORMATTER.parse(((Staff) s1).getDateAdded()).compareTo(DATE_FORMATTER.parse(((Staff) s2).getDateAdded()));
            } catch (ParseException e) {
                return 0;
            }
        }
    };

    @Override
    public Object[] getData() {
        final Object[] data = new Object[getNumberOfFields()];
        data[0] = STAFF_ID_FORMATTER.format(staffId);
        data[1] = staffName;
        data[2] = gender == 1 ? "Male" : "Female";
        data[3] = phoneNumber;
        data[4] = address;
        data[5] = "€" + CURRENCY_FORMATTER.format(wage);
        data[6] = staffLevel == 1 ? "Manager" : "Employee";
        data[7] = dateAdded;

        return data;
    }

    @Override
    public int getNumberOfFields() {
        return 8;
    }
}