package itech.s4sgen.controllers;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import itech.s4sgen.dto.SchoolBusDto;
import itech.s4sgen.dto.StaffDto;
import itech.s4sgen.dto.UtilityBillDto;
import itech.s4sgen.service.SchoolService;
import itech.s4sgen.utils.HelpingClass;

@Controller
public class SchoolSystemController {
	
	@Autowired
	private SchoolService schoolService;
	
	@RequestMapping(value= {"/maintenance_staff_management/{id}",
			"/school_bus_management/{id}",
			"/utitliy_bills_management/{id}",} , method=RequestMethod.GET)
	public String goToManageTeachers(@PathVariable("id") int id,Model model) {
		String login = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = schoolService.verifyUserAccess(login, id);
		if(access) {
			switch(id) {
			case 6:
			{
				model.addAttribute("staff",new StaffDto());
				model.addAttribute("dutyList", HelpingClass.getDutiesList());
				model.addAttribute("staffList" , schoolService.getUserStaffByUserName(login));
				return "schoolsystem/maintenancestaffmanagement";
			}
			case 7:
			{
				model.addAttribute("bus", new SchoolBusDto());
				model.addAttribute("routeList", HelpingClass.getRoutesList());
				model.addAttribute("busesList", schoolService.getUserBusesByUserName(login));
				return "schoolsystem/schoolbusmanagement";
			}
			case 8:
			{
				model.addAttribute("bill", new UtilityBillDto());
				model.addAttribute("billsList", schoolService.getUserUtilityBillsByUserName(login));
				model.addAttribute("services", HelpingClass.getServicesList());
				return "schoolsystem/utilitybillsmanagement";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_maintenance_staff", method = RequestMethod.POST)
	public String addMaintenanceStaff(@ModelAttribute StaffDto dto, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		schoolService.saveMaintenanceStaff(dto, userName);
		model.addAttribute("staff",new StaffDto());
		model.addAttribute("dutyList", HelpingClass.getDutiesList());
		model.addAttribute("staffList" , schoolService.getUserStaffByUserName(userName));
		return "schoolsystem/maintenancestaffmanagement";
	}
	
	@RequestMapping(value="/add_manage_school_bus", method = RequestMethod.POST)
	public String addManageSchoolBus(@ModelAttribute SchoolBusDto dto, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		schoolService.saveSchoolBus(dto, userName);
		model.addAttribute("bus", new SchoolBusDto());
		model.addAttribute("routeList", HelpingClass.getRoutesList());
		model.addAttribute("busesList", schoolService.getUserBusesByUserName(userName));
		return "schoolsystem/schoolbusmanagement";
	}
	
	@RequestMapping(value="/add_manage_utility_bill", method = RequestMethod.POST)
	public String addManageUtilityBill(@ModelAttribute UtilityBillDto dto, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		schoolService.saveUtilityBill(dto, userName);
		model.addAttribute("bill", new UtilityBillDto());
		model.addAttribute("billsList", schoolService.getUserUtilityBillsByUserName(userName));
		model.addAttribute("services", HelpingClass.getServicesList());
		return "schoolsystem/utilitybillsmanagement";
	}
	
	@RequestMapping(value="/edit_maintenance_staff/{id}", method = RequestMethod.GET)
	public String editMaintenanceStaff(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("staff", schoolService.getStaffDtoById(id));
		model.addAttribute("dutyList", HelpingClass.getDutiesList());
		model.addAttribute("staffList" , schoolService.getUserStaffByUserName(userName));
		return "schoolsystem/maintenancestaffmanagement";
	}
	
	@RequestMapping(value="/edit_school_bus/{id}", method = RequestMethod.GET)
	public String editSchoolbus(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("bus", schoolService.getSchoolBusDtoById(id));
		model.addAttribute("routeList", HelpingClass.getRoutesList());
		model.addAttribute("busesList", schoolService.getUserBusesByUserName(userName));
		return "schoolsystem/schoolbusmanagement";
	}
	
	@RequestMapping(value="/edit_utility_bill/{id}", method = RequestMethod.GET)
	public String editUtilityBill(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("bill", schoolService.getUtilityBillDtoById(id));
		model.addAttribute("billsList", schoolService.getUserUtilityBillsByUserName(userName));
		model.addAttribute("services", HelpingClass.getServicesList());
		return "schoolsystem/utilitybillsmanagement";
	}
}
