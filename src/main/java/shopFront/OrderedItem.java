package shopFront;

/**
 * 
 * This class models an ordered
 * item, consisting of one product
 * and the ordered amount of that
 * product. 
 *
 * @author Sheikh
 * @author Tony
 * @author Jason
 * @author Alan
 * @author Jessica
 * 
 * @version 2.0
 * @since 1.0
 *
 */

public class OrderedItem {
	
	/**
	 * The <code>Product</code> to be ordered.
	 */
	private Product product;
	/**
	 * The quantity of the <code>Product</code> to be ordered.
	 */	
	private int quantity;
	
	/**
	 * Constructs an <code>OrderedItem</code> with a <code>Product</code>
	 * and quantity.
	 * 
	 * @param product
	 * @param quantity
	 */
	public OrderedItem(Product product, int quantity){
		this.product = product;
		this.quantity = quantity;
	}
	
	/**
	 * 
	 * @return A <code> String </code> giving the details of the <code>OrderedItem</code>.
	 */
	public String printDetails(){
		return "\nProduct Name: " + product.getProductDesc() + "   Quantity: " + quantity + " items";
	}
	/**
	 * 
	 * @return The <code>Product</code> in the  <code>OrderedItem</code>.
	 */
	public Product getProduct() {
		return product;
	}
	/**
	 * 
	 * @param product Sets the <code>Product</code> in the  <code>OrderedItem</code>.
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
    /**
     * 
     * @return An <code>int</code> representing the ordered amount of the <code>Product</code>.
     */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * 
	 * @param quantity Sets the quantity of the <code>Product</code> to order.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
