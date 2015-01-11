package dit.groupproject.rocketretail.entities;

import java.util.Comparator;

/**
 * A class that is used to model a <code>Product</code>.
 */
public class Product implements Entity {

    private int productId;
    private int stockLevel;
    private int maxLevel;
    private int supplierId;
    private int startLevel;
    private double costPrice, salePrice;
    private String productName;

    public Product(final String productName, final int stockLevel, final int maxLevel, final int supplierId, final double costPrice,
            final double salePrice) {
        this.productId = IdManager.getProductIdAndIncrement();
        this.productName = productName;
        this.startLevel = stockLevel;
        this.maxLevel = maxLevel;
        this.supplierId = supplierId;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
    }

    @Override
    public int getId() {
        return productId;
    }

    @Override
    public void setId(final int id) {
        productId = id;
    }

    public int getStartLevel() {
        return startLevel;
    }

    @Override
    public String getName() {
        return productName;
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

    public static Comparator<Entity> getComparator(final String sortType) {
        if (sortType.equals("Description")) {
            return compareByName;
        } else if (sortType.equals("Stock Level")) {
            return compareByStockLevel;
        } else if (sortType.equals("Supplier ID")) {
            return compareBySupplierId;
        } else if (sortType.equals("Cost Price")) {
            return compareByCostPrice;
        } else if (sortType.equals("Sale Price")) {
            return compareBySalePrice;
        } else {
            return compareById;
        }
    }

    private static Comparator<Entity> compareByStockLevel = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Product) s1).getStockLevel() - ((Product) s2).getStockLevel();
        }
    };

    private static Comparator<Entity> compareBySupplierId = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return ((Product) s1).getSupplierId() - ((Product) s2).getSupplierId();
        }
    };

    private static Comparator<Entity> compareByCostPrice = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return (int) (((Product) s1).getCostPrice() - ((Product) s2).getCostPrice());
        }
    };

    private static Comparator<Entity> compareBySalePrice = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return (int) (((Product) s1).getSalePrice() - ((Product) s2).getSalePrice());
        }
    };

    @Override
    public Object[] getData() {
        final Object[] data = new Object[getNumberOfFields()];
        data[0] = PRODUCT_ID_FORMATTER.format(productId);
        data[1] = productName;
        data[2] = stockLevel;
        data[3] = maxLevel;
        data[4] = supplierId;
        data[5] = costPrice;
        data[6] = salePrice;

        return data;
    }

    @Override
    public int getNumberOfFields() {
        return 7;
    }
}
