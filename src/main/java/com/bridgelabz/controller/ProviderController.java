package com.bridgelabz.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.JmsObject;
import com.bridgelabz.model.Resident;
import com.bridgelabz.model.ServiceProvider;
import com.bridgelabz.utility.ElasticUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProviderController {

	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	ElasticUtility elasticUtility;

	@PostMapping("/sp")
	public void addSP(@RequestBody ServiceProvider serviceProvider) {

		JmsObject<ServiceProvider> jmsObject = new JmsObject<>();
		jmsObject.setId(String.valueOf(serviceProvider.getSpId())).setIndex("provider").setType("provider")
				.setObject(serviceProvider);

		jmsTemplate.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createObjectMessage(jmsObject);
				return message;
			}
		});
	}

	@PutMapping("/provider/{providerId}")
	public Result updateResident(@RequestBody Map<String, Object> map, @PathVariable String providerId) {

		try {
			return elasticUtility.update("resident", "resident", providerId, map);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/provider/{providerId}")
	public ServiceProvider getProviderById(@PathVariable String providerId) {

		try {
			return elasticUtility.getById("provider", "provider", providerId, ServiceProvider.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@DeleteMapping("provider/{providerId}")
	public Result deleteSP(@PathVariable String providerId) {

		try {
			return elasticUtility.deleteById("provider", "provider", providerId);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("/fuzzy/{name}")
	public List<Resident> searchResponse(@PathVariable String name) {

		List<Resident> residents = null;
		try {
			residents = elasticUtility.fuzzyNameSearch("resident", "resident", "name", name, Resident.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return residents;
	}

	@GetMapping("resident/sp/{spId}/text/{text}")
	public List<Resident> searchResidents(@PathVariable String spId, @PathVariable("text") String text) {

		Map<String, Object> restrictions = new HashMap<>();
		restrictions.put("spId", spId);

		Map<String, Float> fields = new HashMap<>();
		fields.put("name", 1.0f);
		fields.put("mob", 3.0f);
		fields.put("houseInfo", 1.0f);
		fields.put("nickName", 1.0f);
		fields.put("altMob", 2.5f);

		try {
			List<Resident> residents = elasticUtility.searchByIdAndText("resident", "resident", Resident.class,
					restrictions, text, fields);
			return residents;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
