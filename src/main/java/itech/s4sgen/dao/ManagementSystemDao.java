package itech.s4sgen.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import itech.s4sgen.models.ManagementSystem;


@Transactional
public interface ManagementSystemDao extends CrudRepository<ManagementSystem, Long> {
	
} 
