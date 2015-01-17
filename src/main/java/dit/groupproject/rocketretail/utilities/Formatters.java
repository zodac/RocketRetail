package dit.groupproject.rocketretail.utilities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Formatters {
    public static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,#00.00");
    public final static DecimalFormat ID_FORMATTER = new DecimalFormat("000000");

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat DAY_FORMATTER = new SimpleDateFormat("dd");
    public static final DateFormat MONTH_FORMATTER = new SimpleDateFormat("MM");
    public static final DateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy");
}