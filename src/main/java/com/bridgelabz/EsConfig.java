package com.bridgelabz;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig {

	/*@Bean
	public TransportClient transportClient() {
		TransportClient client = null;
		try {
			Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

			IndicesExistsRequest existsRequest = new IndicesExistsRequest("resident");

			//if (!client.admin().indices().exists(existsRequest).actionGet().isExists()) {
			if(true) {
				CreateIndexRequest createIndexRequest = new CreateIndexRequest("loc");
				Map<String, String> map = new HashMap<>();
				map.put("location", );
				createIndexRequest.mapping("loc", map);

				createIndexRequest.settings(
						Settings.builder().put("index.number_of_shards", 2).put("index.number_of_replicas", 0));

				CreateIndexResponse resp = client.admin().indices().create(createIndexRequest).actionGet();
				System.out.println("Index created: " + resp.isAcknowledged());
				client.close();
			} else {
				System.out.println("Index already exists");
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return client;
	}*/

	@Bean
	public RestHighLevelClient restClient() throws UnknownHostException {
		RestHighLevelClient client = null;
		client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

		return client;
	}

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Bean
	public SessionFactory getSessionFactory() {
		if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
			throw new NullPointerException("SessionFactory is not a hibernate sessionfactory");
		}
		return entityManagerFactory.unwrap(SessionFactory.class);
	}

}
