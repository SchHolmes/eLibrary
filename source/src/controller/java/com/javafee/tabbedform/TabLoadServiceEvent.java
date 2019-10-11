package com.javafee.tabbedform;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.RollbackException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import com.javafee.common.Constants;
import com.javafee.common.Constants.Role;
import com.javafee.common.IActionForm;
import com.javafee.common.SystemProperties;
import com.javafee.exception.RefusedLoanServiceEventLoadingException;
import com.javafee.hibernate.dao.HibernateDao;
import com.javafee.hibernate.dao.HibernateUtil;
import com.javafee.hibernate.dto.library.Author;
import com.javafee.hibernate.dto.library.Book;
import com.javafee.hibernate.dto.library.Category;
import com.javafee.hibernate.dto.library.Client;
import com.javafee.hibernate.dto.library.Lend;
import com.javafee.hibernate.dto.library.PublishingHouse;
import com.javafee.hibernate.dto.library.Volume;
import com.javafee.loginform.LogInEvent;
import com.javafee.model.LoanTableModel;
import com.javafee.model.VolumeTableModel;

public class TabLoadServiceEvent implements IActionForm {
	@SuppressWarnings("unused")
	private TabbedForm tabbedForm;

	@SuppressWarnings("unused")
	private Set<Volume> lendBook = new HashSet<Volume>();

	@SuppressWarnings("unused")
	private static TabLoadServiceEvent loadServiceEvent = null;

	public TabLoadServiceEvent(TabbedForm tabbedForm) {
		this.control(tabbedForm);
	}

	public static TabLoadServiceEvent getInstance(TabbedForm tabbedForm) {
		if (loadServiceEvent == null) {
			loadServiceEvent = new TabLoadServiceEvent(tabbedForm);
		} else
			new RefusedLoanServiceEventLoadingException("Cannot loan service event loading");
		return loadServiceEvent;
	}

	public void control(TabbedForm tabbedForm) {
		setTabbedForm(tabbedForm);
		initializeForm();

		tabbedForm.getPanelLoanService().getLoanDataPanel().getComboBoxBook()
				.addActionListener(e -> reloadTextAreaDetails());
		tabbedForm.getPanelLoanService().getLoanDataPanel().getCockpitEditionPanel().getBtnAdd()
				.addActionListener(e -> onClickBtnAdd());
		tabbedForm.getPanelLoanService().getBtnLoan().addActionListener(e -> onClickBtnLoan());
		tabbedForm.getPanelLoanService().getBtnProlongation().addActionListener(e -> onClickBtnProlongation());
		tabbedForm.getPanelLoanService().getBtnReturn().addActionListener(e -> onClickBtnReturn());
	}

	@Override
	public void initializeForm() {
		reloadBookFilterPanel();
		reloaLoadServicePanel();
		setVisibleControls();
	}

	public void setTabbedForm(TabbedForm tabbedForm) {
		this.tabbedForm = tabbedForm;
	}

	private void reloadBookFilterPanel() {
		reloadComboBoxAuthor();
		reloadComboBoxCategory();
		reloadComboBoxPublishingHouse();
	}

	private void reloaLoadServicePanel() {
		reloadComboBoxBook();
		reloadComboBoxClient();
	}

	private void reloadTextAreaDetails() {
		Book selectedBook = (Book) tabbedForm.getPanelLoanService().getLoanDataPanel().getComboBoxBook()
				.getSelectedItem();

		StringBuilder author = new StringBuilder();
		StringBuilder category = new StringBuilder();
		StringBuilder publishingHouse = new StringBuilder();
		selectedBook.getAuthor().forEach(e -> author.append(
				e.getSurname() != null ? e.getSurname() : "-" + " " + e.getSurname() != null ? e.getName() : "-")
				.append("\n"));
		selectedBook.getCategory().forEach(e -> category.append(e.getName() != null ? e.getName() : "-").append("\n"));
		selectedBook.getPublishingHouse()
				.forEach(e -> publishingHouse.append(e.getName() != null ? e.getName() : "-").append("\n"));

		tabbedForm.getPanelLoanService().getBookFilterPanel().getTextAreaDetails()
				.setText("Authors in cloud" + "\n"
						+ author.toString() + "\n"
						+ "Category(ies):" + "\n"
						+ category.toString() + "\n"
						+ "Publishing house(s):"
						+ "\n" + publishingHouse.toString());
	}

