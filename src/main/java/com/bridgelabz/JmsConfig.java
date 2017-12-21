package com.bridgelabz;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import com.bridgelabz.listener.ResidentListener;


@Configuration
//@EnableJms
public class JmsConfig {

	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		factory.setTrustAllPackages(true);
		return new CachingConnectionFactory(factory);
	}
	
	@Bean
	public Queue queue() {
		return new ActiveMQQueue("RESIDENT_QUEUE");
	}
	
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(cachingConnectionFactory());
		template.setDefaultDestination(queue());
		return template;
	}
	
	@Bean
	public ResidentListener residentListner() {
		return new ResidentListener();
	}
	
	@Bean
	public DefaultMessageListenerContainer consumer() {
		DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
		MessageListenerAdapter adapter = new MessageListenerAdapter();
		//ResidentListener listener = new ResidentListener();
		//adapter.setDefaultResponseQueueName("RESIDENT_QUEUE");
		adapter.setDelegate(residentListner());
		dmlc.setMessageListener(adapter);
		dmlc.setDestinationName("RESIDENT_QUEUE");
		dmlc.setConnectionFactory(cachingConnectionFactory());
		return dmlc;
	}
	
}
