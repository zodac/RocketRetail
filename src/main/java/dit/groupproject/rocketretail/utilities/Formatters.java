package dit.groupproject.rocketretail.utilities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Formatters {
    public static final DecimalFormat CURRENCY_FORMATTER = new DecimalFormat("#,###,##0.00");
    public final static DecimalFormat ID_FORMATTER = new DecimalFormat("000000");

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    public static final DateFormat DAY_FORMATTER = new SimpleDateFormat("dd", Locale.UK);
    public static final DateFormat MONTH_FORMATTER = new SimpleDateFormat("MM", Locale.UK);
    public static final DateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy", Locale.UK);
}