package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Order;
import itech.s4sgen.models.User;


@Transactional
public interface OrdersDao extends CrudRepository<Order, Long> {

	@Query("Select st from Order st WHERE st.user IN :user")
	List<Order> findAllByUser(@Param("user")User user);

} 
