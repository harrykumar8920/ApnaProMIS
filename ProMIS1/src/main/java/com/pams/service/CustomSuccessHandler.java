package com.pams.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.pams.dao.AppUserDAO;
import com.pams.entity.AddDesignation;
import com.pams.entity.AppUser;
import com.pams.entity.UserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service("customSuccessHandler")
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	AddDesignationRepository addDesignationRepo;
	@Autowired
	private AppUserDAO loginDetailBo;
	// private static final Logger logger = Logger
	// .getLogger(CustomSuccessHandler.class);

	/**
	 * Method for handle the user request
	 * 
	 * @param request        - HttpServletRequest Object of user request
	 * @param response       - HttpServletResponse Object of user response
	 * @param authentication - Authentication object of authenticate user
	 */
	/** redirectStrategy object to redirect the user request based on url **/
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String targetUrl;
		try {
			targetUrl = determineTargetUrl(authentication, request, response);
			logger.info("target url is >>>>>>>>> " + targetUrl);
			redirectStrategy.sendRedirect(request, response, targetUrl);
			
			
			
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Method for find out the url
	 * 
	 * @param Authentication - object of to find out user authorities
	 * @return return authenticate user url
	 * @throws Exception
	 */
	protected String determineTargetUrl(Authentication authentication, HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		AppUser user = null;

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

		AddDesignation designation = null;
		String url = "";
		user = loginDetailBo.findUserAccount(authentication.getName());
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

		req.getSession().setAttribute("userfname", username);
		req.getSession().setAttribute("userName", user.getUserName());
		long designationId = loginDetailBo.findUserDesignation(userDetails.getId());
		if (designationId != 0) {
			designation = addDesignationRepo.findById(designationId).get();
		}

		if (designation != null) {
			req.getSession().setAttribute("userdesignation", designation.getDesignation());
		} else {
			req.getSession().setAttribute("userdesignation", "");
		}

		user = loginDetailBo.findUserAccount(authentication.getName());

		req.getSession().setAttribute("menu", user.getPassChanged());

		Date currentDate = new Date();
		Timestamp currentTimestamp = new Timestamp(currentDate.getTime());

		if (user.getPassChanged() == false || currentTimestamp.compareTo(user.getValidUpto()) >= 0) {
			url = "/NewUserChangedPass";
		} else {
			/*
			 * if (roles.contains("ROLE_DIRECTOR")) { url="/dirHome"; } else
			 */ if (roles.contains("ROLE_ADMIN")) {
				url = "/home";
			} else if (roles.contains("ROLE_OFFICER")) {
				 req.getSession().setAttribute("showFirstLoginPopup", true); 
				url = "/userHome";
			}
			/*
			 * else if (roles.contains("ROLE_NODAL_OFFICER")) { url="/nodalHome"; } else if
			 * (roles.contains("ROLE_APPROVAL")) { url="/aprovHome"; } else if
			 * (roles.contains("ROLE_SUPERVISOR")) { url="/superHome"; }
			 */
			else if (roles.contains("ROLE_PROSECUTORUNITHEAD")) {
				url = "/puhHome";
			} else if (roles.contains("ROLE_DIRECTOR")) {
				url = "/directorhome";
			}

			else if (roles.contains("ROLE_PUH_STAFF")) {
				url = "/puhStaffhome";
			}

			else
				url = "/error403";
		}
		return url;

	}

	/**
	 * Getter Method to get Redirect Strategy
	 * 
	 * @return Object of RedirectStratedy
	 */
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	/**
	 * Setter Method to set Redirect Strategy
	 * 
	 * @param redirectStrategy - Object of RedirectStratedy
	 */
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
	/*
	 * private boolean isSuperAdmin(List<String> roles) { return
	 * roles.contains(NCASConstant.ROLE_SUPERADMIN); }
	 */
	/*
	 * private boolean isAdmin(List<String> roles) { return
	 * roles.contains(NfraConstant.ROLE_ADMIN); }
	 * 
	 * private boolean isUser(List<String> roles) { return
	 * roles.contains(NfraConstant.ROLE_USER); }
	 */
}
