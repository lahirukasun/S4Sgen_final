package itech.s4sgen.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import itech.s4sgen.dto.DoctorDto;
import itech.s4sgen.service.DoctorService;
import itech.s4sgen.utils.HelpingClass;

@Controller
public class DoctorSystemController {

	@Autowired
	private DoctorService doctorService;
	
	@RequestMapping(value= {"/add_manage_doctors/{id}",
			"/manage_doctor_appointment/{id}",
			"/add_manage_doctor_attenance/{id}",
			"/doctor_salary_management/{id}",
			"/generate_doctor_report/{id}"}, method=RequestMethod.GET)
	public String addManageDoctorSystem(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = doctorService.verifyUserAccess(userName, id);
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("doctor", new DoctorDto());
				model.addAttribute("doctorsList",doctorService.getUserDoctorByUserName(userName) );
				model.addAttribute("specialities", HelpingClass.getDoctorsSpecialitiesList());
				return "doctorsystem/doctormanagementsystem";
			}
			case 2:{
				model.addAttribute("appointments", doctorService.getUserAppointmentsByUserName(userName));
				return "doctorsystem/manageappointments";
			}
			case 3:{
				model.addAttribute("doctor", new DoctorDto());
				model.addAttribute("attendanceList", doctorService.getUserDoctorsAttendanceByUserName(userName));
				model.addAttribute("months",HelpingClass.getMonthsList());
				return "doctorsystem/attendancesystem";
			}
			case 4:{
				model.addAttribute("months",HelpingClass.getMonthsList());
				model.addAttribute("doctor", new DoctorDto());
				model.addAttribute("salaryList", doctorService.getDoctorsSalaryOfUserByUserName(userName));
				return "doctorsystem/salarymanagement";
			}
			case 5:{
				model.addAttribute("dataList", doctorService.getUserDoctorsDataByUserName(userName));
				return "doctorsystem/generatedoctorreport";
			}
			}
		}
		return "accessdenied";
	}

	@RequestMapping(value="/add_doctor", method=RequestMethod.POST)
	public String addDoctor(@ModelAttribute DoctorDto doctor, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		doctorService.saveDoctor(doctor, userName);
		model.addAttribute("doctor", new DoctorDto());
		model.addAttribute("doctorsList",doctorService.getUserDoctorByUserName(userName) );
		model.addAttribute("specialities", HelpingClass.getDoctorsSpecialitiesList());
		return "doctorsystem/doctormanagementsystem";
		
	}
	
	@RequestMapping(value="/add_doctor_attendance", method = RequestMethod.POST)
	public String addDoctorAttendance(@ModelAttribute DoctorDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		doctorService.saveDoctorAttendance(dto,login);
		model.addAttribute("doctor", new DoctorDto());
		model.addAttribute("attendanceList", doctorService.getUserDoctorsAttendanceByUserName(login));
		model.addAttribute("months",HelpingClass.getMonthsList());
		return "doctorsystem/attendancesystem";
	}
	
	@RequestMapping(value="/add_doctor_salary", method = RequestMethod.POST)
	public String addDoctorSalary(@ModelAttribute DoctorDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		doctorService.saveDoctorSalary(dto,login);
		model.addAttribute("months",HelpingClass.getMonthsList());
		model.addAttribute("doctor", new DoctorDto());
		model.addAttribute("salaryList", doctorService.getDoctorsSalaryOfUserByUserName(login));
		return "doctorsystem/salarymanagement";
	}
	
	@RequestMapping(value="/doctor_attendance_details/{id}" , method = RequestMethod.GET)
	public ModelAndView getDoctorAttendanceDetails(@PathVariable("id") long id) {
		List<DoctorDto> dtoList = doctorService.getDoctorAttendanceDetail(id);
		ModelAndView mvc = new ModelAndView();
		if(dtoList!=null) {
			mvc.addObject("name",dtoList.get(0).getName());
			mvc.addObject("attendanceList",dtoList);
			mvc.setViewName("doctorsystem/attendancedetail");
			return mvc;
		}
		
		mvc.setViewName("nodetail");
		return mvc;
	}
	
	@RequestMapping(value="/doctor_salary_details/{id}", method = RequestMethod.GET)
	public String getDoctorSalaryDetail(@PathVariable("id") long id, Model model) throws ParseException {
		model.addAttribute("name", doctorService.getDoctorsSalaryOfByDoctorId(id).get(0).getName());
		model.addAttribute("salaryList", doctorService.getDoctorsSalaryOfByDoctorId(id));
		return "doctorsystem/salarydetail";
	}
	
	@RequestMapping(value="/edit_doctor_detail/{id}", method=RequestMethod.GET)
	public String editDoctor(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("doctor", doctorService.getDoctorDtoByDoctorId(id));
		model.addAttribute("doctorsList",doctorService.getUserDoctorByUserName(userName) );
		model.addAttribute("specialities", HelpingClass.getDoctorsSpecialitiesList());
		return "doctorsystem/doctormanagementsystem";
	}
	
	@ResponseBody
	@RequestMapping(value="/update_appoinment_status/{id}", method = RequestMethod.GET)
	public void updateAppointmentStatus(@PathVariable("id") long id) {
		doctorService.updateAppointmentStatus(id);
			//return "true";
		//return "false";
	}
	
	@RequestMapping(value="/edit_doctor_attendance/{id}", method = RequestMethod.GET)
	public String editDoctorAttendance(@PathVariable("id") long id, Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("months",HelpingClass.getMonthsList());
		model.addAttribute("doctor", doctorService.getDoctorAttendanceById(id));
		model.addAttribute("attendanceList", doctorService.getUserDoctorsAttendanceByUserName(login));
		return "doctorsystem/attendancesystem";
	}
	
	@RequestMapping(value="/edit_doctor_salary/{id}", method = RequestMethod.GET)
	public String editDoctorSalary(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("months",HelpingClass.getMonthsList());
		model.addAttribute("doctor", doctorService.getDoctorDtoByDoctorSalaryId(id));
		model.addAttribute("salaryList", doctorService.getDoctorsSalaryOfUserByUserName(login));
		return "doctorsystem/salarymanagement";
	}
	
	@RequestMapping(value="/download_doctor_report/{id}",method = RequestMethod.GET)
	public void generateDoctorReport(@PathVariable("id") long id, HttpServletResponse response) {
		
		File report = null;
		try {
			report = doctorService.generateReport(id);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
}
