package dit.groupproject.rocketretail.entities;

import java.util.concurrent.atomic.AtomicInteger;

public class IdManager {

    public static final int CUSTOMER_ID_START = 10000;
    public static final int ORDER_ID_START = 0;
    public static final int PRODUCT_ID_START = 20000;
    public static final int STAFF_ID_START = 0;
    public static final int SUPPLIER_ID_START = 1000;

    private static AtomicInteger nextCustomerId = new AtomicInteger(CUSTOMER_ID_START);
    private static AtomicInteger nextOrderId = new AtomicInteger(ORDER_ID_START);
    private static AtomicInteger nextProductId = new AtomicInteger(PRODUCT_ID_START);
    private static AtomicInteger nextStaffId = new AtomicInteger(STAFF_ID_START);
    private static AtomicInteger nextSupplierId = new AtomicInteger(SUPPLIER_ID_START);

    public static int getCustomerIdAndIncrement() {
        return nextCustomerId.getAndIncrement();
    }

    public static int getOrderIdAndIncrement() {
        return nextOrderId.getAndIncrement();
    }

    public static int getProductIdAndIncrement() {
        return nextProductId.getAndIncrement();
    }

    public static int getStaffIdAndIncrement() {
        return nextStaffId.getAndIncrement();
    }

    public static int getSupplierIdAndIncrement() {
        return nextSupplierId.getAndIncrement();
    }
}
