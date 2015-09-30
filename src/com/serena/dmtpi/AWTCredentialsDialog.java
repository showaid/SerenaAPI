/**
 * Used to get a password using an AWT Dialog.
 */
package com.serena.dmtpi;

import java.util.Arrays;

final class AWTCredentialsDialog implements java.awt.event.ActionListener {
	private final java.awt.Frame parentFrame;
	private final boolean disposeFrame;
	private final int x;
	private final int y;
	private final boolean askUserID;
	private String userID;
	private char[] password;
	
	AWTCredentialsDialog(final java.awt.Frame parentFrame,
	        final int x, final int y, final boolean askUserID) {
		if (parentFrame == null) {
			this.parentFrame = new java.awt.Frame();
			this.disposeFrame = true;
		} else {
			this.parentFrame = parentFrame;
			this.disposeFrame = false;
		}
		this.x = x;
		this.y = y;
		this.askUserID = askUserID;
	}
	
	String getUserID() {
		return userID;
	}
	
    void setUserID(String userID) {
        this.userID = userID;
    }

	char[] getPassword() {
		return (char[]) password.clone();
	}
	
	void setPassword(char[] password) {
        if (this.password != null) {
            // wipe out the old password
            Arrays.fill(this.password, ' ');
        }
        this.password = (char[]) password.clone();
	}

	private java.awt.Dialog dlg;
	private java.awt.TextField txtu;
	private java.awt.TextField txtp;
	
	void showModal() {
		dlg = new java.awt.Dialog(parentFrame, "Credentials", true);
		dlg.setLocation(x, y);
		dlg.setLayout(new java.awt.GridLayout(0, 1));
		java.awt.Label lbl = new java.awt.Label("User ID:");
		dlg.add(lbl);
		txtu = new java.awt.TextField(25);
		if (userID != null) {
		    txtu.setText(userID);
		}
        txtu.setEnabled(askUserID);
		dlg.add(txtu);
		lbl = new java.awt.Label("Password:");
		dlg.add(lbl);
		txtp = new java.awt.TextField(25);
		txtp.setEchoChar('*');
		if (password != null) {
		    txtp.setText(new String(password));
		}
		txtp.setEnabled(true);
		dlg.add(txtp);
		java.awt.Button btn = new java.awt.Button("OK");
		dlg.add(btn);
		btn.addActionListener(this);
		txtu.addActionListener(this);
		txtp.addActionListener(this);
		dlg.pack();
        if (!askUserID) {
            txtp.requestFocus();
        }
		dlg.setVisible(true);
	}

    public void actionPerformed(java.awt.event.ActionEvent e) {
    	if (txtu.equals(e.getSource())) {
    		// pressed Enter key while in user ID field.
    		txtp.requestFocus();
    	} else {
    		// pressed Enter key while in password field
    		// or pressed the OK button.
            userID = txtu.getText();
            password = txtp.getText().toCharArray();
    		dlg.dispose();
            dlg = null;
    		txtu = null;
    		txtp = null;
    		if (disposeFrame) {
    			parentFrame.dispose();
    		}
    	}
    }
}