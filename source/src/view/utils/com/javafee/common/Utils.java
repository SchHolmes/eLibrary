package com.javafee.common;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Utils {
	public static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public static void displayOptionPane(String message, String title, int messageType) {
		JOptionPane optionPane = new JOptionPane();
		optionPane.setMessage("<html>" + message + "</html>");
		optionPane.setMessageType(messageType);
		JDialog dialog = optionPane.createDialog(null, title);
		dialog.setVisible(true);
	}

	public static int displayConfirmDialog(String message, String title) {
		Object[] options = { "Yes", "No" };
		return JOptionPane.showOptionDialog(null, message, title,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, 
				options, 
				options[0]);
	}
}
