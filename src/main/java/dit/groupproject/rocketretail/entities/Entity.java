package dit.groupproject.rocketretail.entities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public interface Entity {

    final static DecimalFormat CUSTOMER_ID_FORMATTER = new DecimalFormat("00000");
    final static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public Object[] getData();

    public int getNumberOfFields();
}
