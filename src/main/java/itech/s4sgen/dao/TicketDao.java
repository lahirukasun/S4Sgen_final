package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Ticket;
import itech.s4sgen.models.User;


@Transactional
public interface TicketDao extends CrudRepository<Ticket, Long> {

	@Query("Select st from Ticket st WHERE st.user IN :user")
	List<Ticket> findAllByUser(@Param("user")User user);

} 
