package com.javafee.hibernate.dto.library;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "lib_author")
@SequenceGenerator(name = "seq_lib_author", sequenceName = "seq_lib_author", allocationSize = 1)
public class Author {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lib_author")
	@Column(name = "id_author", unique = false, nullable = false, insertable = true, updatable = true)
	private Integer idAuthor;

	@Column(name = "name", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	private String name;

	@Column(name = "surname", unique = false, nullable = true, insertable = true, updatable = true, length = 80)
	private String surname;

	@Column(name = "nickname", unique = false, nullable = true, insertable = true, updatable = true, length = 80)
	private String nickname;

	@Temporal(TemporalType.DATE)
	@Column(name = "birth_date", unique = false, nullable = true, insertable = true, updatable = true, length = 13)
	private Date birthDate;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "author")
	private Set<Book> book = new HashSet<Book>(0);

	public Integer getIdAuthor() {
		return idAuthor;
	}

	public void setIdAuthor(Integer idAuthor) {
		this.idAuthor = idAuthor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Set<Book> getBook() {
		return book;
	}

	public void setBook(Set<Book> book) {
		this.book = book;
	}
	
	@Override
	public String toString() {
		String result = "";
		if(surname != null && name == null)
			result = surname;
		else if(surname == null && name != null)
			result = name;
		else if( surname != null && name != null)
			result = surname + " " + name;
		return result;
	}
}
