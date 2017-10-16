package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Appointment;
import itech.s4sgen.models.User;


@Transactional
public interface AppointmentDao extends CrudRepository<Appointment, Long> {

	@Query("Select st from Appointment st WHERE st.user IN :user")
	List<Appointment> findAllByUser(@Param("user")User user);

	@Query("Select st from Appointment st WHERE st.patientName IN :patientName")
	List<Appointment> findAllByPatientName(@Param("patientName")String patientName);
	
	@Query("Select st from Appointment st WHERE st.user IN :user AND st.checked = :check")
	List<Appointment> findAllByUserAndChecked(@Param("user")User user , @Param("check") boolean check);
} 
