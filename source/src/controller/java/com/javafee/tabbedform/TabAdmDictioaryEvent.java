package com.javafee.tabbedform;

import java.util.Comparator;
import java.util.List;

import javax.persistence.RollbackException;
import javax.swing.DefaultComboBoxModel;

import com.javafee.common.Constants;
import com.javafee.common.IActionForm;
import com.javafee.exception.RefusedAdmDictionaryEventLoadingException;
import com.javafee.hibernate.dao.HibernateDao;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Author;
import com.javafee.hibernate.dto.library.Category;
import com.javafee.hibernate.dto.library.PublishingHouse;

public class TabAdmDictioaryEvent implements IActionForm {
	private TabbedForm tabbedForm;

	private String pressedRadioButton = Constants.RADIO_BUTTON_AUTHOR;

	@SuppressWarnings("unused")
	private static TabAdmDictioaryEvent admDictionaryEvent = null;

	public TabAdmDictioaryEvent(TabbedForm tabbedForm) {
		this.control(tabbedForm);
	}

	public static TabAdmDictioaryEvent getInstance(TabbedForm tabbedForm) {
		if (admDictionaryEvent == null) {
			admDictionaryEvent = new TabAdmDictioaryEvent(tabbedForm);
		} else
			new RefusedAdmDictionaryEventLoadingException("Cannot adm dictionary event loading");
		return admDictionaryEvent;
	}

	public void control(TabbedForm tabbedForm) {
		setTabbedForm(tabbedForm);
		initializeForm();

		tabbedForm.getPanelAdmDictionary().getRadioButtonAuthor()
				.addActionListener(e -> switchRadioButtonsEnable(Constants.RADIO_BUTTON_AUTHOR));
		tabbedForm.getPanelAdmDictionary().getRadioButtonCategory()
				.addActionListener(e -> switchRadioButtonsEnable(Constants.RADIO_BUTTON_CATEGORY));
		tabbedForm.getPanelAdmDictionary().getRadioButtonPublishingHouse()
				.addActionListener(e -> switchRadioButtonsEnable(Constants.RADIO_BUTTON_PUBLISHING_HOUSE));
		tabbedForm.getPanelAdmDictionary().getComboBoxAuthor().addActionListener(e -> onChangeComboBoxAuthor());
		tabbedForm.getPanelAdmDictionary().getComboBoxCategory().addActionListener(e -> onChangeComboBoxCategory());
		tabbedForm.getPanelAdmDictionary().getComboBoxPublishingHouse()
				.addActionListener(e -> onChangeComboBoxPublishingHouse());
		tabbedForm.getPanelAdmDictionary().getCockpitEditionPanel().getBtnAdd()
				.addActionListener(e -> onClickBtnAdd(pressedRadioButton));
		tabbedForm.getPanelAdmDictionary().getCockpitEditionPanel().getBtnModify()
				.addActionListener(e -> onClickBtnModify(pressedRadioButton));
		tabbedForm.getPanelAdmDictionary().getCockpitEditionPanel().getBtnDelete()
				.addActionListener(e -> onClickBtnDelete(pressedRadioButton));
	}

	public void setTabbedForm(TabbedForm tabbedForm) {
		this.tabbedForm = tabbedForm;
	}

	@Override
	public void initializeForm() {
		reloadComboBoxes();
	}

	private void reloadComboBoxes() {
		reloadComboBoxAuthor();
		reloadComboBoxCategory();
		reloadComboBoxPublishingHouse();
	}

	private void reloadComboBoxAuthor() {
		DefaultComboBoxModel<Author> comboBoxAuthor = new DefaultComboBoxModel<Author>();
		HibernateDao<Author, Integer> author = new HibernateDao<Author, Integer>(Author.class);
		List<Author> authorListToSort = author.findAll();
		authorListToSort
				.sort(Comparator.comparing(Author::getSurname, Comparator.nullsFirst(Comparator.naturalOrder())));
		authorListToSort.forEach(c -> comboBoxAuthor.addElement(c));
		comboBoxAuthor.insertElementAt(null, 0);

		tabbedForm.getPanelAdmDictionary().getComboBoxAuthor().setModel(comboBoxAuthor);
	}

