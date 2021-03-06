package com.javafee.hibernate.dto.library;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@NamedQueries({ @NamedQuery(name = "Volume.checkIfInventoryNumberExist", query = "from Volume where inventoryNumber = :inventoryNumber") })
@Table(name = "lib_volume", uniqueConstraints = { @UniqueConstraint(columnNames = { "inventory_number" }) })
@SequenceGenerator(name = "seq_lib_volume", sequenceName = "seq_lib_volume", allocationSize = 1)
public class Volume implements Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lib_volume")
	@Column(name = "id_volume", unique = false, nullable = false, insertable = true, updatable = true)
	private Integer idVolume;
	
	@Column(name = "inventory_number", unique = false, nullable = true, insertable = true, updatable = true, length = 13)
	private String inventoryNumber;
	
	@Column(name = "state", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	private Character state;

	@Column(name = "is_reading_room", unique = false, nullable = true, insertable = true, updatable = true)
	private Boolean isReadingRoom = false;
	
	@Column(name = "is_lended", unique = false, nullable = true, insertable = true, updatable = true)
	private Boolean isLended = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_book")
	private Book book;
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY, mappedBy = "volume")
	private Set<Lend> lend = new HashSet<Lend>(0);
	
	@Column(name = "penalty_value", unique = false, nullable = true, insertable = true, updatable = true, precision = 9, scale = 2)
	private BigDecimal penaltyValue;

	public Integer getIdVolume() {
		return idVolume;
	}

	public void setIdVolume(Integer idVolume) {
		this.idVolume = idVolume;
	}
	
	public String getInventoryNumber() {
		return inventoryNumber;
	}
	
	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}
	
	public Character getState() {
		return state;
	}
	
	public void setState(Character state) {
		this.state = state;
	}

	public Boolean getIsReadingRoom() {
		return isReadingRoom;
	}

	public void setIsReadingRoom(Boolean isReadingRoom) {
		this.isReadingRoom = isReadingRoom;
	}
	
	public Boolean getIsLended() {
		return isLended;
	}
	
	public void setIsLended(Boolean isLended) {
		this.isLended = isLended;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Set<Lend> getLend() {
		return lend;
	}
	
	public void setLend(Set<Lend> lend) {
		this.lend = lend;
	}
	
	public BigDecimal getPenaltyValue() {
		return penaltyValue;
	}
	
	public void setPenaltyValue(BigDecimal penaltyValue) {
		this.penaltyValue = penaltyValue;
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
