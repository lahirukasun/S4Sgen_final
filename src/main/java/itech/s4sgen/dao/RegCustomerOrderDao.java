package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Customer;
import itech.s4sgen.models.RegCustomerOrder;
import itech.s4sgen.models.User;


@Transactional
public interface RegCustomerOrderDao extends CrudRepository<RegCustomerOrder, Long> {

	@Query("Select st from RegCustomerOrder st WHERE st.user IN :user")
	List<RegCustomerOrder> findAllByUser(@Param("user")User user);
	
	@Query("Select st from RegCustomerOrder st WHERE st.customer IN :customer")
	List<RegCustomerOrder> findAllByCustomer(@Param("customer")Customer customer);

} 
