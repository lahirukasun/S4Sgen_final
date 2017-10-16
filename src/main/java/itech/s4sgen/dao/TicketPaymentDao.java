package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.TicketPayment;
import itech.s4sgen.models.User;


@Transactional
public interface TicketPaymentDao extends CrudRepository<TicketPayment, Long> {

	@Query("Select st from TicketPayment st WHERE st.user IN :user")
	List<TicketPayment> findAllByUser(@Param("user")User user);

} 
