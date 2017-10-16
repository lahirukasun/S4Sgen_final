package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Doctor;
import itech.s4sgen.models.User;


@Transactional
public interface DoctorDao extends CrudRepository<Doctor, Long> {

	@Query("Select st from Doctor st WHERE st.user IN :user")
	List<Doctor> findAllByUser(@Param("user")User user);

} 
