package com.javafee.tabbedpane.loanservice;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import com.javafee.model.LoanTableModel;

import lombok.Getter;
import lombok.Setter;
import net.coderazzi.filters.gui.TableFilterHeader;

@Getter
@Setter
public class LoanTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable loanTable;

	public LoanTablePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 200, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);

		loanTable = new JTable();
		@SuppressWarnings("unused")
		TableFilterHeader tableFilterHeader = new TableFilterHeader(loanTable);
		loanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loanTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		loanTable.setModel(new LoanTableModel());
		loanTable.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(loanTable);
	}
}
