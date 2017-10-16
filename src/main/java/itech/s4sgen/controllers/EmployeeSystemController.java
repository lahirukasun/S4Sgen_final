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
import org.springframework.web.servlet.ModelAndView;

import itech.s4sgen.dto.EmployeeDto;
import itech.s4sgen.service.EmployeeService;
import itech.s4sgen.utils.HelpingClass;

@Controller
public class EmployeeSystemController {

	@Autowired
	private EmployeeService employeeService;
	
	@RequestMapping(value= {"/add_manage_employees/{id}",
			"/manage_employee_attendance/{id}",
			"/add_manage_employee_salary/{id}",
			"/employee_bonuses_management/{id}",
			"/generate_employee_report/{id}"}, method=RequestMethod.GET)
	public String addManageEmployeeSystem(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = employeeService.verifyUserAccess(userName, id);
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("employeesList",employeeService.getUserEmployeesByUserName(userName) );
				model.addAttribute("jobsList", HelpingClass.getRestaurantJobsList());
				return "employeesystem/employeemanagementsystem";
			}
			case 2:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("attendanceList", employeeService.getUserEmployeesAttendanceByUserName(userName));
				model.addAttribute("months",HelpingClass.getMonthsList());
				return "employeesystem/attendancesystem";
			}
			case 3:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("salaryList", employeeService.getUserEmployeesSalaryByUserName(userName));
				model.addAttribute("months",HelpingClass.getMonthsList());
				return "employeesystem/salarymanagementsystem";
			}
			case 4:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("bonusList", employeeService.getEmployeesBonusesDetailByUserName(userName));
				return "employeesystem/bonusesmanagement";
			}
			case 5:{
				model.addAttribute("dataList", employeeService.getUserEmployeesDataByUserName(userName));
				return "employeesystem/generateemployeereport";
			}
			}
		}
		return "accessdenied";
	}

	@RequestMapping(value="/add_employee", method=RequestMethod.POST)
	public String addEmployee(@ModelAttribute EmployeeDto employee, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		employeeService.saveEmployee(employee, userName);
		model.addAttribute("employee", new EmployeeDto());
		model.addAttribute("employeesList",employeeService.getUserEmployeesByUserName(userName) );
		model.addAttribute("jobsList", HelpingClass.getRestaurantJobsList());
		return "employeesystem/employeemanagementsystem";
		
	}
	
	@RequestMapping(value="/add_employee_attendance", method = RequestMethod.POST)
	public String addEmployeeAttendance(@ModelAttribute EmployeeDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		employeeService.saveEmployeeAttendance(dto,login);
		model.addAttribute("employee", new EmployeeDto());
		model.addAttribute("attendanceList", employeeService.getUserEmployeesAttendanceByUserName(login));
		model.addAttribute("months",HelpingClass.getMonthsList());
		return "employeesystem/attendancesystem";
	}
	
	@RequestMapping(value="/add_employee_salary", method = RequestMethod.POST)
	public String addEmployeeSalary(@ModelAttribute EmployeeDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		employeeService.saveEmployeeSalary(dto,login);
		model.addAttribute("employee", new EmployeeDto());
		model.addAttribute("salaryList", employeeService.getUserEmployeesSalaryByUserName(login));
		model.addAttribute("months",HelpingClass.getMonthsList());
		return "employeesystem/salarymanagementsystem";
	}
	
	@RequestMapping(value="/add_employee_bonus", method = RequestMethod.POST)
	public String addEmployeeBonus(@ModelAttribute EmployeeDto dto, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		employeeService.saveEmployeeBonus(dto,login);
		model.addAttribute("employee", new EmployeeDto());
		model.addAttribute("bonusList", employeeService.getEmployeesBonusesDetailByUserName(login));
		return "employeesystem/bonusesmanagement";
	}
	
	@RequestMapping(value="/employee_attendance_details/{id}" , method = RequestMethod.GET)
	public ModelAndView getEmployeeAttendanceDetails(@PathVariable("id") long id) {
		List<EmployeeDto> dtoList = employeeService.getEmployeeAttendanceDetail(id);
		ModelAndView mvc = new ModelAndView();
		if(dtoList!=null) {
			mvc.addObject("name",dtoList.get(0).getName());
			mvc.addObject("attendanceList",dtoList);
			mvc.setViewName("employeesystem/attendancedetail");
			return mvc;
		}
		
		mvc.setViewName("nodetail");
		return mvc;
	}
	
	@RequestMapping(value="/employee_salary_details/{id}", method = RequestMethod.GET)
	public String getEmployeeSalaryDetail(@PathVariable("id") long id, Model model) throws ParseException {
		model.addAttribute("name", employeeService.getEmployeesSalaryDetailByEmployeeId(id).get(0).getName());
		model.addAttribute("salaryList", employeeService.getEmployeesSalaryDetailByEmployeeId(id));
		return "employeesystem/salarydetail";
	}
	
	@RequestMapping(value="/edit_employee_detail/{id}", method=RequestMethod.GET)
	public String editEmployee(@PathVariable("id")long id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("employee", employeeService.getEmployeeById(id));
		model.addAttribute("employeesList",employeeService.getUserEmployeesByUserName(userName) );
		model.addAttribute("jobsList", HelpingClass.getRestaurantJobsList());
		return "employeesystem/employeemanagementsystem";
	}
	
	@RequestMapping(value="/edit_employee_attendance/{id}", method = RequestMethod.GET)
	public String editEmployeeAttendance(@PathVariable("id") long id, Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("employee", employeeService.getEmployeeAttendanceById(id));
		model.addAttribute("attendanceList", employeeService.getUserEmployeesAttendanceByUserName(login));
		model.addAttribute("months",HelpingClass.getMonthsList());
		return "employeesystem/attendancesystem";
	}
	
	@RequestMapping(value="/edit_employee_salary/{id}", method = RequestMethod.GET)
	public String editEmployeeSalary(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("employee", employeeService.getEmployeeSalaryById(id));
		model.addAttribute("salaryList", employeeService.getUserEmployeesSalaryByUserName(login));
		model.addAttribute("months",HelpingClass.getMonthsList());
		return "employeesystem/salarymanagementsystem";
	}
	
	@RequestMapping(value="/edit_employee_bonus/{id}", method = RequestMethod.GET)
	public String editEmployeeBonus(@PathVariable("id") long id, Model model) throws ParseException {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("employee", employeeService.getEmployeeBonusById(id));
		model.addAttribute("bonusList", employeeService.getEmployeesBonusesDetailByUserName(login));
		return "employeesystem/bonusesmanagement";
	}
	
	@RequestMapping(value="/download_employee_report/{id}",method = RequestMethod.GET)
	public void generateEmployeeReport(@PathVariable("id") long id, HttpServletResponse response) {
		
		File report = null;
		try {
			report = employeeService.generateReport(id);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
}
