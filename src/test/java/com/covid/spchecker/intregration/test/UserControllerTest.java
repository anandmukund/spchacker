package com.covid.spchecker.intregration.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import com.covid.spchacker.dto.RegisterUser;
import com.covid.spchacker.dto.UserLoginDto;
import com.covid.spchacker.dto.UserLoginResponse;
import com.covid.spchacker.entity.Patients;
import com.google.gson.Gson;

/*
 * this class is integration test so need to application in started state
 * when you run this test for user signup please always change the name and email id 
 * otherwise it will show as duplicate 
 *
 * 
 */

public class UserControllerTest {

	CloseableHttpClient client = null;
	public static String BASE_URL = "http://localhost:8080";
	@Before
	public void createInput() throws IOException {
		client = HttpClients.createDefault();
	}
	@Ignore
	@Test
	public void testIntregrationForSignupAndLogin() throws Exception {

		String url = BASE_URL + "/covid/user/signup";

		System.out.println("API URL: " + url);
		HttpPost httpPost = new HttpPost(url);
		RegisterUser user = new RegisterUser();
		user.setContactNo("1234567890");
		user.setUsername("itest");
		user.setFirstname("Tests");
		user.setPassword("123456789");
		user.setEmail("a@gmail.com");
		user.setRole("Admin");
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		//httpPost.addHeader("x-auth-token", API_KEY);
		Gson gson = new Gson();
		StringEntity content = new StringEntity(gson.toJson(user), ContentType.APPLICATION_JSON);
		httpPost.setEntity(content);
		CloseableHttpResponse response = client.execute(httpPost);


		if (response.getStatusLine().getStatusCode() == 200) { 
			String responseBody = EntityUtils.toString(response.getEntity());
			boolean result  = gson.fromJson(responseBody, Boolean.class);
			assertTrue(result);
		}
		else {
			throw new Exception("Error Updating data into AMS: " + response.getStatusLine().getStatusCode());
		}
		UserLoginDto dto = new UserLoginDto();
		dto.setPassword(user.getPassword());
		dto.setUsername(user.getUsername());
		StringEntity content1 = new StringEntity(gson.toJson(dto), ContentType.APPLICATION_JSON);
		String url1 = BASE_URL + "/covid/user/login";

		System.out.println("API URL: " + url1);
		HttpPost httpPost1 = new HttpPost(url1);
		httpPost1.setEntity(content1);
		CloseableHttpResponse response1 = client.execute(httpPost1);
		if (response1.getStatusLine().getStatusCode() == 200) { 
			String responseBody = EntityUtils.toString(response1.getEntity());
			UserLoginResponse result  = gson.fromJson(responseBody, UserLoginResponse.class);
			assertNotNull(result);
		}
	}

	
	@Test
	public void testIntregrationCovidDataEntry() throws Exception {

		Gson gson = new Gson();
		UserLoginDto dto = new UserLoginDto();
		dto.setPassword("123456789");
		dto.setUsername("itest");
		StringEntity content1 = new StringEntity(gson.toJson(dto), ContentType.APPLICATION_JSON);
		String url1 = BASE_URL + "/covid/user/login";
		UserLoginResponse result = null;
		System.out.println("API URL: " + url1);
		HttpPost httpPost1 = new HttpPost(url1);
		httpPost1.setEntity(content1);
		CloseableHttpResponse response1 = client.execute(httpPost1);
		if (response1.getStatusLine().getStatusCode() == 200) { 
			String responseBody = EntityUtils.toString(response1.getEntity());
			result = gson.fromJson(responseBody, UserLoginResponse.class);
			assertNotNull(result);
		}
		List<Patients> allPatients = new ArrayList<Patients>();
		Patients user = new Patients();
		user.setContactno("1234567890");
		user.setFirstname("Tests");
		user.setGender('M');
		user.setState("Bihar");
		user.setStatus("normal");
		allPatients.add(user);
		String url = BASE_URL + "/covid/data/covid";
		System.out.println("API URL: " + url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		httpPost.setHeader("auth", result.getAuth());
		//httpPost.addHeader("x-auth-token", API_KEY);
	
		StringEntity content = new StringEntity(gson.toJson(allPatients), ContentType.APPLICATION_JSON);
		httpPost.setEntity(content);
		CloseableHttpResponse response = client.execute(httpPost);


		if (response.getStatusLine().getStatusCode() == 200) { 
			String responseBody = EntityUtils.toString(response.getEntity());
			assertNotNull(responseBody);
		}
		else {
			throw new Exception("Error Updating data into AMS: " + response.getStatusLine().getStatusCode());
		}
		
	}
	
}
