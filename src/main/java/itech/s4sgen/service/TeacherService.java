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

import itech.s4sgen.dao.TeacherDao;
import itech.s4sgen.dao.TeacherExamDutyDao;
import itech.s4sgen.dao.TeacherSalaryDao;
import itech.s4sgen.dao.TeacherTimeTableDao;
import itech.s4sgen.dto.TeacherDto;
import itech.s4sgen.models.Teacher;
import itech.s4sgen.models.TeacherExamDuty;
import itech.s4sgen.models.TeacherSalary;
import itech.s4sgen.models.TeacherTimeTable;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class TeacherService {

	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired
	private TeacherExamDutyDao teacherExamDutyDao;
	
	@Autowired
	private TeacherSalaryDao teacherSalaryDao;
	
	@Autowired
	private TeacherTimeTableDao teacherTimeTableDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void saveTeacher(TeacherDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		Teacher teacher;
		if(dto.getId()==0)
			teacher = new Teacher();
		else
			teacher = teacherDao.findOne(dto.getId());
		teacher.setTeacherId(dto.getTeacherId());
		teacher.setName(dto.getName());
		teacher.setMobileNo(dto.getMobile());
		teacher.setTeachingCourse(dto.getCourse());
		teacher.setJoiningDate(HelpingClass.stringToDate(dto.getJoinDate()));
		teacher.setUser(user);
		teacherDao.save(teacher);
	}
	
	public void saveExamDuty(TeacherDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		TeacherExamDuty duty;
		if(dto.getId()==0)
			duty = new TeacherExamDuty();
		else
			duty = teacherExamDutyDao.findOne(dto.getId());
		duty.setCourse(dto.getCourse());
		duty.setExamDate(HelpingClass.stringToDate(dto.getExamDate()));
		duty.setExamTime(dto.getTime());
		duty.setTeacherId(dto.getTeacherId());
		duty.setRoomNo(dto.getRoomNo());
		duty.setUser(user);
		
		teacherExamDutyDao.save(duty);
		
	}
	
	public void saveLectureSchedule(TeacherDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		TeacherTimeTable time;
		if(dto.getId()==0)
			time = new TeacherTimeTable();
		else
			time = teacherTimeTableDao.findOne(dto.getId());
		time.setTeacherId(dto.getTeacherId());
		time.setCourse(dto.getCourse());
		time.setRoomNo(dto.getRoomNo());
		time.setLectureDate(HelpingClass.stringToDate(dto.getExamDate()));
		time.setLectureTime(dto.getTime());
		time.setUser(user);
		teacherTimeTableDao.save(time);
	}
	
	public void saveTeacherSalary(TeacherDto dto, String userName) {
		Teacher teacher = teacherDao.findOneByTeacherIdAndUser(dto.getTeacherId(), userService.getUserByLogin(userName));
		TeacherSalary salary;
		if(dto.getId()==0)
			salary = new TeacherSalary() ;
		else
			salary = teacherSalaryDao.findOne(dto.getId());
		salary.setTeacherId(dto.getTeacherId());
		salary.setMonth(dto.getMonth());
		salary.setSalary(dto.getSalary());
		salary.setTeacher(teacher);
		teacherSalaryDao.save(salary);
	}
	
	public List<TeacherDto> getTeachersOfUserByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		return teacherDao.findAllByUser(user).stream().map(TeacherService::convertToTeacherDto).collect(Collectors.toList());
	}
	
	public List<TeacherDto> getTeachersDutiesOfUserByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		return teacherExamDutyDao.findAllByUser(user).stream().map(TeacherService::convertToTeacherDtoFromExamDuty).collect(Collectors.toList());
	}
	
	public List<TeacherDto> getTeachersLectureScheduleOfUserByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return teacherTimeTableDao.findAllByUser(user).stream().map(TeacherService::convertToTeacherDtoFromTeacherTimeTable).collect(Collectors.toList());
	}
	
	public List<TeacherDto> getTeachersSalaryOfUserByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Teacher> teachers = teacherDao.findAllByUser(user);
		List<TeacherDto> dtoList = new ArrayList<>();
		for(Teacher teacher : teachers){
			List<TeacherSalary> salary = teacherSalaryDao.findAllByTeacher(teacher);
			TeacherDto dto = new TeacherDto();
			dto.setTeacherId(teacher.getTeacherId());
			dto.setId(teacher.getId());
			dto.setName(teacher.getName());
			dto.setMonths(teachers.size());
			dto.setSalary(salary.stream().mapToDouble(s->s.getSalary()).sum());
			dtoList.add(dto);
		}
		return dtoList;
	}

	public List<TeacherDto> getTeachersSalaryOfByTeacherId(long id){
		Teacher teacher = teacherDao.findOne(id);
		List<TeacherDto> dtoList = new ArrayList<>();
		List<TeacherSalary> salary = teacherSalaryDao.findAllByTeacher(teacher);
		for(TeacherSalary ts : salary) {
			TeacherDto dto = new TeacherDto();
			dto.setId(ts.getId());
			dto.setName(teacher.getName().toUpperCase());
			dto.setMonth(ts.getMonth());
			dto.setSalary(ts.getSalary());
			dtoList.add(dto);
		}
		
		return dtoList;
	}
	
	public TeacherDto getTeacherDtoByTeacherSalaryId(long id) {
		TeacherSalary salary = teacherSalaryDao.findOne(id);
		TeacherDto dto = new TeacherDto();
		dto.setId(salary.getId());
		dto.setTeacherId(salary.getTeacherId());
		dto.setMonth(salary.getMonth());
		dto.setSalary(salary.getSalary());
		return dto;
	}
	
	public TeacherDto getTeacherDtoById(long id) {
		Teacher teacher = teacherDao.findOne(id);
		TeacherDto dto = new TeacherDto();
		dto.setId(teacher.getId());
		dto.setTeacherId(teacher.getTeacherId());
		dto.setName(teacher.getName());
		dto.setMobile(teacher.getMobileNo());
		dto.setCourse(teacher.getTeachingCourse());
		dto.setJoinDate(teacher.getJoiningDate()+"");
		
		return dto;
	}
	
	public TeacherDto getTeacherExamDutyById(long id) {
		TeacherExamDuty duty = teacherExamDutyDao.findOne(id);
		TeacherDto dto = new TeacherDto();
		dto.setId(duty.getId());
		dto.setTeacherId(duty.getTeacherId());
		dto.setCourse(duty.getCourse());
		dto.setRoomNo(duty.getRoomNo());
		dto.setExamDate(duty.getExamDate()+"");
		dto.setTime(duty.getExamTime());
		return dto;
	}
	
	public TeacherDto getLectureScheduleById(long id) {
		TeacherTimeTable time = teacherTimeTableDao.findOne(id);
		TeacherDto dto = new TeacherDto();
		dto.setId(time.getId());
		dto.setTeacherId(time.getTeacherId());
		dto.setCourse(time.getCourse());
		dto.setRoomNo(time.getRoomNo());
		dto.setExamDate(time.getLectureDate()+"");
		dto.setTime(time.getLectureTime());
		return dto;
	}
	
	public File generateReport(long id) throws FileNotFoundException, DocumentException {
		Teacher st = teacherDao.findOne(id);
		List<TeacherExamDuty> duties = teacherExamDutyDao.findAllByTeacherId(st.getTeacherId());
		List<TeacherTimeTable> timeTable = teacherTimeTableDao.findAllByTeacherId(st.getTeacherId());
		List<TeacherSalary> salaries = teacherSalaryDao.findAllByTeacherId(st.getTeacherId());
		
		System.out.println("duties :" + duties.size());
		System.out.println("timeTable :" + timeTable.size());
		System.out.println("salaries :" + salaries.size());
		
		File pdf = new File("C:/temp/"+st.getTeacherId()+".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(st.getName() + " Teacher Report");
		doc.addAuthor("s4sgen");
		
		doc.add(new Paragraph(st.getName().toUpperCase() + "    " + st.getTeacherId() + " Report"));
		doc.add(new Paragraph("Teaching Course: \t" + st.getTeachingCourse()));
		doc.add(new Paragraph("Joining Date   : \t" + st.getJoiningDate()));
		doc.add(new Paragraph(""));
		doc.add(new Paragraph("== Lecture Schedule =="));
		for(TeacherTimeTable time : timeTable) {
			doc.add(new Paragraph(time.getCourse() + ": \t" + time.getLectureDate() + ": \t" + time.getLectureTime()));
		}
		doc.add(new Paragraph(""));
		doc.add(new Paragraph("== Exam Duties =="));
		for(TeacherExamDuty duty: duties) {
			doc.add(new Paragraph(duty.getCourse() + ": \t" + duty.getExamDate() + ": \t" + duty.getExamTime()));
		}
		doc.add(new Paragraph(""));
		doc.add(new Paragraph("== Salaries Paid =="));
		for(TeacherSalary sal: salaries) {
			doc.add(new Paragraph(sal.getMonth() + ": \t" + sal.getSalary()));
		}
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static TeacherDto convertToTeacherDto(Teacher teacher) {
		TeacherDto dto = new TeacherDto();
		dto.setId(teacher.getId());
		dto.setTeacherId(teacher.getTeacherId());
		dto.setName(teacher.getName());
		dto.setMobile(teacher.getMobileNo());
		dto.setCourse(teacher.getTeachingCourse());
		dto.setJoinDate(teacher.getJoiningDate()+"");
		
		return dto;
	}
	
	public static TeacherDto convertToTeacherDtoFromExamDuty(TeacherExamDuty duty) {
		TeacherDto dto = new TeacherDto();
		dto.setId(duty.getId());
		dto.setTeacherId(duty.getTeacherId());
		dto.setCourse(duty.getCourse());
		dto.setRoomNo(duty.getRoomNo());
		dto.setExamDate(duty.getExamDate()+"");
		dto.setTime(duty.getExamTime());
		return dto;
	}
	
	public static TeacherDto convertToTeacherDtoFromTeacherTimeTable(TeacherTimeTable time) {
		TeacherDto dto = new TeacherDto();
		dto.setId(time.getId());
		dto.setTeacherId(time.getTeacherId());
		dto.setCourse(time.getCourse());
		dto.setRoomNo(time.getRoomNo());
		dto.setExamDate(time.getLectureDate()+"");
		dto.setTime(time.getLectureTime());
		return dto;
	}
	
}
