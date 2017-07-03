/* *
 * Enhanced VNC Thumbnail Viewer 1.4.0
 *  This dialog uses for theme settings
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ThemeSettingDialog extends JDialog implements ActionListener, KeyListener {

    private static final int PADDING = 15;
    // private static final int SPACING = 9;
    
    private EnhancedVncThumbnailViewer evnctv;
    private JButton okButton, cancelButton;
    private JComboBox<String> selector;
    
    public ThemeSettingDialog(EnhancedVncThumbnailViewer tnviewer) {
        super(tnviewer, true);
        evnctv = tnviewer;
        
        // Initial components
        setFont(new Font("Helvetica", Font.PLAIN, 14));
        
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JLabel themeLabel = new JLabel("Theme:");
        selector = new JComboBox<String>(ThemeSetting.LIST);
        selector.setSelectedItem(ThemeSetting.getName());

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        selector.addActionListener(this);
        
        // Panel
        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel();
        panel.add(themeLabel);
        panel.add(selector);
        panel.add(okButton);
        panel.add(cancelButton);
        
        // Layout
        panel.setLayout(layout);
        
        layout.putConstraint(SpringLayout.WEST, themeLabel, PADDING, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, themeLabel, PADDING + 5, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, selector, PADDING + 60, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, selector, PADDING + 3, SpringLayout.NORTH, panel);
        
        layout.putConstraint(SpringLayout.EAST, cancelButton, -PADDING, SpringLayout.EAST, panel);
        layout.putConstraint(SpringLayout.SOUTH, cancelButton, -PADDING - 5, SpringLayout.SOUTH, panel);
        layout.putConstraint(SpringLayout.EAST, okButton, -PADDING + 9, SpringLayout.WEST, cancelButton);
        layout.putConstraint(SpringLayout.SOUTH, okButton, -PADDING - 5, SpringLayout.SOUTH, panel);
        
        add(panel);

        // Dialog
        Point loc = tnviewer.getLocation();
        Dimension dim = tnviewer.getSize();
        loc.x += (dim.width / 2) - 50;
        loc.y += (dim.height / 2) - 50;
        setLocation(loc);

        setTitle("Theme Settings");
        setSize(430, 180);
        validate();
        setResizable(false);
        setVisible(true);
    }

    private void saveSetting() {
        try {
            String theme = (String) selector.getSelectedItem();
            if (theme.equals("")) {
                JOptionPane.showMessageDialog(this, "Please select a theme.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // If theme has been changed
                if (!ThemeSetting.getName().equals(theme)) {
                    String msg = "Changed theme from " + ThemeSetting.getName() + " to " + theme;
                    RecentSettingsList.addRecent(new RecentSetting(msg, "Theme"));
                    System.out.println(msg);
                }

                ThemeSetting.use(theme);
                evnctv.setGuiTheme();
                
                JOptionPane.showMessageDialog(this, "Maybe you must reconnect each viewers for apply theme if those exists", "Infomation", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot use this theme.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            saveSetting();
        } else if (e.getSource() == cancelButton) {
            this.dispose();
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            saveSetting();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}