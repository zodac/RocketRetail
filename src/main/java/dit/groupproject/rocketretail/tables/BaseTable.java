package dit.groupproject.rocketretail.tables;

import java.text.DecimalFormat;

public abstract class BaseTable {

    protected final static DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,#00.00");
    protected final static DecimalFormat CUSTOMER_ID_FORMATTER = new DecimalFormat("00000");
    protected final static DecimalFormat ORDER_ID_FORMATTER = new DecimalFormat("0000");
    protected final static DecimalFormat PRODUCT_ID_FORMATTER = new DecimalFormat("00000");
    protected final static DecimalFormat STAFF_ID_FORMATTER = new DecimalFormat("000");
    protected final static DecimalFormat SUPPLIER_ID_FORMATTER = new DecimalFormat("0000");

    protected final static String[] DAYS_AS_NUMBERS = { "", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31" };
    protected final static String[] MONTHS_AS_NUMBERS = { "", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12" };
}
