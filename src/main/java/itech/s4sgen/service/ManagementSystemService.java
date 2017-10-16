package itech.s4sgen.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itech.s4sgen.dao.ManagementSystemDao;
import itech.s4sgen.dto.ManagementSystemDto;
import itech.s4sgen.models.ManagementSystem;

@Service
public class ManagementSystemService {

	@Autowired
	private ManagementSystemDao managementSystemDao;
	
	public ManagementSystem getOneById(long id) {
		return managementSystemDao.findOne(id);
	}
	
	public List<ManagementSystemDto> getAllManagementSystems(){
		List<ManagementSystem> systems;
		systems = (List<ManagementSystem>) managementSystemDao.findAll();
		
		List<ManagementSystemDto> systemsDto = systems.stream().map(ManagementSystemService::convertToDto).collect(Collectors.toList());
		
		return systemsDto;
	}
	
	public static ManagementSystemDto convertToDto(ManagementSystem system) {
		ManagementSystemDto dto = new ManagementSystemDto();
		dto.setId(system.getId());
		dto.setName(system.getName());
		dto.setDescription(system.getDescription());
		dto.setLogo(system.getLogo());
		return dto;
	}
	
}
