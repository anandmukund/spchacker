package com.covid.spchacker.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covid.spchacker.cache.copy.ApplicationCache;
import com.covid.spchacker.entity.Patients;
import com.covid.spchacker.entity.User;
import com.covid.spchacker.repository.PatientsRepository;
import com.covid.spchacker.repository.UserRepository;

@Service
public class CovidService {

	private static final Logger logger = LoggerFactory.getLogger(CovidService.class);

	@Autowired 
	UserRepository userRepository;

	@Autowired 
	PatientsRepository patientsRepository;

	public List<Patients> getSingleStateData(String statename,String auth) throws Exception{
		logger.info("Request recived for user getting covid data for state {}" , statename);
		checkLoginUser(auth);

		return patientsRepository.getAllPatientsByState(statename);
	}

	public Map<String ,List<Patients>> getAllStateData(String auth) throws Exception{
		logger.info("Request recived for user getting covid data for state {}" , "all srartes");
		checkLoginUser(auth);
		Map<String ,List<Patients>> result = new HashMap<String, List<Patients>>();
		Iterable<Patients> pt =  patientsRepository.findAll();
		Iterator<Patients> iter = pt.iterator();
		while(iter.hasNext()){
			Patients single = iter.next();
			if(result.containsKey(single.getState())) {
				List<Patients> ptl = result.get(single.getState());
				ptl.add(single);
				result.put(single.getState(), ptl);
			} else {
				List<Patients> ptl = new ArrayList<Patients>();
				ptl.add(single);
				result.put(single.getState(), ptl);
			}
		}
		return result;
	}

	public Iterable<Patients> savePatents(List<Patients> allPatients){
		logger.info("Request recived for user to save/update covid data  with size {}" , allPatients.size());
		Iterable<Patients> iterable = allPatients;
		return patientsRepository.saveAll(iterable);
	}

	public List<Patients> getStateDataByGender(String state,String auth,char gender) throws Exception{
		logger.info("Request recived for user to Get covid data  for state size {} with gender " , state , gender);
		checkLoginUser(auth);
		return patientsRepository.getAllPatientsByGenderAndState(gender, state);
	}

	public List<Patients> getDataByGender(String auth,char gender) throws Exception{
		logger.info("Request recived for user to Get covid data  for state size {} with gender " , "all state" , gender);
		
		checkLoginUser(auth);
		return patientsRepository.getAllPatientsByGender(gender);
	}

	public List<Patients> getDataByAdmitDate(Date startDate,Date endDate,String auth) throws Exception{
		checkLoginUser(auth);
		return patientsRepository.getAllPatientsAdmitBetweenDate(startDate, endDate);
	}

	public List<Patients> getDataByReleaseDate(Date startDate,Date endDate,String auth) throws Exception{
		checkLoginUser(auth);
		return patientsRepository.getAllPatientsReleaseBetweenDate(startDate, endDate);
	}
	
	public List<Patients> getDataByAdmitDateforState(Date startDate,Date endDate,String auth , String state) throws Exception{
		checkLoginUser(auth);
		return patientsRepository.getAllPatientsForstateAdmitBetweenDate(startDate, endDate, state);
	}

	public List<Patients> getDataByReleaseDateforState(Date startDate,Date endDate,String auth,String state) throws Exception{
		checkLoginUser(auth);
		return patientsRepository.getAllPatientsstateReleaseBetweenDate(startDate, endDate, state);
	}

	private void checkLoginUser(String auth) throws Exception {
		User ur = null;
		if(ApplicationCache.INSTANCE.userAuth.containsKey(auth)) {
			ur = ApplicationCache.INSTANCE.userAuth.get(auth);
		}
		if(ur == null) {
			throw new Exception("Invalid Request -- either invalid auth or user is logout");
		}
	}
}