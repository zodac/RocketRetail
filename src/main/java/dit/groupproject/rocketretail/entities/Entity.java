package dit.groupproject.rocketretail.entities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;

public interface Entity {

    final static DecimalFormat CUSTOMER_ID_FORMATTER = new DecimalFormat("00000");
    final static DecimalFormat SUPPLIER_ID_FORMATTER = new DecimalFormat("0000");

    final static DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public Object[] getData();

    public int getNumberOfFields();

    public int getId();

    public void setId(final int id);

    Comparator<Entity> compareById = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return s1.getId() - s2.getId();
        }
    };
}
