package com.javafee.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.javafee.common.Constants;
import com.javafee.common.SystemProperties;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Volume;

public class VolumeTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	protected List<Volume> volumes;
	private String[] columns;

	public VolumeTableModel() {
		super();
		this.prepareHibernateDao();
		this.columns = new String[] { "Title", "ISBN no", "Inventory no", "No of page", "No of tome",
				"Is reading room" };
	}

	public Volume getVolume(int index) {
		return volumes.get(index);
	}

	public void setVolume(int index, Volume volume) {
		volumes.set(index, volume);
	}

	public void add(Volume volume) {
		volumes.add(volume);
		this.fireTableDataChanged();
	}

	public void remove(Volume volume) {
		volumes.remove(volume);
		this.fireTableDataChanged();
	}

	@SuppressWarnings("unchecked")
	protected void prepareHibernateDao() {
		this.volumes = HibernateUtil.getSession().createQuery("from Volume as vol join fetch vol.book").list();
		this.volumes = volumes.stream().filter(vol -> !vol.getIsLended()).collect(Collectors.toList());
	}

	@SuppressWarnings("unused")
	private void executeUpdate(String entityName, Object object) {
		HibernateUtil.beginTransaction();
		HibernateUtil.getSession().update(entityName, object);
		HibernateUtil.commitTransaction();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public int getRowCount() {
		return volumes.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Volume volume = volumes.get(row);
		switch (Constants.VolumeTableColumn.getByNumber(col)) {
		case COL_BOOK_TITLE:
			return volume.getBook().getTitle();
		case COL_BOOK_ISBN_NUMBER:
			return volume.getBook().getIsbnNumber();
		case COL_INVENTORY_NUMBER:
			return volume.getInventoryNumber();
		case COL_BOOK_NUMBER_OF_PAGE:
			return volume.getBook().getNumberOfPage();
		case COL_BOOK_NUMBER_OF_TOME:
			return volume.getBook().getNumberOfTomes();
		case COL_IS_READING_ROOM:
			return volume.getIsReadingRoom()
					? "Yes"
					: "No";
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public String getColumnName(int col) {
		return columns[col];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void fireTableChanged(TableModelEvent e) {
		super.fireTableChanged(e);
	}
}