	private void reloadComboBoxCategory() {
		DefaultComboBoxModel<Category> comboBoxCategory = new DefaultComboBoxModel<Category>();
		HibernateDao<Category, Integer> category = new HibernateDao<Category, Integer>(Category.class);
		List<Category> categoryListToSort = category.findAll();
		categoryListToSort
				.sort(Comparator.comparing(Category::getName, Comparator.nullsFirst(Comparator.naturalOrder())));
		categoryListToSort.forEach(c -> comboBoxCategory.addElement(c));
		comboBoxCategory.insertElementAt(null, 0);

		tabbedForm.getPanelAdmDictionary().getComboBoxCategory().setModel(comboBoxCategory);
	}

	private void reloadComboBoxPublishingHouse() {
		DefaultComboBoxModel<PublishingHouse> comboBoxPublishingHouse = new DefaultComboBoxModel<PublishingHouse>();
		HibernateDao<PublishingHouse, Integer> publishingHouse = new HibernateDao<PublishingHouse, Integer>(
				PublishingHouse.class);
		List<PublishingHouse> publishingHouseListToSort = publishingHouse.findAll();
		publishingHouseListToSort
				.sort(Comparator.comparing(PublishingHouse::getName, Comparator.nullsFirst(Comparator.naturalOrder())));
		publishingHouseListToSort.forEach(c -> comboBoxPublishingHouse.addElement(c));
		comboBoxPublishingHouse.insertElementAt(null, 0);

		tabbedForm.getPanelAdmDictionary().getComboBoxPublishingHouse().setModel(comboBoxPublishingHouse);
	}

