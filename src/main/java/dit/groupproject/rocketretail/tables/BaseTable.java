package dit.groupproject.rocketretail.tables;

import static dit.groupproject.rocketretail.utilities.Formatters.ID_FORMATTER;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import dit.groupproject.rocketretail.database.Database;
import dit.groupproject.rocketretail.entities.Entity;
import dit.groupproject.rocketretail.entityhelpers.AddEntityHelper;
import dit.groupproject.rocketretail.entityhelpers.DeleteEntityHelper;
import dit.groupproject.rocketretail.entityhelpers.EditEntityHelper;
import dit.groupproject.rocketretail.entityhelpers.ViewEntityHelper;
import dit.groupproject.rocketretail.gui.GuiCreator;
import dit.groupproject.rocketretail.main.ShopDriver;
import dit.groupproject.rocketretail.main.TableState;

public abstract class BaseTable {

    protected static int showDialog(final String title, final JPanel myPanel) {
        return JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
    }

    protected static Object[][] createTableData(final ArrayList<Entity> items) {
        final Object[][] data = new Object[items.size()][items.get(0).getNumberOfFields()];
        int dataIndex = 0;

        for (final Entity item : items) {
            data[dataIndex++] = item.getData();
        }
        return data;
    }

    protected static void setTableState(final TableState newState) {
        if (ShopDriver.getCurrentTableState() != newState) {
            GuiCreator.frame.remove(GuiCreator.leftPanel);
        }
        ShopDriver.setCurrentTable(newState);
    }

    protected static void resetGui() {
        GuiCreator.frame.remove(GuiCreator.mainPanel);
        GuiCreator.frame.setTitle("Rocket Retail Inc - " + ShopDriver.getCurrentTableState().toString());
        GuiCreator.frame.repaint();
        GuiCreator.mainPanel = new JPanel(new BorderLayout(0, 1));
    }

    protected static void updateGui(final JScrollPane scrollPane, final JPanel buttonPanel) {
        GuiCreator.mainPanel.add(scrollPane, BorderLayout.NORTH);
        GuiCreator.mainPanel.add(buttonPanel, BorderLayout.CENTER);
        GuiCreator.setFrame(false, false, true);
    }

    protected static JButton createAddButton(final String addButtonTitle) {
        final JButton addButton = new JButton(addButtonTitle);
        addButton.addActionListener(AddEntityHelper.addEntityPanel());
        return addButton;
    }

    protected static JComboBox<String> createEditBox(final String[] itemsToEdit) {
        final JComboBox<String> editBox = new JComboBox<String>(itemsToEdit);
        editBox.addActionListener(EditEntityHelper.editEntityPanel(editBox));
        return editBox;
    }

    protected static JComboBox<String> createDeleteBox(final String[] itemsToDelete) {
        final JComboBox<String> deleteBox = new JComboBox<String>(itemsToDelete);
        deleteBox.addActionListener(DeleteEntityHelper.deleteEntityPanel(deleteBox));
        return deleteBox;
    }

    protected static JScrollPane createScrollableTable(final Object[][] data, final String[] columnNames) {
        final JTable table = new JTable(data, columnNames);
        table.setColumnSelectionAllowed(false);
        table.setFillsViewportHeight(true);
        table.addMouseListener(ViewEntityHelper.viewEntityTable(table));

        final JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(GuiCreator.BACKGROUND_COLOUR);
        return scrollPane;
    }

    protected static JPanel createButtonPanel(final String tableStateName, final JComboBox<String> sortOptions) {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);

        final ArrayList<Entity> items = Database.getCurrentTableItems();

        final String[] itemsToEdit = new String[items.size() + 1];
        final String[] itemsToDelete = new String[itemsToEdit.length];
        itemsToEdit[0] = "Edit " + tableStateName;
        itemsToDelete[0] = "Delete " + tableStateName;

        int editAndDeleteIndex = 1;
        for (final Entity item : items) {
            itemsToEdit[editAndDeleteIndex] = "ID: " + ID_FORMATTER.format(item.getId()) + " (" + item.getName() + ")";
            itemsToDelete[editAndDeleteIndex] = itemsToEdit[editAndDeleteIndex++];
        }

        final JButton addButton = createAddButton("Add " + tableStateName);
        final JComboBox<String> editSelection = createEditBox(itemsToEdit);
        final JComboBox<String> deleteSelection = createDeleteBox(itemsToDelete);

        buttonPanel.add(addButton);
        buttonPanel.add(editSelection);
        buttonPanel.add(deleteSelection);
        buttonPanel.add(sortOptions);
        return buttonPanel;
    }
}
