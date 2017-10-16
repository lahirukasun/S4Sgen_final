package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Employee;
import itech.s4sgen.models.EmployeeAttendance;


@Transactional
public interface EmployeeAttendanceDao extends CrudRepository<EmployeeAttendance, Long> {

	@Query("Select st from EmployeeAttendance st WHERE st.employee IN :employee")
	List<EmployeeAttendance> findAllByEmployee(@Param("employee")Employee employee);

} 
