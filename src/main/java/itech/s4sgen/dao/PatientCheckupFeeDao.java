package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.PatientCheckupFee;
import itech.s4sgen.models.User;


@Transactional
public interface PatientCheckupFeeDao extends CrudRepository<PatientCheckupFee, Long> {

	@Query("Select st from PatientCheckupFee st WHERE st.user IN :user")
	List<PatientCheckupFee> findAllByUser(@Param("user")User user);

} 
