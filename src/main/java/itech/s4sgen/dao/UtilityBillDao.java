package itech.s4sgen.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import itech.s4sgen.models.User;
import itech.s4sgen.models.UtilityBill;


@Transactional
public interface UtilityBillDao extends CrudRepository<UtilityBill, Long> {

	@Query("Select st from UtilityBill st WHERE st.user IN :user")
	List<UtilityBill> findAllByUser(@Param("user")User user);

} 
