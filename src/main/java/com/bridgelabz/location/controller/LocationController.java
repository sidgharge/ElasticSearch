package com.bridgelabz.location.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.location.model.LatLng;
import com.bridgelabz.location.model.Location;
import com.bridgelabz.location.model.Location2;
import com.bridgelabz.location.model.LocationDetails;
import com.bridgelabz.location.model.LocationDto;
import com.bridgelabz.location.repository.Location2Repository;
import com.bridgelabz.location.repository.LocationRepository;
import com.bridgelabz.location.service.GoogleMapService;
import com.bridgelabz.utility.ElasticUtility;

@RestController
public class LocationController {

	@Autowired
	LocationRepository locationRepository;

	@Autowired
	ElasticUtility elasticUtility;

	@Autowired
	Location2Repository location2Repository;

	@Autowired
	GoogleMapService service;

	/**
	 * Adds all the documents from database to elasticsearch index
	 */
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

	/**
	 * Gets nearby places from given lat lng stored in elasticsearch index
	 * 
	 * @param lat
	 * @param lon
	 * @param distance
	 * @return
	 */
	@RequestMapping("/nearby/{lat}/{lon}/{distance}")
	public List<LocationDto> getNearBy(@PathVariable float lat, @PathVariable float lon,
			@PathVariable String distance) {
		try {
			List<LocationDto> locations = elasticUtility.getNearByLocations("loc", "loc", LocationDto.class, lat, lon,
					distance);
			return locations;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// @RequestMapping("/create")
	/**
	 * create new table and adds unique entries to the table from old table
	 */
	public void deleteDuplicates() {
		Iterable<Location> locations = locationRepository.findAll();

		List<Location> list = new LinkedList<>();
		locations.forEach(list::add);

		List<Location2> list2 = new LinkedList<>();

		for (int i = 0; i < list.size(); i++) {
			boolean add = true;
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).equals(list.get(j))) {
					add = false;
					break;
				}
			}
			if (add) {
				Location2 loc = new Location2();
				loc.copy(list.get(i));
				list2.add(loc);
			}
		}

		location2Repository.save(list2);

	}

	@GetMapping("/addPlaces")
	public String addPlaces() throws IOException {
		Iterable<Location> locations = locationRepository.findAll();
		int counter = 0;
		int addedlocations = 0;
		for (Location location : locations) {
			/*if (counter < 40) {
				counter++;
				continue;
			}
			if (counter >= 49) {
				break;
			}*/

			counter++;
			List<LocationDetails> details = service.getNearByPlaces(new LatLng(location.getLat(), location.getLng()));
			System.out.println("Got details from map...");
			for (LocationDetails locationDetails : details) {
				List<LocationDto> dtos = elasticUtility.getNearByLocations("loc", "loc", LocationDto.class,
						(float) locationDetails.getLocation().getLat(), (float) locationDetails.getLocation().getLng(),
						"1000");
				System.out.println("Nearby location count: " + dtos);
				if (dtos == null || dtos.isEmpty()) {
					Location newLocation = new Location();
					newLocation.copyFromLocationDetails(location, locationDetails);
					newLocation = locationRepository.save(newLocation);
					/*LocationDto locationDto = new LocationDto();
					locationDto.copy(newLocation);*/
					elasticUtility.save(newLocation, "loc", "loc", String.valueOf(newLocation.getLocationId()));
					addedlocations++;
				}
			}
			System.out.println("Done: " + counter);
		}
		return "Got new location count: " + addedlocations;
	}
}
