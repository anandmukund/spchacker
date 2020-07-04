package com.covid.spchacker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.covid.spchacker.dto.RegisterUser;
import com.covid.spchacker.dto.UserLoginDto;
import com.covid.spchacker.dto.UserLoginResponse;
import com.covid.spchacker.service.UserServices;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserServices userServices;

	@PostMapping("/signup")
	public ResponseEntity<Object> signup(@RequestBody RegisterUser user ) throws Exception{
		Boolean result = null;
		try {
			result =  userServices.signUpUser(user);
		} 
		catch(Exception ex) {
			if(ex.getLocalizedMessage().equals("username is required") || ex.getLocalizedMessage().equals("firstname is required")||
					ex.getLocalizedMessage().equals("email is required") || ex.getLocalizedMessage().equals("password should be more then  7 char")) {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.BAD_REQUEST);	
			} else if(ex.getLocalizedMessage().equals("User exist") || ex.getLocalizedMessage().equals("Different User with same email is exist")) {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.CONFLICT);
			} else {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(result,HttpStatus.CREATED);

	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody UserLoginDto user ) throws Exception{
		
		UserLoginResponse resp = null;
		try {
			resp = userServices.loginUser(user.getUsername(), user.getPassword());
		} catch(Exception ex) {
			if(ex.getLocalizedMessage().equals("invalid password") || ex.getLocalizedMessage().equals("invalid user")) {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.UNAUTHORIZED);
			} else {
				return new ResponseEntity<>(ex.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<>(resp,HttpStatus.OK);

	}
	
	@GetMapping("/logout")
	public  ResponseEntity<Object> logout(@RequestHeader("auth") String auth ){
		Boolean result = null;
		try {
		result=  userServices.logout(auth);
		} catch(Exception ex ) {
			return new ResponseEntity<>(ex.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		 return new ResponseEntity<>(result,HttpStatus.OK) ;

	}
}
