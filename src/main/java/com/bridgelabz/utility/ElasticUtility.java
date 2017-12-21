package com.bridgelabz.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ElasticUtility {

	@Autowired
	RestHighLevelClient client;

	public <T> String save(T object, String index, String type, String id) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(object);
		IndexRequest indexRequest = new IndexRequest(index, type, id);
		indexRequest.source(json, XContentType.JSON);
		IndexResponse indexResponse = client.index(indexRequest);

		return indexResponse.getId();
	}

	public <T> List<T> fuzzyNameSearch(String index, String type, String name, String value, Class<T> className) throws IOException {
		FuzzyQueryBuilder fuzzyBuilder = QueryBuilders.fuzzyQuery(name, value).fuzziness(Fuzziness.fromEdits(2));
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(fuzzyBuilder);
		SearchRequest request = new SearchRequest(index);
		request.types(type);
		request.source(builder);
		
		SearchResponse response = client.search(request);
		
		List<T> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		response.getHits().forEach(hit -> {
			try {
				results.add(mapper.readValue(hit.getSourceAsString(), className));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return results;
	}

}
