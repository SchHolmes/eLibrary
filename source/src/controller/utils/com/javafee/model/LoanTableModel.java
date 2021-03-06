package com.javafee.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.javafee.common.Constants;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Lend;

public class LoanTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Lend> lends;
	private String[] columns;

	public LoanTableModel() {
		super();
		this.prepareHibernateDao();
		this.columns = new String[] { "Client", "Pesel no", "Document no", "Title", "ISBN no", "Inventory no",
				"Lend date", "Return date", "Penalty" };
	}

	public Lend getLend(int index) {
		return lends.get(index);
	}

	public void setLend(int index, Lend lend) {
		lends.set(index, lend);
	}

	public void add(Lend lend) {
		lends.add(lend);
		this.fireTableDataChanged();
	}

	public void delete(Lend lend) {
		lends.remove(lend);
		this.fireTableDataChanged();
	}

	@SuppressWarnings("unchecked")
	private void prepareHibernateDao() {
		this.lends = HibernateUtil.getSession().createQuery("from Lend as len join fetch len.volume").list();
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
		return lends.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Lend lend = lends.get(row);

		switch (Constants.LendTableColumn.getByNumber(col)) {
		case COL_CLIENT_BASIC_DATA:
			return lend.getClient();
		case COL_CLIENT_PESEL_NUMBER:
			return lend.getClient().getPeselNumber();
		case COL_CLIENT_DOCUMENT_NUMBER:
			return lend.getClient().getDocumentNumber();
		case COL_VOLUME_BOOK_TITLE:
			return lend.getVolume().getBook();
		case COL_VOLUME_BOOK_ISBN_NUMBER:
			return lend.getVolume().getBook().getIsbnNumber();
		case COL_VOLUME_INVENTORY_NUMBER:
			return lend.getVolume().getInventoryNumber();
		case COL_LEND_DATE:
			return Constants.APPLICATION_DATE_FORMAT.format(lend.getLendDate());
		case COL_RETURNED_DATE:
			return Constants.APPLICATION_DATE_FORMAT.format(lend.getReturnedDate());
		case COL_PENALTY:
			return calculatePenalty(lend.getReturnedDate());
		default:
			return null;
		}
	}

	private double calculatePenalty(Date returnDate) {
		int diffMonth = 0;

		if (new Date().after(returnDate)) {
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(returnDate);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(new Date());

			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		}

		return diffMonth * Constants.PENALTY_VALUE;
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
