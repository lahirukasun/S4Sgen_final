package itech.s4sgen.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import itech.s4sgen.dao.RoleDao;
import itech.s4sgen.dao.SystemDetailDao;
import itech.s4sgen.dao.UserDao;
import itech.s4sgen.dto.AppointmentDto;
import itech.s4sgen.dto.CustomerDto;
import itech.s4sgen.dto.DoctorDto;
import itech.s4sgen.dto.EmployeeDto;
import itech.s4sgen.dto.JourneyDto;
import itech.s4sgen.dto.ManagementSystemDto;
import itech.s4sgen.dto.OrderDto;
import itech.s4sgen.dto.PatientDto;
import itech.s4sgen.dto.SchoolBusDto;
import itech.s4sgen.dto.SettingDto;
import itech.s4sgen.dto.StaffDto;
import itech.s4sgen.dto.StudentDto;
import itech.s4sgen.dto.SubManagementSystemDto;
import itech.s4sgen.dto.SystemDetailDto;
import itech.s4sgen.dto.SystemFeaturesDto;
import itech.s4sgen.dto.TeacherDto;
import itech.s4sgen.dto.TicketDto;
import itech.s4sgen.dto.UserDto;
import itech.s4sgen.dto.UtilityBillDto;
import itech.s4sgen.models.ManagementSystem;
import itech.s4sgen.models.Role;
import itech.s4sgen.models.SubManagementSystem;
import itech.s4sgen.models.SystemDetail;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ManagementSystemService managementSystemService;
	
	@Autowired
	private SubManagementSystemService subManagementSystemService;
	
	@Autowired
	private SystemFeaturesService systemFeaturesService;
	
	@Autowired
	private SystemDetailDao systemDetailDao;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private SchoolService schoolService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private BookingService bookingService;
	
	
	public User getUserByLogin(String userName) {
		return userDao.findByLogin(userName);
	}
	
	public String loginSuperUser(String userName) {
		User user = userDao.findOneByLoginAndRoles(userName, roleDao.findByRole("SUPER ADMIN"));
		if(user!=null)
			return "DONE";
		return "ERROR";
	}
	
	public Object[] loginUser(String userName,Model model) {
		User user = userDao.findByLogin(userName);
		Object[] objs;

		SystemDetail detail = systemDetailDao.findOneByUser(user);
		
		if(user.getManagementSystem()==null) {
			objs = new Integer[1];
			objs[0] = 1;
			return objs;
		}
		else if(user.getSubManagementSystem()==null) {
			objs = new Object[2];
			objs[0] = new Integer(2);
			objs[1] = user.getManagementSystem();
			return objs;
		}
		else if(user.getFeatures()==null) {
			objs = new Object[2];
			objs[0] = new Integer(3);
			objs[1] = user.getSubManagementSystem();
			return objs;
		}
		else if(detail == null){
			objs = new Object[2];
			objs[0] = new Integer(4);
			objs[1] = new SystemDetailDto();
			return objs;
		}
		else{
			Object[] pageData = selectUserStartPage(user, model);
			objs = new Object[3];
			objs[0] = 5;
			objs[1] = (Model)pageData[0];
			objs[2] = (String)pageData[1];
			return objs;
		}
	}

	public String registerUser(UserDto user) {
		User userr = userDao.findByLogin(user.getLogin());
		
		if(userr!=null)
			return "EXISTS";
		
		User u = new User();
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setLogin(user.getLogin());
		u.setEmail(user.getEmail());
		u.setPassword(HelpingClass.cryptWithMD5(user.getPassword()));
		u.setCreateDate(new Date());
		Role userRole = roleDao.findByRole("ADMIN");
        u.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		u.setActive(true);
		
		System.out.println("gonna saver user...");

		User us = userDao.save(u);

		if(us!=null)
			return "DONE";
		else
			return "ERROR";
	}

	public Object[] selectUserStartPage(User user, Model model) {
		SubManagementSystem sms = user.getSubManagementSystem();
		String featureString = user.getFeatures();
		System.out.println("features: " + featureString);
		int index = Character.getNumericValue(featureString.charAt(0));
		int smsId = (int)sms.getId();
		System.out.println("sms: " + smsId + " index: " + index);
		switch(smsId) {
		case 1:{
			return getStartPageForStudentSystem(user.getLogin(), index,model);
		}
		case 2:{
			return getStartPageForTeacherSystem(user.getLogin(), index,model);
		}
		case 3:{
			if(featureString.contains("-"))
				index = 1;
			else if(featureString.contains("1,2"))
				index = 2;
			return getStartPageForSchoolSystem(user.getLogin(), index,model);
		}
		case 4:{
			return getStartPageForPatientSystem(user.getLogin(), index,model);
		}
		case 5:{
			return getStartPageForDoctorSystem(user.getLogin(), index,model);
		}
		case 6:{
			return getStartPageForAppointmentsSystem(user.getLogin(), index,model);
		}
		case 7:{
			return getStartPageForCustomerSystem(user.getLogin(), index,model);
		}
		case 8:{
			return getStartPageForRestaurantOrderSystem(user.getLogin(), index,model);
		}
		case 9:{
			return getStartPageForEmployeesSystem(user.getLogin(), index,model);
		}
		case 10:{
			return getStartPageForTicketSystem(user.getLogin(), index,model);
		}
		case 11:{
			return getStartPageForBookingSystem(user.getLogin(), index,model);
		}
		case 12:{
			return getStartPageForEmployeesSystem(user.getLogin(), index,model);
		}
		}
		return null;
	}
	
	public Model getProfileData(Model model, String login) {
		User user = userDao.findByLogin(login);
		ManagementSystemDto mDto = ManagementSystemService.convertToDto(user.getManagementSystem());
		SubManagementSystemDto smDto = SubManagementSystemService.convertToDto(user.getSubManagementSystem());
		String featureString = user.getFeatures();
		List<SystemFeaturesDto> sfDtos = systemFeaturesService.getAllBySubManagementSystem(user.getSubManagementSystem());
		Iterator<SystemFeaturesDto> it = sfDtos.iterator();
		System.out.println("List: " + sfDtos.size());
		int count = 1;
		while(it.hasNext()) {
			SystemFeaturesDto sfDto = it.next();
			sfDto.setId(count);
			System.out.println("id is: " + sfDto.getId());
			if(!featureString.contains(String.valueOf(sfDto.getId())))
				it.remove();
			count++;
		}
		
		System.out.println("features: " + featureString);
		System.out.println("List: " + sfDtos.size());
		
		model.addAttribute("userName", user.getFirstName() + " " + user.getLastName());
		model.addAttribute("managementSystem", mDto);
		model.addAttribute("subManagementSystem", smDto);
		model.addAttribute("systemFeatures", sfDtos);
		
		return model;
		
	}
	
	public String updateUserPassword(SettingDto setting, String userName) {
		User user = getUserByLogin(userName);
		if(user.getPassword().equals(HelpingClass.cryptWithMD5(setting.getPassword()))) {
			user.setPassword(HelpingClass.cryptWithMD5(setting.getNewPassword()));
			User u = userDao.save(user);
			if(u!=null)
				return "DONE";
		}else
			return "INCORRECT";
		
		return "ERROR";
	}
	
	public List<SubManagementSystemDto> saveUserSelectedManagementSystem(long managementSystemId, String userName) {
		User user = userDao.findByLogin(userName);
		ManagementSystem managementSystem = managementSystemService.getOneById(managementSystemId);
		user.setManagementSystem(managementSystem);
		userDao.save(user);
		List<SubManagementSystemDto> subManagementSystems = subManagementSystemService.getAllByManagementSystem(managementSystem);
		return subManagementSystems;
	}
	
	public List<SystemFeaturesDto> saveUserSelectedSubManagementSystem(long subManagementSystemId, String userName) {
		User user = userDao.findByLogin(userName);
		SubManagementSystem subManagementSystem = subManagementSystemService.getOneById(subManagementSystemId);
		user.setSubManagementSystem(subManagementSystem);
		userDao.save(user);
		List<SystemFeaturesDto> subManagementSystems = systemFeaturesService.getAllBySubManagementSystem(subManagementSystem);
		return subManagementSystems;
	}
	
	public String saveUserSelectedSystemFeatures(int[] features,String userName) {
		User user = userDao.findByLogin(userName);
		StringBuilder featureString = new StringBuilder();
		if(user.getSubManagementSystem().getId()==3) {
			for(int i=0 ; i<features.length; i++) {
				System.out.println(features[i]);
				switch(features[i]+1) {
				case 1:
					featureString.append("1,2,3,4,5-");
					break;
				case 2:
					featureString.append("1,2,3,4,5,");
					break;
				case 3:
					featureString.append("6,");
					break;
				case 4:
					featureString.append("7,");
					break;
				case 5:
					featureString.append("8");
					break;
				}
			}
		}else {
			for(int i=0 ; i<features.length; i++) {
				System.out.println(features[i]);
				if(i==(features.length-1))
					featureString.append(features[i]+1);
				else
					featureString.append((features[i]+1) + ",");
			}
		}
		
		user.setFeatures(featureString.toString());
		userDao.save(user);
		return "systemdetail";
	
	}
	
	public Object[] saveUserSystemDetails(SystemDetailDto dto, String userName, Model model) {
		User user = userDao.findByLogin(userName);
		
		SystemDetail detail = new SystemDetail();
		detail.setProjectName(dto.getProjectName());
		detail.setDomainName(dto.getDomainName());
		detail.setMobile(dto.getMobile());
		detail.setUser(user);
		
		MultipartFile file = dto.getLogo();
		
		try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            FileOutputStream fos = new FileOutputStream("C:/temp/"+file.getOriginalFilename());
            fos.write(bytes);
            fos.close();
            //Path path = Paths.get("/images/" + file.getOriginalFilename());
            //Files.write(path, bytes);
            File oldfile = new File("C:/temp/" + file.getOriginalFilename());
    		File newfile = new File(userName +"_logo.png");

    		detail.setUserLogo(newfile.getName());
    		
    		if(oldfile.renameTo(newfile)){
    			System.out.println("Rename succesful");
    		}else{
    			System.out.println("Rename failed");
    		}
            

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		systemDetailDao.save(detail);
		
		return selectUserStartPage(user, model);
	}
	
	public Object[] getStartPageForStudentSystem(String login, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:
		{
			model.addAttribute("isSchool", false);
			model.addAttribute("studentsList", studentService.getStudentsOfUserByUserName(login));
			model.addAttribute("programs",HelpingClass.getStudyProgramsList());
			model.addAttribute("student", new StudentDto());
			landingPageData[0] = model;
			landingPageData[1] = "studentsystem/studentmanagementsystem";
			return landingPageData;
		}
		case 2:
		{
			model.addAttribute("semesters",HelpingClass.getSemestersList());
			model.addAttribute("marksList", studentService.getUserStudentsExamsMarksByUserName(login));
			model.addAttribute("courses",HelpingClass.getStudyCoursesList());
			model.addAttribute("student", new StudentDto());
			landingPageData[0] = model;
			landingPageData[1] = "studentsystem/examsmarkingsystem";
			return landingPageData;
		}
		case 3:
		{
			model.addAttribute("months",HelpingClass.getMonthsList());
			model.addAttribute("student", new StudentDto());
			model.addAttribute("attendanceList", studentService.getUserStudentsAttendanceByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "studentsystem/attendancesystem";
			return landingPageData;
		}
		case 4:
		{
			model.addAttribute("semesters",HelpingClass.getSemestersList());
			model.addAttribute("student", new StudentDto());
			model.addAttribute("feeList", studentService.getUserStudentsFeeByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "studentsystem/feemanagementsystem";
			return landingPageData;
		}
		case 5:
		{
			model.addAttribute("studentsData", studentService.getUserStudentsDataByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "studentsystem/generatestudentreport";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForTeacherSystem(String login, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:
		{
			model.addAttribute("teachersList", teacherService.getTeachersOfUserByUserName(login));
			model.addAttribute("courses",HelpingClass.getStudyCoursesList());
			model.addAttribute("teacher", new TeacherDto());
			landingPageData[0] = model;
			landingPageData[1] = "teachersystem/teachermanagementsystem";
			return landingPageData;
		}
		case 2:
		{
			model.addAttribute("dutyList", teacherService.getTeachersDutiesOfUserByUserName(login));
			model.addAttribute("courses",HelpingClass.getStudyCoursesList());
			model.addAttribute("teacher", new TeacherDto());
			landingPageData[0] = model;
			landingPageData[1] = "teachersystem/teacherexamduties";
			return landingPageData;
		}
		case 3:
		{
			model.addAttribute("timeTable",teacherService.getTeachersLectureScheduleOfUserByUserName(login));
			model.addAttribute("courses",HelpingClass.getStudyCoursesList());
			model.addAttribute("teacher", new TeacherDto());
			landingPageData[0] = model;
			landingPageData[1] = "teachersystem/teachertimetable";
			return landingPageData;
		}
		case 4:
		{
			model.addAttribute("months",HelpingClass.getMonthsList());
			model.addAttribute("teacher", new TeacherDto());
			model.addAttribute("salaryList", teacherService.getTeachersSalaryOfUserByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "teachersystem/salarymanagement";
			return landingPageData;
		}
		case 5:
		{
			model.addAttribute("teacherData", teacherService.getTeachersSalaryOfUserByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "teachersystem/teacherreport";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForSchoolSystem(String login, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:
		{
			landingPageData = getStartPageForStudentSystem(login, pageIndex,model);
			landingPageData[0] = ((Model)landingPageData[0]).addAttribute("isSchool", true);
			return landingPageData;
		}
		case 2:
		{
			landingPageData = getStartPageForTeacherSystem(login,pageIndex,model);
			landingPageData[0] = ((Model)landingPageData[0]).addAttribute("isSchool", true);
			return landingPageData;
		}
		case 6:
		{
			model.addAttribute("staff",new StaffDto());
			model.addAttribute("dutyList", HelpingClass.getDutiesList());
			model.addAttribute("staffList" , schoolService.getUserStaffByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "schoolsystem/maintenancestaffmanagement";
			return landingPageData;
		}
		case 7:
		{
			model.addAttribute("bus", new SchoolBusDto());
			model.addAttribute("routeList", HelpingClass.getRoutesList());
			model.addAttribute("busesList", schoolService.getUserBusesByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "schoolsystem/schoolbusmanagement";
			return landingPageData;
		}
		case 8:
		{
			model.addAttribute("bill", new UtilityBillDto());
			model.addAttribute("billsList", schoolService.getUserUtilityBillsByUserName(login));
			model.addAttribute("services", HelpingClass.getServicesList());
			landingPageData[0] = model;
			landingPageData[1] = "schoolsystem/utilitybillsmanagement";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForPatientSystem(String login, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:{
			model.addAttribute("patient",new PatientDto());
			model.addAttribute("patientsList", patientService.getUserPatientsByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "patientsystem/patientmanagementsystem";
			return landingPageData;
		}
		case 2:{
			model.addAttribute("patient",new PatientDto());
			model.addAttribute("appointmentList", patientService.getUserAdmittedPatientAppointmensByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "patientsystem/manageappointments";
			return landingPageData;
		}
		case 3:{
			model.addAttribute("patientsData", patientService.getUserPatientsDataByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "patientsystem/patientreport";
			return landingPageData;
		}
		case 4:{
			model.addAttribute("payment", new PatientDto());
			model.addAttribute("paymentList" , patientService.getUserPatientPaymentsByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "patientsystem/payments";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForDoctorSystem(String login, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:{
			model.addAttribute("doctor", new DoctorDto());
			model.addAttribute("doctorsList",doctorService.getUserDoctorByUserName(login) );
			model.addAttribute("specialities", HelpingClass.getDoctorsSpecialitiesList());
			landingPageData[0] = model;
			landingPageData[1] = "doctorsystem/doctormanagementsystem";
			return landingPageData;
		}
		case 2:{
			model.addAttribute("appointments", doctorService.getUserAppointmentsByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "doctorsystem/manageappointments";
			return landingPageData;
		}
		case 3:{
			model.addAttribute("doctor", new DoctorDto());
			model.addAttribute("attendanceList", doctorService.getUserDoctorsAttendanceByUserName(login));
			model.addAttribute("months",HelpingClass.getMonthsList());
			landingPageData[0] = model;
			landingPageData[1] = "studentsystem/attendancesystem";
			return landingPageData;
		}
		case 4:{
			model.addAttribute("months",HelpingClass.getMonthsList());
			model.addAttribute("doctor", new DoctorDto());
			model.addAttribute("salaryList", doctorService.getDoctorsSalaryOfUserByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "doctorsystem/salarymanagement";
			return landingPageData;
		}
		case 5:{
			model.addAttribute("dataList", doctorService.getDoctorsSalaryOfUserByUserName(login));
			landingPageData[0] = model;
			landingPageData[1] = "doctorsystem/generatedoctorreport";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForAppointmentsSystem(String userName, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:{
			model.addAttribute("appointment", new AppointmentDto());
			model.addAttribute("allAppList",appointmentService.getUserAppointmentsByUserName(userName));
			model.addAttribute("activeAppList",appointmentService.getUserActiveAppointmentsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "appointmentsystem/manageappointments";
			return landingPageData;
		}
		case 2:{
			model.addAttribute("payment", new AppointmentDto());
			model.addAttribute("payments",appointmentService.getUserCheckupFeeByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "appointmentsystem/payments";
			return landingPageData;
		}
		case 3:{
			model.addAttribute("data", appointmentService.getAppointmentsDataByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "appointmentsystem/generatepatientreport";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForCustomerSystem(String userName, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
			case 1:{
				model.addAttribute("customer", new CustomerDto());
				model.addAttribute("types", HelpingClass.getMemberShipTypeList());
				model.addAttribute("customers", customerService.getUserCustomersByUserName(userName));
				landingPageData[0] = model;
				landingPageData[1] = "customersystem/customermanagementsystem";
				return landingPageData;
			}
			case 2:{
				model.addAttribute("order", new CustomerDto());
				model.addAttribute("orders", customerService.getUserCustomerOrdersByUserName(userName));
				landingPageData[0] = model;
				landingPageData[1] = "customersystem/ordersystem";
				return landingPageData;
			}
			case 3:{
				model.addAttribute("payment", new CustomerDto());
				model.addAttribute("payments", customerService.getUserOrderPaymentsByUserName(userName));
				landingPageData[0] = model;
				landingPageData[1] = "customersystem/paymentsystem";
				return landingPageData;
			}
			case 4:{
				model.addAttribute("customerData", customerService.getUserCustomersDataByUserName(userName));
				landingPageData[0] = model;
				landingPageData[1] = "customersystem/generatecustomerreport";
				return landingPageData;
			}
		}
		return null;
	}
	
	public Object[] getStartPageForRestaurantOrderSystem(String userName, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:{
			model.addAttribute("order",new OrderDto());
			model.addAttribute("ordersList",ordersService.getUserOrdersByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "ordersystem/ordersystem";
			return landingPageData;
		}
		case 2:{
			model.addAttribute("payment",new OrderDto());
			model.addAttribute("paymentsList",ordersService.getUserOrderPaymentsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "ordersystem/paymentsystem";
			return landingPageData;
		}
		case 3:{
			model.addAttribute("reviewList",ordersService.getUserOrdersReviews(userName));
			landingPageData[0] = model;
			landingPageData[1] = "ordersystem/ordersreviews";
			return landingPageData;
		}
		case 4:{
			landingPageData[0] = model;
			landingPageData[1] = "ordersystem/generateorderreport";
			return landingPageData;
		}
		}
		return null;
	}
	
	public Object[] getStartPageForEmployeesSystem(String userName, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
			case 1:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("employeesList",employeeService.getUserEmployeesByUserName(userName) );
				model.addAttribute("jobsList", HelpingClass.getRestaurantJobsList());
				landingPageData[0] = model;
				landingPageData[1] = "employeesystem/employeemanagementsystem";
				return landingPageData;
			}
			case 2:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("attendanceList", employeeService.getUserEmployeesAttendanceByUserName(userName));
				model.addAttribute("months",HelpingClass.getMonthsList());
				landingPageData[0] = model;
				landingPageData[1] = "employeesystem/attendancesystem";
				return landingPageData;
			}
			case 3:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("salaryList", employeeService.getUserEmployeesSalaryByUserName(userName));
				model.addAttribute("months",HelpingClass.getMonthsList());
				landingPageData[0] = model;
				landingPageData[1] = "employeesystem/salarymanagementsystem";
				return landingPageData;
			}
			case 4:{
				model.addAttribute("employee", new EmployeeDto());
				model.addAttribute("bonusList", employeeService.getEmployeesBonusesDetailByUserName(userName));
				landingPageData[0] = model;
				landingPageData[1] = "employeesystem/bonusesmanagement";
				return landingPageData;
			}
			case 5:{
				model.addAttribute("dataList", employeeService.getUserEmployeesDataByUserName(userName));
				landingPageData[0] = model;
				landingPageData[1] = "employeesystem/generateemployeereport";
				return landingPageData;
			}
		}
		return null;
	}

	public Object[] getStartPageForTicketSystem(String userName, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:{
			model.addAttribute("ticket",new TicketDto());
			landingPageData[0] = model;
			landingPageData[1] = "ticketsystem/addtickets";
			return landingPageData;
		}
		case 2:{
			model.addAttribute("tickets",ticketService.getUserTicketsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] ="ticketsystem/availabletickets";
			return landingPageData;
		}
		case 3:{
			model.addAttribute("ticket",new TicketDto());
			model.addAttribute("tickets",ticketService.getUserReservationsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "ticketsystem/managereservation";
			return landingPageData;
		}
		case 4:{
			model.addAttribute("ticket",new TicketDto());
			model.addAttribute("payments",ticketService.getUserTicketPaymentsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "ticketsystem/makepayment";
			return landingPageData;
		}
		case 5:{
			model.addAttribute("data",ticketService.getUserTicketsDataByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "ticketsystem/generatereport";
			return landingPageData;
		}
		}
		return null;
	}

	public Object[] getStartPageForBookingSystem(String userName, int pageIndex,Model model) {
		
		Object[] landingPageData = new Object[2];
		switch(pageIndex) {
		case 1:{
			model.addAttribute("journey",new JourneyDto());
			landingPageData[0] = model;
			landingPageData[1] = "bookingsystem/addjourney";
			return landingPageData;
		}
		case 2:{
			model.addAttribute("journeys",bookingService.getUserJourneysByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "bookingsystem/availablejourneys";
			return landingPageData;
		}
		case 3:{
			model.addAttribute("ticket",new TicketDto());
			model.addAttribute("tickets",bookingService.getUserReservationsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "bookingsystem/managereservation";
			return landingPageData;
		}
		case 4:{
			model.addAttribute("ticket",new TicketDto());
			model.addAttribute("payments",bookingService.getUserTicketPaymentsByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "bookingsystem/makepayment";
			return landingPageData;
		}
		case 5:{
			model.addAttribute("data",bookingService.getUserTicketsDataByUserName(userName));
			landingPageData[0] = model;
			landingPageData[1] = "booking/generatereport";
			return landingPageData;
		}
		}
		return null;
	}
}
