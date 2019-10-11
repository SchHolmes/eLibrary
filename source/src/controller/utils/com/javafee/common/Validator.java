package com.javafee.common;

import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Client;
import com.javafee.hibernate.dto.library.Worker;

public final class Validator {
	public static boolean validateClientUpdate(Client client) {
		Client existingLoginClient = (Client) HibernateUtil.getSession().getNamedQuery("Client.checkWithComparingIdIfClientLoginExist").setParameter("login", client.getLogin())
				.setParameter("id", client.getIdUserData()).uniqueResult();
		Client existingPeselClient = (Client) HibernateUtil.getSession().getNamedQuery("UserData.checkWithComparingIdIfUserDataPeselExist").setParameter("peselNumber",  client.getPeselNumber()).setParameter("id", client.getIdUserData()).uniqueResult();
		return client.getPeselNumber() != null && (existingLoginClient != null || existingPeselClient != null) ? false : true;
	}
	
	public static boolean validateClientUpdate(Worker client) {
		Worker existingLoginClient = (Worker) HibernateUtil.getSession().getNamedQuery("Worker.checkWithComparingIdIfClientLoginExist").setParameter("login", client.getLogin())
				.setParameter("id", client.getIdUserData()).uniqueResult();
		Worker existingPeselClient = (Worker) HibernateUtil.getSession().getNamedQuery("UserData.checkWithComparingIdIfUserDataPeselExist").setParameter("peselNumber",  client.getPeselNumber()).setParameter("id", client.getIdUserData()).uniqueResult();
		return client.getPeselNumber() != null && (existingLoginClient != null || existingPeselClient != null) ? false : true;
	}
}
