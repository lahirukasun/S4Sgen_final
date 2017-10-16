package itech.s4sgen.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import itech.s4sgen.dto.PatientDto;
import itech.s4sgen.service.PatientService;

@Controller
public class PatientSystemController {

	@Autowired
	private PatientService patientService;
	
	@RequestMapping(value= {"/add_manage_patients/{id}",
			"/add_manage_patient_appointments/{id}",
			"/generate_patient_report/{id}",
			"/add_manage_payments/{id}"} , method = RequestMethod.GET)
	public String patientManagementSystem(Model model, @PathVariable("id") int id) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = patientService.verifyUserAccess(userName, id);
		
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("patient",new PatientDto());
				model.addAttribute("patientsList", patientService.getUserPatientsByUserName(userName));
				return "patientsystem/patientmanagementsystem";
			}
			case 2:{
				model.addAttribute("patient",new PatientDto());
				model.addAttribute("appointmentList", patientService.getUserAdmittedPatientAppointmensByUserName(userName));
				return "patientsystem/manageappointments";
			}
			case 3:{
				model.addAttribute("patientsData", patientService.getUserPatientsDataByUserName(userName));
				return "patientsystem/patientreport";
			}
			case 4:{
				model.addAttribute("payment", new PatientDto());
				model.addAttribute("paymentList" , patientService.getUserPatientPaymentsByUserName(userName));
				return "patientsystem/payments";
			}
			}
		}
		
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_patient" , method = RequestMethod.POST)
	public String addNewPatient(@ModelAttribute PatientDto patient, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		patientService.savePatient(patient, userName);
		model.addAttribute("patient",new PatientDto());
		model.addAttribute("patientsList", patientService.getUserPatientsByUserName(userName));
		return "patientsystem/patientmanagementsystem";
	}
	
	@RequestMapping(value="/add_admitted_patient_appointment" , method = RequestMethod.POST)
	public String addAdmittedPatientAppointment(@ModelAttribute PatientDto patient, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		patientService.saveAdmittedPatientAppointment(patient, userName);
		model.addAttribute("patient",new PatientDto());
		model.addAttribute("appointmentList", patientService.getUserAdmittedPatientAppointmensByUserName(userName));
		return "patientsystem/manageappointments";
	}
	
	@RequestMapping(value="/add_admitted_patient_payment",method = RequestMethod.POST)
	public String addAdmittedPatientPayment(@ModelAttribute PatientDto patient, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		patientService.saveAdmittedPatientPayment(patient);
		model.addAttribute("payment", new PatientDto());
		model.addAttribute("paymentList" , patientService.getUserPatientPaymentsByUserName(userName));
		return "patientsystem/payments";
	}
	
	@RequestMapping(value="/download_patient_report/{id}",method = RequestMethod.GET)
	public void generateStudentReport(@PathVariable("id") long id, HttpServletResponse response) {
		
		File report = null;
		try {
			report = patientService.generateReport(id);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
	@RequestMapping(value="/edit_patient/{id}", method = RequestMethod.GET)
	public String editExistingPatient(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("patient",patientService.getPatientById(id));
		model.addAttribute("patientsList", patientService.getUserPatientsByUserName(userName));
		return "patientsystem/patientmanagementsystem";
	}
	
	@RequestMapping(value="/edit_admitted_patient_appointment/{id}", method = RequestMethod.GET)
	public String editExistingPatientAppointment(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("patient",patientService.getAppointmentById(id));
		model.addAttribute("appointmentList", patientService.getUserAdmittedPatientAppointmensByUserName(userName));
		return "patientsystem/manageappointments";
	}
	
	@RequestMapping(value="/edit_admitted_patient_payment/{id}", method = RequestMethod.GET)
	public String editExistingPatientPayment(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("payment", new PatientDto());
		model.addAttribute("paymentList" , patientService.getUserPatientPaymentsByUserName(userName));
		return "patientsystem/payments";
	}

}
