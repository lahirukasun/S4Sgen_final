package itech.s4sgen.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.SystemDetail;
import itech.s4sgen.models.User;


@Transactional
public interface SystemDetailDao extends CrudRepository<SystemDetail, Long> {

	@Query("Select st from SystemDetail st WHERE st.user = :user")
	SystemDetail findOneByUser(@Param("user")User user);

} 
