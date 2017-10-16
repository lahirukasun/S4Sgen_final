package itech.s4sgen.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import itech.s4sgen.models.Role;

@Transactional
public interface RoleDao extends CrudRepository<Role, Long>{
	Role findByRole(String role);

}
