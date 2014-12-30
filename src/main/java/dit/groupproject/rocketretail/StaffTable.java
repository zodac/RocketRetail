package dit.groupproject.rocketretail;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
/**
 * A class that is used to model
 * a <code>StaffTable</code>
 */
public class StaffTable {
	/**
	 * A DecimalFormatter which formats Integers into Strings with the given formatting.
	 * Formats doubles to have three digits.
	 */
	static DecimalFormat doubleFormatter = new DecimalFormat("000");
	/**
	 * A DecimalFormatter which formats Integers into Strings with the given formatting.
	 * Formats doubles to have commas, always show to two decimal places, and have at least two
	 * digits before the decimal point.
	 */
	static DecimalFormat doubleFormatter2 = new DecimalFormat("#,###,#00.00");
	/**
	 * A DecimalFormatter which formats Integers into Strings with the given formatting.
	 * Formats doubles to have four digits.
	 */
	static DecimalFormat doubleFormatter3 = new DecimalFormat("0000");
	static String type = "Sort by...";
	static boolean first = true, reverse = false;
	
	/**
	 * creates the staff menu item
	 * */
	public static JMenuItem createMenu(boolean manager){
		JMenuItem staffItem = new JMenuItem("Staff");
		staffItem.addActionListener(new ActionListener(){
			/**
			 * Calls the staff method on activation i.e. mouse click
			 * 
			 * @see StaffTable#staff()
			 * */
			public void actionPerformed(ActionEvent e){
				staff();
			}
		});
		
		staffItem.setEnabled(manager);
		
		//Return menuItem
		return staffItem;
	}
	
