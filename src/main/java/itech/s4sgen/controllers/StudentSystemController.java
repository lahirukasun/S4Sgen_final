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

import itech.s4sgen.dto.StudentDto;
import itech.s4sgen.service.StudentService;
import itech.s4sgen.utils.HelpingClass;

@Controller
public class StudentSystemController {

	@Autowired
	private StudentService studentService; 
	
	
	@RequestMapping(value= {"/add_manage_students/{id}",
			"/students_exams_marks/{id}",
			"/students_attendance_track/{id}",
			"/students_fee_management/{id}",
			"/generate_student_report/{id}",} , method=RequestMethod.GET)
	public String goToManageStudents(@PathVariable("id") int id,Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = studentService.verifyUserAccess(login, id);
		if(access) {
			switch(id) {
			case 1:
			{
				model.addAttribute("studentsList", studentService.getStudentsOfUserByUserName(login));
				model.addAttribute("programs",HelpingClass.getStudyProgramsList());
				model.addAttribute("student", new StudentDto());
				return "studentsystem/studentmanagementsystem";
			}
			case 2:
			{
				model.addAttribute("semesters",HelpingClass.getSemestersList());
				model.addAttribute("marksList", studentService.getUserStudentsExamsMarksByUserName(login));
				model.addAttribute("courses",HelpingClass.getStudyCoursesList());
				model.addAttribute("student", new StudentDto());
				return "studentsystem/examsmarkingsystem";
			}
			case 3:
			{
				model.addAttribute("months",HelpingClass.getMonthsList());
				model.addAttribute("student", new StudentDto());
				model.addAttribute("attendanceList", studentService.getUserStudentsAttendanceByUserName(login));
				return "studentsystem/attendancesystem";
			}
			case 4:
			{
				model.addAttribute("semesters",HelpingClass.getSemestersList());
				model.addAttribute("student", new StudentDto());
				model.addAttribute("feeList", studentService.getUserStudentsFeeByUserName(login));
				return "studentsystem/feemanagementsystem";
			}
			case 5:
			{
				model.addAttribute("studentsData", studentService.getUserStudentsDataByUserName(login));
				return "studentsystem/generatestudentreport";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_student", method = RequestMethod.POST)
	public String addStudent(@ModelAttribute StudentDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		studentService.saveStudent(dto,login);
		model.addAttribute("student", new StudentDto());
		model.addAttribute("studentsList", studentService.getStudentsOfUserByUserName(login));
		model.addAttribute("programs",HelpingClass.getStudyProgramsList());
		return "studentsystem/studentmanagementsystem";
	}
	
	@RequestMapping(value="/add_student_marks", method = RequestMethod.POST)
	public String addStudentMarks(@ModelAttribute StudentDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		studentService.saveStudentMarks(dto,login);
		model.addAttribute("student", new StudentDto());
		model.addAttribute("marksList", studentService.getUserStudentsExamsMarksByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		model.addAttribute("semesters",HelpingClass.getSemestersList());
		return "studentsystem/examsmarkingsystem";
	}
	
	@RequestMapping(value="/add_student_attendance", method = RequestMethod.POST)
	public String addStudentAttendance(@ModelAttribute StudentDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		studentService.saveStudentAttendance(dto,login);
		model.addAttribute("student", new StudentDto());
		model.addAttribute("attendanceList", studentService.getUserStudentsAttendanceByUserName(login));
		model.addAttribute("months",HelpingClass.getMonthsList());
		return "studentsystem/attendancesystem";
	}
	
	@RequestMapping(value="/add_student_fee", method = RequestMethod.POST)
	public String addStudentFee(@ModelAttribute StudentDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		studentService.saveStudentFee(dto,login);
		model.addAttribute("student", new StudentDto());
		model.addAttribute("feeList", studentService.getUserStudentsFeeByUserName(login));
		model.addAttribute("semesters",HelpingClass.getSemestersList());
		return "studentsystem/feemanagementsystem";
	}

	@RequestMapping(value="/student_marks_details/{id}" , method = RequestMethod.GET)
	public ModelAndView getStudentMarksDetails(@PathVariable("id") long id) {
		System.out.println("came here");
		List<StudentDto> dtoList = studentService.getStudentMarksDetail(id);
		ModelAndView mvc = new ModelAndView();
		if(dtoList!=null) {
			mvc.addObject("marks",dtoList);
			mvc.setViewName("studentsystem/marksdetail");
			return mvc;
		}
		
		mvc.setViewName("nodetail");
		return mvc;
	}
	
	@RequestMapping(value="/student_attendance_details/{id}" , method = RequestMethod.GET)
	public ModelAndView getStudentAttendanceDetails(@PathVariable("id") long id) {
		List<StudentDto> dtoList = studentService.getStudentAttendanceDetail(id);
		ModelAndView mvc = new ModelAndView();
		if(dtoList!=null) {
			mvc.addObject("name",dtoList.get(0).getName());
			mvc.addObject("attendanceList",dtoList);
			mvc.setViewName("studentsystem/attendancedetail");
			return mvc;
		}
		
		mvc.setViewName("nodetail");
		return mvc;
	}
	
	@RequestMapping(value="/student_fee_details/{id}" , method = RequestMethod.GET)
	public ModelAndView getStudentFeeDetails(@PathVariable("id") long id) {
		List<StudentDto> dtoList = studentService.getStudentFeeDetail(id);
		ModelAndView mvc = new ModelAndView();
		if(dtoList!=null) {
			mvc.addObject("name",dtoList.get(0).getName());
			mvc.addObject("feeList",dtoList);
			mvc.setViewName("studentsystem/feedetail");
			return mvc;
		}
		
		mvc.setViewName("nodetail");
		return mvc;
	}
	
	@RequestMapping(value="/edit_student_details/{id}", method = RequestMethod.GET)
	public String editStudentDetails(@PathVariable("id") long id, Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("studentsList", studentService.getStudentsOfUserByUserName(login));
		model.addAttribute("programs",HelpingClass.getStudyProgramsList());
		model.addAttribute("student", studentService.getStudentById(id));
		return "studentsystem/studentmanagementsystem";
	}
	
	@RequestMapping(value="/edit_exam_marks/{id}", method = RequestMethod.GET)
	public String editStudentMarks(@PathVariable("id") long id, Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("marksList", studentService.getUserStudentsExamsMarksByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		model.addAttribute("student", studentService.getStudentMarksById(id));
		model.addAttribute("semesters",HelpingClass.getSemestersList());
		return "studentsystem/examsmarkingsystem";
	}
	
	@RequestMapping(value="/edit_student_attendance/{id}", method = RequestMethod.GET)
	public String editStudentAttendance(@PathVariable("id") long id, Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("months",HelpingClass.getMonthsList());
		model.addAttribute("student", studentService.getStudentAttendanceById(id));
		model.addAttribute("attendanceList", studentService.getUserStudentsAttendanceByUserName(login));
		return "studentsystem/attendancesystem";
	}
	
	@RequestMapping(value="/edit_student_fee/{id}", method = RequestMethod.GET)
	public String editStudentFee(@PathVariable("id") long id, Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("semesters",HelpingClass.getSemestersList());
		model.addAttribute("student", studentService.getStudentFeeById(id));
		model.addAttribute("feeList", studentService.getUserStudentsFeeByUserName(login));
		return "studentsystem/feemanagementsystem";
	}
	
	@RequestMapping(value="/download_student_report/{id}",method = RequestMethod.GET)
	public void generateStudentReport(@PathVariable("id") long id, HttpServletResponse response) {
		
		File report = null;
		try {
			report = studentService.generateReport(id);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/roll_no_validation", method=RequestMethod.GET)
	public boolean validateRollNo(String rollNo) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return studentService.rollNoValidation(rollNo,userName);
	}
}
