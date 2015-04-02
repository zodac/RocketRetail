package dit.groupproject.rocketretail.entities;

import static dit.groupproject.rocketretail.utilities.Formatters.CURRENCY_FORMATTER;
import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

import java.text.NumberFormat;
import java.util.Comparator;

/**
 * A class that is used to model a <code>Product</code>.
 */
public class Product implements Entity {

    private int productId;
    private int currentStockLevel;
    private int maxStockLevel;
    private int supplierId;
    private int startLevel;
    private double costPrice, salePrice;
    private String productName;

    public Product(final String productName, final int currentStockLevel, final int maxStockLevel, final int supplierId, final double costPrice,
            final double salePrice) {
        this.productId = IdManager.getProductIdAndIncrement();
        this.productName = productName;
        this.startLevel = currentStockLevel;
        this.maxStockLevel = maxStockLevel;
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

    public int getCurrentStockLevel() {
        return currentStockLevel;
    }

    public void setCurrentStockLevel(final int currentStockLevel) {
        this.currentStockLevel = currentStockLevel;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public int getMaxStockLevel() {
        return maxStockLevel;
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
            return ((Product) s1).getCurrentStockLevel() - ((Product) s2).getCurrentStockLevel();
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
        data[0] = ID_FORMATTER.format(productId);
        data[1] = productName;
        data[2] = NumberFormat.getIntegerInstance().format(currentStockLevel);
        data[3] = NumberFormat.getIntegerInstance().format(maxStockLevel);
        data[4] = supplierId;
        data[5] = CURRENCY_FORMATTER.format(costPrice);
        data[6] = CURRENCY_FORMATTER.format(salePrice);

        return data;
    }

    @Override
    public int getNumberOfFields() {
        return 7;
    }
}
