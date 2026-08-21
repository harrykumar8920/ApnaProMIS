package com.pams;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import com.pams.config.JWTAuthorizationFilterBrowser;
import com.pams.utils.SaltGenerator;
import com.pams.utils.Utils;

@SpringBootApplication
public class PromisApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromisApplication.class, args);
	}
	
	@Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            // Set relaxed query characters
            connector.setProperty("relaxedQueryChars", "|,{,},[,],\\,\uD83D\uDCA1");
        });
    }
	
	
	@Bean(name = "saltGen")
	public SaltGenerator getSaltGen() {
		return new SaltGenerator();
	}
	 
		
	@Bean(name="utils")
	public Utils utils(){
		Utils utils = new Utils();
		return utils;
	}
	 @Bean
	    public FilterRegistrationBean<JWTAuthorizationFilterBrowser> jwtAuthorizationFilter1RegistrationBean() {
	        FilterRegistrationBean<JWTAuthorizationFilterBrowser> registrationBean = new FilterRegistrationBean<>();
	        JWTAuthorizationFilterBrowser jwtAuthorizationFilter1 = new JWTAuthorizationFilterBrowser();

	        registrationBean.setFilter(jwtAuthorizationFilter1);
	       
	        return registrationBean;
	    }

	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	            connector.setProperty("relaxedQueryChars", "|{}[]");
	        }
	    });
	    return factory;
	}
}

