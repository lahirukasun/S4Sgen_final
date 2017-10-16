package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.Journey;
import itech.s4sgen.models.User;


@Transactional
public interface JourneyDao extends CrudRepository<Journey, Long> {

	@Query("Select st from Journey st WHERE st.user IN :user")
	List<Journey> findAllByUser(@Param("user")User user);

} 
