package com.covid.spchacker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.covid.spchacker.cache.copy.ApplicationCache;
import com.covid.spchacker.cache.copy.RandomAuth;
import com.covid.spchacker.dto.RegisterUser;
import com.covid.spchacker.dto.UserLoginResponse;
import com.covid.spchacker.entity.User;
import com.covid.spchacker.repository.UserRepository;

@Service
public class UserServices {

	private static final Logger logger = LoggerFactory.getLogger(UserServices.class);
	
	@Autowired 
	UserRepository userRepository;

	public UserLoginResponse loginUser(String username , String password) throws Exception {
		User ur = userRepository.getUserByUsername(username);
		if(ur != null) { 
			if(RandomAuth.decode(ur.getPassword()).equals(password)) {
				UserLoginResponse resp = new UserLoginResponse();
				resp.setUsername(ur.getUsername());
				resp.setAuth(ur.getAuth());
				ApplicationCache.INSTANCE.userAuth.put(ur.getAuth(), ur);
				return resp;
			} else {
				throw new Exception("invalid password");
			}
		} else {
			throw new Exception("invalid user");
		}
	}

	public Boolean signUpUser(RegisterUser user) throws Exception {
		logger.info("Request recived in Service class for user signup with username {}" , user.getUsername());
		User saveuser = validateAndtransferRequest(user);
	    userRepository.save(saveuser);
	    logger.info("new user created in service class with   with username {}" , user.getUsername());
		return true;
	}

	private User validateAndtransferRequest(RegisterUser user) throws Exception {
		
		if(user.getUsername() == null) {
			throw new Exception("username is required");
		}
		if(user.getFirstname() == null) {
			throw new Exception("firstname is required");
		}
		
		if(user.getEmail() == null) {
			throw new Exception("email is required");
		}
		
		if(user.getPassword() == null || user.getPassword().length() < 8) {
			throw new Exception("password should be more then  7 char");
		}
		User ur = userRepository.getUserByUsername(user.getUsername());
		if(ur != null) {
		throw new Exception("User exist");
		}
		User ure = userRepository.getUserByEmail(user.getEmail());
		if(ure != null) {
			throw new Exception("Different User with same email is exist");
			}
		User saveuser = new User();
		saveuser.setAuth(RandomAuth.generateNewToken());
		saveuser.setContactno(user.getContactNo());
		saveuser.setEmail(user.getEmail());
		saveuser.setEnabled(true);
		saveuser.setPassword(RandomAuth.encode(user.getPassword()));
		saveuser.setRole(user.getRole());
		saveuser.setUsername(user.getUsername());
		return saveuser;
	}

	public boolean logout(String userName) {
		if(ApplicationCache.INSTANCE.userAuth.containsKey(userName)) {
		ApplicationCache.INSTANCE.userAuth.remove(userName);
		}  else {
			
			return false;
		}
		return true;
	}
	
	
}
