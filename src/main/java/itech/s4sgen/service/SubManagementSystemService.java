package itech.s4sgen.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itech.s4sgen.dao.SubManagementSystemDao;
import itech.s4sgen.dto.SubManagementSystemDto;
import itech.s4sgen.models.ManagementSystem;
import itech.s4sgen.models.SubManagementSystem;

@Service
public class SubManagementSystemService {

	@Autowired
	private SubManagementSystemDao subManagementSystemDao;
	
	public SubManagementSystem getOneById(long id) {
		return subManagementSystemDao.findOne(id);
	}
	
	public List<SubManagementSystemDto> getAllByManagementSystem(ManagementSystem managementSystem){
		List<SubManagementSystem> systems;
		systems = (List<SubManagementSystem>) subManagementSystemDao.findAllByManagementSystem(managementSystem);
		
		List<SubManagementSystemDto> systemsDto = systems.stream().map(SubManagementSystemService::convertToDto).collect(Collectors.toList());
		
		return systemsDto;
	}
	
	public static SubManagementSystemDto convertToDto(SubManagementSystem system) {
		SubManagementSystemDto dto = new SubManagementSystemDto();
		dto.setId(system.getId());
		dto.setName(system.getName());
		dto.setLogo(system.getLogo());
		return dto;
	}
	
}
