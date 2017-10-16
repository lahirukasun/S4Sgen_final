package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Patient;
import itech.s4sgen.models.User;


@Transactional
public interface PatientDao extends CrudRepository<Patient, Long> {

	@Query("Select st from Patient st WHERE st.user IN :user")
	List<Patient> findAllByUser(@Param("user")User user);

	Patient findOneByAdmitId(String admitId);
} 
