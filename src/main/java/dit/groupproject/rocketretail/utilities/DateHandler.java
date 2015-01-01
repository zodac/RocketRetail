package dit.groupproject.rocketretail.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHandler {

    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat DAY_FORMATTER = new SimpleDateFormat("dd");
    public static final DateFormat MONTH_FORMATTER = new SimpleDateFormat("MM");
    public static final DateFormat YEAR_FORMATTER = new SimpleDateFormat("yyyy");

    public static final int YEAR_START = 2000;
    public static final int YEAR_CURRENT = Integer.parseInt(YEAR_FORMATTER.format(new Date()));

    public static final String[] DAYS_AS_NUMBERS = { "", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28",
            "29", "30", "31" };
    public static final String[] MONTHS_AS_NUMBERS = { "", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12" };
    public static final String[] YEARS_AS_NUMBERS = { "", "2000", "2001", "2002", "2003", "2004", "2005", "2006",
            "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019",
            "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" };

}
