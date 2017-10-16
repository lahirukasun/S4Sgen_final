package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Teacher;
import itech.s4sgen.models.TeacherSalary;


@Transactional
public interface TeacherSalaryDao extends CrudRepository<TeacherSalary, Long> {

	@Query("Select st from TeacherSalary st WHERE st.teacher IN :teacher")
	List<TeacherSalary> findAllByTeacher(@Param("teacher")Teacher user);
	
	@Query("Select st from TeacherSalary st WHERE st.teacherId IN :teacherId")
	List<TeacherSalary> findAllByTeacherId(@Param("teacherId")String teacherId);

} 
