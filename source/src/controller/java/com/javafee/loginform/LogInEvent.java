package com.javafee.loginform;

import java.util.Date;

import com.javafee.common.Common;
import com.javafee.common.Constants;
import com.javafee.common.Params;
import com.javafee.exception.RefusedLogInException;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Client;
import com.javafee.hibernate.dto.library.LibraryWorker;
import com.javafee.hibernate.dto.library.Worker;

import lombok.Getter;

public final class LogInEvent {
	@Getter
	private static LogInEvent logInEvent = null;
	@Getter
	private static Constants.Role role;
	@Getter
	private static Client client;
	@Getter
	private static Worker worker;
	@Getter
	private static LibraryWorker libraryWorker;
	@Getter
	private static Boolean isAdmin;
	@Getter
	private static Date logInDate;

	public enum LogInFailureCause {
		NOT_REGISTERED, NOT_HIRED, BAD_PASSWORD, NO_USER, UNIDENTIFIED
	}

	private LogInEvent() {
	}

	public static LogInEvent getInstance(String login, String password) throws RefusedLogInException {
		if (checkLogAndRole(login, password)) {
			logInEvent = new LogInEvent();
			logInDate = new Date();
		} else
			throw new RefusedLogInException("Cannot log in to the system");
		return logInEvent;
	}

	public Object getUser() {
		Object user = null;
		if (role == Constants.Role.CLIENT)
			user = client;
		else if (role == Constants.Role.WORKER_ACCOUNTANT || role == Constants.Role.WORKER_LIBRARIAN)
			user = worker;

		return user;
	}

	private static boolean checkLogAndRole(String login, String password) {
		boolean result = false;
		client = (Client) HibernateUtil.getSession().getNamedQuery("Client.checkIfClientLoginExist")
				.setParameter("login", login).uniqueResult();
		worker = (Worker) HibernateUtil.getSession().getNamedQuery("Worker.checkIfWorkerLoginExist")
				.setParameter("login", login).uniqueResult();
		isAdmin = Common.isAdmin(login, password);

		if (client != null) {
			isAdmin = Common.isAdmin(client);
			if (isAdmin)
				role = Constants.Role.ADMIN;
			if (checkLoginAndPassword(password)) {
				if (client.getRegistered()) {
					role = Constants.Role.CLIENT;
					result = true;
				} else
					Params.getInstance().add("NOT_REGISTERED", LogInFailureCause.NOT_REGISTERED);
			}
		} else if (worker == null && isAdmin) {
			role = Constants.Role.ADMIN;
			result = true;
		} else if (worker != null) {
			if (checkLoginAndPassword(password)) {
				if (worker.getRegistered()) {
					if (checkIfHired(worker)) {
						if (libraryWorker.getIsAccountant() != null)
							role = libraryWorker.getIsAccountant() ? Constants.Role.WORKER_ACCOUNTANT : Constants.Role.WORKER_LIBRARIAN;
						result = true;
					} else
						Params.getInstance().add("NOT_HIRED", LogInFailureCause.NOT_HIRED);
				} else
					Params.getInstance().add("NOT_REGISTERED", LogInFailureCause.NOT_REGISTERED);
			}
		} else
			Params.getInstance().add("NO_USER", LogInFailureCause.NO_USER);

		return result;
	}

	// TODO 08.07.2017 Method checks if worker is hired in one library, that's the
	// programmer assumption.
	private static boolean checkIfHired(Worker worker) {
		libraryWorker = (LibraryWorker) HibernateUtil.getSession()
				.getNamedQuery("LibraryWorker.checkIfLibraryWorkerHiredExist")
				.setParameter("idWorker", worker.getIdUserData()).uniqueResult();
		return libraryWorker != null;
	}

	private static boolean checkLoginAndPassword(String password) {
		boolean result = false;
		String md5 = Common.createMd5(password);

		if (client != null && md5.equals(client.getPassword()))
			result = true;
		else if (worker != null && md5.equals(worker.getPassword()))
			result = true;

		if (!result && worker != null)
			Params.getInstance().add("BAD_PASSWORD", LogInFailureCause.BAD_PASSWORD);
		if (!result && client != null)
			Params.getInstance().add("BAD_PASSWORD", LogInFailureCause.BAD_PASSWORD);

		return result;
	}

	public static void clearLogInData() {
		LogInEvent.role = null;
		LogInEvent.logInDate = null;
	}
}
