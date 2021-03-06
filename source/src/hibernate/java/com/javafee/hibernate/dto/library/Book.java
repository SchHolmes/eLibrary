package com.javafee.hibernate.dto.library;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "lib_book")
@SequenceGenerator(name = "seq_lib_book", sequenceName = "seq_lib_book", allocationSize = 1)
public class Book implements Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_lib_book")
	@Column(name = "id_book", unique = false, nullable = false, insertable = true, updatable = true)
	private Integer idBook;
	
	@Column(name = "isbn_number", unique = false, nullable = true, insertable = true, updatable = true, length = 13)
	private String isbnNumber;

	@Column(name = "title", unique = false, nullable = true, insertable = true, updatable = true, length = 200)
	private String title;

	@Column(name = "number_of_page", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	private Integer numberOfPage;

	@Column(name = "number_of_tomes", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	private Integer numberOfTomes;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "lib_book_author", //
			joinColumns = @JoinColumn(name = "id_book"), //
			inverseJoinColumns = @JoinColumn(name = "id_author"))
	private Set<Author> author = new HashSet<Author>(0);

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "lib_book_publishing_house", //
			joinColumns = @JoinColumn(name = "id_book"), //
			inverseJoinColumns = @JoinColumn(name = "id_publishing_house"))
	private Set<PublishingHouse> publishingHouse = new HashSet<PublishingHouse>(0);

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "lib_book_category", //
			joinColumns = @JoinColumn(name = "id_book"), //
			inverseJoinColumns = @JoinColumn(name = "id_category"))
	private Set<Category> category = new HashSet<Category>(0);

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "book")
	private Set<Volume> volume = new HashSet<Volume>(0);

	public Integer getIdBook() {
		return idBook;
	}

	public void setIdBook(Integer idBook) {
		this.idBook = idBook;
	}

	public String getTitle() {
		return title;
	}
	
	public String getIsbnNumber() {
		return isbnNumber;
	}

	public void setIsbnNumber(String isbnNumber) {
		this.isbnNumber = isbnNumber;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNumberOfPage() {
		return numberOfPage;
	}

	public void setNumberOfPage(Integer numberOfPage) {
		this.numberOfPage = numberOfPage;
	}

	public Integer getNumberOfTomes() {
		return numberOfTomes;
	}

	public void setNumberOfTomes(Integer numberOfTomes) {
		this.numberOfTomes = numberOfTomes;
	}

	public Set<Author> getAuthor() {
		return author;
	}

	public void setAuthor(Set<Author> author) {
		this.author = author;
	}

	public Set<PublishingHouse> getPublishingHouse() {
		return publishingHouse;
	}

	public void setPublishingHouse(Set<PublishingHouse> publishingHouse) {
		this.publishingHouse = publishingHouse;
	}

	public Set<Category> getCategory() {
		return category;
	}

	public void setCategory(Set<Category> category) {
		this.category = category;
	}

	public Set<Volume> getVolume() {
		return volume;
	}

	public void setVolume(Set<Volume> volume) {
		this.volume = volume;
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
	
	@Override
	public String toString() {
		return getTitle();
	}
}
