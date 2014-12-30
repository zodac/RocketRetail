package dit.groupproject.rocketretail.entities;

import java.text.DecimalFormat;

/**
 * A class that is used to model a <code>Staff</code> member.
 */
public class Staff {

    private final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,##0.00");

    private int staffId, staffPin, gender, staffLevel;
    private double wage;
    private String staffName, phoneNumber, address;
    private String dateAdded;

    public Staff(final int staffPin, final String staffName, final int gender, final String phoneNumber,
            final String address, final double wage, final int staffLevel, final String dateAdded) {
        this.staffId = IdManager.getStaffIdAndIncrement();
        this.staffPin = staffPin;
        this.staffName = staffName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.wage = wage;
        this.staffLevel = staffLevel;
        this.dateAdded = dateAdded;

        System.out.println("Adding " + staffName + ", with ID: " + staffId);
    }

    public String printDetails() {
        final String level = (staffLevel == 1) ? "Manager" : "Employee";

        return "ID:\t\t" + staffId + "\nStaff Name:\t" + staffName + "\nPhone Number:\t" + phoneNumber + "\nAddress:\t"
                + address + "\nWage:\t\t€" + CURRENCY_FORMATTER.format(wage) + "\nStaff Level:\t" + level
                + "\nDate Added:\t" + dateAdded + "\n\n";
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(final int staffId) {
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
}