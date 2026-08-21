package com.pams.controllers;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.codec.json.JsonObjectDecoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class HomeParichayController {
	

@GetMapping("/parichayclient/sessionCheck")
public void test(HttpServletRequest request,HttpServletResponse response) throws IOException
{

// System.out.println("Session Check func called");
// HttpSession session = request.getSession();
// System.out.println("session check func : "+session);
// session.setMaxInactiveInterval(10*60);
//
// response.setContentType("text/html");
//     PrintWriter pwriter = response.getWriter();
//     session.setAttribute("username", "sunil");
//    
//     System.out.println("request.getRequestedSessionId() "+request.getRequestedSessionId() );
//     System.out.println("request.isRequestedSessionIdValid() "+request.isRequestedSessionIdValid() );
//    
//    
//
//    
// if(request.isRequestedSessionIdValid())
// {
//
//   
// System.out.println("session Active ");
// String name = session.getAttribute("username").toString();
// System.out.println("name :"+name);
// pwriter.print("sessionActive : "+name);
// session.invalidate();
//
// }
// else
// {
// String name = session.getAttribute("username").toString();
// System.out.println("name2 :"+name);
// System.out.println("session notActive");
// pwriter.print("session notActive : "+name);
// }
// pwriter.close();
}
@GetMapping("/parichayclient/home")

protected String home(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

  

    var encryptedString = request.getParameter("string");
    if (encryptedString == null || encryptedString.isBlank()) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty 'string' parameter");
        return null;
    }

    
    // Step 1: handshake
    var handshakeResponse = hsFunc(encryptedString);
  

    // Step 2: decrypt
    var decryptedResponse = decrptFunc(handshakeResponse);
   

    // Step 3: parse JSON using pattern matching
    var parsed = JSONValue.parse(decryptedResponse);
    if (!(parsed instanceof JSONObject jsonObject)) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid JSON structure");
        return null;
    }

    var data = (JSONObject) jsonObject.get("data");
    if (data == null) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing 'data' section");
        return null;
    }

    var signature = (JSONObject) data.get("signature");
    if (signature == null) {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Missing 'signature' section");
        return null;
    }

    // Step 4: safely extract fields
    var userName = (String) signature.getOrDefault("userName", "");
    var sessionId = (String) signature.getOrDefault("sessionId", "");
    var browserId = (String) signature.getOrDefault("browserId", "");
    var localTokenId = (String) signature.getOrDefault("localTokenId", "");
    var ua = (String) signature.getOrDefault("ua", "");
    var parichayId = (String) signature.getOrDefault("parichayId", "");
    var serviceName = "ParichayClient";

    // Step 5: manage session
    HttpSession session = request.getSession(true);
    session.setAttribute("userName", userName);
    session.setAttribute("sessionId", sessionId);
    session.setAttribute("browserId", browserId);
    session.setAttribute("localTokenId", localTokenId);
    session.setAttribute("ua", ua);
    session.setAttribute("parichayId", parichayId);
    session.setAttribute("serviceName", serviceName);

    

    return "home";
}


public String decrptFunc(String hs_resp) throws IOException, InterruptedException {
String requestBody = "{\"EncryptedString\":\"" + hs_resp + "\"}";
System.out.println("decrptFunc requestBody : " + requestBody);
String DEC_URL =ResourceBundle.getBundle("application").getString("DEC_URL");
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DEC_URL))
.POST(BodyPublishers.ofString(requestBody)).build();

System.out.println("decrptFunc request" + request);

HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

System.out.println("decryptFunc response" + response);

if (response.statusCode() != 200) {
System.out.println("Call to decryption api failed");
return "null";
}
String str = response.body();

System.out.println(str);
   
System.out.println("decryption func return resp" + str);

return str;
}

