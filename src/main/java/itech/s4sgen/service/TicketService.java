package itech.s4sgen.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import itech.s4sgen.dao.ReservationDao;
import itech.s4sgen.dao.TicketDao;
import itech.s4sgen.dao.TicketPaymentDao;
import itech.s4sgen.dto.TicketDto;
import itech.s4sgen.models.Reservation;
import itech.s4sgen.models.Ticket;
import itech.s4sgen.models.TicketPayment;
import itech.s4sgen.models.User;

@Service
public class TicketService {

	@Autowired
	private TicketDao ticketDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private TicketPaymentDao ticketPaymentDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void saveTicket(TicketDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		Ticket ticket;
		if(dto.getId()==0)
			ticket = new Ticket();
		else
			ticket =  ticketDao.findOne(dto.getId());
		ticket.setTicketId(dto.getTicketId());
		ticket.setBusNo(dto.getBusNo());
		ticket.setDepartureTime(dto.getDepartureTime());
		ticket.setArrivalTime(dto.getArrivalTime());
		ticket.setDestination(dto.getDestination());
		ticket.setUser(user);
		ticketDao.save(ticket);
	}
	
	public void saveReservation(TicketDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		Reservation res;
		if(dto.getId()==0)
			res = new Reservation();
		else
			res = reservationDao.findOne(dto.getId());
		res.setTicketId(dto.getTicketId());
		res.setPassengerName(dto.getPassengerName());
		res.setPassengerMobile(dto.getPassengerMobile());
		res.setTicketPrice(dto.getTicketPrice());
		res.setUser(user);
		reservationDao.save(res);
	}
	
	public void saveTicketPayment(TicketDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		TicketPayment tp;
		if(dto.getId()==0)
			tp = new TicketPayment();
		else
			tp = ticketPaymentDao.findOne(dto.getId());
		tp.setTicketId(dto.getTicketId());
		tp.setAmountReceived(dto.getAmountReceived());
		tp.setAmountDeducted(dto.getAmountDeducted());
		tp.setUser(user);
		ticketPaymentDao.save(tp);
	}
	
	public TicketDto getTicketById(long id) {
		return convertToDtoFromTicket(ticketDao.findOne(id));
	}
	
	public TicketDto getReservationById(long id) {
		return convertToDtoFromReservation(reservationDao.findOne(id));
	}
	
	public TicketDto getTicketPaymentById(long id) {
		return convertToDtoFromTicketPayment(ticketPaymentDao.findOne(id));
	}
	
	public List<TicketDto> getUserTicketsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return ticketDao.findAllByUser(user).stream().map(TicketService::convertToDtoFromTicket).collect(Collectors.toList());
	}
	
	public List<TicketDto> getUserReservationsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return reservationDao.findAllByUser(user).stream().map(TicketService::convertToDtoFromReservation).collect(Collectors.toList());
	}
	
	public List<TicketDto> getUserTicketPaymentsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return ticketPaymentDao.findAllByUser(user).stream().map(TicketService::convertToDtoFromTicketPayment).collect(Collectors.toList());
	}
	
	public TicketDto getUserTicketsDataByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		TicketDto dto = new TicketDto();
		List<TicketPayment> payments = ticketPaymentDao.findAllByUser(user);
		dto.setTicketId(ticketDao.findAllByUser(user).size()+"");
		dto.setDestination(reservationDao.findAllByUser(user).size()+"");
		dto.setId(payments.size());
		dto.setTicketPrice(payments.stream().mapToDouble(p->p.getAmountReceived()).sum());
		return dto;
	}
	
	public File generateReport(String userName) throws FileNotFoundException, DocumentException {
		User user = userService.getUserByLogin(userName);
		List<Ticket> tickets = ticketDao.findAllByUser(user);
		List<Reservation> res = reservationDao.findAllByUser(user);
		List<TicketPayment> tps = ticketPaymentDao.findAllByUser(user);
		File pdf = new File("C:/temp/"+userName+".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(userName + " Tickets Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(userName.toUpperCase() + " Tickets Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Total Tickets: " + tickets.size()));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Total Bookings: " + res.size()));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Total Ticket Payments: " + tps.size()));
		doc.add(new Paragraph("\n"));
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static TicketDto convertToDtoFromTicket(Ticket ticket) {
		TicketDto dto = new TicketDto();
		dto.setId(ticket.getId());
		dto.setTicketId(ticket.getTicketId());
		dto.setBusNo(ticket.getBusNo());
		dto.setDepartureTime(ticket.getDepartureTime());
		dto.setArrivalTime(ticket.getArrivalTime());
		dto.setDestination(ticket.getDestination());
		return dto;
	}
	
	public static TicketDto convertToDtoFromReservation(Reservation res) {
		TicketDto dto = new TicketDto();
		dto.setId(res.getId());
		dto.setTicketId(res.getTicketId());
		dto.setPassengerName(res.getPassengerName());
		dto.setPassengerMobile(res.getPassengerMobile());
		dto.setTicketPrice(res.getTicketPrice());
		return dto;
	}
	
	public static TicketDto convertToDtoFromTicketPayment(TicketPayment payment) {
		TicketDto dto =  new TicketDto();
		dto.setId(payment.getId());
		dto.setTicketId(payment.getTicketId());
		dto.setAmountReceived(payment.getAmountReceived());
		dto.setAmountDeducted(payment.getAmountDeducted());
		return dto;
	}

}
