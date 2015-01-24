package dit.groupproject.rocketretail.entityhelpers;

import static dit.groupproject.rocketretail.gui.GuiCreator.DEFAULT_LABEL_FONT;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import dit.groupproject.rocketretail.gui.GuiCreator;

public class AbstractEntityHelper {

    protected final static Font BOLD_LABEL_FONT = new Font(DEFAULT_LABEL_FONT.getFontName(), Font.BOLD, DEFAULT_LABEL_FONT.getSize());

    protected final static ActionListener cancelListener = new ActionListener() {
        public void actionPerformed(final ActionEvent e) {
            removeLeftPanel();
        }
    };

    protected static void removeLeftPanel() {
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.frame.validate();
    }

    protected static void resetLeftPanel() {
        GuiCreator.frame.remove(GuiCreator.leftPanel);
        GuiCreator.frame.repaint();
        GuiCreator.leftPanel = new JPanel();
    }

    protected static void updateLeftPanel(final JPanel innerPanel) {
        GuiCreator.leftPanel.add(innerPanel);
        GuiCreator.setFrame(true, false, false);
    }

    protected static JPanel addLabelsToPanel(final String[] labels) {
        final JPanel innerPanel = new JPanel(new GridBagLayout());
        innerPanel.setBackground(GuiCreator.BACKGROUND_COLOUR);
        int labelIndex = 0;

        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(1, 10, 0, 5);
        gridBagConstraints.gridx = 0;

        for (final String label : labels) {
            gridBagConstraints.gridy = labelIndex++;
            innerPanel.add(new JLabel(label + ":"), gridBagConstraints);
        }
        return innerPanel;
    }
}