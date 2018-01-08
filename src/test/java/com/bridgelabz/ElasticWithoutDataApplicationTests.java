package com.bridgelabz;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.model.Resident;
import com.bridgelabz.model.ServiceProvider;
import com.bridgelabz.utility.ElasticUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticWithoutDataApplicationTests {

	/*MockMvc mockMvc;
	
	WebApplicationContext context;
	
	@InjectMocks
	ElasticUtility elasticUtility;
	
	@Autowired
	@Spy
	RestHighLevelClient client;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	@Ignore
	public void save() throws IOException {
		ServiceProvider sp = new ServiceProvider();
		sp.setName("Natraj");
		sp.setSpId(1);
		
		String id =elasticUtility.save(sp, "provider", "provider", String.valueOf(sp.getSpId()));
		Assert.assertEquals("1", id);
	}
	
	@Test
	@Ignore
	public void getById() throws JsonParseException, JsonMappingException, IOException {
		Resident actualResident = elasticUtility.getById("resident", "resident", String.valueOf(1), Resident.class);
		
		Resident expectedResident = new Resident();
		expectedResident.setResidentId(1);
		expectedResident.setName("Sid");
		expectedResident.setNickName("Akshay");
		expectedResident.setMob("5465666");
		expectedResident.setAltMob("1212121562");
		expectedResident.setHouseInfo("Satara");
		expectedResident.setSpId(2);
		
		Assert.assertEquals(expectedResident, actualResident);
	}
	
	@Test
	@Ignore
	public void deleteById() throws IOException {
		
		Result actualResult= elasticUtility.deleteById("provider", "provider", "1");
		Assert.assertEquals(Result.DELETED, actualResult);
	}
	
	@Test
	@Ignore
	public void update() throws IOException {
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("name", "Siddharth");
		dataMap.put("mob", "7219159877");
		dataMap.put("spId",1);
		Result actualResult = elasticUtility.update("resident", "resident", "1", dataMap);
		
		Assert.assertEquals(Result.UPDATED, actualResult);
	}
	
	@Test
	@Ignore
	public void searchByIdAndText() throws IOException {
		Map<String, Object> restrictions = new HashMap<>();
		restrictions.put("spId", 1);
		Map<String, Float> fields = new HashMap<>();
		fields.put("name", 1.0f);
		fields.put("mob", 3.0f);
		fields.put("houseInfo", 1.0f);
		fields.put("nickName", 1.0f);
		fields.put("altMob", 2.5f);
		List<Resident> residents = elasticUtility.searchOnFieldsWithRestrictions("resident", "resident", Resident.class, restrictions, "sa", fields);
		
		Assert.assertEquals(2, residents.size());
		
		Assert.assertEquals(2, residents.get(0).getResidentId());
		
		Assert.assertEquals(1, residents.get(1).getResidentId());
	}
	
	@Test
	public void searchByText() throws IOException {
		List<Resident> residents = elasticUtility.searchOnFields("resident", "resident", Resident.class, "sa");
		System.out.println(residents);
	}
*/
}