	private void reloadComboBoxAuthor() {
		DefaultComboBoxModel<Author> comboBoxAuthor = new DefaultComboBoxModel<Author>();
		HibernateDao<Author, Integer> author = new HibernateDao<Author, Integer>(Author.class);
		List<Author> authorListToSort = author.findAll();
		authorListToSort
				.sort(Comparator.comparing(Author::getSurname, Comparator.nullsFirst(Comparator.naturalOrder())));
		authorListToSort.forEach(c -> comboBoxAuthor.addElement(c));
		comboBoxAuthor.insertElementAt(null, 0);

		tabbedForm.getPanelLoanService().getBookFilterPanel().getComboBoxAuthor().setModel(comboBoxAuthor);
	}

	private void reloadComboBoxCategory() {
		DefaultComboBoxModel<Category> comboBoxCategory = new DefaultComboBoxModel<Category>();
		HibernateDao<Category, Integer> category = new HibernateDao<Category, Integer>(Category.class);
		List<Category> categoryListToSort = category.findAll();
		categoryListToSort
				.sort(Comparator.comparing(Category::getName, Comparator.nullsFirst(Comparator.naturalOrder())));
		categoryListToSort.forEach(c -> comboBoxCategory.addElement(c));
		comboBoxCategory.insertElementAt(null, 0);

		tabbedForm.getPanelLoanService().getBookFilterPanel().getComboBoxCategory().setModel(comboBoxCategory);
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

		tabbedForm.getPanelLoanService().getBookFilterPanel().getComboBoxPublishingHouse()
				.setModel(comboBoxPublishingHouse);
	}

	private void reloadComboBoxBook() {
		DefaultComboBoxModel<Book> comboBoxBook = new DefaultComboBoxModel<Book>();
		HibernateDao<Book, Integer> book = new HibernateDao<Book, Integer>(Book.class);
		List<Book> bookListToSort = book.findAll();
		bookListToSort.sort(Comparator.comparing(Book::getTitle, Comparator.nullsFirst(Comparator.naturalOrder())));
		bookListToSort.forEach(c -> comboBoxBook.addElement(c));
		comboBoxBook.insertElementAt(null, 0);

		tabbedForm.getPanelLoanService().getLoanDataPanel().getComboBoxBook().setModel(comboBoxBook);
	}

	private void reloadComboBoxClient() {
		DefaultComboBoxModel<Client> comboBoxClient = new DefaultComboBoxModel<Client>();
		HibernateDao<Client, Integer> client = new HibernateDao<Client, Integer>(Client.class);
		List<Client> clientListToSort = client.findAll();
		List<Client> filteredActiveClientList = clientListToSort.stream().filter(u -> u.getRegistered() == true)
				.collect(Collectors.toList());
		filteredActiveClientList
				.sort(Comparator.comparing(Client::getSurname, Comparator.nullsFirst(Comparator.naturalOrder())));
		filteredActiveClientList.forEach(c -> comboBoxClient.addElement(c));
		comboBoxClient.insertElementAt(null, 0);

		tabbedForm.getPanelLoanService().getLoanDataPanel().getComboBoxClient().setModel(comboBoxClient);
	}

