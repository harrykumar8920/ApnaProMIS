package com.pams.service;

import java.net.URI;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
//https://attacomsian.com/blog/spring-boot-resttemplate-get-request-parameters-headers
//https://www.concretepage.com/spring-5/spring-resttemplate-postforentity
//https://www.geeksforgeeks.org/spring-resttemplate/
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.pams.dto.GetDataFromSNMSDTO;
import com.pams.dto.PersonDto;
import com.pams.dto.User;

@Controller
public class RestTemplateProvider {
	// @Value("${app.title}")
	private String appTitle = "444";

	@GetMapping("/value")
	public String getValue() {
		System.out.println(snmsapi);
		return appTitle;
	}

	@Value("${file.snmsapi1}")
	public String snmsapi;

	// Creating an instance of RestTemplate class
	RestTemplate rest = new RestTemplate();

	// Method
	public User getUserData() {
		return rest.getForObject("http://localhost:8080/RestApi/getData", User.class);
	}

	public String ApiCallForList1(String urlTest, String methodType, String consumeType) throws Exception

	{

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// URI url = new URI("http://localhost:9091/SNMS/user1");
		URI url = new URI(snmsapi);
		User objEmp = new User();
		objEmp.setUsername("dk@sfio.in");
		objEmp.setToken(
				"079cb782f77f8ebc31a8055e45f3faa19806b65560c46e29975789f6cc1d97e0dc711ce5ab57fc604bf6c7413792fa0e645235e6b205574eaddf9ced5803b6da");

		HttpEntity<User> requestEntity = new HttpEntity<>(objEmp, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url, requestEntity, User.class);

		System.out.println("Status Code: " + responseEntity.getStatusCode());
		System.out.println("Id: " + responseEntity.getBody().getToken());

		// String url1 = "http://localhost:9090/SNMS/ncrb/ncrbDtl";

		// create an instance of RestTemplate
		RestTemplate restTemplate1 = new RestTemplate();

		// create headers
		HttpHeaders headers1 = new HttpHeaders();

		// set `Content-Type` and `Accept` headers
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// example of custom header
		headers1.set("Authorization", responseEntity.getBody().getToken());

		// build the request
		HttpEntity request1 = new HttpEntity(headers1);

		// make an HTTP GET request with headers
		ResponseEntity<String> res = restTemplate1.exchange(urlTest, HttpMethod.GET, request1, String.class

		);
		String abc = res.getBody();
		return abc;

		/*
		 * JSONObject jsonObject = new JSONObject(abc);
		 * 
		 * List<String> list = new ArrayList<String>(); JSONArray jsonArray =
		 * jsonObject.getJSONArray("companyNCRBDTO");//AccusedNCRBDTO for(int i = 0 ; i
		 * < jsonArray.length(); i++) {
		 * list.add(jsonArray.getJSONObject(i).getString("nameOfCase"));
		 * System.out.println(jsonArray.getJSONObject(i).getString("nameOfCase")); }
		 * 
		 * JSONObject obj = null; if( (obj =
		 * jsonObject.optJSONObject("accusedNCRBDTO"))!=null ){ // it's an error , now
		 * you can fetch the error object values from obj }
		 * 
		 * else { System.out.println("abc"); }
		 * 
		 * // check response if (res.getStatusCode() == HttpStatus.OK) {
		 * System.out.println("Request Successful."); System.out.println(res.getBody());
		 * } else { System.out.println("Request Failed");
		 * System.out.println(res.getStatusCode());
		 */
	}

	// Method
	public String ApiCallForList(GetDataFromSNMSDTO getDataFromSNMSDTO, String urlTest, String methodType,
			String consumeType) throws Exception

