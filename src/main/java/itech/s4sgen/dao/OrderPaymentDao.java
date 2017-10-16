package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.OrderPayment;
import itech.s4sgen.models.User;


@Transactional
public interface OrderPaymentDao extends CrudRepository<OrderPayment, Long> {

	@Query("Select st from OrderPayment st WHERE st.user IN :user")
	List<OrderPayment> findAllByUser(@Param("user")User user);
	
	OrderPayment findOneByOrderId(int orderId);

} 
