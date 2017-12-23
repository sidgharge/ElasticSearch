package com.bridgelabz.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

	public <T> T getById(String index, String type, String id, Class<T> className)
			throws JsonParseException, JsonMappingException, IOException {
		GetRequest getRequest = new GetRequest(index, type, id);
		GetResponse response = client.get(getRequest);
		ObjectMapper mapper = new ObjectMapper();

		T object = mapper.readValue(response.getSourceAsString(), className);

		return object;
	}

	public Result deleteById(String index, String type, String id) throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
		DeleteResponse response = client.delete(deleteRequest);
		return response.getResult();
	}

	public Result update(String index, String type, String id, Map<String, Object> dataMap) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(dataMap);
		UpdateRequest updateRequest = new UpdateRequest(index, type, id);
		updateRequest.doc(json, XContentType.JSON);
		UpdateResponse response = client.update(updateRequest);
		return response.getResult();
	}

	public <T> List<T> searchByIdAndText(String index, String type, Class<T> classType,
			Map<String, Object> restrictions, String text, Map<String, Float> fields) throws IOException {
		text = "*" + text + "*";

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder = boolQueryBuilder.must(QueryBuilders.queryStringQuery(text).lenient(true).fields(fields));

		restrictions.forEach((field, value) -> boolQueryBuilder.must(QueryBuilders.matchQuery(field, value)));

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(builder);
		SearchRequest searchRequest = new SearchRequest(index).types(type).source(sourceBuilder);
		SearchResponse searchResponse;
		searchResponse = client.search(searchRequest);

		List<T> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		searchResponse.getHits().forEach(hit -> {
			try {
				results.add(mapper.readValue(hit.getSourceAsString(), classType));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return results;

	}

	public <T> List<T> fuzzyNameSearch(String index, String type, String name, String value, Class<T> className)
			throws IOException {
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

	public <T> List<T> searchByText(String index, String type, Class<T> classType, String text) throws IOException {
		QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery(text);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(builder);
		SearchRequest request = new SearchRequest(index).types(type).source(sourceBuilder);
		SearchResponse response = client.search(request);
		List<T> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		response.getHits().forEach(hit -> {
			try {
				results.add(mapper.readValue(hit.getSourceAsString(), classType));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return results;
	}

	public <T> List<T> filteredQuery(String index, String type, Class<T> classType, String name, String text)
			throws IOException {
		QueryBuilder builder = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery(name, text));
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(builder);
		SearchRequest request = new SearchRequest(index).types(type).source(sourceBuilder);
		SearchResponse response = client.search(request);
		List<T> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		response.getHits().forEach(hit -> {
			try {
				results.add(mapper.readValue(hit.getSourceAsString(), classType));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return results;
	}

	public <T> void bulkRequest(String index, String type, Class<T> classType, Map<String, T> objects)
			throws IOException {

		BulkRequest bulkRequest = new BulkRequest();

		ObjectMapper mapper = new ObjectMapper();

		for (String id : objects.keySet()) {
			String json = mapper.writeValueAsString(objects.get(id));
			IndexRequest request = new IndexRequest(index, type, id);
			request.source(json, XContentType.JSON);
			bulkRequest.add(request);
		}

		/*
		 * for (T t : objects.entrySet()) { String json = mapper.writeValueAsString(t);
		 * IndexRequest request = new IndexRequest(index, type); request.source(json,
		 * XContentType.JSON); bulkRequest.add(request); }
		 */

		BulkResponse response = client.bulk(bulkRequest);
		System.out.println(response.getTook());
	}

	public <T> List<T> getNearByLocations(String index, String type, Class<T> classType, float lat, float lon,
			String distance) throws IOException {

		GeoDistanceQueryBuilder builder = QueryBuilders.geoDistanceQuery("location");
		builder.point(lat, lon);
		builder.distance(distance, DistanceUnit.METERS);
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(builder);
		SearchRequest request = new SearchRequest(index).types(type).source(sourceBuilder);
		SearchResponse response = client.search(request);
		List<T> results = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		for (SearchHit hit : response.getHits()) {
			results.add(mapper.readValue(hit.getSourceAsString(), classType));
		}
		/*response.getHits().forEach(hit -> {
			try {
				results.add(mapper.readValue(hit.getSourceAsString(), classType));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});*/
		return results;
	}

}
