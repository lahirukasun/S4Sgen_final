package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Student;
import itech.s4sgen.models.User;


@Transactional
public interface StudentDao extends CrudRepository<Student, Long> {

	@Query("Select st from Student st WHERE st.user IN :user")
	List<Student> findAllByUser(@Param("user")User user);

	Student findStudentByRollNoAndUser(String rollNo,User user);
} 
