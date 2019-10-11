package com.javafee.uniform;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import lombok.Getter;

@Getter
public class AdmIsAccountantPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JCheckBox chckbxIsAccountant;
	private DecisionPanel decisionPanel;

	public AdmIsAccountantPanel() {
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Adm is accountant",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 161, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		chckbxIsAccountant = new JCheckBox("Is accountant:");
		GridBagConstraints gbc_chckbxIsAccountant = new GridBagConstraints();
		gbc_chckbxIsAccountant.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxIsAccountant.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxIsAccountant.gridx = 0;
		gbc_chckbxIsAccountant.gridy = 0;
		add(chckbxIsAccountant, gbc_chckbxIsAccountant);

		decisionPanel = new DecisionPanel();
		GridBagConstraints gbc_decisionPanel = new GridBagConstraints();
		gbc_decisionPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_decisionPanel.gridx = 1;
		gbc_decisionPanel.gridy = 0;
		add(decisionPanel, gbc_decisionPanel);

	}
}
