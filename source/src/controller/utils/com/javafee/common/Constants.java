package com.javafee.common;

import java.text.SimpleDateFormat;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
	public enum Tab {
		TAB_CLIENT(0), TAB_BOOK(2), TAB_ADM_DICTIONARY(3), TAB_LIBRARY(1), TAB_LOAN_SERVICE(4), TAB_ADM_WORKER(5);

		private final Integer value;

		Tab(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static Tab getByNumber(int tabbedPaneSelectedIndex) {
			return java.util.stream.Stream.of(Tab.values())
					.filter(item -> item.getValue().equals(tabbedPaneSelectedIndex)).findFirst().get();
		}
	}

	public enum Role {
		ADMIN, WORKER_ACCOUNTANT, WORKER_LIBRARIAN, CLIENT
	}

	public enum ClientTableColumn {
		COL_PESEL_NUMBER(0), COL_DOCUMENT_NUMBER(1), COL_LOGIN(2), COL_NAME(3), COL_SURNAME(4), COL_ADDRESS(5),
		COL_CITY(6), COL_SEX(7), COL_BIRTH_DATE(8), COL_REGISTERED(9);

		private final Integer value;

		ClientTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static ClientTableColumn getByNumber(int clientTableSelectedIndex) {
			return java.util.stream.Stream.of(ClientTableColumn.values())
					.filter(item -> item.getValue().equals(clientTableSelectedIndex)).findFirst().get();
		}
	}

	public enum BookTableColumn {
		COL_TITLE(0), COL_ISBN_NUMBER(1), COL_NUMBER_OF_PAGES(2), COL_NUMBER_OF_TOMES(3);

		private final Integer value;

		BookTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static BookTableColumn getByNumber(int bookTableSelectedIndex) {
			return Stream.of(BookTableColumn.values()).filter(item -> item.getValue().equals(bookTableSelectedIndex))
					.findFirst().get();
		}
	}

	public enum AuthorTableColumn {
		COL_NAME(0), COL_SURNAME(1), COL_BIRTH_DATE(2);

		private final Integer value;

		AuthorTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static AuthorTableColumn getByNumber(int authorTableSelectedIndex) {
			return Stream.of(AuthorTableColumn.values())
					.filter(item -> item.getValue().equals(authorTableSelectedIndex)).findFirst().get();
		}
	}

	public enum CategoryTableColumn {
		COL_NAME(0);

		private final Integer value;

		CategoryTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static CategoryTableColumn getByNumber(int categoryTableSelectedIndex) {
			return Stream.of(CategoryTableColumn.values())
					.filter(item -> item.getValue().equals(categoryTableSelectedIndex)).findFirst().get();
		}
	}

	public enum PublishingHouseTableColumn {
		COL_NAME(0);

		private final Integer value;

		PublishingHouseTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static PublishingHouseTableColumn getByNumber(int publishingHouseTableSelectedIndex) {
			return Stream.of(PublishingHouseTableColumn.values())
					.filter(item -> item.getValue().equals(publishingHouseTableSelectedIndex)).findFirst().get();
		}
	}

	public enum VolumeTableColumn {
		COL_BOOK_TITLE(0), COL_BOOK_ISBN_NUMBER(1), COL_INVENTORY_NUMBER(2), COL_BOOK_NUMBER_OF_PAGE(3),
		COL_BOOK_NUMBER_OF_TOME(4), COL_IS_READING_ROOM(5);

		private final Integer value;

		VolumeTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static VolumeTableColumn getByNumber(int volumeTableSelectedIndex) {
			return Stream.of(VolumeTableColumn.values())
					.filter(item -> item.getValue().equals(volumeTableSelectedIndex)).findFirst().get();
		}
	}

	public enum LendTableColumn {
		COL_CLIENT_BASIC_DATA(0), COL_CLIENT_PESEL_NUMBER(1), COL_CLIENT_DOCUMENT_NUMBER(2), COL_VOLUME_BOOK_TITLE(3),
		COL_VOLUME_BOOK_ISBN_NUMBER(4), COL_VOLUME_INVENTORY_NUMBER(5), COL_LEND_DATE(6), COL_RETURNED_DATE(7),
		COL_PENALTY(8);

		private final Integer value;

		LendTableColumn(final int newValue) {
			value = newValue;
		}

		public Integer getValue() {
			return value;
		}

		public static LendTableColumn getByNumber(int lendTableSelectedIndex) {
			return Stream.of(LendTableColumn.values()).filter(item -> item.getValue().equals(lendTableSelectedIndex))
					.findFirst().get();
		}
	}

	public enum Context {
		ADDITION, MODIFICATION, CANCELED, LOAN, READING_ROOM;
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
	public static final SimpleDateFormat APPLICATION_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	public static final String APPLICATION_CURRENCY = "PLN";
	
	public static final String LANGUAGE_RESOURCE_BUNDLE = "messages";

	public static final String RADIO_BUTTON_AUTHOR = "Author";
	public static final String RADIO_BUTTON_CATEGORY = "Category";
	public static final String RADIO_BUTTON_PUBLISHING_HOUSE = "Publishing house";

	public static final Boolean DATA_BASE_REGISTER_DEFAULT_FLAG = false;
	public static final Character DATA_BASE_MALE_SIGN = 'M';
	public static final Character DATA_BASE_FEMALE_SIGN = 'F';
	public final String DATA_BASE_PACKAGE_TO_SCAN = "com.javafee.hibernate.dto";
	public final String DATA_BASE_URL = "127.0.0.1:5432/elibrary";
	public final String DATA_BASE_USER = "postgres";
	public final String DATA_BASE_PASSWORD = "admin123";
	public final String DATA_BASE_ADMIN_LOGIN = "admin";
	public final String DATA_BASE_ADMIN_PASSWORD = "f7e3c24e1a04758097f69be41aa3cf18";
	public static double PENALTY_VALUE = 0.60;

}
