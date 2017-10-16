package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Doctor;
import itech.s4sgen.models.DoctorAttendance;


@Transactional
public interface DoctorAttendanceDao extends CrudRepository<DoctorAttendance, Long> {

	@Query("Select st from DoctorAttendance st WHERE st.doctor IN :doctor")
	List<DoctorAttendance> findAllByDoctor(@Param("doctor")Doctor doctor);

} 
