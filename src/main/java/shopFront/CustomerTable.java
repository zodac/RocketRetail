package shopFront;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * A class that is used to display
 * a table with multiple <code>Customer</code>
 * entries. It offers sorting options and
 * options to add, edit and delete customers
 * 
 * @author Sheikh
 * @author Tony
 * @author Alan
 * @author Jessica
 * @author Jason
 * @version 2.0
 * @since 1.0
 * 
 */
public class CustomerTable {
	
	/**
	 * Formats the customer's id.
	 */
	static DecimalFormat doubleFormatter = new DecimalFormat("00000");
	/**
	 * Formats monetary values in the customer table.
	 */
	static DecimalFormat doubleFormatter2 = new DecimalFormat("#,###,#00.00");
	/**
	 * Formats an order id 
	 */
	static DecimalFormat doubleFormatter3 = new DecimalFormat("0000");
	/**
	 * The type to sort the table by
	 */
	static String type = "Sort by...";
	/**
	 * A flag to decide if it's the first time 
	 * the table is being loaded. 
	 */
	static boolean first = true;
	/**
	 * A flag to decide whether <code>Customer</code> 
	 * records are added to an <code>ArrayList</code 
	 * in ascending or descending order. 
	 */
	static boolean reverse = false;
	
	/**
	 * This method creates a Customer menu item
	 * and adds an <code>ActionListener</code> to
	 * it.
	 * 
	 * @return A <code>JMenuItem</code> to be added
	 * to a <code>JMenu</code>.
	 */
	public static JMenuItem createMenu(boolean manager){
		JMenuItem customerItem = new JMenuItem("Customer");
		customerItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				customer();
			}
		});
		
		customerItem.setEnabled(manager);
		
		return customerItem;
	}
	
	/**
	 * This method prepares and places all the GUI components
	 * into a <code>JPanel</code>. It adds the <code>JPanel</code> to the 
	 * <code>JFrame</code> using <code>ShopDriver</code>'s
	 * <code>setFrame()</code> method.
	 * 
	 * This method adds <code>ActionListener</code>s to
	 * GUI components such as the <code>JTable</code>
	 * and multiple <code>JComboBox</code>.
	 */
	public static void customer(){
		if(!ShopDriver.currentTable.equals("Customer"))
			ShopDriver.frame.remove(ShopDriver.leftPanel);
		
		ShopDriver.currentTable = "Customer";
		
		ShopDriver.frame.remove(ShopDriver.mainPanel);
		ShopDriver.frame.setTitle("Rocket Retail Inc - Customers");
		ShopDriver.frame.repaint();
		ShopDriver.mainPanel = new JPanel(new BorderLayout(0, 1));
		
		if(first){
			SortByID(false);
			first = false;
		}
		
		String[] columnNames = { "ID", "Name", "Phone Number", "Address", "VAT Number", "Last Purchase", "Date Added"};
		Object[][] data = new Object[ShopDriver.customers.size()][7];
		
		for(int i=0; i<ShopDriver.customers.size(); i++){

			data[i][0] = doubleFormatter.format(ShopDriver.customers.get(i).getCustomerID());
			data[i][1] = ShopDriver.customers.get(i).getCustomerName();
			data[i][2] = ShopDriver.customers.get(i).getPhoneNumber();
			data[i][3] = ShopDriver.customers.get(i).getAddress();
			data[i][4] = ShopDriver.customers.get(i).getVatNumber();
			data[i][5] = ShopDriver.customers.get(i).getLastPurchase();
			data[i][6] = ShopDriver.customers.get(i).getDateAdded();
		}
		
		
		final JTable table = new JTable(data, columnNames);
		table.setColumnSelectionAllowed(false);
		table.setFillsViewportHeight(true);
		
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(table.getSelectedRow() >= 0){ 				
					Customer input = null;
					
					for(Customer c : ShopDriver.customers){
						if(c.getCustomerID() == Integer.parseInt((String) table.getValueAt(table.getSelectedRow(), 0)))
							input = c;
					}
					showCustomerInfo(input);
				}
			}
		});
		
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBackground(ShopDriver.backgroundColour);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ShopDriver.backgroundColour);
		
		String[] customerMemberArrayEdit = new String[ShopDriver.customers.size()+1];
		customerMemberArrayEdit[0] = "Edit Customer";
		for(int i = 0; i < ShopDriver.customers.size()+1; i++){
			if(i < ShopDriver.customers.size())
				customerMemberArrayEdit[i+1] = "ID: " + doubleFormatter.format(ShopDriver.customers.get(i).getCustomerID()) + " (" + ShopDriver.customers.get(i).getCustomerName() + ")";
		}
		
		String[] customerMemberArrayDelete = new String[ShopDriver.customers.size()+1];
		customerMemberArrayDelete[0] = "Delete Customer";
		for(int i = 0; i < ShopDriver.customers.size()+1; i++){
			if(i < ShopDriver.customers.size())
				customerMemberArrayDelete[i+1] = "ID: " + doubleFormatter.format(ShopDriver.customers.get(i).getCustomerID()) + " (" + ShopDriver.customers.get(i).getCustomerName() + ")";
		}
		
		
		JButton addButton = new JButton("Add Customer");
		final JComboBox<String> editBox = new JComboBox<String>(customerMemberArrayEdit);
		final JComboBox<String> deleteBox = new JComboBox<String>(customerMemberArrayDelete);
		
		String[] options= {"Sort by...", "ID", "Name", "Address", "VAT Number", "Last Purchase", "Date Added"};
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
		if(type.equals("VAT Number"))
			index = 4;
		if(type.equals("Last Purchase"))
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
				edit(Integer.parseInt(((String) editBox.getSelectedItem()).substring(4, 9)));
			}
		});
		
		deleteBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				delete(Integer.parseInt(((String) deleteBox.getSelectedItem()).substring(4, 9)),
										  ((String) deleteBox.getSelectedItem()).substring(11, ((String) deleteBox.getSelectedItem()).length()-1));
			}
		});
		
		sortOptions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
			
				if(sortOptions.getSelectedItem().equals("Sort by...")){}
				
				else if(type.equals((String) sortOptions.getSelectedItem())){
					if(reverse)
						reverse = false;
					else reverse = true;
					sortArrayList();
				}
				
				else{
					type = (String) sortOptions.getSelectedItem();
					sortArrayList();
				}
				customer();
			}
		});
		
		buttonPanel.add(addButton);
		buttonPanel.add(editBox);
		buttonPanel.add(deleteBox);
		buttonPanel.add(sortOptions);
		
		ShopDriver.mainPanel.add(scrollPane, BorderLayout.NORTH);
		ShopDriver.mainPanel.add(buttonPanel, BorderLayout.CENTER);
		
		ShopDriver.setFrame(false, false, true);
	}
	
	/**
	 * This method builds a form for adding
	 * a <code>Customer</code>. It also has the 
	 * logic to add a <code>Customer</code> to
	 * an <code>ArrayList</code> of customers.
	 */
	public static void add(){
		
		ShopDriver.frame.remove(ShopDriver.leftPanel);
		ShopDriver.frame.repaint();
		ShopDriver.leftPanel = new JPanel();
		
		
		JPanel innerPanel = new JPanel(new GridBagLayout());
		innerPanel.setBackground(ShopDriver.backgroundColour);

		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0;  
        g.gridy = 0;  
        innerPanel.add(new JLabel("Customer ID:"), g);  
        g.gridy = 1;  
        innerPanel.add(new JLabel("Customer Name:"), g);  
        g.gridy = 2;  
        innerPanel.add(new JLabel("Phone Number:"), g);  
        g.gridy = 3;  
        innerPanel.add(new JLabel("Address:"), g);  
        g.gridy = 4;  
        innerPanel.add(new JLabel("VAT Number:"), g);  
        g.gridy = 5;  
        innerPanel.add(new JLabel("Last Purchase:"), g);  
        g.gridy = 6;  
        innerPanel.add(new JLabel("Date Added:"), g);  
            
        g.insets = new Insets(1, 10, 0, 5); 					
        
        g.gridx = 1;  
        g.gridy = 0; 
        g.gridwidth=3;
        final JTextField custIDField = new JTextField(null, 20);  
        custIDField.setEditable(false);
        custIDField.setText(doubleFormatter.format(firstAvailableID()));
        innerPanel.add(custIDField, g);  
        g.gridy = 1;  
        g.gridwidth=3;
        final JTextField custNameField = new JTextField(null, 20);  
        innerPanel.add(custNameField, g);  
        g.gridy = 2; 
        g.gridwidth=3;
        final JTextField phoneNoField = new JTextField(null, 20);  
        innerPanel.add(phoneNoField, g);  
        g.gridy = 3; 
        g.gridwidth=3;
        final JTextField addressField = new JTextField(null, 20);  
        innerPanel.add(addressField, g);      
        g.gridy = 4;  
        g.gridwidth=3;
        final JTextField vatNoField = new JTextField(null, 20);  
        innerPanel.add(vatNoField, g);  
        g.gridy = 5;   
        g.gridx = 1;
        g.gridwidth=1;
        final JComboBox<String> lastPurchaseDay = new JComboBox<String>(ShopDriver.days); 
        innerPanel.add(lastPurchaseDay, g);
      
        g.gridy = 5;
        g.gridx = 2;
        g.gridwidth=1-2;
        final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(ShopDriver.months); 
        innerPanel.add(lastPurchaseMonth, g); 
        
        g.gridy = 5;
        g.gridx = 3;
        g.gridwidth=2-3;
        
        final JComboBox<String> lastPurchaseYear = new JComboBox<String>(ShopDriver.years); 
        innerPanel.add(lastPurchaseYear, g); 
        
        g.gridy = 6;   
        g.gridx = 1;
        g.gridwidth=1;
        final JComboBox<String> dateAddedDay = new JComboBox<String>(ShopDriver.days); 
        innerPanel.add(dateAddedDay, g);
      
        g.gridy = 6;
        g.gridx = 2;
        g.gridwidth=1-2;
        final JComboBox<String> dateAddedMonth = new JComboBox<String>(ShopDriver.months); 
        innerPanel.add(dateAddedMonth, g); 
        
        g.gridy = 6;
        g.gridx = 3;
        g.gridwidth=2-3;
        
        final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.years); 
        innerPanel.add(dateAddedYear, g);
        
        dateAddedDay.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(0, 2)));
	    dateAddedMonth.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(3, 5)));
	    dateAddedYear.setSelectedIndex(Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).substring(6, 10))-(ShopDriver.yearStart-1));       
            
		g.gridx = 0;
		g.gridy = 8;		
		innerPanel.add(new JLabel(" "), g);
		g.gridx = 1;
		g.gridy = 8;
		innerPanel.add(new JLabel(" "), g);
		
		JButton save = new JButton("Save");
		save.setLayout(new GridBagLayout());
		g = new GridBagConstraints();
		g.insets = new Insets(1, 0, 0, 0);
		g.gridx = 1;
		g.gridy = 9;
		innerPanel.add(save, g);
		JButton cancel = new JButton("Cancel");
		g.gridx = 3;
		g.gridy = 9;
		innerPanel.add(cancel, g);

        save.addActionListener(new ActionListener(){             
            public void actionPerformed(ActionEvent e){   
            	
            	ArrayList<JTextField> textFields = new ArrayList<JTextField>();
            	textFields.add(custNameField);
            	textFields.add(phoneNoField);
            	textFields.add(addressField);
            	textFields.add(vatNoField);
            	ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
            	addedBoxes.add(dateAddedDay);
				addedBoxes.add(dateAddedMonth);
				addedBoxes.add(dateAddedYear);
            	ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<JComboBox<String>>();
            	lastPurchaseBoxes.add(lastPurchaseDay);
				lastPurchaseBoxes.add(lastPurchaseMonth);
				lastPurchaseBoxes.add(lastPurchaseYear);
            	
				boolean valid = ShopDriver.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes);
				
				if(valid){
					ShopDriver.customers.add(new Customer(Integer.parseInt(custIDField.getText()),   
														  custNameField.getText(),
														  phoneNoField.getText(),  
														  addressField.getText(),
														  vatNoField.getText(),   
														  lastPurchaseDay.getSelectedItem() + "/" +
																  lastPurchaseMonth.getSelectedItem() + "/" +
																  lastPurchaseYear.getSelectedItem(),
														  dateAddedDay.getSelectedItem() + "/" +
																  dateAddedMonth.getSelectedItem() + "/" +
																  dateAddedYear.getSelectedItem()
														  )
											);
					
					ShopDriver.setConfirmMessage("Customer " + custNameField.getText() + " added");
					ShopDriver.frame.remove(ShopDriver.leftPanel);
	                ShopDriver.frame.repaint();  
	                ShopDriver.frame.validate(); 
		                          
	                SortByID(false);
	                customer();
				}
            }  
        });  
        
        cancel.addActionListener(new ActionListener(){  
            public void actionPerformed(ActionEvent e){  
                ShopDriver.frame.remove(ShopDriver.leftPanel);  
                ShopDriver.frame.repaint();  
                ShopDriver.frame.validate();
            }  
        }); 
		
		ShopDriver.leftPanel.add(innerPanel);
		
		ShopDriver.setFrame(true, false, false);
	}
	
	/**
	 * This method builds a form for updating
	 * a <code>Customer</code>'s details. 
	 * It also has the logic to update a 
	 * <code>Customer</code>'s details in the
	 * <code>ArrayList</code> of customers.
	 */
	public static void edit(int customerID){
		
		ShopDriver.frame.remove(ShopDriver.leftPanel);
		ShopDriver.frame.repaint();
		ShopDriver.leftPanel = new JPanel();
		
		for(Customer c : ShopDriver.customers){
   			if (customerID == c.getCustomerID()){
   				final int index = ShopDriver.customers.indexOf(c);
                
                final JPanel innerPanel = new JPanel(new GridBagLayout());  
                innerPanel.setBackground(ShopDriver.backgroundColour);
                GridBagConstraints g = new GridBagConstraints();  
                    
                  
                g.gridx = 0;  
                g.gridy = 0;  
                innerPanel.add(new JLabel("Customer ID:"), g);  
                g.gridy = 1;  
                innerPanel.add(new JLabel("Customer Name:"), g);  
                g.gridy = 2;  
                innerPanel.add(new JLabel("Phone Number:"), g);  
                g.gridy = 3;  
                innerPanel.add(new JLabel("Address:"), g);  
                g.gridy = 4;  
                innerPanel.add(new JLabel("VAT Number:"), g);  
                g.gridy = 5;  
                innerPanel.add(new JLabel("Last Purchase:"), g);  
                g.gridy = 6;  
                innerPanel.add(new JLabel("Date Added:"), g);  
                
                g.insets = new Insets(1, 10, 0, 5); 
                   
                  
                g.gridx = 1;  
                g.gridy = 0; 
                g.gridwidth=3;
                final JTextField custIDField = new JTextField(null, 20);
                custIDField.setEditable(false);
                innerPanel.add(custIDField, g);  
                g.gridy = 1; 
                g.gridwidth=3;
                final JTextField custNameField = new JTextField(null, 20);  
                innerPanel.add(custNameField, g);  
                g.gridy = 2;
                g.gridwidth=3;
                final JTextField phoneNoField = new JTextField(null, 20);  
                innerPanel.add(phoneNoField, g);  
                g.gridy = 3; 
                g.gridwidth=3;
                final JTextField addressField = new JTextField(null, 20);  
                innerPanel.add(addressField, g);      
                g.gridy = 4;  
                g.gridwidth=3;
                final JTextField vatNoField = new JTextField(null, 20);  
                innerPanel.add(vatNoField, g);  
                
                g.gridy = 5;   
                g.gridx = 1;
                g.gridwidth=1;
                final JComboBox<String> lastPurchaseDay = new JComboBox<String>(ShopDriver.days); 
                innerPanel.add(lastPurchaseDay, g);
              
                g.gridy = 5;
                g.gridx = 2;
                g.gridwidth=1-2;
                final JComboBox<String> lastPurchaseMonth = new JComboBox<String>(ShopDriver.months); 
                innerPanel.add(lastPurchaseMonth, g); 
                
                g.gridy = 5;
                g.gridx = 3;
                g.gridwidth=2-3;
                
                final JComboBox<String> lastPurchaseYear = new JComboBox<String>(ShopDriver.years); 
                innerPanel.add(lastPurchaseYear, g); 
                
                g.gridy = 6;   
                g.gridx = 1;
                g.gridwidth=1;
                final JComboBox<String> dateAddedDay = new JComboBox<String>(ShopDriver.days); 
                innerPanel.add(dateAddedDay, g);
              
                g.gridy = 6;
                g.gridx = 2;
                g.gridwidth=1-2;
                final JComboBox<String> dateAddedMonth = new JComboBox<String>(ShopDriver.months); 
                innerPanel.add(dateAddedMonth, g); 
                
                g.gridy = 6;
                g.gridx = 3;
                g.gridwidth=2-3;
                
                final JComboBox<String> dateAddedYear = new JComboBox<String>(ShopDriver.years); 
                innerPanel.add(dateAddedYear, g);
                    
                  
                custIDField.setText("" + c.getCustomerID());  
                custNameField.setText(c.getCustomerName());  
                phoneNoField.setText(c.getPhoneNumber());  
                addressField.setText(c.getAddress());  
                vatNoField.setText(c.getVatNumber()); 
                lastPurchaseDay.setSelectedIndex(Integer.parseInt(c.getLastPurchase().substring(0, 2)));
                lastPurchaseMonth.setSelectedIndex(Integer.parseInt(c.getLastPurchase().substring(3, 5)));
                lastPurchaseYear.setSelectedIndex(Integer.parseInt(c.getLastPurchase().substring(6, 10))-(ShopDriver.yearStart-1));
                dateAddedDay.setSelectedIndex(Integer.parseInt(c.getDateAdded().substring(0, 2)));
                dateAddedMonth.setSelectedIndex(Integer.parseInt(c.getDateAdded().substring(3, 5)));
                dateAddedYear.setSelectedIndex(Integer.parseInt(c.getDateAdded().substring(6, 10))-(ShopDriver.yearStart-1));
				

               
        		g.gridx = 0;
        		g.gridy = 8;		
        		innerPanel.add(new JLabel(" "), g);
        		g.gridx = 1;
        		g.gridy = 8;
        		innerPanel.add(new JLabel(" "), g);
                  
        		JButton save = new JButton("Save");
        		save.setLayout(new GridBagLayout());
        		g = new GridBagConstraints();
        		g.insets = new Insets(1, 0, 0, 0);
        		g.gridx = 1;
        		g.gridy = 9;
        		innerPanel.add(save, g);
        		JButton cancel = new JButton("Cancel");
        		g.gridx = 3;
        		g.gridy = 9;
        		innerPanel.add(cancel, g);
                
                save.addActionListener(new ActionListener(){
                	public void actionPerformed(ActionEvent e){
                		
                		ArrayList<JTextField> textFields = new ArrayList<JTextField>();
                    	textFields.add(custNameField);
                    	textFields.add(phoneNoField);
                    	textFields.add(addressField);
                    	textFields.add(vatNoField);
                    	ArrayList<JComboBox<String>> addedBoxes = new ArrayList<JComboBox<String>>();
                    	addedBoxes.add(dateAddedDay);
        				addedBoxes.add(dateAddedMonth);
        				addedBoxes.add(dateAddedYear);
                    	ArrayList<JComboBox<String>> lastPurchaseBoxes = new ArrayList<JComboBox<String>>();
                    	lastPurchaseBoxes.add(lastPurchaseDay);
        				lastPurchaseBoxes.add(lastPurchaseMonth);
        				lastPurchaseBoxes.add(lastPurchaseYear);
                    	
        				boolean valid = ShopDriver.checkFields(textFields, null, null, null, null, addedBoxes, lastPurchaseBoxes);
        				
        				if(valid){
							ShopDriver.customers.add(index, new Customer(Integer.parseInt(custIDField.getText()),   
																		 custNameField.getText(),   
																		 phoneNoField.getText(),  
																		 addressField.getText(),   
																		 vatNoField.getText(),   
																		 lastPurchaseDay.getSelectedItem() + "/" +
																				 lastPurchaseMonth.getSelectedItem() + "/" +
																				 lastPurchaseYear.getSelectedItem(),
																		 dateAddedDay.getSelectedItem() + "/" +
																				 dateAddedMonth.getSelectedItem() + "/" +
																				 dateAddedYear.getSelectedItem()
																		)
													);
								
							ShopDriver.setConfirmMessage("Customer " + custNameField.getText() + "'s details editted");
							ShopDriver.frame.remove(ShopDriver.leftPanel);  
		                    ShopDriver.frame.repaint();  
		                    ShopDriver.frame.validate();
		                    ShopDriver.customers.remove(index + 1);
	                    customer();
        				}
	                }  
	            });
                
                cancel.addActionListener(new ActionListener(){  
                    public void actionPerformed(ActionEvent e){  
                        ShopDriver.frame.remove(ShopDriver.leftPanel);  
                        ShopDriver.frame.repaint();  
                        ShopDriver.frame.validate();
                    }  
                });
                
				ShopDriver.leftPanel.add(innerPanel);   
            } 
        } 
		
		ShopDriver.setFrame(true, false, false);
	}
	
	/**
	 * This method shows a dialog box 
	 * asking a user if they want to delete
	 * a <code>Customer</code>. This method
	 * also has logic to remove the 
	 * <code>Customer</code> from system records.
	 */
	public static void delete(int customerID, String customerName){
		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Do you want to delete " + customerName + "?"));
		
		int i = -1;											
		
	    if (ShopDriver.showDialog("Please confirm", myPanel) == JOptionPane.OK_OPTION){
	    	
			ShopDriver.frame.remove(ShopDriver.leftPanel);
			ShopDriver.frame.repaint();
			ShopDriver.leftPanel = new JPanel();
			
	    	for(Customer c : ShopDriver.customers){
	    		if(customerID == c.getCustomerID())
	    			i = ShopDriver.customers.indexOf(c);	
		    }
	    }
	    
	    if (i != -1) 									 
			ShopDriver.setConfirmMessage(customerName + " deleted");
	    	ShopDriver.customers.remove(i);
	    
	   
	    customer();
		
		ShopDriver.frame.validate();
	}
	
	/**
	 * When a user clicks on a <code>Customer</code> in
	 * the customer table, the <code>Customer</code>'s
	 * details are brought up and a table is built 
	 * showing the orders that they have made. 
	 * 
	 * @param c Takes in a <code>Customer</code>'s details.
	 */
	public static void showCustomerInfo(Customer c){
	
		ShopDriver.frame.remove(ShopDriver.mainPanel);
		ShopDriver.frame.setTitle("Rocket Retail Inc - " + c.getCustomerName());
		ShopDriver.frame.repaint();
		ShopDriver.mainPanel = new JPanel(new BorderLayout(0, 1));
		
		JPanel titlePanel = new JPanel(new GridBagLayout());
		JPanel innerPanel = new JPanel(new BorderLayout(0, 1));
		JPanel buttonPanel = new JPanel();
		titlePanel.setBackground(ShopDriver.backgroundColour);
		innerPanel.setBackground(ShopDriver.backgroundColour);
		buttonPanel.setBackground(ShopDriver.backgroundColour);
		
		JLabel customerLabel = new JLabel("Customer");
		JLabel vatNumberLabel = new JLabel("VAT Number");
		JLabel phoneNoLabel = new JLabel("Phone Number");
		JLabel addressLabel = new JLabel("Address");
		JLabel dateAddedLabel = new JLabel("Date Added");
		JLabel titleLabel = new JLabel("Sales to " + c.getCustomerName());
		
		customerLabel.setFont(new Font(customerLabel.getFont().getFontName(), Font.BOLD, customerLabel.getFont().getSize()));
		vatNumberLabel.setFont(new Font(vatNumberLabel.getFont().getFontName(), Font.BOLD, vatNumberLabel.getFont().getSize()));
		phoneNoLabel.setFont(new Font(phoneNoLabel.getFont().getFontName(), Font.BOLD, phoneNoLabel.getFont().getSize()));
		addressLabel.setFont(new Font(addressLabel.getFont().getFontName(), Font.BOLD, addressLabel.getFont().getSize()));
		dateAddedLabel.setFont(new Font(dateAddedLabel.getFont().getFontName(), Font.BOLD, dateAddedLabel.getFont().getSize()));
		titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, titleLabel.getFont().getSize()));
		
		int textFieldSize = 20;
		
		JTextField customerField = new JTextField(c.getCustomerName() + " (" + doubleFormatter.format(c.getCustomerID()) + ")", textFieldSize);
		customerField.setEditable(false);
		JTextField vatNumberField = new JTextField(c.getVatNumber(), textFieldSize);
		vatNumberField.setEditable(false);
		
		JTextField phoneNoField = new JTextField(c.getPhoneNumber(), textFieldSize);
		phoneNoField.setEditable(false);
		JTextField addressField = new JTextField(c.getAddress(), textFieldSize);
		addressField.setEditable(false);
		JTextField dateAddedField = new JTextField(c.getDateAdded(), textFieldSize);
		dateAddedField.setEditable(false);
		
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(1, 10, 0, 5);
		titlePanel.add(customerLabel, g);
		g.gridx = 1;
		titlePanel.add(customerField, g);
		g.gridx = 4;
		titlePanel.add(vatNumberLabel, g);
		g.gridx = 5;
		titlePanel.add(vatNumberField, g);
		
		
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
		
		int count = 0;
		
		for(Order o : ShopDriver.orders){
			if(o.getTraderID() == c.getCustomerID())
				count++;
		}
		
		String[] columnNames = { "Order ID", "Order Date", "Delivery Date", "Total Cost"};
		Object[][] data = new Object[count + 1][4];
		int indexArray = 0;
		double total = 0;
		
		for(int i = 0; i < ShopDriver.orders.size(); i++){
			if(c.getCustomerID() == ShopDriver.orders.get(i).getTraderID()){

				data[indexArray][0] = doubleFormatter3.format(ShopDriver.orders.get(i).getOrderID()); 
				data[indexArray][1] = ShopDriver.orders.get(i).getOrderDate();
				data[indexArray][2] = ShopDriver.orders.get(i).getDeliveryDate();
				data[indexArray][3] = "€" + doubleFormatter2.format(ShopDriver.orders.get(i).getTotalSale());
				total += ShopDriver.orders.get(i).getTotalSale();
				indexArray++;
			}
		}
		data[count][2] = "<html><b>Total Sales</b></html>";
		data[count][3] = "<html><b>€" + doubleFormatter2.format(total) + "</b></html>";

		JTable table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		table.setEnabled(false);
		
		JScrollPane scrollPane = new JScrollPane(table);
		innerPanel.add(scrollPane, BorderLayout.CENTER);
		
		JButton backButton = new JButton("Back to Customers");
		backButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				customer();
			}
		});
		
		
		buttonPanel.add(backButton);
		innerPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		
		ShopDriver.mainPanel.add(titlePanel, BorderLayout.NORTH);
		ShopDriver.mainPanel.add(innerPanel, BorderLayout.CENTER);
		
		ShopDriver.setFrame(false, false, true);
	}
	
	
	/**
	 * 
	 * This method creates an unused <code>int</code>
	 * that can be used for a new <code>Customer</code>.
	 * 
	 * @return The first available id for creating
	 * a new <code>Customer</code>.
	 */
	public static int firstAvailableID(){
		type = "ID";
		SortByID(false);
		int output = 0, i = ShopDriver.customerIDStart;
		boolean found = false;

		while(!found && i < (ShopDriver.customers.size()+ShopDriver.customerIDStart)){
			if(ShopDriver.customers.get(i-ShopDriver.customerIDStart).getCustomerID() == i){}
			else{
				output = i;
				found = true;
			}
			i++;
		}
		
		if(found)
			return output;
		else return ShopDriver.customers.size()+ShopDriver.customerIDStart;
	}
	
	/**
	 * This method sorts the <code>Customer</code>s by their ID.
	 * 
	 * @param reverse A boolean to decide whether to add the <code>Customer</code>s
	 * to an <code>ArrayList</code> in ascending or descending order.
	 */
	
	public static void SortByID(boolean reverse){
		ArrayList<Customer> tempArrayList = new ArrayList<Customer>();
		int count = ShopDriver.customerIDStart, offset = 0;
		boolean found = false;
		
		for(int i = 0; i < ShopDriver.customers.size() + offset; i++){
			found = false;
			for(Customer p : ShopDriver.customers){
				if(count == p.getCustomerID()){
					tempArrayList.add(p);
					found = true;
				}
			}
			if(!found)
				offset++;
			count++;
		}
		
		ShopDriver.customers.clear();
		
		if(!reverse){
			for(int i = 0 ; i < tempArrayList.size(); i++){
				ShopDriver.customers.add(tempArrayList.get(i));
			}
		}
		else if (reverse){
			for(int i = tempArrayList.size()-1; i >= 0; i--){
				ShopDriver.customers.add(tempArrayList.get(i));
			}
		}
	}
	
	/**
	 * This method has a control structure
	 * to determine which sorting parameter
	 * was chosen to sort the <code>Customer</code>s by.
	 */
	public static void sortArrayList(){
		if(type.equals("ID"))
			SortByID(reverse);
		if(type.equals("Name"))
			SortByName(reverse);
		if(type.equals("Address"))
			SortByAddress(reverse);
		if(type.equals("VAT Number"))
			SortByVatNumber(reverse);
		if(type.equals("Last Purchase"))
			SortByDate(false, reverse);
		if(type.equals("Date Added"))
			SortByDate(true, reverse);
	}
	
	/**
	 * This method sorts the <code>Customer</code>s by name 
	 * using a <code>Comparator</code>.
	 * 
	 * @param reverse A boolean to decide whether the <code>Customer</code>s
	 * should be sorted in ascending or descending order.
	 */
	public static void SortByName(boolean reverse){
		if(!reverse){
			Collections.sort(ShopDriver.customers, new Comparator<Customer>(){
				public int compare(Customer s1, Customer s2){
					return s1.getCustomerName().compareToIgnoreCase(s2.getCustomerName());
				}
			});	
		}
		
		if(reverse){
			Collections.sort(ShopDriver.customers, new Comparator<Customer>(){
				public int compare(Customer s1, Customer s2){
					return s2.getCustomerName().compareToIgnoreCase(s1.getCustomerName());
				}
			});	
		}
	}
	
	/**
	 * This method sorts the <code>Customer</code>s by address 
	 * using a <code>Comparator</code>.
	 * 
	 * @param reverse A boolean to decide whether the <code>Customer</code>
	 * addresses should be sorted in ascending or descending order.
	 */
	public static void SortByAddress(boolean reverse){
		if(!reverse){
			Collections.sort(ShopDriver.customers, new Comparator<Customer>(){
				public int compare(Customer s1, Customer s2){
					return s1.getAddress().compareToIgnoreCase(s2.getAddress());
				}
			});	
		}
		
		if(reverse){
			Collections.sort(ShopDriver.customers, new Comparator<Customer>(){
				public int compare(Customer s1, Customer s2){
					return s2.getAddress().compareToIgnoreCase(s1.getAddress());
				}
			});	
		}
	}
	/**
	 * This method sorts the <code>Customer</code>s by Vat Number
	 * using a <code>Comparator</code>.
	 * 
	 * @param reverse A boolean to decide whether the <code>Customer</code>
	 * vat numbers should be sorted in ascending or descending order.
	 */
	public static void SortByVatNumber(boolean reverse){
		if(!reverse){
			Collections.sort(ShopDriver.customers, new Comparator<Customer>(){
				public int compare(Customer s1, Customer s2){
					return s1.getVatNumber().compareToIgnoreCase(s2.getVatNumber());
				}
			});
		}
		
		if(reverse){
			Collections.sort(ShopDriver.customers, new Comparator<Customer>(){
				public int compare(Customer s1, Customer s2){
					return s2.getVatNumber().compareToIgnoreCase(s1.getVatNumber());
				}
			});	
		}
	}
	
	/**
	 * This method sorts the <code>Customer</code>s by the date they
	 * were added. 
	 * 
	 * @param reverse A boolean to decide whether the <code>Customer</code>
	 * add dates should be sorted in ascending or descending order.
	 */
	public static void SortByDate(final boolean DateAdded, final boolean reverse){
		
		Collections.sort(ShopDriver.customers, new Comparator<Customer>() {
	        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
	        
			@Override
			public int compare(Customer c1, Customer c2) {
				try {
					String string1 = ""; 
					String string2 = "";
					
					if(DateAdded){
						string1 = c1.getDateAdded();
						string2 = c2.getDateAdded();
					}
					else{
						string1 = c1.getLastPurchase();
						string2 = c2.getLastPurchase();
					}
					if(!reverse)
						return f.parse(string1).compareTo(f.parse(string2));
					else return f.parse(string2).compareTo(f.parse(string1));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
	    });
	}
}