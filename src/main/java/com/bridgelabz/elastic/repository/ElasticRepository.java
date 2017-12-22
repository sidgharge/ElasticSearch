package com.bridgelabz.elastic.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse.Result;
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
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class ElasticRepository {

	@Autowired
	RestHighLevelClient client;
	
	public String save(String json, String index, String type, String id) throws IOException {

		IndexRequest indexRequest = new IndexRequest(index, type, id);
		indexRequest.source(json, XContentType.JSON);
		IndexResponse indexResponse = client.index(indexRequest);

		return indexResponse.getId();
	}
	
	public String getById(String index, String type, String id) throws IOException {
		GetRequest getRequest = new GetRequest(index, type, id);
		GetResponse response = client.get(getRequest);
		
		return response.getSourceAsString();
	}
	
	public Result deleteById(String index, String type, String id) throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
		DeleteResponse response = client.delete(deleteRequest);
		return response.getResult();
	}
	
	public Result update(String index, String type, String id, String json) throws IOException {
		UpdateRequest updateRequest = new UpdateRequest(index, type, id);
		updateRequest.doc(json, XContentType.JSON);
		UpdateResponse response = client.update(updateRequest);
		return response.getResult();
	}
	
	public SearchHits searchByIdAndText(String index, String type, 
			Map<String, Object> restrictions, String text, Map<String, Float> fields) throws IOException {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		QueryBuilder builder = boolQueryBuilder.must(QueryBuilders.queryStringQuery(text).lenient(true).fields(fields));

		restrictions.forEach((field, value) -> boolQueryBuilder.must(QueryBuilders.matchQuery(field, value)));

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(builder);
		SearchRequest searchRequest = new SearchRequest(index).types(type).source(sourceBuilder);
		SearchResponse searchResponse;
		searchResponse = client.search(searchRequest);

		return searchResponse.getHits();

	}
}
