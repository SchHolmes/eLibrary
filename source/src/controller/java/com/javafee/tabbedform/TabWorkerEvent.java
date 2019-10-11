package com.javafee.tabbedform;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import com.javafee.common.Constants;
import com.javafee.common.Constants.Role;
import com.javafee.common.IActionForm;
import com.javafee.common.IRegistrationForm;
import com.javafee.common.Params;
import com.javafee.common.Utils;
import com.javafee.common.Validator;
import com.javafee.exception.LogGuiException;
import com.javafee.exception.RefusedRegistrationException;
import com.javafee.exception.RefusedWorkerEventLoadingException;
import com.javafee.hibernate.dao.HibernateDao;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.association.City;
import com.javafee.hibernate.dto.library.LibraryWorker;
import com.javafee.hibernate.dto.library.Worker;
import com.javafee.loginform.LogInEvent;
import com.javafee.loginform.RegistrationEvent;
import com.javafee.model.WorkerTableModel;

public final class TabWorkerEvent implements IActionForm, IRegistrationForm {
	private TabbedForm tabbedForm;

	private static TabWorkerEvent workerEvent = null;
	private RegistrationEvent registrationEvent;

	private TabWorkerEvent(TabbedForm tabbedForm) {
		this.control(tabbedForm);
	}

	public static TabWorkerEvent getInstance(TabbedForm tabbedForm) {
		if (workerEvent == null) {
			workerEvent = new TabWorkerEvent(tabbedForm);
		} else
			new RefusedWorkerEventLoadingException("Cannot client event loading");
		return workerEvent;
	}

