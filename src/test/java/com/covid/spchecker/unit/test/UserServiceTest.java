package com.covid.spchecker.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.covid.spchacker.SpchackerApplication;
import com.covid.spchacker.cache.copy.RandomAuth;
import com.covid.spchacker.dto.RegisterUser;
import com.covid.spchacker.dto.UserLoginResponse;
import com.covid.spchacker.entity.User;
import com.covid.spchacker.entity.UserRepository;
import com.covid.spchacker.service.UserServices;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpchackerApplication.class})
public class UserServiceTest {
	
	@InjectMocks
	UserServices userService = new UserServices() ;
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	public void signUpUserTest() throws Exception {
		RegisterUser fE = cretate1();
		Mockito.when(userRepository.save(cretate())).thenReturn(cretate());
		Boolean dto =userService.signUpUser(fE);
		assertNotNull(dto);
		assertTrue(dto);
		
	}
	
	@Test
	public void loginLogoutTest() throws Exception {
		User fE = cretate();
		Mockito.when(userRepository.getUserByUsername(fE.getUsername())).thenReturn(cretate());
		UserLoginResponse resp = userService.loginUser(fE.getUsername(), "123456789");
		assertNotNull(resp);
		assertEquals(fE.getAuth(), resp.getAuth());
		assertEquals(fE.getUsername(), resp.getUsername());
		
		Boolean result = userService.logout(fE.getAuth());
		assertTrue(result);
		
	}
	
	
	private RegisterUser cretate1() {
		RegisterUser user = new RegisterUser();
		user.setContactNo("1234567890");
		user.setUsername("itest");
		user.setFirstname("Tests");
		user.setPassword("123456789");
		user.setEmail("a@gmail.com");
		user.setRole("Admin");
		return user;
	}
	
	private User cretate() {
		User user = new User();
		user.setContactno("1234567890");
		user.setUsername("itest");
		String pwd = RandomAuth.encode("123456789");
		user.setPassword(pwd);
		user.setEmail("a@gmail.com");
		user.setRole("Admin");
		user.setAuth("auth");
		return user;
	}

}
