package dit.groupproject.rocketretail.entities;

import java.text.DecimalFormat;

/**
 * A class that is used to model a <code>Product</code>.
 */
public class Product {

    private final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,##0.00");

    private int productId, stockLevel, maxLevel, supplierId;
    private int startLevel;
    private double costPrice, salePrice;
    private String productDescription;

    public Product(final String productDescription, final int stockLevel, final int maxLevel, final int supplierId,
            final double costPrice, final double salePrice) {
        this.productId = IdManager.getProductIdAndIncrement();
        this.productDescription = productDescription;
        this.startLevel = stockLevel;
        this.maxLevel = maxLevel;
        this.supplierId = supplierId;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
    }

    public String printDetails() {
        return "Product ID:\t" + productId + "\nProduct Desc:\t" + productDescription + "\nStock Level:\t" + stockLevel
                + "\nMaximum Level:\t" + maxLevel + "\nSupplier ID:\t" + supplierId + "\nCost Price:\t€"
                + CURRENCY_FORMATTER.format(costPrice) + "\nSale Price:\t€" + CURRENCY_FORMATTER.format(salePrice)
                + "\n\n";
    }

    public int getStartLevel() {
        return startLevel;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public int getStockLevel() {
        return stockLevel;
    }

    public void setStockLevel(final int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public double getSalePrice() {
        return salePrice;
    }
}
