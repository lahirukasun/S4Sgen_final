package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Reservation;
import itech.s4sgen.models.User;


@Transactional
public interface ReservationDao extends CrudRepository<Reservation, Long> {

	@Query("Select st from Reservation st WHERE st.user IN :user")
	List<Reservation> findAllByUser(@Param("user")User user);

} 
