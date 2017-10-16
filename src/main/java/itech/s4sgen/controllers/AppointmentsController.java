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
import org.springframework.web.bind.annotation.ResponseBody;

import itech.s4sgen.dto.AppointmentDto;
import itech.s4sgen.service.AppointmentService;

@Controller
public class AppointmentsController {

	@Autowired
	private AppointmentService appointmentService;
	
	@RequestMapping(value= {"/add_manage_appointments/{id}",
			"/add_manage_checkup_payment/{id}",
			"/generate_patient_checkup_report/{id}"},method=RequestMethod.GET)
	public String addManageAppointments(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = appointmentService.verifyUserAccess(userName, id);
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("appointment", new AppointmentDto());
				model.addAttribute("allAppList",appointmentService.getUserAppointmentsByUserName(userName));
				model.addAttribute("activeAppList",appointmentService.getUserActiveAppointmentsByUserName(userName));
				return "appointmentsystem/manageappointments";
			}
			case 2:{
				model.addAttribute("payment", new AppointmentDto());
				model.addAttribute("payments",appointmentService.getUserCheckupFeeByUserName(userName));
				return "appointmentsystem/payments";
			}
			case 3:{
				model.addAttribute("data", appointmentService.getAppointmentsDataByUserName(userName));
				return "appointmentsystem/generatepatientreport";
			}
			}
		}
		return "accessdenied";
	}

	@RequestMapping(value="/add_new_chekcup",method=RequestMethod.POST)
	public String addNewCheckup(@ModelAttribute AppointmentDto appointment, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		appointmentService.saveAppointment(appointment, userName);
		model.addAttribute("appointment", new AppointmentDto());
		model.addAttribute("allAppList",appointmentService.getUserAppointmentsByUserName(userName));
		model.addAttribute("activeAppList",appointmentService.getUserActiveAppointmentsByUserName(userName));
		return "appointmentsystem/manageappointments";
	}
	
	@RequestMapping(value="/add_checkup_payment",method=RequestMethod.POST)
	public String addCheckupPayment(@ModelAttribute AppointmentDto appointment, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		appointmentService.saveCheckupPayment(appointment, userName);
		model.addAttribute("payment", new AppointmentDto());
		model.addAttribute("payments",appointmentService.getUserCheckupFeeByUserName(userName));
		return "appointmentsystem/payments";
	}
	
	@RequestMapping(value="/edit_existing_checkup/{id}",method=RequestMethod.GET)
	public String editExistingCheckup(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("appointment", appointmentService.getAppointmentDtoById(id));
		model.addAttribute("allAppList",appointmentService.getUserAppointmentsByUserName(userName));
		model.addAttribute("activeAppList",appointmentService.getUserActiveAppointmentsByUserName(userName));
		return "appointmentsystem/manageappointments";
	}
	
	@RequestMapping(value="/edit_checkup_fee/{id}",method=RequestMethod.GET)
	public String editCheckupFee(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("payment", appointmentService.getCheckupFeeById(id));
		model.addAttribute("payments",appointmentService.getUserCheckupFeeByUserName(userName));
		return "appointmentsystem/payments";
	}
	
	@ResponseBody
	@RequestMapping(value="update_existing_checkup/{id}", method=RequestMethod.GET)
	public void updateExistingCheckup(@PathVariable("id")long id) {
		appointmentService.updateAppointmentStatus(id);
	}
	
	@RequestMapping(value="/download_appointments_report",method = RequestMethod.GET)
	public void generateStudentReport(HttpServletResponse response) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		File report = null;
		try {
			report = appointmentService.generateReport(userName);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
}
