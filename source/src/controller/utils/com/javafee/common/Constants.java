package com.javafee.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
	public enum Role {
		ADMIN, WORKER_ACCOUNTANT, WORKER_LIBRARIAN, CLIENT
	}

	public final int MAIN_SPLASH_SCREEN_DURATION = 1000;
	public final String MAIN_SPLASH_SCREEN_IMAGE = "source/resources/images/splashScreen.jpg";

	public final String APPLICATION_NAME = "e-library";
	public final String APPLICATION_LANGUAGE = "pl";
	public final String APPLICATION_LANGUAGE_PL = "pl";
	public final String APPLICATION_LANGUAGE_EN = "en";
	public static final Integer APPLICATION_MIN_PASSWORD_LENGTH = 8;
	public static final Integer APPLICATION_MAX_PASSWORD_LENGTH = 16;
	public static final Integer APPLICATION_GENERATE_PASSWORD_LENGTH = 16;

	public static final String LANGUAGE_RESOURCE_BUNDLE = "messages";

	public final String DATA_BASE_PACKAGE_TO_SCAN = "com.javafee.hibernate.dto";
	public final String DATA_BASE_URL = "127.0.0.1:5432/elibrary";
	public final String DATA_BASE_USER = "postgres";
	public final String DATA_BASE_PASSWORD = "admin123";
	public final String DATA_BASE_ADMIN_LOGIN = "admin";
	public final String DATA_BASE_ADMIN_PASSWORD = "f7e3c24e1a04758097f69be41aa3cf18";

}
