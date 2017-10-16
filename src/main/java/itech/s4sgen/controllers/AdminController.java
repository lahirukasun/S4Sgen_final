package itech.s4sgen.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import itech.s4sgen.dao.UserDao;
import itech.s4sgen.service.AdminService;
import itech.s4sgen.service.UserService;

@Controller
public class AdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value="/admin_login", method = RequestMethod.GET)
	public String getUsersForAdmin(Model model) {
		model.addAttribute("users",adminService.getAllUsersForAdmin());
		return "admin/registeredusers";
	}
	
	@ResponseBody
	@RequestMapping(value="/update_user_status/{id}",method=RequestMethod.GET)
	public HashMap<String,String> updateUserStatusFromAdmin(@PathVariable("id")long id) {
		HashMap<String,String> map = new HashMap<>();
		map.put("status", adminService.updateUserStatusById(id));
		return map;
	}

	@RequestMapping(value="/user_detail/{id}" , method = RequestMethod.GET)
	public String goToMyProfile(@PathVariable("id")long id,Model model) {
		String user = userDao.findOne(id).getLogin();
		model = userService.getProfileData(model, user);
		return "admin/profilePage";
	}
}
