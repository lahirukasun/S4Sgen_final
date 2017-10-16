package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Student;
import itech.s4sgen.models.StudentFee;


@Transactional
public interface StudentFeeDao extends CrudRepository<StudentFee, Long> {

	@Query("Select st from StudentFee st WHERE st.student IN :student")
	List<StudentFee> findAllByStudent(@Param("student")Student student);

} 
