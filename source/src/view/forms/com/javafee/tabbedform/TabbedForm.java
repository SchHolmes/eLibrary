package com.javafee.tabbedform;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.javafee.common.Constants;
import com.javafee.common.SystemProperties;
import com.javafee.startform.LogInForm;
import com.javafee.tabbedform.clients.ClientTablePanel;

public class TabbedForm {

	private JFrame frmElibrary;
	
	private JTabbedPane tabbedPane;
	private ClientTablePanel panelClient;
	
	private JLabel lblLogInInformation;
	private JLabel lblSystemInformation;
	
	private JButton btnInformation;
	private JButton btnLogOut;

	public TabbedForm() {
		try {
			initialize();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frmElibrary = new JFrame();
		frmElibrary.setTitle(Constants.APPLICATION_NAME);
		frmElibrary.setIconImage(Toolkit.getDefaultToolkit().getImage(LogInForm.class.getResource("/images/splashScreen.jpg")));
		frmElibrary.setBounds(100, 100, 534, 362);
		frmElibrary.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{67, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmElibrary.getContentPane().setLayout(gridBagLayout);
		
		lblLogInInformation = new JLabel("Logged as:");
		GridBagConstraints gbc_lblLogInInformation = new GridBagConstraints();
		gbc_lblLogInInformation.anchor = GridBagConstraints.WEST;
		gbc_lblLogInInformation.insets = new Insets(5, 5, 5, 5);
		gbc_lblLogInInformation.gridx = 0;
		gbc_lblLogInInformation.gridy = 0;
		frmElibrary.getContentPane().add(lblLogInInformation, gbc_lblLogInInformation);
		
		lblSystemInformation = new JLabel("e-library 2017 \\u00A9");
		lblSystemInformation.setForeground(SystemColor.textHighlight);
		GridBagConstraints gbc_lblSystemInformation = new GridBagConstraints();
		gbc_lblSystemInformation.gridwidth = 13;
		gbc_lblSystemInformation.gridx = 0;
		gbc_lblSystemInformation.gridy = 2;
		frmElibrary.getContentPane().add(lblSystemInformation, gbc_lblSystemInformation);
		
		btnInformation = new JButton("About");
		btnInformation.setIcon(new ImageIcon(new ImageIcon(LogInForm.class.getResource("/images/btnInformation-ico.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
		btnInformation.setEnabled(false);
		GridBagConstraints gbc_btnInformation = new GridBagConstraints();
		gbc_btnInformation.anchor = GridBagConstraints.EAST;
		gbc_btnInformation.insets = new Insets(5, 0, 5, 5);
		gbc_btnInformation.gridx = 11;
		gbc_btnInformation.gridy = 0;
		frmElibrary.getContentPane().add(btnInformation, gbc_btnInformation);
		
		btnLogOut = new JButton("Log out");
		btnLogOut.setIcon(new ImageIcon(new ImageIcon(LogInForm.class.getResource("/images/btnLogOut-ico.png")).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
		GridBagConstraints gbc_btnLogOut = new GridBagConstraints();
		gbc_btnLogOut.anchor = GridBagConstraints.EAST;
		gbc_btnLogOut.insets = new Insets(5, 0, 5, 5);
		gbc_btnLogOut.gridx = 12;
		gbc_btnLogOut.gridy = 0;
		frmElibrary.getContentPane().add(btnLogOut, gbc_btnLogOut);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridwidth = 13;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		frmElibrary.getContentPane().add(tabbedPane, gbc_tabbedPane);
		
		panelClient = new ClientTablePanel();
		tabbedPane.addTab("Clients", null, panelClient, null);
		
		frmElibrary.pack();
	}

	public JFrame getFrame() {
		return frmElibrary;
	}

	public void setFrame(JFrame frame) {
		this.frmElibrary = frame;
	}

	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setTabbedPane(JTabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public JLabel getLblLogInInformation() {
		return lblLogInInformation;
	}

	public void setLblLogInInformation(JLabel lblLogInInformation) {
		this.lblLogInInformation = lblLogInInformation;
	}

	public JButton getBtnInformation() {
		return btnInformation;
	}

	public void setBtnInformation(JButton btnInformation) {
		this.btnInformation = btnInformation;
	}

	public JButton getBtnLogOut() {
		return btnLogOut;
	}

	public void setBtnLogOut(JButton btnLogOut) {
		this.btnLogOut = btnLogOut;
	}

	public ClientTablePanel getPanelClient() {
		return panelClient;
	}
}
