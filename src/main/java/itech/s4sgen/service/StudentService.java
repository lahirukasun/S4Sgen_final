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

import itech.s4sgen.dao.StudentAttendanceDao;
import itech.s4sgen.dao.StudentDao;
import itech.s4sgen.dao.StudentExamDao;
import itech.s4sgen.dao.StudentFeeDao;
import itech.s4sgen.dto.StudentDto;
import itech.s4sgen.models.Student;
import itech.s4sgen.models.StudentAttendance;
import itech.s4sgen.models.StudentExam;
import itech.s4sgen.models.StudentFee;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class StudentService {

	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private StudentAttendanceDao studentAttendanceDao;
	
	@Autowired
	private StudentExamDao studentExamDao;
	
	@Autowired
	private StudentFeeDao studentFeeDao;
	
	@Autowired
	private UserService userService;
	
	public void saveStudent(StudentDto dto, String login) throws ParseException {
		User user = userService.getUserByLogin(login);
		Student st;
		if(dto.getId()==0)
			st = new Student();
		else
			st = studentDao.findOne(dto.getId());
		
		st.setRollNo(dto.getRollNo());
		st.setName(dto.getName());
		st.setFatherName(dto.getFatherName());
		st.setStudyProgram(dto.getStudyProgram());
		st.setDateOfBirth(HelpingClass.stringToDate(dto.getDob()));
		st.setUser(user);
		studentDao.save(st);
	}
	
	public void saveStudentMarks(StudentDto dto, String login) throws ParseException {
		
		Student st = studentDao.findStudentByRollNoAndUser(dto.getRollNo(), userService.getUserByLogin(login));
		StudentExam se;
		
		if(dto.getId()==0) {
			se = new StudentExam();
		}else {
			se = studentExamDao.findOne(dto.getId());
		}
		se.setTotalMarks(dto.getTotalMarks());
		se.setSubjectiveMarks(dto.getSubjectiveMarks());
		se.setStudent(st);
		se.setRollNo(dto.getRollNo());
		se.setObjectiveMarks(dto.getObjectiveMarks());
		se.setCourse(dto.getCourse());
		se.setSemester(HelpingClass.getSemestersList().indexOf(dto.getSemester()));
		studentExamDao.save(se);
	}
	
	public void saveStudentAttendance(StudentDto dto, String login) {
		Student st = studentDao.findStudentByRollNoAndUser(dto.getRollNo(), userService.getUserByLogin(login));
		StudentAttendance sa;
		if(dto.getId()==0)
			sa = new StudentAttendance();
		else
			sa = studentAttendanceDao.findOne(dto.getId());
		sa.setRollNo(dto.getRollNo());
		sa.setMonth(dto.getMonth());
		sa.setAttendance(dto.getAttendance());
		sa.setStudent(st);
		studentAttendanceDao.save(sa);
	}
	
	public void saveStudentFee(StudentDto dto, String login) {
		Student st = studentDao.findStudentByRollNoAndUser(dto.getRollNo(), userService.getUserByLogin(login));
		StudentFee sf;
		if(dto.getId()==0)
			sf = new StudentFee();
		else
			sf = studentFeeDao.findOne(dto.getId());
		sf.setRollNo(dto.getRollNo());
		sf.setSemester(dto.getSemester());
		sf.setFee((long)dto.getFee());
		sf.setStudent(st);
		studentFeeDao.save(sf);
	}
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public StudentDto getStudentById(long id) {
		return convertToStudentDto(studentDao.findOne(id));
	}
	
	public StudentDto getStudentMarksById(long id) {
		StudentExam se = studentExamDao.findOne(id);
		StudentDto dto = new StudentDto();
		dto.setId(se.getId());
		dto.setRollNo(se.getRollNo());
		dto.setCourse(se.getCourse());
		dto.setSemester(HelpingClass.getSemestersList().get(se.getSemester()));
		dto.setTotalMarks(se.getTotalMarks());
		dto.setObjectiveMarks(se.getObjectiveMarks());
		dto.setSubjectiveMarks(se.getSubjectiveMarks());
		
		return dto;
	}
	
	public StudentDto getStudentAttendanceById(long id) {
		StudentAttendance se = studentAttendanceDao.findOne(id);
		StudentDto dto = new StudentDto();
		dto.setId(se.getId());
		dto.setRollNo(se.getRollNo());
		dto.setMonth(se.getMonth());
		dto.setAttendance(se.getAttendance());
		
		return dto;
	}
	
	public StudentDto getStudentFeeById(long id) {
		StudentFee se = studentFeeDao.findOne(id);
		StudentDto dto = new StudentDto();
		dto.setId(se.getId());
		dto.setRollNo(se.getRollNo());
		dto.setSemester(se.getSemester());
		dto.setFee(se.getFee());
		
		return dto;
	}
	
	public boolean rollNoValidation(String rollNo, String userName) {
		User user = userService.getUserByLogin(userName);
		Student st = studentDao.findStudentByRollNoAndUser(rollNo,user);
		if(st==null)
			return true;
		return false;
	}
	
	public void deleteStudentById(long id) {
		 studentDao.delete(id);
	}
	
	public void deleteStudentAttendanceById(long id) {
		 studentAttendanceDao.delete(id);
	}
	
	public void deleteStudentExamById(long id) {
		 studentExamDao.delete(id);
	}
	
	public void deleteStudentFeeById(long id) {
		 studentFeeDao.delete(id);
	}
	
	public List<StudentDto> getStudentsOfUserByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		return studentDao.findAllByUser(user).stream().map(StudentService::convertToStudentDto).collect(Collectors.toList());
	}
	
	public List<StudentDto> getUserStudentsExamsMarksByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		List<Student> students = studentDao.findAllByUser(user);
		List<StudentDto> marksList = new ArrayList<>();
		for(Student st : students) {
			List<StudentExam> examsMarks = studentExamDao.findAllByStudent(st);
			System.out.println("examMarks" + examsMarks.size());
			if(examsMarks!=null && examsMarks.size()>0) {
				marksList.add(convertToStudentDtoFromStudentExamMarks(examsMarks));
			}
		}
		return marksList;
	}
	
	public List<StudentDto> getUserStudentsAttendanceByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		List<Student> students = studentDao.findAllByUser(user);
		List<StudentDto> attendanceList = new ArrayList<>();
		for(Student st : students) {
			List<StudentAttendance> attendance = studentAttendanceDao.findAllByStudent(st);
			System.out.println("attendance" + attendance.size());
			if(attendance!=null && attendance.size()>0) {
				attendanceList.add(convertToStudentDtoFromStudentAttendance(attendance));
			}
		}
		return attendanceList;
	}
	
	public List<StudentDto> getUserStudentsFeeByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		List<Student> students = studentDao.findAllByUser(user);
		List<StudentDto> feeList = new ArrayList<>();
		for(Student st : students) {
			List<StudentFee> fee = studentFeeDao.findAllByStudent(st);
			System.out.println("fee" + fee.size());
			if(fee!=null && fee.size()>0) {
				feeList.add(convertToStudentDtoFromStudentFee(fee));
			}
		}
		return feeList;
	}
	
	public List<StudentDto> getUserStudentsDataByUserName(String userName) {
		User user = userService.getUserByLogin(userName);
		List<Student> students = studentDao.findAllByUser(user);
		List<StudentDto> feeList = new ArrayList<>();
		for(Student st : students) {
			List<StudentAttendance> attendance = studentAttendanceDao.findAllByStudent(st);
			List<StudentExam> examsMarks = studentExamDao.findAllByStudent(st);
			System.out.println("attendance " + attendance.size());
			System.out.println("examsMarks " + examsMarks.size());
			if((examsMarks!=null && examsMarks.size()>0) && (attendance!=null && attendance.size()>0)) {
				feeList.add(convertToStudentDtoFromStudentData(examsMarks,attendance));
			}
		}
		return feeList;
	}

	public File generateReport(long id) throws FileNotFoundException, DocumentException {
		Student st = studentDao.findOne(id);
		List<StudentAttendance> attendance = studentAttendanceDao.findAllByStudent(st);
		List<StudentExam> examsMarks = studentExamDao.findAllByStudent(st);
		StudentDto stData = new StudentDto();
		System.out.println("attendance " + attendance.size());
		System.out.println("examsMarks " + examsMarks.size());
		if((examsMarks!=null && examsMarks.size()>0) && (attendance!=null && attendance.size()>0)) {
			stData = convertToStudentDtoFromStudentData(examsMarks,attendance);
		}
		File pdf = new File("C:/temp/"+st.getRollNo()+".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(st.getName() + " Student Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(st.getName().toUpperCase() + " " + st.getFatherName().toUpperCase() + " Student Report"));
		doc.add(new Paragraph(""));
		doc.add(new Paragraph(""));
		doc.add(new Paragraph("=== Attendance Report ==="));
		for(StudentAttendance sa : attendance) {
			doc.add(new Paragraph(sa.getMonth() + " attendance \t: " + sa.getAttendance() + " days"));
		}
		
		doc.add(new Paragraph("Total Attendance %age \t: " + stData.getPercentage() + "%"));
		doc.add(new Paragraph(""));
		doc.add(new Paragraph("=== Exams Report ==="));
		for(StudentExam se : examsMarks) {
			doc.add(new Paragraph(se.getCourse() + " \t: " + se.getTotalMarks() + " \t: " + (se.getObjectiveMarks()+se.getSubjectiveMarks())));
		}
		doc.add(new Paragraph("Total Exams %age \t\t: " + stData.getTotalMarks() + "%"));
		
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public List<StudentDto> getStudentMarksDetail(long studentId) {
		Student student = studentDao.findOne(studentId);
		List<StudentExam> seList = studentExamDao.findAllByStudent(student);
		List<StudentDto> dtoList = new ArrayList<>();
		if(seList!=null && seList.size()>0) {
			for(StudentExam se : seList) {
				StudentDto dto = new StudentDto();
				dto.setId(se.getId());
				dto.setName(student.getName() + " " + student.getFatherName() + "   " + student.getRollNo());
				dto.setCourse(se.getCourse());
				dto.setSemester(HelpingClass.getSemestersList().get(se.getSemester()));
				dto.setTotalMarks(se.getTotalMarks());
				dto.setObjectiveMarks(se.getObjectiveMarks());
				dto.setSubjectiveMarks(se.getSubjectiveMarks());
				dto.setPercentage(((se.getObjectiveMarks()+se.getSubjectiveMarks())/se.getTotalMarks())*100);
				dtoList.add(dto);
			}
			return dtoList;
		}
		return null;
	}
	
	public List<StudentDto> getStudentAttendanceDetail(long studentId) {
		Student student = studentDao.findOne(studentId);
		List<StudentAttendance> saList = studentAttendanceDao.findAllByStudent(student);
		List<StudentDto> dtoList = new ArrayList<>();
		if(saList!=null && saList.size()>0) {
			for(StudentAttendance sa : saList) {
				StudentDto dto = new StudentDto();
				dto.setId(sa.getId());
				dto.setName(student.getName() + " " + student.getFatherName() + "   " + student.getRollNo());
				dto.setMonth(sa.getMonth());
				dto.setAttendance(sa.getAttendance());
				dto.setPercentage(((double)sa.getAttendance()/(double)22)*100);
				dtoList.add(dto);
			}
			return dtoList;
		}
		return null;
	}
	
	public List<StudentDto> getStudentFeeDetail(long studentId) {
		Student student = studentDao.findOne(studentId);
		List<StudentFee> feList = studentFeeDao.findAllByStudent(student);
		List<StudentDto> dtoList = new ArrayList<>();
		if(feList!=null && feList.size()>0) {
			for(StudentFee sa : feList) {
				StudentDto dto = new StudentDto();
				dto.setId(sa.getId());
				dto.setName(student.getName() + " " + student.getFatherName() + "   " + student.getRollNo());
				dto.setSemester(sa.getSemester());
				dto.setFee(sa.getFee());
				dtoList.add(dto);
			}
			return dtoList;
		}
		return null;
	}
	
	public static StudentDto convertToStudentDto(Student student) {
		StudentDto dto = new StudentDto();
		dto.setId(student.getId());
		dto.setRollNo(student.getRollNo());
		dto.setName(student.getName());
		dto.setFatherName(student.getFatherName());
		dto.setStudyProgram(student.getStudyProgram());
		dto.setDob(student.getDateOfBirth()+"");
		return dto;
	}
	
	public StudentDto convertToStudentDtoFromStudentExamMarks(List<StudentExam> examsMarks) {
		StudentDto dto = new StudentDto();
		dto.setId(examsMarks.get(0).getStudent().getId());
		dto.setRollNo(examsMarks.get(0).getRollNo());
		dto.setName(examsMarks.get(0).getStudent().getName());
		dto.setTotalMarks(examsMarks.stream().mapToDouble(se->se.getTotalMarks()).sum());
		dto.setObtainedMarks(examsMarks.stream().mapToDouble(se->se.getObjectiveMarks()+se.getSubjectiveMarks()).sum());
		dto.setPercentage((dto.getObtainedMarks()/dto.getTotalMarks())*100);
		dto.setSemester(HelpingClass.getSemestersList().get(examsMarks.get(0).getSemester()));
		
		return dto;
	}
	
	public StudentDto convertToStudentDtoFromStudentAttendance(List<StudentAttendance> attendance) {
		StudentDto dto = new StudentDto();
		dto.setId(attendance.get(0).getStudent().getId());
		dto.setRollNo(attendance.get(0).getRollNo());
		dto.setName(attendance.get(0).getStudent().getName());
		dto.setMonths(attendance.size());
		dto.setPercentage((attendance.stream().mapToDouble(a->a.getAttendance()).sum()/(double)(dto.getMonths()*22))*100);
		
		return dto;
	}
	
	public StudentDto convertToStudentDtoFromStudentFee(List<StudentFee> fee) {
		StudentDto dto = new StudentDto();
		dto.setId(fee.get(0).getStudent().getId());
		dto.setRollNo(fee.get(0).getRollNo());
		dto.setName(fee.get(0).getStudent().getName());
		dto.setMonths(fee.size());
		dto.setFee(fee.stream().mapToDouble(f->f.getFee()).sum());
		return dto;
	}

	public StudentDto convertToStudentDtoFromStudentData(List<StudentExam> examsMarks,List<StudentAttendance> attendance) {
		StudentDto dto = new StudentDto();
		dto.setId(examsMarks.get(0).getStudent().getId());
		dto.setRollNo(examsMarks.get(0).getRollNo());
		dto.setName(examsMarks.get(0).getStudent().getName());
		double totalMarks = examsMarks.stream().mapToDouble(se->se.getTotalMarks()).sum();
		dto.setObtainedMarks(examsMarks.stream().mapToDouble(se->se.getObjectiveMarks()+se.getSubjectiveMarks()).sum());
		dto.setTotalMarks((dto.getObtainedMarks()/totalMarks)*100);
		dto.setPercentage((attendance.stream().mapToDouble(a->a.getAttendance()).sum()/(double)(attendance.size()*22))*100);
		return dto;
	}
}
