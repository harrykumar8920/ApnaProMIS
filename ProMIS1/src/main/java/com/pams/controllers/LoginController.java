package com.pams.controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import com.pams.dao.AppRoleDAO;
import com.pams.dao.AppUserDAO;
import com.pams.entity.AppUser;
import com.pams.entity.SnmsErrorReference;
import com.pams.service.AddDesignationRepository;
import com.pams.service.AppRoleRepository;
import com.pams.service.SnmsErrorRefRepository;
import com.pams.service.UnitDetailsRepository;
import com.pams.service.UserDetailsRepository;
import com.pams.service.UserDetailsServiceImpl;
import com.pams.service.UserManagementCustom;
import com.pams.utils.Crypt;
import com.pams.utils.PromisException;
import com.pams.utils.RandomString;
import com.pams.utils.Utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private UserManagementCustom userMangCustom;

	@Autowired
	private AppUserDAO appUserDAO;

	@Autowired
	private AppRoleDAO appRoleDao;

	@Autowired
	private Utils utils;

	@Autowired
	private SnmsErrorRefRepository snmsErrorRefRepository;

	@Autowired
	private UnitDetailsRepository unitDetailsRepository;

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private AppRoleRepository appRoleRepository;

	@Autowired
	private AddDesignationRepository addDesignationRepository;

	@RequestMapping(value = { "/", "/index" })
	public String index() {
		logger.info("index page is loaded");
		return "index";
	}

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, HttpServletRequest req,
			HttpServletResponse resp, ModelMap model) throws NoSuchAlgorithmException {

		// saltGen.generateSalt();
		// String salt=saltGen.genSalt();
		req.getSession().setAttribute("key", Crypt.encodeKey("mustbe16byteskey"));
		// req.getSession().setAttribute("key", BCrypt.gensalt(4, new SecureRandom(new
		// byte[128])));

		String randomToken = RandomString.getAlphaNumericString(10);
		HttpSession session = req.getSession();

		System.out.println("randomsession  " + session.getAttribute("random_Token"));
		if (session.getAttribute("random_Token") == null) {
			session.setAttribute("random_Token", randomToken);
			model.addAttribute("randomToken", randomToken);
		}

		if (error != null) {

			String strError = getErrorMessage(req, "SPRING_SECURITY_LAST_EXCEPTION");
			model.addAttribute("error", strError);
			return "login1";
		}
		if (logout != null) {
			model.addAttribute("logout", "You have been logout sucessfully!");
			return "login1";
		}
		return "login1";
	}

	private String getErrorMessage(HttpServletRequest request, String key) {
		Exception exception = (Exception) request.getSession().getAttribute(key);
		System.out.println(exception.getMessage());
		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else {
			error = exception.getMessage();
		}
		return error;

	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     
		
		  if (auth != null) { new SecurityContextLogoutHandler().logout(request,
		  response, auth); }
		 
		
		  return "redirect:/login";
		
			//This code for parichay 08 Dec 2025
		/*
		 * System.out.println("Logout Func called."); long timeStamp =
		 * System.currentTimeMillis(); System.out.println("timeStamp : "+timeStamp);
		 * String Service =
		 * ResourceBundle.getBundle("application").getString("SERVICE");
		 * System.out.println("Service Name : " + Service); String ParichayUrl =
		 * ResourceBundle.getBundle("application").getString("PARICHAY_URL");
		 * System.out.println("ParichayUrl : " + ParichayUrl); HttpSession
		 * session=request.getSession(false); String
		 * userName=(String)session.getAttribute("userName"); String
		 * sessionId=(String)session.getAttribute("sessionId");
		 * 
		 * String hmac_sign =
		 * hmacFunc_logout(timeStamp,Service,ParichayUrl,userName,sessionId);
		 * System.out.println("hmac_response : " + hmac_sign); String logOutURL =
		 * "http://parichay.staging.nic.in/pnv1/salt/api/client/logout?userName="+
		 * userName+"&service="+Service+"&sessionId="+sessionId+"&tid="+timeStamp+"&cs="
		 * +hmac_sign ; System.out.println(logOutURL); String logOutURLtrim =
		 * logOutURL.trim(); System.out.println("logOutURL trim2 : " + logOutURLtrim);
		 * response.sendRedirect(logOutURLtrim);
		 */
			 
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

	@RequestMapping(value = "/home")
	public String home(ModelMap modelMap) {
		modelMap.addAttribute("totleUsers", userDetailsRepository.count());
		modelMap.addAttribute("totleUnits", unitDetailsRepository.count());
		modelMap.addAttribute("totleDesignation", addDesignationRepository.count());
		modelMap.addAttribute("totleRole", appRoleRepository.count());
		return "home";
	}

	// Director Login
	@RequestMapping(value = "/dirHome")
	public String dirHome(ModelMap modelMap) {
		modelMap.addAttribute("totleUsers", userDetailsRepository.count());
		modelMap.addAttribute("totleUnits", unitDetailsRepository.count());
		modelMap.addAttribute("totleDesignation", addDesignationRepository.count());
		modelMap.addAttribute("totleRole", appRoleRepository.count());
		return "dirHome";
	}

	// Prosecutor unit head

	@ExceptionHandler(value = { Exception.class, RuntimeException.class, PromisException.class })
	public String handleError(Exception ex) throws PromisException, Exception {
		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute("ErrorString", getErrorMessage(ex));
		ErrorPageRedirect(loginUser, modelMap);
		return "redirect:/errorPage";
	}

	public String getErrorMessage(Exception ex) throws PromisException, Exception {
		SnmsErrorReference err = new SnmsErrorReference();
		String errorString = "000";
		String errorMsg = "Error";
		if (ex instanceof PromisException) {
			PromisException dex = (PromisException) ex;
			errorString = dex.getERROR_CODE();
			errorMsg = utils.getError(errorString, dex.getParameter());
		} else {
			if (ex instanceof MissingServletRequestParameterException)
				return utils.getMessage("errmsg.missing");
			if (ex instanceof org.springframework.validation.BindException)
				return utils.getMessage("errmsg.mismatch");
			errorMsg = utils.getError(errorString);
		}
		StackTraceElement[] traces = ex.getStackTrace();
		StackTraceElement trace = (traces == null || traces.length == 0) ? null : traces[0];
		String erMsg = (trace == null) ? ""
				: trace.getClassName() + ":" + trace.getMethodName() + ":" + trace.getLineNumber();
		erMsg = (ex.getMessage() == null) ? (erMsg + ":" + ex) : (erMsg + ":" + ex.getMessage());
		err.setErrorMessage(erMsg);
		err.setErrorCode(errorString);
		snmsErrorRefRepository.save(err);
		return (errorMsg + " with reference No:" + err.getId());
	}

	@RequestMapping(value = "/{keyword}", method = RequestMethod.GET)
	public String errorRedirect(@PathVariable("keyword") String keyword, ModelMap modelMap)
			throws PromisException, Exception {
		modelMap.addAttribute("ErrorString", keyword);
		modelMap.addAttribute("ErrorUrl", "login");
		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		ErrorPageRedirect(loginUser, modelMap);
		return "errorPage404";
	}

	@RequestMapping(value = "/error403")
	public String denyAccess(ModelMap modelMap) throws PromisException, Exception {
		modelMap.addAttribute("ErrorString", "You are not permitted to use this function. Contact Administrator");
		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		ErrorPageRedirect(loginUser, modelMap);
		userDetailsService.getCurrentSession().getAttribute("ErrorUrl");
		return "errorPage";
	}

	@RequestMapping(value = "/error500")
	public String exsmaptin(ModelMap modelMap) throws PromisException, Exception {
		modelMap.addAttribute("ErrorString", "You are not permitted to use this function. Contact Administrator");
		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		ErrorPageRedirect(loginUser, modelMap);
		userDetailsService.getCurrentSession().getAttribute("ErrorUrl");
		return "errorPage";
	}

	@RequestMapping(value = "/errorPage")
	public String errorPage(ModelMap modelMap) throws PromisException, Exception {
		modelMap.addAttribute("ErrorString", userDetailsService.getCurrentSession().getAttribute("ErrorString"));
		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		ErrorPageRedirect(loginUser, modelMap);
		return "errorPage";
	}

	private String redirectToError(String msg) throws PromisException, Exception {
		AppUser loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());
		// userDetailsService.getCurrentSession().setAttribute("ErrorString",
		// utils.getMessage(msg));
		// userDetailsService.getCurrentSession().removeAttribute(NAME);
		ModelMap model = new ModelMap();
		model.addAttribute("ErrorString", utils.getMessage(msg));
		ErrorPageRedirect(loginUser, model);
		return "redirect:/errorPage";
	}

	public void ErrorPageRedirect(AppUser loginUser, ModelMap model) {
		String appRoleName = appRoleDao.getRoleName(loginUser.getUserId());
		if (appRoleName.contains("ROLE_ADMIN"))
			model.addAttribute("ErrorUrl", "home");
		else if (appRoleName.contains("ROLE_DIRECTOR"))
			model.addAttribute("ErrorUrl", "directorhome");
		else if (appRoleName.contains("ROLE_PROSECUTORUNITHEAD"))
			model.addAttribute("ErrorUrl", "puhHome");
		else if (appRoleName.contains("ROLE_OFFICER"))
			model.addAttribute("ErrorUrl", "userHome");
		else if (appRoleName.contains("ROLE_PUH_STAFF"))
			model.addAttribute("ErrorUrl", "puhStaffhome");
		else
			model.addAttribute("ErrorUrl", "login");
	}

	/*
	 * @RequestMapping("/captcha") public void captchaGen1(HttpServletRequest
	 * request, HttpServletResponse response) throws Exception {
	 * captchaBean.build(); CaptchaServletUtil.writeImage(response,
	 * captchaBean.getImage()); request.getSession().setAttribute(NAME,
	 * captchaBean); }
	 */

	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("image/jpg");
		int iTotalChars = 6;
		int iHeight = 40;
		int iWidth = 150;
		Font fntStyle1 = new Font("Arial", Font.BOLD, 30);
		Random randChars = new Random();
		String sImageCode = (Long.toString(Math.abs(randChars.nextLong()), 36)).substring(0, iTotalChars);
		BufferedImage biImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2dImage = (Graphics2D) biImage.getGraphics();
		int iCircle = 15;
		for (int i = 0; i < iCircle; i++) {
//			g2dImage.setColor(new Color(randChars.nextInt(255), randChars.nextInt(255), randChars.nextInt(255)));
			g2dImage.setColor(new Color(255, 255, 255));
		}
		g2dImage.setFont(fntStyle1);
		for (int i = 0; i < iTotalChars; i++) {
			g2dImage.setColor(new Color(255, 255, 255));
			if (i % 2 == 0) {
				g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 24);
			} else {
				g2dImage.drawString(sImageCode.substring(i, i + 1), 25 * i, 35);
			}
		}
		OutputStream osImage = response.getOutputStream();
		ImageIO.write(biImage, "jpeg", osImage);
		g2dImage.dispose();
		HttpSession session = request.getSession();
		session.setAttribute("captcha_security", sImageCode);
		osImage.close();
	}

	// dated on 27-05-2020
	@RequestMapping("/passHashing")
	public String passHash() throws Exception {
		return "passwordHashing";
	}

}
