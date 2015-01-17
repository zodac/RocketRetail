package dit.groupproject.rocketretail.entities;

import java.util.Comparator;

public interface Entity {

    public Object[] getData();

    public int getNumberOfFields();

    public int getId();

    public void setId(final int id);

    public String getName();

    Comparator<Entity> compareById = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return s1.getId() - s2.getId();
        }
    };

    Comparator<Entity> compareByName = new Comparator<Entity>() {
        public int compare(final Entity s1, final Entity s2) {
            return s1.getName().compareToIgnoreCase(s2.getName());
        }
    };
}
