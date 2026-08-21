package com.pams.controllers;

//import org.json.simple.JSONObject;
import java.io.IOException;
//import org.json.simple.JSONValue;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginPage {




@GetMapping("/parichayclient/login")
protected void login(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException
//public static void login(HttpServletRequest request,HttpServletResponse response) throws IOException, InterruptedException
{
   
    System.out.println("Login Func called.");
    long timeStamp = System.currentTimeMillis();
System.out.println("timeStamp : "+timeStamp);
String Service = ResourceBundle.getBundle("application").getString("SERVICE");
System.out.println("Service Name : " + Service);
String ParichayUrl = ResourceBundle.getBundle("application").getString("PARICHAY_URL");
System.out.println("ParichayUrl : " + ParichayUrl);
String hmac_sign = hmacFunc(timeStamp,Service,ParichayUrl);
System.out.println("hmac_response : " + hmac_sign);
String  encrpt_sign = encrptFunc();
        System.out.println("encryption_response : " + encrpt_sign);
        String loginURL = "http://parichay.staging.nic.in/pnv1/api/login?sid=" + Service + "&tid="
+ timeStamp + "&cs=" + hmac_sign + "&string=" + encrpt_sign + "&lang=ma";
        String loginURLtrim = loginURL.trim();
        System.out.println("loginURL trim2 : " + loginURLtrim);
        response.sendRedirect(loginURLtrim);
    

}

private static String encrptFunc() throws IOException, InterruptedException {
    System.out.println("encrptFunc called");

    // Read from application.properties
    String ENCR_URL = ResourceBundle.getBundle("application").getString("ENCR_URL");
    String requestBody = "{\"AESString\":\"Parichay12345\"}";

    // Create HTTP request (Java 11+ HttpClient API)
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ENCR_URL))
            .header("Content-Type", "application/json") // important header
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    // Send and get response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String str = response.body();
    System.out.println("Response Body: " + str);

    // ✅ Parse JSON using Jackson (built into Spring Boot)
    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    com.fasterxml.jackson.databind.JsonNode json = mapper.readTree(str);

    // Extract the nested value: data.signature
    String encrptReturnReponse = json.path("data").path("signature").asText();
    System.out.println("encrptReturnReponse :- " + encrptReturnReponse);

    return encrptReturnReponse;
}




private static String hmacFunc(long timeStamp, String SERVICE, String PARICHAY_URL) throws IOException, InterruptedException {
    System.out.println("hmac function called");

    // Build HMAC string and request body
    String hmacString = "Parichay" + timeStamp + PARICHAY_URL + "/pnv1/api/login" + SERVICE;
    String requestBody = "{\"HmacString\":\"" + hmacString + "\"}";

    System.out.println("hmacString        "+hmacString);
    // Get URL from properties
    String HMAC_URL = ResourceBundle.getBundle("application").getString("HMAC_URL");

    // Create HTTP request
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(HMAC_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

    // Send and receive response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    String responseBody = response.body();
    System.out.println("HMAC Response: " + responseBody);

    // ✅ Parse JSON using Jackson
    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    com.fasterxml.jackson.databind.JsonNode json = mapper.readTree(responseBody);

    // Extract nested "data.signature"
    String hmacReturnResponse = json.path("data").path("signature").asText();
    System.out.println("hmacReturnReponse :- " + hmacReturnResponse);

    return hmacReturnResponse;
}




/*
 * private static String encrptFunc() throws IOException, InterruptedException {
 * 
 * System.out.println("encrptFunc called"); String ENCR_URL =
 * ResourceBundle.getBundle("application").getString("ENCR_URL"); String
 * requestBody = "{\"AESString\":\"Parichay12345\"}"; HttpClient client =
 * HttpClient.newHttpClient(); HttpRequest request = HttpRequest.newBuilder()
 * .uri(URI.create(ENCR_URL)) .POST(BodyPublishers.ofString(requestBody))
 * .build(); HttpResponse<String> response = client.send(request,
 * BodyHandlers.ofString()); String str =response.body();
 * System.out.println(str); Object obj=JSONValue.parse(str); JSONObject json
 * =(JSONObject) obj ; String encrptReturnReponse = ((JSONObject)
 * json.get("data")).get("signature").toString();
 * System.out.println("encrptReturnReponse :- " + encrptReturnReponse); return
 * encrptReturnReponse; }
 */

/*
 * private static String hmacFunc(long timeStamp, String SERVICE, String
 * PARICHAY_URL) throws IOException, InterruptedException {
 * System.out.println("hmac function called");
 * 
 * String hmacString =
 * "Parichay"+timeStamp+PARICHAY_URL+"/pnv1/api/login"+SERVICE; String
 * requestBody = "{\"HmacString\":\"" + hmacString + "\"}"; String HMAC_URL =
 * ResourceBundle.getBundle("application").getString("HMAC_URL"); HttpClient
 * client = HttpClient.newHttpClient(); HttpRequest request =
 * HttpRequest.newBuilder() .uri(URI.create(HMAC_URL))
 * .POST(BodyPublishers.ofString(requestBody)) .build(); HttpResponse<String>
 * response = client.send(request, BodyHandlers.ofString()); String str
 * =response.body(); System.out.println(str); Object obj=JSONValue.parse(str);
 * JSONObject json =(JSONObject) obj ; String encrptReturnReponse =
 * ((JSONObject) json.get("data")).get("signature").toString();
 * System.out.println("hmacReturnReponse :- " + encrptReturnReponse);
 * 
 * return encrptReturnReponse ;
 * 
 * }
 */
}
