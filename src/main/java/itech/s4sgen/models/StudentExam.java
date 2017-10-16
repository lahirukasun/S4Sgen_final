
package itech.s4sgen.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="student_exam_marking")
public class StudentExam implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;
	
	@Column(name="roll_no")
	private String rollNo;
	
	@Column(name="course")
	private String course;
	
	@Column(name="obj_marks")
	private double objectiveMarks;
	
	@Column(name="sub_marks")
	private double subjectiveMarks;
	
	@Column(name="total_marks")
	private double totalMarks;
	
	@Column(name="semester")
	private int semester;
	

	@ManyToOne
	@JoinColumn(name="student", referencedColumnName="id")
	private Student student;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public double getObjectiveMarks() {
		return objectiveMarks;
	}

	public void setObjectiveMarks(double objectiveMarks) {
		this.objectiveMarks = objectiveMarks;
	}

	public double getSubjectiveMarks() {
		return subjectiveMarks;
	}

	public void setSubjectiveMarks(double subjectiveMarks) {
		this.subjectiveMarks = subjectiveMarks;
	}

	public double getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(double totalMarks) {
		this.totalMarks = totalMarks;
	}
	
	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	
}  

