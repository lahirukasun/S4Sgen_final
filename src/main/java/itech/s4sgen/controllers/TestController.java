package itech.s4sgen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import itech.s4sgen.dto.StudentDto;
import itech.s4sgen.utils.HelpingClass;


@Controller
public class TestController {
	
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	public String goToProfile() {
		return "profilepage";
	}
	
	@RequestMapping(value="/settings", method=RequestMethod.GET)
	public String goToSettings() {
		return "settings";
	}
	
	@RequestMapping(value="/studentsystem", method=RequestMethod.GET)
	public String goRegistration(Model model) {
		model.addAttribute("programs", HelpingClass.getStudyProgramsList());
		model.addAttribute("student", new StudentDto());
		return "studentsystem/studentmanagementsystem";
	}
	
	@RequestMapping(value="/exmas_system", method=RequestMethod.GET)
	public String goExamsManagement() {
		return "examsmarkingsystem";
	}
	
	@RequestMapping(value="/attendancesystem", method=RequestMethod.GET)
	public String goAttendanceManagement() {
		return "attendancesystem";
	}
	
	@RequestMapping(value="/generatereport", method=RequestMethod.GET)
	public String goStudentReport() {
		return "generatestudentreport";
	}
	
	@RequestMapping(value="/fee_management", method=RequestMethod.GET)
	public String goStudentFeeSystem() {
		return "feemanagementsystem";
	}
	
	@RequestMapping(value="/marks_detail", method=RequestMethod.GET)
	public String goStudentMarksDetail() {
		return "marksdetail";
	}

	@RequestMapping(value="/attendance_detail", method=RequestMethod.GET)
	public String goStudentAttendanceDetail() {
		return "attendancedetail";
	}
	
	@RequestMapping(value="/fee_detail", method=RequestMethod.GET)
	public String goStudentFeeDetail() {
		return "feedetail";
	}
	
	@RequestMapping(value="/teachermanagementsystem", method=RequestMethod.GET)
	public String goTeacherManagementSystem() {
		return "teachermanagementsystem";
	}
	
	/*@RequestMapping(value="/registration", method=RequestMethod.GET)
	public String goRegistration(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "registration";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView goLogin() {
		UserDto user = new UserDto();
		ModelAndView mvc = new ModelAndView();
		mvc.setViewName("login");
		mvc.addObject("user", user);
		return mvc;
	}*/

}
