//
//  Copyright (C) 2007-2008 David Czechowski  All Rights Reserved.
//
//  This is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This software is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this software; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
//  USA.
//

/*
 * Enhanced VNC Thumbnail Viewer 1.001
 *      - Added KeyListener
 * 
 * Enhanced VNC Thumbnail Viewer 1.0
 *      - Added computer name field
 *      - Changed UI from awt to swing
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//
// The Dialog is used to add another host to EnhancedVncThumbnailViewer
//
class AddHostDialog extends JDialog implements ActionListener, ItemListener, KeyListener {

    static String readEncPassword(String encPass) {
        if (encPass.length() != 16) {
            // FIX-ME: change this, to something that's easier to detect
            //throw new Exception("VNC Enc. Passwords must be 16 chars");
            System.out.println("VNC Enc. Passwords must be 16 chars");
            return encPass;
        }

        byte[] pw = {0, 0, 0, 0, 0, 0, 0, 0};
        int len = encPass.length() / 2;
        if (len > 8) {
            len = 8;
        }
        for (int i = 0; i < len; i++) {
            String hex = encPass.substring(i * 2, i * 2 + 2);
            Integer x = new Integer(Integer.parseInt(hex, 16));
            pw[i] = x.byteValue();
        }
        byte[] key = {23, 82, 107, 6, 35, 78, 88, 7};
        DesCipher des = new DesCipher(key);
        des.decrypt(pw, 0, pw, 0);
        return new String(pw);
    }
    EnhancedVncThumbnailViewer tnviewer;
    JTextField hostField;
    JTextField portField;
    JTextField usernameField;
    JPasswordField passwordField;
    Choice authChoice;
    JButton connectButton;
    JButton cancelButton;
    // Added on Enhanced VNC Thumbnail Viewer 1.0 ***
    JTextField compnameField;

    //
    // Constructor.
    //
    public AddHostDialog(EnhancedVncThumbnailViewer tnviewer) {
        super(tnviewer, true);
        this.tnviewer = tnviewer;

        // GUI Stuff:
        setResizable(false);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);

        setFont(new Font("Helvetica", Font.PLAIN, 14));

        hostField = new JTextField("", 10);
        portField = new JTextField("5900", 10);
        usernameField = new JTextField("", 10);
        usernameField.enable(false); // not needed by default
        passwordField = new JPasswordField("", 10);
        passwordField.enable(false); // not needed by default

        compnameField = new JTextField("", 10); // Added on evnctv 1.0 ***

        authChoice = new Choice();
        authChoice.addItemListener(this);
        authChoice.add("(none)");
        authChoice.add("Password");
        authChoice.add("VNC Enc. Password");
        authChoice.add("MS-Logon");

        connectButton = new JButton("Connect...");
        cancelButton = new JButton("Cancel");
        
        connectButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        // Added on evnctv 1.001
        hostField.addKeyListener(this);
        portField.addKeyListener(this);
        usernameField.addKeyListener(this);
        passwordField.addKeyListener(this);
        compnameField.addKeyListener(this);
        authChoice.addKeyListener(this);

        // End of Row Components:
        c.gridwidth = GridBagConstraints.REMAINDER; //end row
        gridbag.setConstraints(authChoice, c);
        gridbag.setConstraints(hostField, c);
        gridbag.setConstraints(portField, c);
        gridbag.setConstraints(compnameField, c); // Added on evnctv 1.0 ***
        gridbag.setConstraints(usernameField, c);
        gridbag.setConstraints(passwordField, c);
        gridbag.setConstraints(connectButton, c);

        add(new JLabel("Host", JLabel.RIGHT));
        add(hostField);
        add(new JLabel("Port", JLabel.RIGHT));
        add(portField);
        add(new JLabel("Computer Name", JLabel.RIGHT));
        add(compnameField);
        add(new JLabel("Authentication:", JLabel.RIGHT));
        add(authChoice);
        add(new JLabel("Username", JLabel.RIGHT));
        add(usernameField);
        add(new JLabel("Password", JLabel.RIGHT));
        add(passwordField);
        add(cancelButton);
        add(connectButton);

        setTitle("Add new host"); // Added on version 2.0 ***

        Point loc = tnviewer.getLocation();
        Dimension dim = tnviewer.getSize();
        loc.x += (dim.width / 2) - 50;
        loc.y += (dim.height / 2) - 50;
        setLocation(loc);
        pack();
        validate();
        setVisible(true);
    }

    void callAddHost() {
        String host = hostField.getText();
        int port = Integer.parseInt(portField.getText());

        // Password:
        String pass = passwordField.getText();

        // MS-Logon:
        String user = usernameField.getText();

        // Encrypted Password:
        if (authChoice.getSelectedItem() == "VNC Enc. Password") {
            pass = readEncPassword(pass);
        }

        // Computer name:
        String compname = compnameField.getText();

        tnviewer.launchViewer(host, port, pass, user, compname);
    }

    // Action Listener Event:
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == connectButton) {
            callAddHost();
        }

        this.dispose();
    }

    public void itemStateChanged(ItemEvent e) {
        if (authChoice.getSelectedItem() == "(none)") {
            passwordField.enable(false);
        } else {
            passwordField.enable(true);
        }

        if (authChoice.getSelectedItem() == "MS-Logon") {
            usernameField.enable(true);
        } else {
            usernameField.enable(false);
        }
    }

    /*
     * Added on evnctv 1.001
     */
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_ENTER == e.getKeyCode()) {
            callAddHost();
            this.dispose();
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
