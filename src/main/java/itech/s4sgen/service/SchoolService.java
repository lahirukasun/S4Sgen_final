package itech.s4sgen.service;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itech.s4sgen.dao.MaintenanceStaffDao;
import itech.s4sgen.dao.SchoolBusDao;
import itech.s4sgen.dao.UtilityBillDao;
import itech.s4sgen.dto.SchoolBusDto;
import itech.s4sgen.dto.StaffDto;
import itech.s4sgen.dto.UtilityBillDto;
import itech.s4sgen.models.MaintenanceStaff;
import itech.s4sgen.models.SchoolBus;
import itech.s4sgen.models.User;
import itech.s4sgen.models.UtilityBill;
import itech.s4sgen.utils.HelpingClass;

@Service
public class SchoolService {
	
	@Autowired
	private MaintenanceStaffDao maintenanceStaffDao;
	
	@Autowired
	private SchoolBusDao schoolBusDao;
	
	@Autowired
	private UtilityBillDao utilityBillDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void saveMaintenanceStaff(StaffDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		MaintenanceStaff staff;
		if(dto.getId()==0)
			staff = new MaintenanceStaff();
		else
			staff = maintenanceStaffDao.findOne(dto.getId());
		staff.setMemberId(dto.getMemberId());
		staff.setName(dto.getName());
		staff.setCellNo(dto.getMobile());
		staff.setDuty(dto.getDuty());
		staff.setJoiningDate(HelpingClass.stringToDate(dto.getJoinDate()));
		staff.setUser(user);
		maintenanceStaffDao.save(staff);
	}
	
	public void saveSchoolBus(SchoolBusDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		SchoolBus bus;
		if(dto.getId()==0)
			bus = new SchoolBus();
		else
			bus = schoolBusDao.findOne(dto.getId());
		bus.setBusRegNo(dto.getBusRegNo());
		bus.setDriverName(dto.getDriverName());
		bus.setDriverMobile(dto.getDriverMobile());
		bus.setRoute(dto.getRoute());
		bus.setStudents(dto.getStudents());
		bus.setUser(user);
		
		schoolBusDao.save(bus);
	}
	
	public void saveUtilityBill(UtilityBillDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		UtilityBill bill;
		if(dto.getId()==0)
			bill = new UtilityBill();
		else
			bill = utilityBillDao.findOne(dto.getId());
		bill.setBillId(dto.getBillId());
		bill.setBillPayer(dto.getBillPayer());
		bill.setUtility(dto.getUtility());
		bill.setAmount(dto.getAmount());
		bill.setPaidDate(HelpingClass.stringToDate(dto.getPaidDate()));
		bill.setUser(user);
		utilityBillDao.save(bill);
	}
	
	public List<StaffDto> getUserStaffByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return maintenanceStaffDao.findAllByUser(user).stream().map(SchoolService::convertToStaffDto).collect(Collectors.toList());
	}
	
	public List<SchoolBusDto> getUserBusesByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return schoolBusDao.findAllByUser(user).stream().map(SchoolService::convertToSchoolBusDto).collect(Collectors.toList());
	}
	
	public List<UtilityBillDto> getUserUtilityBillsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return utilityBillDao.findAllByUser(user).stream().map(SchoolService::convertToUtilityBillDto).collect(Collectors.toList());
	}
	
	public StaffDto getStaffDtoById(long id) {
		MaintenanceStaff staff = maintenanceStaffDao.findOne(id);
		StaffDto dto = new StaffDto();
		dto.setId(staff.getId());
		dto.setMemberId(staff.getMemberId());
		dto.setName(staff.getName());
		dto.setDuty(staff.getDuty());
		dto.setMobile(staff.getCellNo());
		dto.setJoinDate(staff.getJoiningDate()+"");
		return dto;
	}
	
	public SchoolBusDto getSchoolBusDtoById(long id) {
		SchoolBus bus = schoolBusDao.findOne(id);
		SchoolBusDto dto = new SchoolBusDto();
		dto.setId(bus.getId());
		dto.setBusRegNo(bus.getBusRegNo());
		dto.setDriverName(bus.getDriverName());
		dto.setDriverMobile(bus.getDriverMobile());
		dto.setRoute(bus.getRoute());
		dto.setStudents(bus.getStudents());
		return dto;
	}
	
	public UtilityBillDto getUtilityBillDtoById(long id) {
		UtilityBill bill = utilityBillDao.findOne(id);
		UtilityBillDto dto = new UtilityBillDto();
		dto.setId(bill.getId());
		dto.setBillId(bill.getBillId());
		dto.setBillPayer(bill.getBillPayer());
		dto.setAmount(bill.getAmount());
		dto.setUtility(bill.getUtility());
		dto.setPaidDate(bill.getPaidDate()+"");
		return dto;
	}
	
	public static StaffDto convertToStaffDto(MaintenanceStaff staff) {
		StaffDto dto = new StaffDto();
		dto.setId(staff.getId());
		dto.setMemberId(staff.getMemberId());
		dto.setName(staff.getName());
		dto.setDuty(staff.getDuty());
		dto.setMobile(staff.getCellNo());
		dto.setJoinDate(staff.getJoiningDate()+"");
		return dto;
	}
	
	public static SchoolBusDto convertToSchoolBusDto(SchoolBus bus) {
		SchoolBusDto dto = new SchoolBusDto();
		dto.setId(bus.getId());
		dto.setBusRegNo(bus.getBusRegNo());
		dto.setDriverName(bus.getDriverName());
		dto.setDriverMobile(bus.getDriverMobile());
		dto.setRoute(bus.getRoute());
		dto.setStudents(bus.getStudents());
		return dto;
	}
	
	public static UtilityBillDto convertToUtilityBillDto(UtilityBill bill) {
		UtilityBillDto dto = new UtilityBillDto();
		dto.setId(bill.getId());
		dto.setBillId(bill.getBillId());
		dto.setBillPayer(bill.getBillPayer());
		dto.setAmount(bill.getAmount());
		dto.setUtility(bill.getUtility());
		dto.setPaidDate(bill.getPaidDate()+"");
		return dto;
	}

}
