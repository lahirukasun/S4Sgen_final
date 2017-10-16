package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.TeacherTimeTable;
import itech.s4sgen.models.User;


@Transactional
public interface TeacherTimeTableDao extends CrudRepository<TeacherTimeTable, Long> {

	@Query("Select st from TeacherTimeTable st WHERE st.user IN :user")
	List<TeacherTimeTable> findAllByUser(@Param("user")User user);

	@Query("Select st from TeacherTimeTable st WHERE st.teacherId IN :teacherId")
	List<TeacherTimeTable> findAllByTeacherId(@Param("teacherId")String teacherId);
	
} 
