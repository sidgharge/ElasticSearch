package com.bridgelabz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.Resident;
import com.bridgelabz.model.ServiceProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class HomeController {

	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@PreDestroy
	public void closeClient() {
		System.out.println("Closing client....");
		try {
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/sp")
	public String addSP(@RequestBody ServiceProvider serviceProvider) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(serviceProvider);
			IndexRequest indexRequest = new IndexRequest("elasticsp", "sp", String.valueOf(serviceProvider.getSpId()));
			indexRequest.source(json, XContentType.JSON);
			IndexResponse indexResponse = client.index(indexRequest);
			return indexResponse.getId();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "No!!!";
	}

	@PostMapping("/resident")
	public void addResident(@RequestBody Resident resident) {
		
		jmsTemplate.send(new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createObjectMessage(resident);
				return message;
			}
		});
		
	}

	@GetMapping("get/{id}")
	public ServiceProvider getSP(@PathVariable("id") int id) {

		ServiceProvider provider = null;
		try {
			GetRequest getRequest = new GetRequest("elasticsp", "sp", String.valueOf(id));
			GetResponse response = client.get(getRequest);
			ObjectMapper mapper = new ObjectMapper();

			provider = mapper.readValue(response.getSourceAsString(), ServiceProvider.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return provider;
	}

	@DeleteMapping("delete/{id}")
	public Result deleteSP(@PathVariable("id") int id) {
		DeleteRequest deleteRequest = new DeleteRequest("elasticsp", "sp", String.valueOf(id));
		DeleteResponse response;
		try {
			response = client.delete(deleteRequest);
			return response.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("search/sp/{spId}/text/{text}")
	public List<Resident> searchResidents(@PathVariable("spId") int spId, @PathVariable("text") String text) {

		text = "*" + text + "*";

		QueryBuilder builder = QueryBuilders
				.boolQuery().must(QueryBuilders.queryStringQuery(text).lenient(true).field("name").field("mob")
						.field("houseInfo").field("nickName").field("altMob"))
				.must(QueryBuilders.matchQuery("spId", spId));

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(builder);
		SearchRequest searchRequest = new SearchRequest("resident").types("resident").source(sourceBuilder);
		SearchResponse searchResponse;
		try {
			searchResponse = client.search(searchRequest);

			List<Resident> residents = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper();
			searchResponse.getHits().forEach(hit -> {
				try {
					residents.add(mapper.readValue(hit.getSourceAsString(), Resident.class));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			return residents;
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return null;
	}
	
	
}
