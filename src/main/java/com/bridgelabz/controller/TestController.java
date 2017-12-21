package com.bridgelabz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Resident;
import com.bridgelabz.utility.ElasticUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TestController {
	
	@Autowired
	RestHighLevelClient client;
	
	@Autowired
	ElasticUtility utility;

	@RequestMapping("/fuzzy/{name}")
	public List<Resident> searchResponse(@PathVariable String name) {
		
		List<Resident> residents = null;
		try {
			residents = utility.fuzzyNameSearch("resident", "resident", "name", name, Resident.class);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return residents;
	}
	
}
