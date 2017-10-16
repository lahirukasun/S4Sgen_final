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

import itech.s4sgen.dto.JourneyDto;
import itech.s4sgen.dto.TicketDto;
import itech.s4sgen.service.BookingService;

@Controller
public class BookingSystemController {

	@Autowired
	private BookingService bookingService;
	
	@RequestMapping(value= {"/add_new_journey_in_system/{id}",
			"/available_journeys_in_system/{id}",
			"/add_manage_journey_booking/{id}",
			"/add_journey_payment_in_system/{id}",
			"/generate_journeys_report/{id}"},method=RequestMethod.GET)
	public String addManageBookings(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = bookingService.verifyUserAccess(userName, id);
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("journey",new JourneyDto());
				return "bookingsystem/addjourney";
			}
			case 2:{
				model.addAttribute("journeys",bookingService.getUserJourneysByUserName(userName));
				return "bookingsystem/availablejourneys";
			}
			case 3:{
				model.addAttribute("ticket",new TicketDto());
				model.addAttribute("tickets",bookingService.getUserReservationsByUserName(userName));
				return "bookingsystem/managereservation";
			}
			case 4:{
				model.addAttribute("ticket",new TicketDto());
				model.addAttribute("payments",bookingService.getUserTicketPaymentsByUserName(userName));
				return "bookingsystem/makepayment";
			}
			case 5:{
				model.addAttribute("data",bookingService.getUserTicketsDataByUserName(userName));
				return "booking/generatereport";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_new_journey",method=RequestMethod.POST)
	public String addNewTicket(@ModelAttribute JourneyDto journey, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		bookingService.saveJourney(journey, userName);
		model.addAttribute("journey", new JourneyDto());
		return "bookingsystem/addjourney";
	}
	
	@RequestMapping(value="/add_journey_reservation",method=RequestMethod.POST)
	public String addTicketReservation(@ModelAttribute TicketDto ticket, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		bookingService.saveReservation(ticket, userName);
		model.addAttribute("ticket",new TicketDto());
		model.addAttribute("tickets",bookingService.getUserReservationsByUserName(userName));
		return "ticketsystem/managereservation";
	}
	
	@RequestMapping(value="/add_journey_payment",method=RequestMethod.POST)
	public String addTicketPayment(@ModelAttribute TicketDto ticket, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		bookingService.saveTicketPayment(ticket, userName);
		model.addAttribute("ticket",new TicketDto());
		model.addAttribute("payments",bookingService.getUserReservationsByUserName(userName));
		return "ticketsystem/makepayment";
	}
	
	@RequestMapping(value="/edit_added_journey/{id}",method=RequestMethod.GET)
	public String editExistingCheckup(@PathVariable("id")long id, Model model) {
		model.addAttribute("journey", bookingService.getJourneyById(id));
		return "bookingsystem/addjourney";
	}
	
	@RequestMapping(value="/edit_journey_reservation/{id}",method=RequestMethod.GET)
	public String editTicketReservation(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("ticket", bookingService.getReservationById(id));
		model.addAttribute("tickets",bookingService.getUserReservationsByUserName(userName));
		return "ticketsystem/managereservation";
	}
	
	@RequestMapping(value="/edit_journey_payment/{id}",method=RequestMethod.GET)
	public String editTicketPayment(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("ticket",bookingService.getTicketPaymentById(id));
		model.addAttribute("payments",bookingService.getUserReservationsByUserName(userName));
		return "ticketsystem/makepayment";
	}
	
	@RequestMapping(value="/download_journeys_report",method = RequestMethod.GET)
	public void generateStudentReport(HttpServletResponse response) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		File report = null;
		try {
			report = bookingService.generateReport(userName);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
}
