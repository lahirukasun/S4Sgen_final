package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Teacher;
import itech.s4sgen.models.User;


@Transactional
public interface TeacherDao extends CrudRepository<Teacher, Long> {

	@Query("Select st from Teacher st WHERE st.user IN :user")
	List<Teacher> findAllByUser(@Param("user")User user);

	Teacher findOneByTeacherIdAndUser(String teacherId, User user);
} 
