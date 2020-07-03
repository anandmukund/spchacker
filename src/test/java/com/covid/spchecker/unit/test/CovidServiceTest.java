package com.covid.spchecker.unit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.covid.spchacker.SpchackerApplication;
import com.covid.spchacker.cache.copy.ApplicationCache;
import com.covid.spchacker.cache.copy.RandomAuth;
import com.covid.spchacker.entity.Patients;
import com.covid.spchacker.entity.User;
import com.covid.spchacker.repository.PatientsRepository;
import com.covid.spchacker.service.CovidService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpchackerApplication.class})
public class CovidServiceTest {
	
	@InjectMocks
	CovidService covidService = new CovidService() ;
	
	@Mock
	PatientsRepository patientsRepository;
	
	@Test
	public void savePatentsTest() throws Exception {
		List<Patients> allPatients = getPatients();
		 Iterable<Patients> iterable = allPatients;
		 Mockito.when(patientsRepository.saveAll(iterable)).thenReturn(iterable);
		 Iterable<Patients> itrab = covidService.savePatents(allPatients);
		 assertNotNull(itrab);
	}
	
	
	@Test
	public void getAllStateDataTest() throws Exception {
		List<Patients> allPatients = getPatients();
		Iterable<Patients> iterable = allPatients;
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		//Mockito.when(userRepository.getUserByAuth("auth")).thenReturn(cretate());
		Mockito.when(patientsRepository.findAll()).thenReturn(iterable);
		Map<String ,List<Patients>> resp = covidService.getAllStateData("auth");
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getSingleStateDataTest() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsByState("bihar")).thenReturn(allPatients);
		List<Patients> resp = covidService.getSingleStateData("bihar", "auth");
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getSingleStateDataWithGenderTest() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsByGenderAndState('M', "bihar")).thenReturn(allPatients);
		List<Patients> resp = covidService.getStateDataByGender("bihar", "auth", 'M');
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getDataWithGenderTest() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsByGender('M')).thenReturn(allPatients);
		List<Patients> resp = covidService.getDataByGender("auth", 'M');
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getAllPatientAdmitBetweenDate() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsAdmitBetweenDate(new Date(1000000), new Date(1000000))).thenReturn(allPatients);
		List<Patients> resp = covidService.getDataByAdmitDate(new Date(1000000), new Date(1000000), "auth");
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getAllPatientStateAdmitBetweenDate() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsForstateAdmitBetweenDate(new Date(1000000), new Date(1000000), "bihar")).thenReturn(allPatients);
		List<Patients> resp = covidService.getDataByAdmitDateforState(new Date(1000000), new Date(1000000), "auth","bihar");
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getAllPatientreleaseBetweenDate() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsReleaseBetweenDate(new Date(1000000), new Date(1000000))).thenReturn(allPatients);
		List<Patients> resp = covidService.getDataByReleaseDate(new Date(1000000), new Date(1000000), "auth");
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	@Test
	public void getAllPatientStateReleaseBetweenDate() throws Exception {
		List<Patients> allPatients = getPatients();
		ApplicationCache.INSTANCE.userAuth.put("auth",cretate());
		Mockito.when(patientsRepository.getAllPatientsstateReleaseBetweenDate(new Date(1000000), new Date(1000000), "bihar")).thenReturn(allPatients);
		List<Patients> resp = covidService.getDataByReleaseDateforState(new Date(1000000), new Date(1000000), "auth","bihar");
		assertNotNull(resp);
		assertEquals(resp.size() , 1);

	}
	
	
	
	 @Test(expected = Exception.class)
	 public void getSingleStateDataWithoutloginTest() throws Exception {
			List<Patients> allPatients = getPatients();
			ApplicationCache.INSTANCE.userAuth.clear();
			Mockito.when(patientsRepository.getAllPatientsByState("bihar")).thenReturn(allPatients);
			List<Patients> resp = covidService.getSingleStateData("bihar", "auth");
			assertNotNull(resp);
			assertEquals(resp.size() , 1);

		}
	 
	 @Test(expected = Exception.class)
	 public void getAllStateDataWithoutloginTest() throws Exception {
			List<Patients> allPatients = getPatients();
			ApplicationCache.INSTANCE.userAuth.clear();
			Mockito.when(patientsRepository.getAllPatientsByState("bihar")).thenReturn(allPatients);
			covidService.getAllStateData("auth");

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

	
	private List<Patients> getPatients(){
		List<Patients> allPatients = new ArrayList<Patients>();
		Patients user = new Patients();
		user.setContactno("1234567890");
		user.setFirstname("Tests");
		user.setGender('M');
		user.setState("Bihar");
		user.setStatus("normal");
		allPatients.add(user);
		
		return allPatients;
	}
}
