package itech.s4sgen.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import itech.s4sgen.dao.EmployeeAttendanceDao;
import itech.s4sgen.dao.EmployeeBonusDao;
import itech.s4sgen.dao.EmployeeDao;
import itech.s4sgen.dao.EmployeeSalaryDao;
import itech.s4sgen.dto.EmployeeDto;
import itech.s4sgen.models.Employee;
import itech.s4sgen.models.EmployeeAttendance;
import itech.s4sgen.models.EmployeeBonus;
import itech.s4sgen.models.EmployeeSalary;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeAttendanceDao employeeAttendanceDao;
	
	@Autowired
	private EmployeeSalaryDao employeeSalaryDao;
	
	@Autowired
	private EmployeeBonusDao employeeBonusDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		return false;
	}
	
	public void saveEmployee(EmployeeDto dto , String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		Employee emp;
		if(dto.getId()==0)
			emp = new Employee();
		else 
			emp = employeeDao.findOne(dto.getId());
		emp.setName(dto.getName());
		emp.setMobile(dto.getMobile());
		emp.setEmail(dto.getEmail());
		emp.setJob(dto.getJob());
		emp.setJobDuration(dto.getJobDuration());
		emp.setJoiningDate(HelpingClass.stringToDate(dto.getJoiningDate()));
		emp.setUser(user);
		employeeDao.save(emp);
	}
	
	public void saveEmployeeAttendance(EmployeeDto dto, String login) {
		Employee st = employeeDao.findOne(Long.valueOf(dto.getEmployeeId()));
		EmployeeAttendance sa;
		if(dto.getId()==0)
			sa = new EmployeeAttendance();
		else
			sa = employeeAttendanceDao.findOne(dto.getId());
		sa.setEmployeeId(dto.getEmployeeId());
		sa.setMonth(dto.getMonth());
		sa.setAttendance(dto.getAttendance());
		sa.setPerformance(dto.getPerformance());
		sa.setEmployee(st);
		employeeAttendanceDao.save(sa);
	}
	
	public void saveEmployeeSalary(EmployeeDto dto, String userName) {
		Employee employee = employeeDao.findOne(Long.valueOf(dto.getEmployeeId()));
		EmployeeSalary salary;
		if(dto.getId()==0)
			salary = new EmployeeSalary() ;
		else
			salary = employeeSalaryDao.findOne(dto.getId());
		salary.setEmployeeId(dto.getEmployeeId());
		salary.setMonth(dto.getMonth());
		salary.setSalary(dto.getSalary());
		salary.setEmployee(employee);
		employeeSalaryDao.save(salary);
	}
	
	public void saveEmployeeBonus(EmployeeDto dto, String userName) {
		EmployeeBonus bonus;
		if(dto.getId()==0)
			bonus = new EmployeeBonus();
		else
			bonus = employeeBonusDao.findOne(dto.getId());
		bonus.setEmployeeId(dto.getEmployeeId());
		bonus.setBonus(dto.getSalary());
		bonus.setEmployee(employeeDao.findOne((long)dto.getEmployeeId()));
		employeeBonusDao.save(bonus);
	}
	
	public List<EmployeeDto> getUserEmployeesByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return employeeDao.findAllByUser(user).stream().map(EmployeeService::convertToEmployeeDtoFromEmployee).collect(Collectors.toList());
	}

	public List<EmployeeDto> getUserEmployeesSalaryByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Employee> employees = employeeDao.findAllByUser(user);
		List<EmployeeDto> dtoList = new ArrayList<>();
		for(Employee employee : employees){
			List<EmployeeSalary> salary = employeeSalaryDao.findAllByEmployee(employee);
			EmployeeDto dto = new EmployeeDto();
			dto.setEmployeeId((int)employee.getId());
			dto.setId(employee.getId());
			dto.setName(employee.getName());
			dto.setMonths(salary.size());
			dto.setSalary((long)salary.stream().mapToDouble(s->s.getSalary()).sum());
			dtoList.add(dto);
		}
		return dtoList;
	}

	public List<EmployeeDto> getUserEmployeesAttendanceByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Employee> employees = employeeDao.findAllByUser(user);
		List<EmployeeDto> dtoList = new ArrayList<>();
		if(employees.size()>0) {
			for(Employee employee : employees){
				List<EmployeeAttendance> att = employeeAttendanceDao.findAllByEmployee(employee);
				if(att.size()>0)
					dtoList.add(convertToEmployeeDtoFromEmployeeAttendance(att));
			}
		}
		
		return dtoList;
	}
	
	public List<EmployeeDto> getEmployeesSalaryDetailByEmployeeId(long id){
		Employee employee = employeeDao.findOne(id);
		List<EmployeeDto> dtoList = new ArrayList<>();
		List<EmployeeSalary> salary = employeeSalaryDao.findAllByEmployee(employee);
		for(EmployeeSalary ts : salary) {
			EmployeeDto dto = new EmployeeDto();
			dto.setId(ts.getId());
			dto.setName(employee.getName().toUpperCase());
			dto.setMonth(ts.getMonth());
			dto.setSalary(ts.getSalary());
			dtoList.add(dto);
		}

		return dtoList;
	}
	
	public Map<Integer,Double> getEmployeesBonusesDetailByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		Map<Integer,Double> bonus = new HashMap<>();
		List<Employee> employees = employeeDao.findAllByUser(user);
		for(Employee emp : employees) {
			List<EmployeeBonus> bonuses = employeeBonusDao.findAllByEmployee(emp);
			bonus.put((int) emp.getId(), bonuses.stream().mapToDouble(b->b.getBonus()).sum());
		}
		return bonus;
	}
	
	public EmployeeDto getEmployeeById(long id) {
		return convertToEmployeeDtoFromEmployee(employeeDao.findOne(id));
	}
	
	public List<EmployeeDto> getEmployeeAttendanceDetail(long employeeId) {
		Employee employee = employeeDao.findOne(employeeId);
		List<EmployeeAttendance> saList = employeeAttendanceDao.findAllByEmployee(employee);
		List<EmployeeDto> dtoList = new ArrayList<>();
		if(saList!=null && saList.size()>0) {
			for(EmployeeAttendance sa : saList) {
				EmployeeDto dto = new EmployeeDto();
				dto.setId(sa.getId());
				dto.setName(employee.getName() + " " + employee.getId());
				dto.setMonth(sa.getMonth());
				dto.setAttendance(sa.getAttendance());
				dto.setPercentage(sa.getPerformance());
				dto.setPercentage(((double)sa.getAttendance()/(double)22)*100);
				dtoList.add(dto);
			}
			return dtoList;
		}
		return null;
	}

	public EmployeeDto getEmployeeAttendanceById(long id) {
		EmployeeAttendance emp = employeeAttendanceDao.findOne(id);
		EmployeeDto dto = new EmployeeDto();
		dto.setId(emp.getId());
		dto.setEmployeeId(emp.getEmployeeId());
		dto.setAttendance(emp.getAttendance());
		dto.setPerformance(emp.getPerformance());
		dto.setPercentage((emp.getAttendance()/(double)(25))*100);

		return dto;
	}

	public EmployeeDto getEmployeeSalaryById(long id) {
		EmployeeSalary emp = employeeSalaryDao.findOne(id);
		EmployeeDto dto = new EmployeeDto();
		dto.setId(emp.getId());
		dto.setEmployeeId(emp.getEmployeeId());
		dto.setMonth(emp.getMonth());
		dto.setSalary(emp.getSalary());

		return dto;
	}

	public EmployeeDto getEmployeeBonusById(long id) {
		EmployeeDto dto = new EmployeeDto();
		EmployeeBonus bonus = employeeBonusDao.findOne(id);
		dto.setEmployeeId(bonus.getEmployeeId());
		dto.setSalary(bonus.getBonus());
		return dto;
	}
	
	public List<EmployeeDto> getUserEmployeesDataByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Employee> employees = employeeDao.findAllByUser(user);
		List<EmployeeDto> dtoList = new ArrayList<>();
		for(Employee employee : employees){
			List<EmployeeSalary> salary = employeeSalaryDao.findAllByEmployee(employee);
			List<EmployeeAttendance> attendance = employeeAttendanceDao.findAllByEmployee(employee);
			EmployeeDto dto = new EmployeeDto();
			dto.setEmployeeId((int)employee.getId());
			dto.setId(employee.getId());
			dto.setName(employee.getName());
			dto.setMonths(employees.size());
			dto.setSalary((long)salary.stream().mapToDouble(s->s.getSalary()).sum());
			int att = attendance.stream().mapToInt(a->a.getAttendance()).sum();
			double attt = attendance.size()*25;
			dto.setPercentage((att/attt)*100);
			dto.setPerformance((attendance.stream().mapToDouble(a->a.getPerformance()).sum()/(double)(attendance.size())));
			dtoList.add(dto);
		}
		return dtoList;
	}

	public File generateReport(long id) throws FileNotFoundException, DocumentException {
		Employee st = employeeDao.findOne(id);
		List<EmployeeAttendance> attendance = employeeAttendanceDao.findAllByEmployee(st);
		List<EmployeeSalary> employeeSalary = employeeSalaryDao.findAllByEmployee(st);
		System.out.println("attendance " + attendance.size());
		System.out.println("salaries " + employeeSalary.size());
		
		File pdf = new File("C:/temp/"+st.getId()+" " + st.getName() +".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(st.getName().toUpperCase() + " Employee Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(st.getName().toUpperCase() + " " + st.getId() + " Employee Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("=== Attendance Report ==="));
		for(EmployeeAttendance sa : attendance) {
			doc.add(new Paragraph(sa.getMonth() + " attendance \t: " + sa.getAttendance() + " days"));
		}
		doc.add(new Paragraph("\n"));
		int att = attendance.stream().mapToInt(a->a.getAttendance()).sum();
		double attt = attendance.size()*22;
		
		doc.add(new Paragraph("Total Attendance %age \t: " + (att/attt)*100 + "%"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("=== Salary Report ==="));
		for(EmployeeSalary sal : employeeSalary) {
			doc.add(new Paragraph(sal.getMonth() + " \t:\t " + sal.getSalary()));
		}
		doc.add(new Paragraph("\n"));
		
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static EmployeeDto convertToEmployeeDtoFromEmployee(Employee emp) {
		EmployeeDto dto = new EmployeeDto();
		dto.setId(emp.getId());
		dto.setName(emp.getName());
		dto.setMobile(emp.getMobile());
		dto.setEmail(emp.getEmail());
		dto.setJob(emp.getJob());
		dto.setJobDuration(emp.getJobDuration());
		dto.setJoiningDate(emp.getJoiningDate()+"");
		return dto;
	}
	
	public EmployeeDto convertToEmployeeDtoFromEmployeeAttendance(List<EmployeeAttendance> attendance) {
		EmployeeDto dto = new EmployeeDto();
		dto.setId(attendance.get(0).getEmployee().getId());
		dto.setEmployeeId(attendance.get(0).getEmployeeId());
		dto.setName(attendance.get(0).getEmployee().getName());
		dto.setMonths(attendance.size());
		dto.setAttendance(attendance.stream().mapToInt(a->a.getAttendance()).sum());
		dto.setPerformance((attendance.stream().mapToDouble(a->a.getPerformance()).sum()/(double)(attendance.size())));
		dto.setPercentage((attendance.stream().mapToDouble(a->a.getAttendance()).sum()/(double)(dto.getMonths()*25))*100);

		return dto;
	}
}
