/*package com.bridgelabz.listener;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.model.ServiceProvider;
import com.bridgelabz.utility.ElasticUtility;

public class ServiceProviderListener implements MessageListener {

	@Autowired
	RestHighLevelClient client;

	@Autowired
	ElasticUtility utility;

	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objectMessage = (ObjectMessage) message;
			ServiceProvider serviceProvider = (ServiceProvider) objectMessage.getObject();
			System.out.println("Got provider: " + serviceProvider);

			String id = utility.save(serviceProvider, "provider", "provider", String.valueOf(serviceProvider.getSpId()));
			System.out.println("Provider added at index: " + id);

		} catch (JMSException | IOException e) {
			e.printStackTrace();
		}
	}

}
*/