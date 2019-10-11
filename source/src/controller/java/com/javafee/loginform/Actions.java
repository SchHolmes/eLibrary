package com.javafee.loginform;

import javax.swing.JOptionPane;

import com.javafee.common.Constants;
import com.javafee.common.LogGuiException;
import com.javafee.common.Params;
import com.javafee.common.SystemProperties;
import com.javafee.exception.RefusedLogInException;
import com.javafee.startform.LogInForm;
import com.javafee.startform.MainSplashScreen;

public class Actions {
	private LogInForm logInForm = new LogInForm();

	private LogInEvent logInEvent;

	public void control() {
		if (MainSplashScreen.isNull())
			MainSplashScreen.getInstance(Constants.MAIN_SPLASH_SCREEN_IMAGE, logInForm,
					Constants.MAIN_SPLASH_SCREEN_DURATION);
		else
			logInForm.getFrame().setVisible(true);

		logInForm.getBtnLogIn().addActionListener(e -> onClickBtnLogIn());
	}

	private void onClickBtnLogIn() {
		if (validateLogIn()) {
			try {
				logInEvent = LogInEvent.getInstance(logInForm.getTextFieldLogin().getText(),
						String.valueOf(logInForm.getPasswordField().getPassword()));
			} catch (RefusedLogInException e) {
				StringBuilder errorBuilder = new StringBuilder();

				if (Params.getInstance().get("NO_USER") != null) {
					errorBuilder.append(
							"No user in database.<br>");
					Params.getInstance().remove("NO_USER");
				}
				if (Params.getInstance().get("BAD_PASSWORD") != null) {
					errorBuilder.append(
							"Invalid password.<br>");
					Params.getInstance().remove("BAD_PASSWORD");
				}
				if (Params.getInstance().get("NOT_REGISTERED") != null) {
					errorBuilder.append(
							"User not registered. Please wait for user authentication after which you will be<br>able to log into the system.<br>");
					Params.getInstance().remove("NOT_REGISTERED");
				}
				if (Params.getInstance().get("NOT_HIRED") != null) {
					errorBuilder.append(
							"User not hired.<br>");
					Params.getInstance().remove("NOT_HIRED");
				}

				LogGuiException.logError(
						"Log in error",
						errorBuilder.toString(), e);

				clearLogInFailsInParams();
			}

			if (logInEvent != null) {
				JOptionPane.showMessageDialog(logInForm.getLogInPanel(),
						"Success, you typed the right password.",
						"Success",
						JOptionPane.INFORMATION_MESSAGE);
				openTabbedForm();
			}
		}
	}

	private void openTabbedForm() {
		logInForm.getFrame().setVisible(false);
		com.javafee.tabbedform.Actions actions = new com.javafee.tabbedform.Actions();
		actions.control();
	}

	private void clearLogInFailsInParams() {
		Params.getInstance().remove("NO_USER");
		Params.getInstance().remove("BAD_PASSWORD");
		Params.getInstance().remove("NOT_REGISTERED");
		Params.getInstance().remove("NOT_HIRED");
	}

	private boolean validateLogIn() {
		boolean result = false;
		if (logInForm.getTextFieldLogin().getText().isEmpty() || logInForm.getPasswordField().getPassword().length == 0)
			JOptionPane.showMessageDialog(logInForm.$$$getRootComponent$$$(),
					"There is not enough data in log in form. Require at least login and password.",
					"Not enough data",
					JOptionPane.ERROR_MESSAGE);
		else
			result = true;

		return result;
	}
}
