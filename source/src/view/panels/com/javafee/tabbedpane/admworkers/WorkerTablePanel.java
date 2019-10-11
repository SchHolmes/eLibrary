package com.javafee.tabbedpane.admworkers;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.javafee.model.WorkerTableModel;
import com.javafee.uniform.AdmIsAccountantPanel;
import com.javafee.uniform.AdmIsRegisteredPanel;
import com.javafee.uniform.CockpitEditionPanel;

import lombok.Getter;
import lombok.Setter;
import net.coderazzi.filters.gui.TableFilterHeader;

@Getter
@Setter
public class WorkerTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable workerTable;

	private WorkerDataPanel workerDataPanel;
	private CockpitEditionPanel cockpitEditionPanel;
	private AdmIsRegisteredPanel admIsRegisteredPanel;
	private AdmIsAccountantPanel admIsAccountantPanel;

	public WorkerTablePanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 843, 538, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 275, 0, 44, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 6;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		add(scrollPane, gbc_scrollPane);

		workerTable = new JTable();
		@SuppressWarnings("unused")
		TableFilterHeader tableFilterHeader = new TableFilterHeader(workerTable);
		workerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		workerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		workerTable.setModel(new WorkerTableModel());
		workerTable.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(workerTable);

		workerDataPanel = new WorkerDataPanel();
		workerDataPanel.setBorder(new TitledBorder(null, "Worker data", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		GridBagConstraints gbc_workerDataPanel = new GridBagConstraints();
		gbc_workerDataPanel.gridheight = 2;
		gbc_workerDataPanel.insets = new Insets(0, 0, 5, 0);
		gbc_workerDataPanel.fill = GridBagConstraints.BOTH;
		gbc_workerDataPanel.gridx = 1;
		gbc_workerDataPanel.gridy = 0;
		workerDataPanel.getBtnRegisterNow().setVisible(false);
		add(workerDataPanel, gbc_workerDataPanel);
		
		admIsAccountantPanel = new AdmIsAccountantPanel();
		GridBagConstraints gbc_admIsAccountantPanel = new GridBagConstraints();
		gbc_admIsAccountantPanel.insets = new Insets(0, 0, 5, 0);
		gbc_admIsAccountantPanel.fill = GridBagConstraints.BOTH;
		gbc_admIsAccountantPanel.gridx = 1;
		gbc_admIsAccountantPanel.gridy = 2;
		add(admIsAccountantPanel, gbc_admIsAccountantPanel);

		admIsRegisteredPanel = new AdmIsRegisteredPanel();
		GridBagConstraints gbc_admIsRegisteredPanel = new GridBagConstraints();
		gbc_admIsRegisteredPanel.gridheight = 2;
		gbc_admIsRegisteredPanel.insets = new Insets(0, 0, 5, 0);
		gbc_admIsRegisteredPanel.fill = GridBagConstraints.BOTH;
		gbc_admIsRegisteredPanel.gridx = 1;
		gbc_admIsRegisteredPanel.gridy = 3;
		add(admIsRegisteredPanel, gbc_admIsRegisteredPanel);

		cockpitEditionPanel = new CockpitEditionPanel();
		GridBagConstraints gbc_cockpitEditionPanel = new GridBagConstraints();
		gbc_cockpitEditionPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_cockpitEditionPanel.anchor = GridBagConstraints.NORTH;
		gbc_cockpitEditionPanel.gridx = 1;
		gbc_cockpitEditionPanel.gridy = 5;
		add(cockpitEditionPanel, gbc_cockpitEditionPanel);
	}
}
