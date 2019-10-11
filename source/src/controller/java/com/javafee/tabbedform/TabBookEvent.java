package com.javafee.tabbedform;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.javafee.common.Constants;
import com.javafee.common.IActionForm;
import com.javafee.common.Params;
import com.javafee.exception.LogGuiException;
import com.javafee.exception.RefusedBookEventLoadingException;
import com.javafee.hibernate.dto.library.Author;
import com.javafee.hibernate.dto.library.Book;
import com.javafee.hibernate.dto.library.Category;
import com.javafee.hibernate.dto.library.PublishingHouse;
import com.javafee.model.BookTableModel;

public class TabBookEvent implements IActionForm {
	private TabbedForm tabbedForm;

	@SuppressWarnings("unused")
	private static TabBookEvent bookEvent = null;
	private BookAddModEvent bookAddModEvent;

	private List<Author> authorList = new ArrayList<Author>();
	private List<Category> categoryList = new ArrayList<Category>();
	private List<PublishingHouse> publishingHouseList = new ArrayList<PublishingHouse>();

	public TabBookEvent(TabbedForm tabbedForm) {
		this.control(tabbedForm);
	}

	public static TabBookEvent getInstance(TabbedForm tabbedForm) {
		if (bookEvent == null) {
			bookEvent = new TabBookEvent(tabbedForm);
		} else
			new RefusedBookEventLoadingException("Cannot book event loading");
		return bookEvent;
	}

	public void control(TabbedForm tabbedForm) {
		setTabbedForm(tabbedForm);
		initializeForm();

		tabbedForm.getPanelBook().getCockpitEditionPanelBook().getBtnAdd().addActionListener(e -> onClickBtnAdd());
		tabbedForm.getPanelBook().getCockpitEditionPanelBook().getBtnModify()
				.addActionListener(e -> onClickBtnModify());
		tabbedForm.getPanelBook().getCockpitEditionPanelBook().getBtnDelete()
				.addActionListener(e -> onClickBtnDelete());
	}

	private void onClickBtnAdd() {
		if (bookAddModEvent == null)
			bookAddModEvent = new BookAddModEvent();
		bookAddModEvent.control(Constants.Context.ADDITION,
				(BookTableModel) tabbedForm.getPanelBook().getBookTable().getModel());
	}

	private void onClickBtnModify() {
		if (tabbedForm.getPanelBook().getBookTable().getSelectedRow() != -1) {
			int selectedRowIndex = tabbedForm.getPanelBook().getBookTable()
					.convertRowIndexToModel(tabbedForm.getPanelBook().getBookTable().getSelectedRow());
			if (selectedRowIndex != -1) {
				Book selectedBook = ((BookTableModel) tabbedForm.getPanelBook().getBookTable().getModel())
						.getBook(selectedRowIndex);
				Book bookShallowClone = (Book) selectedBook.clone();

				Params.getInstance().add("selectedBookShallowClone", bookShallowClone);
				Params.getInstance().add("selectedRowIndex", selectedRowIndex);

				if (bookAddModEvent == null)
					bookAddModEvent = new BookAddModEvent();
				bookAddModEvent.control(Constants.Context.MODIFICATION,
						(BookTableModel) tabbedForm.getPanelBook().getBookTable().getModel());

			}
		} else {
			LogGuiException.logWarning("Book not selected",
					"Book has not been selected. The action can not been performed.");
		}
	}

	private void onClickBtnDelete() {

	}

	@Override
	public void initializeForm() {
	}

	public void setTabbedForm(TabbedForm tabbedForm) {
		this.tabbedForm = tabbedForm;
	}

	private String prepareSelectQueryFilter(Author author, Category category, PublishingHouse publishingHouse) {
		StringBuilder selectQueryFilter = new StringBuilder().append("from Book b where ");
		String prefix = "";

		if (author != null) {
			selectQueryFilter.append(":author in elements(b.author)");
			prefix = " and ";
		}

		if (category != null) {
			selectQueryFilter.append(prefix).append(":category in elements(b.category)");
			prefix = " and ";
		}

		if (publishingHouse != null)
			selectQueryFilter.append(prefix).append(":publishingHouse in elements(b.publishingHouse)");

		return selectQueryFilter.toString();
	}

	private void setParametersSelectQueryFilter(Author author, Category category, PublishingHouse publishingHouse,
			Query query) {
		if (author != null)
			query.setParameter("author", author);
		if (category != null)
			query.setParameter("category", category);
		if (publishingHouse != null)
			query.setParameter("publishingHouse", publishingHouse);
	}
}