	private void onClickBtnAdd(String pressedRadioButton) {
		if(tabbedForm.getPanelAdmDictionary().getRadioButtonAuthor().isSelected())
			pressedRadioButton = Constants.RADIO_BUTTON_AUTHOR;
		if(tabbedForm.getPanelAdmDictionary().getRadioButtonCategory().isSelected())
			pressedRadioButton = Constants.RADIO_BUTTON_CATEGORY;
		if(tabbedForm.getPanelAdmDictionary().getRadioButtonPublishingHouse().isSelected())
			pressedRadioButton = Constants.RADIO_BUTTON_PUBLISHING_HOUSE;
		
		switch (pressedRadioButton) {
		case Constants.RADIO_BUTTON_AUTHOR:
			try {
				HibernateUtil.beginTransaction();
				Author author = new Author();
				author.setName(tabbedForm.getPanelAdmDictionary().getTextFieldAuthorName().getText() != null
						? tabbedForm.getPanelAdmDictionary().getTextFieldAuthorName().getText().toString()
						: null);
				author.setNickname(tabbedForm.getPanelAdmDictionary().getTextFieldAuthorNickname().getText() != null
						? tabbedForm.getPanelAdmDictionary().getTextFieldAuthorNickname().getText().toString()
						: null);
				author.setSurname(tabbedForm.getPanelAdmDictionary().getTextFieldAuthorSurname().getText() != null
						? tabbedForm.getPanelAdmDictionary().getTextFieldAuthorSurname().getText().toString()
						: null);
				author.setBirthDate(tabbedForm.getPanelAdmDictionary().getDateChooserBirthDate().getDate() != null
						? tabbedForm.getPanelAdmDictionary().getDateChooserBirthDate().getDate()
						: null);
				HibernateUtil.getSession().save(author);
				HibernateUtil.commitTransaction();
				reloadComboBoxAuthor();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			}
			break;
		case Constants.RADIO_BUTTON_CATEGORY:
			try {
				HibernateUtil.beginTransaction();
				Category category = new Category();
				category.setName(tabbedForm.getPanelAdmDictionary().getTextFieldCategoryName().getText() != null
						? tabbedForm.getPanelAdmDictionary().getTextFieldCategoryName().getText().toString()
						: null);
				HibernateUtil.getSession().save(category);
				HibernateUtil.commitTransaction();
				reloadComboBoxCategory();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			}
			break;
		case Constants.RADIO_BUTTON_PUBLISHING_HOUSE:
			try {
				HibernateUtil.beginTransaction();
				PublishingHouse publishingHouse = new PublishingHouse();
				publishingHouse
						.setName(tabbedForm.getPanelAdmDictionary().getTextFieldPublishingHouseName().getText() != null
								? tabbedForm.getPanelAdmDictionary().getTextFieldPublishingHouseName().getText()
										.toString()
								: null);
				HibernateUtil.getSession().save(publishingHouse);
				HibernateUtil.commitTransaction();
				reloadComboBoxPublishingHouse();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void onClickBtnModify(String pressedRadioButton) {

	}

	private void onClickBtnDelete(String pressedRadioButton) {

	}

	private void onChangeComboBoxAuthor() {
		fillDictionaryData(Constants.RADIO_BUTTON_AUTHOR);
	}

	private void onChangeComboBoxCategory() {
		fillDictionaryData(Constants.RADIO_BUTTON_CATEGORY);
	}

	private void onChangeComboBoxPublishingHouse() {
		fillDictionaryData(Constants.RADIO_BUTTON_PUBLISHING_HOUSE);
	}

	private void fillDictionaryData(String pressedRadioButton) {
		switch (pressedRadioButton) {
		case Constants.RADIO_BUTTON_AUTHOR:
			Author author = (Author) tabbedForm.getPanelAdmDictionary().getComboBoxAuthor().getSelectedItem();
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorName().setText(author.getName());
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorNickname().setText(author.getNickname());
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorSurname().setText(author.getSurname());
			tabbedForm.getPanelAdmDictionary().getDateChooserBirthDate().setDate(author.getBirthDate());
			break;
		case Constants.RADIO_BUTTON_CATEGORY:
			Category category = (Category) tabbedForm.getPanelAdmDictionary().getComboBoxCategory().getSelectedItem();
			tabbedForm.getPanelAdmDictionary().getTextFieldCategoryName().setText(category.getName());
			break;
		case Constants.RADIO_BUTTON_PUBLISHING_HOUSE:
			PublishingHouse publishingHouse = (PublishingHouse) tabbedForm.getPanelAdmDictionary()
					.getComboBoxPublishingHouse().getSelectedItem();
			tabbedForm.getPanelAdmDictionary().getTextFieldPublishingHouseName().setText(publishingHouse.getName());
			break;
		}
	}

	private void switchRadioButtonsEnable(String pressedRadioButton) {
		switch (pressedRadioButton) {
		case Constants.RADIO_BUTTON_AUTHOR:
			pressedRadioButton = Constants.RADIO_BUTTON_AUTHOR;
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorName().setEnabled(true);
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorNickname().setEnabled(true);
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorSurname().setEnabled(true);
			tabbedForm.getPanelAdmDictionary().getDateChooserBirthDate().setEnabled(true);
			tabbedForm.getPanelAdmDictionary().getTextFieldCategoryName().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldPublishingHouseName().setEnabled(false);
			break;
		case Constants.RADIO_BUTTON_CATEGORY:
			pressedRadioButton = Constants.RADIO_BUTTON_CATEGORY;
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorName().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorNickname().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorSurname().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getDateChooserBirthDate().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldCategoryName().setEnabled(true);
			tabbedForm.getPanelAdmDictionary().getTextFieldPublishingHouseName().setEnabled(false);
			break;
		case Constants.RADIO_BUTTON_PUBLISHING_HOUSE:
			pressedRadioButton = Constants.RADIO_BUTTON_PUBLISHING_HOUSE;
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorName().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorNickname().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldAuthorSurname().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getDateChooserBirthDate().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldCategoryName().setEnabled(false);
			tabbedForm.getPanelAdmDictionary().getTextFieldPublishingHouseName().setEnabled(true);
			break;
		}
	}
}
