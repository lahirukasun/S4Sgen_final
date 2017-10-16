package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.SubManagementSystem;
import itech.s4sgen.models.SystemFeatures;


@Transactional
public interface SystemFeaturesDao extends CrudRepository<SystemFeatures, Long> {
	
	@Query("Select sf from SystemFeatures sf WHERE sf.parentSystem IN :subManagementSystem")
	List<SystemFeatures> findAllBySubManagementSystem(@Param("subManagementSystem")SubManagementSystem subManagementSystem);
} 
