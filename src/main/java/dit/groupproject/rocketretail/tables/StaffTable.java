package dit.groupproject.rocketretail.tables;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entities.Staff;
import dit.groupproject.rocketretail.main.TableState;

/**
 * A class that is used to display a table with multiple {@link Staff} entries.
 * It offers sorting options and options to add, edit and delete staff members.
 */
public class StaffTable extends AbstractTable {

    public boolean firstTimeLoadingTable = true;
    public boolean descendingOrderSort = false;
    private String currentSortOption = "Sort by...";

    private static StaffTable instance = null;

    public static StaffTable getInstance() {
        if (instance == null) {
            instance = new StaffTable();
        }
        return instance;
    }

    /**
     * This method creates a Staff menu item and adds an
     * <code>ActionListener</code> to it.
     * 
     * @return A <code>JMenuItem</code> to be added to a <code>JMenu</code>.
     */
    public JMenuItem createMenu(final TableState newState, final boolean manager) {
        final JMenuItem menuItem = new JMenuItem(newState.toString());

        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                createTableGui();
            }
        });

        menuItem.setEnabled(manager);
        return menuItem;
    }

    /**
     * Shows the table of staff members and their details. Sorts the table by ID
     * on first run. Options to sort table by ID, Name, Address, Wage, Level and
     * Date Added. Options to Add, Edit and Delete Staff Members.
     */
    @Override
    public void createTableGui() {
        setTableState(TableState.STAFF);
        resetGui();

        if (firstTimeLoadingTable) {
            sortItems();
            firstTimeLoadingTable = false;
        }

        final String[] staffColumnNames = { "ID", "Name", "Gender", "Phone Number", "Address", "Wage", "Level", "Date Added" };
        final Object[][] data = createTableData(Database.getStaffMembers());
        final JScrollPane scrollPane = createScrollableTable(data, staffColumnNames);
        final JComboBox<String> sortOptions = createSortOptions();
        final JPanel buttonPanel = createButtonPanel(TableState.STAFF.toString(), sortOptions);

        updateGui(scrollPane, buttonPanel);
    }

    @Override
    public void sortItems() {
        Comparator<Entity> comparator = Staff.getComparator(currentSortOption);

        if (descendingOrderSort) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(Database.getStaffMembers(), comparator);
    }

    @Override
    protected void reverseSortOrder() {
        descendingOrderSort = !descendingOrderSort;
    }

    @Override
    protected String getCurrentSortOption() {
        return currentSortOption;
    }

    @Override
    protected void setCurrentSortOption(final String sortOption) {
        this.currentSortOption = sortOption;
    }

    @Override
    protected String[] getSortOptionTitles() {
        return new String[] { "Sort by...", "ID", "Name", "Address", "Wage", "Level", "Date Added" };
    }
}