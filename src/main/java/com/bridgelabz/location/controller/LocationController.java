package com.bridgelabz.location.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.location.model.Location;
import com.bridgelabz.location.model.LocationDto;
import com.bridgelabz.location.repository.LocationRepository;
import com.bridgelabz.utility.ElasticUtility;

@RestController
public class LocationController {
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	ElasticUtility elasticUtility;

	@RequestMapping("/addall")
	public void addFromDatabase() {
		Map<String, LocationDto> locationMap = new HashMap<>();
		
		Iterable<Location> locations = locationRepository.findAll();
		int count = 0;
		for (Location location : locations) {
			LocationDto dto = new LocationDto();
			dto.copy(location);
			locationMap.put(String.valueOf(dto.getLocationId()), dto);
			count++;
			if (count % 10 == 0) {
				System.out.println("Done: " + count);
			}
		}
		
		try {
			System.out.println("Size: " + locationMap.size());
			elasticUtility.bulkRequest("loc", "loc", LocationDto.class, locationMap);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERRR.....");
		}
	}
	
	
	@RequestMapping("/nearby/{lat}/{lon}/{distance}")
	public List<LocationDto> getNearBy(@PathVariable float lat, @PathVariable float lon, @PathVariable String distance){
		try {
			List<LocationDto> locations = elasticUtility.getNearByLocations("loc", "loc", LocationDto.class, lat, lon, distance);
			return locations;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
