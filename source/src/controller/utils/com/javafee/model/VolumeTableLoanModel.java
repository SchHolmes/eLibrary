package com.javafee.model;

import java.util.stream.Collectors;

import com.javafee.hibernate.dao.HibernateUtil;

public class VolumeTableLoanModel extends VolumeTableModel {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void prepareHibernateDao() {
		this.volumes = HibernateUtil.getSession().createQuery("from Volume as vol join fetch vol.book").list();
		this.volumes = volumes.stream().filter(vol -> !vol.getIsReadingRoom()).collect(Collectors.toList());
	}
}
