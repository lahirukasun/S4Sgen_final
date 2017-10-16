package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.ManagementSystem;
import itech.s4sgen.models.SubManagementSystem;


@Transactional
public interface SubManagementSystemDao extends CrudRepository<SubManagementSystem, Long> {
	
	@Query("Select sms from SubManagementSystem sms WHERE sms.parentSystem IN :managementSystem")
	List<SubManagementSystem> findAllByManagementSystem(@Param("managementSystem") ManagementSystem managementSystem);
	
} 
