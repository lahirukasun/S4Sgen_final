package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Customer;
import itech.s4sgen.models.User;


@Transactional
public interface CustomerDao extends CrudRepository<Customer, Long> {

	@Query("Select st from Customer st WHERE st.user IN :user")
	List<Customer> findAllByUser(@Param("user")User user);

} 
