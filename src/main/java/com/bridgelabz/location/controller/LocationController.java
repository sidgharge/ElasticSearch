package com.bridgelabz.location.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.location.model.HousingComplex;
import com.bridgelabz.location.model.LatLng;
import com.bridgelabz.location.model.Location;
import com.bridgelabz.location.model.LocationDetails;
import com.bridgelabz.location.model.Location;
import com.bridgelabz.location.repository.ComplexRepository;
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
	ComplexRepository complexRepository;

	/*
	 * @Autowired Location2Repository location2Repository;
	 */

	@Autowired
	GoogleMapService service;

	/**
	 * Adds all the documents from database to elasticsearch index
	 */
	@RequestMapping("/addall")
	public void addFromDatabase() {
		Map<String, Location> locationMap = new HashMap<>();

		Iterable<Location> locations = locationRepository.findAll();
		int count = 0;
		for (Location location : locations) {
			locationMap.put(String.valueOf(location.getLocationId()), location);
			count++;
			if (count % 10 == 0) {
				System.out.println("Done: " + count);
			}
		}

		try {
			System.out.println("Size: " + locationMap.size());
			elasticUtility.bulkRequest("loc", "loc", Location.class, locationMap);
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
	@GetMapping("/nearby/{lat}/{lon}/{distance}")
	public List<Location> getNearBy(@PathVariable float lat, @PathVariable float lon, @PathVariable String distance) {
		try {
			List<Location> locations = elasticUtility.getNearByLocations("loc", "loc", Location.class, lat, lon,
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
	/*
	 * public void deleteDuplicates() { Iterable<Location> locations =
	 * locationRepository.findAll();
	 * 
	 * List<Location> list = new LinkedList<>(); locations.forEach(list::add);
	 * 
	 * List<Location2> list2 = new LinkedList<>();
	 * 
	 * for (int i = 0; i < list.size(); i++) { boolean add = true; for (int j = i +
	 * 1; j < list.size(); j++) { if (list.get(i).equals(list.get(j))) { add =
	 * false; break; } } if (add) { Location2 loc = new Location2();
	 * loc.copy(list.get(i)); list2.add(loc); } }
	 * 
	 * location2Repository.save(list2);
	 * 
	 * }
	 */

	@GetMapping("/addPlaces")
	public String addPlaces() throws IOException {
		Iterable<Location> locations = locationRepository.findAll();
		int counter = 0;
		int addedlocations = 0;
		for (Location location : locations) {
			if (counter < 50) {
				counter++;
				continue;
			}
			if (counter >= 70) {
				break;
			}

			counter++;
			List<LocationDetails> details = service.getNearByPlaces(location.getLatLng());
			System.out.println("Got details from map...");
			for (LocationDetails locationDetails : details) {
				List<Location> nearByLocations = elasticUtility.getNearByLocations("loc", "loc", Location.class,
						(float) locationDetails.getLocation().getLat(), (float) locationDetails.getLocation().getLon(),
						"1000");
				System.out.println("Nearby location count: " + nearByLocations.size());
				if (nearByLocations == null || nearByLocations.isEmpty()) {
					Location newLocation = new Location();
					newLocation.copyFromLocationDetails(location, locationDetails);
					newLocation = locationRepository.save(newLocation);
					elasticUtility.save(newLocation, "loc", "loc", String.valueOf(newLocation.getLocationId()));
					addedlocations++;
				}
			}
			System.out.println("Done: " + counter);
		}
		return "Got new location count: " + addedlocations;
	}

	@GetMapping("/housingcomplex")
	public void addHousingComplexes() {
		Iterable<Location> locations = locationRepository.findAll();

		for (Location location : locations) {
			try {
				List<LocationDetails> complexes = service.getHousingComplexes(location.getLatLng());
				for (LocationDetails locationDetails : complexes) {
					HousingComplex complex = new HousingComplex();
					complex.setLocationId(location.getLocationId());
					complex.setComplexName(locationDetails.getName());
					complex.setLatLng(locationDetails.getLocation());
					complex = complexRepository.save(complex);
					elasticUtility.save(complex, "complex", "complex", String.valueOf(complex.getComplexId()));
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	@GetMapping("/update")
	public void updateLocations() {
		Iterable<Location> locations = locationRepository.findAll();
		int counter = 0;
		int changedPos = 0;
		for (Location location : locations) {
			try {
				counter++;
				if (counter % 10 == 0) {
					System.out.println(counter);
				}
				/*
				 * Map<String, String> locationInfo =
				 * service.getPlaceInfoFromLatLng(location.getLatLng().getLat(),
				 * location.getLatLng().getLon());
				 */
				
				if (counter < 86 && counter > 47) {
					Map<String, String> locationInfo = service.getSublocalityDetails(location.getLatLng().getLat(),
							location.getLatLng().getLon());
					boolean isChanged = false;
					if (locationInfo.get("zipcode") != null
							&& !locationInfo.get("zipcode").equals(String.valueOf(location.getZip()))) {
						
						location.setZip(Integer.parseInt(locationInfo.get("zipcode")));
						isChanged = true;
					}
					if (locationInfo.get("sublocality_level_1") != null
							&& !locationInfo.get("sublocality_level_1").equals(location.getArea())) {
						location.setArea(locationInfo.get("sublocality_level_1"));
						isChanged = true;
					}
					if (locationInfo.get("Level2") != null && !locationInfo.get("Level2").equals(location.getPlace())) {
						location.setPlace(locationInfo.get("Level2"));
						isChanged = true;
					}
					if (isChanged) {
						changedPos++;
						locationRepository.save(location);
						elasticUtility.save(location, "loc", "loc", String.valueOf(location.getLocationId()));

					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Changed count: " + changedPos);
	}

	@PostMapping("/test")
	public void test(@RequestBody Location loc) {
		LatLng latLng = loc.getLatLng();
		try {
			service.getPlaceInfoFromLatLng(latLng.getLat(), latLng.getLon());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Hello");
	}

	@GetMapping("/complex/{locationId}/{page}")
	public List<HousingComplex> getHousingComplexes(@PathVariable String locationId, @PathVariable int page) {
		try {
			int from = 5 * page;
			return elasticUtility.searchByTermAndValue("complex", "complex", HousingComplex.class, "locationId",
					locationId, from);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@CrossOrigin
	@RequestMapping("/latlng")
	public List<LatLng> getLatLngs() {
		Iterable<Location> locations = locationRepository.findAll();
		List<LatLng> latLngs = new ArrayList<>();
		for (Location location : locations) {
			latLngs.add(location.getLatLng());
		}
		return latLngs;
	}
}
