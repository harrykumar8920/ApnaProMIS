package com.pams.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pams.dao.AppUserDAO;
import com.pams.entity.AppUser;
import com.pams.entity.PasswordRandomToken;
import com.pams.utils.Crypt;
import com.pams.utils.SaltGenerator;
import com.pams.utils.Utils;
import com.pams.validation.ProMISValidator;

import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{
	
	 
	 private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
		
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AppUserDAO appUserDAO;
	
	
	
	@Autowired
	private SaltGenerator saltGen;
	
    @Autowired
    private AuditBeanBo auditBeanBo;
	@Autowired
	private Utils utils;
	
	@Autowired
	PasswordRandomTokenRepository prtRepo;
	
 
	
	
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
    	
    	ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		
        String captchaTemp = attr.getRequest().getParameter("captcha");	       
 	       String captcha=Crypt.decrypt(captchaTemp, attr.getRequest().getParameter("key"));
 	      
 	       
 	//	String captcha = attr.getRequest().getParameter("captcha");
        
 	     ProMISValidator snmsVal = new ProMISValidator();
 		//captcha = snmsVal.getSafeString(captcha);
 		
 		//Commit the captach code start 
 		
 		  if ("".equals(captcha)) throw new
 		  InternalAuthenticationServiceException("Empty Captcha");
 		  
 		  if (!captcha.equals(attr.getRequest().getSession().getAttribute(
 		  "captcha_security"))) throw new
 		  InternalAuthenticationServiceException("Invalid Captcha");
 		 
    	
    
 		 System.out.println("abcd========================================");
 		String name = authentication.getName();
 		String password = authentication.getCredentials().toString();
 		UserDetails userDetails=null;
 		try {
 		
 			userDetails = userDetailsService.loadUserByUsername(name);
 		}catch (Exception e) {
 		System.out.println("exception Message" + e.getMessage());
 		}
 		if (userDetails== null)
 			throw new InternalAuthenticationServiceException("Invalid Username or Password");

 		AppUser user = null;
 			user = appUserDAO.findUserAccount(name);
 		if (user.getEnabled()== 0)	
 		throw new InternalAuthenticationServiceException("User is Deactivated. Please Contact Admin. ");
 	
 		Long loginUId = user.getUserId();
 		int loginUID = loginUId.intValue();
 		String rrr = appUserDAO.findUserDetails(user).getLastName();
		
 		
 	com.pams.entity.UserDetails userDetails1 = appUserDAO.findUserDetails(user);

 		String loginUName = userDetails1.getSalutation()+" "+userDetails1.getFirstName() != null ? userDetails1.getSalutation()+" "+userDetails1.getFirstName() + " " : "";
 		loginUName += userDetails1.getMiddleName() != null ? userDetails1.getMiddleName() + " " : "";
 		loginUName += userDetails1.getLastName() != null ? userDetails1.getLastName() : "";

 	//	System.out.println("encoded passord:   " + password);

 		/* String base64EncodedKey=Crypt.encodeKey("mustbe16byteskey"); */
 		String base64EncodedKey = attr.getRequest().getParameter("key");
 		String randomPageKey = attr.getRequest().getParameter("randomToken");
 		if (!randomPageKey.equals(attr.getRequest().getSession().getAttribute("random_Token"))) 
 		{
 			System.out.println("conditionfail");
 		}
 		String randomtoken = attr.getRequest().getSession().getAttribute("random_Token").toString();
 		PasswordRandomToken prt = new PasswordRandomToken();
 		List<PasswordRandomToken> prt1 = prtRepo.findAllByPasswordKeyAndRandomToken(password, base64EncodedKey);

 		if (!prt1.isEmpty()) {

 			prt.setPasswordKey(password);
 			prt.setRandomToken(base64EncodedKey);
 			prtRepo.save(prt);

 			try {
 				auditBeanBo.setAuditBean(loginUID, loginUName, utils.getMessage("log.login.app"), loginUID,
 						utils.getMessage("log.login.unauthpassword"), utils.getMessage("log.login.fail"), loginUName,
 						"true");
 				auditBeanBo.save();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}

 			return null;
 		} else {
 			prt.setPasswordKey(password);
 			prt.setRandomToken(base64EncodedKey);
 			prtRepo.save(prt);			

 			//String decrypt = Crypt.decrypt(password, base64EncodedKey);
 			String dbpassword = userDetails.getPassword().toLowerCase();
 			if(dbpassword.equalsIgnoreCase(null)) 
 				throw new InternalAuthenticationServiceException("Empty Database  Password");
 			String dbpassword_salt = dbpassword + randomtoken;

 			String saltedPassword = "";
 			try {

 				saltedPassword = SHAhashing.toHexString(SHAhashing.getSHA(dbpassword_salt)).toLowerCase();
 			} catch (NoSuchAlgorithmException e1) {
 				// TODO Auto-generated catch block
 				e1.printStackTrace();
 			}
 			
 			if(user.getPassChanged()==false && !saltedPassword.equalsIgnoreCase(password))
 		 		throw new InternalAuthenticationServiceException("New Password Policy has been implemented, please login using default password and change it. ");
 			
 			if(!saltedPassword.equals(password))
 	 	 		throw new InternalAuthenticationServiceException("Invalid Username or Password");
 			
        if (password.equals(saltedPassword) && user.getEnabled() == 1)
       
        {           
            try
            {
            	
            	   
            	
                auditBeanBo.setAuditBean(loginUID, loginUName,
                        utils.getConfigMessage("log.login.app"), loginUID,
                        utils.getConfigMessage("log.login.authuser"),
                        utils.getConfigMessage("log.login.success"), loginUName,
                        "true");
                		auditBeanBo.save();
                HttpSession hsession = attr.getRequest().getSession(false);
                SecurityContextHolder.clearContext();
                if (hsession != null)
                    hsession.invalidate();
            }
           /* catch (SnmsException e1)
            {
                //logger.error(e1.getMessage(),e1);
            	e1.printStackTrace();
            }*/
            catch (Exception e)
            {
            	logger.info(e.getMessage());
                //logger.error(e.getMessage(),e);
            }
            return new UsernamePasswordAuthenticationToken(name,
                    userDetails.getPassword(), userDetails.getAuthorities());
        }
        else
        {
            try
            {           	
                auditBeanBo.setAuditBean(loginUID, loginUName,
                        utils.getMessage("log.login.app"), loginUID,
                        utils.getMessage("log.login.unauthuser"),
                        utils.getMessage("log.login.fail"), loginUName, "true");
                auditBeanBo.save();
            }
           /* catch (SnmsException e)
            {
                //logger.error(e.getMessage(),e);
            }*/
            catch (Exception e)
            {
                logger.error(e.getMessage(),e);
            }
        }
 		}
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    
	
}
