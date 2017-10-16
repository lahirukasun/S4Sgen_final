package itech.s4sgen.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import itech.s4sgen.dao.AppointmentDao;
import itech.s4sgen.dao.DoctorAttendanceDao;
import itech.s4sgen.dao.DoctorDao;
import itech.s4sgen.dao.DoctorSalaryDao;
import itech.s4sgen.dto.DoctorDto;
import itech.s4sgen.dto.PatientDto;
import itech.s4sgen.models.Appointment;
import itech.s4sgen.models.Doctor;
import itech.s4sgen.models.DoctorAttendance;
import itech.s4sgen.models.DoctorSalary;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class DoctorService {

	@Autowired
	private DoctorDao doctorDao;

	@Autowired
	private DoctorAttendanceDao doctorAttendanceDao;

	@Autowired
	private DoctorSalaryDao doctorSalaryDao;

	@Autowired
	private AppointmentDao appointmentDao;

	@Autowired
	private UserService userService;

	@Autowired
	private PatientService patientService;

	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		return false;
	}

	public void saveDoctor(DoctorDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		Doctor doc;
		if(dto.getId()==0)
			doc = new Doctor();
		else
			doc = doctorDao.findOne(dto.getId());
		doc.setName(dto.getName());
		doc.setMobile(dto.getMobile());
		doc.setSpeciality(dto.getSpeciality());
		doc.setRoomNo(dto.getRoomNo());
		doc.setTime(dto.getTime());
		doc.setJoiningDate(HelpingClass.stringToDate(dto.getJoiningDate()));
		doc.setUser(user);
		doctorDao.save(doc);
	}

	public boolean updateAppointmentStatus(long id) {
		Appointment app = appointmentDao.findOne(id);
		app.setChecked(true);
		Appointment appp = appointmentDao.save(app);
		if(appp!=null)
			return true;
		return false;
	}

	public void saveDoctorAttendance(DoctorDto dto, String login) {
		Doctor st = doctorDao.findOne(Long.parseLong(dto.getDoctorId()));
		DoctorAttendance sa;
		if(dto.getId()==0)
			sa = new DoctorAttendance();
		else
			sa = doctorAttendanceDao.findOne(dto.getId());
		sa.setDoctorId(dto.getDoctorId());
		sa.setMonth(dto.getMonth());
		sa.setAttendance(dto.getAttendance());
		sa.setDoctor(st);
		doctorAttendanceDao.save(sa);
	}

	public void saveDoctorSalary(DoctorDto dto, String userName) {
		Doctor doctor = doctorDao.findOne(Long.valueOf(dto.getDoctorId()));
		DoctorSalary salary;
		if(dto.getId()==0)
			salary = new DoctorSalary() ;
		else
			salary = doctorSalaryDao.findOne(dto.getId());
		salary.setDoctorId(dto.getDoctorId());
		salary.setMonth(dto.getMonth());
		salary.setSalary(dto.getSalary());
		salary.setDoctor(doctor);
		doctorSalaryDao.save(salary);
	}

	public DoctorDto getDoctorAttendanceById(long id) {
		DoctorAttendance se = doctorAttendanceDao.findOne(id);
		DoctorDto dto = new DoctorDto();
		dto.setId(se.getId());
		dto.setDoctorId(se.getId()+"");
		dto.setMonth(se.getMonth());
		dto.setAttendance(se.getAttendance());

		return dto;
	}

	public List<DoctorDto> getUserDoctorByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return doctorDao.findAllByUser(user).stream().map(DoctorService::convertToDoctorDtoFromDoctor).collect(Collectors.toList());
	}

	public List<PatientDto> getUserAppointmentsByUserName(String userName){
		return patientService.getUserAdmittedPatientAppointmensByUserName(userName);
	}

	public List<DoctorDto> getUserDoctorsAttendanceByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		List<Doctor> doctors = doctorDao.findAllByUser(user);
		List<DoctorDto> attendanceList = new ArrayList<>();
		for(Doctor st : doctors) {
			List<DoctorAttendance> attendance = doctorAttendanceDao.findAllByDoctor(st);
			System.out.println("attendance" + attendance.size());
			if(attendance!=null && attendance.size()>0) {
				attendanceList.add(convertToDoctorDtoFromDoctorAttendance(attendance));
			}
		}
		return attendanceList;
	}

	public List<DoctorDto> getDoctorsSalaryOfUserByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Doctor> doctors = doctorDao.findAllByUser(user);
		List<DoctorDto> dtoList = new ArrayList<>();
		for(Doctor doctor : doctors){
			List<DoctorSalary> salary = doctorSalaryDao.findAllByDoctor(doctor);
			DoctorDto dto = new DoctorDto();
			dto.setDoctorId(doctor.getId()+"");
			dto.setId(doctor.getId());
			dto.setName(doctor.getName());
			dto.setMonths(salary.size());
			dto.setSalary((long)salary.stream().mapToDouble(s->s.getSalary()).sum());
			dtoList.add(dto);
		}
		return dtoList;
	}

	public List<DoctorDto> getUserDoctorsDataByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Doctor> doctors = doctorDao.findAllByUser(user);
		List<DoctorDto> dtoList = new ArrayList<>();
		for(Doctor doctor : doctors){
			List<DoctorSalary> salary = doctorSalaryDao.findAllByDoctor(doctor);
			List<DoctorAttendance> attendance = doctorAttendanceDao.findAllByDoctor(doctor);
			DoctorDto dto = new DoctorDto();
			dto.setDoctorId(doctor.getId()+"");
			dto.setId(doctor.getId());
			dto.setName(doctor.getName());
			dto.setMonths(doctors.size());
			dto.setSalary((long)salary.stream().mapToDouble(s->s.getSalary()).sum());
			int att = attendance.stream().mapToInt(a->a.getAttendance()).sum();
			double attt = attendance.size()*22;
			System.out.println("att: " + att);
			System.out.println("attt: " + attt);
			System.out.println("perc: " + (att/attt)*100);
			dto.setPercentage((att/attt)*100);
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public DoctorDto getDoctorDtoByDoctorId(long id) {
		Doctor doctor = doctorDao.findOne(id);
		DoctorDto dto = new DoctorDto();
		dto.setId(doctor.getId());
		dto.setName(doctor.getName());
		dto.setMobile(doctor.getMobile());
		dto.setSpeciality(doctor.getSpeciality()); 
		dto.setRoomNo(doctor.getRoomNo());
		dto.setTime(doctor.getTime());
		dto.setJoiningDate(doctor.getJoiningDate()+"");

		return dto;
	}

	public List<DoctorDto> getDoctorAttendanceDetail(long doctorId) {
		Doctor doctor = doctorDao.findOne(doctorId);
		List<DoctorAttendance> saList = doctorAttendanceDao.findAllByDoctor(doctor);
		List<DoctorDto> dtoList = new ArrayList<>();
		if(saList!=null && saList.size()>0) {
			for(DoctorAttendance sa : saList) {
				DoctorDto dto = new DoctorDto();
				dto.setId(sa.getId());
				dto.setName(doctor.getName() + " " + doctor.getId());
				dto.setMonth(sa.getMonth());
				dto.setAttendance(sa.getAttendance());
				dto.setPercentage(((double)sa.getAttendance()/(double)22)*100);
				dtoList.add(dto);
			}
			return dtoList;
		}
		return null;
	}

	public List<DoctorDto> getDoctorsSalaryOfByDoctorId(long id){
		Doctor doctor = doctorDao.findOne(id);
		List<DoctorDto> dtoList = new ArrayList<>();
		List<DoctorSalary> salary = doctorSalaryDao.findAllByDoctor(doctor);
		for(DoctorSalary ts : salary) {
			DoctorDto dto = new DoctorDto();
			dto.setId(ts.getId());
			dto.setName(doctor.getName().toUpperCase());
			dto.setMonth(ts.getMonth());
			dto.setSalary(ts.getSalary());
			dtoList.add(dto);
		}

		return dtoList;
	}

	public DoctorDto getDoctorDtoByDoctorSalaryId(long id) {
		DoctorSalary salary = doctorSalaryDao.findOne(id);
		DoctorDto dto = new DoctorDto();
		dto.setId(salary.getId());
		dto.setDoctorId(salary.getDoctorId());
		dto.setMonth(salary.getMonth());
		dto.setSalary(salary.getSalary());
		return dto;
	}

	public File generateReport(long id) throws FileNotFoundException, DocumentException {
		Doctor st = doctorDao.findOne(id);
		List<DoctorAttendance> attendance = doctorAttendanceDao.findAllByDoctor(st);
		List<DoctorSalary> doctorSalary = doctorSalaryDao.findAllByDoctor(st);

		System.out.println("attendance " + attendance.size());
		System.out.println("salaries " + doctorSalary.size());
		
		File pdf = new File("C:/temp/"+st.getId()+" " + st.getName() +".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(st.getName().toUpperCase() + " Doctor Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(st.getName().toUpperCase() + " " + st.getId() + " Doctor Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("=== Attendance Report ==="));
		for(DoctorAttendance sa : attendance) {
			doc.add(new Paragraph(sa.getMonth() + " attendance \t: " + sa.getAttendance() + " days"));
		}
		doc.add(new Paragraph("\n"));
		int att = attendance.stream().mapToInt(a->a.getAttendance()).sum();
		double attt = attendance.size()*22;
		
		doc.add(new Paragraph("Total Attendance %age \t: " + (att/attt)*100 + "%"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("=== Salary Report ==="));
		for(DoctorSalary sal : doctorSalary) {
			doc.add(new Paragraph(sal.getMonth() + " \t:\t " + sal.getSalary()));
		}
		doc.add(new Paragraph("\n"));
		
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static DoctorDto convertToDoctorDtoFromDoctor(Doctor doctor) {
		DoctorDto dto = new DoctorDto();
		dto.setId(doctor.getId());
		dto.setName(doctor.getName());
		dto.setMobile(doctor.getMobile());
		dto.setSpeciality(doctor.getSpeciality());
		dto.setRoomNo(doctor.getRoomNo());
		dto.setTime(doctor.getTime());
		dto.setJoiningDate(doctor.getJoiningDate()+"");

		return dto;
	}

	public DoctorDto convertToDoctorDtoFromDoctorAttendance(List<DoctorAttendance> attendance) {
		DoctorDto dto = new DoctorDto();
		dto.setId(attendance.get(0).getDoctor().getId());
		dto.setDoctorId(attendance.get(0).getDoctorId());
		dto.setName(attendance.get(0).getDoctor().getName());
		dto.setMonths(attendance.size());
		dto.setPercentage((attendance.stream().mapToDouble(a->a.getAttendance()).sum()/(double)(dto.getMonths()*22))*100);

		return dto;
	}
}
