package com.bridgelabz.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.model.Resident;
import com.bridgelabz.utility.ElasticUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResidentListener implements MessageListener {
	
	@Autowired
	RestHighLevelClient client;
	
	@Autowired
	ElasticUtility utility;

	@Override
	public void onMessage(Message message) {
		
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			Resident resident = (Resident) objectMessage.getObject();
			System.out.println("Got resident: " + resident);
			
			String id = utility.save(resident, "resident", "resident", String.valueOf(resident.getResidentId()));
			System.out.println("Resident added at index: " + id);
			
		} catch (JMSException | IOException e) {
			e.printStackTrace();
		}
	}

}
