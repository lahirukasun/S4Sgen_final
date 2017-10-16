package itech.s4sgen.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import itech.s4sgen.dto.TicketDto;
import itech.s4sgen.service.TicketService;

@Controller
public class TicketSystemController {

	@Autowired
	private TicketService ticketService;
	
	@RequestMapping(value= {"/add_new_tickets_in_system/{id}",
			"/available_tickets_in_system/{id}",
			"/manage_booking_in_system/{id}",
			"/add_ticket_payment_in_system/{id}",
			"/generate_tickets_report/{id}"},method=RequestMethod.GET)
	public String addManageAppointments(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = ticketService.verifyUserAccess(userName, id);
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("ticket",new TicketDto());
				return "ticketsystem/addtickets";
			}
			case 2:{
				model.addAttribute("tickets",ticketService.getUserTicketsByUserName(userName));
				return "ticketsystem/availabletickets";
			}
			case 3:{
				model.addAttribute("ticket",new TicketDto());
				model.addAttribute("tickets",ticketService.getUserReservationsByUserName(userName));
				return "ticketsystem/managereservation";
			}
			case 4:{
				model.addAttribute("ticket",new TicketDto());
				model.addAttribute("payments",ticketService.getUserTicketPaymentsByUserName(userName));
				return "ticketsystem/makepayment";
			}
			case 5:{
				model.addAttribute("data",ticketService.getUserTicketsDataByUserName(userName));
				return "ticketsystem/generatereport";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_new_ticket",method=RequestMethod.POST)
	public String addNewTicket(@ModelAttribute TicketDto ticket, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		ticketService.saveTicket(ticket, userName);
		model.addAttribute("ticket", new TicketDto());
		return "ticketsystem/addtickets";
	}
	
	@RequestMapping(value="/add_ticket_reservation",method=RequestMethod.POST)
	public String addTicketReservation(@ModelAttribute TicketDto ticket, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		ticketService.saveReservation(ticket, userName);
		model.addAttribute("ticket",new TicketDto());
		model.addAttribute("tickets",ticketService.getUserReservationsByUserName(userName));
		return "ticketsystem/managereservation";
	}
	
	@RequestMapping(value="/add_ticket_payment",method=RequestMethod.POST)
	public String addTicketPayment(@ModelAttribute TicketDto ticket, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		ticketService.saveTicketPayment(ticket, userName);
		model.addAttribute("ticket",new TicketDto());
		model.addAttribute("payments",ticketService.getUserTicketPaymentsByUserName(userName));
		return "ticketsystem/makepayment";
	}
	
	@RequestMapping(value="/edit_added_ticket/{id}",method=RequestMethod.GET)
	public String editExistingCheckup(@PathVariable("id")long id, Model model) {
		model.addAttribute("ticket", ticketService.getTicketById(id));
		return "ticketsystem/addtickets";
	}
	
	@RequestMapping(value="/edit_ticket_reservation/{id}",method=RequestMethod.GET)
	public String editTicketReservation(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("ticket", ticketService.getReservationById(id));
		model.addAttribute("tickets",ticketService.getUserReservationsByUserName(userName));
		return "ticketsystem/managereservation";
	}
	
	@RequestMapping(value="/edit_ticket_payment/{id}",method=RequestMethod.GET)
	public String editTicketPayment(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("ticket",ticketService.getTicketPaymentById(id));
		model.addAttribute("payments",ticketService.getUserReservationsByUserName(userName));
		return "ticketsystem/makepayment";
	}
	
	@RequestMapping(value="/download_tickets_report",method = RequestMethod.GET)
	public void generateStudentReport(HttpServletResponse response) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		File report = null;
		try {
			report = ticketService.generateReport(userName);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
}
