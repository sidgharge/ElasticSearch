package com.bridgelabz.elastic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.elastic.repository.ElasticRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ElasticService {

	@Autowired
	ElasticRepository elasticRepository;

	public <T> String save(T object, String index, String type, String id) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(object);
		return elasticRepository.save(json, index, type, id);
	}

	public <T> T getById(String index, String type, String id, Class<T> className)
			throws JsonParseException, JsonMappingException, IOException {

		String json = elasticRepository.getById(index, type, id);

		ObjectMapper mapper = new ObjectMapper();

		T object = mapper.readValue(json, className);

		return object;
	}

	public String deleteById(String index, String type, String id) throws IOException {
		Result result = elasticRepository.deleteById(index, type, id);
		return result.toString();
	}

	public String update(String index, String type, String id, Map<String, Object> dataMap) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(dataMap);

		Result result = elasticRepository.update(index, type, id, json);

		return result.toString();
	}

	public <T> List<T> searchByIdAndText(String index, String type, Class<T> classType,
			Map<String, Object> restrictions, String text, Map<String, Float> fields) throws IOException {
		text = "*" + text + "*";

		SearchHits hits = elasticRepository.searchByIdAndText(index, type, restrictions, text, fields);

		List<T> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		
		for (SearchHit hit : hits) {
			results.add(mapper.readValue(hit.getSourceAsString(), classType));
		}
		
		return results;

	}
}
