package shopFront;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Order {

	private int orderID;
	private int staffID;
	private int traderID;
	private String orderDate;
	private String deliveryDate = "";
	private boolean isSupplier;
	private boolean isActive = true;
	public ArrayList<OrderedItem> orderedItems = new ArrayList<OrderedItem>();
	
	public Order(int orderID, int staffID, int traderID, String orderDate, ArrayList<OrderedItem> orderedItems, boolean isActive) {
		
		this.orderID = orderID;
		this.staffID = staffID;
		this.traderID = traderID;
		this.orderDate = orderDate;
		this.orderedItems = orderedItems;
		this.isActive = isActive;
		
		if(traderID >= ShopDriver.customerIDStart && traderID < ShopDriver.productIDStart)
			isSupplier = false;
		else isSupplier = true;
		
		for(OrderedItem x : orderedItems){
			//If Customer order, reduce stock level
			if(traderID >= ShopDriver.customerIDStart && traderID < ShopDriver.productIDStart){
				if (x.getProduct().getStockLevel() < x.getQuantity()){}
				else x.getProduct().setStockLevel(x.getProduct().getStockLevel() - x.getQuantity());
			}
		}
				
		if(!isActive && isSupplier){
			for(OrderedItem oi : orderedItems){
				
				for(Product p : ShopDriver.products){
					if(oi.getProduct().getProductID() == p.getProductID())
						p.setStockLevel(p.getStockLevel() + oi.getQuantity());
				}
			}
		}
		
		
		DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        
		
		
		
		for(Supplier s : ShopDriver.suppliers){
			if(s.getSupplierID() == traderID){
				try {
					if(f.parse(s.getLastPurchase()).compareTo(f.parse(orderDate)) < 0)
						s.setLastPurchase(orderDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(Customer c : ShopDriver.customers){
			if(c.getCustomerID() == traderID){
				try {
					if(f.parse(c.getLastPurchase()).compareTo(f.parse(orderDate)) < 0)
						c.setLastPurchase(orderDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		
		if (!isActive)
			this.deliveryDate = orderDate;
	}
	
	public String printDetails(){
		String suppOrCustLabel = "";
		
		if(traderID >= ShopDriver.supplierIDStart && traderID < ShopDriver.customerIDStart)
			suppOrCustLabel = "Supplier";
		else if(traderID >= ShopDriver.customerIDStart && traderID < ShopDriver.productIDStart)
			suppOrCustLabel = "Customer";
		
		
		String output = "Order ID:\t" + orderID + 
						"\nStaff ID:\t" + staffID + 
						"\n" + suppOrCustLabel + " ID:\t" + traderID + 
						"\nOrder Date:\t" + orderDate;
		
		for(OrderedItem oi : orderedItems){
			output = output + "\nProduct ID:\t" + oi.getProduct().getProductID() + "\nQuantity:\t" + oi.getQuantity();
		}
		
		return (output + "\n\n");
	}
	
	public double getTotalCost(){
		
		double totalCost = 0;
		
		for(OrderedItem oi : this.orderedItems){
			totalCost += oi.getQuantity()*oi.getProduct().getCostPrice();
		}
		
		return totalCost;
	}
	
	public double getTotalSale(){
		double totalSale = 0;
		
		for(OrderedItem oi : this.orderedItems){
			totalSale += oi.getQuantity()*oi.getProduct().getSalePrice();
		}
		
		return totalSale;
	}
	
	public boolean includesProduct(Product p){
		
		for(OrderedItem oi : this.orderedItems){
			if(oi.getProduct().getProductID() == p.getProductID())
				return true;
		}
		
		return false;
	}
	
	public void completeOrder(String deliveryDate){
		
		this.deliveryDate = deliveryDate;
		
		for(OrderedItem oi : orderedItems){
			if(this.isSupplier()) //ifSupplier
				oi.getProduct().setStockLevel(oi.getProduct().getStockLevel() + oi.getQuantity());
		}
		
		this.isActive = false;
	}
	
	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getStaffID() {
		return staffID;
	}

	public void setStaffID(int staffID) {
		this.staffID = staffID;
	}

	public int getTraderID() {
		return traderID;
	}

	public void setTraderID(int traderID) {
		this.traderID = traderID;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public ArrayList<OrderedItem> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(ArrayList<OrderedItem> orderedItems) {
		this.orderedItems = orderedItems;
	}

	public boolean isSupplier() {
		return isSupplier;
	}

	public void setSupplier(boolean isSupplier) {
		this.isSupplier = isSupplier;
	}
}
