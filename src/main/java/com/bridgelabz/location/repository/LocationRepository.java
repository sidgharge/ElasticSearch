package com.bridgelabz.location.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.location.model.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long>{

	@Query("from Location")
	public List<Location> getAll();
}
