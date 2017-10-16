package itech.s4sgen.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import itech.s4sgen.dto.UserDto;

@Controller
public class MainController {



	@RequestMapping(value="/", method=RequestMethod.GET)
	public String goHome() {
		return "home";
	}

	@RequestMapping(value="/error", method = RequestMethod.GET)
	public String goErrorPage() {
		return "error";
	}
	
	@RequestMapping(value="/access_denied", method = RequestMethod.GET)
	public String goAccessDeniedPage() {
		return "accessdenied";
	}
	
	@RequestMapping(value="/registration", method=RequestMethod.GET)
	public String goRegistration(Model model) {
		UserDto user = new UserDto();
		model.addAttribute("user", user);
		return "registration";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public ModelAndView goLogin() {
		ModelAndView mvc = new ModelAndView();
		mvc.setViewName("login");
		return mvc;
	}

}
