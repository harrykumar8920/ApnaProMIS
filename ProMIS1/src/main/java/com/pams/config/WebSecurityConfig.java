package com.pams.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pams.service.CustomAuthenticationProvider;
import com.pams.service.CustomSuccessHandler;
import com.pams.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	private CustomSuccessHandler customSuccessHandler;

	@Bean
	@Lazy
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(
				sessionRegistry());
		sessionAuthenticationStrategy.setMaximumSessions(1);
		sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(false);
		return sessionAuthenticationStrategy;
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
		authenticationManagerBuilder.userDetailsService(userDetailsService);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		
		  http.csrf().ignoringRequestMatchers("/hello1",
		  "/showmcaOrderDetailsByCaseNumber", "/respNotice", "/respSummon",
		  "/showsSubList", "/getInspectorListReportByUnitId", "/AddCompanyDetails",
		  "/initiateApprove",
		  "/genComplaintPreview1","/api/**","/parichayclient/login","/authByParichay");
		 
		
	

		// Session management configuration
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).maximumSessions(1)
				.expiredUrl("/login").and().sessionFixation().migrateSession().invalidSessionUrl("/login");

		// Frame options
		http.headers().frameOptions().sameOrigin();

		// Authorization configuration
		http.authorizeHttpRequests()
				.requestMatchers("/", "/index", "/captcha", "/forgotPass", "/getForgotOtp", "/forgotPassword",
						"/saveForgotPass", "/getUniqueEmail", "/showsSectionList", "/showsSubList",
						"/getInspectorListReportByUnitId", "/showsCourtDtl", "/genComplaintPreview",
						"/genComplaintPreview1")
				.permitAll().requestMatchers("/actuator/**", "/login", "/passHashing","/api/**", "/students/**","/parichayclient/login","/authByParichay").permitAll()
				.requestMatchers("/unsignPdf", "/generateHash", "/hello1", "/respNotice", "/respSummon")
				.hasAnyRole("USER", "DIRECTOR").requestMatchers("/user/**").hasAuthority("ROLE_OFFICER")
				.requestMatchers("/userHome", "/prosecutorTaskList", "/prosecutorPendingTaskList",
						"/ApproveproTaskList", "/totalNumberOfCourtCases", "/totalNumberOfSendBackCourtCases",
						"/totalforwardCourtCase", "/additionalDetails", "/viewPendingTaskdtl1",
						"/SaveproCourtCaseDetails", "/SaveAccusedDetails1", "/prosecutorTaskList1",
						"/deleteAccusedStatus", "/saveAccusedStatus", "/addNewAccusedStatus", "/addNewAccusedStatusGet",
						"/getPunishment", "/getChargeSub", "/getInstanse", "/backFromAccusedStatus", "/viewStatus1",
						"/viewAccusedStatus", "/backFromView", "/chagerget", "/saveChargeInstance", "/chargeAdd",
						"/deleteInstance", "/backFromCharge", "/addCharge")
				.hasAuthority("ROLE_OFFICER")
				.requestMatchers("/home", "/getUsers", "/getUnits", "/editUnit", "/addUnit", "/addNewUnit",
						"/getDesignations", "/editDesignation", "/addDesignation", "/getRoles", "/addUser",
						"/addNewUser", "/editUser", "/saveUpdateUser", "/addDesignation", "/addRole", "/createNewRole",
						"/addlocation", "/addNewLocation", "/editDetails", "/addlocation", "/addCourt", "/addNewCourt",
						"/addCourtCaseName", "/editCourtCase", "/deleteCourtCase", "/pofficer", "/editPairavi",
						"/addpairavi", "/type", "/addnewtype", "/edittyp", "/deletetyp", "/typeCase",
						"/addNewTypeofCase", "/editTypeofCase", "/categoriesofCompany", "/addcategoriesofcompany",
						"/editcategories", "/deletecategories", "/clauses", "/addnewclause", "/punishment",
						"/savePunishment", "/addStatus", "/addNewstatus", "/typebench", "/addTypeofResponse",
						"/typeofResponse", "/addNewTypeofResponse", "/addActPage", "/updateAct", "/editingAct",
						"/addActSec", "/addNewSec", "/updateActSec", "/editActSec", "/addSubSection", "/addNewSubSec",
						"/saveAddSubSec", "/editSubSec", "/auditTrailReport", "/addTask", "/createNewTasks",
						"/editTask", "/addSubTask", "/createNewSubTasks", "/editSubTask", "/showAuditTrail")
				.hasAuthority("ROLE_ADMIN")
				.requestMatchers("/addCriminalDtl/").hasAnyRole("OFFICER", "PROSECUTORUNITHEAD")
				.requestMatchers("/puhHome", "/puhApproval", "/puhApprovedAndRejectedTaskList","/dynamic-report","/updateCasePriority"
						,"totalCourtCaseDtl","/pendingAssignedTask","/pendingFinalizecourtCaseDtl","/totalPendingCourtCaseDtlForApproval","/ListOfCourtCase1","/ListOfCourtCase2"
						)
				.hasAuthority("ROLE_PROSECUTORUNITHEAD")
				.requestMatchers("/directorhome").hasAuthority("ROLE_DIRECTOR")
				.requestMatchers("/puhStaffhome", "/editProCourtCase").hasAuthority("ROLE_PUH_STAFF")
				.requestMatchers("/listOfCourtCasesS", "/listOfSendBackCourtCases", "/listOfForwardCourtCases",
						"/saveaddCase","/addinspector","/saveInspector","/AddNewCase")
				.hasAnyRole("PUH_STAFF", "OFFICER").requestMatchers("/viewPendingTaskdtl2","/listOfCourtCasesnew","/downloadFiles","/todayHearingDetailsProsecution")
				.hasAnyRole("PUH_STAFF", "OFFICER", "PROSECUTORUNITHEAD")
				.requestMatchers("/reportOfCourtCase", "/weeklycauseList", "/weeklyListOfCasesWhereMCAIsParty",
						"/weeklyListOfCasesWhereMCAIsParty1", "/monthlyProgressiiveReport16",
						"/monthlyProgresiveReport160", "/monthlyProgressiiveReport9", "/monthlyProgresiveReport99",
						"/monthlyProgressiiveReport8", "/monthlyProgressiiveReport88", "/monthlyProgressiiveReport7",
						"monthlyProgressiiveReport77", "/monthlyProgressiiveReport6", "/monthlyProgressiiveReport66",
						"/monthlyProgressiiveReport5", "/monthlyProgressiiveReport55", "/monthlyProgresiveReport44",
						"/monthlyProgresiveReport4", "/monthlyProgressiiveReport3","/reportByCIN","/searchCaseByCin","/viewDetails")
				.hasAnyRole("PUH_STAFF", "PROSECUTORUNITHEAD")
				.requestMatchers("/showCert", "/getCertList", "/saveCert", "/successReg", "/unregDSC", "/viewpdffile")
				.hasAnyRole("DIRECTOR", "USER").requestMatchers("/approvedOrders", "/esignOrder", "/savepdf")
				.hasRole("DIRECTOR").requestMatchers("/getApprovedOrder").hasAnyRole("ADMIN_OFFICER", "DIRECTOR")
				.requestMatchers("/esignNotice", "/savenoticePdf", "/esignSummon", "/savesummonPdf").hasRole("USER")
				.requestMatchers("/approvedNotice", "/getApprovedNotice", "/approvedSummon", "/getApprovedSummons")
				.hasAnyRole("USER", "DIRECTOR").anyRequest().authenticated().and().formLogin().loginPage("/login")
				.failureUrl("/login?error=true").loginProcessingUrl("/securityCheck")
				.successHandler(customSuccessHandler).permitAll().and().logout().logoutUrl("/logout")
				.deleteCookies("JSESSIONID").invalidateHttpSession(true).and().exceptionHandling()
				.accessDeniedPage("/error403");
		http
        .headers(headers -> headers
            .addHeaderWriter((request, response) -> {
                response.setHeader(
                    "Cross-Origin-Embedder-Policy",
                    "require-corp"
                );
            })
        );
		http
        .headers(headers -> headers
            .addHeaderWriter((request, response) -> {
                response.setHeader(
                    "Cross-Origin-Opener-Policy",
                    "same-origin"
                );
            })
        );
		
		http
        .headers(headers -> headers
            .addHeaderWriter((request, response) -> {
                response.setHeader(
                    "Cross-Origin-Resource-Policy",
                    "same-origin"
                );
            })
        );



		

		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
				"plugins**", "fonts**", "/.well-known/**");
	}
	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
	    ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	    MappingJackson2HttpMessageConverter converter = 
	        new MappingJackson2HttpMessageConverter(mapper);
	    return converter;
	}
}
