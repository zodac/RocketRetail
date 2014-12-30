package dit.groupproject.rocketretail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

/**
 * The main driver for the store program.
 *
 */
public class ShopDriver{
	
	//Class variables
	/**
	 * A Color object containing the colour the GUI background is set to.
	 */
	public static Color backgroundColour = new Color(170, 209, 243); //Blue
//	public static Color backgroundColour = new Color(224, 217, 233); //Pink
//	public static Color backgroundColour = new Color(238, 238, 238); //Default colour
	
	/**
	 * An array of Strings holding numeric values for the day (for dates).
	 */
	static String[] days =   { "", "01", "02","03","04","05",  "06",  "07", "08", "09", "10",
						   		   "11", "12","13", "14","15", "16", "17", "18", "19", "20",
						   		   "21","22", "23", "24", "25", "26", "27","28","29", "30", "31" };
	/**
	 * An array of Strings holding numeric values for the month (for dates).
	 */
	static String[] months = { "", "01","02","03","04","05",  "06",  "07", "08", "09","10", "11","12" };
	
	/**
	 * An array of Strings holding numeric values for the year (for dates). Also defines the start and end year of the store.
	 */
	static String[]	years =  { "", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
								   "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
								   "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" };
	
	/**
	 * An ArrayList of Staff objects
	 */
	static ArrayList<Staff> staffMembers = new ArrayList<Staff>();
	
	/**
	 * An ArrayList of Product objects
	 */
	static ArrayList<Product> products = new ArrayList<Product>();
	
	/**
	 * An ArrayList of Supplier objects
	 */
	static ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	
	/**
	 * An ArrayList of Customer objects
	 */
	static ArrayList<Customer> customers = new ArrayList<Customer>();
	
	/**
	 * An ArrayList of Order objects
	 */
	static ArrayList<Order> orders = new ArrayList<Order>();
	
	/**
	 * The frame containing all panels for the GUI.
	 */
	static JFrame frame = new JFrame();								//Main GUI frame
	
	/**
	 * The top panel of the frame. Displays the logo.
	 */
	static JPanel headerPanel;
	
	/**
	 * The central panel of the frame. Displays the JTables and Homescreen.
	 */
	static JPanel mainPanel;
	
	/**
	 * The left panel of the frame. Displays the fields for adding/editing.
	 */
	static JPanel leftPanel;
	
	/**
	 * The right panel of the frame. Displays the P/L reports.
	 */
	static JPanel rightPanel;
	
	/**
	 * The bottom panel of the frame. Displays the confirmation message.
	 */
	static JPanel bottomPanel;
	
	/**
	 * The JLabel which is set to a confirmation message on operation completion.
	 */
	static JLabel confirmationLabel;
	
	/**
	 * Integer defining the height for JTextAreas.
	 */
	static final int textAreaHeight = 23;
	
	/**
	 * Integer defining the width for JTextAreas.
	 */
	static final int textAreaWidth = 45;
	
	/**
	 * Integer defining the start year of the store.
	 */
	static final int yearStart = Integer.parseInt(years[1]);
	
