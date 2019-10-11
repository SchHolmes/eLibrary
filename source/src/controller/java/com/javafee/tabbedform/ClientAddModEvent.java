package com.javafee.tabbedform;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import com.javafee.common.Constants;
import com.javafee.common.Constants.Context;
import com.javafee.common.Constants.Role;
import com.javafee.common.Params;
import com.javafee.common.SystemProperties;
import com.javafee.common.Utils;
import com.javafee.common.Validator;
import com.javafee.exception.LogGuiException;
import com.javafee.exception.RefusedRegistrationException;
import com.javafee.hibernate.dao.HibernateDao;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.association.City;
import com.javafee.hibernate.dto.library.Client;
import com.javafee.loginform.RegistrationEvent;
import com.javafee.model.ClientTableModel;
import com.javafee.tabbedform.clients.frames.ClientAddModFrame;

public class ClientAddModEvent {

	private ClientAddModFrame clientAddModFrame;

	private RegistrationEvent registrationEvent;
	private ClientTableModel clientTableModel;

	public void control(Context context, ClientTableModel clientTableModel) {
		this.clientTableModel = clientTableModel;
		openClientAddModFrame(context);

		clientAddModFrame.getCockpitConfirmationPanel().getBtnAccept()
				.addActionListener(e -> onClickBtnAccept(context));
	}

	private void onClickBtnAccept(Context context) {
		if (context == Context.ADDITION) {
			if (validateRegistration())
				registerNow();
		} else if (context == Context.MODIFICATION) {
			modificateClient();
		}
	}

	private void modificateClient() {
		Client clientShallowClone = (Client) Params.getInstance().get("selectedClientShallowClone");
		Params.getInstance().remove("selectedClientShallowClone");

		clientShallowClone.setPeselNumber(clientAddModFrame.getClientDataPanel().getTextFieldPeselNumber().getText());
		clientShallowClone
				.setDocumentNumber(clientAddModFrame.getClientDataPanel().getTextFieldDocumentNumber().getText());
		clientShallowClone.setName(clientAddModFrame.getClientDataPanel().getTextFieldName().getText());
		clientShallowClone.setSurname(clientAddModFrame.getClientDataPanel().getTextFieldSurname().getText());
		clientShallowClone.setAddress(clientAddModFrame.getClientDataPanel().getTextFieldAddress().getText());
		clientShallowClone.setCity((City) clientAddModFrame.getClientDataPanel().getComboBoxCity().getSelectedItem());
		clientShallowClone.setSex(
				clientAddModFrame.getClientDataPanel().getRadioButtonMale().isSelected() ? Constants.DATA_BASE_MALE_SIGN
						: Constants.DATA_BASE_FEMALE_SIGN);
		clientShallowClone
				.setBirthDate(clientAddModFrame.getClientDataPanel().getDateChooserBirthDate().getDate() != null
						? clientAddModFrame.getClientDataPanel().getDateChooserBirthDate().getDate()
						: null);
		clientShallowClone.setLogin(clientAddModFrame.getClientDataPanel().getTextFieldLogin().getText());

		// if (!validatePasswordFieldIsEmpty())
		// Utils.displayOptionPane(
		// SystemProperties.getInstance().getResourceBundle()
		// .getString("tabClientEvent.validatePasswordFieldIsEmptyWarning2"),
		// SystemProperties.getInstance().getResourceBundle()
		// .getString("tabClientEvent.validatePasswordFieldIsEmptyWarning2Title"),
		// JOptionPane.WARNING_MESSAGE);

		if (Validator.validateClientUpdate(clientShallowClone)) {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession()
					.evict(clientTableModel.getClient((Integer) Params.getInstance().get("selectedRowIndex")));
			HibernateUtil.getSession().update(Client.class.getName(), clientShallowClone);
			HibernateUtil.commitTransaction();
		}
		//
		clientTableModel.setClient((Integer) Params.getInstance().get("selectedRowIndex"), clientShallowClone);
		clientTableModel.fireTableDataChanged();
		Params.getInstance().remove("selectedRowIndex");
		// reloadClientTable();
		//
		// } else {
		// LogGuiException.logWarning(
		// SystemProperties.getInstance().getResourceBundle()
		// .getString("clientTableModel.constraintViolationErrorTitle"),
		// SystemProperties.getInstance().getResourceBundle()
		// .getString("clientTableModel.constraintViolationError"));
		// }
	}