	{

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		URI url = new URI(snmsapi);
		User objEmp = new User();
		objEmp.setUsername("dk@sfio.in");
		objEmp.setToken(
				"339bddc0b3ca82b5b4193928b2099c62399b1e6a8ea91e4221fcc70e86fa27598939030c950c1790da9bf099cd279a7b894b4777ca4d0bc4f1afdd8e260dfbbe");

		HttpEntity<User> requestEntity = new HttpEntity<>(objEmp, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url, requestEntity, User.class);

		System.out.println("Status Code: " + responseEntity.getStatusCode());
		System.out.println("Id: " + responseEntity.getBody().getToken());

		// create headers
		HttpHeaders headers1 = new HttpHeaders();

		// set `Content-Type` and `Accept` headers
		headers1.setContentType(MediaType.APPLICATION_JSON);
		// headers1.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// example of custom header
		headers1.set("Authorization", responseEntity.getBody().getToken());

		// build the request

		HttpEntity<GetDataFromSNMSDTO> request1 = new HttpEntity<>(getDataFromSNMSDTO, headers1);

		ResponseEntity<String> res = restTemplate.postForEntity(urlTest, request1, String.class);

		// make an HTTP GET request with headers
		/*
		 * ResponseEntity<String> res = restTemplate1.exchange( urlTest, HttpMethod.GET,
		 * request1, String.class
		 * 
		 * );
		 */

		return res.getBody();

	}

	public HttpStatusCode updatePerson(Integer personid, String snmsapi) throws Exception {

		// String snmsapi = configProp.getConfigValue("app.title");

		final String baseUrl = snmsapi + "/caseId/"
				+ "/user?userName=dk@sfio.in&password=079cb782f77f8ebc31a8055e45f3faa19806b65560c46e29975789f6cc1d97e0dc711ce5ab57fc604bf6c7413792fa0e645235e6b205574eaddf9ced5803b6da";
		// final String baseUrl =
		// "http://localhost:9090/SNMS/user?userName=dk@sfio.in&password=079cb782f77f8ebc31a8055e45f3faa19806b65560c46e29975789f6cc1d97e0dc711ce5ab57fc604bf6c7413792fa0e645235e6b205574eaddf9ced5803b6da";
		// URI uri = new URI(baseUrl);
		// User employee = new
		// User("dk@sfio.in","079cb782f77f8ebc31a8055e45f3faa19806b65560c46e29975789f6cc1d97e0dc711ce5ab57fc604bf6c7413792fa0e645235e6b205574eaddf9ced5803b6da"
		// );

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		URI url = new URI(snmsapi + "/user1");
		User objEmp = new User();
		objEmp.setUsername("dk@sfio.in");
		objEmp.setToken(
				"079cb782f77f8ebc31a8055e45f3faa19806b65560c46e29975789f6cc1d97e0dc711ce5ab57fc604bf6c7413792fa0e645235e6b205574eaddf9ced5803b6da");

		HttpEntity<User> requestEntity = new HttpEntity<>(objEmp, headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<User> responseEntity = restTemplate.postForEntity(url, requestEntity, User.class);

		System.out.println("Status Code: " + responseEntity.getStatusCode());
		System.out.println("Id: " + responseEntity.getBody().getToken());

		// String url1 = "http://localhost:9090/SNMS/updateInGep";

		URI url1 = new URI(snmsapi + "/updateInGep");

		// create an instance of RestTemplate
		// RestTemplate restTemplate1 = new RestTemplate();

		PersonDto personDto = new PersonDto();
		personDto.setPersonId(personid);

		// create headers
		HttpHeaders headers1 = new HttpHeaders();

		// set `Content-Type` and `Accept` headers
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		// example of custom header
		headers1.set("Authorization", responseEntity.getBody().getToken());

		// build the request
		// HttpEntity request1 = new HttpEntity(personDto,headers1);

		HttpEntity<PersonDto> request1 = new HttpEntity<>(personDto, headers1);

		// make an HTTP GET request with headers
		ResponseEntity<PersonDto> res = restTemplate.postForEntity(url1, request1, PersonDto.class);

		System.out.println("Status Code: " + res.getBody().getPersonId());
		System.out.println("Status Code: " + responseEntity.getStatusCode());
		return responseEntity.getStatusCode();

	}

}
