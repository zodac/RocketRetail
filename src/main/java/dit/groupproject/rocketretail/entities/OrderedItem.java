package dit.groupproject.rocketretail.entities;

/**
 * This class models an ordered item, consisting of one product and the ordered
 * amount of that product.
 */
public class OrderedItem {

    private Product product;
    private int quantity;

    public OrderedItem(final Product product, final int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
