package com.pams.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pams.dto.GetDataFromSNMSByCaseNumberDTO;
import com.pams.dto.GetDataFromSNMSDTO;
import com.pams.service.WebClientProvider;

import reactor.core.publisher.Mono;

@Controller
public class ApiCourtCaseDtlController {
    @Value("${file.snmsapi}")
    private String snmsapi;
    @Value("${file.snmsapi}")
    private String snmsap2;
    private final WebClientProvider webClientProvider;

    public ApiCourtCaseDtlController(WebClientProvider webClientProvider) {
        this.webClientProvider = webClientProvider;
    }

    @GetMapping(value = "/showmcaOrderDetails", produces = "application/json")
    public @ResponseBody String showmcaOrderDetails(
            ModelMap model,
            @RequestParam("mcaorder") String mcaNo,
            @RequestParam("compname") String compName) {

        // Replace spaces with "?"
        mcaNo = mcaNo.replace(" ", "?");
        compName = compName.replace(" ", "?");

        GetDataFromSNMSDTO getDataFromSNMSDTO = new GetDataFromSNMSDTO();
        getDataFromSNMSDTO.setCompNameo(compName);
        getDataFromSNMSDTO.setMcaOrderNo(mcaNo);

        // Make the API call and get the Mono<String>
        Mono<String> apiCallForList = webClientProvider.apiCallForList(getDataFromSNMSDTO, snmsapi + "/showmcaOrderDetails");

        // Block to get the response as a String (synchronous)
        String response = apiCallForList.block();

        // Log the response for debugging
        System.out.println("API Response: " + response);

        // Return the response
        return response != null ? response : "{}"; // Return empty JSON if null
    }
    
    @GetMapping(value = "/showmcaOrderDetailsByCaseNumber", produces = "application/json")
    public @ResponseBody String showmcaOrderDetailsByCaseNumber(
            ModelMap model,
            @RequestParam("mcaorder") String mcaNo) {

        mcaNo = mcaNo.replace(" ", "?");
        

        GetDataFromSNMSByCaseNumberDTO getDataFromSNMSDTO = new GetDataFromSNMSByCaseNumberDTO();
     
        getDataFromSNMSDTO.setCaseNumber(mcaNo);

       
        Mono<String> apiCallForList = webClientProvider.apiCallForList2(getDataFromSNMSDTO, snmsap2 + "/showmcaOrderDetailsByCaseNumber");

        
        String response = apiCallForList.block();

       
        System.out.println("API Response: " + response);

       
        return response != null ? response : "{}"; 
    }
    
}