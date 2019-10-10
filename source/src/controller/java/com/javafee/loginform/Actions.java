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
							SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInError3"));
					Params.getInstance().remove("NO_USER");
				}
				if (Params.getInstance().get("BAD_PASSWORD") != null) {
					errorBuilder.append(
							SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInError2"));
					Params.getInstance().remove("BAD_PASSWORD");
				}
				if (Params.getInstance().get("NOT_REGISTERED") != null) {
					errorBuilder.append(
							SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInError4"));
					Params.getInstance().remove("NOT_REGISTERED");
				}
				if (Params.getInstance().get("NOT_HIRED") != null) {
					errorBuilder.append(
							SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInError9"));
					Params.getInstance().remove("NOT_HIRED");
				}

				LogGuiException.logError(
						SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInErrorTitle"),
						errorBuilder.toString(), e);

				clearLogInFailsInParams();
			}

			if (logInEvent != null) {
				JOptionPane.showMessageDialog(logInForm.getLogInPanel(),
						SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInSuccess1"),
						SystemProperties.getInstance().getResourceBundle().getString("logInForm.logInSuccess1Title"),
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
					SystemProperties.getInstance().getResourceBundle().getString("logInForm.validateLogInError1"),
					SystemProperties.getInstance().getResourceBundle().getString("logInForm.validateLogInError1Title"),
					JOptionPane.ERROR_MESSAGE);
		else
			result = true;

		return result;
	}
}
