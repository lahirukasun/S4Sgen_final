package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.TeacherExamDuty;
import itech.s4sgen.models.User;



@Transactional
public interface TeacherExamDutyDao extends CrudRepository<TeacherExamDuty, Long> {

	@Query("Select st from TeacherExamDuty st WHERE st.user IN :user")
	List<TeacherExamDuty> findAllByUser(@Param("user")User user);
	
	@Query("Select st from TeacherExamDuty st WHERE st.teacherId IN :teacherId")
	List<TeacherExamDuty> findAllByTeacherId(@Param("teacherId")String teacherId);

} 
