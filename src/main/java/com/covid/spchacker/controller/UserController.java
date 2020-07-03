package com.covid.spchacker.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
	public boolean signup(@RequestBody RegisterUser user ) throws Exception{
		 return userServices.signUpUser(user);

	}

	@PostMapping("/login")
	public UserLoginResponse login(@RequestBody UserLoginDto user ) throws Exception{
		UserLoginResponse resp = userServices.loginUser(user.getUsername(), user.getPassword());
		 return resp;

	}
	
	@GetMapping("/logout")
	public boolean logout(@RequestHeader("auth") String auth ) throws Exception{
		 return userServices.logout(auth);

	}
}
