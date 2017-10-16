package itech.s4sgen.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itech.s4sgen.dao.RoleDao;
import itech.s4sgen.dao.UserDao;
import itech.s4sgen.dto.UserDto;
import itech.s4sgen.models.Role;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class AdminService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@PostConstruct
	private void checkIfSuperAdminExists() {
		User user = userDao.findOneByLoginAndRoles("admin", roleDao.findByRole("SUPER ADMIN"));
		if(user==null) {
			Role role = roleDao.findByRole("SUPER ADMIN");
			Role r = null;
			if(role==null) {
				System.out.println("role null");
				role = new Role();
				role.setRole("SUPER ADMIN");
				r = roleDao.save(role);
			}else
				r = new Role(role);
			User admin = new User();
			admin.setLogin("admin");
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setPassword(HelpingClass.cryptWithMD5("admin"));
			admin.setCreateDate(new Date());
			admin.setEmail("admin@s4sgen.com");
			System.out.println("role : " + r);
			admin.setRoles(new HashSet<Role>(Arrays.asList(r)));
			admin.setActive(true);
			userDao.save(admin);
			System.out.println("Super Admin did not Exist and Added now !! :)");
		}else
			return;
	}
	
	public List<UserDto> getAllUsersForAdmin(){
		List<User> users = userDao.findAllUserByRoles(roleDao.findByRole("ADMIN"));
		List<UserDto> userList = new ArrayList<>();
		for(User u: users) {
			UserDto dto = new UserDto();
			dto.setId(u.getId());
			dto.setFirstName(u.getFirstName());
			dto.setLastName(u.getLastName());
			dto.setLogin(u.getLogin());
			dto.setEmail(u.getEmail());
			dto.setCreateDate(u.getCreateDate());
			dto.setActive(u.isActive());
			dto.setManaementSystem(u.getManagementSystem());
			dto.setSubManagementSystem(u.getSubManagementSystem());
			userList.add(dto);
		}
		return userList;
	}
	
	public String updateUserStatusById(long id) {
		User user = userDao.findOne(id);
		if(user.isActive())
			user.setActive(false);
		else
			user.setActive(true);
		User u = userDao.save(user);
		if(u!=null)
			return "DONE";
		return "ERROR";
	}

}
