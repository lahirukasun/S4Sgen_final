package itech.s4sgen.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import itech.s4sgen.dao.AppointmentDao;
import itech.s4sgen.dao.PatientCheckupFeeDao;
import itech.s4sgen.dto.AppointmentDto;
import itech.s4sgen.models.Appointment;
import itech.s4sgen.models.PatientCheckupFee;
import itech.s4sgen.models.User;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentDao appointmentDao;
	
	@Autowired
	private PatientCheckupFeeDao patientCheckupFeeDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void saveAppointment(AppointmentDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		Appointment app;
		if(dto.getId()==0)
			app = new Appointment();
		else
			app = appointmentDao.findOne(dto.getId());
		app.setPatientName(dto.getPatientName());
		app.setPatientAge(dto.getPatientAge());
		app.setPatientProblem(dto.getPatientProblem());
		app.setDoctorName(dto.getDoctorName());
		app.setFee(dto.getFee());
		app.setChecked(false);
		app.setUser(user);
		appointmentDao.save(app);
	}
	
	public void saveCheckupPayment(AppointmentDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		PatientCheckupFee cp;
		if(dto.getId()==0)
			cp = new PatientCheckupFee();
		else
			cp = patientCheckupFeeDao.findOne(dto.getId());
		cp.setPatientName(dto.getPatientName());
		cp.setAmount(dto.getFee());
		cp.setUser(user);
		patientCheckupFeeDao.save(cp);
	}
	
	public AppointmentDto getAppointmentDtoById(long id) {
		return convertToAppointmentDtoByAppointment(appointmentDao.findOne(id));
	}
	
	public boolean updateAppointmentStatus(long id) {
		Appointment app = appointmentDao.findOne(id);
		app.setChecked(true);
		Appointment appp = appointmentDao.save(app);
		if(appp!=null)
			return true;
		return false;
	}
	
	public List<AppointmentDto> getUserAppointmentsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return appointmentDao.findAllByUser(user).stream().map(AppointmentService::convertToAppointmentDtoByAppointment).collect(Collectors.toList());
	}
	
	public List<AppointmentDto> getUserActiveAppointmentsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return appointmentDao.findAllByUserAndChecked(user,false).stream().map(AppointmentService::convertToAppointmentDtoByAppointment).collect(Collectors.toList());
	}
	
	public List<AppointmentDto> getUserCheckupFeeByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return patientCheckupFeeDao.findAllByUser(user).stream().map(AppointmentService::convertToAppointmentDtoByPatientCheckupFee).collect(Collectors.toList());
	}
	
	public AppointmentDto getCheckupFeeById(long id) {
		return convertToAppointmentDtoByPatientCheckupFee(patientCheckupFeeDao.findOne(id));
	}
	
	public AppointmentDto getAppointmentsDataByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		AppointmentDto dto = new AppointmentDto();
		dto.setFee(patientCheckupFeeDao.findAllByUser(user).stream().mapToDouble(f->f.getAmount()).sum());
		dto.setId(appointmentDao.findAllByUser(user).size());
		return dto;
	}
	
	public File generateReport(String userName) throws FileNotFoundException, DocumentException {
		User user = userService.getUserByLogin(userName);
		double fees = patientCheckupFeeDao.findAllByUser(user).stream().mapToDouble(f->f.getAmount()).sum();
		int apps = appointmentDao.findAllByUser(user).size();
		
		File pdf = new File("C:/temp/"+userName+".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(userName + " Appointments Report");
		doc.addAuthor("s4sgen");
		
		doc.add(new Paragraph(userName.toUpperCase() + " Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Total Appoinments:\t\t" + apps));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Total Collections:\t\t" + fees));
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static AppointmentDto convertToAppointmentDtoByAppointment(Appointment app) {
		AppointmentDto dto = new AppointmentDto();
		dto.setId(app.getId());
		dto.setPatientName(app.getPatientName());
		dto.setPatientAge(app.getPatientAge());
		dto.setPatientProblem(app.getPatientProblem());
		dto.setDoctorName(app.getDoctorName());
		dto.setFee(app.getFee());
		dto.setChecked(app.isChecked());
		return dto;
	}
	
	public static AppointmentDto convertToAppointmentDtoByPatientCheckupFee(PatientCheckupFee app) {
		AppointmentDto dto = new AppointmentDto();
		dto.setId(app.getId());
		dto.setPatientName(app.getPatientName());
		dto.setFee(app.getAmount());
		return dto;
	}
}
