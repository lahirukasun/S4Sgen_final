package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import itech.s4sgen.models.Role;
import itech.s4sgen.models.User;


@Transactional
public interface UserDao extends CrudRepository<User, Long> {

  public User findByLogin(String login);

  /*@Query("SELECT u FROM User u LEFT JOIN on Role r WHERE u.login = :login AND r")
  public User findOneByLoginAndRoles(String login,Role roles);*/
  
  public User findOneByLoginAndRoles(String login,Role roles);
  
  /*@Query("SELECT u FROM User u WHERE u.id IN (SELECT ur.userId FROM Role ur WHERE ur.name = :role)")
  public List<User> findAllByRoles(Role roles);*/
  
  public List<User> findAllUserByRoles(Role roles);
  
} 
