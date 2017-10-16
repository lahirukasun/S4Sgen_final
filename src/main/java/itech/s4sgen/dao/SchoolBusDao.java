package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.User;
import itech.s4sgen.models.SchoolBus;


@Transactional
public interface SchoolBusDao extends CrudRepository<SchoolBus, Long> {

	@Query("Select st from SchoolBus st WHERE st.user IN :user")
	List<SchoolBus> findAllByUser(@Param("user")User user);

} 
