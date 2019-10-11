package com.javafee.tabbedform;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.javafee.common.Constants;
import com.javafee.common.Constants.Context;
import com.javafee.common.IActionForm;
import com.javafee.common.Params;
import com.javafee.common.SystemProperties;
import com.javafee.exception.LogGuiException;
import com.javafee.exception.RefusedLibraryEventLoadingException;
import com.javafee.hibernate.dto.library.Author;
import com.javafee.hibernate.dto.library.Category;
import com.javafee.hibernate.dto.library.PublishingHouse;
import com.javafee.hibernate.dto.library.Volume;
import com.javafee.model.VolumeTableLoanModel;
import com.javafee.model.VolumeTableModel;
import com.javafee.model.VolumeTableReadingRoomModel;

public class TabLibraryEvent implements IActionForm {
	private TabbedForm tabbedForm;

	@SuppressWarnings("unused")
	private static TabLibraryEvent libraryEvent = null;
	private LibraryAddModEvent libraryAddModEvent;

	private List<Author> authorList = new ArrayList<Author>();
	private List<Category> categoryList = new ArrayList<Category>();
	private List<PublishingHouse> publishingHouseList = new ArrayList<PublishingHouse>();

	public TabLibraryEvent(TabbedForm tabbedForm) {
		this.control(tabbedForm);
	}

	public static TabLibraryEvent getInstance(TabbedForm tabbedForm) {
		if (libraryEvent == null) {
			libraryEvent = new TabLibraryEvent(tabbedForm);
		} else
			new RefusedLibraryEventLoadingException("Cannot book event loading");
		return libraryEvent;
	}

	public void control(TabbedForm tabbedForm) {
		setTabbedForm(tabbedForm);
		initializeForm();

		tabbedForm.getPanelLibrary().getCockpitEditionPanelLoan().getBtnAdd()
				.addActionListener(e -> onClickBtnAddVolumeLoan());
		tabbedForm.getPanelLibrary().getCockpitEditionPanelLoan().getBtnModify()
				.addActionListener(e -> onClickBtnModifyVolumeLoan());
		tabbedForm.getPanelLibrary().getCockpitEditionPanelReadingRoom().getBtnAdd()
				.addActionListener(e -> onClickBtnAddVolumeReadingRoom());
		tabbedForm.getPanelLibrary().getCockpitEditionPanelReadingRoom().getBtnModify()
				.addActionListener(e -> onClickBtnModifyVolumeReadingRoom());
	}

	private void onClickBtnModifyVolumeReadingRoom() {
		if (tabbedForm.getPanelLibrary().getReadingRoomBookTable().getSelectedRow() != -1) {
			int selectedRowIndex = tabbedForm.getPanelLibrary().getReadingRoomBookTable()
					.convertRowIndexToModel(tabbedForm.getPanelLibrary().getReadingRoomBookTable().getSelectedRow());
			if (selectedRowIndex != -1) {
				Volume selectedVolume = ((VolumeTableReadingRoomModel) tabbedForm.getPanelLibrary()
						.getReadingRoomBookTable().getModel()).getVolume(selectedRowIndex);
				Volume volumeShallowClone = (Volume) selectedVolume.clone();

				Params.getInstance().add("selectedVolumeShallowClone", volumeShallowClone);
				Params.getInstance().add("selectedRowIndex", selectedRowIndex);

				if (libraryAddModEvent == null)
					libraryAddModEvent = new LibraryAddModEvent();
				libraryAddModEvent.control(Constants.Context.MODIFICATION, Context.READING_ROOM,
						(VolumeTableModel) tabbedForm.getPanelLibrary().getReadingRoomBookTable().getModel());

			}
		} else {
			LogGuiException.logWarning(
					SystemProperties.getInstance().getResourceBundle()
							.getString("libraryAddModEvent.notSelectedVolumeWarningTitle"),
					SystemProperties.getInstance().getResourceBundle()
							.getString("libraryAddModEvent.notSelectedVolumeWarning"));
		}

	}

	private void onClickBtnModifyVolumeLoan() {
		if (tabbedForm.getPanelLibrary().getLoanVolumeTable().getSelectedRow() != -1) {
			int selectedRowIndex = tabbedForm.getPanelLibrary().getLoanVolumeTable()
					.convertRowIndexToModel(tabbedForm.getPanelLibrary().getLoanVolumeTable().getSelectedRow());
			if (selectedRowIndex != -1) {
				Volume selectedVolume = ((VolumeTableLoanModel) tabbedForm.getPanelLibrary().getLoanVolumeTable()
						.getModel()).getVolume(selectedRowIndex);
				Volume volumeShallowClone = (Volume) selectedVolume.clone();

				Params.getInstance().add("selectedVolumeShallowClone", volumeShallowClone);
				Params.getInstance().add("selectedRowIndex", selectedRowIndex);

				if (libraryAddModEvent == null)
					libraryAddModEvent = new LibraryAddModEvent();
				libraryAddModEvent.control(Constants.Context.MODIFICATION, Context.LOAN,
						(VolumeTableModel) tabbedForm.getPanelLibrary().getLoanVolumeTable().getModel());

			}
		} else {
			LogGuiException.logWarning(
					SystemProperties.getInstance().getResourceBundle()
							.getString("libraryAddModEvent.notSelectedVolumeWarningTitle"),
					SystemProperties.getInstance().getResourceBundle()
							.getString("libraryAddModEvent.notSelectedVolumeWarning"));
		}
	}

	private void onClickBtnAddVolumeLoan() {
		if (libraryAddModEvent == null)
			libraryAddModEvent = new LibraryAddModEvent();
		libraryAddModEvent.control(Constants.Context.ADDITION, Constants.Context.LOAN,
				(VolumeTableModel) tabbedForm.getPanelLibrary().getLoanVolumeTable().getModel());
	}

	private void onClickBtnAddVolumeReadingRoom() {
		if (libraryAddModEvent == null)
			libraryAddModEvent = new LibraryAddModEvent();
		libraryAddModEvent.control(Constants.Context.ADDITION, Constants.Context.READING_ROOM,
				(VolumeTableReadingRoomModel) tabbedForm.getPanelLibrary().getReadingRoomBookTable().getModel());

	}

	private void onClickBtnDelete() {

	}

	@Override
	public void initializeForm() {
		// reloadBookFilterPanel();
		// switchPerspectiveToAdm(LogInEvent.getRole() == Role.ADMIN ||
		// LogInEvent.getRole() == Role.WORKER_ACCOUNTANT);
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