	/**
	 * Integer defining the current year.
	 */
	static final int yearCurrent = Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(6, 10));
	
	/**
	 * Integer defining the start ID values for Suppliers.
	 */
	static final int supplierIDStart = 1000;
	
	/**
	 * Integer defining the start ID values for Customers.
	 */
	static final int customerIDStart = 10000;
	
	/**
	 * Integer defining the start ID values for Products.
	 */
	static final int productIDStart = 20000;
	
	/**
	 * A Staff object set to the current logged in staff member.
	 */
	static Staff currentStaff;
	
	/**
	 * A String holding the title of the current active Table in mainPanel.
	 */
	static String currentTable = "";
	
	
	//Methods
	/**
	 * The ShopDriver constructor. Called at the start of the program (again if user chooses to logout).<br />
	 * Calls the {@link #logon()} method, creates the menu-bar,
	 * and sets {@link #mainPanel} to the homescreen.<br />
	 * Then shows the GUI on-screen.
	 * 
	 * @see			#logon()
	 * @see			MenuGUI#createMenuBar(JMenuBar, boolean)
	 * @see			HomeScreen#setScreen()
	 * @see			#showGUI(JMenuBar)
	 */
	public ShopDriver(){		
		boolean manager = false; 									//Boolean used to enable/disable options in menuBar depending on staff member's clearance							
		
		logon();													//Authenticates user
		if(currentStaff.getStaffLevel() == 1)
			manager = true;											//Sets boolean to define options available
		
		JMenuBar menuBar = new JMenuBar();							//Create menuBar					
		MenuGUI.createMenuBar(menuBar, manager);					//Populate menuBar with menus, submenus & ActionListeners
		HomeScreen.setScreen();										//Open GUI at home page
		showGUI(menuBar);											//Display GUI with menuBar	
	}

	/**
	 * Creates and defines the GUI:
	 * <ul><li>Sets the "look and feel" to Windows default</li>
	 * <li>Sets the {@link #frame} size and title</li>
	 * <li>Initialises the JPanels</li>
	 * <li>Places the panels in {@link #frame}</li>
	 * <li>Sets the background colour of the panels and {@link #frame}</li>
	 * <li>Centers the GUI in the middle of the screen</li></ul> 
	 */
	public static void createGUI(){
		//Set "look and feel" to Windows default
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		//Set size and title
		frame.setSize(1150, 700);									//Width*Height
		frame.setTitle("Rocket Retail Inc");
		
		//Initialise panels
		JLabel headerLabel = new JLabel(new ImageIcon("src/res/rocketRetail.png"));
		headerPanel = new JPanel(new BorderLayout());
		headerPanel.add(headerLabel, BorderLayout.CENTER);
		headerPanel.setPreferredSize(new Dimension(750, 60));
		
		bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(750, 30));
		bottomPanel.setBackground(backgroundColour);
		confirmationLabel = new JLabel();
		confirmationLabel.setFont(new Font(confirmationLabel.getFont().getFontName(), Font.BOLD, confirmationLabel.getFont().getSize()));
		bottomPanel.add(confirmationLabel);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		
		frame.add(headerPanel, BorderLayout.NORTH);
		mainPanel = new JPanel(new BorderLayout(0, 2));
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		
		//Set the background colour
		frame.setBackground(backgroundColour);
		headerPanel.setBackground(backgroundColour);
		
		//Centers GUI on screen
        Insets i = frame.getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration());
        int maxWidth = (frame.getToolkit().getScreenSize().width - i.left - i.right);
        int maxHeight = (frame.getToolkit().getScreenSize().height - i.top - i.bottom);
        frame.setLocation((int) ((maxWidth - frame.getWidth()) / 2), (int) ((maxHeight - frame.getHeight() ) / 2));
	}
	
	/**
	 * Sets the JLabel {@link #confirmationLabel} (in {@link #bottomPanel}) to the input message.<br />
	 * Includes a timer which hides the message after 4000 milliseconds.
	 * 
	 * @param title			a String holding the message to display on-screen
	 */
	public static void setConfirmMessage(String title){
		confirmationLabel.setText(title);
		Timer t = new Timer(4000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                confirmationLabel.setText("");
            }
        });
		t.setRepeats(false);
		t.start();	
	}
	
	/**
	 * Adds menu-bar from {@link #ShopDriver()} to the frame.<br />
	 * Displays GUI on-screen.
	 * 
	 * @param menuBar		the menuBar to be added to the screen
	 */
	public void showGUI(JMenuBar menuBar){
		//Prepare frame for display
		frame.setJMenuBar(menuBar); 								//Add menuBar to frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//Exit application on frame close
		frame.setVisible(true);    									//Show frame
	}
	
	/**
	 * Creates a {@link #showDialog(String, JPanel)} pop-up asking for a user's ID and PIN. 
	 * Matches ID and PIN to values in {@link #staffMembers}.<br />
	 * After three unsuccessfully attempts, the program closes.
	 */
	public static void logon(){
		JPanel myPanel = new JPanel();
		int count = 0;												//Counter for attempts
		JTextField idField = new JTextField(5);						//JTextField for ID input
		JPasswordField pinField = new JPasswordField(5);			//JPasswordField for PIN input
		boolean found = false;
		
		//Restricts input fields to certain character length
		idField.setDocument(new JTextFieldLimit(3));
		pinField.setDocument(new JTextFieldLimit(4));

		//Add JLabels and JTextFields to panel
	    myPanel.add(new JLabel("Staff ID:"));
	    myPanel.add(idField);
	    myPanel.add(new JLabel("Staff PIN:"));
	    myPanel.add(pinField);
	    JLabel errorLabel = new JLabel();
	    		
	    while(count < 3 && !found){									//Allows you to attempt a login three times
	    	if(count == 1){
	    		myPanel.add(errorLabel);
	    		errorLabel.setText("2 attempts remaining");
	    	}
	    	else if(count == 2)
	    		errorLabel.setText("1 attempt remaining");
	    	
	        if (showDialog("Please enter your staff ID and PIN", myPanel) == JOptionPane.OK_OPTION) {
	        	for(Staff s : staffMembers){ 
	        		if(s.getStaffID() == Integer.parseInt(idField.getText()) && s.getStaffPIN() == Integer.parseInt(String.valueOf(pinField.getPassword()))){
	        			currentStaff = s;
	        			found = true;
	        		}
	        	}
	        }
	        else System.exit(0);									//If user cancels, the program closes
	        count++;												//If incorrect, count increments
	    }
	    if(!found){
		    JOptionPane.showMessageDialog(null, "Too many incorrect logon attempts!", "Logon Fail", JOptionPane.PLAIN_MESSAGE); //Prompts user before closing
		    System.exit(0);											//Closes after three incorrect attempts
	    }
	}
	
	/** 
	 * Replaces the {@link JOptionPane#showConfirmDialog(Component, Object, String, int, int, Icon)}
	 * pop-up with a simpler one.<br />
	 * Reduces the amount of code needed for dialog boxes.
	 * 
	 * @param title			a String containing the dialog title
	 * @param myPanel		a JPanel displayed in the dialog
	 * 
	 * @return				an Integer, set to 0 if "Ok" is pressed, or 2 is "Cancel" is pressed
	 */
	public static int showDialog(String title, JPanel myPanel){
		return JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
	}
	
	/**
	 * Resets ArrayLists, then calls methods in {@link InitialiseArray} to populate them.
	 * 
	 * @see				InitialiseArray#addStaff()
	 * @see				InitialiseArray#addSuppliers()
	 * @see				InitialiseArray#addProducts()
	 * @see				InitialiseArray#addCustomers()
	 * @see				InitialiseArray#addOrders(boolean)
	 */
	public static void initialiseArrays(){
		staffMembers.clear();
		InitialiseArray.addStaff();
		suppliers.clear();
		InitialiseArray.addSuppliers();
		products.clear();
		InitialiseArray.addProducts();
		customers.clear();
	    InitialiseArray.addCustomers();
	    orders.clear();
		InitialiseArray.addOrders(false);
		InitialiseArray.addOrders(true);
	}
	
	/**
	 * If relevant boolean is set to true, resets a panel ({@link #mainPanel}, {@link #leftPanel} or {@link #rightPanel}),
	 * then updates the frame.
	 * 
	 * @param left			a boolean used to reset {@link #leftPanel}
	 * @param right			a boolean used to reset {@link #rightPanel}
	 * @param main			a boolean used to reset {@link #mainPanel}
	 */
	public static void setFrame(boolean left, boolean right, boolean main){
		
		if(main){
			frame.add(mainPanel, BorderLayout.CENTER);
			mainPanel.setBackground(backgroundColour);
		}
		if(left){
			frame.add(leftPanel, BorderLayout.WEST);
			leftPanel.setBackground(backgroundColour);
		}
		if(right){
			frame.add(rightPanel, BorderLayout.EAST);
			rightPanel.setBackground(backgroundColour);
		}
		
		frame.validate();
	}
	
	/** Method which allows user to logout, and restarts application at the logon screen */
	public static void Logout(){
		frame.dispose();											//Close frame
		leftPanel.removeAll();										//Reset panels
		rightPanel.removeAll();
		
		@SuppressWarnings("unused")
		ShopDriver t = new ShopDriver();							//Create new ShopDriver object, which re-calls the constructor
	}
	
	/**
	 * Checks set of input fields for invalid entries. Returns boolean determined by validity of fields - 
	 * true if not invalid entries, false if any invalid entries and sets a red border around invalid field.<br />
	 * Checks for:
	 * <ul><li>JTextField (String) - checks for empty fields</li>
	 * <li>JTextField (Integer) - checks for empty fields, or fields with non-Integer inputs</li>
	 * <li>JTextField (Double) - checks for empty fields, or fields with non-Double inputs</li>
	 * <li>JPasswordField - checks for empty fields, or field with any non-numeric characters</li>
	 * <li>JComboBox (Normal) - checks for empty selection</li>
	 * <li>JComboBox (Date) - checks for empty selections, or invalid dates (e.g., 29th February, 31st April, etc.)</li></ul>
	 * 
	 * @param textFields			An ArrayList of JTextFields (String)
	 * @param intFields				An ArrayList of JTextFields (Integer)
	 * @param doubleFields			An ArrayList of JTextFields (Double)
	 * @param pinFields				An ArrayList of JPasswordFields
	 * @param comboBoxes			An ArrayList of JComboBoxes (Normal)
	 * @param addedBoxes			An ArrayList of JComboBoxes (Date)
	 * @param lastPurchaseBoxes		An ArrayList of JComboBoxes (Date)
	 * 
	 * @return						a boolean set to true if no invalid entries, or else false
	 */
	public static boolean checkFields(ArrayList<JTextField> textFields, ArrayList<JTextField> intFields, ArrayList<JTextField> doubleFields,
								   ArrayList<JPasswordField> pinFields, ArrayList<JComboBox<String>> comboBoxes, ArrayList<JComboBox<String>> addedBoxes, ArrayList<JComboBox<String>> lastPurchaseBoxes){
		
		boolean valid = true;
		Border errorBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red);
		Border validBorder = UIManager.getBorder("TextField.border");
		
		if(textFields.size() > 0){
			for(JTextField text : textFields){
				if(text.getText().length() == 0){
					//matching label = red
					text.setBorder(errorBorder);
					valid = false;
				}
				else text.setBorder(validBorder);
			}
		}
		
		if(pinFields != null){
			for(JPasswordField password : pinFields){
				String input = String.valueOf(password.getPassword());
				if(!input.matches("^\\d*$") || input.length() !=4){
					password.setBorder(errorBorder);
					valid = false;
				}
				else password.setBorder(validBorder);
			}
		}
		
		if(intFields != null){
			for(JTextField intField : intFields){
				try{
					Integer.parseInt(intField.getText());
					intField.setBorder(validBorder);
				}
				catch (NumberFormatException e){
					intField.setBorder(errorBorder);
					valid = false;
				}
			}
		}
		
		if(doubleFields != null){
			for(JTextField doubleField : doubleFields){
				try{
					Double.parseDouble(doubleField.getText());	
					doubleField.setBorder(validBorder);
				}
				catch (NumberFormatException e){
					doubleField.setBorder(errorBorder);
					valid = false;
				}
			}
		}		
		
		if(comboBoxes != null){
			for(JComboBox<String> stringBox : comboBoxes){
				if(((String) stringBox.getSelectedItem()).length() == 0){
					stringBox.setBorder(errorBorder);
					valid = false;
				}
				else stringBox.setBorder(validBorder);
			}
		}
		
		if(addedBoxes != null){
			
			for(JComboBox<String> addedBox : addedBoxes){
				if(((String) addedBox.getSelectedItem()).length() == 0){
					addedBox.setBorder(errorBorder);
					valid = false;
				}
				else addedBox.setBorder(validBorder);
			}
			
			int day = addedBoxes.get(0).getSelectedIndex();
			int month = addedBoxes.get(1).getSelectedIndex();
			int year = addedBoxes.get(2).getSelectedIndex() + (ShopDriver.yearStart-1);
			
			//If month is April, June, September or November, day cannot be 31
			//OR if month is February and is leap year, day cannot be 30 or 31
			//OR if February but not a leap year, day cannot be 29, 30 or 31
			
			if((day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) || (day == 30 && month == 2) || (day == 29 && month == 2 && year%4 != 0)){ 
				addedBoxes.get(0).setBorder(errorBorder);
				valid = false;
			}
		}
		
		if(lastPurchaseBoxes != null){
			
			for(JComboBox<String> lastPurchaseBox : lastPurchaseBoxes){
				if(((String) lastPurchaseBox.getSelectedItem()).length() == 0){
					lastPurchaseBox.setBorder(errorBorder);
					valid = false;
				}
				else lastPurchaseBox.setBorder(validBorder);
			}
			
			int day = lastPurchaseBoxes.get(0).getSelectedIndex();
			int month = lastPurchaseBoxes.get(1).getSelectedIndex();
			int year = lastPurchaseBoxes.get(2).getSelectedIndex() + (ShopDriver.yearStart-1);
			
			//If month is April, June, September or November, day cannot be 31
			//OR if month is February and is leap year, day cannot be 30 or 31
			//OR if February but not a leap year, day cannot be 29, 30 or 31
			
			if((day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) || (day == 30 && month == 2) || (day == 29 && month == 2 && year%4 != 0)){ 
				lastPurchaseBoxes.get(0).setBorder(errorBorder);
				valid = false;
			}
		}
		
		if(!valid)
			setConfirmMessage("Invalid entry!");
		
		return valid;
	}
	
	/**
	 * Main method. Initialises arrays, creates the GUI and calls the constructor.
	 * 
	 * @see				#initialiseArrays()
	 * @see				InitialiseArray#InitialiseArray()
	 * @see				#createGUI()
	 */
	public static void main(String[] args){
		
		initialiseArrays();											//Method to fill arrays with information
		currentStaff = staffMembers.get(0);							//Sets to manager by default so logon can be disabled
		InitialiseArray.generateOrders(10, false, false);			//Creates 10 random orders to start the program (and doesn't post a confirmation)	
		createGUI();												//Method to initialise GUI
		@SuppressWarnings("unused")
		ShopDriver t = new ShopDriver();							//Calls the ShopDriver constructor
	}
}