	/**
	 * Shows the table of staff members and their details.
	 * Sorts the table by ID on first run.
	 * Options to sort table by ID, Name, Address, Wage, Level and Date Added.
	 * Options to Add, Edit and Delete Staff Members.
	 * 
	 * @see StaffTable#add()
	 * @see StaffTable#edit(int)
	 * @see StaffTable#delete(int, String)
	 * @see StaffTable#sortArrayList()
	 * */
	public static void staff(){
		if(!ShopDriver.currentTable.equals("Staff"))
			ShopDriver.frame.remove(ShopDriver.leftPanel);
		
		ShopDriver.currentTable = "Staff";
		
		//Reset ShopDriver.frame
		ShopDriver.frame.remove(ShopDriver.mainPanel);
		ShopDriver.frame.setTitle("Rocket Retail Inc - Staff Members");
		ShopDriver.frame.repaint();
		ShopDriver.mainPanel = new JPanel(new BorderLayout(0, 1));
		
		
		
		//When first run, ensure ArrayList (and table) is sorted by ID
		if(first){
			SortByID(false);
			first = false;
		}
		
		String[] columnNames = { "ID", "Name", "Gender", "Phone Number", "Address", "Wage", "Level", "Date Added" };
		Object[][] data = new Object[ShopDriver.staffMembers.size()][8];
		
		for(int i=0; i<ShopDriver.staffMembers.size(); i++){
			String level = "";
			if(ShopDriver.staffMembers.get(i).getStaffLevel() == 2)
				level = "Employee";
			else level = "Manager";
			
			String gender = "";
	        if(ShopDriver.staffMembers.get(i).getGender() == 1)
	        	gender = "Male";
	        else if(ShopDriver.staffMembers.get(i).getGender() == 2)
	        	gender = "Female";
			
			data[i][0] = doubleFormatter.format(ShopDriver.staffMembers.get(i).getStaffID());
			data[i][1] = ShopDriver.staffMembers.get(i).getStaffName();
			data[i][2] = gender; 
			data[i][3] = ShopDriver.staffMembers.get(i).getPhoneNumber();
			data[i][4] = ShopDriver.staffMembers.get(i).getAddress();
			data[i][5] = "€" + doubleFormatter2.format(ShopDriver.staffMembers.get(i).getWage());
			data[i][6] = level;
			data[i][7] = ShopDriver.staffMembers.get(i).getDateAdded();
		}
		
		
		final JTable table = new JTable(data, columnNames);
		table.setColumnSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(table.getSelectedRow() >= 0){ //Invalid selections return -1					
					Staff input = null;
					
					for(Staff s : ShopDriver.staffMembers){
						if(s.getStaffID() == Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)))
							input = s;
					}
					showStaffInfo(input);
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ShopDriver.backgroundColour);
		
		String[] staffMemberArrayEdit = new String[ShopDriver.staffMembers.size()+1];
		staffMemberArrayEdit[0] = "Edit Staff";
		for(int i = 0; i < ShopDriver.staffMembers.size()+1; i++){
			if(i < ShopDriver.staffMembers.size())
				staffMemberArrayEdit[i+1] = "ID: " + doubleFormatter.format(ShopDriver.staffMembers.get(i).getStaffID()) + " (" + ShopDriver.staffMembers.get(i).getStaffName() + ")";
		}
		
		String[] staffMemberArrayDelete = new String[ShopDriver.staffMembers.size()+1];
		staffMemberArrayDelete[0] = "Delete Staff";
		for(int i = 0; i < ShopDriver.staffMembers.size()+1; i++){
			if(i < ShopDriver.staffMembers.size())
				staffMemberArrayDelete[i+1] = "ID: " + doubleFormatter.format(ShopDriver.staffMembers.get(i).getStaffID()) + " (" + ShopDriver.staffMembers.get(i).getStaffName() + ")";
		}
		
		
		JButton addButton = new JButton("Add Staff");
		final JComboBox<String> editBox = new JComboBox<String>(staffMemberArrayEdit);
		final JComboBox<String> deleteBox = new JComboBox<String>(staffMemberArrayDelete);
		
		String[] options= {"Sort by...", "ID", "Name", "Address", "Wage", "Level", "Date Added"};
		final JComboBox<String> sortOptions = new JComboBox<String>(options);
		int index = 0;
		
		if(type.equals("Sort by..."))
			index = 0;
		if(type.equals("ID"))
			index = 1;
		if(type.equals("Name"))
			index = 2;
		if(type.equals("Address"))
			index = 3;
		if(type.equals("Wage"))
			index = 4;
		if(type.equals("Level"))
			index = 5;
		if(type.equals("Date Added"))
			index = 6;
		sortOptions.setSelectedIndex(index);
		
		
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				add();
			}
		});
		
		editBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				edit(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 7)));
			}
		});
		
		deleteBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				delete(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 7)),
										  ((String) deleteBox.getSelectedItem()).substring(9, ((String) deleteBox.getSelectedItem()).length()-1));
			}
		});
		
		sortOptions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//If already sorted by type, reverse order
				if(sortOptions.getSelectedItem().equals("Sort by...")){}
				
				else if(type.equals((String) sortOptions.getSelectedItem())){
					if(reverse)
						reverse = false;
					else reverse = true;
					sortArrayList();
				}
				
				//Else sort in ascending order
				else{
					type = (String) sortOptions.getSelectedItem();
					sortArrayList();
				}
				staff();
			}
		});
		
		buttonPanel.add(addButton);
		buttonPanel.add(editBox);
		buttonPanel.add(deleteBox);
		buttonPanel.add(sortOptions);
		
		ShopDriver.mainPanel.add(scrollPane, BorderLayout.NORTH);
		ShopDriver.mainPanel.add(buttonPanel, BorderLayout.CENTER);
		
		//Update ShopDriver.frame
		ShopDriver.setFrame(false, false, true);
	}
	/**
	 * Adds a staff member to the arrayList of staff.
	 * Brings up a form on a panel for the user to fill in all attributes. 
	 * */
	public static void add(){
		//Reset ShopDriver.frame
		ShopDriver.frame.remove(ShopDriver.leftPanel);
		ShopDriver.frame.repaint();
		ShopDriver.leftPanel = new JPanel();
		
		//Panel items
		JPanel innerPanel = new JPanel(new GridBagLayout());
		innerPanel.setBackground(ShopDriver.backgroundColour);
		GridBagConstraints g = new GridBagConstraints();
		
		//JLabels with GridBagLayout
		g.gridx = 0;
		g.gridy = 0;
		innerPanel.add(new JLabel("Staff ID:"), g);
		g.gridy = 1;
		innerPanel.add(new JLabel("Staff PIN:"), g);
		g.gridy = 2;
		innerPanel.add(new JLabel("Name:"), g);
		g.gridy = 3;
		innerPanel.add(new JLabel("Gender:"), g);
		g.gridy = 4;
		innerPanel.add(new JLabel("Phone Number:"), g);
		g.gridy = 5;
		innerPanel.add(new JLabel("Address:"), g);
		g.gridy = 6;
		innerPanel.add(new JLabel("Wage:"), g);
		g.gridy = 7;
		innerPanel.add(new JLabel("Staff Level:"), g);
		g.gridy = 8;
		innerPanel.add(new JLabel("Date Added:"), g);
		
		g.insets = new Insets(1, 10, 0, 5); 					//Border to left, and small one on top as a spacer between rows
		
		String[] genderOptions = { "", "Male", "Female" };
		String[] staffLevelOptions = { "", "Manager", "Employee" };
		g.fill = GridBagConstraints.HORIZONTAL;
		//JTextFields with GridBagLayout
		g.gridx = 1;
		g.gridy = 0;
		g.gridwidth=3;
		final JTextField idField = new JTextField(null, 20);
		idField.setText(doubleFormatter.format(firstAvailableID()));
		idField.setEditable(false);
		innerPanel.add(idField, g);
		g.gridy = 1;
		g.gridwidth=3;
		final JPasswordField pinField = new JPasswordField(null, 20);
		pinField.setDocument(new JTextFieldLimit(4));
		innerPanel.add(pinField, g);
		g.gridy = 2;
		g.gridwidth=3;
		final JTextField nameField = new JTextField(null, 20);
		innerPanel.add(nameField, g);
		g.gridy = 3;
		g.gridwidth=3;
		
		final JComboBox<String> genderField = new JComboBox<String>(genderOptions);
		innerPanel.add(genderField, g);
		g.gridy = 4;
		g.gridwidth=3;
		final JTextField phoneNoField = new JTextField(null, 20);
		innerPanel.add(phoneNoField, g);
		g.gridy = 5;
		g.gridwidth=3;
		final JTextField addressField = new JTextField(null, 20);
		innerPanel.add(addressField, g);
		g.gridy = 6;
		g.gridwidth=3;
		final JTextField wageField = new JTextField(null, 20);
		innerPanel.add(wageField, g);
		g.gridy = 7;
		g.gridwidth=3;
		final JComboBox<String> staffLevelField = new JComboBox<String>(staffLevelOptions);
		innerPanel.add(staffLevelField, g);
		
		g.gridy = 8;
        g.gridx = 1;
        g.gridwidth=1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(ShopDriver.days); 
        innerPanel.add(dateAddedDay, g);
        g.gridy = 8;
        g.gridx = 2;
        g.gridwidth=1-2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(ShopDriver.months); 
        innerPanel.add(dateAddedMonth, g); 
        g.gridy = 8;
        g.gridx = 3;
        g.gridwidth=2-3;
        final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.years); 
        innerPanel.add(dateAddedYear, g);
        
        dateAddedDay.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(0, 2)));
	    dateAddedMonth.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(3, 5)));
	    dateAddedYear.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(6, 10))-(ShopDriver.yearStart-1)); 
		
		//Spacer
		g.gridx = 0;
		g.gridy = 9;		
		innerPanel.add(new JLabel(" "), g);
		g.gridx = 1;
		g.gridy = 9;
		innerPanel.add(new JLabel(" "), g);
		
		JButton save = new JButton("Save");
		save.setLayout(new GridBagLayout());
		g = new GridBagConstraints();
		g.insets = new Insets(1, 0, 0, 0);
		g.gridx = 1;
		g.gridy = 10;
		innerPanel.add(save, g);
		JButton cancel = new JButton("Cancel");
		g.gridx = 3;
		g.gridy = 10;
		innerPanel.add(cancel, g);
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				ArrayList<JTextField> textFields = new ArrayList<JTextField>();
				textFields.add(nameField);
				textFields.add(phoneNoField);
				textFields.add(addressField);
				ArrayList<JTextField> doubleFields = new ArrayList<JTextField>();
				doubleFields.add(wageField);
				ArrayList<JPasswordField> pinFields = new ArrayList<JPasswordField>();
				pinFields.add(pinField);
				ArrayList<JComboBox<String>> comboBoxes = new ArrayList<JComboBox<String>>();
				comboBoxes.add(genderField);
				comboBoxes.add(staffLevelField);
				ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
				addedBoxes.add(dateAddedDay);
				addedBoxes.add(dateAddedMonth);
				addedBoxes.add(dateAddedYear);
				
				boolean valid = ShopDriver.checkFields(textFields, null, doubleFields, pinFields, comboBoxes, addedBoxes, null);
				
				if(valid){
					ShopDriver.staffMembers.add(new Staff(Integer.parseInt(idField.getText()), 
														  Integer.parseInt(String.valueOf(pinField.getPassword())),
														  nameField.getText(),
														  genderField.getSelectedIndex(),
														  phoneNoField.getText(),
														  addressField.getText(), 
														  Double.parseDouble(wageField.getText()), 
														  staffLevelField.getSelectedIndex(), 
														  dateAddedDay.getSelectedItem() + "/" +
																  dateAddedMonth.getSelectedItem() + "/" +
																  dateAddedYear.getSelectedItem()
														  )
												);
					
					ShopDriver.setConfirmMessage("Staff member " + nameField.getText() + " added");
					ShopDriver.frame.remove(ShopDriver.leftPanel);
					ShopDriver.frame.validate();
					staff();
				}
			}		
		});
	
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ShopDriver.frame.remove(ShopDriver.leftPanel);
				ShopDriver.frame.validate();
			}
		});
		
		//Add innerPanel
		ShopDriver.leftPanel.add(innerPanel);
		
		//Update ShopDriver.frame
		ShopDriver.setFrame(true, false, false);
	}
	/**
	 * Edits a staff member on the arrayList of staff.
	 * Brings up a form on a panel for the user to fill and edit attributes.
	 * Requires the staffID to be passed so as to bring up the correct staff member to edit. 
	 * @param staffID (int)
	 * */
	public static void edit(int staffID){
		//Reset ShopDriver.frame
		ShopDriver.frame.remove(ShopDriver.leftPanel);
		ShopDriver.frame.repaint();
		ShopDriver.leftPanel = new JPanel();

		for(Staff t : ShopDriver.staffMembers){
			if (staffID == t.getStaffID()){	
				final int index = ShopDriver.staffMembers.indexOf(t);
				
				//Panel items
   				JPanel innerPanel = new JPanel(new GridBagLayout());
   				innerPanel.setBackground(ShopDriver.backgroundColour);
				GridBagConstraints g = new GridBagConstraints();
				
				//JLabels with GridBagLayout
				g.gridx = 0;
				g.gridy = 0;
				innerPanel.add(new JLabel("Staff ID:"), g);
				g.gridy = 1;
				innerPanel.add(new JLabel("Staff PIN:"), g);
				g.gridy = 2;
				innerPanel.add(new JLabel("Name:"), g);
				g.gridy = 3;
				innerPanel.add(new JLabel("Gender:"), g);
				g.gridy = 4;
				innerPanel.add(new JLabel("Phone Number:"), g);
				g.gridy = 5;
				innerPanel.add(new JLabel("Address:"), g);
				g.gridy = 6;
				innerPanel.add(new JLabel("Wage:"), g);
				g.gridy = 7;
				innerPanel.add(new JLabel("Staff Level:"), g);
				g.gridy = 8;
				innerPanel.add(new JLabel("Date Added:"), g);	
				
				g.insets = new Insets(1, 10, 0, 5);
				String[] genderOptions = { "", "Male", "Female" };
				String[] staffLevelOptions = { "", "Manager", "Employee" };
				g.fill = GridBagConstraints.HORIZONTAL;
				
				//JTextFields with GridBagLayout
				g.gridx = 1;
				g.gridy = 0;
				g.gridwidth=3;
				final JTextField idField = new JTextField(null, 20);
				idField.setEditable(false);
				innerPanel.add(idField, g);
				g.gridy = 1;
				g.gridwidth=3;
				final JPasswordField pinField = new JPasswordField(null, 20);
				pinField.setDocument(new JTextFieldLimit(4));
				innerPanel.add(pinField, g);
				g.gridy = 2;
				g.gridwidth=3;
				final JTextField nameField = new JTextField(null, 20);
				innerPanel.add(nameField, g);
				g.gridy = 3;	
				g.gridwidth=3;
				final JComboBox<String> genderField = new JComboBox<String>(genderOptions);
				innerPanel.add(genderField, g);
				g.gridy = 4;	
				g.gridwidth=3;
				final JTextField phoneNoField = new JTextField(null, 20);
				innerPanel.add(phoneNoField, g);						
				g.gridy = 5;
				g.gridwidth=3;
				final JTextField addressField = new JTextField(null, 20);
				innerPanel.add(addressField, g);							
				g.gridy = 6;
				g.gridwidth=3;
				final JTextField wageField = new JTextField(null, 20);
				innerPanel.add(wageField, g);			
				g.gridy = 7;
				g.gridwidth=3;
				final JComboBox<String> staffLevelField = new JComboBox<String>(staffLevelOptions);
				innerPanel.add(staffLevelField, g);
				
				g.gridy = 8;
		        g.gridx = 1;
		        g.gridwidth=1;
				final JComboBox<String> dateAddedDay = new JComboBox<String>(ShopDriver.days); 
                innerPanel.add(dateAddedDay, g);
              
                g.gridy = 8;
                g.gridx = 2;
                g.gridwidth=1-2;
                final JComboBox<String> dateAddedMonth = new JComboBox<String>(ShopDriver.months); 
                innerPanel.add(dateAddedMonth, g); 
                
                g.gridy = 8;
                g.gridx = 3;
                g.gridwidth=2-3;
                final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.years); 
                innerPanel.add(dateAddedYear, g); 
                
				//Set JTextFields with current data
				idField.setText(doubleFormatter.format(staffID));
				pinField.setText("" + t.getStaffPIN());
				nameField.setText(t.getStaffName());
				genderField.setSelectedIndex(t.getGender());
				phoneNoField.setText(t.getPhoneNumber());
				addressField.setText(t.getAddress());
				wageField.setText("" + t.getWage());
				staffLevelField.setSelectedIndex(t.getStaffLevel());
				dateAddedDay.setSelectedIndex(Integer.parseInt(t.getDateAdded().substring(0, 2)));
	            dateAddedMonth.setSelectedIndex(Integer.parseInt(t.getDateAdded().substring(3, 5)));
	            dateAddedYear.setSelectedIndex(Integer.parseInt(t.getDateAdded().substring(6, 10))-(ShopDriver.yearStart-1));
	            
	            //Spacer
				g.gridx = 0;
				g.gridy = 9;		
				innerPanel.add(new JLabel(" "), g);
				g.gridx = 1;
				g.gridy = 9;
				innerPanel.add(new JLabel(" "), g);
				
				JButton save = new JButton("Save");
				save.setLayout(new GridBagLayout());
				g = new GridBagConstraints();
				g.insets = new Insets(1, 0, 0, 0);
				g.gridx = 1;
				g.gridy = 10;
				innerPanel.add(save, g);
				JButton cancel = new JButton("Cancel");
				g.gridx = 3;
				g.gridy = 10;
				innerPanel.add(cancel, g);
				
				
				save.addActionListener(new ActionListener(){
   					public void actionPerformed(ActionEvent e){
   						
   						ArrayList<JTextField> textFields = new ArrayList<JTextField>();
   						textFields.add(nameField);
   						textFields.add(phoneNoField);
   						textFields.add(addressField);
   						ArrayList<JTextField> doubleFields = new ArrayList<JTextField>();
   						doubleFields.add(wageField);
   						ArrayList<JPasswordField> pinFields = new ArrayList<JPasswordField>();
   						pinFields.add(pinField);
   						ArrayList<JComboBox<String>> comboBoxes = new ArrayList<JComboBox<String>>();
   						comboBoxes.add(genderField);
   						comboBoxes.add(staffLevelField);
   						ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
   						addedBoxes.add(dateAddedDay);
   						addedBoxes.add(dateAddedMonth);
   						addedBoxes.add(dateAddedYear);
   						
   						boolean valid = ShopDriver.checkFields(textFields, null, doubleFields, pinFields, comboBoxes, addedBoxes, null);
   						
   						if(valid){
	   						//Add the staff at this index
							ShopDriver.staffMembers.add(index, new Staff(Integer.parseInt(idField.getText()), 
																		 Integer.parseInt(String.valueOf(pinField.getPassword())),
																		 nameField.getText(),
																		 genderField.getSelectedIndex(),
																		 phoneNoField.getText(),
																		 addressField.getText(), 
																		 Double.parseDouble(wageField.getText()), 
																		 staffLevelField.getSelectedIndex(),
																		 dateAddedDay.getSelectedItem() + "/" +
																				 dateAddedMonth.getSelectedItem() + "/" +
																				 dateAddedYear.getSelectedItem()
																		)
														); 
							
							ShopDriver.setConfirmMessage("Staff member " + nameField.getText() + "'s details editted");
							ShopDriver.frame.remove(ShopDriver.leftPanel);
							ShopDriver.frame.validate();
							
	   						//Since adding an entry to ArrayList pushes down previous entry, which needs to be removed
	   						ShopDriver.staffMembers.remove(index + 1);
	   						staff();
   						}
   					}		
   				});       									
	          
				cancel.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						ShopDriver.frame.remove(ShopDriver.leftPanel);
						ShopDriver.frame.validate();
					}
				});
				//Add innerPanel
				ShopDriver.leftPanel.add(innerPanel);	
			}			
		}
		//Update ShopDriver.frame
		ShopDriver.setFrame(true, false, false);
	}
	/**
	 * Deletes a staff member from the arrayList of staff.
	 * Brings up a confirmation window to ensure the user is certain they selected the 
	 * correct staff member. Also shows the staff members name and ID in confirmation window.
	 * @param staffID (int)
	 * @param staffName (String)
	 * */
	public static void delete(int staffID, String staffName){		
		if(staffID == ShopDriver.currentStaff.getStaffID()){
			JOptionPane.showMessageDialog(null, "You can't delete yourself!", "Deletion Error", JOptionPane.PLAIN_MESSAGE);
		}
		else{
			JPanel myPanel = new JPanel();
			myPanel.add(new JLabel("Do you want to delete " + staffName + "?"));
			
			int i = -1;											//Holds index of object to be deleted
			
		    if (ShopDriver.showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION){
		    	//Reset ShopDriver.frame
				ShopDriver.frame.remove(ShopDriver.leftPanel);
				ShopDriver.frame.repaint();
				ShopDriver.leftPanel = new JPanel();
				
		    	for(Staff s : ShopDriver.staffMembers){
		    		if(staffID == s.getStaffID())
		    			i = ShopDriver.staffMembers.indexOf(s); //Object can't be removed while being accessed - save its index location for now
			    }
		    }
		    
		    if (i != -1) 										//If an object has been found, we can now remove it from the ArrayList
			ShopDriver.setConfirmMessage(staffName + " deleted");
		    	ShopDriver.staffMembers.remove(i);
		    
		    //Update ShopDriver.frame
		    staff();
		}
		ShopDriver.frame.validate();
	}
	/**
	 * Called after the user clicks on a staff member entry on the table.
	 * Brings up a panel of information about that staff member. Along with two tables
	 * of information on orders handled by that staff member. One for customer orders, one
	 * for supplier orders. Requires the Staff object to be passed into it so as to show 
	 * the correct information.
	 * @param s (Staff) 
	 * */
	public static void showStaffInfo(Staff s){
		//Reset ShopDriver.frame
		ShopDriver.frame.remove(ShopDriver.mainPanel);
		ShopDriver.frame.setTitle("Rocket Retail Inc - " + s.getStaffName());
		ShopDriver.frame.repaint();
		ShopDriver.mainPanel = new JPanel(new BorderLayout(0, 1));
		
		JPanel titlePanel = new JPanel(new GridBagLayout());
		JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
		JPanel myPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		titlePanel.setBackground(ShopDriver.backgroundColour);
		innerPanel.setBackground(ShopDriver.backgroundColour);
		myPanel.setBackground(ShopDriver.backgroundColour);
		buttonPanel.setBackground(ShopDriver.backgroundColour);
		
		String level = "";
		if(s.getStaffLevel() == 1)
			level = "Manager";
		else level = "Employee";
		
		JLabel staffLabel = new JLabel("Staff");
		JLabel levelLabel = new JLabel("Staff Level");
		JLabel wageLabel = new JLabel("Wage");
		JLabel phoneNoLabel = new JLabel("Phone Number");
		JLabel addressLabel = new JLabel("Address");
		JLabel dateAddedLabel = new JLabel("Date Added");
		JLabel titleLabel = new JLabel("Orders placed by " + s.getStaffName());
		staffLabel.setFont(new Font(staffLabel.getFont().getFontName(), Font.BOLD, staffLabel.getFont().getSize()));
		levelLabel.setFont(new Font(levelLabel.getFont().getFontName(), Font.BOLD, levelLabel.getFont().getSize()));
		wageLabel.setFont(new Font(wageLabel.getFont().getFontName(), Font.BOLD, wageLabel.getFont().getSize()));
		phoneNoLabel.setFont(new Font(phoneNoLabel.getFont().getFontName(), Font.BOLD, phoneNoLabel.getFont().getSize()));
		addressLabel.setFont(new Font(addressLabel.getFont().getFontName(), Font.BOLD, addressLabel.getFont().getSize()));
		dateAddedLabel.setFont(new Font(dateAddedLabel.getFont().getFontName(), Font.BOLD, dateAddedLabel.getFont().getSize()));
		titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));
		
		int textFieldSize = 15;
		
		JTextField staffField = new JTextField(s.getStaffName() + " (" + doubleFormatter.format(s.getStaffID()) + ")", textFieldSize);
		staffField.setEditable(false);
		JTextField levelField = new JTextField(level, textFieldSize);
		levelField.setEditable(false);
		JTextField wageField = new JTextField("€" + doubleFormatter2.format(s.getWage()), textFieldSize);
		wageField.setEditable(false);
		
		
		JTextField phoneNoField = new JTextField(s.getPhoneNumber(), textFieldSize);
		phoneNoField.setEditable(false);
		JTextField addressField = new JTextField(s.getAddress(), textFieldSize);
		addressField.setEditable(false);
		JTextField dateAddedField = new JTextField(s.getDateAdded(), textFieldSize);
		dateAddedField.setEditable(false);
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(1, 10, 0, 5);
		titlePanel.add(staffLabel, g);
		g.gridx = 1;
		titlePanel.add(staffField, g);
		g.gridx = 2;
		titlePanel.add(levelLabel, g);
		g.gridx = 3;
		titlePanel.add(levelField, g);
		g.gridx = 4;
		titlePanel.add(wageLabel, g);
		g.gridx = 5;
		titlePanel.add(wageField, g);
		
		g.gridy = 1;
		g.gridx = 0;
		titlePanel.add(phoneNoLabel, g);
		g.gridx = 1;
		titlePanel.add(phoneNoField, g);
		g.gridx = 2;
		titlePanel.add(addressLabel, g);
		g.gridx = 3;
		titlePanel.add(addressField, g);
		g.gridx = 4;
		titlePanel.add(dateAddedLabel, g);
		g.gridx = 5;
		titlePanel.add(dateAddedField, g);
		
		
		g.gridy = 2;
		g.gridx = 2;
		g.gridwidth = 2;
		titlePanel.add(titleLabel, g);
		
		int count = 0, count2 = 0;
		int arrayIndex = 0;
		
		for(Order o : ShopDriver.orders){
			if(o.getStaffID() == s.getStaffID())
				if(!o.isSupplier())
					count++;
				else count2++;
		}
		
		String[] columnNames = { "Order ID", "Trader", "Total Cost" };
		Object[][] data = new Object[count+1][3];
		double total = 0;
		
		for(int i = 0; i < ShopDriver.orders.size(); i++){
			
			if(!ShopDriver.orders.get(i).isSupplier() && ShopDriver.orders.get(i).getStaffID() == s.getStaffID()){
				
				double totalOrderPrice = 0;
				for(int j = 0; j < ShopDriver.orders.get(i).getOrderedItems().size(); j++){
					totalOrderPrice += ShopDriver.orders.get(i).getOrderedItems().get(j).getQuantity()*
							 ShopDriver.orders.get(i).getOrderedItems().get(j).getProduct().getSalePrice();
				}
				
				String name = "";
				
				for(Customer c : ShopDriver.customers){
					if(ShopDriver.orders.get(i).getTraderID() ==  c.getCustomerID())
						name = c.getCustomerName();
				}
				
				
				data[arrayIndex][0] = doubleFormatter3.format(ShopDriver.orders.get(i).getOrderID());
				data[arrayIndex][1] = name + " (" + ShopDriver.orders.get(i).getTraderID() + ")";
				data[arrayIndex][2] = "€" + doubleFormatter2.format(totalOrderPrice);
				arrayIndex++;
				total += totalOrderPrice;
			}
		}
		data[count][1] = "<html><b>Customer Total</b></html>";
		data[count][2] = "<html><b>€" + doubleFormatter2.format(total) + "</b></html>";
		
		JTable customerTable = new JTable(data, columnNames);
		customerTable.setFillsViewportHeight(true);
		customerTable.setEnabled(false);
		
		String[] columnNames2 = { "Order ID", "Trader", "Total Cost" };
		Object[][] data2 = new Object[count2+1][3];
		total = arrayIndex = 0;
		
		for(int i = 0; i < ShopDriver.orders.size(); i++){
			
			if(ShopDriver.orders.get(i).isSupplier() && ShopDriver.orders.get(i).getStaffID() == s.getStaffID()){
				
				double totalOrderPrice = 0;
				for(int j = 0; j < ShopDriver.orders.get(i).getOrderedItems().size(); j++){
					totalOrderPrice += ShopDriver.orders.get(i).getOrderedItems().get(j).getQuantity()*
							 ShopDriver.orders.get(i).getOrderedItems().get(j).getProduct().getCostPrice();
				}
				
				String name = "";
				
				for(Supplier supp : ShopDriver.suppliers){
					if(ShopDriver.orders.get(i).getTraderID() ==  supp.getSupplierID())
						name = supp.getSupplierName();
				}
				
				
				data2[arrayIndex][0] = doubleFormatter3.format(ShopDriver.orders.get(i).getOrderID());
				data2[arrayIndex][1] = name + " (" + ShopDriver.orders.get(i).getTraderID() + ")";
				data2[arrayIndex][2] = "€" + doubleFormatter2.format(totalOrderPrice);
				arrayIndex++;
				total += totalOrderPrice;
			}
		}
		data2[count2][1] = "<html><b>Supplier Total</b></html>";
		data2[count2][2] = "<html><b>€" + doubleFormatter2.format(total) + "</b></html>";	
		
		JTable supplierTable = new JTable(data2, columnNames2);
		supplierTable.setFillsViewportHeight(true);
		supplierTable.setEnabled(false);
		
			
		
		
		JScrollPane scrollPane = new JScrollPane(customerTable);
		myPanel.add(scrollPane, BorderLayout.WEST);
		
		JScrollPane scrollPane2 = new JScrollPane(supplierTable);
		myPanel.add(scrollPane2, BorderLayout.EAST);
		
		JButton backButton = new JButton("Back to Staff Members");
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				staff();
			}
		});
		
		
		buttonPanel.add(backButton);
		innerPanel.add(myPanel, BorderLayout.NORTH);
		innerPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		
		ShopDriver.mainPanel.add(titlePanel, BorderLayout.NORTH);
		ShopDriver.mainPanel.add(innerPanel, BorderLayout.CENTER);
		
		//Update frame
		ShopDriver.setFrame(false, false, true);
	}
	/**
	 * Finds the first available ID in the staffmembers arrayList.
	 * As staff members are added and removed over time, gaps may occur in the series
	 * of allowed numbers for ID assignments. This method finds the first available in 
	 * numerical order.
	 * Returns the value of the first available ID number.
	 * */
	public static int firstAvailableID(){
		type = "ID";
		SortByID(false);
		int output = 0, i = 0;
		boolean found = false;

		while(!found && i < ShopDriver.staffMembers.size()){
			if(ShopDriver.staffMembers.get(i).getStaffID() == i){}
			else{
				output = i;
				found = true;
			}
			i++;
		}
		
		if(found)
			return output;
		else return ShopDriver.staffMembers.size();
	}
	/**
	 * This checks the type of sort method required by the user and calls the 
	 * relevant method.
	 * @see StaffTable#SortByID(boolean)
	 * @see StaffTable#SortByName(boolean) 
	 * @see StaffTable#SortByAddress(boolean)
	 * @see StaffTable#SortByWage(boolean)
	 * @see StaffTable#SortByDateAdded(boolean)
	 * */
	public static void sortArrayList(){
		
		if(type.equals("ID"))
			SortByID(reverse);
		if(type.equals("Name"))
			SortByName(reverse);
		if(type.equals("Address"))
			SortByAddress(reverse);
		if(type.equals("Wage"))
			SortByWage(reverse);
		if(type.equals("Level"))
			SortByLevel(reverse);
		if(type.equals("Date Added"))
			SortByDateAdded(reverse);
	}
	/**
	 * sort by name method sorts the arrayList of staff based on the names of the 
	 * staff members (alphabetically). It requires the Boolean reverse to be passed 
	 * into it to know if the list should be sorted alphabetically or reverse alphabetically.
	 * @param reverse (Boolean)
	 * */
	public static void SortByName(boolean reverse){
		if(!reverse){
			Collections.sort(ShopDriver.staffMembers, new Comparator<Staff>(){
				public int compare(Staff s1, Staff s2){
					return s1.getStaffName().compareToIgnoreCase(s2.getStaffName());
				}
			});	
		}
		
		if(reverse){
			Collections.sort(ShopDriver.staffMembers, new Comparator<Staff>(){
				public int compare(Staff s1, Staff s2){
					return s2.getStaffName().compareToIgnoreCase(s1.getStaffName());
				}
			});	
		}
	}
	/**
	 * sort by address method sorts the arrayList of staff based on the addresses of the 
	 * staff members (alphabetically). It requires the Boolean reverse to be passed 
	 * into it to know if the list should be sorted alphabetically or reverse alphabetically.
	 * @param reverse (Boolean)
	 * */
	public static void SortByAddress(boolean reverse){
		if(!reverse){
			Collections.sort(ShopDriver.staffMembers, new Comparator<Staff>(){
				public int compare(Staff s1, Staff s2){
					return s1.getAddress().compareToIgnoreCase(s2.getAddress());
				}
			});	
		}
		
		if(reverse){
			Collections.sort(ShopDriver.staffMembers, new Comparator<Staff>(){
				public int compare(Staff s1, Staff s2){
					return s2.getAddress().compareToIgnoreCase(s1.getAddress());
				}
			});	
		}
	}
	/**
	 * sort by ID method sorts the arrayList of staff based on the IDs of the 
	 * staff members (numerically). It requires the Boolean reverse to be passed 
	 * into it to know if the list should be sorted numerically or reversed.
	 * @param reverse (Boolean)
	 * */
	public static void SortByID(boolean reverse){
		ArrayList<Staff> tempArrayList = new ArrayList<Staff>();
		int count = 0, offset = 0;
		boolean found = false;
		
		for(int i = 0; i < ShopDriver.staffMembers.size() + offset; i++){
			found = false;
			for(Staff s : ShopDriver.staffMembers){
				if(count == s.getStaffID()){
					tempArrayList.add(s);
					found = true;
				}
			}
			if(!found)
				offset++;
			count++;
		}
		
		ShopDriver.staffMembers.clear();
		
		if(!reverse){
			for(int i = 0 ; i < tempArrayList.size(); i++){
				ShopDriver.staffMembers.add(tempArrayList.get(i));
			}
		}
		else if (reverse){
			for(int i = tempArrayList.size()-1; i >= 0; i--){
				ShopDriver.staffMembers.add(tempArrayList.get(i));
			}
		}
	}
	/**
	 * sort by wage method sorts the arrayList of staff based on the wages of the 
	 * staff members (numerically). It requires the Boolean reverse to be passed 
	 * into it to know if the list should be sorted numerically or reversed.
	 * @param reverse (Boolean)
	 * */
	public static void SortByWage(boolean reverse){
		double lowestWage = 999999999;
		ArrayList<Staff> tempArrayList = new ArrayList<Staff>();
		int indexL = 0;
		
		tempArrayList.clear();
		boolean found;
		
		while(ShopDriver.staffMembers.size()!=0)
		{
			//go through the array list and find the lowest wage listed
			for(Staff s : ShopDriver.staffMembers){
				if(lowestWage>s.getWage()){
					lowestWage = s.getWage();
				}
			}
			
			found = false;
			//find an entry in the arrayList with a wage matching the lowest wage found 
			for(Staff s : ShopDriver.staffMembers){
				if(lowestWage==s.getWage() && !found){
					//when a matching entry is found, add to tempArrayList
					tempArrayList.add(s);
					
					//note the index of this entry
					indexL = ShopDriver.staffMembers.indexOf(s);

					found = true;
				}
				if(found)
					break;
			}
			ShopDriver.staffMembers.remove(indexL);
			
			lowestWage = 99999999;
		}

		ShopDriver.staffMembers.clear();
		
		if(!reverse){
			for(int i = 0 ; i < tempArrayList.size(); i++){
				ShopDriver.staffMembers.add(tempArrayList.get(i));
			}
		}
		else if (reverse){
			for(int i = tempArrayList.size()-1; i >= 0; i--){
				ShopDriver.staffMembers.add(tempArrayList.get(i));
			}
		}
	}
	/**
	 * sort by level method sorts the arrayList of staff based on the levels of the 
	 * staff members. It requires the Boolean reverse to be passed into it to know if 
	 * the list should be sorted managers first or last.
	 * @param reverse (Boolean)
	 * */
	public static void SortByLevel(Boolean reverse){
		ArrayList<Staff> tempArrayList = new ArrayList<Staff>();
		
		tempArrayList.clear();
		
		for(Staff s : ShopDriver.staffMembers){
			if(s.getStaffLevel()==1)
				tempArrayList.add(s);
		}
		for(Staff s : ShopDriver.staffMembers){
			if(s.getStaffLevel()==2)
				tempArrayList.add(s);
		}
		
		ShopDriver.staffMembers.clear();
		
		if(reverse){
			for(int i = 0 ; i < tempArrayList.size(); i++){
				ShopDriver.staffMembers.add(tempArrayList.get(i));
			}
		}
		else if (!reverse){
			for(int i = tempArrayList.size()-1; i >= 0; i--){
				ShopDriver.staffMembers.add(tempArrayList.get(i));
			}
		}
	}
	/**
	 * sort by date added method sorts the arrayList of staff based on the dates the  
	 * staff members joined. It requires the Boolean reverse to be passed 
	 * into it to know if the list should be sorted chronologically or reversed.
	 * Calls three other methods to sort the staff arrayList first by days, then months, then years.
	 * 
	 * @param reverse (boolean)
	 * */
	public static void SortByDateAdded(final boolean reverse){

		Collections.sort(ShopDriver.staffMembers, new Comparator<Staff>() {
	        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
	        
			@Override
			public int compare(Staff s1, Staff s2) {
				try {
					if(!reverse)
						return f.parse(s1.getDateAdded()).compareTo(f.parse(s2.getDateAdded()));
					else return f.parse(s2.getDateAdded()).compareTo(f.parse(s1.getDateAdded()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
	    });
	}
}