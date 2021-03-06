package com.javafee.common;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.javafee.hibernate.dto.common.UserData;
import com.javafee.hibernate.dto.library.Client;
import com.javafee.hibernate.dto.library.Worker;

import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.CharacterRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.NumericalSequenceRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordGenerator;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;

public final class Common {

	public static final String createMd5(String password) {
		String md5 = null;
		if (password != null)
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(password.getBytes(), 0, password.length());
				md5 = new BigInteger(1, digest.digest()).toString(16);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}

		return md5;
	}

	public static final String generatePassword() {
		PasswordGenerator generator = new PasswordGenerator();

		// create character rules to generate passwords with
		List<CharacterRule> rules = new ArrayList<CharacterRule>();
		rules.add(new DigitCharacterRule(1));
		rules.add(new NonAlphanumericCharacterRule(1));
		rules.add(new UppercaseCharacterRule(1));
		rules.add(new LowercaseCharacterRule(1));

		return generator.generatePassword(Constants.APPLICATION_GENERATE_PASSWORD_LENGTH, rules);
	}

	public static final boolean checkPasswordStrenght(String password) {
		boolean result = false;
		// password must be between 8 and 16 chars long
		LengthRule lengthRule = new LengthRule(Constants.APPLICATION_MIN_PASSWORD_LENGTH,
				Constants.APPLICATION_MAX_PASSWORD_LENGTH);
		// don't allow whitespace
		WhitespaceRule whitespaceRule = new WhitespaceRule();
		// control allowed characters
		CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
		// require at least 1 digit in passwords
		charRule.getRules().add(new DigitCharacterRule(1));
		// require at least 1 non-alphanumeric char
		charRule.getRules().add(new NonAlphanumericCharacterRule(1));
		// require at least 1 upper case char
		charRule.getRules().add(new UppercaseCharacterRule(1));
		// require at least 1 lower case char
		charRule.getRules().add(new LowercaseCharacterRule(1));
		// require at least 3 of the previous rules be met
		charRule.setNumberOfCharacteristics(3);
		// don't allow alphabetical sequences
		AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();
		// don't allow numerical sequences of length 3
		NumericalSequenceRule numSeqRule = new NumericalSequenceRule(3, true);
		// don't allow qwerty sequences
		QwertySequenceRule qwertySeqRule = new QwertySequenceRule();
		// don't allow 4 repeat characters
		RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);
		// group all rules together in a List
		List<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(lengthRule);
		ruleList.add(whitespaceRule);
		ruleList.add(charRule);
		ruleList.add(alphaSeqRule);
		ruleList.add(numSeqRule);
		ruleList.add(qwertySeqRule);
		ruleList.add(repeatRule);

		PasswordValidator validator = new PasswordValidator(ruleList);
		PasswordData passwordData = new PasswordData(new Password(password));
		RuleResult ruleResult = validator.validate(passwordData);

		if (ruleResult.isValid()) {
			System.out.println("Valid password");
			result = true;
		}

		return result;
	}

	public static boolean isAdmin(String login, String password) {
		return Constants.DATA_BASE_ADMIN_LOGIN.equals(login)
				&& Constants.DATA_BASE_ADMIN_PASSWORD.equals(Common.createMd5(password));
	}

	public static boolean isAdmin(Worker worker) {
		return Constants.DATA_BASE_ADMIN_LOGIN.equals(worker.getLogin())
				&& Constants.DATA_BASE_ADMIN_PASSWORD.equals(worker.getPassword());
	}

	public static boolean isAdmin(Client client) {
		return Constants.DATA_BASE_ADMIN_LOGIN.equals(client.getLogin())
				&& Constants.DATA_BASE_ADMIN_PASSWORD.equals(client.getPassword());
	}

	public static boolean isAdmin(UserData userData) {
		return Constants.DATA_BASE_ADMIN_LOGIN.equals(userData.getLogin())
				&& Constants.DATA_BASE_ADMIN_PASSWORD.equals(userData.getPassword());
	}
}
