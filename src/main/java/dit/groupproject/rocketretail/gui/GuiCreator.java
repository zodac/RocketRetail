package dit.groupproject.rocketretail.gui;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dit.groupproject.rocketretail.menus.MenuGui;

public class GuiCreator {

    public static final Color BACKGROUND_COLOUR = new Color(170, 209, 243); // Blue
    // public static final Color BACKGROUND_COLOUR = new Color(224, 217, 233);
    // //Pink
    // public static final Color BACKGROUND_COLOUR = new Color(238, 238, 238);
    // //Default
    public static final String BOLD_HTML_FORMATTING_TAGS = "<html><b>%s</b></html>";
    public static final String BOLD_FONT_HTML_FORMATTING_TAGS_WITH_COLOUR = String.format(BOLD_HTML_FORMATTING_TAGS, "<font color=\"%s\">%s</font>");

    public static final Font DEFAULT_LABEL_FONT = new JLabel().getFont();
    public static final Font BOLD_LABEL_FONT = new Font(DEFAULT_LABEL_FONT.getFontName(), Font.BOLD, DEFAULT_LABEL_FONT.getSize());

    public static JFrame frame = new JFrame();
    public static JPanel mainPanel;
    public static JPanel leftPanel;
    public static JPanel rightPanel;

    static JPanel headerPanel;
    static JPanel bottomPanel;
    static JLabel confirmationLabel;

    /**
     * Creates and defines the GUI:
     * <ul>
     * <li>Sets the "look and feel" to Windows default</li>
     * <li>Sets the {@link #frame} size and title</li>
     * <li>Initialises the JPanels</li>
     * <li>Places the panels in {@link #frame}</li>
     * <li>Sets the background colour of the panels and {@link #frame}</li>
     * <li>Centers the GUI in the middle of the screen</li>
     * </ul>
     */
    public static void createGui() {
        setThemeToWindows();

        frame.setSize(1150, 700);
        frame.setTitle("Rocket Retail Inc");

        createHeaderPanel();
        createConfirmationPanel();

        mainPanel = new JPanel(new BorderLayout(0, 2));
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        frame.setBackground(BACKGROUND_COLOUR);
        headerPanel.setBackground(BACKGROUND_COLOUR);

        centreGuiOnScreen();
    }

    private static void setThemeToWindows() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println("Error loading Windows theme!");
        }
    }

    private static void createConfirmationPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(750, 30));
        bottomPanel.setBackground(BACKGROUND_COLOUR);
        confirmationLabel = new JLabel();
        confirmationLabel.setFont(new Font(confirmationLabel.getFont().getFontName(), Font.BOLD, confirmationLabel.getFont().getSize()));
        bottomPanel.add(confirmationLabel);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private static void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(750, 60));
        try {
            final JLabel headerLabel = new JLabel(new ImageIcon(ImageIO.read(ClassLoader.getSystemResource("images/rocketRetail.png"))));
            headerPanel.add(headerLabel, BorderLayout.CENTER);
        } catch (IOException e) {
            System.out.println("Error loading heading banner!");
        }
        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private static void centreGuiOnScreen() {
        final GraphicsConfiguration localGraphics = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getDefaultConfiguration();
        final Insets i = frame.getToolkit().getScreenInsets(localGraphics);
        int maxWidth = frame.getToolkit().getScreenSize().width - i.left - i.right;
        int maxHeight = frame.getToolkit().getScreenSize().height - i.top - i.bottom;

        frame.setLocation(((maxWidth - frame.getWidth()) / 2), ((maxHeight - frame.getHeight()) / 2));
    }

    public static void showGui(JMenuBar menuBar) {
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Displays a message at the bottom of the GUI.
     * <p>
     * Message remains on screen for 4 seconds.
     * 
     * @param title
     *            the message to display on GUI
     */
    public static void setConfirmationMessage(final String title) {
        confirmationLabel.setText(title);
        // TODO Set BG too - perhaps as param?
        final Timer confirmationTimer = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmationLabel.setText("");
            }
        });
        confirmationTimer.setRepeats(false);
        confirmationTimer.start();
    }

    /**
     * If relevant boolean is set to true, resets a panel ({@link #mainPanel},
     * {@link #leftPanel} or {@link #rightPanel}), then updates the frame.
     * 
     * @param left
     *            a boolean used to reset {@link #leftPanel}
     * @param right
     *            a boolean used to reset {@link #rightPanel}
     * @param main
     *            a boolean used to reset {@link #mainPanel}
     */
    public static void setFrame(boolean left, boolean right, boolean main) {

        if (main) {
            frame.add(mainPanel, BorderLayout.CENTER);
            mainPanel.setBackground(BACKGROUND_COLOUR);
        }
        if (left) {
            frame.add(leftPanel, BorderLayout.WEST);
            leftPanel.setBackground(BACKGROUND_COLOUR);
        }
        if (right) {
            frame.add(rightPanel, BorderLayout.EAST);
            rightPanel.setBackground(BACKGROUND_COLOUR);
        }

        frame.validate();
    }

    public static void relaunchGui() {
        frame.dispose();
        leftPanel.removeAll();
        rightPanel.removeAll();
        launchGui();
    }

    public static void launchGui() {
        // final boolean isManager = LoginHandler.loginAsManager();
        final boolean isManager = true;

        final JMenuBar menuBar = new JMenuBar();
        final MenuGui menuGui = new MenuGui();
        menuGui.createMenuBar(menuBar, isManager);
        HomeScreen.setHomeScreen();
        GuiCreator.showGui(menuBar);
    }

    public static boolean getConfirmationResponse(final String confirmationMessage) {
        final JPanel myPanel = new JPanel();
        myPanel.add(new JLabel(confirmationMessage));
        return showConfirmDialog(null, myPanel, "Please confirm", OK_CANCEL_OPTION, PLAIN_MESSAGE, null) == OK_OPTION;
    }

    public static void showMandatoryDialog(final String title, final JPanel myPanel) {
        if (JOptionPane.showConfirmDialog(null, myPanel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null) != JOptionPane.OK_OPTION) {
            System.exit(0);
        }

    }

    public static void showFailureMessage(final String title, final String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
    }
}
