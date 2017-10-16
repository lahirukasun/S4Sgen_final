package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Patient;
import itech.s4sgen.models.PatientPayment;


@Transactional
public interface PatientPaymentDao extends CrudRepository<PatientPayment, Long> {

	@Query("Select st from PatientPayment st WHERE st.patient IN :patient")
	List<PatientPayment> findAllByPatient(@Param("patient")Patient patient);

} 
