package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Employee;
import itech.s4sgen.models.User;


@Transactional
public interface EmployeeDao extends CrudRepository<Employee, Long> {

	@Query("Select st from Employee st WHERE st.user IN :user")
	List<Employee> findAllByUser(@Param("user")User user);

} 
