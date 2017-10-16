package itech.s4sgen.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import itech.s4sgen.dao.SystemFeaturesDao;
import itech.s4sgen.dto.SystemFeaturesDto;
import itech.s4sgen.models.SubManagementSystem;
import itech.s4sgen.models.SystemFeatures;

@Service
public class SystemFeaturesService {

	@Autowired
	private SystemFeaturesDao systemFeaturesDao;
	
	
	public List<SystemFeaturesDto> getAllBySubManagementSystem(SubManagementSystem subManagementSystem){
		List<SystemFeatures> systems;
		
		systems = (List<SystemFeatures>) systemFeaturesDao.findAllBySubManagementSystem(subManagementSystem);
		List<SystemFeaturesDto> systemsDto = systems.stream().map(SystemFeaturesService::convertToDto).collect(Collectors.toList());
		
		return systemsDto;
	}
	
	public static SystemFeaturesDto convertToDto(SystemFeatures system) {
		SystemFeaturesDto dto = new SystemFeaturesDto();
		dto.setId(system.getId());
		dto.setFeature(system.getFeature());
		return dto;
	}
}
