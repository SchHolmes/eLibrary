package com.javafee.model;

import java.util.Date;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import com.javafee.common.Constants;
import com.javafee.common.Validator;
import com.javafee.exception.LogGuiException;
import com.javafee.hibernate.dao.HibernateDao;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.association.City;
import com.javafee.hibernate.dto.library.Client;

public class ClientTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Client> clients;
	private String[] columns;

	private HibernateDao<Client, Integer> clientDao;

	public ClientTableModel() {
		super();
		this.prepareHibernateDao();
		this.columns = new String[] { "Pesel no",
				"Document no",
				"Login", "Name",
				"Surname",
				"Address", "City",
				"Gender", "Birth date",
				"Registered" };
	}
	
	public Client getClient(int index) {
		return clients.get(index);
	}

	public void setClient(int index, Client client) {
		clients.set(index, client);
	}

	public void add(Client client) {
		clients.add(client);
		this.fireTableDataChanged();
	}
	
	public void remove(Client client) {
		clients.remove(client);
		this.fireTableDataChanged();
	}

	private void prepareHibernateDao() {
		this.clientDao = new HibernateDao<Client, Integer>(Client.class);
		this.clients = clientDao.findAll();
	}

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
		return clients.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Client client = clients.get(row);
		switch (com.javafee.common.Constants.ClientTableColumn.getByNumber(col)) {
		case COL_PESEL_NUMBER:
			return client.getPeselNumber();
		case COL_DOCUMENT_NUMBER:
			return client.getDocumentNumber();
		case COL_LOGIN:
			return client.getLogin();
		case COL_NAME:
			return client.getName();
		case COL_SURNAME:
			return client.getSurname();
		case COL_ADDRESS:
			return client.getAddress();
		case COL_CITY:
			return client.getCity() != null ? client.getCity().getName() : null;
		case COL_SEX:
			if (client.getSex() != null) {
				if (Constants.DATA_BASE_MALE_SIGN.toString().equals(client.getSex().toString()))
					return "Male";
				else if (Constants.DATA_BASE_FEMALE_SIGN.toString().equals(client.getSex().toString()))
					return "Female";
			} else
				return null;
		case COL_BIRTH_DATE:
			return client.getBirthDate() != null ? Constants.APPLICATION_DATE_FORMAT.format(client.getBirthDate()) : null;
		case COL_REGISTERED:
			return client.getRegistered() ? "Yes"
					: "No";
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Client client = clients.get(row);
		Client clientShallowClone = (Client) client.clone();

		switch (com.javafee.common.Constants.ClientTableColumn.getByNumber(col)) {
		case COL_PESEL_NUMBER:
			clientShallowClone.setPeselNumber(value.toString());
			break;
		case COL_DOCUMENT_NUMBER:
			clientShallowClone.setDocumentNumber(value.toString());
			break;
		case COL_LOGIN:
			clientShallowClone.setLogin(value.toString());
			break;
		case COL_NAME:
			clientShallowClone.setName(value.toString());
			break;
		case COL_SURNAME:
			clientShallowClone.setSurname(value.toString());
			break;
		case COL_ADDRESS:
			clientShallowClone.setAddress(value.toString());
			break;
		case COL_CITY:
			clientShallowClone.setCity((City) value);
			break;
		case COL_SEX:
			clientShallowClone.setSex((Character) value);
			break;
		case COL_BIRTH_DATE:
			clientShallowClone.setBirthDate((Date) value);
			break;
		case COL_REGISTERED:
			clientShallowClone.setRegistered((Boolean) value);
			break;
		}

		if (Validator.validateClientUpdate(clientShallowClone)) {
			executeUpdate(Client.class.getName(), client);
			clients.set(row, clientShallowClone);
		} else {
			LogGuiException.logWarning("Login already exist",
					"Login already exist. Change login.");
		}

		this.fireTableRowsUpdated(row, row);
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
