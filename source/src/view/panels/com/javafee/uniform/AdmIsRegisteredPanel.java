package com.javafee.uniform;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.javafee.common.SystemProperties;

public class AdmIsRegisteredPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JCheckBox chckbxIsRegistered;
	private DecisionPanel decisionPanel;
	
	public AdmIsRegisteredPanel() {
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Adm registration agreement form", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 161, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		chckbxIsRegistered = new JCheckBox("Registered");
		GridBagConstraints gbc_chckbxIsRegistered = new GridBagConstraints();
		gbc_chckbxIsRegistered.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxIsRegistered.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxIsRegistered.gridx = 0;
		gbc_chckbxIsRegistered.gridy = 0;
		add(chckbxIsRegistered, gbc_chckbxIsRegistered);
		
		decisionPanel = new DecisionPanel();
		GridBagConstraints gbc_decisionPanel = new GridBagConstraints();
		gbc_decisionPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_decisionPanel.gridx = 1;
		gbc_decisionPanel.gridy = 0;
		add(decisionPanel, gbc_decisionPanel);
		
		
	}
	
	public JCheckBox getChckbxIsRegistered() {
		return chckbxIsRegistered;
	}
	
	public DecisionPanel getDecisionPanel() {
		return decisionPanel;
	}
}
