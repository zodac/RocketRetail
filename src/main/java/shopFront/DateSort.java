package shopFront;

import java.util.ArrayList;

/**
 * The DateSort class fills two 2D arrays ({@link #custDates} and {@link #suppDates}) with sales and purchases by year and month.
 * These two arrays can be used by other classes for graphing or printing out sales/purchases over a period of time.
 */
public class DateSort {
	
	//Class variables
	/**
	 * A 2D array which stores sales by year and month
	 */
	static double custDates[][] = new double[ShopDriver.years.length-1][12];
	
	/**
	 * A 2D array which stores purchases by year and month
	 */
	static double suppDates[][] = new double[ShopDriver.years.length-1][12];
	
	/**
	 * An ArrayList<Order> which temporarily holds information from {@link ShopDriver#orders} to be manipulated by
	 * {@link #indexTotal(int, int)}.
	 * 
	 * @see			#indexTotal(int, int)
	 */
	private static ArrayList<Order> tempOrdersArrayList = new ArrayList<Order>();

	
	//Methods
	/**
	 * Copies orders from {@link ShopDriver#orders} into {@link #tempOrdersArrayList} and sorts by month and year.
	 * Initialises {@link #custDates} and {@link #suppDates}, then calls {@link #indexTotal(int, int)}
	 * 
	 * @param yearStart		an integer defining the start date for sales/purchases to be accessed
	 * @param yearEnd		an integer defining the end date for sales/purchases to be accessed
	 * 
	 * @see					ShopDriver#orders
	 * @see					#sortMonthOrderDate()
	 * @see					#sortYearOrderDate()
	 * @see					#indexTotal(int, int)
	 */
	public static void SortDate(int yearStart, int yearEnd){
		
		tempOrdersArrayList = new ArrayList<Order>();
		for(int i = 0 ; i < ShopDriver.orders.size(); i++){
			tempOrdersArrayList.add(ShopDriver.orders.get(i));
		}
		
		custDates = new double[ShopDriver.years.length-1][12];
		suppDates = new double[ShopDriver.years.length-1][12];
		
		sortMonthOrderDate();
		sortYearOrderDate();
		indexTotal(yearStart, yearEnd);
	}
	
	/**
	 * Prints out contents of {@link #custDates} and {@link #suppDates} between chosen years.
	 * 
	 * @param yearStart		an integer defining the start date for sales/purchases to be accessed
	 * @param yearEnd		an integer defining the end date for sales/purchases to be accessed
	 */
	public static void printDatesOrders(int yearStart, int yearEnd){		
		for(int i = yearStart-ShopDriver.yearStart; i < yearEnd-ShopDriver.yearStart; i++){
			System.out.println("Year " + (i+ShopDriver.yearStart) + "\n===============");
			for(int j = 0; j < 12; j++){
				if(custDates[i][j] != 0)
					System.out.println("Customer: " + custDates[i][j]);
				if(suppDates[i][j] != 0)
					System.out.println("Supplier: " + suppDates[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Sorts orders in {@link #tempOrdersArrayList} by month.
	 */
	public static void sortYearOrderDate(){
		double lowestYear = 9999;
		ArrayList<Order> tempYearArrayList = new ArrayList<Order>();
		int indexY = 0;
		tempYearArrayList.clear();
		boolean found;
		
		while(tempOrdersArrayList.size()!=0){
			for(Order s : tempOrdersArrayList){
				if(lowestYear > Integer.parseInt(s.getOrderDate().substring(6, 10))){
					lowestYear = Integer.parseInt(s.getOrderDate().substring(6, 10));
				}
			}
			found = false;
			for(Order s : tempOrdersArrayList){
				if(lowestYear == Integer.parseInt(s.getOrderDate().substring(6, 10)) && !found){
					tempYearArrayList.add(s);					
					indexY = tempOrdersArrayList.indexOf(s);
					found = true;
				}
				if(found)
					break;
			}
			tempOrdersArrayList.remove(indexY);
			lowestYear = 9999;
		}
		tempOrdersArrayList.clear();
		for(int i = 0 ; i < tempYearArrayList.size(); i++){
			tempOrdersArrayList.add(tempYearArrayList.get(i));
		}
	}
	
	/**
	 * Sorts orders in {@link #tempOrdersArrayList} by year.
	 */
	public static void sortMonthOrderDate(){
		double lowestMonth = 9999;
		ArrayList<Order> tempMonthArrayList = new ArrayList<Order>();
		int indexM = 0;
		tempMonthArrayList.clear();
		boolean found;
		
		while(tempOrdersArrayList.size()!=0){
			for(Order s : tempOrdersArrayList){
				if(lowestMonth > Integer.parseInt(s.getOrderDate().substring(3, 5))){
					lowestMonth = Integer.parseInt(s.getOrderDate().substring(3, 5));
				}
			}
			found = false;
			for(Order s : tempOrdersArrayList){
				if(lowestMonth == Integer.parseInt(s.getOrderDate().substring(3, 5)) && !found){
					tempMonthArrayList.add(s);					
					indexM = tempOrdersArrayList.indexOf(s);
					found = true;
				}
				if(found)
					break;
			}
			tempOrdersArrayList.remove(indexM);
			lowestMonth = 9999;
		}
		tempOrdersArrayList.clear();
		for(int i = 0 ; i < tempMonthArrayList.size(); i++){
			tempOrdersArrayList.add(tempMonthArrayList.get(i));
		}
	}
	
	/**
	 * Cycles through {@link ShopDriver#orders} and sorts sales and purchases into {@link #custDates} and {@link #suppDates}
	 * by year and month. Purchases are negative.
	 * 
	 * @param yearStart		an integer defining the start date for sales/purchases to be accessed
	 * @param yearEnd		an integer defining the end date for sales/purchases to be accessed
	 */
	public static void indexTotal(int yearStart, int yearEnd){

		ArrayList<Order> yearList = new ArrayList<Order>();
		ArrayList<Order> monthList = new ArrayList<Order>();
		
		double custTotal = 0;
		double suppTotal = 0;
		for(int i = (yearStart-ShopDriver.yearStart); i < (yearEnd-ShopDriver.yearStart); i++){	
			yearList = new ArrayList<Order>();
			for(int x = 0; x < tempOrdersArrayList.size(); x++){
				if(Integer.parseInt(tempOrdersArrayList.get(x).getOrderDate().substring(6, 10))-ShopDriver.yearStart == i){
					yearList.add(tempOrdersArrayList.get(x));
				}
			}
			
			for(int j = 0; j < 12; j++){
				custTotal = 0;
				suppTotal = 0;
				monthList = new ArrayList<Order>();
				
				for(int x = 0; x < yearList.size(); x++){
					if(Integer.parseInt(yearList.get(x).getOrderDate().substring(3, 5)) == (j+1)){
						monthList.add(yearList.get(x));
					}
				}
				
				for(Order o : monthList){
					for(int k = 0; k < o.getOrderedItems().size(); k++){
						if(o.isSupplier())
							suppTotal += o.getOrderedItems().get(k).getProduct().getCostPrice()*o.getOrderedItems().get(k).getQuantity();
						else custTotal += o.getOrderedItems().get(k).getProduct().getSalePrice()*o.getOrderedItems().get(k).getQuantity();
					}
				}
				
				suppDates[i][j] = (-1)*suppTotal;
				custDates[i][j] = custTotal;
			}
		}		
	}
}
