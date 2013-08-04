package com.ainia.ecgApi.controller.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ainia.ecgApi.core.security.AuthUserImpl;
import com.ainia.ecgApi.core.security.AuthenticateService;
import com.ainia.ecgApi.core.utils.EncodeUtils;
import com.ainia.ecgApi.core.web.AjaxResult;
import com.ainia.ecgApi.domain.sys.Employee;
import com.ainia.ecgApi.domain.sys.User;
import com.ainia.ecgApi.service.sys.EmployeeService;
import com.ainia.ecgApi.service.sys.UserService;

/**
 * <p>login controller</p>
 * Copyright: Copyright (c) 2013
 * Company:   
 * LoginController.java
 * @author pq
 * @createdDate 2013-6-22
 * @version
 */
@Controller
@RequestMapping("api")
public class LoginController {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticateService authenticateService;

	/**
	 * <p>check the user login</p>
	 * void
	 */
	@RequestMapping(value = "auth" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String , String>> login(@RequestParam("username") String username , 
					  @RequestParam("password") String password) {
		Employee employee = employeeService.findByUsername(username);
		AjaxResult ajaxResult = new AjaxResult();
		Map<String , String> result = new HashMap<String , String>(1);
		if (employee == null) {
			ajaxResult.setStatus(HttpStatus.NOT_FOUND.value());
		}
		if (!authenticateService.checkPassword(employee.getPassword() , password , null)) {
			ajaxResult.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		else {
			authenticateService.setCurrentUser(new AuthUserImpl(employee.getId() , employee.getName() , employee.getUsername() , Employee.class.getSimpleName() , employee.getRolesArray()));
			employee.setSalt(EncodeUtils.encodeHex(EncodeUtils.asciiSum(employee.getPassword()).getBytes()));
			employee.setLastLoginDate(new Date());
			employeeService.update(employee);
			ajaxResult.setStatus(HttpStatus.OK.value());
			result.put(AjaxResult.AUTH_TOKEN , authenticateService.generateToken(username , Employee.class.getSimpleName() , employee.getSalt()));
		}
		return new ResponseEntity<Map<String , String>>(result, HttpStatus.valueOf(ajaxResult.getStatus()));
	}
	
	/**
	 * <p>check the user login</p>
	 * void
	 */
	@RequestMapping(value = "user/auth" , method = RequestMethod.POST , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<String , String>> userLogin(@RequestParam("username") String username , 
					  @RequestParam("password") String password) {
		User user = userService.findByUsername(username);
		AjaxResult ajaxResult = new AjaxResult();
		Map<String , String> result = new HashMap<String , String>(1);
		if (user == null) {
			ajaxResult.setStatus(HttpStatus.NOT_FOUND.value());
		}
		if (user != null) {
			if (!authenticateService.checkPassword(user.getPassword() , password , null)) {
				ajaxResult.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
			else {
				String month = String.valueOf(new DateTime().getMonthOfYear());
				authenticateService.setCurrentUser(new AuthUserImpl(user.getId() ,user.getName() , user.getUsername() , User.class.getSimpleName()));
				user.setSalt(EncodeUtils.encodeHex(EncodeUtils.asciiSum(user.getPassword()).getBytes()));
				user.setLastLoginDate(new Date());
				userService.update(user);
				ajaxResult.setStatus(HttpStatus.OK.value());
				result.put(AjaxResult.AUTH_TOKEN , authenticateService.generateToken(username , User.class.getSimpleName(), user.getSalt()));
				result.put(AjaxResult.AUTH_ID , user.getId().toString());
			}
		}
		
		return new ResponseEntity<Map<String , String>>(result, HttpStatus.valueOf(ajaxResult.getStatus()));
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setAuthenticateService(AuthenticateService authenticateService) {
		this.authenticateService = authenticateService;
	}
	
}