private String hsFunc(String encrString) throws IOException, InterruptedException {
String url =ResourceBundle.getBundle("application").getString("HANDSHAKING_URL") +"/" + encrString + "/" + ResourceBundle.getBundle("application").getString("SERVICE");
System.out.print("hsfunc url : " + url);
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
client.sendAsync(request, BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(System.out::println).join();

HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
String str = response.body();
System.out.println("str : " + str);

return str;

}


@GetMapping("/parichayclient/tokenValidate")
private boolean isTokenValid(HttpServletRequest request, HttpServletResponse response)throws Exception, InterruptedException {

System.out.println("TokenValidate func called.");
HttpSession session=request.getSession(false);
String userName=(String)session.getAttribute("userName"); 
String sessionId=(String)session.getAttribute("sessionId"); 
String browserId=(String)session.getAttribute("browserId"); 
String localTokenId=(String)session.getAttribute("localTokenId"); 
String url =ResourceBundle.getBundle("application").getString("Token_validate");
String SERVICE =ResourceBundle.getBundle("application").getString("SERVICE");

HttpClient client = HttpClient.newHttpClient();

HttpRequest requesthttp = HttpRequest.newBuilder().uri(URI.create(url+"?userName=" + userName + "&service="+SERVICE+ "&sessionId=" + sessionId + "&browserId=" + browserId + "&localTokenId=" + localTokenId))
.GET().build();

System.out.println("tokenValidate request :" + requesthttp);

HttpResponse<String> httpresponse = client.send(requesthttp, BodyHandlers.ofString());

System.out.println("http response" + httpresponse);
if (httpresponse.statusCode() != 200) {
System.out.println("Call to istokenvalid api failed");

return false;
}
else
{
String str = httpresponse.body();
System.out.println("str"+str);
Map<String, String> responseMap = new ObjectMapper().readValue(str, HashMap.class);
System.out.println("responseMap"+responseMap);
if (responseMap.get("status").equals("success")) {
if (responseMap.get("tokenValid").equals("true"))

return true;

} else {
  System.out.println("token validation api failed");

  return false ;

}

}
return false;

}




@GetMapping("/parichayclient/logout")
private void logout(HttpServletRequest request, HttpServletResponse response) throws Exception
{
  
    System.out.println("Logout Func called.");
    long timeStamp = System.currentTimeMillis();
System.out.println("timeStamp : "+timeStamp);
String Service = ResourceBundle.getBundle("application").getString("SERVICE");
System.out.println("Service Name : " + Service);
String ParichayUrl = ResourceBundle.getBundle("application").getString("PARICHAY_URL");
System.out.println("ParichayUrl : " + ParichayUrl);
HttpSession session=request.getSession(false);
String userName=(String)session.getAttribute("userName");
String sessionId=(String)session.getAttribute("sessionId"); 
String hmac_sign = hmacFunc_logout(timeStamp,Service,ParichayUrl,userName,sessionId);
System.out.println("hmac_response : " + hmac_sign);
String logOutURL = "http://parichay.staging.nic.in/pnv1/salt/api/client/logout?userName="+userName+"&service="+Service+"&sessionId="+sessionId+"&tid="+timeStamp+"&cs="+hmac_sign ;
System.out.println(logOutURL);
String logOutURLtrim = logOutURL.trim();
        System.out.println("logOutURL trim2 : " + logOutURLtrim);
        response.sendRedirect(logOutURLtrim);




}


private String hmacFunc_logout(long timeStamp, String service, String parichayUrl,String userName,String sessionId) throws IOException, InterruptedException {
// TODO Auto-generated method stub

String hmacString = "Parichay"+timeStamp+"https://parichay.staging.nic.in/pnv1/salt/api/client/logout"+userName+service+sessionId ;

System.out.println("logout hmacString=========: "+hmacString);
String requestBody = "{\"HmacString\":\"" + hmacString + "\"}";
String HMAC_URL = ResourceBundle.getBundle("application").getString("HMAC_URL");
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create(HMAC_URL))
         .POST(BodyPublishers.ofString(requestBody))
         .build();
   HttpResponse<String> response =  client.send(request, BodyHandlers.ofString());
   String str =response.body();
    System.out.println(str);
    Object obj=JSONValue.parse(str); 
    JSONObject json =(JSONObject) obj  ;
    String hmacReturnReponse = ((JSONObject) json.get("data")).get("signature").toString();
    System.out.println("hmacReturnReponse :- " + hmacReturnReponse);

    return hmacReturnReponse ;

}

@GetMapping("/parichayclient/sessionTimeOut")
private void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws Throwable
{
long timeStamp = System.currentTimeMillis();
System.out.println("timeStamp : "+timeStamp);
String Service = ResourceBundle.getBundle("application").getString("SERVICE");
System.out.println("Service Name : " + Service);
String ClientSignature = hmacTimeOut(timeStamp ,Service);

String SesstimeOut = "https://parichay.staging.nic.in/pnv1/salt/api/client/timeout?service="+Service+"&tid="+timeStamp+"&cs="+ClientSignature;
response.sendRedirect(SesstimeOut);
}

private String hmacTimeOut(long timeStamp,String Service) throws Throwable, InterruptedException {


String hmacString = "Parichay"+timeStamp+"https://parichay.staging.nic.in/pnv1/salt/api/client/timeout"+Service;

System.out.println("Timeout hmacString=========: "+hmacString);
String requestBody = "{\"HmacString\":\"" + hmacString + "\"}";
String HMAC_URL = ResourceBundle.getBundle("application").getString("HMAC_URL");
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create(HMAC_URL))
         .POST(BodyPublishers.ofString(requestBody))
         .build();
   HttpResponse<String> response =  client.send(request, BodyHandlers.ofString());
   String str =response.body();
    System.out.println(str);
    Object obj=JSONValue.parse(str); 
    JSONObject json =(JSONObject) obj;
    String hmacReturnReponse = ((JSONObject) json.get("data")).get("signature").toString();
    System.out.println("hmacTimeoutReturnReponse :- " + hmacReturnReponse);
    return hmacReturnReponse ;
}























}



