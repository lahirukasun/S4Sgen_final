package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.EmployeeSalary;
import itech.s4sgen.models.Employee;


@Transactional
public interface EmployeeSalaryDao extends CrudRepository<EmployeeSalary, Long> {

	@Query("Select st from EmployeeSalary st WHERE st.employee IN :employee")
	List<EmployeeSalary> findAllByEmployee(@Param("employee")Employee employee);

} 
