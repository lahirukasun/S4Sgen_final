package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Student;
import itech.s4sgen.models.StudentExam;


@Transactional
public interface StudentExamDao extends CrudRepository<StudentExam, Long> {

	@Query("Select st from StudentExam st WHERE st.student IN :student")
	List<StudentExam> findAllByStudent(@Param("student")Student student);

} 