	public void control(TabbedForm tabbedForm) {
		setTabbedForm(tabbedForm);
		initializeForm();

		tabbedForm.getPanelWorker().getCockpitEditionPanel().getBtnAdd().addActionListener(e -> onClickBtnAdd());
		tabbedForm.getPanelWorker().getCockpitEditionPanel().getBtnModify().addActionListener(e -> onClickBtnModify());
		tabbedForm.getPanelWorker().getCockpitEditionPanel().getBtnDelete().addActionListener(e -> onClickBtnDelete());
		tabbedForm.getPanelWorker().getAdmIsRegisteredPanel().getDecisionPanel().getBtnAccept()
				.addActionListener(e -> onClickBtnAccept());
		tabbedForm.getPanelWorker().getAdmIsAccountantPanel().getDecisionPanel().getBtnAccept()
				.addActionListener(e -> onClickBtnAcceptAccountant());
		tabbedForm.getPanelWorker().getWorkerTable().getModel().addTableModelListener(e -> reloadClientTable());
		tabbedForm.getPanelWorker().getWorkerTable().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting())
				onClientTableListSelectionChange();
		});
	}

	@Override
	public void initializeForm() {
		reloadRegistrationPanel();
		switchPerspectiveToAdm(LogInEvent.getRole() == Role.ADMIN || LogInEvent.getRole() == Role.WORKER_ACCOUNTANT);
	}

	public void setTabbedForm(TabbedForm tabbedForm) {
		this.tabbedForm = tabbedForm;
	}

	@Override
	public void reloadRegistrationPanel() {
		reloadComboBoxCity();
	}

	private void reloadComboBoxCity() {
		DefaultComboBoxModel<City> comboBoxCity = new DefaultComboBoxModel<City>();
		HibernateDao<City, Integer> city = new HibernateDao<City, Integer>(City.class);
		List<City> cityListToSort = city.findAll();
		cityListToSort.sort(Comparator.comparing(City::getName, Comparator.nullsFirst(Comparator.naturalOrder())));
		cityListToSort.forEach(c -> comboBoxCity.addElement(c));

		tabbedForm.getPanelWorker().getWorkerDataPanel().getComboBoxCity().setModel(comboBoxCity);
	}

	private void reloadChckbxIsRegistered(boolean isRegistered) {
		tabbedForm.getPanelWorker().getAdmIsRegisteredPanel().getChckbxIsRegistered().setSelected(isRegistered);
	}

	private void reloadClientTable() {
		tabbedForm.getPanelWorker().getWorkerTable().repaint();
	}

	private void onClickBtnAdd() {
		if (validateRegistration())
			onClickBtnRegisterNow();
	}

	private void onClickBtnModify() {
		int selectedRowIndex = tabbedForm.getPanelWorker().getWorkerTable()
				.convertRowIndexToModel(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow());

		if (selectedRowIndex != -1) {
			Worker selectedClient = ((WorkerTableModel) tabbedForm.getPanelWorker().getWorkerTable().getModel())
					.getWorker(selectedRowIndex);
			Worker clientShallowClone = (Worker) selectedClient.clone();

			clientShallowClone.setPeselNumber(
					tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldPeselNumber().getText());
			clientShallowClone.setDocumentNumber(
					tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldDocumentNumber().getText());
			clientShallowClone.setName(tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldName().getText());
			clientShallowClone
					.setSurname(tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldSurname().getText());
			clientShallowClone
					.setAddress(tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldAddress().getText());
			clientShallowClone.setCity(
					(City) tabbedForm.getPanelWorker().getWorkerDataPanel().getComboBoxCity().getSelectedItem());
			clientShallowClone.setSex(tabbedForm.getPanelWorker().getWorkerDataPanel().getRadioButtonMale().isSelected()
					? Constants.DATA_BASE_MALE_SIGN
					: Constants.DATA_BASE_FEMALE_SIGN);
			clientShallowClone.setBirthDate(
					tabbedForm.getPanelWorker().getWorkerDataPanel().getDateChooserBirthDate().getDate() != null
							? tabbedForm.getPanelWorker().getWorkerDataPanel().getDateChooserBirthDate().getDate()
							: null);
			clientShallowClone.setLogin(tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldLogin().getText());

			if (!validatePasswordFieldIsEmpty())
				Utils.displayOptionPane(
						"Despite password field is not empty it could not be changed. Contact with administrator to change the password.",
						"Password field not empty", JOptionPane.WARNING_MESSAGE);
			if (Validator.validateClientUpdate(clientShallowClone)) {
				HibernateUtil.beginTransaction();
				HibernateUtil.getSession().evict(selectedClient);
				HibernateUtil.getSession().update(Worker.class.getName(), clientShallowClone);
				HibernateUtil.commitTransaction();

				((WorkerTableModel) tabbedForm.getPanelWorker().getWorkerTable().getModel()).setWorker(selectedRowIndex,
						clientShallowClone);
				reloadClientTable();

			} else {
				LogGuiException.logWarning("Login already exist", "Login already exist. Change login.");
			}
		}

		this.reloadClientTable();
	}

	private void onClickBtnDelete() {

	}

	@Override
	public void onClickBtnRegisterNow() {
		try {
			Character sex = tabbedForm.getPanelWorker().getWorkerDataPanel().getGroupRadioButtonSex()
					.getSelection() != null
							? tabbedForm.getPanelWorker().getWorkerDataPanel().getGroupRadioButtonSex().getSelection()
									.getActionCommand().charAt(0)
							: null;
			Date birthDate = tabbedForm.getPanelWorker().getWorkerDataPanel().getDateChooserBirthDate()
					.getDate() != null
							? Constants.APPLICATION_DATE_FORMAT
									.parse(Constants.APPLICATION_DATE_FORMAT.format(tabbedForm.getPanelWorker()
											.getWorkerDataPanel().getDateChooserBirthDate().getDate()))
							: null;

			RegistrationEvent.forceClearRegistrationEvenet();
			if (birthDate != null && birthDate.before(new Date())
					&& tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldSurname().getText() != null
					&& tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldName().getText() != null
					&& tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldPeselNumber().getText() != null) {

				registrationEvent = RegistrationEvent.getInstance(
						tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldPeselNumber().getText(),
						tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldDocumentNumber().getText(),
						tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldName().getText(),
						tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldSurname().getText(),
						tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldAddress().getText(),
						(City) tabbedForm.getPanelWorker().getWorkerDataPanel().getComboBoxCity().getSelectedItem(),
						sex, birthDate, tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldLogin().getText(),
						String.valueOf(
								tabbedForm.getPanelWorker().getWorkerDataPanel().getPasswordField().getPassword()),
						Role.WORKER_LIBRARIAN);

				WorkerTableModel ctm = (WorkerTableModel) tabbedForm.getPanelWorker().getWorkerTable().getModel();
				ctm.add((Worker) RegistrationEvent.userData);
			} else {
				Utils.displayOptionPane(
						"Podana data urodzenia nie jest wczeœniejszza od be¿¹cej, wype³nione muszê byæ dane nazwiska, imienia, numeru pesel.",
						"Z³a data", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (RefusedRegistrationException e) {
			StringBuilder errorBuilder = new StringBuilder();

			if (Params.getInstance().get("ALREADY_REGISTERED") != null) {
				errorBuilder.append("User with given login or pesel number already exist. Change login.<br>");
				Params.getInstance().remove("ALREADY_REGISTERED");
			}
			if (Params.getInstance().get("PARAMETERS_ERROR") != null) {
				errorBuilder.append("Typed parameters are incorrect. Change wrong data.<br>");
			}
			if (Params.getInstance().get("WEAK_PASSWORD") != null) {
				errorBuilder.append(
						"Password is too weak. Change it, remember that it have to:<br>- password must be between 8 and 16 chars long, <br>- don't allow whitespace,<br>- require at least 1 digit in passwords,<br>- require at least 1 non-alphanumeric char,<br>- require at least 1 upper case char,<br>- require at least 1 lower case char,<br>- don't allow qwerty sequences,<br>- don't allow 4 repeat characters.<br>");
			}

			LogGuiException.logError("Registration error", errorBuilder.toString(), e);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (registrationEvent != null)
			Utils.displayOptionPane(
					"Success, you typed correct data and you are now register. Please wait for user<br> authentication after which you will be able to log into the system. If you have any<br> questions contact with e-library administrator.<br>",
					"Success", JOptionPane.INFORMATION_MESSAGE);
	}

	private void onClickBtnAccept() {
		if (validateClientTableSelection(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow())) {
			int selectedRowIndex = tabbedForm.getPanelWorker().getWorkerTable()
					.convertRowIndexToModel(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow());
			Worker selectedClient = ((WorkerTableModel) tabbedForm.getPanelWorker().getWorkerTable().getModel())
					.getWorker(selectedRowIndex);
			Worker clientShallowClone = (Worker) selectedClient.clone();

			clientShallowClone.setRegistered(
					tabbedForm.getPanelWorker().getAdmIsRegisteredPanel().getChckbxIsRegistered().isSelected());

			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().evict(selectedClient);
			HibernateUtil.getSession().update(Worker.class.getName(), clientShallowClone);
			HibernateUtil.commitTransaction();

			((WorkerTableModel) tabbedForm.getPanelWorker().getWorkerTable().getModel()).setWorker(selectedRowIndex,
					clientShallowClone);
			reloadClientTable();
		} else {
			Utils.displayOptionPane("Select client from table to end registration process.", "Client not selected",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void onClickBtnAcceptAccountant() {
		if (validateClientTableSelection(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow())) {
			int selectedRowIndex = tabbedForm.getPanelWorker().getWorkerTable()
					.convertRowIndexToModel(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow());
			Worker selectedClient = ((WorkerTableModel) tabbedForm.getPanelWorker().getWorkerTable().getModel())
					.getWorker(selectedRowIndex);
			LibraryWorker libraryWorker = selectedClient.getLibraryWorker().iterator().next();
			LibraryWorker libraryWorkerClone= (LibraryWorker) libraryWorker.clone();

			libraryWorkerClone.setIsAccountant(
					tabbedForm.getPanelWorker().getAdmIsAccountantPanel().getChckbxIsAccountant().isSelected());

			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().evict(libraryWorker);
			HibernateUtil.getSession().update(LibraryWorker.class.getName(), libraryWorkerClone);
			HibernateUtil.commitTransaction();
		} else {
			Utils.displayOptionPane("Select client from table to end change account settings process.",
					"Client not selected", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void onClientTableListSelectionChange() {
		if (tabbedForm.getPanelWorker().getWorkerTable()
				.convertRowIndexToModel(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow()) != -1)
			fillRegistrationPanel(tabbedForm.getPanelWorker().getWorkerTable()
					.convertRowIndexToModel(tabbedForm.getPanelWorker().getWorkerTable().getSelectedRow()));
	}

	private void fillRegistrationPanel(int selectedRow) {
		tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldPeselNumber()
				.setText(tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
						Constants.ClientTableColumn.COL_PESEL_NUMBER.getValue()) != null
								? tabbedForm.getPanelWorker().getWorkerTable().getModel()
										.getValueAt(selectedRow,
												Constants.ClientTableColumn.COL_PESEL_NUMBER.getValue())
										.toString()
								: "");

		tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldDocumentNumber()
				.setText(tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
						Constants.ClientTableColumn.COL_DOCUMENT_NUMBER.getValue()) != null
								? tabbedForm.getPanelWorker().getWorkerTable().getModel()
										.getValueAt(selectedRow,
												Constants.ClientTableColumn.COL_DOCUMENT_NUMBER.getValue())
										.toString()
								: "");

		tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldLogin()
				.setText(
						tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
								Constants.ClientTableColumn.COL_LOGIN.getValue()) != null
										? tabbedForm.getPanelWorker().getWorkerTable().getModel()
												.getValueAt(selectedRow,
														Constants.ClientTableColumn.COL_LOGIN.getValue())
												.toString()
										: "");

		tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldName()
				.setText(
						tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
								Constants.ClientTableColumn.COL_NAME.getValue()) != null
										? tabbedForm.getPanelWorker().getWorkerTable().getModel()
												.getValueAt(selectedRow,
														Constants.ClientTableColumn.COL_NAME.getValue())
												.toString()
										: "");

		tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldSurname()
				.setText(
						tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
								Constants.ClientTableColumn.COL_SURNAME.getValue()) != null
										? tabbedForm.getPanelWorker().getWorkerTable().getModel()
												.getValueAt(selectedRow,
														Constants.ClientTableColumn.COL_SURNAME.getValue())
												.toString()
										: "");

		tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldAddress()
				.setText(
						tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
								Constants.ClientTableColumn.COL_ADDRESS.getValue()) != null
										? tabbedForm.getPanelWorker().getWorkerTable().getModel()
												.getValueAt(selectedRow,
														Constants.ClientTableColumn.COL_ADDRESS.getValue())
												.toString()
										: "");

		tabbedForm.getPanelWorker().getWorkerDataPanel().getComboBoxCity()
				.setSelectedItem(tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
						Constants.ClientTableColumn.COL_CITY.getValue()) != null
								? tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
										Constants.ClientTableColumn.COL_CITY.getValue())
								: null);

		if (tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
				Constants.ClientTableColumn.COL_SEX.getValue()) != null
				&& "Male".equals(tabbedForm.getPanelWorker().getWorkerTable().getModel()
						.getValueAt(selectedRow, Constants.ClientTableColumn.COL_SEX.getValue()).toString()))
			tabbedForm.getPanelWorker().getWorkerDataPanel().getGroupRadioButtonSex().setSelected(
					tabbedForm.getPanelWorker().getWorkerDataPanel().getRadioButtonMale().getModel(), true);
		else if (tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
				Constants.ClientTableColumn.COL_SEX.getValue()) != null
				&& "Female".equals(tabbedForm.getPanelWorker().getWorkerTable().getModel()
						.getValueAt(selectedRow, Constants.ClientTableColumn.COL_SEX.getValue()).toString()))
			tabbedForm.getPanelWorker().getWorkerDataPanel().getGroupRadioButtonSex().setSelected(
					tabbedForm.getPanelWorker().getWorkerDataPanel().getRadioButtonFemale().getModel(), true);

		try {
			tabbedForm.getPanelWorker().getWorkerDataPanel().getDateChooserBirthDate()
					.setDate(tabbedForm.getPanelWorker().getWorkerTable().getModel().getValueAt(selectedRow,
							Constants.ClientTableColumn.COL_BIRTH_DATE.getValue()) != null
									? Constants.APPLICATION_DATE_FORMAT.parse(tabbedForm.getPanelWorker()
											.getWorkerTable().getModel()
											.getValueAt(selectedRow,
													Constants.ClientTableColumn.COL_BIRTH_DATE.getValue())
											.toString())
									: null);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		reloadChckbxIsRegistered("Yes".equals(tabbedForm.getPanelWorker().getWorkerTable().getModel()
				.getValueAt(selectedRow, Constants.ClientTableColumn.COL_REGISTERED.getValue())) ? true : false);
	}

	private void switchPerspectiveToAdm(boolean isAdminOrAccountant) {
		tabbedForm.getPanelWorker().getAdmIsRegisteredPanel().setEnabled(isAdminOrAccountant);
		tabbedForm.getPanelWorker().getAdmIsRegisteredPanel().setVisible(isAdminOrAccountant);
	}

	@Override
	public boolean validateRegistration() {
		boolean result = false;

		if (tabbedForm.getPanelWorker().getWorkerDataPanel().getTextFieldLogin().getText().isEmpty()
				|| tabbedForm.getPanelWorker().getWorkerDataPanel().getPasswordField().getPassword().length == 0)
			JOptionPane.showMessageDialog(tabbedForm.getFrame(),
					"There is not enough data in registration form. Require at least login and password.",
					"Not enough data", JOptionPane.ERROR_MESSAGE);
		else
			result = true;

		return result;
	}

	public boolean validateClientTableSelection(int index) {
		return index > -1;
	}

	public boolean validatePasswordFieldIsEmpty() {
		return tabbedForm.getPanelWorker().getWorkerDataPanel().getPasswordField().getPassword() != null ? false : true;
	}
}