	private void openClientAddModFrame(Context context) {
		if (clientAddModFrame == null || (clientAddModFrame != null && !clientAddModFrame.isDisplayable())) {
			clientAddModFrame = new ClientAddModFrame();
			if (context == Context.MODIFICATION) {
				clientAddModFrame.getClientDataPanel().getLblPassword().setVisible(false);
				clientAddModFrame.getClientDataPanel().getPasswordField().setVisible(false);
				reloadRegistrationPanel();
			}
			clientAddModFrame.setVisible(true);
		} else {
			clientAddModFrame.toFront();
		}
	}

	private void reloadRegistrationPanel() {
		reloadComboBoxCity();
		fillRegistrationPanel();
	}

	private void reloadComboBoxCity() {
		DefaultComboBoxModel<City> comboBoxCity = new DefaultComboBoxModel<City>();
		HibernateDao<City, Integer> city = new HibernateDao<City, Integer>(City.class);
		List<City> cityListToSort = city.findAll();
		cityListToSort.sort(Comparator.comparing(City::getName, Comparator.nullsFirst(Comparator.naturalOrder())));
		cityListToSort.forEach(c -> comboBoxCity.addElement(c));

		clientAddModFrame.getClientDataPanel().getComboBoxCity().setModel(comboBoxCity);
	}

