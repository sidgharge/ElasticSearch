package com.bridgelabz.location.repository;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	
}