	private void onClickBtnAdd() {
		if (validateIfInventoryNumberNotExist(
				tabbedForm.getPanelLoanService().getLoanDataPanel().getTextFieldInventoryNumber().getText())) {
			try {
				HibernateUtil.beginTransaction();
				Volume volume = new Volume();
				volume.setBook(
						(Book) tabbedForm.getPanelLoanService().getLoanDataPanel().getComboBoxBook().getSelectedItem());
				volume.setInventoryNumber(
						tabbedForm.getPanelLoanService().getLoanDataPanel().getTextFieldInventoryNumber().getText());
				volume.setIsReadingRoom(
						tabbedForm.getPanelLoanService().getLoanDataPanel().getChckbxIsReadingRoom().isSelected() ? true
								: false);
				volume.setIsLended(false);
				HibernateUtil.getSession().save(volume);
				HibernateUtil.commitTransaction();

				VolumeTableModel vtm = (VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel()
						.getVolumeTable().getModel();
				vtm.add(volume);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (RollbackException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(tabbedForm.getFrame(), "Istnieje numer inwentarzowy.", "Numer",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onClickBtnLoan() {
		try {

			if (HibernateUtil.getSession()
					.createQuery("select idLend from Lend l where l.volume.inventoryNumber = :inventoryNumber")
					.setParameter("inventoryNumber",
							((VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable()
									.getModel()).getVolume(
											tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable()
													.convertRowIndexToModel(tabbedForm.getPanelLoanService()
															.getVolumeTablePanel().getVolumeTable().getSelectedRow()))
											.getInventoryNumber())
					.list().isEmpty() &&

					HibernateUtil.getSession().createQuery(
							"select idLend from Lend l where l.client.idUserData = :idUserData and l.volume.penaltyValue != 0")
							.setParameter("idUserData", ((Client) tabbedForm.getPanelLoanService().getLoanDataPanel()
									.getComboBoxClient().getSelectedItem()).getIdUserData())
							.list().isEmpty()

			) {
				HibernateUtil.beginTransaction();
				Lend loan = new Lend();
				Date lendDate = Constants.APPLICATION_DATE_FORMAT
						.parse(Constants.APPLICATION_DATE_FORMAT.format(new Date()));

				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 1);
				loan.setClient((Client) tabbedForm.getPanelLoanService().getLoanDataPanel().getComboBoxClient()
						.getSelectedItem());
				loan.setVolume(((VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel()
						.getVolumeTable().getModel())
								.getVolume(tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable()
										.convertRowIndexToModel(tabbedForm.getPanelLoanService().getVolumeTablePanel()
												.getVolumeTable().getSelectedRow())));
				loan.setLendDate(lendDate);
				loan.setReturnedDate(cal.getTime());
				loan.getVolume().setIsLended(true);
				HibernateUtil.getSession().save(loan);
				HibernateUtil.commitTransaction();

				LoanTableModel vtm = (LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel()
						.getLoanTable().getModel();
				vtm.add(loan);

				((VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable().getModel())
						.remove(loan.getVolume());
				((VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable().getModel())
						.fireTableDataChanged();
			} else {
				JOptionPane.showMessageDialog(tabbedForm.getFrame(),
						"Egzemplarz zosta³ wypo¿yczony lub klient ma zarejestrowan¹ karê w systemie.",
						"loanServicePanel.penaltyErrorTitle", JOptionPane.ERROR_MESSAGE);

			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (RollbackException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void onClickBtnReturn() {
		Lend currentLoan = ((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable()
				.getModel()).getLend(
						tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().convertRowIndexToModel(
								tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getSelectedRow()));
		currentLoan.getVolume().setIsLended(false);
		if (calculatePenalty() != new BigDecimal(0).doubleValue()) {
			JOptionPane.showMessageDialog(tabbedForm.getFrame(),
					"User have to pay penalty because of untimely book returning. Penalty is:" + " "
							+ calculatePenalty() + Constants.APPLICATION_CURRENCY,
					"Penalty",
					JOptionPane.ERROR_MESSAGE);
		}

		try {
			HibernateUtil.beginTransaction();

			((VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable().getModel())
					.add(currentLoan.getVolume());
			((VolumeTableModel) tabbedForm.getPanelLoanService().getVolumeTablePanel().getVolumeTable().getModel())
					.fireTableDataChanged();

			HibernateUtil.getSession().delete(Lend.class.getName(),
					((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getModel())
							.getLend(tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable()
									.convertRowIndexToModel(tabbedForm.getPanelLoanService().getLoanTablePanel()
											.getLoanTable().getSelectedRow())));
			((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getModel())
					.delete(currentLoan);

			HibernateUtil.commitTransaction();
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("deprecation")
	private void onClickBtnProlongation() {
		if (tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().convertRowIndexToModel(
				tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getSelectedRow()) != -1) {
			Lend lend = ((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable()
					.getModel())
							.getLend(tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable()
									.convertRowIndexToModel(tabbedForm.getPanelLoanService().getLoanTablePanel()
											.getLoanTable().getSelectedRow()));

			Date lendData = lend.getLendDate();
			Date returnData = lend.getReturnedDate();

			Calendar cal = Calendar.getInstance();
			cal.setTime(lendData);

			Date deadLine = new Date();
			deadLine.setMonth(lendData.getMonth());
			deadLine.setYear(lendData.getYear());
			deadLine.setDate(lendData.getDate());
			if (lendData.getMonth() == 9) {
				deadLine.setMonth(0);
				deadLine.setYear(deadLine.getYear() + 1);
			} else if (lendData.getMonth() == 10) {
				deadLine.setMonth(1);
				deadLine.setYear(deadLine.getYear() + 1);
			} else if (lendData.getMonth() == 11) {
				deadLine.setMonth(2);
				deadLine.setYear(deadLine.getYear() + 1);
			} else
				deadLine.setMonth(deadLine.getMonth() + 3);

			Boolean error = false;

			if (returnData.getMonth() == 11) {
				returnData.setMonth(0);
				returnData.setYear(returnData.getYear() + 1);
				if (returnData.after(deadLine))
					error = true;
			} else {
				returnData.setMonth(returnData.getMonth() + 1);
				if (returnData.after(deadLine))
					error = true;
			}

			if (!error) {
				lend.setReturnedDate(returnData);

				HibernateUtil.beginTransaction();
				HibernateUtil.getSession().update(Lend.class.getName(), lend);
				HibernateUtil.commitTransaction();

				((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getModel())
						.setLend(tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getSelectedRow(),
								lend);
				((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getModel())
						.fireTableDataChanged();
			} else {
				JOptionPane.showMessageDialog(tabbedForm.getFrame(),
						"Loan limit overstep error. Prolongation can not be made because of overstepping.",
						"Prolongation limit",
						JOptionPane.ERROR_MESSAGE);
			}

		} else {
			JOptionPane.showMessageDialog(tabbedForm.getFrame(),
					"Loan does not have been selected. The action can not been performed.",
					"Not enough data",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private double calculatePenalty() {
		Date returnDate = ((LoanTableModel) tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable()
				.getModel()).getLend(
						tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().convertRowIndexToModel(
								tabbedForm.getPanelLoanService().getLoanTablePanel().getLoanTable().getSelectedRow()))
						.getReturnedDate();

		int diffMonth = 0;

		if (new Date().after(returnDate)) {
			Calendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(returnDate);
			Calendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(new Date());

			int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
			diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		}

		return diffMonth * Constants.PENALTY_VALUE;
	}

	private void setVisibleControls() {
		tabbedForm.getPanelLoanService().getBookFilterPanel().getAuthorAddMultiPanel().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getCategoryAddMultiPanel().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getPublishingHouseAddMultiPanel().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getAdmBookDataPanel().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getComboBoxAuthor().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getComboBoxCategory().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getComboBoxPublishingHouse().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getLblAuthor().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getLblCategory().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getLblPublishingHouse().setVisible(false);
		tabbedForm.getPanelLoanService().getBookFilterPanel().getDecisionPanel().getBtnAccept().setVisible(false);

		if (com.javafee.loginform.LogInEvent.getRole() == Role.WORKER_ACCOUNTANT
				|| LogInEvent.getRole() == Role.ADMIN) {
			tabbedForm.getPanelLoanService().getLoanDataPanel().getTextFieldInventoryNumber().setVisible(true);
			tabbedForm.getPanelLoanService().getLoanDataPanel().getLblInventoryNumber().setVisible(true);
			tabbedForm.getPanelLoanService().getLoanDataPanel().getChckbxIsReadingRoom().setVisible(true);
			tabbedForm.getPanelLoanService().getLoanDataPanel().getCockpitEditionPanel().setVisible(true);
		} else {
			tabbedForm.getPanelLoanService().getLoanDataPanel().getTextFieldInventoryNumber().setVisible(false);
			tabbedForm.getPanelLoanService().getLoanDataPanel().getLblInventoryNumber().setVisible(false);
			tabbedForm.getPanelLoanService().getLoanDataPanel().getChckbxIsReadingRoom().setVisible(false);
			tabbedForm.getPanelLoanService().getLoanDataPanel().getCockpitEditionPanel().setVisible(false);
		}
	}

	private boolean validateIfInventoryNumberNotExist(String inventoryNumber) {
		boolean result = false;
		Volume volume = (Volume) HibernateUtil.getSession().getNamedQuery("Volume.checkIfInventoryNumberExist")
				.setParameter("inventoryNumber", inventoryNumber).uniqueResult();

		if (volume == null) {
			result = true;
		}

		return result;
	}
}
