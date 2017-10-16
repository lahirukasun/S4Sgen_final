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

import itech.s4sgen.dto.TeacherDto;
import itech.s4sgen.service.TeacherService;
import itech.s4sgen.utils.HelpingClass;

@Controller
public class TeacherSystemController {

	@Autowired
	private TeacherService teacherService;

	@RequestMapping(value= {"/add_manage_teachers/{id}",
			"/teachers_exams_duties/{id}",
			"/teachers_time_table/{id}",
			"/teachers_salary_management/{id}",
			"/generate_teacher_report/{id}",} , method=RequestMethod.GET)
	public String goToManageTeachers(@PathVariable("id") int id,Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = teacherService.verifyUserAccess(login, id);
		if(access) {
			switch(id) {
			case 1:
			{
				model.addAttribute("teachersList", teacherService.getTeachersOfUserByUserName(login));
				model.addAttribute("courses",HelpingClass.getStudyCoursesList());
				model.addAttribute("teacher", new TeacherDto());
				return "teachersystem/teachermanagementsystem";
			}
			case 2:
			{
				model.addAttribute("dutyList", teacherService.getTeachersDutiesOfUserByUserName(login));
				model.addAttribute("courses",HelpingClass.getStudyCoursesList());
				model.addAttribute("teacher", new TeacherDto());
				return "teachersystem/teacherexamduties";
			}
			case 3:
			{
				model.addAttribute("timeTable",teacherService.getTeachersLectureScheduleOfUserByUserName(login));
				model.addAttribute("courses",HelpingClass.getStudyCoursesList());
				model.addAttribute("teacher", new TeacherDto());
				return "teachersystem/teachertimetable";
			}
			case 4:
			{
				model.addAttribute("months",HelpingClass.getMonthsList());
				model.addAttribute("teacher", new TeacherDto());
				model.addAttribute("salaryList", teacherService.getTeachersSalaryOfUserByUserName(login));
				return "teachersystem/salarymanagement";
			}
			case 5:
			{
				model.addAttribute("teacherData", teacherService.getTeachersSalaryOfUserByUserName(login));
				return "teachersystem/teacherreport";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_teacher", method = RequestMethod.POST)
	public String addTeacher(@ModelAttribute TeacherDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		teacherService.saveTeacher(dto,login);
		model.addAttribute("teachersList", teacherService.getTeachersOfUserByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		model.addAttribute("teacher", new TeacherDto());
		return "teachersystem/teachermanagementsystem";
	}
	
	@RequestMapping(value="/add_exam_duty", method = RequestMethod.POST)
	public String addExamDuty(@ModelAttribute TeacherDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		teacherService.saveExamDuty(dto,login);
		model.addAttribute("teacher", new TeacherDto());
		model.addAttribute("dutyList", teacherService.getTeachersDutiesOfUserByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		return "teachersystem/teacherexamduties";
	}
	
	@RequestMapping(value="/add_lecture_schedule", method = RequestMethod.POST)
	public String addLectureSchedule(@ModelAttribute TeacherDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		teacherService.saveLectureSchedule(dto,login);
		model.addAttribute("timeTable",teacherService.getTeachersLectureScheduleOfUserByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		model.addAttribute("teacher", new TeacherDto());
		return "teachersystem/teachertimetable";
	}
	
	@RequestMapping(value="/add_teacher_salary", method = RequestMethod.POST)
	public String addTeacherSalary(@ModelAttribute TeacherDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		teacherService.saveTeacherSalary(dto,login);
		model.addAttribute("months",HelpingClass.getMonthsList());
		model.addAttribute("teacher", new TeacherDto());
		model.addAttribute("salaryList", teacherService.getTeachersSalaryOfUserByUserName(login));
		return "teachersystem/salarymanagement";
	}
	
	@RequestMapping(value="/teacher_salary_details/{id}", method = RequestMethod.GET)
	public String getTeacherSalaryDetail(@PathVariable("id") long id, Model model) throws ParseException {
		model.addAttribute("name", teacherService.getTeachersSalaryOfByTeacherId(id).get(0).getName());
		model.addAttribute("salaryList", teacherService.getTeachersSalaryOfByTeacherId(id));
		return "teachersystem/salarydetail";
	}
	
	@RequestMapping(value="/edit_teacher/{id}", method = RequestMethod.GET)
	public String editTeacher(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("teacher", teacherService.getTeacherDtoById(id));
		model.addAttribute("teachersList", teacherService.getTeachersOfUserByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		return "teachersystem/teachermanagementsystem";
	}
	
	@RequestMapping(value="/edit_exam_duty/{id}", method = RequestMethod.GET)
	public String editExamDuty(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("teacher", teacherService.getTeacherExamDutyById(id));
		model.addAttribute("dutyList", teacherService.getTeachersDutiesOfUserByUserName(login));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		return "teachersystem/teacherexamduties";
	}
	
	@RequestMapping(value="/edit_lecture_schedule/{id}", method = RequestMethod.GET)
	public String editLectureSchedule(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("teacher", teacherService.getLectureScheduleById(id));
		model.addAttribute("courses",HelpingClass.getStudyCoursesList());
		model.addAttribute("timeTable",teacherService.getTeachersLectureScheduleOfUserByUserName(login));
		return "teachersystem/teachertimetable";
	}
	
	@RequestMapping(value="/edit_teacher_salary/{id}", method = RequestMethod.GET)
	public String editTeacherSalary(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("months",HelpingClass.getMonthsList());
		model.addAttribute("teacher", teacherService.getTeacherDtoByTeacherSalaryId(id));
		model.addAttribute("salaryList", teacherService.getTeachersSalaryOfUserByUserName(login));
		return "teachersystem/salarymanagement";
	}
	
	@RequestMapping(value="/download_teacher_report/{id}",method = RequestMethod.GET)
	public void generateStudentReport(@PathVariable("id") long id, HttpServletResponse response) {
		
		File report = null;
		try {
			report = teacherService.generateReport(id);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
}
