package shopFront;
import java.text.DecimalFormat;
/**
 * A class that is used to model
 * a <code>Product</code>
 */
public class Product {

	private int productID, stockLevel, maxLevel, supplierID;
	private int startLevel;
	private double costPrice, salePrice;
	private String productDesc;
	
	//Formatter
	/**
	 * A DecimalFormatter which formats Integers into Strings with the given formatting.
	 * Formats doubles to have commas, always show to two decimal places, and have at least two
	 * digits before the decimal point.
	 */
	static DecimalFormat doubleformatter = new DecimalFormat("#,###,##0.00");
	 
	/**
     * Creates the Product object.
     * 
     * @param productID (int) the unique identifier for the product
     * @param productDesc (String) the product description
     * @param stockLevel (int) the current stock level for that product
     * @param maxLevel (int) the max stock level for that product
     * @param supplierID (int) the ID for the supplier carrying that product
     * @param costPrice (double) the cost price for that product
     * @param salePrice (double) the sale price for that product 
     * */	
	public Product(int productID, String productDesc, int stockLevel, int maxLevel, int supplierID, double costPrice, double salePrice){
		this.productID = productID;
		this.productDesc = productDesc;
		this.startLevel = this.stockLevel = stockLevel;
		this.maxLevel = maxLevel;
		this.supplierID = supplierID;
		this.costPrice = costPrice;
		this.salePrice = salePrice;
	}
	/**
     * Prints product details to console.
     * */
	public String printDetails(){
		
		String output = "Product ID:\t" + productID +
						"\nProduct Desc:\t" + productDesc +
						"\nStock Level:\t" + stockLevel +
						"\nMaximum Level:\t" + maxLevel +
						"\nSupplier ID:\t"+ supplierID +
						"\nCost Price:\t€"+ doubleformatter.format(costPrice) +
						"\nSale Price:\t€"+ doubleformatter.format(salePrice) + "\n\n";
		
		return output;
	}
	/**
     * returns the startLevel (int)
     * */
	public int getStartLevel(){
		return startLevel;
	}
	/**
     * changes the productID attribute
     * @param startLevel (int)
     * */
	public void setStartLevel(int startLevel){
		this.startLevel = startLevel;
	}
	/**
     * returns the productID (int)
     * */
	public int getProductID() {
		return productID;
	}
	/**
     * changes the productID attribute
     * @param productID (int)
     * */
	public void setProductID(int productID) {
		this.productID = productID;
	}
	/**
     * returns the productDesc (int)
     * */
	public String getProductDesc() {
		return productDesc;
	}
	/**
     * changes the productDesc attribute
     * @param productDesc (String)
     * */
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	/**
     * returns the stockLevel (int)
     * */
	public int getStockLevel() {
		return stockLevel;
	}
	/**
     * changes the stockLevel attribute
     * @param stockLevel (int)
     * */
	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}
	/**
     * returns the supplierID (int)
     * */
	public int getSupplierID() {
		return supplierID;
	}
	/**
     * changes the supplierID attribute
     * @param supplierID (int)
     * */
	public void setSupplierID(int supplierID) {
		this.supplierID = supplierID;
	}
	/**
     * returns the costPrice (int)
     * */
	public double getCostPrice() {
		return costPrice;
	}
	/**
     * changes the costPrice attribute
     * @param costPrice (double)
     * */
	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}
	/**
     * returns the max level (int)
     * */
	public int getMaxLevel() {
		return maxLevel;
	}
	/**
     * changes the maxLevel attribute
     * @param maxLevel (int)
     * */
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	/**
     * returns the salePrice (double)
     * */
	public double getSalePrice() {
		return salePrice;
	}
	/**
     * changes the salePrice attribute
     * @param salePrice (double)
     * */
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}
}
