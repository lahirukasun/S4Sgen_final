package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.User;
import itech.s4sgen.models.MaintenanceStaff;


@Transactional
public interface MaintenanceStaffDao extends CrudRepository<MaintenanceStaff, Long> {

	@Query("Select st from MaintenanceStaff st WHERE st.user IN :user")
	List<MaintenanceStaff> findAllByUser(@Param("user")User user);

} 
