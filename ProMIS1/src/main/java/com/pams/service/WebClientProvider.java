package com.pams.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pams.dto.GetDataFromSNMSByCaseNumberDTO;
import com.pams.dto.GetDataFromSNMSDTO;
import com.pams.dto.User;

import reactor.core.publisher.Mono;

@Service
public class WebClientProvider {
	@Value("${file.snmsapi1}")
	public String snmsapi;
    private final WebClient webClient;

    public WebClientProvider(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> apiCallForList(GetDataFromSNMSDTO getDataFromSNMSDTO, String urlTest) {

        // Create authentication user object
        User objEmp = new User();
        objEmp.setUsername("dk@sfio.in");
        objEmp.setToken(
                "a2cf31af71d670da0bf167b025e33a7586664d4cfc03968568bacdd793414215df457ea34c453109c20f62be71bf33d60957e5fe96ed5e4e2dd2f686e555dd02"
        );

        // First API call for authentication
        
        Mono<String> flatMap = webClient.post()
        .uri(snmsapi)  // Authentication API
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(objEmp)
        .retrieve()
        .bodyToMono(User.class)
        .flatMap(userResponse -> {
            // Extract token from response
            String authToken = userResponse.getToken();

            // Second API call with the retrieved token
            return webClient.post()
                    .uri(urlTest)  // Target API endpoint
                    .header(HttpHeaders.AUTHORIZATION, authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(getDataFromSNMSDTO)
                    .retrieve()
                    .bodyToMono(String.class);
        });
        
        return flatMap;
    }
    
    public Mono<String> apiCallForList2(GetDataFromSNMSByCaseNumberDTO getDataFromSNMSDTO, String urlTest) {

        // Create authentication user object
        User objEmp = new User();
        objEmp.setUsername("dk@sfio.in");
        objEmp.setToken(
                "a2cf31af71d670da0bf167b025e33a7586664d4cfc03968568bacdd793414215df457ea34c453109c20f62be71bf33d60957e5fe96ed5e4e2dd2f686e555dd02"
        );

        // First API call for authentication
        
        Mono<String> flatMap = webClient.post()
        .uri(snmsapi)  // Authentication API
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(objEmp)
        .retrieve()
        .bodyToMono(User.class)
        .flatMap(userResponse -> {
            // Extract token from response
            String authToken = userResponse.getToken();

            // Second API call with the retrieved token
            return webClient.post()
                    .uri(urlTest)  // Target API endpoint
                    .header(HttpHeaders.AUTHORIZATION, authToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(getDataFromSNMSDTO)
                    .retrieve()
                    .bodyToMono(String.class);
        });
        
        return flatMap;
    }
}