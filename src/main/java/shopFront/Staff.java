package shopFront;
import java.text.DecimalFormat; 
/**
 * A class that is used to model
 * a <code>Staff</code>
 */
public class Staff { 
	
    private int staffID, staffPIN, gender, staffLevel; //For gender, male = 1, female = 2. No, that's not a hidden message
    private double wage; 
    private String staffName, phoneNumber, address; 
    private String dateAdded;
    
    //Formatters 
    /**
	 * A DecimalFormatter which formats Integers into Strings with the given formatting.
	 * Formats doubles to have commas, always show to two decimal places, and have at least two
	 * digits before the decimal point.
	 */
    static DecimalFormat doubleFormatter = new DecimalFormat("#,###,##0.00"); 
    
    /**
     * Creates the Staff object.
     * 
     * @param staffID (int) the unique identifier for staff member
     * @param staffPIN (int) the password the staff member uses to log in
     * @param staffName (String) the staff members name
     * @param gender (int) gender of staff member -male (1) or female (2)
     * @param phoneNumber (String) the phone number of the staff member
     * @param address (String) the address of the staff member
     * @param wage (double) the wage the staff member earns
     * @param staffLevel (int) the staff members level -manager (1) or employee (2)
     * @param dateAdded (String) the date the staff member was added to the system 
     * */
    public Staff(int staffID, int staffPIN, String staffName, int gender, String phoneNumber, String address, double wage, int staffLevel, String dateAdded){ 
        this.staffID = staffID; 
        this.staffPIN = staffPIN; 
        this.staffName = staffName; 
        this.gender = gender;
        this.phoneNumber = phoneNumber; 
        this.address = address; 
        this.wage = wage; 
        this.staffLevel = staffLevel; 
        this.dateAdded = dateAdded; 
    } 
    
    /**
     * Prints staff members details to console.
     * */
    public String printDetails(){
    	
        String level = null; 
        if(staffLevel == 1) 
            level = "Manager"; 
        else if (staffLevel == 2) 
            level = "Employee"; 
          
        String output = "ID:\t\t" + staffID + 
                        "\nStaff Name:\t" + staffName +  
                        "\nPhone Number:\t" + phoneNumber +  
                        "\nAddress:\t" + address +  
                        "\nWage:\t\t€" + doubleFormatter.format(wage) +  
                        "\nStaff Level:\t" + level +  
                        "\nDate Added:\t" + dateAdded + "\n\n";
        return output; 
    } 
      
    //Getters & Setters 
    /**
     * returns the staffID (int)
     * @return staffID (int)
     * */
    public int getStaffID() { 
        return staffID; 
    } 
    /**
     * changes the staffID attribute
     * @param staffID (int)
     * */
    public void setStaffID(int staffID) { 
        this.staffID = staffID; 
    } 
    /**
     * returns the staffPIN (int)
     * @return staffPIN (int)
     * */
    public int getStaffPIN() { 
        return staffPIN; 
    } 
    /**
     * changes the staffPIN attribute
     * @param staffPIN (int)
     * */
    public void setStaffPIN(int staffPIN) { 
        this.staffPIN = staffPIN; 
    } 
    /**
     * returns the staffLevel (int)
     * @return staffLevel 
     * */
    public int getStaffLevel() { 
        return staffLevel; 
    } 
    /**
     * changes the staffLevel
     * @param staffLevel (int)
     * */
    public void setStaffLevel(int staffLevel) { 
        this.staffLevel = staffLevel; 
    } 
    /**
     * returns the staffName attribute (String)
     * @return staffName (String)
     * */
    public String getStaffName() { 
        return staffName; 
    } 
    /**
     * changes the staffName
     * @param staffName (String)
     * */
    public void setStaffName(String staffName) { 
        this.staffName = staffName; 
    } 
    /**
     * returns the phoneNumber attribute (String)
     * @return phoneNumber (String)
     * */
    public String getPhoneNumber() { 
        return phoneNumber; 
    } 
    /**
     * changes the phoneNumber attribute
     * @param phoneNumber (String)
     * */
    public void setPhoneNumber(String phoneNumber) { 
        this.phoneNumber = phoneNumber; 
    } 
    /**
     * returns the address attribute (String)
     * @return address (String)
     * */
    public String getAddress() { 
        return address; 
    } 
    /**
     * changes the address attribute (String)
     * @param address (String)
     * */
    public void setAddress(String address) { 
        this.address = address; 
    } 
    /**
     * returns the wage attribute (double)
     * @return wage (double)
     * */
    public double getWage() { 
        return wage; 
    } 
    /**
     * changes the wage attribute (String)
     * @param wage (double)
     * */
    public void setWage(double wage) { 
        this.wage = wage; 
    } 
    /**
     * returns the dateAdded attribute (String)
     * @return dateAdded (String)
     * */
    public String getDateAdded() { 
        return dateAdded; 
    } 
    /**
     * changes the dateAdded attribute (String)
     * @param dateAdded (String)
     * */
    public void setDateAdded(String dateAdded) { 
        this.dateAdded = dateAdded; 
    }
    /**
     * returns the gender attribute (int)
     * @return gender (int)
     * */
	public int getGender() {
		return gender;
	}
	/**
	 * changes the gender attribute
	 * @param gender (int)
	 * */
	public void setGender(int gender) {
		this.gender = gender;
	} 
      
} 