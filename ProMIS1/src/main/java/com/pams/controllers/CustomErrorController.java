
package com.pams.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.pams.dao.AppRoleDAO;
import com.pams.dao.AppUserDAO;
import com.pams.entity.AppUser;
import com.pams.service.UserDetailsServiceImpl;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;


 
@Controller
public class CustomErrorController  implements ErrorController {
	  @Autowired
	    private AppUserDAO appUserDAO;
	  @Autowired
		private UserDetailsServiceImpl userDetailsService;

	  @Autowired
	    private AppRoleDAO appRoleDao;
	  @Value("${server.servlet.context-path}")
	    private String servletContextPath;
		
    @GetMapping("/error")
    public String handleError(HttpServletRequest request,ModelMap modelMap) {
        String errorPage = "errorPage"; // default
         
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Integer statusCode=0;
        if (status != null) {
             statusCode = Integer.valueOf(status.toString());
             
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                errorPage = "errorPage404";
                 
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 403 Forbidden error
                errorPage = "errorPage404";
                
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                errorPage = "errorPage404";
                 
            }
           
        }
        
        modelMap.addAttribute("ErrorString","");
		modelMap.addAttribute("ErrorUrl", "login");
		
		 String requestUrl=((HttpServletRequest) request).getHeader("Referer");
		 System.out.println("requestUrl======="+requestUrl);
		 String appBaseUrl=requestUrl!=null?requestUrl.substring(0, requestUrl.lastIndexOf("/")):"";
		 System.out.println("appUrl======="+appBaseUrl);
		
		AppUser loginUser =null;

		if ((userDetailsService.getLoginUserName() !=null))    		
		{	 loginUser = appUserDAO.findUserAccount(userDetailsService.getLoginUserName());}
		else
		{
			throw new InternalAuthenticationServiceException("UserId or Password is wrong. ");
		}
		
		if (statusCode != 429 && loginUser !=null )
		{
				
		ErrorPageRedirect(loginUser,modelMap);
		}
		else if (loginUser ==null)
		{
			modelMap.addAttribute("ErrorString","Site might be unavailable at this time or check given inputs . ");	
		}
		else			
		{
			modelMap.addAttribute("ErrorString","User has sent so many request in short Time. Pls try after 1 hour. ");	
		}
              return errorPage;
    }
    public void ErrorPageRedirect(AppUser loginUser,ModelMap model) 
	{
    	String appRoleName="";
    	if (!("".equals(appRoleDao.getRoleName(loginUser.getUserId()))))    		
    		appRoleName=appRoleDao.getRoleName(loginUser.getUserId());
    	if (appRoleName!=null)
    	{
		if (appRoleName.contains("ROLE_DIRECTOR")) 
			model.addAttribute("ErrorUrl", servletContextPath+"/directorhome");
		 else if (appRoleName.contains("ROLE_ADMIN")) 
			 model.addAttribute("ErrorUrl", servletContextPath+"/home");
		 else if (appRoleName.contains("ROLE_PROSECUTORUNITHEAD")) 
			 model.addAttribute("ErrorUrl", servletContextPath+"/puhHome");
		else if (appRoleName.contains("ROLE_OFFICER")) 
			model.addAttribute("ErrorUrl", servletContextPath+"/userHome");
		else if (appRoleName.contains("ROLE_PUH_STAFF")) 
			model.addAttribute("ErrorUrl", servletContextPath+"/puhStaffhome");
    	}
		else
			model.addAttribute("ErrorUrl",servletContextPath+ "/login");
	}
  
    public String getErrorPath() {
        return "/error";
    }
}