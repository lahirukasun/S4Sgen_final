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
import itech.s4sgen.dao.PatientDao;
import itech.s4sgen.dao.PatientPaymentDao;
import itech.s4sgen.dto.PatientDto;
import itech.s4sgen.models.Appointment;
import itech.s4sgen.models.Patient;
import itech.s4sgen.models.PatientPayment;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class PatientService {

	@Autowired
	private AppointmentDao appointmentDao;
	
	@Autowired
	private PatientDao patientDao;
	
	@Autowired
	private PatientPaymentDao patientPaymentDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void savePatient(PatientDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		Patient patient;
		if(dto.getId()==0)
			patient = new Patient();
		else
			patient = patientDao.findOne(dto.getId());
		patient.setAdmitId(dto.getAdmitId());
		patient.setName(dto.getName());
		patient.setPartnerMobile(dto.getPartnerMobile());
		patient.setAdmittedIn(dto.getAdmittedIn());
		patient.setNo(dto.getNo());
		patient.setAdmitDate(HelpingClass.stringToDate(dto.getAdmitDate()));
		patient.setDisease(dto.getDisease());
		patient.setUser(user);
		patientDao.save(patient);
	}
	
	public void saveAdmittedPatientAppointment(PatientDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		Appointment appointment;
		if(dto.getId()==0)
			appointment = new Appointment();
		else
			appointment = appointmentDao.findOne(dto.getId());
		appointment.setPatientName(dto.getName());
		appointment.setPatientAge(dto.getAge());
		appointment.setDoctorName(dto.getDoctorName());
		appointment.setPatientProblem(dto.getDisease());
		appointment.setChecked(false);
		appointment.setFee(dto.getFee());
		appointment.setUser(user);
		appointmentDao.save(appointment);
	}
	
	public void saveAdmittedPatientPayment(PatientDto dto) {
		PatientPayment pp;
		if(dto.getId()==0)
			pp = new PatientPayment();
		else
			pp = patientPaymentDao.findOne(dto.getId());
		Patient patient = patientDao.findOneByAdmitId(dto.getAdmitId());
		System.out.println("patient is: " + patient);
		pp.setAdmitId(dto.getAdmitId());
		pp.setPatientName(dto.getName());
		pp.setAmount(dto.getFee());
		pp.setPatient(patient);
		patientPaymentDao.save(pp);
	}
	
	public PatientDto getPatientById(long id) {
		Patient patient = patientDao.findOne(id);
		PatientDto dto = new PatientDto();
		dto.setId(patient.getId());
		dto.setAdmitId(patient.getAdmitId());
		dto.setName(patient.getName());
		dto.setPartnerMobile(patient.getPartnerMobile());
		dto.setAdmittedIn(patient.getAdmittedIn());
		dto.setNo(patient.getNo());
		dto.setAdmitDate(patient.getAdmitDate()+"");
		dto.setDisease(patient.getDisease());
		return dto;
	}
	
	public PatientDto getAppointmentById(long id) {
		Appointment appointment = appointmentDao.findOne(id);
		PatientDto dto = new PatientDto();
		dto.setId(appointment.getId());
		dto.setName(appointment.getPatientName());
		dto.setAge(appointment.getPatientAge());
		dto.setDoctorName(appointment.getDoctorName());
		dto.setDisease(appointment.getPatientProblem());
		dto.setFee(appointment.getFee());
		return dto;
	}
	
	public List<PatientDto> getUserAdmittedPatientAppointmensByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		return appointmentDao.findAllByUser(user).stream().map(PatientService::convertToPatientDtoFromAppointment).collect(Collectors.toList());
	}
	
	public List<PatientDto> getUserPatientsByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		return patientDao.findAllByUser(user).stream().map(PatientService::convertToPatientDtoFromPatient).collect(Collectors.toList());
	}
	
	public List<PatientDto> getUserPatientsDataByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Patient> patients = patientDao.findAllByUser(user);
		List<PatientDto> patientsData = new ArrayList<>();
		for(Patient p : patients) {
			List<Appointment> apps = appointmentDao.findAllByPatientName(p.getName());
			List<PatientPayment> pps = patientPaymentDao.findAllByPatient(p);
			PatientDto dto = new PatientDto();
			dto.setId(p.getId());
			dto.setAdmitId(p.getAdmitId());
			dto.setName(p.getName());
			dto.setAdmittedIn(p.getAdmittedIn() + " " + p.getNo());
			dto.setNo(apps.size());
			dto.setFee(pps.stream().mapToDouble(pp->pp.getAmount()).sum());
			patientsData.add(dto);
		}
		return patientsData;
	}
	
	public List<PatientDto> getUserPatientPaymentsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Patient> patients = patientDao.findAllByUser(user);
		List<PatientDto> dtoList = new ArrayList<>();
		for(Patient p : patients) {
			dtoList.add(convertToPatientDtoFromPatienPayment(p));
		}
		return dtoList;
	}
	
	public File generateReport(long id) throws FileNotFoundException, DocumentException {
		Patient patient = patientDao.findOne(id);
		List<Appointment> apps = appointmentDao.findAllByPatientName(patient.getName());
		List<PatientPayment> pps = patientPaymentDao.findAllByPatient(patient);
		System.out.println("appointments " + apps.size());
		System.out.println("patienpayments " + pps.size());
		
		File pdf = new File("C:/temp/"+patient.getAdmitId()+".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(patient.getName().toUpperCase() + " Patient Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(patient.getName().toUpperCase() + " Patient Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Admit Id: \t" + patient.getAdmitId()));
		doc.add(new Paragraph("Admitted In: \t" + patient.getAdmittedIn() + " " + patient.getNo()));
		doc.add(new Paragraph("Disease: \t" + patient.getDisease()));
		doc.add(new Paragraph("Admit Date: \t" + patient.getAdmitDate()));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Appointments:-"));
		for(Appointment app : apps) {
			doc.add(new Paragraph("Doctor: \t" + app.getDoctorName()));
			doc.add(new Paragraph("Fee: \t" + app.getFee()));
		}
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Payments:-"));
		for(PatientPayment pp : pps) {
			doc.add(new Paragraph("Amount: \t" + pp.getAmount()));
		}
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static PatientDto convertToPatientDtoFromPatient(Patient patient) {
		PatientDto dto = new PatientDto();
		dto.setId(patient.getId());
		dto.setAdmitId(patient.getAdmitId());
		dto.setName(patient.getName());
		dto.setPartnerMobile(patient.getPartnerMobile());
		dto.setAdmittedIn(patient.getAdmittedIn());
		dto.setNo(patient.getNo());
		dto.setAdmitDate(patient.getAdmitDate()+"");
		dto.setDisease(patient.getDisease());
		return dto;
	}
	
	public static PatientDto convertToPatientDtoFromAppointment(Appointment appointment) {
		PatientDto dto = new PatientDto();
		dto.setId(appointment.getId());
		dto.setName(appointment.getPatientName());
		dto.setAge(appointment.getPatientAge());
		dto.setDoctorName(appointment.getDoctorName());
		dto.setDisease(appointment.getPatientProblem());
		dto.setFee(appointment.getFee());
		dto.setChecked(appointment.isChecked());
		return dto;
	}
	
	public PatientDto convertToPatientDtoFromPatienPayment(Patient patient) {
		List<PatientPayment> pp = patientPaymentDao.findAllByPatient(patient);
		PatientDto dto = new PatientDto();
		dto.setId(patient.getId());
		dto.setAdmitId(patient.getAdmitId());
		dto.setName(patient.getName());
		dto.setFee(pp.stream().mapToDouble(p->p.getAmount()).sum());
		return dto;
	}

	/*public static void main(String args[]) {
		String featureString = "1,2,3,4,5";
		System.out.println("features  : " + featureString);
		System.out.println("feature at : " + featureString.charAt(0));
		int index = Character.getNumericValue(featureString.charAt(0));
		int smsId = 4;
		System.out.println("sms: " + smsId + " index: " + index);
	}*/
}
