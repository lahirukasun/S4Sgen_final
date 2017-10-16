package itech.s4sgen.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import itech.s4sgen.dto.SettingDto;
import itech.s4sgen.dto.SubManagementSystemDto;
import itech.s4sgen.dto.SystemDetailDto;
import itech.s4sgen.dto.SystemFeaturesDto;
import itech.s4sgen.dto.UserDto;
import itech.s4sgen.models.ManagementSystem;
import itech.s4sgen.models.SubManagementSystem;
import itech.s4sgen.models.SystemDetail;
import itech.s4sgen.service.ManagementSystemService;
import itech.s4sgen.service.SubManagementSystemService;
import itech.s4sgen.service.SystemFeaturesService;
import itech.s4sgen.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ManagementSystemService managementSystemService;

	@Autowired
	private SubManagementSystemService subManagementSystemService;

	@Autowired
	private SystemFeaturesService systemFeaturesService;


	@RequestMapping(value="/register_user", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute UserDto user, Model model) {

		String status = userService.registerUser(user);

		if(status.equals("DONE")) {
			return "login";
		}else if(status.equals("EXISTS")){
			return "redirect:/registration?exists=true";
		}else {
			return "error";
		}

	}

	@RequestMapping(value="/login_user", method = RequestMethod.GET)
	public String loginUser(Model model) {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		if(user.equals("admin")) {
			if(userService.loginSuperUser(user).equals("DONE"))
				return "redirect:/admin_login";
		}
		
		Object[] data = userService.loginUser(user,model);
		int state = ((Integer)data[0]).intValue();


		switch(state) {
		case 1:
		{
			model.addAttribute("systems", managementSystemService.getAllManagementSystems());
			return "systems";
		}
		case 2:
		{
			model.addAttribute("subSystems", subManagementSystemService.getAllByManagementSystem((ManagementSystem)data[1]));
			return "subsystems";
		}
		case 3:
		{
			model.addAttribute("systemFeatures", systemFeaturesService.getAllBySubManagementSystem((SubManagementSystem)data[1]));
			return "systemFeatures";
		}
		case 4:
		{
			model.addAttribute("detailDto", (SystemDetailDto)data[1]); 
			return "systemdetail";
		}
		case 5:
		{
			model = (Model)data[1];
			String page = (String)data[2];
			return page;
		}
		default:
			return "error";
		}
	}

	@RequestMapping(value="/myprofile" , method = RequestMethod.GET)
	public String goToMyProfile(Model model) {
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		model = userService.getProfileData(model, user);
		return "profilePage";
	}
	
	@RequestMapping(value="/mysettings", method=RequestMethod.GET)
	public String goToSettings(Model model) {
		model.addAttribute("setting",new SettingDto());
		return "settings";
	}
	
	@RequestMapping(value="/update_user_password", method=RequestMethod.POST)
	public String updateUserPassword(@ModelAttribute SettingDto setting) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		String status = userService.updateUserPassword(setting,userName);
		if(status.equals("DONE"))
			return "redirect:/mysettings?reset=true";
		else if(status.equals("INCORRECT"))
			return "redirect:/mysettings?incorrect=true";
		else
			return "redirect:/mysettings?error=true";
	}
	
	@RequestMapping(value="/save_user_managementsystem/{id}", method = RequestMethod.GET)
	public String saveUserManagementSystem(Model model, @PathVariable("id") long id) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		List<SubManagementSystemDto> subSystems = userService.saveUserSelectedManagementSystem(id, userName);
		model.addAttribute("subSystems", subSystems);
		return "subsystems";
	}

	@RequestMapping(value="/save_user_submanagementsystem/{id}", method = RequestMethod.GET)
	public String saveUserSubManagementSystem(Model model, @PathVariable("id") long id) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		List<SystemFeaturesDto> systemFeatures = userService.saveUserSelectedSubManagementSystem(id, userName);
		model.addAttribute("systemFeatures", systemFeatures);
		return "systemfeatures";
	}


	@RequestMapping(value="/save_user_systemfeatures", method = RequestMethod.POST)
	public String saveUserSystemFeatures(Model model, @RequestParam(value = "features" , required = false) int[] features) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		String page= userService.saveUserSelectedSystemFeatures(features, userName);
		model.addAttribute("detailDto", new SystemDetailDto());
		return page;
	}
	
	@RequestMapping(value="/save_user_systemdetails", method = RequestMethod.POST)
	public String saveUserSystemFeatures(@RequestParam(value="projectName") String projectName,@RequestParam(value = "logo") MultipartFile logo,
			@RequestParam(value = "domainName") String domainName,
			@RequestParam(value = "mobile") String mobile,
			Model model){
		SystemDetailDto dto = new SystemDetailDto();
		dto.setProjectName(projectName);
		dto.setDomainName(domainName);
		dto.setMobile(mobile);
		dto.setLogo(logo);
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Object[] pageData = userService.saveUserSystemDetails(dto, userName, model);
		model = (Model)pageData[0];
		String page = (String)pageData[1];
		return page;
	}
} 