	private void fillRegistrationPanel() {
		clientAddModFrame.getClientDataPanel().getTextFieldPeselNumber()
				.setText(((Client) Params.getInstance().get("selectedClientShallowClone")).getPeselNumber() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getPeselNumber().toString()
						: "");

		clientAddModFrame.getClientDataPanel().getTextFieldDocumentNumber()
				.setText(((Client) Params.getInstance().get("selectedClientShallowClone")).getDocumentNumber() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getDocumentNumber()
								.toString()
						: "");

		clientAddModFrame.getClientDataPanel().getTextFieldLogin()
				.setText(((Client) Params.getInstance().get("selectedClientShallowClone")).getLogin() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getLogin().toString()
						: "");

		clientAddModFrame.getClientDataPanel().getTextFieldName()
				.setText(((Client) Params.getInstance().get("selectedClientShallowClone")).getName() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getName().toString()
						: "");

		clientAddModFrame.getClientDataPanel().getTextFieldSurname()
				.setText(((Client) Params.getInstance().get("selectedClientShallowClone")).getSurname() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getSurname().toString()
						: "");

		clientAddModFrame.getClientDataPanel().getTextFieldAddress()
				.setText(((Client) Params.getInstance().get("selectedClientShallowClone")).getAddress() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getAddress().toString()
						: "");

		clientAddModFrame.getClientDataPanel().getComboBoxCity()
				.setSelectedItem(((Client) Params.getInstance().get("selectedClientShallowClone")).getCity() != null
						? ((Client) Params.getInstance().get("selectedClientShallowClone")).getCity()
						: null);

		if (((Client) Params.getInstance().get("selectedClientShallowClone")).getSex() != null
				&& Constants.DATA_BASE_MALE_SIGN.toString().equals(((Client) Params.getInstance().get("selectedClientShallowClone")).getSex().toString()))
			clientAddModFrame.getClientDataPanel().getGroupRadioButtonSex()
					.setSelected(clientAddModFrame.getClientDataPanel().getRadioButtonMale().getModel(), true);
		else if (((Client) Params.getInstance().get("selectedClientShallowClone")).getSex() != null
				&& Constants.DATA_BASE_FEMALE_SIGN.toString().equals(((Client) Params.getInstance().get("selectedClientShallowClone")).getSex().toString()))
			clientAddModFrame.getClientDataPanel().getGroupRadioButtonSex()
					.setSelected(clientAddModFrame.getClientDataPanel().getRadioButtonFemale().getModel(), true);

		try {
			clientAddModFrame.getClientDataPanel().getDateChooserBirthDate()
					.setDate(((Client) Params.getInstance().get("selectedClientShallowClone")).getBirthDate() != null
							? Constants.APPLICATION_DATE_FORMAT.parse(Constants.APPLICATION_DATE_FORMAT.format(
									((Client) Params.getInstance().get("selectedClientShallowClone")).getBirthDate()))
							: null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void registerNow() {
		try {
			Character sex = clientAddModFrame.getClientDataPanel().getGroupRadioButtonSex().getSelection() != null
					? clientAddModFrame.getClientDataPanel().getGroupRadioButtonSex().getSelection().getActionCommand()
							.charAt(0)
					: null;
			Date birthDate = clientAddModFrame.getClientDataPanel().getDateChooserBirthDate().getDate() != null
					? Constants.APPLICATION_DATE_FORMAT.parse(Constants.APPLICATION_DATE_FORMAT
							.format(clientAddModFrame.getClientDataPanel().getDateChooserBirthDate().getDate()))
					: null;

			RegistrationEvent.forceClearRegistrationEvenet();
			if (birthDate != null && birthDate.before(new Date())) {
				registrationEvent = RegistrationEvent.getInstance(
						clientAddModFrame.getClientDataPanel().getTextFieldPeselNumber().getText(),
						clientAddModFrame.getClientDataPanel().getTextFieldDocumentNumber().getText(),
						clientAddModFrame.getClientDataPanel().getTextFieldName().getText(),
						clientAddModFrame.getClientDataPanel().getTextFieldSurname().getText(),
						clientAddModFrame.getClientDataPanel().getTextFieldAddress().getText(),
						(City) clientAddModFrame.getClientDataPanel().getComboBoxCity().getSelectedItem(), sex,
						birthDate, clientAddModFrame.getClientDataPanel().getTextFieldLogin().getText(),
						String.valueOf(clientAddModFrame.getClientDataPanel().getPasswordField().getPassword()),
						Role.CLIENT);

				clientTableModel.add((Client) RegistrationEvent.userData);
			} else {
				Utils.displayOptionPane(
						"Podana data urodzenia nie jest wczeœniejszza od be¿¹cej, wype³nione muszê byæ dane nazwiska, imienia, numeru pesel.",
						"Z³a data", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (RefusedRegistrationException e) {
			StringBuilder errorBuilder = new StringBuilder();

			if (Params.getInstance().get("ALREADY_REGISTERED") != null) {
				errorBuilder.append(
						"User with given login or pesel number already exist. Change login.<br>");
				Params.getInstance().remove("ALREADY_REGISTERED");
			}
			if (Params.getInstance().get("PARAMETERS_ERROR") != null) {
				errorBuilder.append(
						"Typed parameters are incorrect. Change wrong data.<br>");
			}
			if (Params.getInstance().get("WEAK_PASSWORD") != null) {
				errorBuilder.append(
						"Password is too weak. Change it, remember that it have to:<br>- password must be between 8 and 16 chars long, <br>- don't allow whitespace,<br>- require at least 1 digit in passwords,<br>- require at least 1 non-alphanumeric char,<br>- require at least 1 upper case char,<br>- require at least 1 lower case char,<br>- don't allow qwerty sequences,<br>- don't allow 4 repeat characters.<br>");
			}

			LogGuiException.logError(
					"Registration error",
					errorBuilder.toString(), e);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (registrationEvent != null)
			Utils.displayOptionPane(
					"Success, you typed correct data and you are now register. Please wait for user<br> authentication after which you will be able to log into the system. If you have any<br> questions contact with e-library administrator.<br>",
					"Success",
					JOptionPane.INFORMATION_MESSAGE);
	}

	private boolean validateRegistration() {
		boolean result = false;

		if (clientAddModFrame.getClientDataPanel().getTextFieldLogin().getText().isEmpty()
				|| clientAddModFrame.getClientDataPanel().getPasswordField().getPassword().length == 0)
			JOptionPane.showMessageDialog(clientAddModFrame,
					"There is not enough data in registration form. Require at least login and password.",
					"Not enough data",
					JOptionPane.ERROR_MESSAGE);
		else
			result = true;

		return result;
	}
}
