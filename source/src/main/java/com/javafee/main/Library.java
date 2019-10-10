package com.javafee.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.javafee.loginform.Actions;

public class Library {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				Actions actions = new Actions();
				actions.control();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
