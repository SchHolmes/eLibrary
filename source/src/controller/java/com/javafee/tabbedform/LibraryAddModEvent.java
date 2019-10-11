package com.javafee.tabbedform;

import com.javafee.common.Constants;
import com.javafee.common.Params;
import com.javafee.common.SystemProperties;
import com.javafee.exception.LogGuiException;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Book;
import com.javafee.hibernate.dto.library.Volume;
import com.javafee.model.BookTableModel;
import com.javafee.model.VolumeTableLoanModel;
import com.javafee.model.VolumeTableModel;
import com.javafee.model.VolumeTableReadingRoomModel;
import com.javafee.tabbedpane.library.frames.LibraryAddModFrame;

public class LibraryAddModEvent {

	private LibraryAddModFrame libraryAddModFrame;

	private VolumeTableLoanModel volumeTableLoanModel;
	private VolumeTableReadingRoomModel volumeTableReadingRoomModel;

	public void control(Constants.Context context, Constants.Context loanOrReadingRoom,
			VolumeTableModel volumeTableModel) {
		if (loanOrReadingRoom == Constants.Context.LOAN)
			volumeTableLoanModel = (VolumeTableLoanModel) volumeTableModel;
		else if (loanOrReadingRoom == Constants.Context.READING_ROOM)
			volumeTableReadingRoomModel = (VolumeTableReadingRoomModel) volumeTableModel;

		openLibraryAddModFrame(context);

		libraryAddModFrame.getCockpitConfirmationPanel().getBtnAccept()
				.addActionListener(e -> onClickBtnAccept(context, loanOrReadingRoom));
	}

	private void onClickBtnAccept(Constants.Context context, Constants.Context loanOrReadingRoom) {
		if (context == Constants.Context.ADDITION) {
			if (validateTablesSelection()) {
				createVolume(loanOrReadingRoom);
			} else {
				LogGuiException.logWarning(
						"Rows not selected",
						"Any rows has been selected. The action can not been performed.");
			}
		} else if (context == Constants.Context.MODIFICATION) {
			modificateVolume(loanOrReadingRoom);
		}
	}

	private void modificateVolume(Constants.Context loanOrReadingRoom) {
		if (libraryAddModFrame.getBookTable().getSelectedRow() != -1) {
			Volume volumeShallowClone = (Volume) Params.getInstance().get("selectedVolumeShallowClone");
			Params.getInstance().remove("selectedVolumeShallowClone");

			int selectedRowIndex = libraryAddModFrame.getBookTable()
					.convertRowIndexToModel(libraryAddModFrame.getBookTable().getSelectedRow());

			Book selectedBook = ((BookTableModel) libraryAddModFrame.getBookTable().getModel())
					.getBook(selectedRowIndex);
			volumeShallowClone.setInventoryNumber(libraryAddModFrame.getTextFieldInventoryNumber().getText());
			volumeShallowClone.setBook(selectedBook);

			HibernateUtil.beginTransaction();
			if (loanOrReadingRoom == Constants.Context.LOAN) {
				HibernateUtil.getSession()
						.evict(volumeTableLoanModel.getVolume((Integer) Params.getInstance().get("selectedRowIndex")));
				HibernateUtil.getSession().update(Volume.class.getName(), volumeShallowClone);
			} else if (loanOrReadingRoom == Constants.Context.READING_ROOM) {
				HibernateUtil.getSession().evict(
						volumeTableReadingRoomModel.getVolume((Integer) Params.getInstance().get("selectedRowIndex")));
				HibernateUtil.getSession().update(Volume.class.getName(), volumeShallowClone);
			}
			HibernateUtil.commitTransaction();

			if (loanOrReadingRoom == Constants.Context.LOAN) {
				volumeTableLoanModel.setVolume((Integer) Params.getInstance().get("selectedRowIndex"),
						volumeShallowClone);
				volumeTableLoanModel.fireTableDataChanged();
			} else if (loanOrReadingRoom == Constants.Context.READING_ROOM) {
				volumeTableReadingRoomModel.setVolume((Integer) Params.getInstance().get("selectedRowIndex"),
						volumeShallowClone);
				volumeTableReadingRoomModel.fireTableDataChanged();
			}
			Params.getInstance().remove("selectedRowIndex");

		} else {
			LogGuiException.logWarning(
					"Book not selected",
					"Book does not have been selected. The action can not been performed.");
		}
	}

	private void createVolume(Constants.Context loanOrReadingRoom) {
		if (libraryAddModFrame.getBookTable().getSelectedRow() != -1) {
			int selectedRowIndex = libraryAddModFrame.getBookTable()
					.convertRowIndexToModel(libraryAddModFrame.getBookTable().getSelectedRow());
			if (selectedRowIndex != -1) {
				Book selectedBook = ((BookTableModel) libraryAddModFrame.getBookTable().getModel())
						.getBook(selectedRowIndex);
				String inventoryNumber = libraryAddModFrame.getTextFieldInventoryNumber().getText();

				HibernateUtil.beginTransaction();
				Volume volume = new Volume();
				volume.setBook(selectedBook);
				volume.setInventoryNumber(inventoryNumber);
				if (loanOrReadingRoom == Constants.Context.READING_ROOM)
					volume.setIsReadingRoom(true);

				HibernateUtil.getSession().save(volume);
				HibernateUtil.commitTransaction();

				if (loanOrReadingRoom == Constants.Context.LOAN)
					volumeTableLoanModel.add(volume);
				else if (loanOrReadingRoom == Constants.Context.READING_ROOM)
					volumeTableReadingRoomModel.add(volume);
			}
		}
	}

	private void openLibraryAddModFrame(Constants.Context context) {
		if (libraryAddModFrame == null || (libraryAddModFrame != null && !libraryAddModFrame.isDisplayable())) {
			libraryAddModFrame = new LibraryAddModFrame();
			if (context == Constants.Context.MODIFICATION) {
				fillTextBoxInventoryNumber();
				reloadTable();
			}
			libraryAddModFrame.setVisible(true);
		} else {
			libraryAddModFrame.toFront();
		}
	}

	private void reloadTable() {
		Integer selectedBookIndex = ((BookTableModel) libraryAddModFrame.getBookTable().getModel()).getBooks()
				.indexOf(((Volume) Params.getInstance().get("selectedVolumeShallowClone")).getBook());
		libraryAddModFrame.getBookTable().setRowSelectionInterval(selectedBookIndex, selectedBookIndex);
	}

	private void fillTextBoxInventoryNumber() {
		libraryAddModFrame.getTextFieldInventoryNumber()
				.setText(((Volume) Params.getInstance().get("selectedVolumeShallowClone")).getInventoryNumber() != null
						? ((Volume) Params.getInstance().get("selectedVolumeShallowClone")).getInventoryNumber()
								.toString()
						: "");
	}

	private boolean validateTablesSelection() {
		boolean result = false;

		if (libraryAddModFrame.getBookTable().getSelectedRowCount() != 0) {
			result = true;
		}

		return result;
	}
}
