package com.pams.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.pams.dao.AppUserDAO;
import com.pams.entity.AddDesignation;
import com.pams.entity.AppUser;
import com.pams.entity.UserDetails;
import com.pams.entity.UserRole;
import com.pams.service.AddDesignationRepository;
import com.pams.service.UserRoleRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginParichayController {
	@Autowired
	private AppUserDAO loginDetailBo;
	@Autowired
	private AddDesignationRepository addDesignationRepo;
	@Autowired
	private UserRoleRepository userRoleRepository;

	@GetMapping("/authByParichay")
	public String login(HttpServletRequest request) throws IOException, Throwable {

		// Create or get session
		HttpSession session = request.getSession(true);

		// Read parameters returned by Parichay
		String encrString = request.getParameter("string");

		String url = ResourceBundle.getBundle("application").getString("HANDSHAKING_URL") + "/" + encrString + "/"
				+ ResourceBundle.getBundle("application").getString("SERVICE");
		System.out.print("hsfunc url : " + url);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest requesta = HttpRequest.newBuilder().uri(URI.create(url)).build();
		// client.sendAsync(requesta,
		// BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(System.out::println).join();

		HttpResponse<String> response = client.send(requesta, BodyHandlers.ofString());
		String str = response.body();
		System.out.println("str : " + str);

		var decryptedResponse = decrptFunc(str);

		// Step 3: parse JSON using pattern matching
		var parsed = JSONValue.parse(decryptedResponse);
		if (!(parsed instanceof JSONObject jsonObject)) {

			return null;
		}

		var data = (JSONObject) jsonObject.get("data");
		if (data == null) {

			return null;
		}

		var signature = (JSONObject) data.get("signature");
		if (signature == null) {

			return null;
		}

		// Step 4: safely extract fields
		String userName = (String) signature.getOrDefault("email", "");
		//String userName = (String) signature.getOrDefault("userName", "");
		 var sessionId = (String) signature.getOrDefault("sessionId", "");
		// var browserId = (String) signature.getOrDefault("browserId", "");
		// var localTokenId = (String) signature.getOrDefault("localTokenId", "");
		// var ua = (String) signature.getOrDefault("ua", "");
		// var parichayId = (String) signature.getOrDefault("parichayId", "");
		
		
		
		    session.setAttribute("userName", userName);
		 
		    session.setAttribute("sessionId", sessionId);
		
		

		AppUser user = null;

		AddDesignation designation = null;

		user = loginDetailBo.findUserAccount(userName);
		UserDetails userDetails = loginDetailBo.findUserDetails(user);

		String salutation = userDetails.getSalutation();
		String firstName = userDetails.getFirstName();
		String middleName = userDetails.getMiddleName();
		String lastName = userDetails.getLastName();

		StringBuilder usernameBuilder = new StringBuilder();

		if (salutation != null && !salutation.isEmpty()) {
			usernameBuilder.append(salutation).append(" ");
		}

		if (firstName != null && !firstName.isEmpty()) {
			usernameBuilder.append(firstName).append(" ");
		}

		if (middleName != null && !middleName.isEmpty()) {
			usernameBuilder.append(middleName).append(" ");
		}

		if (lastName != null && !lastName.isEmpty()) {
			usernameBuilder.append(lastName);
		}

		String username = usernameBuilder.toString().trim();

		request.getSession().setAttribute("userfname", username);

		long designationId = loginDetailBo.findUserDesignation(userDetails.getId());
		if (designationId != 0) {
			designation = addDesignationRepo.findById(designationId).get();
		}

		if (designation != null) {
			request.getSession().setAttribute("userdesignation", designation.getDesignation());
		} else {
			request.getSession().setAttribute("userdesignation", "");
		}

		UserRole byAppUser = userRoleRepository.findByAppUser(user);

		List<SimpleGrantedAuthority> authorities = List
				.of(new SimpleGrantedAuthority(byAppUser.getAppRole().getRoleName()));

		// Step 3: Create authentication object
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, null,
				authorities);

		// Step 4: Set authentication in Spring Security context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// Step 5: Store context in session (so it persists)
		session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		String url1 = "";

		if (roles.contains("ROLE_ADMIN")) {
			url1 = "redirect:/home";
		} else if (roles.contains("ROLE_OFFICER")) {
			url1 = "redirect:/userHome";
		}

		else if (roles.contains("ROLE_PROSECUTORUNITHEAD")) {
			url1 = "redirect:/puhHome";
		} else if (roles.contains("ROLE_DIRECTOR")) {
			url1 = "redirect:/directorhome";
		}

		else if (roles.contains("ROLE_PUH_STAFF")) {
			url1 = "redirect:/puhStaffhome";
		}

		else
			url1 = "redirect:/error403";
		return url1;

	}

	public String decrptFunc(String hs_resp) throws IOException, InterruptedException {
		String requestBody = "{\"EncryptedString\":\"" + hs_resp + "\"}";
		System.out.println("decrptFunc requestBody : " + requestBody);
		String DEC_URL = ResourceBundle.getBundle("application").getString("DEC_URL");
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
}
