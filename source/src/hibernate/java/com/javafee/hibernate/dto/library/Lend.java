package com.javafee.hibernate.dto.library;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.internal.util.Cloneable;

@Entity
@Table(name = "lib_lend")
@SequenceGenerator(name = "seq_lib_lend", sequenceName = "seq_lib_lend", allocationSize = 1)
public class Lend extends Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lib_lend")
	@Column(name = "id_lend", unique = false, nullable = false, insertable = true, updatable = true)
	private Integer idLend;

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_client", unique = false, nullable = true, insertable = true, updatable = true)
	private Client client = new Client();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_volume")
	private Volume volume;

	@Temporal(TemporalType.DATE)
	@Column(name = "lend_date", unique = false, nullable = true, insertable = true, updatable = true, length = 13)
	private Date lendDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "returned_date", unique = false, nullable = true, insertable = true, updatable = true, length = 13)
	private Date returnedDate;

	public Integer getIdLend() {
		return idLend;
	}

	public void setIdLend(Integer idLend) {
		this.idLend = idLend;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Volume getVolume() {
		return volume;
	}

	public void setVolume(Volume volume) {
		this.volume = volume;
	}

	public Date getLendDate() {
		return lendDate;
	}

	public void setLendDate(Date lendDate) {
		this.lendDate = lendDate;
	}

	public Date getReturnedDate() {
		return returnedDate;
	}

	public void setReturnedDate(Date returnedDate) {
		this.returnedDate = returnedDate;
	}

	@Override
	public Object clone() {
		Object result = null;
		try {
			result = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return result;
	}
}
