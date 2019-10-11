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
import com.javafee.loginform.LogInEvent;
import com.javafee.startform.LogInForm;
import com.javafee.tabbedform.clients.ClientTablePanel;
import com.javafee.tabbedpane.admdictionaries.AdmDictionaryPanel;
import com.javafee.tabbedpane.books.BookTablePanel;
import com.javafee.tabbedpane.library.LibraryTablePanel;
import com.javafee.tabbedpane.loanservice.LoanServicePanel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TabbedForm {

	private JFrame frame;

	private JTabbedPane tabbedPane;
	private ClientTablePanel panelClient;
	private LibraryTablePanel panelLibrary;
	private BookTablePanel panelBook;
	private AdmDictionaryPanel panelAdmDictionary;
	private LoanServicePanel panelLoanService;

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

	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame = new JFrame();
		frame.setTitle(Constants.APPLICATION_NAME);
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(LogInForm.class.getResource("/images/splashScreen.jpg")));
		frame.setBounds(100, 100, 534, 362);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 67, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frame.getContentPane().setLayout(gridBagLayout);

		lblLogInInformation = new JLabel("Logged as:");
		GridBagConstraints gbc_lblLogInInformation = new GridBagConstraints();
		gbc_lblLogInInformation.anchor = GridBagConstraints.WEST;
		gbc_lblLogInInformation.insets = new Insets(5, 5, 5, 5);
		gbc_lblLogInInformation.gridx = 0;
		gbc_lblLogInInformation.gridy = 0;
		frame.getContentPane().add(lblLogInInformation, gbc_lblLogInInformation);

		lblSystemInformation = new JLabel("e-library 2019 \\u00A9");
		lblSystemInformation.setForeground(SystemColor.textHighlight);
		GridBagConstraints gbc_lblSystemInformation = new GridBagConstraints();
		gbc_lblSystemInformation.gridwidth = 13;
		gbc_lblSystemInformation.gridx = 0;
		gbc_lblSystemInformation.gridy = 2;
		frame.getContentPane().add(lblSystemInformation, gbc_lblSystemInformation);

		btnInformation = new JButton("About");
		btnInformation
				.setIcon(new ImageIcon(new ImageIcon(LogInForm.class.getResource("/images/btnInformation-ico.png"))
						.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
		btnInformation.setEnabled(false);
		GridBagConstraints gbc_btnInformation = new GridBagConstraints();
		gbc_btnInformation.anchor = GridBagConstraints.EAST;
		gbc_btnInformation.insets = new Insets(5, 0, 5, 5);
		gbc_btnInformation.gridx = 11;
		gbc_btnInformation.gridy = 0;
		frame.getContentPane().add(btnInformation, gbc_btnInformation);

		btnLogOut = new JButton("Log out");
		btnLogOut.setIcon(new ImageIcon(new ImageIcon(LogInForm.class.getResource("/images/btnLogOut-ico.png"))
				.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
		GridBagConstraints gbc_btnLogOut = new GridBagConstraints();
		gbc_btnLogOut.anchor = GridBagConstraints.EAST;
		gbc_btnLogOut.insets = new Insets(5, 0, 5, 5);
		gbc_btnLogOut.gridx = 12;
		gbc_btnLogOut.gridy = 0;
		frame.getContentPane().add(btnLogOut, gbc_btnLogOut);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.gridwidth = 13;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 1;
		frame.getContentPane().add(tabbedPane, gbc_tabbedPane);

		panelClient = new ClientTablePanel();
		if (LogInEvent.getRole() != Constants.Role.CLIENT)
		tabbedPane.addTab("Clients", null, panelClient, null);

		panelLibrary = new LibraryTablePanel();
		tabbedPane.addTab("Library", null, panelLibrary, null);
		
		panelBook = new BookTablePanel();
		if (LogInEvent.getRole() != Constants.Role.CLIENT)
		tabbedPane.addTab("Books", null, panelBook, null);
		
		panelAdmDictionary = new AdmDictionaryPanel();
		if (LogInEvent.getRole() != Constants.Role.CLIENT)
		tabbedPane.addTab("Dictionaries", null, panelAdmDictionary, null);
		
		panelLoanService = new LoanServicePanel();
		if (LogInEvent.getRole() != Constants.Role.CLIENT)
		tabbedPane.addTab("Loans", null, panelLoanService, null);
		
		frame.pack();
	}
}
