package com.javafee.uniform;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.javafee.startform.LogInForm;

public class CockpitConfirmationPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton btnAccept;

	public CockpitConfirmationPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		btnAccept = new JButton(
				"Accept");
		btnAccept.setIcon(new ImageIcon(new ImageIcon(LogInForm.class.getResource("/images/btnAccept-ico.png"))
				.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
		GridBagConstraints gbc_btnAccept = new GridBagConstraints();
		gbc_btnAccept.anchor = GridBagConstraints.NORTH;
		gbc_btnAccept.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAccept.gridx = 0;
		gbc_btnAccept.gridy = 0;
		add(btnAccept, gbc_btnAccept);
	}

	public JButton getBtnAccept() {
		return btnAccept;
	}

	public void setBtnAccept(JButton btnAccept) {
		this.btnAccept = btnAccept;
	}
}
