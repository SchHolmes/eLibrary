package com.javafee.tabbedform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.javafee.common.Constants;
import com.javafee.common.IActionForm;
import com.javafee.loginform.LogInEvent;

public class Actions implements IActionForm {
	private TabbedForm tabbedForm = new TabbedForm();

	public void control() {
		tabbedForm.getFrame().setVisible(true);
		initializeForm();

		tabbedForm.getBtnInformation().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		tabbedForm.getBtnLogOut().addActionListener(e -> onClickBtnLogOut());
		tabbedForm.getTabbedPane().addChangeListener(e -> onChangeTabbedPane());
	}

	@Override
	public void initializeForm() {
		//reloadLblLogInInformationDynamic();
		reloadTabbedPane();
	}

	private void reloadLblLogInInformationDynamic() {
		StringBuilder logInInformation = new StringBuilder(tabbedForm.getLblLogInInformation().getText());
		logInInformation.append(" " + LogInEvent.getWorker().getName() + " " + LogInEvent.getWorker().getSurname());
		if (LogInEvent.getIsAdmin())
			logInInformation.append(", Admin");
		tabbedForm.getLblLogInInformation().setText(logInInformation.toString());
	}

	private void reloadTabbedPane() {
		switch (Constants.Tab.getByNumber(tabbedForm.getTabbedPane().getSelectedIndex())) {
		case TAB_CLIENT:
			TabClientEvent.getInstance(tabbedForm);
			break;
		/*case TAB_LIBRARY:
			TabLibraryEvent.getInstance(tabbedForm);
			break;
		case TAB_BOOK:
			TabBookEvent.getInstance(tabbedForm);
			break;
		case TAB_ADM_DICTIONARY:
			if(LogInEvent.getRole() == Role.WORKER_ACCOUNTANT || LogInEvent.getRole() == Role.ADMIN)
				TabAdmDictioaryEvent.getInstance(tabbedForm);
			break;
		case TAB_LOAN_SERVICE:
			TabLoadServiceEvent.getInstance(tabbedForm);
			break;
		case TAB_ADM_WORKER:
			if(LogInEvent.getRole() == Role.WORKER_ACCOUNTANT || LogInEvent.getRole() == Role.ADMIN)
				TabWorkerEvent.getInstance(tabbedForm);
			break;*/
		default:
			break;
		}
	}

	private void onClickBtnLogOut() {
		tabbedForm.getFrame().dispose();
		openStartForm();
	}

	private void onChangeTabbedPane() {
		reloadTabbedPane();
	}

	private void openStartForm() {
		com.javafee.loginform.Actions actions = new com.javafee.loginform.Actions();
		actions.control();
	}
}
