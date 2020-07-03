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
import com.covid.spchacker.dto.RegisterUser;
import com.covid.spchacker.dto.UserDetails;
import com.covid.spchacker.dto.UserLoginDto;
import com.covid.spchacker.dto.UserLoginResponse;
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
	public void main() {
		SpchackerApplication.main(new String[] {});
	}
	
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
		List<Patients> allPatients1 = getPatients();
		allPatients.addAll(allPatients1);
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
			covidService.getSingleStateData("bihar", "auth");

		}
	 
	 @Test(expected = Exception.class)
	 public void getAllStateDataWithoutloginTest() throws Exception {
			List<Patients> allPatients = getPatients();
			ApplicationCache.INSTANCE.userAuth.clear();
			Mockito.when(patientsRepository.getAllPatientsByState("bihar")).thenReturn(allPatients);
			covidService.getAllStateData("auth");
		}
	 
	 @Test
	 public void entityTest() throws Exception {
		 Patients pt = new Patients();
		 pt.setAdmitdate(new Date(100000));
		 pt.setAge(12);
		 pt.setContactno("1234567890");
		 pt.setFirstname("fn");
		 pt.setGender('M');
		 pt.setId(1L);
		 pt.setLastname("Ln");
		 pt.setReleasedate(new Date(100000));
		 pt.setState("BR");
		 pt.setStatus("NRML");
		 pt.setSymptoms("COLD");
		 pt.setSymptomsfrom(10);

		 assertNotNull(pt.getAdmitdate());
		 assertNotNull(pt.getAge());
		 assertNotNull(pt.getContactno());
		 assertNotNull(pt.getFirstname());
		 assertNotNull(pt.getGender());
		 assertNotNull(pt.getId());
		 assertNotNull(pt.getLastname());
		 assertNotNull(pt.getReleasedate());
		 assertNotNull(pt.getState());
		 assertNotNull(pt.getStatus());
		 assertNotNull(pt.getSymptoms());
		 assertNotNull(pt.getSymptomsfrom());
		 
		 User ur = new User();
		 ur.setAuth("auth");
		 ur.setContactno("1234567890");
		 ur.setEmail("a@a.com");
		 ur.setEnabled(true);
		 ur.setId(1L);
		 ur.setPassword("12345678");
		 ur.setRole("ADMIN");
		 ur.setUsername("UN");
		 
		 assertNotNull(ur.getAuth());
		 assertNotNull(ur.getContactno());
		 assertNotNull(ur.getEmail());
		 assertNotNull(ur.getId());
		 assertNotNull(ur.getPassword());
		 assertNotNull(ur.getRole());
		 assertNotNull(ur.getUsername());
		 assertNotNull(ur.isEnabled());


	 }
	 
	 
	 @Test
	 public void dtoTest() throws Exception {
		 RegisterUser pt = new RegisterUser();
		 pt.setAge(12);
		 pt.setContactNo("1234567");
		 pt.setEmail("a@a.com");
		 pt.setFirstname("fn");
		 pt.setFirstname("fn");
		 pt.setLastname("Ln");
		 pt.setPassword("qqqq");
		 pt.setRole("admin");
		 pt.setState("BR");
		 pt.setUsername("UN");
		 assertNotNull(pt.getAge());
		 assertNotNull(pt.getContactNo());
		 assertNotNull(pt.getEmail());
		 assertNotNull(pt.getFirstname());
		 assertNotNull(pt.getLastname());
		 assertNotNull(pt.getPassword());
		 assertNotNull(pt.getRole());
		 assertNotNull(pt.getState());
		 assertNotNull(pt.getUsername());
		 
		 UserDetails ur = new UserDetails();
		 ur.setAge(12);
		 ur.setContactNo("123456");
		 ur.setFirstname("FN");
		 ur.setState("BT");
		 ur.setLastname("LN");
		 assertNotNull(ur.getAge());
		 assertNotNull(ur.getContactNo());
		 assertNotNull(ur.getFirstname());
		 assertNotNull(ur.getLastname());
		 assertNotNull(ur.getState());
        
		 UserLoginDto dto = new UserLoginDto();
		 dto.setPassword("pwd");
		 dto.setUsername("UN");
		 assertNotNull(dto.getPassword());
		 assertNotNull(dto.getUsername());
		 
		 UserLoginResponse resp = new UserLoginResponse();
		 resp.setAuth("auth");
		 resp.setUsername("un");
		 
		 assertNotNull(resp.getAuth());
		 assertNotNull(resp.getUsername());
        
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
