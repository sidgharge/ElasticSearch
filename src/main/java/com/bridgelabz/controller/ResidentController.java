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

import org.dom4j.DocumentException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.model.JmsObject;
import com.bridgelabz.model.Resident;
import com.bridgelabz.model.ServiceProvider;
import com.bridgelabz.utility.ElasticUtility;

@RestController
public class ResidentController {

	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	ElasticUtility elasticUtility;

	@PreDestroy
	public void closeClient() {
		System.out.println("Closing client....");
		try {
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/resident")
	public void addResident(@RequestBody Resident resident) {

		try {
			elasticUtility.saveAsync(resident, "resident", "resident", String.valueOf(resident.getResidentId()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*JmsObject<Resident> jmsObject = new JmsObject<>();
		jmsObject.setId(String.valueOf(resident.getResidentId())).setIndex("resident").setType("resident")
				.setObject(resident);

		jmsTemplate.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createObjectMessage(jmsObject);
				return message;
			}
		});*/

	}

	@PutMapping("/resident/{residentId}")
	public Result updateResident(@RequestBody Map<String, Object> map, @PathVariable String residentId) {
		try {
			return elasticUtility.update("resident", "resident", residentId, map);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("/resident/{residentId}")
	public Resident getResidentById(@PathVariable String residentId) {

		try {
			return elasticUtility.getById("resident", "resident", residentId, Resident.class);
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
			return null;
		}

	}

	@DeleteMapping("resident/{residentId}")
	public Result deleteSP(@PathVariable String residentId) {

		try {
			return elasticUtility.deleteById("resident", "resident", residentId);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping("provider/resident/{residentId}/text/{text}")
	public List<ServiceProvider> searchResidents(@PathVariable String residentId, @PathVariable("text") String text) {

		Map<String, Object> restrictions = new HashMap<>();
		restrictions.put("residentId", residentId);

		Map<String, Float> fields = new HashMap<>();
		fields.put("name", 1.0f);

		try {
			List<ServiceProvider> residents = elasticUtility.searchOnFieldsWithRestrictions("provider", "provider", ServiceProvider.class,
					restrictions, text, fields);
			return residents;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@GetMapping("/resident/text/{text}")
	public List<Resident> search(@PathVariable String text){
		try {
			text = text + "*";
			//return elasticUtility.searchByText("resident", "resident", Resident.class, text);
			return elasticUtility.queryStringQuery("resident", "resident", Resident.class, text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/resident/filter/{name}/{text}")
	public List<Resident> filteredSearch(@PathVariable String text, @PathVariable String name){
		try {
			return elasticUtility.filteredQuery("resident", "resident", Resident.class, name, text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PostMapping("/sp/bulk")
	public void bulkRequest(@RequestBody List<ServiceProvider> providers) {
		try {
			Map<String, ServiceProvider> dataMap = new HashMap<>();
			for (ServiceProvider serviceProvider : providers) {
				dataMap.put(String.valueOf(serviceProvider.getSpId()), serviceProvider);
			}
			elasticUtility.bulkRequest("provider", "provider", ServiceProvider.class, dataMap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
